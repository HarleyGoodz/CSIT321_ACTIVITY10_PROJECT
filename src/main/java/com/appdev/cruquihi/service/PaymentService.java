package com.appdev.cruquihi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.PaymentEntity;
import com.appdev.cruquihi.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public PaymentService() {
        super();
    }

    // CREATE
    public PaymentEntity postPaymentRecord(PaymentEntity paymentEntity) {
        return paymentRepository.save(paymentEntity);
    }

    // READ ALL
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    // READ BY ID
    public Optional<PaymentEntity> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    // UPDATE
    public PaymentEntity updatePayment(Integer id, PaymentEntity newPaymentDetails) {
        try {
            PaymentEntity payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Payment with ID " + id + " not found."));

            payment.setPayment_method(newPaymentDetails.getPayment_method());
            payment.setPayment_amount(newPaymentDetails.getPayment_amount());
            payment.setPayment_timestamp(newPaymentDetails.getPayment_timestamp());
            payment.setPayment_status(newPaymentDetails.getPayment_status());
            payment.setRefernence_code(newPaymentDetails.getRefernence_code());

            return paymentRepository.save(payment);

        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    // DELETE
    public String deletePayment(Integer id) {
        if (paymentRepository.findById(id).isPresent()) {
            paymentRepository.deleteById(id);
            return "Payment with ID " + id + " has been deleted.";
        }
        return "Payment with ID " + id + " not found.";
    }
}