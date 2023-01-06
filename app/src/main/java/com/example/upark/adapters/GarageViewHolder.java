package com.example.upark.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.R;
import com.example.upark.models.Garage;

import java.util.function.Consumer;

public class GarageViewHolder extends RecyclerView.ViewHolder {
    private final TextView garageName;
    private final TextView garageAddress;
    private final TextView garageDistance;
    private final TextView garagePrice;
    private View view;

    public GarageViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        garageName = itemView.findViewById(R.id.garage_name);
        garageAddress = itemView.findViewById(R.id.garage_address);
        garageDistance = itemView.findViewById(R.id.garage_distance);
        garagePrice = itemView.findViewById(R.id.garage_price);



    }
    public void bind(final Garage garage){
        garageName.setText(garage.getName());
        garageAddress.setText(garage.getAddress());
        garagePrice.setText(String.valueOf(garage.getHourFees()));
    }
    public void bind(final Garage garage, Runnable onClick){
        bind(garage);
        view.setOnClickListener(v -> onClick.run());
    }

}

