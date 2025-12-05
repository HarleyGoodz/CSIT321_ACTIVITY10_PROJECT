package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.PaymentEntity;
import com.appdev.cruquihi.service.PaymentService;

@RestController
@RequestMapping(path = "/api/payment")   // ❗ FIXED: removed method=GET
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    // 🔵 TEST ROUTE
    @GetMapping("/print")
    public String print() {
        return "This is your payment!";
    }

    // 🟢 CREATE payment
    @PostMapping("/add")
    public PaymentEntity addPayment(@RequestBody PaymentEntity paymentEntity) {
        return paymentService.postPaymentRecord(paymentEntity);
    }

    // 🟢 PURCHASE TICKET (returns ticketPrice + remainingWallet)
    @PostMapping("/purchase")
    public Map<String, Object> purchase(
            @RequestParam int userId,
            @RequestParam int ticketId) {
        return paymentService.purchase(userId, ticketId);
    }

    // 🟡 READ all
    @GetMapping("/get-all")
    public List<PaymentEntity> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // 🟡 READ by ID
    @GetMapping("/get/{id}")
    public Optional<PaymentEntity> getPaymentById(@PathVariable Integer id) {
        return paymentService.getPaymentById(id);
    }

    // 🟠 UPDATE by ID
    @PutMapping("/update/{id}")
    public PaymentEntity updatePayment(
            @PathVariable Integer id,
            @RequestBody PaymentEntity newDetails) {
        return paymentService.updatePayment(id, newDetails);
    }

    // 🔴 DELETE by ID
    @DeleteMapping("/delete/{id}")
    public String deletePayment(@PathVariable Integer id) {
        return paymentService.deletePayment(id);
    }
}
