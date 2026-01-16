package com.pinapp.jnotifier.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void isJNotifierException() {
        ValidationException ex = new ValidationException("msg");
        assertTrue(ex instanceof JNotifierException);
        assertEquals("msg", ex.getMessage());
    }
}
