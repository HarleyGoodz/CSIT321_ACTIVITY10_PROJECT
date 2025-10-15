package com.appdev.cruquihi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.EventEntity;
import com.appdev.cruquihi.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    EventRepository erepo;

    public EventService() {
        super();
    }

    // CREATE
    public EventEntity createEvent(EventEntity event) {
        return erepo.save(event);
    }

    // READ ALL
    public List<EventEntity> getAllEvents() {
        return erepo.findAll();
    }

    // READ BY ID
    public Optional<EventEntity> getEventById(Integer id) {
        return erepo.findById(id);
    }

    // UPDATE
    public EventEntity updateEvent(Integer id, EventEntity newEventDetails) {
        EventEntity event = new EventEntity();
        try {
            event = erepo.findById(id).get();
            event.setEventName(newEventDetails.getEventName());
            event.setEventDescription(newEventDetails.getEventDescription());
            event.setEventVenue(newEventDetails.getEventVenue());
            event.setEventStartTime(newEventDetails.getEventStartTime());
            event.setEventEndTime(newEventDetails.getEventEndTime());
            event.setEventStatus(newEventDetails.getEventStatus());
            event.setEventCategory(newEventDetails.getEventCategory());
            event.setCreatedBy(newEventDetails.getCreatedBy());
            event.setCreatedAt(newEventDetails.getCreatedAt());
            event.setTicketLimit(newEventDetails.getTicketLimit());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Event with ID " + id + " not found.");
        } finally {
            return erepo.save(event);
        }
    }

    // DELETE
    public String deleteEvent(Integer id) {
        String msg = "";

        if (erepo.findById(id).isPresent()) {
            erepo.deleteById(id);
            msg = "Event with ID " + id + " has been deleted.";
        } else {
            msg = "Event with ID " + id + " not found.";
        }

        return msg;
    }
}
