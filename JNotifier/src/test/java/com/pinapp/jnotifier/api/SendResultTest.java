package com.pinapp.jnotifier.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendResultTest {

    @Test
    void acceptedIncludesMessageId() {
        SendResult result = SendResult.accepted("id-123");
        assertEquals(SendResult.Status.ACCEPTED, result.status());
        assertTrue(result.providerMessageId().isPresent());
        assertEquals("id-123", result.providerMessageId().get());
        assertTrue(result.details().isEmpty());
    }

    @Test
    void failedIncludesDetails() {
        SendResult result = SendResult.failed("oops");
        assertEquals(SendResult.Status.FAILED, result.status());
        assertTrue(result.providerMessageId().isEmpty());
        assertTrue(result.details().isPresent());
        assertEquals("oops", result.details().get());
    }
}
