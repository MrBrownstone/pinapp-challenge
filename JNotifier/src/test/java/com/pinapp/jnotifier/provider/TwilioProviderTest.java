package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.api.SmsMessage;
import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwilioProviderTest {

    @Test
    void rejectsBlankCredentials() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new TwilioProvider(" ", "token"));
        assertEquals("accountSid blank", ex.getMessage());
        ValidationException ex2 = assertThrows(ValidationException.class, () -> new TwilioProvider("sid", " "));
        assertEquals("authToken blank", ex2.getMessage());
    }

    @Test
    void deliversAcceptedResult() {
        TwilioProvider provider = new TwilioProvider("AC123", "token");
        SmsMessage message = new SmsMessage("+15551112222", null, "+15553334444", "Body", null);

        SendResult result = provider.deliver(message);
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().orElse("").startsWith("SM-"));
    }
}
