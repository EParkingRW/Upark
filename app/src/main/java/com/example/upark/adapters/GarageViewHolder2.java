package com.example.upark.adapters;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.upark.databinding.SearchedParkResultBinding;
import com.example.upark.helpers.T;
import com.example.upark.models.Garage;

public class GarageViewHolder2 extends GarageViewHolder{
    private final String TAG = this.getClass().getSimpleName();
    private final SearchedParkResultBinding binding;
    private final View view;

    public GarageViewHolder2(@NonNull SearchedParkResultBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        view = null;
    }
    public GarageViewHolder2(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        binding = null;
    }
    public SearchedParkResultBinding getBinding(){
        return binding;
    }
    public void bind(final Garage garage){
        if(binding == null){
            return;
        }
        binding.garageName.setText(garage.getName());
        binding.garageAddress.setText(garage.getAddress());
        binding.garageDistance.setText(garage.getDisplayDistance());
        binding.availableSlots.setText(String.valueOf(garage.getAvailableSlotsValue()));

        try {
            garage.getAvailableSlots().observeForever(integer -> binding.availableSlots.setText(String.valueOf(integer)));
            garage.getDistanceInKm().observeForever(DoubleValue -> binding.garageDistance.setText(T.formatDistanceInM(DoubleValue)));
        }catch (Exception e){
            Log.e(TAG, "Error: "+e.getMessage());
            Log.e(TAG, "Error: "+e.toString());
        }
    }
    public void bind(final Garage garage, Runnable onClick){
        if(binding == null){
            view.setOnClickListener(v -> onClick.run());
            return;
        }
        bind(garage);
        binding.getRoot().setOnClickListener(v -> onClick.run());
    }

    @Override
    public void addIndividualListener(int id, Runnable onClick) {
        try {
            binding.getRoot().findViewById(id).setOnClickListener(v -> onClick.run());
        }catch (Exception e){Log.e(TAG, "Error: "+e.getMessage());}

    }
}
