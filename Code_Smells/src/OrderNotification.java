public class OrderNotification {
    public static void sendConfirmationEmail(Order order) {
        String message = "Thank you for your order, " + order.getCustomerName() + "!\n\nYour order details:\n";
        for (Item item : order.getItems()) {
            message += item.getName() + " - $" + item.getPrice() + "\n";
        }
        message += "Total: $" + order.calculateTotalPrice();
        EmailSender.sendEmail(order.getCustomerEmail(), "Order Confirmation", message);
    }
}
