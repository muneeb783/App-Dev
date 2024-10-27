package com.example.wandersync.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class  Destination {
    private String name;
    private long daysPlanned;
    private String username;
    private String startDate;
    private String endDate;
    public Destination() {
    }

    public Destination(String name, long daysPlanned, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        this.name = name;
        this.daysPlanned = daysPlanned;
        this.startDate = startDate.format(formatter);
        this.endDate = endDate.format(formatter);
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
    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
