package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

public record PushCondition(String value) implements PushTarget {
    public PushCondition {
        if (value == null || value.isBlank()) throw new ValidationException("push condition blank");
    }
    @Override public TargetType type() { return TargetType.CONDITION; }
}
