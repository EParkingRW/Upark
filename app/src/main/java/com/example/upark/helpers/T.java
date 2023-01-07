package com.example.upark.helpers;

import android.util.Log;

import androidx.core.util.Consumer;

import com.example.upark.helpers.functions.Haversine;
import com.example.upark.models.Garage;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class T {
    private final static ArrayList<java.util.function.Consumer<LatLng>> userLocationSubscribers = new ArrayList<>();

    public static Garage getStaticGarage(){
        Garage garage = new Garage(-1.9427915708395525, 30.059941500484385,200,getUUID());
        garage.setHourFees(200);
        garage.setName("Rubangura plaza");
        garage.setImageURL("https://lh5.googleusercontent.com/p/AF1QipMFSlW1tZzWx6aEI3hQLPqpCrhLbfdzpex1uzsF=w408-h906-k-no");
        garage.setOpeningTime("8:00");
        garage.setClosingTime("21:00");
        garage.setDescription("Kwa Rubangura, KN 2 ST Rubangura House(108 Door underground floor, KN 2 St, Kigali, Rwanda");
        garage.setAddress("Kwa Rubangura, KN 2 ST ");



        return garage;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    public static void runOnUiThread(Runnable runnable){
        runOnUiThread(runnable, aBoolean -> Log.d("TClass", "isCopy: "+ aBoolean));
    }
    public static void runOnUiThread(Runnable runnable, Consumer<Boolean> isSuccess){
        try {
            final AtomicReference<Boolean> complete = new AtomicReference<>(false);
            AtomicInteger count = new AtomicInteger(0);
            Consumer<Boolean> onComplete = (isComp) -> {
                if(!isComp){
                    count.incrementAndGet();
                    if(count.get() == S.getActivities().size()){
                        isSuccess.accept(false);
                    }
                }else {
                    complete.set(true);
                    isSuccess.accept(true);
                }
            };
            S.getActivities().forEach(each->{
                try {
                    if(!complete.get()){
                        each.runOnUiThread(() ->{
                            runnable.run();
                            onComplete.accept(true);
                        });

                    }
                }catch (Exception ignore){onComplete.accept(false);}
            });
        }catch (Exception e){
            isSuccess.accept(false);
        }
    }
    public static double distanceBetweenCoordinates(double startLat, double startLong,
                                  double endLat, double endLong) {
        return Haversine.distance(startLat, startLong, endLat, endLong);
    }

    public static void subScribeToUserLocation(java.util.function.Consumer<LatLng> subscriber){
        if(S.getMyLocation() != null){
            subscriber.accept(S.getMyLocation());
        }
        userLocationSubscribers.add(subscriber);
    }
    public static void updateSubscribers(){
        userLocationSubscribers.forEach(each -> each.accept(S.getMyLocation()));
    }

    public static String formatDistanceInKm(double value){
        DecimalFormat df = new DecimalFormat("#.###");
        return  df.format(value) + " km";
    }
    public static String formatDistanceInM(double value){
        DecimalFormat df = new DecimalFormat("####");
        return  df.format(value*1000) + " m";
    }
}
