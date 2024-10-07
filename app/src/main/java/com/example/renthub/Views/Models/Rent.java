package com.example.renthub.Views.Models;

public class Rent {
    String carName;
    String id;
    String carUID;
    String userID;
    String startDate;
    String endDate;
    String total;
    String location;
    String status;


    public Rent(String id, String carUID, String userID, String startDate, String endDate, String total, String location, String status, String carName) {
        this.id = id;
        this.carUID = carUID;
        this.userID = userID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
        this.location = location;
        this.status = status;
        this.carName = carName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarUID() {
        return carUID;
    }

    public void setCarUID(String carUID) {
        this.carUID = carUID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
