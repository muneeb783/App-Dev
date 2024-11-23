package com.example.wandersync.Model;

import java.util.Objects;

public class TravelPost {

    private int id; // Unique identifier
    private String startDate;
    private String endDate;
    private String destination;
    private String accommodations;
    private String bookingReservation;
    private String notes;

    public TravelPost(int id, String startDate, String endDate, String destination,
                      String accommodations, String bookingReservation, String notes) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.accommodations = accommodations;
        this.bookingReservation = bookingReservation;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDestination() {
        return destination;
    }

    public String getAccommodations() {
        return accommodations;
    }

    public String getBookingReservation() {
        return bookingReservation;
    }

    public String getNotes() {
        return notes;
    }

    // Equals and hashCode for DiffUtil in RecyclerView
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelPost that = (TravelPost) o;
        return id == that.id &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(destination, that.destination) &&
                Objects.equals(accommodations, that.accommodations) &&
                Objects.equals(bookingReservation, that.bookingReservation) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, destination, accommodations, bookingReservation, notes);
    }
}