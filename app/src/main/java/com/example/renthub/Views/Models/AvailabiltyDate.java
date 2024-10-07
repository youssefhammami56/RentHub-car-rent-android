package com.example.renthub.Views.Models;

public class AvailabiltyDate {
    String id;
    String startDate;
    String endDate;
    String carId;

    public AvailabiltyDate(String id, String startDate, String endDate, String carId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carId = carId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}
