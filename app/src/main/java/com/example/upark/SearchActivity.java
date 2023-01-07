package com.example.upark;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.adapters.GarageAdapter;
import com.example.upark.customViews.CustomEditText;
import com.example.upark.customViews.DrawableClickListener;
import com.example.upark.helpers.B;
import com.example.upark.helpers.S;
import com.google.android.material.button.MaterialButton;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private CustomEditText searchEditText;
    private RecyclerView garageListRecycler;
    private GarageAdapter garageAdapter;

//    dialog variables
    private SeekBar rangeDistanceInput;
    private MaterialButton sortByDistance;
    private MaterialButton sortByAvailableSlots;
    private MaterialButton sortByLowerPrice;
    private Button resetButton;
    private Button applyFilterBtn;




    private Dialog dialog;
    private final Filters filter;
    public SearchActivity(){
        filter = new Filters();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        S.addActivity(this);
        try {
            garageAdapter = new GarageAdapter(B.getInstance().getGarages());
            Intent intent = new Intent(this, GarageDetails.class);

            garageAdapter.setListener(garage -> {
                try {
                    intent.putExtra("garageId", garage.getId());
                    startActivity(intent);
                }catch (Exception e){
                    Log.e(TAG,"Error: "+ e.getMessage());
                    e.printStackTrace();
                }});


            Toolbar toolbar = findViewById(R.id.appBar);
            garageListRecycler = findViewById(R.id.garageListRecycler);

            toolbar.setNavigationOnClickListener(v -> finish());


            searchEditText = findViewById(R.id.editTextSearch);

            searchEditText.setDrawableClickListener(target -> {
                if (target == DrawableClickListener.DrawablePosition.RIGHT) {
                    showFilterDialog();
                }
            });

//        garageListRecycler.setHasFixedSize(true);
            garageListRecycler.setLayoutManager(new LinearLayoutManager(this));
            garageListRecycler.setVerticalScrollBarEnabled(true);
            garageListRecycler.setAdapter(garageAdapter);
        }catch (Exception e){Log.e(TAG, "Error: "+e.getMessage());}


    }
    private void showFilterDialog(){
        if(dialog == null){
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.seach_filter_dialog);

            rangeDistanceInput = dialog.findViewById(R.id.rangeInput);
            sortByDistance = dialog.findViewById(R.id.sortByDistance);
            sortByAvailableSlots = dialog.findViewById(R.id.sortByAvailableSlots);
            sortByLowerPrice = dialog.findViewById(R.id.sortByLowerPrice);
            resetButton = dialog.findViewById(R.id.resetButton);
            applyFilterBtn = dialog.findViewById(R.id.applyFilter);


            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            initiateBehaviorAndValues();
        }

        dialog.show();
    }

    private void initiateBehaviorAndValues() {
        rangeDistanceInput.setMax(filter.maxDistanceRange);
        sortByAvailableSlots.setOnClickListener(v -> {
            changeButtonColor(sortByAvailableSlots);
            filter.sortBy = SortBy.SLOTS_AVAILABLE;
        });
        sortByDistance.setOnClickListener(v -> {
            changeButtonColor(sortByDistance);
            filter.sortBy = SortBy.DISTANCE;
        });
        sortByLowerPrice.setOnClickListener(v -> {
            changeButtonColor(sortByLowerPrice);
            filter.sortBy = SortBy.LOWER_PRICE;
        });
        resetButton.setOnClickListener(v -> {
            filter.reset();
            dialog.dismiss();
        });
        applyFilterBtn.setOnClickListener(v -> {
            dialog.dismiss();
            applyFilters(filter);
        });
        rangeDistanceInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               filter.maxDistanceRange = progress;
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { /*This method is called when the user starts dragging the slider.*/}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {/*This method is called when the user stops dragging the slider.*/}
        });

    }
    private void applyFilters(Filters filter){

    }
    private void changeButtonColor(MaterialButton activeButton){
        activeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.main));
        activeButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        if(activeButton != sortByDistance){
            sortByDistance.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            sortByDistance.setTextColor(ContextCompat.getColor(this, R.color.main));
        }
        if(activeButton != sortByLowerPrice){
            sortByLowerPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            sortByLowerPrice.setTextColor(ContextCompat.getColor(this, R.color.main));
        }
        if(activeButton != sortByAvailableSlots){
            sortByAvailableSlots.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            sortByAvailableSlots.setTextColor(ContextCompat.getColor(this, R.color.main));
        }

    }
    enum SortBy{DISTANCE, SLOTS_AVAILABLE, LOWER_PRICE}
    static class Filters{
        public SortBy sortBy;
        public int distanceRange;
        public int maxDistanceRange;
        public Filters(){
            reset();
        }
        public void reset(){
            sortBy = SortBy.DISTANCE;
            maxDistanceRange = 100;
            distanceRange = maxDistanceRange;
        }
    }
}