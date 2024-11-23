package com.example.wandersync.model;

import java.util.List;
import java.util.Objects;

public class TravelPost {

    private String travelPlanId; // Unique identifier for the travel plan
    private String userId; // User who created the travel plan
    private String startDate;
    private String endDate;
    private String notes;
    private String enteredDestination; // Entered destination as a string
    private String enteredAccommodation; // Entered accommodation as a string
    private String enteredDining; // Entered dining as a string
    private List<com.example.wandersync.model.Destination> destinations; // List of destinations for the travel plan
    private List<com.example.wandersync.model.Accommodation> accommodations; // List of accommodations for the travel plan
    private List<com.example.wandersync.model.DiningReservation> diningReservations; // List of dining reservations for the travel plan

    // Constructor
    public TravelPost(String travelPlanId, String userId, String startDate, String endDate, String notes,
                      String enteredDestination, String enteredAccommodation, String enteredDining,
                      List<com.example.wandersync.model.Destination> destinations, List<com.example.wandersync.model.Accommodation> accommodations,
                      List<com.example.wandersync.model.DiningReservation> diningReservations) {
        this.travelPlanId = travelPlanId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.enteredDestination = enteredDestination;
        this.enteredAccommodation = enteredAccommodation;
        this.enteredDining = enteredDining;
        this.destinations = destinations;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
    }

    // Default constructor for Firebase deserialization
    public TravelPost() {
        this.travelPlanId = "";
        this.userId = "";
        this.startDate = "";
        this.endDate = "";
        this.notes = "";
        this.enteredDestination = "";
        this.enteredAccommodation = "";
        this.enteredDining = "";
        this.destinations = null;
        this.accommodations = null;
        this.diningReservations = null;
    }

    // Getters
    public String getTravelPlanId() {
        return travelPlanId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getEnteredDestination() {
        return enteredDestination;
    }

    public String getEnteredAccommodation() {
        return enteredAccommodation;
    }

    public String getEnteredDining() {
        return enteredDining;
    }

    public List<com.example.wandersync.model.Destination> getDestinations() {
        return destinations;
    }

    public List<com.example.wandersync.model.Accommodation> getAccommodations() {
        return accommodations;
    }

    public List<com.example.wandersync.model.DiningReservation> getDiningReservations() {
        return diningReservations;
    }

    // Setters
    public void setTravelPlanId(String travelPlanId) {
        this.travelPlanId = travelPlanId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setEnteredDestination(String enteredDestination) {
        this.enteredDestination = enteredDestination;
    }

    public void setEnteredAccommodation(String enteredAccommodation) {
        this.enteredAccommodation = enteredAccommodation;
    }

    public void setEnteredDining(String enteredDining) {
        this.enteredDining = enteredDining;
    }

    public void setDestinations(List<com.example.wandersync.model.Destination> destinations) {
        this.destinations = destinations;
    }

    public void setAccommodations(List<com.example.wandersync.model.Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public void setDiningReservations(List<com.example.wandersync.model.DiningReservation> diningReservations) {
        this.diningReservations = diningReservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelPost that = (TravelPost) o;
        return Objects.equals(travelPlanId, that.travelPlanId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(enteredDestination, that.enteredDestination) &&
                Objects.equals(enteredAccommodation, that.enteredAccommodation) &&
                Objects.equals(enteredDining, that.enteredDining) &&
                Objects.equals(destinations, that.destinations) &&
                Objects.equals(accommodations, that.accommodations) &&
                Objects.equals(diningReservations, that.diningReservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(travelPlanId, userId, startDate, endDate, notes, enteredDestination, enteredAccommodation, enteredDining, destinations, accommodations, diningReservations);
    }
}
