package com.example.discount;

public class TaxableItem extends Item {
    private double taxRate;

    public TaxableItem(String name, double price, int quantity, Discount discount, double taxRate) {
        super(name, price, quantity, discount);
        this.taxRate = taxRate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double rate) {
        if (rate >= 0) {
            taxRate = rate;
        }
    }

    @Override
    public double getTotalPrice() {
        double discountedPrice = super.getTotalPrice();
        return discountedPrice + (discountedPrice * taxRate / 100);
    }
}
