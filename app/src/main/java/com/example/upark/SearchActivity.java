package com.example.upark;

import android.app.Dialog;
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

import com.example.upark.customViews.CustomEditText;
import com.example.upark.customViews.DrawableClickListener;
import com.google.android.material.button.MaterialButton;

import java.security.SecureRandom;

enum SortBy{DISTANCE, SLOTS_AVAILABLE, LOWER_PRICE}
class Filters{
    public SortBy sortBy;
    public int distanceRange;
    public int maxRange;
    public Filters(){
        sortBy = SortBy.DISTANCE;
        maxRange = 100;
        distanceRange = maxRange/2;
    }
}
public class SearchActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private CustomEditText searchEditText;

//    dialog variables
    private SeekBar rangeInput;
    private MaterialButton sortByDistance;
    private MaterialButton sortByAvailableSlots;
    private MaterialButton sortByLowerPrice;
    private Button restButton;
    private Button applyFilter;




    private Dialog dialog;
    private final Filters filter;
    public SearchActivity(){
        filter = new Filters();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        searchEditText = findViewById(R.id.editTextSearch);

        searchEditText.setDrawableClickListener(target -> {
            if (target == DrawableClickListener.DrawablePosition.RIGHT) {
                showFilterDialog();
            }
        });

    }
    private void showFilterDialog(){
        if(dialog == null){
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.seach_filter_dialog);

            rangeInput = dialog.findViewById(R.id.rangeInput);
            sortByDistance = dialog.findViewById(R.id.sortByDistance);
            sortByAvailableSlots = dialog.findViewById(R.id.sortByAvailableSlots);
            sortByLowerPrice = dialog.findViewById(R.id.sortByLowerPrice);
            restButton = dialog.findViewById(R.id.restButton);
            applyFilter = dialog.findViewById(R.id.applyFilter);


            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            initiateBehaviorAndValues();
        }

        dialog.show();
    }

    private void initiateBehaviorAndValues() {
        rangeInput.setMax(filter.maxRange);
        sortByAvailableSlots.setOnClickListener(v -> {
            changeButtonColor(sortByAvailableSlots);
        });
        sortByDistance.setOnClickListener(v -> {
            changeButtonColor(sortByDistance);
        });
        sortByLowerPrice.setOnClickListener(v -> {
            changeButtonColor(sortByLowerPrice);
        });


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
}