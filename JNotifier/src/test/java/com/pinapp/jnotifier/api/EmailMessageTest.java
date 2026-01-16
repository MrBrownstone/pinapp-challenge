package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailMessageTest {

    @Test
    void createsValidEmailMessage() {
        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Hi", "Body");
        assertEquals("alice@example.com", message.from());
        assertEquals("bob@example.com", message.to());
        assertEquals("Body", message.body());
        assertEquals(EmailMessage.CHANNEL, message.channel());
    }

    @Test
    void allowsNullSubject() {
        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", null, "Body");
        assertNull(message.subject());
    }

    @Test
    void rejectsInvalidFrom() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new EmailMessage("bad", "bob@example.com", "Hi", "Body"));
        assertEquals("invalid from email", ex.getMessage());
    }

    @Test
    void rejectsInvalidTo() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new EmailMessage("alice@example.com", "bad", "Hi", "Body"));
        assertEquals("invalid to email", ex.getMessage());
    }

    @Test
    void rejectsBlankBody() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> new EmailMessage("alice@example.com", "bob@example.com", "Hi", " "));
        assertEquals("body must not be blank", ex.getMessage());
    }
}
