package com.example.wandersync.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiningReservation {

    private String location;
    private String website;
    private long reservationTime;
    private String review;

    // No-argument constructor required by Firebase
    public DiningReservation() {
        // Default constructor required for calls to DataSnapshot.getValue(DiningReservation.class)
    }

    // Constructor with parameters
    public DiningReservation(String location, String website, long reservationTime, String review) {
        this.location = location;
        this.website = website;
        this.reservationTime = reservationTime;
        this.review = review;
    }

    // Getters and setters for each field
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(long reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getReservationDate() {
        return new Date(reservationTime);  // Converts timestamp to Date object
    }

    public String getFormattedReservationDate() {
        // Use SimpleDateFormat to return formatted date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(reservationTime));
    }

    public String getFormattedReservationTime() {
        // Format the time as "HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(reservationTime));
    }
}
