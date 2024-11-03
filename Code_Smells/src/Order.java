import java.util.List;

public class Order {
    private List<Item> items;
    private String customerName;
    private String customerEmail;

    public Order(List<Item> items, String customerName, String customerEmail) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public double calculateTotalPrice() {
        double total = items.stream().mapToDouble(Item::getTotalPrice).sum();
        total = applyGiftCardDiscount(total);
        total = applyOrderDiscount(total);
        return total;
    }

    private double applyGiftCardDiscount(double total) {
        if (hasGiftCard()) {
            total -= 10.0; // Subtract $10 for gift card
        }
        return total;
    }

    private double applyOrderDiscount(double total) {
        if (total > 100.0) {
            total *= 0.9; // Apply 10% discount for orders over $100
        }
        return total;
    }

    public boolean hasGiftCard() {
        return items.stream().anyMatch(item -> item instanceof GiftCardItem);
    }

    public void sendConfirmationEmail() {
        OrderNotification.sendConfirmationEmail(this);
    }

    public List<Item> getItems() {
        return items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void printOrder() {
        System.out.println("Order Details:");
        items.forEach(item -> System.out.println(item.getName() + " - $" + item.getPrice()));
    }
}
