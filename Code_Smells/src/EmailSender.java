package com.example.discount;

import java.util.logging.Logger;

public class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    private EmailSender() {
        // Prevent instantiation
    }

    public static void sendEmail(String customerEmail, String subject, String message) {
        logger.info("Email to: " + customerEmail);
        logger.info("Subject: " + subject);
        logger.info("Body: " + message);
    }
}
