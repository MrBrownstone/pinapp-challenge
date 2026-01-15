package com.pinapp.jnotifier.api;

public record PushTopic(String value) implements PushTarget {
    public PushTopic {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("push topic blank");
    }
    @Override public TargetType type() { return TargetType.TOPIC; }
}
