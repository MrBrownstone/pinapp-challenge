package com.pinapp.jnotifier.error;

public class JNotifierException extends RuntimeException {
    public JNotifierException(String message) {
        super(message);
    }

    public JNotifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
