package com.appdev.cruquihi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appdev.cruquihi.config.EventDeletedEvent;
import com.appdev.cruquihi.entity.EventEntity;
import com.appdev.cruquihi.entity.PaymentEntity;
import com.appdev.cruquihi.entity.TicketEntity;
import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.repository.EventRepository;
import com.appdev.cruquihi.repository.PaymentRepository;
import com.appdev.cruquihi.repository.QrValidationRepository;
import com.appdev.cruquihi.repository.TicketRepository;
import com.appdev.cruquihi.repository.UserRepository;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired UserRepository urepo;
    @Autowired EventRepository erepo;
    @Autowired TicketRepository trepo;
    @Autowired PaymentRepository prepo;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired QrValidationRepository qrRepo;

    @Autowired
    private PaymentService paymentService;

    public EventService() {}

    // ============================
    // CREATE EVENT
    // ============================
    public EventEntity createEvent(EventEntity event, int userId) {
        UserEntity user = urepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        event.setUser(user);
        event.setCreatedBy(user.getFullname());
        event.setCreatedAt(LocalDateTime.now());

        return erepo.save(event);
    }

    // ============================
    // GET ALL
    // ============================
    public List<EventEntity> getAllEvents() {
        return erepo.findAll();
    }

    // ============================
    // GET BY ID
    // ============================
    public Optional<EventEntity> getEventById(Integer id) {
        return erepo.findById(id);
    }

    // ============================
    // UPDATE (SAFE, NON-DESTRUCTIVE)
    // ============================
    public EventEntity updateEvent(Integer id, EventEntity update) {

        EventEntity event = erepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event with ID " + id + " not found"));

        if (update.getEventName() != null) event.setEventName(update.getEventName());
        if (update.getEventDescription() != null) event.setEventDescription(update.getEventDescription());
        if (update.getEventVenue() != null) event.setEventVenue(update.getEventVenue());
        if (update.getEventStartTime() != null) event.setEventStartTime(update.getEventStartTime());
        if (update.getEventEndTime() != null) event.setEventEndTime(update.getEventEndTime());
        if (update.getEventStatus() != null) event.setEventStatus(update.getEventStatus());
        if (update.getEventCategory() != null) event.setEventCategory(update.getEventCategory());
        if (update.getTicketLimit() != 0) event.setTicketLimit(update.getTicketLimit());

        // DO NOT overwrite createdAt
        return erepo.save(event);
    }

    // =============================================================
    // DELETE EVENT (Dual Mode: Cancel if purchased, Delete if none)
    // =============================================================

    @Transactional
    public String deleteEvent(Integer id) {

        Optional<EventEntity> opt = erepo.findById(id);
        if (!opt.isPresent()) return "Event with ID " + id + " not found.";

        EventEntity event = opt.get();

        // Load tickets
        List<TicketEntity> tickets = trepo.findAllByEvent_EventId(id);

        List<Integer> ticketIds = (tickets == null)
                ? Collections.emptyList()
                : tickets.stream().map(TicketEntity::getTicketId).collect(Collectors.toList());

        // Load payments for these tickets
        List<PaymentEntity> payments = ticketIds.isEmpty()
                ? Collections.emptyList()
                : prepo.findAllByTicket_TicketIdIn(ticketIds);

        // ============================================================
        // CASE 1: Payments exist → CANCEL EVENT (keep rows, refund)
        // ============================================================
        if (payments != null && !payments.isEmpty()) {

            event.setEventStatus("CANCELLED");
            erepo.save(event);

            if (!tickets.isEmpty()) {
                tickets.forEach(t -> t.setAvailability(false));
                trepo.saveAll(tickets);
            }

            List<Integer> refundFailed = new ArrayList<>();

            for (PaymentEntity p : payments) {
                try {
                    String curr = p.getPayment_status() == null ? "" : p.getPayment_status().toUpperCase();
                    if ("REFUNDED".equals(curr)) continue;

                    Integer userId = (p.getUser() == null) ? null : p.getUser().getUserId();

                    paymentService.requestInstantRefund(p.getId(), userId);

                } catch (Exception ex) {
                    log.error("Refund failed for payment {}: {}", p.getId(), ex.getMessage());
                    refundFailed.add(p.getId());
                }
            }

            // Delete QR validations
            List<Integer> paymentIds = payments.stream()
                    .map(PaymentEntity::getId)
                    .collect(Collectors.toList());

            if (!paymentIds.isEmpty()) qrRepo.deleteAllByPayment_IdIn(paymentIds);

            // Notify users
            List<String> recipients = new ArrayList<>();

            // event owner
            if (event.getUser() != null && event.getUser().getEmailAddress() != null)
                recipients.add(event.getUser().getEmailAddress());

            // buyers
            payments.stream()
                .map(p -> p.getUser() == null ? null : p.getUser().getEmailAddress())
                .filter(Objects::nonNull)
                .distinct()
                .forEach(recipients::add);

            publisher.publishEvent(
                new EventDeletedEvent(this, id, event.getEventName(), recipients)
            );

            String msg = "Event with ID " + id + " marked CANCELLED. ";
            msg += refundFailed.isEmpty()
                   ? "All payments refunded."
                   : "Refunds failed for payments: " + refundFailed;

            return msg;
        }

        // ============================================================
        // CASE 2: No payments → DELETE EVENT ENTIRELY
        // ============================================================

        // Optionally delete QR validations tied to tickets
        // (if you have such a relation)

        if (tickets != null && !tickets.isEmpty()) trepo.deleteAll(tickets);

        erepo.delete(event);

        // Notify organizer only
        if (event.getUser() != null && event.getUser().getEmailAddress() != null) {
            ArrayList<String> recipients = new ArrayList<>();
            recipients.add(event.getUser().getEmailAddress());
            publisher.publishEvent(
                new EventDeletedEvent(this, id, event.getEventName(), recipients)
            );
        }

        return "Event with ID " + id + " deleted (no tickets were purchased).";
    }

    // ============================
    // GET EVENTS BY USER
    // ============================
    public List<EventEntity> getEventsByUser(Integer userId) {
        return erepo.findAllByUserUserId(userId);
    }
}
