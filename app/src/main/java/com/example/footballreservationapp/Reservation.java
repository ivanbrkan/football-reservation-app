package com.example.footballreservationapp;

public class Reservation {
    private String id;
    private String date;
    private String time;
    private String userId;
    private String status;

    public Reservation() {
    }

    public Reservation(String date, String time, String userId, String status) {
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
