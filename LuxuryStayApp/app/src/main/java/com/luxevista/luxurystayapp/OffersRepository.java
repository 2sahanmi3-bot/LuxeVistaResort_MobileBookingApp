package com.luxevista.luxurystayapp;

import java.util.ArrayList;
import java.util.List;

public class OffersRepository {
    private static final List<Offer> offers = new ArrayList<>();
    static {
        offers.add(new Offer("Spa Wellness Retreat", "Relax and rejuvenate with our spa package.", "spa_wellness_retreat"));
        offers.add(new Offer("Early Bird Special", "Book early and save big!", "early_bird_special"));
        offers.add(new Offer("Family Fun Pack", "Enjoy family activities all weekend.", "family_fun_pack"));
        offers.add(new Offer("Romantic Dinner", "25% off on candlelight dinner for two.", "romantic_dinner"));
        offers.add(new Offer("Suite Upgrade", "Upgrade to a suite for just LKR50 extra per night.", "suit_upgrade"));
        offers.add(new Offer("Spa Summer Deal", "Get 30% off on all spa bookings.", "spa_summer_deal"));
        offers.add(new Offer("Beach Sunrise", "Enjoy a sunrise breakfast by the beach.", "offer_beach"));
        offers.add(new Offer("Family Pool Fun", "Kids swim free with every family booking!", "offer_pool"));
    }
    public static List<Offer> getAllOffers() {
        return offers;
    }
    public static Offer getOfferById(int id) {
        // No id field, so just return the first offer for demo purposes
        if (!offers.isEmpty()) return offers.get(0);
        return null;
    }
    public List<Offer> getHotelOffers() {
        List<Offer> offers = new ArrayList<>();
        offers.add(new Offer("Summer Special", "Get 20% off on all rooms!", "offer1.jpg"));
        offers.add(new Offer("Spa Discount", "Enjoy a relaxing spa at half price.", "offer2.jpg"));
        // Add more offers as needed
        return offers;
    }
} 