package com.nextgen.carrental.app.model;

import java.util.Date;

/**
 * Holds each chat message
 */

public class ChatMessage {
    public static final int VIEW_TYPE_MESSAGE_SENT = 0;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;

    private String message;
    private String sender;
    private boolean selfMessage = false;
    private long createdAt;

    public ChatMessage() {
        createdAt = new Date().getTime();
    }

    public ChatMessage(String message, String sender) {
        this();
        this.message = message;
        this.sender = sender;
    }
    public ChatMessage(String message, boolean isSelfMessage) {
        this();
        this.message = message;
        this.selfMessage = isSelfMessage;
    }

    public int getType() {
        return selfMessage
                ? VIEW_TYPE_MESSAGE_SENT
                : VIEW_TYPE_MESSAGE_RECEIVED;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isSelfMessage() {
        return selfMessage;
    }

}
