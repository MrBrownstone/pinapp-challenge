package com.pinapp.jnotifier.api;

public interface NotificationsClient {
    <M extends Message> SendResult send(M message);
}
