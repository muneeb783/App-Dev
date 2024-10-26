package com.example.wandersync.Model;

public class Destination {
    private String name;
    private long daysPlanned;

    public Destination(String name, long daysPlanned) {
        this.name = name;
        this.daysPlanned = daysPlanned;
    }

    public String getName() {
        return name;
    }

    public long getDaysPlanned() {
        return daysPlanned;
    }
}
