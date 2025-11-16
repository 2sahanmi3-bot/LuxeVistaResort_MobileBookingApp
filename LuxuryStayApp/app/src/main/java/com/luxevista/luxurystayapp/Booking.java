package com.luxevista.luxurystayapp;

public class Booking {
    private String userId;
    private String roomType;
    private String roomDescription;
    private double roomPrice;
    private String roomImageUrl;
    private long checkInDate;
    private long checkOutDate;
    private long timestamp;
    private String status; // "pending", "confirmed", "cancelled"

    public Booking() {}

    public Booking(String userId, String roomType, String roomDescription, double roomPrice, String roomImageUrl, long checkInDate, long checkOutDate) {
        this.userId = userId;
        this.roomType = roomType;
        this.roomDescription = roomDescription;
        this.roomPrice = roomPrice;
        this.roomImageUrl = roomImageUrl;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.timestamp = System.currentTimeMillis();
        this.status = "pending";
    }

    public String getUserId() { return userId; }
    public String getRoomType() { return roomType; }
    public String getRoomDescription() { return roomDescription; }
    public double getRoomPrice() { return roomPrice; }
    public String getRoomImageUrl() { return roomImageUrl; }
    public long getCheckInDate() { return checkInDate; }
    public long getCheckOutDate() { return checkOutDate; }
    public long getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 