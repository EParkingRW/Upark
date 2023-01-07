package com.example.upark.helpers;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.example.upark.helpers.backend.GarageBackend;
import com.example.upark.models.Garage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


/* this class will contains backend codes
* */
public class B {
    private final ObservableArrayList<Garage> garages;
    private static B instance;
    private final ListChangedCallback listChangedCallback;
    private B(){
        garages = new ObservableArrayList<>();
        addGarage(T.getStaticGarage());
        listChangedCallback = new ListChangedCallback();
        garages.addOnListChangedCallback(listChangedCallback);
    }
    public static B getInstance(){
        if(instance == null){
            instance = new B();
        }
        return instance;
    }

    public void addGarage(Garage garage){
        if(garages.contains(garage)){
            Log.d(this.getClass().getSimpleName(), "garage Exist :" +garage);
            Garage existingGarage = garages.stream().filter(each -> each.getId().equals(garage.getId())).findAny().orElse(null);
            Objects.requireNonNull(existingGarage).copy(garage);
        }else {
            garages.add(garage);
        }

    }
    public void onGarageReady(Consumer<Garage> garageReceiver) {
        if(garageReceiver == null){
            return;
        }
        listChangedCallback.addCallback(garageReceiver);
        garages.forEach(garageReceiver);
    }
    public void initialLoadGarages(Context context, Consumer<Garage> onGarage){
        Consumer<List<Garage>> onReady = (garages_) ->{
            garages.clear();
            garages.addAll(garages_);
            garages.forEach(onGarage);
        };
        GarageBackend.getGarages(context, onReady);
    }
    public ObservableArrayList<Garage> getGarages(){
        return this.garages;
    }

    public Garage quickFindGarage(String garageId) {
        return garages.stream().filter(garage -> Objects.equals(garageId, garage.getId())).findAny().orElse(null);
    }

    public String getToken(){
        return null;
    }


    private static class ListChangedCallback extends ObservableList.OnListChangedCallback<ObservableList<Garage>>{
        final List<Consumer<Garage>> garageReceivers;
        public ListChangedCallback(){
            this.garageReceivers = new ArrayList<>();
        }
        @Override
        public void onChanged(ObservableList<Garage> sender) {
            // This method is called whenever the list is changed.
        }

        @Override
        public void onItemRangeChanged(ObservableList<Garage> sender, int positionStart, int itemCount) {
            // This method is called when one or more items in the list have changed.
        }

        @Override
        public void onItemRangeInserted(ObservableList<Garage> sender, int positionStart, int itemCount) {
            // This method is called when one or more items have been added to the list.
            int i = positionStart;
            Log.d("positionStart_itemCount_size", positionStart+"_"+itemCount+"_"+sender.size());
            while (i < positionStart+itemCount){
                final int index = i;
                Log.d("index_while", index +"");
                garageReceivers.forEach(each -> {
                    Log.d("callBackRuns", sender.get(index).toString());
                    each.accept(sender.get(index));
                });
                ++i;
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList<Garage> sender, int fromPosition, int toPosition, int itemCount) {
            // This method is called when one or more items have been moved within the list.
        }

        @Override
        public void onItemRangeRemoved(ObservableList<Garage> sender, int positionStart, int itemCount) {
            // This method is called when one or more items have been removed from the list.
        }
        public void addCallback(Consumer<Garage> consumer){
            this.garageReceivers.add(consumer);
        }
    }
}