package com.pinapp.jnotifier.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JNotifierExceptionTest {

    @Test
    void capturesMessageAndCause() {
        RuntimeException cause = new RuntimeException("root");
        JNotifierException ex = new JNotifierException("msg", cause);
        assertEquals("msg", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
