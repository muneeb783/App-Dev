package com.example.wandersync.model;

public class Accommodation {
    private String location;
    private String hotelName;
    private String checkInDate;    // Store check-in as String
    private String checkOutDate;// Store check-out as String
    private String checkInOut;
    private String numRooms;
    private String roomType;
    private boolean isExpired;     // Field to indicate if the reservation is expired
    private String userID;         // User ID to link to the user

    // No-argument constructor required for Firebase
    public Accommodation() {}

    public Accommodation(String userID, String location, String hotelName, String checkInDate, String checkOutDate, String numRooms, String roomType) {
        this.userID = userID;
        this.location = location;
        this.hotelName = hotelName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = numRooms;
        this.roomType = roomType;
        this.isExpired = false;
        this.checkInOut = "Check-in: " + checkInDate + " Check-out: " + checkOutDate;
    }

    // Getters and setters for all fields
    public String getLocation() { return location; }
    public String getHotelName() { return hotelName; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public String getNumRooms() { return numRooms; }
    public String getRoomType() { return roomType; }
    public boolean isExpired() { return isExpired; }
    public String getUserID() { return userID; }
    public String getCheckInOut() {return checkInOut; }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
