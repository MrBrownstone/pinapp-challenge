package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class ApnsProvider implements NotificationProvider<PushMessage> {
    private static final Logger log = LoggerFactory.getLogger(ApnsProvider.class);

    private final String bundleId;

    public ApnsProvider(String bundleId) {
        if (bundleId == null || bundleId.isBlank()) throw new ValidationException("bundleId blank");
        this.bundleId = bundleId;
    }

    @Override public String name() { return "apns"; }

    @Override
    public SendResult deliver(PushMessage message) {
        log.info("APNs push bundleId={} targetType={} target={} title={} body={} dataKeys={}",
                bundleId,
                message.target().type(),
                message.target().value(),
                message.title(),
                message.body(),
                message.data().keySet()
        );

        return SendResult.accepted("APNS-" + UUID.randomUUID());
    }
}
