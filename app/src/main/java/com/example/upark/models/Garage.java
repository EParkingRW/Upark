package com.example.upark.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.upark.helpers.T;
import com.example.upark.interfaces.TriConsumer;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class Garage implements Serializable {
    private final String TAG  = Garage.class.getSimpleName();
    private final String id;
    private String name;
    private double latitude;
    private double longitude;
    private String imageURL;
    private String CompanyId; // company
    private double hourFees;
    private String openingTime; // ex: 8
    private String closingTime; // ex : 9
    private String address;
    private String description;
    private int slots;
    private int takenSlots;
    private final MutableLiveData<Integer> availableSlots;
    private final MutableLiveData<Double> distanceInKm;
    private final ArrayList<TriConsumer<Garage, Integer, Integer>> availableSpaceSubscriber;

    private String userId;

    public Garage(double latitude, double longitude, int slots, String id){
        this.availableSpaceSubscriber = new ArrayList<>();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.slots = slots;
        this.availableSlots = new MutableLiveData<>(slots);
        this.distanceInKm = new MutableLiveData<>(-1.0);
        updateAvailableSlots();
        subscribeUserLocation();
    }
    public Garage(String id){
        this.availableSpaceSubscriber = new ArrayList<>();
        this.availableSlots = new MutableLiveData<>(0);
        this.takenSlots = 0;
        this.slots = 0;
        this.id = id;
        this.distanceInKm = new MutableLiveData<>(-1.0);
        subscribeUserLocation();
    }
    private void subscribeUserLocation(){
        T.subScribeToUserLocation(this::updateDistance);
    }

    public String getId() {
        return id;
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
    public int getAvailableSlotsValue(){
        Integer value = availableSlots.getValue();
        return value!=null? value:-1 ;
    }

    public void setHourFees(double hourFees) {
        this.hourFees = hourFees;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
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
        updateAvailableSlots();
    }

    public MutableLiveData<Double> getDistanceInKm() {
        return distanceInKm;
    }
    public String getDisplayDistance(){
        Double value = getDistanceInKm().getValue();
        if(value == null){
            return "";
        }
        return T.formatDistanceInM(value);
    }
    public int getDistanceInMeter(){
        Double value = distanceInKm.getValue();
        if(value == null){
            return -1;
        }
        if(value < 0){
            return -1;
        }
        return (int)(value*1000);
    }
    public void setDistanceInKm(double distanceInKm){
        this.distanceInKm.setValue(distanceInKm);
    }

    public int getTakenSlots() {
        return takenSlots;
    }

    public void setTakenSlots(int takenSlots) {
        this.takenSlots = takenSlots;
        updateAvailableSlots();
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
        return openingTime + " - " + closingTime;
    }
    public String getHourFeesDisplay(){
        return "RWF " + hourFees;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Garage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", imageURL='" + imageURL + '\'' +
                ", CompanyId='" + CompanyId + '\'' +
                ", hourFees=" + hourFees +
                ", startTime=" + openingTime +
                ", endTime=" + closingTime +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", slots=" + slots +
                ", takenSlots=" + takenSlots +
                ", availableSlots=" + availableSlots +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Garage garage = (Garage) o;
        return Objects.equals(id, garage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void copy(Garage garage) {
        Runnable runnable = () ->{
            this.name = garage.getName();
            this.latitude = garage.getLatitude();
            this.longitude = garage.getLongitude();
            this.imageURL = garage.getImageURL();
            this.CompanyId = garage.getCompanyId();
            this.hourFees = garage.getHourFees();
            this.openingTime = garage.getOpeningTime();
            this.closingTime = garage.getClosingTime();
            this.address = garage.getAddress();
            this.description = garage.getDescription();
            this.slots = garage.getSlots();
            this.takenSlots = garage.getTakenSlots();
            this.userId = garage.getUserId();
        };
        runnable.run();


        updateAvailableSlots();
    }
    public void updateAvailableSlots(Consumer<Runnable> runOnUiThread){
        int newValue = slots - takenSlots;
        int oldValue = 0;
        if (availableSlots.getValue() != null){
            oldValue = availableSlots.getValue();
        }
        if(oldValue == newValue){
            return;
        }
        int finalOldValue = oldValue;
        Runnable runnable = () -> {
            availableSlots.setValue(slots - takenSlots);
            Log.d("GarageClass", "Value: "+ availableSlots.getValue());
            try {
                availableSpaceSubscriber.forEach(each -> each.accept(this, finalOldValue, newValue));
            }catch (Exception e){
                Log.e(TAG, "Error :" + e.getMessage());
            }
        };
        runOnUiThread.accept(runnable);
    }
    public void updateAvailableSlots(){
        updateAvailableSlots(T::runOnUiThread);
    }
    public void updateDistance(LatLng latLng,Consumer<Runnable> runOnUiThread){
        if(latLng == null){
            return;
        }
        double distance = T.distanceBetweenCoordinates(
                this.getLatitude(), this.getLongitude(),
                latLng.latitude, latLng.longitude);
        Runnable runnable = ()-> setDistanceInKm(distance);
        runOnUiThread.accept(runnable);
    }
    public void updateDistance(LatLng latLng){
        updateDistance(latLng, T::runOnUiThread);
    }
    public boolean contains(String text){
        try {
            if(text == null){
                return false;
            }
            String searchString = text.toLowerCase(Locale.ROOT);
            if(this.getName().contains(searchString)){
                return true;
            }
            else if(Objects.requireNonNull(this.getAddress()).contains(searchString)){
                return true;
            }
            else if((this.getAvailableSlots().getValue()+"").contains(searchString)){
                return true;
            }
            else if((this.getDistanceInMeter()+"").contains(searchString)){
                return true;
            }
            else if((this.getHourFees()+"").contains(searchString)){
                return true;
            }
            else if((this.getDescription()).contains(searchString)){
                return true;
            }
            else return getWorkingTime().contains(searchString);


        }catch (Exception e){
            return false;
        }
    }
    public void addAvailableSpaceConsumer(TriConsumer<Garage, Integer, Integer> consumer){
        this.availableSpaceSubscriber.add(consumer);
    }
}
