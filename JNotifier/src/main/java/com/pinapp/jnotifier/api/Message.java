package com.pinapp.jnotifier.api;

public interface Message {
    ChannelKey channel();
    String body();
}
