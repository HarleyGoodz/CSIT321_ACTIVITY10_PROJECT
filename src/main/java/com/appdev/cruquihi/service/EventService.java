package com.appdev.cruquihi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.EventEntity;
import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.repository.EventRepository;
import com.appdev.cruquihi.repository.UserRepository;

@Service
public class EventService {

    @Autowired
    UserRepository urepo;

    @Autowired
    EventRepository erepo;

    public EventService() {
        super();
    }

    // CREATE EVENT
    public EventEntity createEvent(EventEntity event, int userId) {
        UserEntity user = urepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        event.setUser(user);                      
        event.setCreatedBy(user.getFullname());   // automatically set createdBy to user's fullname
        
        event.setCreatedAt(LocalDateTime.now()); // auto fill in time 
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

    // DELETE
    public String deleteEvent(Integer id) {
        if (erepo.findById(id).isPresent()) {
            erepo.deleteById(id);
            return "Event with ID " + id + " has been deleted.";
        } else {
            return "Event with ID " + id + " not found.";
        }
    }

    public List<EventEntity> getEventsByUser(Integer userId) {
    return erepo.findAllByUserUserId(userId); // or implement repository query
}

}
