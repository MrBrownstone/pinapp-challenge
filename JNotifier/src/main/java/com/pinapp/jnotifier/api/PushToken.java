package com.pinapp.jnotifier.api;

public record PushToken(String value) implements PushTarget {
    public PushToken {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("push token blank");
    }
    @Override public TargetType type() { return TargetType.TOKEN; }
}
