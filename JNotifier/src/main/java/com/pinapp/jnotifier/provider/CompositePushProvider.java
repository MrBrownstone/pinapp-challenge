package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.ChannelKey;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CompositePushProvider implements NotificationProvider<PushMessage> {
    private static final Logger log = LoggerFactory.getLogger(CompositePushProvider.class);

    private final Map<PushPlatform, NotificationProvider<PushMessage>> delegates;

    public CompositePushProvider(Map<PushPlatform, NotificationProvider<PushMessage>> delegates) {
        Objects.requireNonNull(delegates, "delegates");
        this.delegates = new EnumMap<>(PushPlatform.class);
        this.delegates.putAll(delegates);
    }


    @Override
    public String name() {
        return "push-provider";
    }

    @Override
    public SendResult deliver(PushMessage message) {
        NotificationProvider<PushMessage> delegate = delegates.get(message.platform());
        if (delegate == null) {
            return SendResult.failed("No push provider configured for platform: " + message.platform());
        }

        log.debug("Routing push platform={} to provider={}", message.platform(), delegate.name());
        return delegate.deliver(message);
    }
}
