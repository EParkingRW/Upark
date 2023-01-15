package com.example.upark.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.databinding.HistroyCardBinding;
import com.example.upark.models.History;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = HistoryViewHolder.class.getSimpleName();
    private final HistroyCardBinding binding;
    private final View view;

    public HistoryViewHolder(@NonNull HistroyCardBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        view = null;
    }
    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        binding = null;
    }
    public HistroyCardBinding getBinding(){
        return binding;
    }
    public void bind(final History history){
        if(binding == null){
            return;
        }
        binding.title.setText(history.historyType.getDescription());
        binding.message.setText(history.message);
    }
    public void bind(final History history, Runnable onClick){
        if(binding == null){
            view.setOnClickListener(v -> onClick.run());
            return;
        }
        bind(history);
        binding.getRoot().setOnClickListener(v -> onClick.run());
    }

}