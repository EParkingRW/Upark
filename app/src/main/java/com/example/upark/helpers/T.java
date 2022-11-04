package com.example.upark.helpers;

import com.example.upark.models.Garage;

import java.util.UUID;

public class T {
    public static Garage getStaticGarage(){
        Garage garage = new Garage(-1.9427915708395525, 30.059941500484385,200);
        garage.setId(getUUID());
        garage.setHourFees(200);
        garage.setName("Rubangura plaza");
        garage.setImageURL("https://lh5.googleusercontent.com/p/AF1QipMFSlW1tZzWx6aEI3hQLPqpCrhLbfdzpex1uzsF=w408-h906-k-no");
        garage.setStartTime(8);
        garage.setEndTime(21);
        garage.setDescription("Kwa Rubangura, KN 2 ST Rubangura House(108 Door underground floor, KN 2 St, Kigali, Rwanda");
        garage.setAddress("Kwa Rubangura, KN 2 ST ");



        return garage;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
