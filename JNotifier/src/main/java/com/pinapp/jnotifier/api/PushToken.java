package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

public record PushToken(String value) implements PushTarget {
    public PushToken {
        if (value == null || value.isBlank()) throw new ValidationException("push token blank");
    }
    @Override public TargetType type() { return TargetType.TOKEN; }
}
