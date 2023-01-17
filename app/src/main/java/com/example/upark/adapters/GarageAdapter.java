package com.example.upark.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.databinding.SearchedGarageCardBinding;
import com.example.upark.interfaces.GarageAdapterInterface;
import com.example.upark.models.Garage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class GarageAdapter extends RecyclerView.Adapter<GarageViewHolder> implements GarageAdapterInterface {
    protected static final int VIEW_TYPE_FOOTER = 1;
    protected static final int VIEW_TYPE_CELL = 2;
    protected final List<Garage> garages;
    protected Consumer<Garage> onClickListener;
    protected final Map<Integer,Consumer<Garage>> individualListener;
    protected String TAG = this.getClass().getSimpleName();

    public void setListener(Consumer<Garage> listener){
        onClickListener = listener;
    }

    public GarageAdapter(List<Garage> garages) {
        this.garages = garages;
        this.individualListener = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        // this will help to add view at the end of default value
        // full reference see at footer comment [1]
        return (position == garages.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @NonNull
    @Override
    public abstract GarageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder, int position) {
        // to avoid null reference of questions arraylist because value we add to the getItemCout() function
        // reference [1] #check in footer comment


        if (position != garages.size()) {
            final Garage garage = garages.get(position);
            holder.bind(garage, ()-> onClickListener.accept(garage));
            individualListener.forEach((id, consumer) -> {
                try {
                    Runnable runnable = () ->{
                        consumer.accept(garage);
                    };
                    holder.addIndividualListener(id,runnable);
                }catch (Exception e){
                    Log.e(TAG, "Error: "+e.getMessage());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return garages.size() + 1;
    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return this;
    }

    @Override
    public List<Garage> getGarages() {
        return garages;
    }
    public void setListener(int id, Consumer<Garage> consumer){
        individualListener.put(id, consumer);
    }
}
