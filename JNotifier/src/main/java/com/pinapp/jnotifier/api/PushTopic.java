package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

public record PushTopic(String value) implements PushTarget {
    public PushTopic {
        if (value == null || value.isBlank()) throw new ValidationException("push topic blank");
    }
    @Override public TargetType type() { return TargetType.TOPIC; }
}
