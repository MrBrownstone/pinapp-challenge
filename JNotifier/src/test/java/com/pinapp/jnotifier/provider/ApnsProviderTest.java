package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApnsProviderTest {

    @Test
    void rejectsBlankBundleId() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new ApnsProvider(" "));
        assertEquals("bundleId blank", ex.getMessage());
    }

    @Test
    void deliversAcceptedResult() {
        ApnsProvider provider = new ApnsProvider("com.example.app");
        PushMessage message = new PushMessage(
                PushPlatform.IOS,
                new PushToken("ios-token"),
                "Hi",
                "Body",
                Map.of("k", "v")
        );

        SendResult result = provider.deliver(message);
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().orElse("").startsWith("APNS-"));
    }
}
