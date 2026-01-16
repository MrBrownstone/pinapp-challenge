package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PushMessageTest {

    @Test
    void createsMessageWithDataCopy() {
        Map<String, String> data = new HashMap<>();
        data.put("k", "v");
        PushMessage message = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("token"),
                "Title",
                "Body",
                data
        );

        data.put("k2", "v2");
        assertEquals(1, message.data().size());
        assertEquals("v", message.data().get("k"));
        assertThrows(UnsupportedOperationException.class, () -> message.data().put("k3", "v3"));
        assertEquals(PushMessage.CHANNEL, message.channel());
    }

    @Test
    void defaultsToEmptyData() {
        PushMessage message = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("token"),
                "Title",
                "Body",
                null
        );
        assertTrue(message.data().isEmpty());
    }

    @Test
    void rejectsNullPlatform() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new PushMessage(null, new PushToken("token"), "Title", "Body", Map.of()));
        assertEquals("platform must not be null", ex.getMessage());
    }

    @Test
    void rejectsNullTarget() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new PushMessage(PushPlatform.ANDROID, null, "Title", "Body", Map.of()));
        assertEquals("target must not be null", ex.getMessage());
    }

    @Test
    void rejectsMissingTitleAndBody() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new PushMessage(PushPlatform.ANDROID, new PushToken("token"), " ", " ", Map.of()));
        assertEquals("either title or body must be provided", ex.getMessage());
    }
}
