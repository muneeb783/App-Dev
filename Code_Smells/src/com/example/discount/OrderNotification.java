package com.example.discount;

public class OrderNotification {
    private OrderNotification() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void sendConfirmationEmail(Order order) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Thank you for your order, %s!%n%nYour order details:%n", order.getCustomerName()));
        
        for (Item item : order.getItems()) {
            messageBuilder.append(String.format("%s - $%.2f%n", item.getName(), item.getPrice()));
        }
        
        messageBuilder.append(String.format("Total: $%.2f", order.calculateTotalPrice()));
        
        EmailSender.sendEmail(order.getCustomerEmail(), "Order Confirmation", messageBuilder.toString());
    }
}
