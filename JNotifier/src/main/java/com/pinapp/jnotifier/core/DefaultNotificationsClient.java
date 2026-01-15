package com.pinapp.jnotifier.core;

import com.pinapp.jnotifier.api.Message;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.NotificationsClient;
import com.pinapp.jnotifier.api.SendResult;

import java.util.Objects;

public final class DefaultNotificationsClient implements NotificationsClient {
    private final ProviderRegistry registry;

    public DefaultNotificationsClient(ProviderRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    @Override
    public SendResult send(Message message) {
        NotificationProvider<? extends Message> provider = registry.getProvider(message.channel());
        return deliverUnsafe(provider, message);
    }

    @SuppressWarnings("unchecked")
    private static <M extends Message> SendResult deliverUnsafe(NotificationProvider<? extends Message> provider, Message message) {
        // channel match is already guaranteed by the registry key,
        // so this cast is safe as long as you register correctly.
        return ((NotificationProvider<M>) provider).deliver((M) message);
    }
}
