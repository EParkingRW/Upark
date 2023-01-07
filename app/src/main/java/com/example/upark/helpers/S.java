package com.example.upark.helpers;

import androidx.activity.ComponentActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

public class S {
    private static final Set<ComponentActivity> activities = new HashSet<>();
    public static Set<ComponentActivity> getActivities(){
        return activities;
    }
    public static void addActivity(ComponentActivity activity){
        activities.add(activity);
    }

    private static LatLng myLocation = null;

    public static void setMyLocation(LatLng location){
        myLocation = location;
        T.updateSubscribers();
    }
    public static LatLng getMyLocation(){
        return myLocation;
    }
}
