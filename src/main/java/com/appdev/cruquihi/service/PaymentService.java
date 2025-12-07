package com.appdev.cruquihi.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdev.cruquihi.entity.PaymentEntity;
import com.appdev.cruquihi.entity.QrValidationEntity;
import com.appdev.cruquihi.entity.TicketEntity;
import com.appdev.cruquihi.entity.UserEntity;
import com.appdev.cruquihi.repository.PaymentRepository;
import com.appdev.cruquihi.repository.QrValidationRepository;
import com.appdev.cruquihi.repository.TicketRepository;
import com.appdev.cruquihi.repository.UserRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private QrValidationRepository qrRepo;

    public PaymentService() {
        super();
    }

    // 🟢 PURCHASE TICKET (CORRECTED)
    public Map<String, Object> purchase(int userId, int ticketId) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TicketEntity ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // 🛑 BLOCK MULTIPLE PURCHASES (1 per event only)
        boolean alreadyBought =
                paymentRepo.existsByUserUserIdAndTicketEventEventId(
                        userId,
                        ticket.getEvent().getEventId()
                );

        if (alreadyBought) {
            throw new RuntimeException("User already purchased a ticket for this event.");
        }

        double ticketPrice = ticket.getTicketPrice();
        double wallet = user.getWalletAmount();

        if (wallet < ticketPrice) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        // Deduct wallet
        double remaining = wallet - ticketPrice;
        user.setWalletAmount(remaining);
        userRepo.save(user);

        // Create payment record
        PaymentEntity payment = new PaymentEntity();
        payment.setPayment_method("wallet");
        payment.setPayment_amount(ticketPrice);
        payment.setPayment_timestamp(LocalDate.now());
        payment.setPayment_status("SUCCESS");
        payment.setReference_code(UUID.randomUUID().toString());
        payment.setTicket(ticket);
        payment.setUser(user);

        paymentRepo.save(payment);

        // ===============================
        // 🔥 UPDATE THIS TICKET AVAILABILITY
        // ===============================
        ticket.setAvailability(false);
        ticketRepo.save(ticket);

        // ===============================
        // 🔥 CREATE QR VALIDATION ENTRY
        // ===============================
        String qrValue = UUID.randomUUID().toString();
        String validatedBy = "System";
        String generatedAt = LocalDate.now().toString();
        String usedAt = "Not used yet";

        QrValidationEntity qr = new QrValidationEntity();
        qr.setGenerated_at(generatedAt);
        qr.setValidation_status("Valid");
        qr.setValidated_by(validatedBy);
        qr.setUsed_at(usedAt);
        qr.setPayment(payment);

        qrRepo.save(qr);

        // SEND EMAIL
        emailService.sendTicketEmail(
                user.getEmailAddress(),
                ticketPrice,
                remaining,
                qrValue,
                validatedBy,
                generatedAt,
                usedAt
        );

        // Response to frontend
        Map<String, Object> result = new HashMap<>();
        result.put("ticketPrice", ticketPrice);
        result.put("remainingWallet", remaining);

        return result;
    }

    // ============================
    // ORIGINAL METHODS
    // ============================

    public PaymentEntity postPaymentRecord(PaymentEntity paymentEntity) {
        return paymentRepo.save(paymentEntity);
    }

    public List<PaymentEntity> getAllPayments() {
        return paymentRepo.findAll();
    }

    public Optional<PaymentEntity> getPaymentById(Integer id) {
        return paymentRepo.findById(id);
    }

    public PaymentEntity updatePayment(Integer id, PaymentEntity newDetails) {
        Optional<PaymentEntity> optional = paymentRepo.findById(id);
        if (optional.isPresent()) {
            PaymentEntity existing = optional.get();
            existing.setPayment_method(newDetails.getPayment_method());
            existing.setPayment_amount(newDetails.getPayment_amount());
            existing.setPayment_timestamp(newDetails.getPayment_timestamp());
            existing.setPayment_status(newDetails.getPayment_status());
            existing.setReference_code(newDetails.getReference_code());
            return paymentRepo.save(existing);
        }
        return null;
    }

    public String deletePayment(Integer id) {
        if (paymentRepo.existsById(id)) {
            paymentRepo.deleteById(id);
            return "Payment deleted";
        } else {
            return "Payment not found";
        }
    }
}
