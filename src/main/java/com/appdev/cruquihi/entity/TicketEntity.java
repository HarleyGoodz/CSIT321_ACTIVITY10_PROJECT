package com.appdev.cruquihi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    @ManyToOne
    @JoinColumn(name = "event_id") // FK -> event
    private EventEntity event;

    private double ticketPrice;
    private String ticketType;
    private Boolean availability;

    public TicketEntity() {
        super();
    }


    public TicketEntity(int ticketId, EventEntity event, double ticketPrice, String ticketType,
            Boolean availability) {
        this.ticketId = ticketId;
        this.event = event;
        this.ticketPrice = ticketPrice;
        this.ticketType = ticketType;
        this.availability = availability;
    }

    public int getTicketId() {
        return ticketId;
    }
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public EventEntity getEvent() {
        return event;
    }
    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketType() {
        return ticketType;
    }
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Boolean getAvailability() {
        return availability;
    }
    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}
