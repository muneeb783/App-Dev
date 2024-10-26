package com.example.wandersync.Model;

public class  Destination {
    private String name;
    private long daysPlanned;
    private String username;
    public Destination() {
    }

    public Destination(String name, long daysPlanned) {
        this.name = name;
        this.daysPlanned = daysPlanned;
        this.username = "";
    }

    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String str) {
        this.username = str;
    }
    public long getDaysPlanned() {
        return daysPlanned;
    }

}
