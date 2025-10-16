package com.appdev.cruquihi.cruzquirantehisolerg6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.cruzquirantehisolerg6.entity.PaymentEntity;
import com.appdev.cruquihi.cruzquirantehisolerg6.repository.PaymentRepository;


@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public PaymentService(){
        super();
        
    }

    public PaymentEntity postPaymentRecord(PaymentEntity paymentEntity) {
        return paymentRepository.save(paymentEntity);
    }

    public List< PaymentEntity > getAllPayments() {
        return paymentRepository.findAll();
    }

}

