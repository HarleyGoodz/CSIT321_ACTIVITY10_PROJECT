package com.appdev.cruquihi.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String payment_method;
    private Double payment_amount;
    private float payment_timestamp;
    private String payment_status;
    private String reference_code;

    public PaymentEntity(){
        super();
    }

    public PaymentEntity(int id, String payment_method, Double payment_amount, float payment_timestamp,
     String payment_status, String reference_code) {
        this.id = id;
        this.payment_method = payment_method;
        this.payment_amount = payment_amount;
        this.payment_timestamp = payment_timestamp;
        this.payment_status = payment_status;
        this.reference_code = reference_code;
    }   

    public void setId(int id) {
        this.id = id;
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

    public float getPayment_timestamp() {
        return payment_timestamp;
    }

    public void setPayment_timestamp(float payment_timestamp) {
        this.payment_timestamp = payment_timestamp;
    }   

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getRefernence_code() {
        return reference_code;
    }

    public void setRefernence_code(String refernence_code) {
        this.reference_code = refernence_code;
    }
}