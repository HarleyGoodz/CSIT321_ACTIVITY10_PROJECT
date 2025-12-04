package com.appdev.cruquihi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class QrValidationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte[] qr_code;
    private String validation_status;
    private String validated_by;
    private String generated_at;
    private String used_at;

    // ====== 🔥 ADD THIS ONLY — Payment FK (One-to-One) ======
    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private PaymentEntity payment;
    // =========================================================

    public QrValidationEntity(){
        super();
    }

    public QrValidationEntity(int id, byte[] qr_code, String validation_status,
                              String validated_by, String generated_at, String used_at) {
        this.id = id;
        this.qr_code = qr_code;
        this.validation_status = validation_status;
        this.validated_by = validated_by;
        this.generated_at = generated_at;
        this.used_at = used_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getQr_code() {
        return qr_code;
    }
    public void setQr_code(byte[] qr_code) {
        this.qr_code = qr_code;
    }

    public String getValidation_status() {
        return validation_status;
    }
    public void setValidation_status(String validation_status) {
        this.validation_status = validation_status;
    }

    public String getValidated_by() {
        return validated_by;
    }
    public void setValidated_by(String validated_by) {
        this.validated_by = validated_by;
    }

    public String getGenerated_at() {
        return generated_at;
    }
    public void setGenerated_at(String generated_at) {
        this.generated_at = generated_at;
    }

    public String getUsed_at() {
        return used_at;
    }
    public void setUsed_at(String used_at) {
        this.used_at = used_at;
    }

    // ====== 🔥 ADD GETTER + SETTER FOR PAYMENT ======
    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
    // =================================================
}
