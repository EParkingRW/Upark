package com.example.upark.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.upark.databinding.SearchedGarageCardBinding;
import com.example.upark.models.Garage;

import java.util.List;

public class GarageAdapter1 extends GarageAdapter {
    public GarageAdapter1(List<Garage> garages) {
        super(garages);
    }


    @NonNull
    @Override
    public GarageViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_CELL){
            SearchedGarageCardBinding binding = SearchedGarageCardBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new GarageViewHolder1(binding);

        }
        else {
            TextView text = new TextView(parent.getContext());
            text.setText("end of result");
            return new GarageViewHolder1(text);
        }

    }

}