package com.appdev.cruquihi.cruzquirantehisolerg6.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appdev.cruquihi.cruzquirantehisolerg6.entity.PaymentEntity;
import com.appdev.cruquihi.cruzquirantehisolerg6.service.PaymentService;

import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping(method = RequestMethod.GET,path="/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/print")
    public String print() {
    return "This is your payment!";
    }

    @PostMapping("/postPaymentRecord")
    public PaymentEntity postPaymentRecord(@RequestBody PaymentEntity paymentEntity) {
        return paymentService.postPaymentRecord(paymentEntity);
    }

    @GetMapping("/getAllPayments")
    public List<PaymentEntity> getAllPayments(){
        return paymentService.getAllPayments();
    }
}
