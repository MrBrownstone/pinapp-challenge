package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushTopicTest {

    @Test
    void createsTopicWithValue() {
        PushTopic topic = new PushTopic("news");
        assertEquals("news", topic.value());
        assertEquals(PushTarget.TargetType.TOPIC, topic.type());
    }

    @Test
    void rejectsBlankTopic() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new PushTopic(" "));
        assertEquals("push topic blank", ex.getMessage());
    }
}
