package com.pinapp.jnotifier.api;

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
        if (platform == null) throw new IllegalArgumentException("platform must not be null");
        if (target == null) throw new IllegalArgumentException("target must not be null");
        if ((title == null || title.isBlank()) && (body == null || body.isBlank())) {
            throw new IllegalArgumentException("either title or body must be provided");
        }
        data = (data == null) ? Map.of() : Map.copyOf(data);
    }

    @Override public ChannelKey channel() { return CHANNEL; }

}
