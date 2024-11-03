package com.example.discount;

public class AmountDiscount implements Discount {
    private final double amount;

    public AmountDiscount(double amount) {
        this.amount = amount;
    }

    @Override
    public double apply(double price) {
        return price - amount;
    }
}
