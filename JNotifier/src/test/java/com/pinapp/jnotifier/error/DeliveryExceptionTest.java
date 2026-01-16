package com.pinapp.jnotifier.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryExceptionTest {

    @Test
    void isJNotifierException() {
        DeliveryException ex = new DeliveryException("msg");
        assertTrue(ex instanceof JNotifierException);
        assertEquals("msg", ex.getMessage());
    }
}
