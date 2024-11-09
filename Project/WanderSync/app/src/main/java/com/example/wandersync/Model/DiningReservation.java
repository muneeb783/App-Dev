package com.example.wandersync.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiningReservation {

    private String location;
    private String website;
    private long reservationTime;
    private String review;

    public DiningReservation() {
    }

    public DiningReservation(String location, String website, long reservationTime, String review) {
        this.location = location;
        this.website = website;
        this.reservationTime = reservationTime;
        this.review = review;
    }

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(reservationTime));
    }

    public String getFormattedReservationTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(reservationTime));
    }
}
