package com.example.upark.models;

import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;

public class Garage implements Serializable {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String imageURL;
    private String CompanyId; // company
    private double hourFees;
    private int startTime; // ex: 8
    private int endTime; // ex : 9
    private String address;
    private String description;
    private int slots;
    private int takenSlots;
    private final MutableLiveData<Integer> availableSlots;

    public Garage(double latitude, double longitude, int slots){
        this.latitude = latitude;
        this.longitude = longitude;
        this.slots = slots;
        this.availableSlots = new MutableLiveData<>(slots);
    }
    public Garage(){
        this.availableSlots = new MutableLiveData<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public double getHourFees() {
        return hourFees;
    }

    public void setHourFees(double hourFees) {
        this.hourFees = hourFees;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getTakenSlots() {
        return takenSlots;
    }

    public void setTakenSlots(int takenSlots) {
        this.takenSlots = takenSlots;
    }

    public MutableLiveData<Integer> getAvailableSlots() {
        return availableSlots;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingTime() {
        return startTime + ":00 - " + endTime + ":00";
    }
    public String getHourFeesDisplay(){
        return "RWF " + hourFees;
    }
}
