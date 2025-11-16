package com.luxevista.luxurystayapp;

import java.util.ArrayList;
import java.util.List;

public class AttractionsRepository {
    private static final List<Attraction> attractions = new ArrayList<>();
    static {
        attractions.add(new Attraction("Coral Bay Dive", "Explore marine life with scuba tours", "https://via.placeholder.com/150"));
        attractions.add(new Attraction("Sunset Grill", "Seaside dining with fresh seafood", "https://via.placeholder.com/150"));
        attractions.add(new Attraction("Old Town Museum", "Discover the region's history and culture", "https://via.placeholder.com/150"));
    }
    public static List<Attraction> getAllAttractions() {
        return attractions;
    }
    public static Attraction getAttractionById(int id) {
        // No id field, so just return the first attraction for demo purposes
        if (!attractions.isEmpty()) return attractions.get(0);
        return null;
    }
    public List<Attraction> getNearbyAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction("Water Sports", "Exciting water activities at the beach.", "watersports.jpg"));
        attractions.add(new Attraction("Beach Tours", "Guided tours of the local beaches.", "beachtour.jpg"));
        attractions.add(new Attraction("Local Restaurants", "Taste the best local cuisine.", "restaurant.jpg"));
        // Add more attractions as needed
        return attractions;
    }
} 