package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChannelKeyTest {

    @Test
    void createsChannelKeyWithValue() {
        ChannelKey key = new ChannelKey("email");
        assertEquals("email", key.value());
    }

    @Test
    void rejectsBlankChannelKey() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new ChannelKey(" "));
        assertEquals("channel key must not be blank", ex.getMessage());
    }
}
