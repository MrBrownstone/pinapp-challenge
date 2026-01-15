package com.pinapp.jnotifier.api;

public record ChannelKey(String value) {
    public ChannelKey {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("channel key must not be blank");
        }
    }
}
