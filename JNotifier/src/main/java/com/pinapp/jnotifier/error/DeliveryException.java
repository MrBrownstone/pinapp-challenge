package com.pinapp.jnotifier.error;

public class DeliveryException extends JNotifierException {
    public DeliveryException(String message) {
        super(message);
    }

    public DeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
