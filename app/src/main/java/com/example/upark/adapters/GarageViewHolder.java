package com.example.upark.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.models.Garage;

public abstract class GarageViewHolder extends RecyclerView.ViewHolder{
    public GarageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bind(final Garage garage);
    public abstract void bind(final Garage garage, Runnable onClick);
}
