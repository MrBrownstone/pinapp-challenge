package com.pinapp.jnotifier.core;

import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.provider.ApnsProvider;
import com.pinapp.jnotifier.provider.CompositePushProvider;
import com.pinapp.jnotifier.provider.FcmProvider;
import com.pinapp.jnotifier.provider.SendGridProvider;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
}
