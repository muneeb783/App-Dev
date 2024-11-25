package com.example.wandersync.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiningReservation {

    private String location;
    private String website;
    private long reservationTime;
    private String review;
    private String userId; // Add userId to track the user who created the reservation

    public DiningReservation() {
    }

    public DiningReservation(String location, String website,
                             long reservationTime, String review, String userId) {
        this.location = location;
        this.website = website;
        this.reservationTime = reservationTime;
        this.review = review;
        this.userId = userId;  // Store user ID
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getReservationDate() {
        return new Date(reservationTime);
    }

    public String getFormattedReservationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(reservationTime));
    }

    public String getFormattedReservationTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(reservationTime));
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return this.reservationTime < currentTime;
    }
    public String toString() {
        return website;
    }
}
