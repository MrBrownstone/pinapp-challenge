package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendGridProviderTest {

    @Test
    void rejectsBlankApiKey() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new SendGridProvider(" "));
        assertEquals("API key must not be null or empty", ex.getMessage());
    }

    @Test
    void deliversAcceptedResult() {
        SendGridProvider provider = new SendGridProvider("DUMMY_API_KEY");
        EmailMessage message = new EmailMessage("alice@example.com", "bob@example.com", "Hi", "Body");

        SendResult result = provider.deliver(message);
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().orElse("").startsWith("SG-"));
    }
}
