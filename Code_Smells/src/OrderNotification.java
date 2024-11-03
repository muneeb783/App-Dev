package com.example.discount;

import java.util.logging.Logger;

public class OrderNotification {
    private static final Logger logger = Logger.getLogger(OrderNotification.class.getName());

    private OrderNotification() {
        // Prevent instantiation
    }

    public static void sendConfirmationEmail(Order order) {
        StringBuilder message = new StringBuilder("Thank you for your order, " + order.getCustomerName() + "!\n\nYour order details:\n");
        for (Item item : order.getItems()) {
            message.append(item.getName()).append(" - $").append(item.getPrice()).append("\n");
        }
        message.append("Total: $").append(order.calculateTotalPrice());
        EmailSender.sendEmail(order.getCustomerEmail(), "Order Confirmation", message.toString());
    }
}
