package com.pinapp.jnotifier.core;

import com.pinapp.jnotifier.error.DeliveryException;
import com.pinapp.jnotifier.error.JNotifierException;
import com.pinapp.jnotifier.api.Message;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.NotificationsClient;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.error.ValidationException;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class DefaultNotificationsClient implements NotificationsClient {
    private final ProviderRegistry registry;

    public DefaultNotificationsClient(ProviderRegistry registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    @Override
    public SendResult send(Message message) {
        if (message == null) {
            throw new ValidationException("message must not be null");
        }
        NotificationProvider<? extends Message> provider = registry.getProvider(message.channel());
        try {
            return deliverUnsafe(provider, message);
        } catch (JNotifierException e) {
            throw e;
        } catch (RuntimeException e) {
            String channel = message.channel().value();
            throw new DeliveryException(
                    "Delivery failed for channel: " + channel + " via provider: " + provider.name(),
                    e
            );
        }
    }

    public CompletableFuture<SendResult> sendAsync(Message message) {
        return CompletableFuture.supplyAsync(() -> send(message));
    }

    @SuppressWarnings("unchecked")
    private static <M extends Message> SendResult deliverUnsafe(NotificationProvider<? extends Message> provider, Message message) {
        // channel match is already guaranteed by the registry key,
        // so this cast is safe as long as you register correctly.
        return ((NotificationProvider<M>) provider).deliver((M) message);
    }
}
