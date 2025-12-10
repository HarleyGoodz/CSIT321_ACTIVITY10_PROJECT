package com.appdev.cruquihi.service;

import java.time.LocalDate;
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

    @Autowired
    UserRepository urepo;

    @Autowired
    EventRepository erepo;

    @Autowired
    TicketRepository trepo;

    @Autowired
    PaymentRepository prepo;

    @Autowired
    ApplicationEventPublisher publisher;
    
    @Autowired
    QrValidationRepository qrRepo;

    // IMPORTANT: PaymentService must provide a method that will perform the instant refund:
    // public String requestInstantRefund(Integer paymentId, Integer requestingUserId)
    @Autowired
    private PaymentService paymentService;

    public EventService() {
        super();
    }

    // CREATE EVENT
    public EventEntity createEvent(EventEntity event, int userId) {
        UserEntity user = urepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        event.setUser(user);
        event.setCreatedBy(user.getFullname());
        event.setCreatedAt(LocalDateTime.now());
        return erepo.save(event);
    }

    // GET ALL
    public List<EventEntity> getAllEvents() {
        return erepo.findAll();
    }

    // GET BY ID
    public Optional<EventEntity> getEventById(Integer id) {
        return erepo.findById(id);
    }

    // UPDATE
    public EventEntity updateEvent(Integer id, EventEntity update) {

        EventEntity event = erepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event with ID " + id + " not found"));

        event.setEventName(update.getEventName());
        event.setEventDescription(update.getEventDescription());
        event.setEventVenue(update.getEventVenue());
        event.setEventStartTime(update.getEventStartTime());
        event.setEventEndTime(update.getEventEndTime());
        event.setEventStatus(update.getEventStatus());
        event.setEventCategory(update.getEventCategory());
        event.setCreatedAt(update.getCreatedAt());
        event.setTicketLimit(update.getTicketLimit());

        return erepo.save(event);
    }

    /**
     * Deletes the event and attempts to instantly refund all associated payments (credits user wallet
     * and sets payment_status = REFUNDED). It deletes corresponding QR validations, payments, tickets,
     * and finally the event. Any refund failures are reported in the returned message.
     *
     * This is transactional: if any non-handled RuntimeException is thrown by this method the whole
     * operation will rollback. If you prefer refunds to commit independently, annotate
     * PaymentService.requestInstantRefund(...) with
     * @Transactional(propagation = Propagation.REQUIRES_NEW)
     */
    @Transactional
    public String deleteEvent(Integer id) {
        Optional<EventEntity> opt = erepo.findById(id);
        if (!opt.isPresent()) {
            return "Event with ID " + id + " not found.";
        }
        EventEntity event = opt.get();

        // Load tickets for the event
        List<TicketEntity> tickets = trepo.findAllByEvent_EventId(id);
        List<Integer> ticketIds = (tickets == null ? Collections.emptyList()
                : tickets.stream().map(TicketEntity::getTicketId).collect(Collectors.toList()));

        // If any payments exist for these tickets -> do CANCEL flow (keep rows)
        List<PaymentEntity> payments = ticketIds.isEmpty()
                ? Collections.emptyList()
                : prepo.findAllByTicket_TicketIdIn(ticketIds);

        if (payments != null && !payments.isEmpty()) {
            // *** CANCEL flow: mark event CANCELLED, mark tickets unavailable, attempt refunds, notify users ***
            event.setEventStatus("CANCELLED");
            erepo.save(event);

            if (tickets != null && !tickets.isEmpty()) {
                tickets.forEach(t -> {
                    t.setAvailability(false);
                    // t.setIsCancelled(true); // optional if you have such a field
                });
                trepo.saveAll(tickets);
            }

            List<Integer> refundFailed = new ArrayList<>();
            for (PaymentEntity p : payments) {
                try {
                    String curr = p.getPayment_status() == null ? "" : p.getPayment_status().toUpperCase();
                    if ("REFUNDED".equals(curr)) continue;
                    Integer userId = p.getUser() == null ? null : p.getUser().getUserId();
                    paymentService.requestInstantRefund(p.getId(), userId);
                } catch (Exception ex) {
                    System.err.println("Refund failed for payment id " + p.getId() + ": " + ex.getMessage());
                    refundFailed.add(p.getId());
                }
            }

            // cleanup QR validations referencing those payments (optional)
            List<Integer> paymentIds = payments.stream().map(PaymentEntity::getId).collect(Collectors.toList());
            if (!paymentIds.isEmpty()) {
                qrRepo.deleteAllByPayment_IdIn(paymentIds);
            }

            // notify users after commit
            List<String> recipients = new ArrayList<>();
            if (event.getUser() != null && event.getUser().getEmailAddress() != null) {
                recipients.add(event.getUser().getEmailAddress());
            }
            payments.stream()
                    .map(p -> p.getUser() == null ? null : p.getUser().getEmailAddress())
                    .filter(Objects::nonNull)
                    .distinct()
                    .forEach(recipients::add);

            publisher.publishEvent(new EventDeletedEvent(this, id, event.getEventName(), recipients));

            String msg = "Event with ID " + id + " marked CANCELLED. ";
            if (!refundFailed.isEmpty()) {
                msg += "Refunds failed for payment IDs: " + refundFailed.toString();
            } else {
                msg += "All payments refunded (or already refunded).";
            }
            return msg;
        } else {
            // *** DELETE flow: no payments found -> safe to delete tickets then event completely ***
            // delete any QR validations referencing (should be none if payments empty)
            if (!ticketIds.isEmpty()) {
                // If your QR repo keeps references to tickets, add deletion by ticket id here. Example:
                // qrRepo.deleteAllByTicket_TicketIdIn(ticketIds);
            }

            // delete tickets
            if (tickets != null && !tickets.isEmpty()) {
                trepo.deleteAll(tickets);
            }

            // finally delete the event row
            erepo.delete(event);

            // Optionally notify organizer only (no refunds since no purchases)
            List<String> recipients = new ArrayList<>();
            if (event.getUser() != null && event.getUser().getEmailAddress() != null) {
                recipients.add(event.getUser().getEmailAddress());
                publisher.publishEvent(new EventDeletedEvent(this, id, event.getEventName(), recipients));
            }

            return "Event with ID " + id + " deleted (no tickets were purchased).";
        }
    }



    public List<EventEntity> getEventsByUser(Integer userId) {
        return erepo.findAllByUserUserId(userId);
    }
}
