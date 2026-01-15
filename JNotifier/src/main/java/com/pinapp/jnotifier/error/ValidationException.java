package com.pinapp.jnotifier.error;

public class ValidationException extends JNotifierException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
