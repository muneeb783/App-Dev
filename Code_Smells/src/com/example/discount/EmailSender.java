package com.example.discount;

import java.util.logging.Logger;

public class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    private EmailSender() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void sendEmail(String customerEmail, String subject, String message) {
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(String.format("Email to: %s", customerEmail));
            logger.info(String.format("Subject: %s", subject));
            logger.info(String.format("Body: %s", message));
        }
    }
}
