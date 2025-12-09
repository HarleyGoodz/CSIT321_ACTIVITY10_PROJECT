package com.appdev.cruquihi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // 🔥 Create QR Code image from text
    private byte[] generateQrImage(String qrText) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(
                qrText, BarcodeFormat.QR_CODE, 320, 320);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);

        return baos.toByteArray();
    }
    
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false); // false = not multipart
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false = plain text
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // Wrap in runtime to surface problems to logs, but listeners should catch exceptions
            throw new RuntimeException("Failed to send simple email: " + e.getMessage(), e);
        }
    }


    // 🔥 Send ticket email with QR image attached
    public void sendTicketEmail(
            String to,
            double ticketPrice,
            double remainingWallet,
            String qrCodeValue,
            String validatedBy,
            String generatedAt,
            String usedAt
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true); // true = multipart

            helper.setTo(to);
            helper.setSubject("Your Ticket Purchase Details");

            // Generate QR image
            byte[] qrBytes = generateQrImage(qrCodeValue);

            // Email body
            String body =
                    "Thank you for your purchase!\n\n" +
                    "Ticket Price: ₱" + ticketPrice + "\n" +
                    "Remaining Wallet Balance: ₱" + remainingWallet + "\n\n" +
                    "Validated By: " + validatedBy + "\n" +
                    "Generated At: " + generatedAt + "\n" +
                    "Used At: " + usedAt + "\n\n" +
                    "Please scan the attached QR code for ticket validation.\n\n" +
                    "This is an automated message.";

            helper.setText(body);

            // Attach QR image
            helper.addAttachment("ticket-qr.png",
                    new org.springframework.core.io.ByteArrayResource(qrBytes));

            // Send email
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
