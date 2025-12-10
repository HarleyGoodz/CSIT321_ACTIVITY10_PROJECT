package com.appdev.cruquihi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @ManyToOne
    @JoinColumn(name = "user_id") // FK
    private UserEntity user; // creator

    private String eventName;
    private String eventDescription;
    private String eventVenue;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
    private String eventStatus;
    private String eventCategory;
    private String createdBy;
    private LocalDateTime createdAt;
    private Integer ticketLimit;

    // Constructors
    public EventEntity() {
        super();
    }

    public EventEntity(int eventId, UserEntity user, String eventName, String eventDescription,
                   String eventVenue, LocalDateTime eventStartTime, LocalDateTime eventEndTime,
                   String eventStatus, String eventCategory, String createdBy,
                   LocalDateTime createdAt, int ticketLimit) {
    super();
    this.user = user;
    this.eventName = eventName;
    this.eventDescription = eventDescription;
    this.eventVenue = eventVenue;
    this.eventStartTime = eventStartTime;
    this.eventEndTime = eventEndTime;
    this.eventStatus = eventStatus;
    this.eventCategory = eventCategory;
    this.createdBy = createdBy;
    this.createdAt = createdAt;
    this.ticketLimit = ticketLimit;
}

    // Getters and Setters
    public int getEventId() { 
        return eventId; 
    }
    public void setEventId(int eventId) { 
        this.eventId = eventId; 
    }

    public UserEntity getUser() { 
        return user; 
    }
    public void setUser(UserEntity user) { 
        this.user = user; 
    }

    public String getEventName() { 
        return eventName; 
    }
    public void setEventName(String eventName) { 
        this.eventName = eventName; 
    }

    public String getEventDescription() { 
        return eventDescription; 
    }
    public void setEventDescription(String eventDescription) { 
        this.eventDescription = eventDescription; 
    }

    public String getEventVenue() { 
        return eventVenue; 
    }
    public void setEventVenue(String eventVenue) { 
        this.eventVenue = eventVenue; 
    }

    public LocalDateTime getEventStartTime() { 
        return eventStartTime; 
    }
    public void setEventStartTime(LocalDateTime eventStartTime) { 
        this.eventStartTime = eventStartTime; 
    }

    public LocalDateTime getEventEndTime() { 
        return eventEndTime; 
    }
    public void setEventEndTime(LocalDateTime eventEndTime) { 
        this.eventEndTime = eventEndTime; 
    }

    public String getEventStatus() { 
        return eventStatus; 
    }
    public void setEventStatus(String eventStatus) { 
        this.eventStatus = eventStatus; 
    }

    public String getEventCategory() { 
        return eventCategory; 
    }
    public void setEventCategory(String eventCategory) { 
        this.eventCategory = eventCategory; 
    }

    public String getCreatedBy() { 
        return createdBy; 
    }
    public void setCreatedBy(String createdBy) { 
        this.createdBy = createdBy; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public Integer getTicketLimit() { 
        return ticketLimit; 
    }
    public void setTicketLimit(Integer ticketLimit) { 
        this.ticketLimit = ticketLimit; 
    }
    
}