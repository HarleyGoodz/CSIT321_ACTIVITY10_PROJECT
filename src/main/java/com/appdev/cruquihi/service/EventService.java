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

        // 1) mark event cancelled (keep the DB row so tickets/payments can still reference it)
        event.setEventStatus("CANCELLED");
        erepo.save(event);

        // 2) load tickets for the event and mark them unavailable (so they can't be bought again)
        List<TicketEntity> tickets = trepo.findAllByEvent_EventId(id);
        List<Integer> ticketIds = Collections.emptyList();
        if (tickets != null && !tickets.isEmpty()) {
            ticketIds = tickets.stream()
                            .map(TicketEntity::getTicketId)
                            .collect(Collectors.toList());
            tickets.forEach(t -> {
                t.setAvailability(false);
                // optional: if you have a 'cancelled' flag on TicketEntity, set it here
                // t.setIsCancelled(true);
            });
            trepo.saveAll(tickets);
        }

        // 3) load payments for those tickets
        List<PaymentEntity> payments = ticketIds.isEmpty()
                ? Collections.emptyList()
                : prepo.findAllByTicket_TicketIdIn(ticketIds);

        // 4) attempt instant refunds for each payment (credits wallet + sets REFUNDED)
        //    collect failures but do not abort the whole operation
        List<Integer> refundFailed = new ArrayList<>();
        if (payments != null && !payments.isEmpty()) {
            for (PaymentEntity p : payments) {
                try {
                    String curr = p.getPayment_status() == null ? "" : p.getPayment_status().toUpperCase();
                    if ("REFUNDED".equals(curr)) continue;

                    Integer userId = p.getUser() == null ? null : p.getUser().getUserId();

                    // call your PaymentService which credits wallet and updates payment row
                    paymentService.requestInstantRefund(p.getId(), userId);

                    // after this call the payment row should be updated to REFUNDED
                    // and the user's wallet credited.
                } catch (Exception ex) {
                    System.err.println("Refund failed for payment id " + p.getId() + ": " + ex.getMessage());
                    refundFailed.add(p.getId());
                }
            }
        }

        // 5) delete QR validations referencing those payments (optional cleanup)
        List<Integer> paymentIds = Collections.emptyList();
        if (payments != null && !payments.isEmpty()) {
            paymentIds = payments.stream().map(PaymentEntity::getId).collect(Collectors.toList());
        }
        if (paymentIds != null && !paymentIds.isEmpty()) {
            qrRepo.deleteAllByPayment_IdIn(paymentIds);
        }

        // IMPORTANT: DO NOT delete payments or tickets here if you want the My Tickets UI
        // to keep showing the cards (with status REFUNDED). Keeping rows makes it easy
        // for the frontend to display refunded payments and their event/ticket info.

        // 6) publish EventDeletedEvent to notify users (listener sends emails AFTER_COMMIT)
        List<String> recipients = new ArrayList<>();
        if (event.getUser() != null && event.getUser().getEmailAddress() != null) {
            recipients.add(event.getUser().getEmailAddress());
        }
        if (payments != null && !payments.isEmpty()) {
            payments.stream()
                .map(p -> p.getUser() == null ? null : p.getUser().getEmailAddress())
                .filter(Objects::nonNull)
                .distinct()
                .forEach(recipients::add);
        }
        publisher.publishEvent(new EventDeletedEvent(this, id, event.getEventName(), recipients));

        // 7) return summary
        String msg = "Event with ID " + id + " marked CANCELLED. ";
        if (!refundFailed.isEmpty()) {
            msg += "Refunds failed for payment IDs: " + refundFailed.toString();
        } else {
            msg += "All payments refunded (or already refunded).";
        }
        return msg;
    }

    public List<EventEntity> getEventsByUser(Integer userId) {
        return erepo.findAllByUserUserId(userId);
    }
}
