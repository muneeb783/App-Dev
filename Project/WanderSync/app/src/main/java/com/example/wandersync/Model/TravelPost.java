package com.example.wandersync.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;

public class TravelPost implements Serializable {
    //added descriptions to this class..amm
    private String travelPlanId;
    private String userId;
    private String startDate;
    private String endDate;
    private String notes;
    private String enteredDestination;
    private String enteredAccommodation;
    private String enteredDining;
    private List<com.example.wandersync.Model.Destination> destinations;
    private List<com.example.wandersync.Model.Accommodation> accommodations;
    private List<com.example.wandersync.Model.DiningReservation> diningReservations;
    private String rating;
    private List<String> allStr;
    // Constructor
    public TravelPost(List<String> allStr,
                      String enteredAccommodation, String enteredDining,
                      List<com.example.wandersync.Model.Destination> destinations,
                      List<com.example.wandersync.Model.Accommodation> accommodations,
                      List<com.example.wandersync.Model.DiningReservation> diningReservations,
                      String rating) {
        this.travelPlanId = allStr.get(0);
        this.userId = allStr.get(1);
        this.startDate = allStr.get(2);
        this.endDate = allStr.get(3);
        this.notes = allStr.get(4);
        this.enteredDestination = allStr.get(5);
        this.enteredAccommodation = enteredAccommodation;
        this.enteredDining = enteredDining;
        this.destinations = destinations;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
        this.rating = rating;
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
        this.rating = "";
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

    public String getRating() {
        return this.rating;
    }

    public List<com.example.wandersync.Model.Destination> getDestinations() {
        return destinations;
    }

    public List<com.example.wandersync.Model.Accommodation> getAccommodations() {
        return accommodations;
    }

    public List<com.example.wandersync.Model.DiningReservation> getDiningReservations() {
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

    public void setRating(String rating) {
        this.rating = rating;
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

    public void setDestinations(List<com.example.wandersync.Model.Destination> destinations) {
        this.destinations = destinations;
    }

    public void setAccommodations(List<com.example.wandersync.Model.Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public void setDiningReservations(List<com.example.
            wandersync.Model.DiningReservation> diningReservations) {
        this.diningReservations = diningReservations;
    }
    public List<String> getDestinationNames() {
        List<String> destinationNames = new ArrayList<>();
        if (destinations != null) {
            for (com.example.wandersync.Model.Destination destination : destinations) {
                destinationNames.add(destination.getName());
            }
        }
        return destinationNames;
    }

    public List<String> getAccommodationNames() {
        List<String> accommodationNames = new ArrayList<>();
        if (accommodations != null) {
            for (com.example.wandersync.Model.Accommodation accommodation : accommodations) {
                accommodationNames.add(accommodation.getHotelName());
            }
        }
        return accommodationNames;
    }

    public List<String> getDiningReservationNames() {
        List<String> diningNames = new ArrayList<>();
        if (diningReservations != null) {
            for (com.example.wandersync.Model.DiningReservation reservation : diningReservations) {
                diningNames.add(reservation.getWebsite());
            }
        }
        return diningNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TravelPost that = (TravelPost) o;
        return Objects.equals(travelPlanId, that.travelPlanId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(notes, that.notes)
                && Objects.equals(enteredDestination, that.enteredDestination)
                && Objects.equals(enteredAccommodation, that.enteredAccommodation)
                && Objects.equals(enteredDining, that.enteredDining)
                && Objects.equals(destinations, that.destinations)
                && Objects.equals(accommodations, that.accommodations)
                && Objects.equals(diningReservations, that.diningReservations)
                && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(travelPlanId, userId,
                startDate, endDate, notes, enteredDestination,
                enteredAccommodation, enteredDining, destinations,
                accommodations, diningReservations, rating);
    }
}
