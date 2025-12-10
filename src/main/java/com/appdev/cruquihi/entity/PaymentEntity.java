package com.appdev.cruquihi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "payment")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String payment_method;
    private Double payment_amount;
    private LocalDate payment_timestamp;
    private String payment_status;

    @Column(name = "reference_code")
    private String reference_code;

    private String attendee_status = "NONE";

    // ================================
    // ONE-TO-ONE RELATIONSHIPS (FKs)
    // ================================

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticketId")
    private TicketEntity ticket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserEntity user;

    // ================================
    // Constructors
    // ================================

    public PaymentEntity() {
        super();
    }

    public PaymentEntity(int id, String payment_method, Double payment_amount, LocalDate payment_timestamp,
                         String payment_status, String reference_code) {
        this.id = id;
        this.payment_method = payment_method;
        this.payment_amount = payment_amount;
        this.payment_timestamp = payment_timestamp;
        this.payment_status = payment_status;
        this.reference_code = reference_code;
    }

    public PaymentEntity(int id, String payment_method, Double payment_amount, LocalDate payment_timestamp,
                         String payment_status, String reference_code,
                         TicketEntity ticket, UserEntity user) {
        this.id = id;
        this.payment_method = payment_method;
        this.payment_amount = payment_amount;
        this.payment_timestamp = payment_timestamp;
        this.payment_status = payment_status;
        this.reference_code = reference_code;
        this.ticket = ticket;
        this.user = user;
    }

    // ================================
    // Getters and Setters
    // (DO NOT MODIFY ORIGINAL NAMES)
    // ================================

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(Double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public LocalDate getPayment_timestamp() {
        return payment_timestamp;
    }

    public void setPayment_timestamp(LocalDate payment_timestamp) {
        this.payment_timestamp = payment_timestamp;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getReference_code() {
        return reference_code;
    }

    public void setReference_code(String reference_code) {
        this.reference_code = reference_code;
    }

    // FK Getters/Setters
    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAttendee_status() {
    return attendee_status;
}

public void setAttendee_status(String attendee_status) {
    this.attendee_status = attendee_status;
}
}
