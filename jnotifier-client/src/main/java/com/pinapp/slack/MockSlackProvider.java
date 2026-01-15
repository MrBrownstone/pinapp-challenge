package com.pinapp.slack;

import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class MockSlackProvider implements NotificationProvider<SlackMessage> {
    private static final Logger logger = LoggerFactory.getLogger("MockSlackProvider");
    private final String token;

    public MockSlackProvider(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token must not be blank");
        }
        this.token = token;
    }

    @Override
    public String name() {
        return "mock-slack";
    }

    @Override
    public SendResult deliver(SlackMessage message) {
        logger.info("""
                Sending Slack message:
                Workspace: {}
                Channel: {}
                Username: {}
                Text: {}""", message.workspaceId(), message.workspaceChannel(), message.username(), message.text());

        // Placeholder for Slack Web API call using the stored token.
        String id = "SLACK-" + UUID.randomUUID();
        return SendResult.accepted(id);
    }
}
