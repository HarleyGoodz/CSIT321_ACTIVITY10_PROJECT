package com.appdev.cruquihi.config;

import java.util.List;
import org.springframework.context.ApplicationEvent;

public class EventDeletedEvent extends ApplicationEvent {
    private final int eventId;
    private final String eventName;
    private final List<String> recipientEmails;

    public EventDeletedEvent(Object source, int eventId, String eventName, List<String> recipientEmails) {
        super(source);
        this.eventId = eventId;
        this.eventName = eventName;
        this.recipientEmails = recipientEmails;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public List<String> getRecipientEmails() {
        return recipientEmails;
    }
}