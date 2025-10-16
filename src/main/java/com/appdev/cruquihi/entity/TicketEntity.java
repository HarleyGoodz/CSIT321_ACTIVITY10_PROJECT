package com.appdev.cruquihi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    @ManyToOne
    @JoinColumn(name = "event_id") // FK -> event
    private EventEntity event;

    private BigDecimal ticketPrice;
    private String ticketType;
    private Boolean availability;

    public TicketEntity() {
        super();
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

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }
    public void setTicketPrice(BigDecimal ticketPrice) {
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
