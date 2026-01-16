package com.pinapp.jnotifier.core;

import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.error.DeliveryException;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.error.ValidationException;
import com.pinapp.jnotifier.provider.ApnsProvider;
import com.pinapp.jnotifier.provider.CompositePushProvider;
import com.pinapp.jnotifier.provider.FcmProvider;
import com.pinapp.jnotifier.provider.SendGridProvider;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultNotificationsClientTest {

    @Test
    void sendEmailUsesRegisteredProvider() {
        SendGridProvider provider = new SendGridProvider("DUMMY_API_KEY");
        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Greet", "Hello Bob");

        ProviderRegistry registry = new ProviderRegistry(Map.of(EmailMessage.CHANNEL, provider));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        // Act
        SendResult result = client.send(message);

        // Assert
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().isPresent(), "providerMessageId should be present");
        assertTrue(result.providerMessageId().get().startsWith("SG-"), "provider id should start with SG-");
    }

    @Test
    void sendPushRoutesToCompositeProviderForAndroidAndIos() {
        CompositePushProvider provider = new CompositePushProvider(Map.of(
                PushPlatform.ANDROID, new FcmProvider("test-project"),
                PushPlatform.IOS, new ApnsProvider("com.example.app")
        ));

        ProviderRegistry registry = new ProviderRegistry(Map.of(PushMessage.CHANNEL, provider));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        PushMessage androidMessage = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("android-token"),
                "Hi",
                "Android ping",
                Map.of("key", "value")
        );
        PushMessage iosMessage = new PushMessage(
                PushPlatform.IOS,
                new PushToken("ios-token"),
                "Hi",
                "iOS ping",
                Map.of("key", "value")
        );

        SendResult androidResult = client.send(androidMessage);
        SendResult iosResult = client.send(iosMessage);

        assertEquals(SendResult.Status.ACCEPTED, androidResult.status());
        assertTrue(androidResult.providerMessageId().isPresent(), "android providerMessageId should be present");
        assertTrue(androidResult.providerMessageId().get().startsWith("FCM-"), "android provider id should start with FCM-");

        assertEquals(SendResult.Status.ACCEPTED, iosResult.status());
        assertTrue(iosResult.providerMessageId().isPresent(), "ios providerMessageId should be present");
        assertTrue(iosResult.providerMessageId().get().startsWith("APNS-"), "ios provider id should start with APNS-");
    }

    @Test
    void sendRejectsNullMessage() {
        ProviderRegistry registry = new ProviderRegistry(Map.of(
                EmailMessage.CHANNEL, new SendGridProvider("DUMMY_API_KEY")
        ));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        ValidationException ex = assertThrows(ValidationException.class, () -> client.send(null));
        assertEquals("message must not be null", ex.getMessage());
    }

    @Test
    void sendFailsWhenProviderMissing() {
        ProviderRegistry registry = new ProviderRegistry(Map.of());
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Greet", "Hello Bob");
        DeliveryException ex = assertThrows(DeliveryException.class, () -> client.send(message));
        assertTrue(ex.getMessage().contains("No provider registered for channel"));
    }

    @Test
    void sendWrapsUnexpectedProviderRuntimeFailure() {
        ProviderRegistry registry = new ProviderRegistry(Map.of(
                EmailMessage.CHANNEL, new NotificationProvider<EmailMessage>() {
                    @Override public String name() { return "boom-provider"; }
                    @Override public SendResult deliver(EmailMessage message) {
                        throw new RuntimeException("boom");
                    }
                }
        ));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Greet", "Hello Bob");
        DeliveryException ex = assertThrows(DeliveryException.class, () -> client.send(message));
        assertTrue(ex.getMessage().contains("Delivery failed for channel"));
        assertNotNull(ex.getCause());
    }

    @Test
    void sendAsyncReturnsResultForValidMessage() {
        SendGridProvider provider = new SendGridProvider("DUMMY_API_KEY");
        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Greet", "Hello Bob");

        ProviderRegistry registry = new ProviderRegistry(Map.of(EmailMessage.CHANNEL, provider));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        SendResult result = client.sendAsync(message).join();
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().isPresent(), "providerMessageId should be present");
    }

    @Test
    void sendAsyncRejectsNullMessage() {
        ProviderRegistry registry = new ProviderRegistry(Map.of(
                EmailMessage.CHANNEL, new SendGridProvider("DUMMY_API_KEY")
        ));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        CompletionException ex = assertThrows(CompletionException.class, () -> client.sendAsync(null).join());
        assertInstanceOf(ValidationException.class, ex.getCause());
        assertEquals("message must not be null", ex.getCause().getMessage());
    }

    @Test
    void sendAsyncFailsWhenProviderMissing() {
        ProviderRegistry registry = new ProviderRegistry(Map.of());
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Greet", "Hello Bob");
        CompletionException ex = assertThrows(CompletionException.class, () -> client.sendAsync(message).join());
        assertInstanceOf(DeliveryException.class, ex.getCause());
        assertTrue(ex.getCause().getMessage().contains("No provider registered for channel"));
    }

    @Test
    void sendAsyncWrapsUnexpectedProviderRuntimeFailure() {
        ProviderRegistry registry = new ProviderRegistry(Map.of(
                EmailMessage.CHANNEL, new NotificationProvider<EmailMessage>() {
                    @Override public String name() { return "boom-provider"; }
                    @Override public SendResult deliver(EmailMessage message) {
                        throw new RuntimeException("boom");
                    }
                }
        ));
        DefaultNotificationsClient client = new DefaultNotificationsClient(registry);

        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Greet", "Hello Bob");
        CompletionException ex = assertThrows(CompletionException.class, () -> client.sendAsync(message).join());
        assertInstanceOf(DeliveryException.class, ex.getCause());
        assertTrue(ex.getCause().getMessage().contains("Delivery failed for channel"));
        assertNotNull(ex.getCause().getCause());
    }
}
