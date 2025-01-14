package com.dan.naari;

import com.google.firebase.firestore.GeoPoint;

public class Complaint {
    private String incidentType;
    private java.util.Date dateAndTime;
    private GeoPoint location;
    private String description;
    private String userId;

    // Required default constructor for Firebase
    public Complaint() {}

    // Constructor with all fields
    public Complaint(String incidentType, java.util.Date dateAndTime, GeoPoint location,
                     String description, String userId) {
        this.incidentType = incidentType;
        this.dateAndTime = dateAndTime;
        this.location = location;
        this.description = description;
        this.userId = userId;
    }

    // Getters and Setters
    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public java.util.Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(java.util.Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}