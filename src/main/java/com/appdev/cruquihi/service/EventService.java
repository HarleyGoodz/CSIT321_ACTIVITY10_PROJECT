package com.appdev.cruquihi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appdev.cruquihi.entity.EventEntity;
import com.appdev.cruquihi.entity.PaymentEntity;
import com.appdev.cruquihi.entity.TicketEntity;
import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.repository.EventRepository;
import com.appdev.cruquihi.repository.PaymentRepository;
import com.appdev.cruquihi.repository.TicketRepository;
import com.appdev.cruquihi.repository.UserRepository;

@Service
public class EventService {

    @Autowired
    UserRepository urepo;

    @Autowired
    EventRepository erepo;

    @Autowired
    TicketRepository trepo;

    @Autowired
    PaymentRepository prepo;

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
     * Deletes the event and all associated tickets and payments.
     * Operation is transactional so either everything is removed or nothing is.
     */
    @Transactional
    public String deleteEvent(Integer id) {
        Optional<EventEntity> opt = erepo.findById(id);
        if (!opt.isPresent()) {
            return "Event with ID " + id + " not found.";
        }

        // 1) load tickets for the event
        List<TicketEntity> tickets = trepo.findAllByEvent_EventId(id);

        if (tickets != null && !tickets.isEmpty()) {
            // collect ticket ids
            List<Integer> ticketIds = tickets.stream()
                    .map(TicketEntity::getTicketId)
                    .collect(Collectors.toList());

            // 2) delete payments that reference those tickets (if any)
            List<PaymentEntity> payments = prepo.findAllByTicket_TicketIdIn(ticketIds);
            if (payments != null && !payments.isEmpty()) {
                prepo.deleteAll(payments);
            }

            // 3) delete the tickets
            trepo.deleteAll(tickets);
        }

        // 4) delete the event itself
        erepo.deleteById(id);

        return "Event with ID " + id + " and its related tickets/payments have been deleted.";
    }

    public List<EventEntity> getEventsByUser(Integer userId) {
        return erepo.findAllByUserUserId(userId);
    }
}
