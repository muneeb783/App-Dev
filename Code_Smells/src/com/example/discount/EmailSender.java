package com.example.discount;

import java.util.logging.Logger;

public class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    public static void sendEmail(String customerEmail, String subject, String message) {
        logger.info(String.format("Email to: %s", customerEmail));
        logger.info(String.format("Subject: %s", subject));
        logger.info(String.format("Body: %s", message));
    }
}
