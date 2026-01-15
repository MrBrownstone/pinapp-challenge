# JNotifier

JNotifier is a small Java library that provides a single client API for sending
notifications through different channels (email and push). It routes messages
to registered providers based on a channel key and returns a `SendResult` for
each delivery attempt.

## Features
- Unified `NotificationsClient` API for multiple channels.
- Pluggable providers via `ProviderRegistry`.
- Built-in message types for email and push.
- Push routing by platform (iOS/Android) via a composite provider.

## Core concepts
- `Message`: common contract with `channel()`, `from()`, `to()`, `body()`.
- `NotificationProvider<M>`: delivers a specific message type.
- `ProviderRegistry`: maps a `ChannelKey` to a provider.
- `SendResult`: indicates `ACCEPTED`, `SENT`, `REJECTED`, or `FAILED`.

## Usage

### Register providers and send email
```java
import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.core.DefaultNotificationsClient;
import com.pinapp.jnotifier.core.ProviderRegistry;
import com.pinapp.jnotifier.provider.SendGridProvider;

ProviderRegistry registry = ProviderRegistry.builder()
        .register(EmailMessage.CHANNEL, new SendGridProvider("DUMMY_API_KEY"))
        .build();

DefaultNotificationsClient client = new DefaultNotificationsClient(registry);
EmailMessage message = new EmailMessage(
        "alice@example.com",
        "bob@example.com",
        "Greet",
        "Hello Bob"
);

SendResult result = client.send(message);
```

### Register providers and send push
```java
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.core.DefaultNotificationsClient;
import com.pinapp.jnotifier.core.ProviderRegistry;
import com.pinapp.jnotifier.provider.ApnsProvider;
import com.pinapp.jnotifier.provider.CompositePushProvider;
import com.pinapp.jnotifier.provider.FcmProvider;

CompositePushProvider pushProvider = new CompositePushProvider(Map.of(
        PushPlatform.ANDROID, new FcmProvider("my-fcm-project"),
        PushPlatform.IOS, new ApnsProvider("com.example.app")
));

ProviderRegistry registry = ProviderRegistry.builder()
        .register(PushMessage.CHANNEL, pushProvider)
        .build();

DefaultNotificationsClient client = new DefaultNotificationsClient(registry);
PushMessage message = new PushMessage(
        PushPlatform.ANDROID,
        new PushToken("device-token"),
        "Hello",
        "Android ping",
        Map.of("key", "value")
);

client.send(message);
```

### Extend with a new channel (Slack example from jnotifier-client)
```java
import com.pinapp.jnotifier.api.ChannelKey;
import com.pinapp.jnotifier.api.Message;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.core.DefaultNotificationsClient;
import com.pinapp.jnotifier.core.ProviderRegistry;

import java.util.UUID;

public record SlackMessage(
        String from,
        String to,
        String workspaceId,
        String workspaceChannel,
        String username,
        String text
) implements Message {
    public static final ChannelKey CHANNEL = new ChannelKey("slack");

    @Override public ChannelKey channel() { return CHANNEL; }
    @Override public String body() { return text; }
}

public final class MockSlackProvider implements NotificationProvider<SlackMessage> {
    @Override public String name() { return "mock-slack"; }
    @Override public SendResult deliver(SlackMessage message) {
        String id = "SLACK-" + UUID.randomUUID();
        return SendResult.accepted(id);
    }
}

ProviderRegistry registry = ProviderRegistry.builder()
        .register(SlackMessage.CHANNEL, new MockSlackProvider())
        .build();

DefaultNotificationsClient client = new DefaultNotificationsClient(registry);
SlackMessage message = new SlackMessage(
        "alerts-service",
        "#alerts",
        "workspace-123",
        "#alerts",
        "notifier-bot",
        "JNotifier Slack integration says hello"
);

SendResult result = client.send(message);
```

## Notes
- The sample providers (`SendGridProvider`, `FcmProvider`, `ApnsProvider`)
  currently log and return an accepted result; plug in real SDK calls where
  indicated.
- Push targets can be a token, topic, or condition via `PushToken`,
  `PushTopic`, or `PushCondition`.
