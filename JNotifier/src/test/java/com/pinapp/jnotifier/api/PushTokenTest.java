package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushTokenTest {

    @Test
    void createsTokenWithValue() {
        PushToken token = new PushToken("token-123");
        assertEquals("token-123", token.value());
        assertEquals(PushTarget.TargetType.TOKEN, token.type());
    }

    @Test
    void rejectsBlankToken() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new PushToken(" "));
        assertEquals("push token blank", ex.getMessage());
    }
}
