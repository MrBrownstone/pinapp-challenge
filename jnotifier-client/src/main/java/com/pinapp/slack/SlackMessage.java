package com.pinapp.slack;

import com.pinapp.jnotifier.api.ChannelKey;
import com.pinapp.jnotifier.api.Message;

public record SlackMessage(
        String from,
        String to,
        String workspaceId,
        String workspaceChannel,
        String username,
        String text
) implements Message {
    public static final ChannelKey CHANNEL = new ChannelKey("slack");

    public SlackMessage {
        if (workspaceId == null || workspaceId.isBlank()) {
            throw new IllegalArgumentException("workspaceId must not be blank");
        }
        if (workspaceChannel == null || workspaceChannel.isBlank()) {
            throw new IllegalArgumentException("workspaceChannel must not be blank");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text must not be blank");
        }
    }

    @Override
    public ChannelKey channel() {
        return CHANNEL;
    }

    @Override
    public String body() {
        return text;
    }
}
