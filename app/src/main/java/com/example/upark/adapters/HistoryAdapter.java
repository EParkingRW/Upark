package com.example.upark.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.databinding.HistroyCardBinding;
import com.example.upark.databinding.NotificationCardBinding;
import com.example.upark.models.History;

import java.util.List;
import java.util.function.Consumer;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private static final int VIEW_TYPE_FOOTER = 1;
    private static final int VIEW_TYPE_CELL = 2;
    private final List<History> histories;
    private Consumer<History> onClickListener;

    public void setListener(Consumer<History> listener){
        onClickListener = listener;
    }

    public HistoryAdapter(List<History> histories) {
        this.histories = histories;
    }

    @Override
    public int getItemViewType(int position) {
        // this will help to add view at the end of default value
        // full reference see at footer comment [1]
        return (position == histories.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_CELL){
            HistroyCardBinding binding = HistroyCardBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new HistoryViewHolder(binding);

        }
        else {
            TextView text = new TextView(parent.getContext());
            text.setText("end of result");
            return new HistoryViewHolder(text);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        // to avoid null reference of questions arraylist because value we add to the getItemCout() function
        // reference [1] #check in footer comment


        if (position != histories.size()) {
            final History history = histories.get(position);
            holder.bind(history, ()-> onClickListener.accept(history));
        }

    }

    @Override
    public int getItemCount() {
        return histories.size() + 1;
    }
}