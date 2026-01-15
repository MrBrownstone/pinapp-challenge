package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.SendResult;
import com.pinapp.jnotifier.api.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class TwilioProvider implements NotificationProvider<SmsMessage> {
    private static final Logger log = LoggerFactory.getLogger(TwilioProvider.class);

    private final String accountSid;
    private final String authToken;

    public TwilioProvider(String accountSid, String authToken) {
        if (accountSid == null || accountSid.isBlank()) throw new IllegalArgumentException("accountSid blank");
        if (authToken == null || authToken.isBlank()) throw new IllegalArgumentException("authToken blank");
        this.accountSid = accountSid;
        this.authToken = authToken;
    }

    @Override public String name() { return "twilio"; }

    @Override
    public SendResult deliver(SmsMessage message) {
        // Simulated send, but log the real shape Twilio expects
        log.info("Twilio SMS send accountSid={} to={} bodyLen={} from={} messagingServiceSid={} statusCallbackUrl={}",
                accountSid,
                message.to(),
                message.body().length(),
                message.fromOpt().orElse("<none>"),
                message.messagingServiceSidOpt().orElse("<none>"),
                message.statusCallbackUrlOpt().orElse("<none>")
        );

        // Twilio responses usually include a SID for the message
        // (conceptually, you always get an id back). :contentReference[oaicite:5]{index=5}
        return SendResult.accepted("SM-" + UUID.randomUUID());
    }
}
