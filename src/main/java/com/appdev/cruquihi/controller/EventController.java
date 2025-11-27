package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.EventEntity;
import com.appdev.cruquihi.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eserv;

    // CREATE EVENT
    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody Map<String, Object> body) {
        try {
            int userId = (int) body.get("userId");
            body.remove("userId");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            EventEntity event = mapper.convertValue(body, EventEntity.class);

            EventEntity saved = eserv.createEvent(event, userId);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // GET ALL EVENTS
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        List<EventEntity> events = eserv.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // GET EVENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Integer id) {
        Optional<EventEntity> event = eserv.getEventById(id);

        if (event.isPresent()) {
            return ResponseEntity.ok(event.get());
        } else {
            return ResponseEntity.status(404).body("Event not found");
        }
    }

    // UPDATE EVENT
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Integer id, @RequestBody EventEntity updatedEvent) {
        try {
            EventEntity event = eserv.updateEvent(id, updatedEvent);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // DELETE EVENT
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Integer id) {
        String msg = eserv.deleteEvent(id);
        return ResponseEntity.ok(msg);
    }
}
