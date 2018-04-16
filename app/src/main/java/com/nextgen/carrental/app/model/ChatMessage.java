package com.nextgen.carrental.app.model;

import java.util.Date;

/**
 * Holds each chat message
 */

public class ChatMessage {
    private String message;
    private User sender;
    private boolean selfMessage = false;
    private long createdAt;

    public ChatMessage() {
    }

    public ChatMessage(String message, User sender) {
        this.message = message;
        this.sender = sender;
        this.createdAt = new Date().getTime();
    }
    public ChatMessage(String message, User sender, long createdAt) {
        this.message = message;
        this.sender = sender;
        this.createdAt = createdAt;
    }
    public ChatMessage(String message, boolean isSelfMessage) {
        this.message = message;
        this.selfMessage = isSelfMessage;
        this.createdAt = new Date().getTime();
    }
    public ChatMessage(String message, boolean isSelfMessage, long createdAt) {
        this.message = message;
        this.selfMessage = isSelfMessage;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSelfMessage() {
        return selfMessage;
    }

    public void setSelfMessage(boolean selfMessage) {
        this.selfMessage = selfMessage;
    }
}
