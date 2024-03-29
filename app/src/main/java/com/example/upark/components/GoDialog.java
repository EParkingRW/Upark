package com.example.upark.components;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.upark.R;
import com.example.upark.adapters.GarageAdapter2;
import com.example.upark.databinding.GoDestinationPickBinding;
import com.example.upark.helpers.GarageSearch;
import com.example.upark.models.Garage;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GoDialog {
    private final Dialog dialog;
    protected String TAG = this.getClass().getSimpleName();

    public GoDialog(ComponentActivity activity, Consumer<Garage> startRouting){
        com.example.upark.databinding.GoDestinationPickBinding binding = GoDestinationPickBinding.inflate(activity.getLayoutInflater());
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        GarageAdapter2 garageAdapter2 = new GarageAdapter2(new ArrayList<>());
        garageAdapter2.setListener(R.id.letGoBtn, startRouting);
        GarageSearch garageSearch = new GarageSearch(activity, binding.editTextSearch, garageAdapter2);

        binding.SearchRecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.SearchRecycler.setVerticalScrollBarEnabled(true);
        binding.SearchRecycler.setAdapter(garageSearch.getGarageAdapter());
    }
    public void show(){
        dialog.show();
    }
    public void close(){
        dialog.dismiss();
    }
}
