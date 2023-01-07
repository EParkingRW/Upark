package com.example.upark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.R;
import com.example.upark.databinding.ParkingMarkerBinding;
import com.example.upark.databinding.SearchedGarageCardBinding;
import com.example.upark.models.Garage;

import java.util.List;
import java.util.function.Consumer;

public class GarageAdapter extends RecyclerView.Adapter<GarageViewHolder> {
    private static final int VIEW_TYPE_FOOTER = 1;
    private static final int VIEW_TYPE_CELL = 2;
    private List<Garage> garages;
    private Consumer<Garage> onClickListener;

    public void setListener(Consumer<Garage> listener){
        onClickListener = listener;
    }

    public GarageAdapter(List<Garage> garages) {
        this.garages = garages;
    }

    @Override
    public int getItemViewType(int position) {
        // this will help to add view at the end of default value
        // full reference see at footer comment [1]
        return (position == garages.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @NonNull
    @Override
    public GarageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_CELL){
            SearchedGarageCardBinding binding = SearchedGarageCardBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new GarageViewHolder(binding);

        }
        else {
            TextView text = new TextView(parent.getContext());
            text.setText("end of result");
            return new GarageViewHolder(text);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder, int position) {
        // to avoid null reference of questions arraylist because value we add to the getItemCout() function
        // reference [1] #check in footer comment


        if (position != garages.size()) {
            final Garage garage = garages.get(position);
            holder.bind(garage, ()-> onClickListener.accept(garage));
        }

    }

    @Override
    public int getItemCount() {
        return garages.size() + 1;
    }
}