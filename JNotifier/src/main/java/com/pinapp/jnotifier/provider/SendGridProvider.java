package com.pinapp.jnotifier.provider;

import com.pinapp.jnotifier.api.EmailMessage;
import com.pinapp.jnotifier.api.NotificationProvider;
import com.pinapp.jnotifier.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class SendGridProvider implements NotificationProvider<EmailMessage> {
    private static final Logger logger = LoggerFactory.getLogger("SendGridProvider");


    public SendGridProvider(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key must not be null or empty");
        }
        //SendGrid sg = new SendGrid(apiKey);
    }

    @Override
    public String name() {
        return "fake-sendgrid";
    }

    @Override
    public SendResult deliver(EmailMessage message) {

        String subject = message.subject();
        logger.info("""
                Sending email via SendGrid:
                From: {}
                To: {}
                Subject: {}
                Body: {}""", message.from(), message.to(), subject, message.body());

        /*
        ACTUAL BUSINESS LOGIC TO SEND EMAIL VIA SENDGRID

        Email from = new Email(message.from());
        Email to = new Email(message.to());
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);


        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }*/

        String id = "SG-" + UUID.randomUUID();
        return SendResult.accepted(id);
    }
}
