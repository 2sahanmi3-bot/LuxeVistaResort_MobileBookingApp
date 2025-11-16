package com.luxevista.luxurystayapp;

import java.util.ArrayList;
import java.util.List;

public class EventNotificationsRepository {
    public List<EventNotification> getEventNotifications(String userId) {
        List<EventNotification> notifications = new ArrayList<>();
        notifications.add(new EventNotification("Upcoming Booking", "Your room is booked for June 10-12.", System.currentTimeMillis()));
        notifications.add(new EventNotification("Special Discount", "Exclusive 15% off for your next stay!", System.currentTimeMillis()));
        notifications.add(new EventNotification("Resort Event", "Join us for the beach party this Saturday!", System.currentTimeMillis()));
        // Fetch personalized notifications for userId if needed
        return notifications;
    }
} 