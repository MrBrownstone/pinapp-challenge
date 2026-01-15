package com.pinapp.jnotifier.api;

public record PushCondition(String value) implements PushTarget {
    public PushCondition {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("push condition blank");
    }
    @Override public TargetType type() { return TargetType.CONDITION; }
}
