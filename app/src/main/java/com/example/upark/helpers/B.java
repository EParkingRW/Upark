package com.example.upark.helpers;

import androidx.core.util.Consumer;
import androidx.databinding.ObservableArrayList;

import com.example.upark.models.Garage;


/* this class will contains backend codes
* */
public class B {
    private final ObservableArrayList<Garage> garages;
    private static B instance;
    private Consumer<Garage> garageReceiver;
    private B(){
        garages = new ObservableArrayList<>();
        garages.add(T.getStaticGarage());
    }
    public static B getInstance(){
        if(instance == null){
            instance = new B();
        }
        return instance;
    }

    public void onGarageReady(Consumer<Garage> garageReceiver) {
        if(garageReceiver == null){
            return;
        }
        this.garageReceiver = garageReceiver;
        garages.forEach(garageReceiver::accept);
    }
}
