package com.example.wandersync.Model;

public class Destination {
    private String name;
    private int daysPlanned;

    public Destination(String name, int daysPlanned) {
        this.name = name;
        this.daysPlanned = daysPlanned;
    }

    public String getName() {
        return name;
    }

    public int getDaysPlanned() {
        return daysPlanned;
    }
}
