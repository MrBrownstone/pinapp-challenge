package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.api.NotificationProvider;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompositePushProviderTest {

    @Test
    void routesToPlatformProvider() {
        NotificationProvider<PushMessage> androidProvider = mock(NotificationProvider.class);
        when(androidProvider.name()).thenReturn("android-provider");
        when(androidProvider.deliver(any())).thenReturn(SendResult.accepted("FCM-1"));

        CompositePushProvider provider = new CompositePushProvider(Map.of(
                PushPlatform.ANDROID, androidProvider
        ));

        PushMessage message = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("token"),
                "Hi",
                "Body",
                Map.of()
        );

        SendResult result = provider.deliver(message);
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        verify(androidProvider).deliver(message);
    }

    @Test
    void returnsFailedWhenNoProviderForPlatform() {
        CompositePushProvider provider = new CompositePushProvider(Map.of());

        PushMessage message = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("token"),
                "Hi",
                "Body",
                Map.of()
        );

        SendResult result = provider.deliver(message);
        assertEquals(SendResult.Status.FAILED, result.status());
        assertTrue(result.details().orElse("").contains("No push provider configured for platform"));
    }
}
