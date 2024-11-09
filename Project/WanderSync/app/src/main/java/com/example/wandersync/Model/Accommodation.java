package com.example.wandersync.Model;

public class Accommodation {
    private String location;
    private String hotelName;
    private String checkInOut;
    private String numRooms;
    private String roomType;

    public Accommodation(String location, String hotelName, String checkInDate, String checkOutDate, String numRooms, String roomType) {
        this.location = location;
        this.hotelName = hotelName;
        this.checkInOut = "Check-in: " + checkInDate + " Check-out: " + checkOutDate;
        this.numRooms = numRooms;
        this.roomType = roomType;
    }

    public String getLocation() {
        return location;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getCheckInOut() {
        return checkInOut;
    }

    public String getNumRooms() {
        return numRooms;
    }

    public String getRoomType() {
        return roomType;
    }
}
