package com.luxevista.luxurystayapp;

public class ServiceBooking {
    private String userId;
    private String serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceImageUrl;
    private long date;
    private String timeSlot;
    private long timestamp;
    private String status; // "pending", "confirmed", "cancelled"

    public ServiceBooking() {}

    public ServiceBooking(String userId, String serviceId, String serviceName, String serviceDescription, String serviceImageUrl, long date, String timeSlot, long timestamp) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceImageUrl = serviceImageUrl;
        this.date = date;
        this.timeSlot = timeSlot;
        this.timestamp = timestamp;
        this.status = "pending";
    }

    public String getUserId() { return userId; }
    public String getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public String getServiceDescription() { return serviceDescription; }
    public String getServiceImageUrl() { return serviceImageUrl; }
    public long getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public long getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 