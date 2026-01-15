package com.pinapp.jnotifier.core;

import com.pinapp.jnotifier.api.ChannelKey;
import com.pinapp.jnotifier.error.DeliveryException;
import com.pinapp.jnotifier.api.Message;
import com.pinapp.jnotifier.api.NotificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ProviderRegistry {
    private final Map<ChannelKey, NotificationProvider<? extends Message>> providers;

    public ProviderRegistry(Map<ChannelKey, NotificationProvider<? extends Message>> providers) {
        this.providers = Map.copyOf(Objects.requireNonNull(providers));
    }

    public NotificationProvider<? extends Message> getProvider(ChannelKey channel) {
        NotificationProvider<? extends Message> p = providers.get(channel);
        if (p == null) {
            throw new DeliveryException("No provider registered for channel: " + channel.value());
        }
        return p;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<ChannelKey, NotificationProvider<? extends Message>> providers = new HashMap<>();

        public Builder register(ChannelKey channel, NotificationProvider<? extends Message> provider) {
            Objects.requireNonNull(channel, "channel");
            Objects.requireNonNull(provider, "provider");
            providers.put(channel, provider);
            return this;
        }

        public Builder register(String channel, NotificationProvider<? extends Message> provider) {
            return register(new ChannelKey(channel), provider);
        }

        public ProviderRegistry build() {
            return new ProviderRegistry(providers);
        }
    }

}
