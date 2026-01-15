package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;

import java.util.Map;

public record PushMessage(
        PushPlatform platform,
        PushTarget target,
        String title,
        String body,
        Map<String, String> data
) implements Message {

    public static final ChannelKey CHANNEL = new ChannelKey("push");

    public PushMessage {
        if (platform == null) throw new ValidationException("platform must not be null");
        if (target == null) throw new ValidationException("target must not be null");
        if ((title == null || title.isBlank()) && (body == null || body.isBlank())) {
            throw new ValidationException("either title or body must be provided");
        }
        data = (data == null) ? Map.of() : Map.copyOf(data);
    }

    @Override public ChannelKey channel() { return CHANNEL; }

}
