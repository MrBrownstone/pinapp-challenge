package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

import java.util.regex.Pattern;

public record EmailMessage(String from, String to, String subject, String body) implements Message {
    public static final ChannelKey CHANNEL = new ChannelKey("email");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public EmailMessage {
        if (from == null || from.isBlank() || !EMAIL_PATTERN.matcher(from).matches()) {
            throw new ValidationException("invalid from email");
        }
        if (to == null || to.isBlank() || !EMAIL_PATTERN.matcher(to).matches()) {
            throw new ValidationException("invalid to email");
        }
        if (body == null || body.isBlank()) {
            throw new ValidationException("body must not be blank");
        }
    }

    @Override public ChannelKey channel() { return CHANNEL; }
}
