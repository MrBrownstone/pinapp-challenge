package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FcmProviderTest {

    @Test
    void rejectsBlankProjectId() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new FcmProvider(" "));
        assertEquals("projectId blank", ex.getMessage());
    }

    @Test
    void deliversAcceptedResult() {
        FcmProvider provider = new FcmProvider("project-1");
        PushMessage message = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("android-token"),
                "Hi",
                "Body",
                Map.of("k", "v")
        );

        SendResult result = provider.deliver(message);
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().orElse("").startsWith("FCM-"));
    }
}
