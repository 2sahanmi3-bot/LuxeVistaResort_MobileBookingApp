package com.luxevista.luxurystayapp;

import java.util.ArrayList;
import java.util.List;

public class ServicesRepository {
    public static List<Service> getServices() {
        List<Service> services = new ArrayList<>();
        services.add(new Service("spa", "Spa", "Relaxing spa treatments.", "spa"));
        services.add(new Service("dining", "Dining", "Fine dining experience.", "dining"));
        services.add(new Service("cabana", "Cabanas", "Private poolside cabanas.", "cabanas"));
        services.add(new Service("tour", "Tours", "Local sightseeing tours.", "tours"));
        return services;
    }
} 