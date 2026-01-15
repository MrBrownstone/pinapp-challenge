package com.pinapp.jnotifier.api;

import java.util.Optional;

public record SendResult(
        Status status,
        Optional<String> providerMessageId,
        Optional<String> details
) {
    public enum Status { ACCEPTED, SENT, REJECTED, FAILED }

    public static SendResult accepted(String messageId) {
        return new SendResult(Status.ACCEPTED, Optional.ofNullable(messageId), Optional.empty());
    }

    public static SendResult failed(String details) {
        return new SendResult(Status.FAILED, Optional.empty(), Optional.ofNullable(details));
    }
}
