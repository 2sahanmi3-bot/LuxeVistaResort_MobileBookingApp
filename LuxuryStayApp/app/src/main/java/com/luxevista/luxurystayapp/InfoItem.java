package com.luxevista.luxurystayapp;

public class InfoItem {
    public Offer offer;
    public Attraction attraction;

    public InfoItem(Offer offer) { this.offer = offer; }
    public InfoItem(Attraction attraction) { this.attraction = attraction; }
    public boolean isOffer() { return offer != null; }
} 