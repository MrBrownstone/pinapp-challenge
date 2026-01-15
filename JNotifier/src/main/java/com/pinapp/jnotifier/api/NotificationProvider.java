package com.pinapp.jnotifier.api;

public interface NotificationProvider<M extends Message> {
    String name();
    SendResult deliver(M message);
}
