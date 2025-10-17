package com.appdev.cruquihi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkin")
public class CheckinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkinId;

    @ManyToOne
    @JoinColumn(name = "user_id") // FK -> user
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id") // FK -> event
    private EventEntity event;

    private LocalDateTime checkinDate;
    private String checkinStatus;

    public CheckinEntity() {
        super();
    }
    
    

    public CheckinEntity(int checkinId, UserEntity user, EventEntity event, LocalDateTime checkinDate,
            String checkinStatus) {
        this.checkinId = checkinId;
        this.user = user;
        this.event = event;
        this.checkinDate = checkinDate;
        this.checkinStatus = checkinStatus;
    }



    public int getCheckinId() {
        return checkinId;
    }
    public void setCheckinId(int checkinId) {
        this.checkinId = checkinId;
    }

    public UserEntity getUser() {
        return user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }

    public EventEntity getEvent() {
        return event;
    }
    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public LocalDateTime getCheckinDate() {
        return checkinDate;
    }
    public void setCheckinDate(LocalDateTime checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckinStatus() {
        return checkinStatus;
    }
    public void setCheckinStatus(String checkinStatus) {
        this.checkinStatus = checkinStatus;
    }
}
