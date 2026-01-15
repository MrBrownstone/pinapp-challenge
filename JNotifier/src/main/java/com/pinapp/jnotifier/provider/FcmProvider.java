package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class FcmProvider implements NotificationProvider<PushMessage> {
    private static final Logger log = LoggerFactory.getLogger(FcmProvider.class);

    private final String projectId;

    public FcmProvider(String projectId) {
        if (projectId == null || projectId.isBlank()) throw new IllegalArgumentException("projectId blank");
        this.projectId = projectId;
    }

    @Override public String name() { return "fcm"; }

    @Override
    public SendResult deliver(PushMessage message) {
        log.info("FCM push projectId={} targetType={} target={} title={} body={} dataKeys={}",
                projectId,
                message.target().type(),
                message.target().value(),
                message.title(),
                message.body(),
                message.data().keySet()
        );

        // Simulate "accepted"
        return SendResult.accepted("FCM-" + UUID.randomUUID());
    }
}

