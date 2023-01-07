package com.example.upark.helpers;

import androidx.activity.ComponentActivity;

import java.util.HashSet;
import java.util.Set;

public class S {
    public static final Set<ComponentActivity> activities = new HashSet<>();
    public static void addActivity(ComponentActivity activity){
        activities.add(activity);
    }
}
