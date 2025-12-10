package com.appdev.cruquihi.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appdev.cruquihi.entity.PaymentEntity;
import com.appdev.cruquihi.service.PaymentService;

import jakarta.servlet.http.HttpSession;

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

    @PostMapping("/refund/{id}")
    public ResponseEntity<?> requestInstantRefund(@PathVariable Integer id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId"); // adjust to your auth system
        if (userId == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        try {
            String msg = paymentService.requestInstantRefund(id, userId);
            return ResponseEntity.ok(msg);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            // authorization or business rule failure
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to process refund: " + e.getMessage());
        }
    }

    @PostMapping("/attendee/approve/{paymentId}")
    public ResponseEntity<?> approveAttendee(@PathVariable int paymentId) {
        paymentService.updateAttendeeStatus(paymentId, "APPROVED");
        return ResponseEntity.ok("Attendee approved");
    }

    @PostMapping("/attendee/decline/{paymentId}")
    public ResponseEntity<?> declineAttendee(@PathVariable int paymentId) {
        paymentService.updateAttendeeStatus(paymentId, "DECLINED");
        return ResponseEntity.ok("Attendee declined");
    }

    @GetMapping("/get-by-event/{eventId}")
public ResponseEntity<?> getByEvent(@PathVariable int eventId) {
    try {
        List<PaymentEntity> attendees = paymentService.getPaymentsByEvent(eventId);
        return ResponseEntity.ok(attendees);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Failed to load attendees: " + e.getMessage());
    }
}

    
}
