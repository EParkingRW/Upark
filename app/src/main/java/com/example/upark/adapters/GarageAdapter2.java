package com.example.upark.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.upark.databinding.SearchedParkResultBinding;
import com.example.upark.models.Garage;

import java.util.List;

public class GarageAdapter2 extends GarageAdapter {
    public GarageAdapter2(List<Garage> garages){
        super(garages);
    }

    @NonNull
    @Override
    public GarageViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_CELL){
            SearchedParkResultBinding binding = SearchedParkResultBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new GarageViewHolder2(binding);

        }
        else {
            TextView text = new TextView(parent.getContext());
            text.setText("end of result");
            return new GarageViewHolder2(text);
        }

    }
}
