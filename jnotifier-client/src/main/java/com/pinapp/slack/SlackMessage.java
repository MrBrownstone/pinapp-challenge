package com.pinapp.slack;

import com.pinapp.jnotifier.api.ChannelKey;
import com.pinapp.jnotifier.api.Message;
import com.pinapp.jnotifier.error.ValidationException;

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
            throw new ValidationException("workspaceId must not be blank");
        }
        if (workspaceChannel == null || workspaceChannel.isBlank()) {
            throw new ValidationException("workspaceChannel must not be blank");
        }
        if (username == null || username.isBlank()) {
            throw new ValidationException("username must not be blank");
        }
        if (text == null || text.isBlank()) {
            throw new ValidationException("text must not be blank");
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
