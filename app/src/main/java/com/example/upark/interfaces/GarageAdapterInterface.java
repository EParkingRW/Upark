package com.example.upark.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.models.Garage;

import java.util.List;
import java.util.function.Consumer;

public interface GarageAdapterInterface {
    RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter();
    void setListener(Consumer<Garage> consumer);
    void notifyDataSetChanged();
    List<Garage> getGarages();
}
