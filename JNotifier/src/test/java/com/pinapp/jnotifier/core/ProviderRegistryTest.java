package com.pinapp.jnotifier.core;

import com.pinapp.jnotifier.api.ChannelKey;
import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.error.DeliveryException;
import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ProviderRegistryTest {

    @Test
    void returnsRegisteredProvider() {
        NotificationProvider<EmailMessage> provider = mock(NotificationProvider.class);
        ProviderRegistry registry = new ProviderRegistry(Map.of(EmailMessage.CHANNEL, provider));

        assertSame(provider, registry.getProvider(EmailMessage.CHANNEL));
    }

    @Test
    void throwsWhenProviderMissing() {
        ProviderRegistry registry = new ProviderRegistry(Map.of());
        DeliveryException ex = assertThrows(DeliveryException.class, () -> registry.getProvider(EmailMessage.CHANNEL));
        assertTrue(ex.getMessage().contains("No provider registered for channel"));
    }

    @Test
    void builderRejectsNullInputs() {
        ProviderRegistry.Builder builder = ProviderRegistry.builder();
        assertThrows(NullPointerException.class, () -> builder.register((ChannelKey) null, mock(NotificationProvider.class)));
        assertThrows(NullPointerException.class, () -> builder.register(EmailMessage.CHANNEL, null));
    }

    @Test
    void builderRejectsInvalidChannelString() {
        ProviderRegistry.Builder builder = ProviderRegistry.builder();
        ValidationException ex = assertThrows(ValidationException.class, () -> builder.register(" ", mock(NotificationProvider.class)));
        assertEquals("channel key must not be blank", ex.getMessage());
    }
}
