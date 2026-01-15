package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

import java.util.Optional;

public record SmsMessage(
        String from,                    // optional if using messagingServiceSid
        String messagingServiceSid,      // optional if using from
        String to,
        String body,
        String statusCallbackUrl         // optional
) implements Message {

    public static final ChannelKey CHANNEL = new ChannelKey("sms");

    public SmsMessage {
        if (to == null || to.isBlank()) throw new ValidationException("to blank");
        if (body == null || body.isBlank()) throw new ValidationException("body blank");

        boolean hasFrom = from != null && !from.isBlank();
        boolean hasService = messagingServiceSid != null && !messagingServiceSid.isBlank();

        if (hasFrom == hasService) {
            // either exactly one is set, not both, not none
            throw new ValidationException("Provide exactly one of: from OR messagingServiceSid");
        }

        // Minimal sanity check (don’t overdo it now)
        if (!to.startsWith("+")) throw new ValidationException("to should look like E.164 (start with '+')");
        if (hasFrom && !from.startsWith("+")) throw new ValidationException("from should look like E.164 (start with '+')");
    }

    @Override public ChannelKey channel() { return CHANNEL; }

    public Optional<String> fromOpt() { return Optional.ofNullable(from).filter(s -> !s.isBlank()); }
    public Optional<String> messagingServiceSidOpt() { return Optional.ofNullable(messagingServiceSid).filter(s -> !s.isBlank()); }
    public Optional<String> statusCallbackUrlOpt() { return Optional.ofNullable(statusCallbackUrl).filter(s -> !s.isBlank()); }
}
