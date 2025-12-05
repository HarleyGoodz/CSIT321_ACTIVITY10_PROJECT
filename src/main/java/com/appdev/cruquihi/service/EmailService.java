package com.appdev.cruquihi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTicketEmail(
            String to,
            double ticketPrice,
            double remainingWallet,
            String qrCodeValue,
            String validatedBy,
            String generatedAt,
            String usedAt
    ) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Your Ticket Purchase Details");

        msg.setText(
            "Thank you for your purchase!\n\n" +
            "Ticket Price: ₱" + ticketPrice + "\n" +
            "Remaining Wallet Balance: ₱" + remainingWallet + "\n\n" +
            "QR Code Value: " + qrCodeValue + "\n" +
            "Validated By: " + validatedBy + "\n" +
            "Generated At: " + generatedAt + "\n" +
            "Used At: " + usedAt + "\n\n" +
            "Please keep this QR code for event entry.\n\n" +
            "This is an automated message."
        );

        mailSender.send(msg);
    }
}
