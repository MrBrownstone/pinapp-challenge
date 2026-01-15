package com.pinapp.jnotifier.api;

public interface PushTarget {
    TargetType type();
    String value();

    enum TargetType { TOKEN, TOPIC, CONDITION }
}
