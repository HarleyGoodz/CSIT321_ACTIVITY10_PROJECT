package com.appdev.cruquihi.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.appdev.cruquihi.service.EmailService;

@Component
public class EventDeletedListener {

    @Autowired
    private EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventDeleted(EventDeletedEvent ev) {
        List<String> recipients = ev.getRecipientEmails();
        if (recipients == null || recipients.isEmpty()) return;

        String subject = "Event cancelled: " + ev.getEventName();
        String body = String.format(
            "Hello,\n\nThe event \"%s\" (ID: %d) has been deleted by the organizer.\n\n"
            + "If you bought a ticket, please contact support for refund details.\n\nRegards,\nEvent Team",
            ev.getEventName(), ev.getEventId()
        );

        for (String to : recipients) {
            if (to == null || to.isBlank()) continue;
            try {
                emailService.sendSimpleEmail(to, subject, body);
            } catch (Exception ex) {
                // Log and continue — do not rethrow so this doesn't cause other notifications to stop.
                System.err.println("Failed to send deletion email to " + to + ": " + ex.getMessage());
            }
        }
    }
}