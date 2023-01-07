package com.example.upark.helpers;

import android.util.Log;

import androidx.core.util.Consumer;

import com.example.upark.models.Garage;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class T {
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
                    if(count.get() == S.activities.size()){
                        isSuccess.accept(false);
                    }
                }else {
                    complete.set(true);
                    isSuccess.accept(true);
                }
            };
            S.activities.forEach(each->{
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
}
