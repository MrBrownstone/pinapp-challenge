package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmsMessageTest {

    @Test
    void createsMessageWithFrom() {
        SmsMessage message = new SmsMessage(
                "+15551112222",
                null,
                "+15553334444",
                "Hello",
                null
        );
        assertTrue(message.fromOpt().isPresent());
        assertFalse(message.messagingServiceSidOpt().isPresent());
        assertFalse(message.statusCallbackUrlOpt().isPresent());
    }

    @Test
    void createsMessageWithMessagingServiceSid() {
        SmsMessage message = new SmsMessage(
                null,
                "MG123",
                "+15553334444",
                "Hello",
                "https://example.com/status"
        );
        assertFalse(message.fromOpt().isPresent());
        assertTrue(message.messagingServiceSidOpt().isPresent());
        assertTrue(message.statusCallbackUrlOpt().isPresent());
    }

    @Test
    void rejectsMissingFromAndServiceSid() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new SmsMessage(null, null, "+15553334444", "Hello", null));
        assertEquals("Provide exactly one of: from OR messagingServiceSid", ex.getMessage());
    }

    @Test
    void rejectsBothFromAndServiceSid() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new SmsMessage("+15551112222", "MG123", "+15553334444", "Hello", null));
        assertEquals("Provide exactly one of: from OR messagingServiceSid", ex.getMessage());
    }

    @Test
    void rejectsInvalidTo() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new SmsMessage("+15551112222", null, "15553334444", "Hello", null));
        assertEquals("to should look like E.164 (start with '+')", ex.getMessage());
    }

    @Test
    void rejectsInvalidFrom() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new SmsMessage("15551112222", null, "+15553334444", "Hello", null));
        assertEquals("from should look like E.164 (start with '+')", ex.getMessage());
    }
}
