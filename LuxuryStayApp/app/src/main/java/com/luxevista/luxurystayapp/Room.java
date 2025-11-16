package com.luxevista.luxurystayapp;

import androidx.core.util.Pair;

public class Room {
    private String type;
    private String description;
    private double price;
    private String imageUrl;

    // Add fields for selected dates
    private Long checkInDate;
    private Long checkOutDate;

    public Room() {}

    public Room(String type, String description, double price, String imageUrl) {
        this.type = type;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and setters here
    public String getType() { return type; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    public void setSelectedDates(Pair<Long, Long> dates) {
        if (dates != null) {
            this.checkInDate = dates.first;
            this.checkOutDate = dates.second;
        } else {
            this.checkInDate = null;
            this.checkOutDate = null;
        }
    }
    public Long getCheckInDate() { return checkInDate; }
    public Long getCheckOutDate() { return checkOutDate; }
}
