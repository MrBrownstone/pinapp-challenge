package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

public record ChannelKey(String value) {
    public ChannelKey {
        if (value == null || value.isBlank()) {
            throw new ValidationException("channel key must not be blank");
        }
    }
}
