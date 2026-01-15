package com.pinapp;

import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.api.Message;
import com.pinapp.jnotifier.api.NotificationsClient;
import com.pinapp.jnotifier.api.PushMessage;
import com.pinapp.jnotifier.api.PushPlatform;
import com.pinapp.jnotifier.api.PushToken;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.api.SmsMessage;
import com.pinapp.jnotifier.core.DefaultNotificationsClient;
import com.pinapp.jnotifier.core.ProviderRegistry;
import com.pinapp.jnotifier.provider.ApnsProvider;
import com.pinapp.jnotifier.provider.CompositePushProvider;
import com.pinapp.jnotifier.provider.FcmProvider;
import com.pinapp.jnotifier.provider.SendGridProvider;
import com.pinapp.jnotifier.provider.TwilioProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String BANNER = String.join("\n",
            "==============================================",
            "   JNotifier Demo - Multi-Channel Dispatch",
            "=============================================="
    );
    private static final String SEPARATOR = "----------------------------------------------";

    public static void main(String[] args) {
        log.info("\n{}", BANNER);

        CompositePushProvider pushProvider = new CompositePushProvider(Map.of(
                PushPlatform.ANDROID, new FcmProvider("demo-fcm-project"),
                PushPlatform.IOS, new ApnsProvider("com.pinapp.demo")
        ));

        ProviderRegistry registry = ProviderRegistry.builder()
                .register(EmailMessage.CHANNEL, new SendGridProvider("SG.fake-api-key"))
                .register(SmsMessage.CHANNEL, new TwilioProvider("ACXXXXXXXXXXXXXXXX", "auth-token"))
                .register(PushMessage.CHANNEL, pushProvider)
                .build();

        NotificationsClient client = new DefaultNotificationsClient(registry);

        EmailMessage email = new EmailMessage(
                "alice@example.com",
                "bob@example.com",
                "Welcome to Pinapp",
                "Thanks for trying the notification library."
        );
        sendAndLog(client, email, "email");

        SmsMessage sms = new SmsMessage(
                "+15551112222",
                null,
                "+15553334444",
                "Your code is 123456",
                "https://example.com/sms/status"
        );
        sendAndLog(client, sms, "sms");

        PushMessage androidPush = new PushMessage(
                PushPlatform.ANDROID,
                new PushToken("android-device-token"),
                "Hello Android",
                "Pinapp push payload",
                Map.of("campaign", "spring-2025", "tier", "gold")
        );
        sendAndLog(client, androidPush, "push-android");

        PushMessage iosPush = new PushMessage(
                PushPlatform.IOS,
                new PushToken("ios-device-token"),
                "Hello iOS",
                "Pinapp iOS payload",
                Map.of("campaign", "spring-2025", "tier", "platinum")
        );
        sendAndLog(client, iosPush, "push-ios");
    }

    private static void sendAndLog(NotificationsClient client, Message message, String label) {
        log.info("\n{}\nSending {} message...\n{}\n{}",
                SEPARATOR,
                label,
                SEPARATOR,
                formatMessage(message)
        );
        SendResult result = client.send(message);
        log.info("Result: status={} id={} details={}\n{}",
                result.status(),
                result.providerMessageId().orElse("<none>"),
                result.details().orElse("<none>"),
                SEPARATOR
        );
    }

    private static String formatMessage(Message message) {
        if (message instanceof EmailMessage email) {
            return String.join("\n",
                    "Channel: email",
                    "From: " + email.from(),
                    "To: " + email.to(),
                    "Subject: " + email.subject(),
                    "Body:",
                    indent(email.body())
            );
        }
        if (message instanceof SmsMessage sms) {
            return String.join("\n",
                    "Channel: sms",
                    "From: " + sms.fromOpt().orElse("<messaging-service>"),
                    "To: " + sms.to(),
                    "Status Callback: " + sms.statusCallbackUrlOpt().orElse("<none>"),
                    "Body:",
                    indent(sms.body())
            );
        }
        if (message instanceof PushMessage push) {
            return String.join("\n",
                    "Channel: push",
                    "Platform: " + push.platform(),
                    "Target: " + push.target().type() + " -> " + push.target().value(),
                    "Title: " + (push.title() == null ? "<none>" : push.title()),
                    "Body:",
                    indent(push.body() == null ? "" : push.body()),
                    "Data: " + push.data()
            );
        }
        return "Channel: " + message.channel().value();
    }

    private static String indent(String text) {
        if (text == null || text.isBlank()) {
            return "  <empty>";
        }
        return "  " + text.replace("\n", "\n  ");
    }
}
