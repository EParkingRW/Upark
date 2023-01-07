package com.example.upark;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.adapters.GarageAdapter;
import com.example.upark.customViews.CustomEditText;
import com.example.upark.customViews.DrawableClickListener;
import com.example.upark.databinding.SeachFilterDialogBinding;
import com.example.upark.helpers.B;
import com.example.upark.helpers.S;
import com.example.upark.models.Garage;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private CustomEditText searchEditText;
    private GarageAdapter garageAdapter;
    private final ArrayList<Garage> garageToDisplay;
    SeachFilterDialogBinding dialogBinding;

//    dialog variables




    private Dialog dialog;
    private final Filters filter;
    public SearchActivity(){
        filter = new Filters();
        garageToDisplay = new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        S.addActivity(this);
        try {
            garageAdapter = new GarageAdapter(garageToDisplay);
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
            RecyclerView garageListRecycler = findViewById(R.id.garageListRecycler);

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
        addListerOnSearch(searchEditText);
        addListerAndPopulateGarages();
    }

    private void addListerAndPopulateGarages() {
        B.getInstance().onGarageReady(this::addIfMeetFilter);
    }
    private void addIfMeetFilter(Garage garage){
        if(isMeetFilters(garage)){
            garageToDisplay.add(garage);
        }
    }
    private boolean isMeetFilters(Garage garage){
        if(garage == null){
            return false;
        }
        boolean check = garage.getDistanceInMeter() <= filter.distanceRange;
        if(!meetSearch(garage)){
            check = false;
        }
        return check;

    }
    public boolean meetSearch(Garage garage){
        return garage.contains(filter.searchString);
    }

    public void addListerOnSearch(EditText searchInput){
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "searchText:" + s.toString());
                filter.searchString = s.toString();
                runFilter();
                garageAdapter.notifyDataSetChanged();
            }
        });
    }
    public void runFilter(){
        garageToDisplay.removeIf(each -> !isMeetFilters(each));
        B.getInstance().getGarages().stream().filter(this::isMeetFilters).forEach(each -> {
            if(!garageToDisplay.contains(each)){
                garageToDisplay.add(each);
            }
        });
        runSort();
    }
    public void runSort(){
        switch (filter.sortBy){
            case DISTANCE:
                sortByDistance();
                break;
            case LOWER_PRICE:
                sortByLowerPrice();
                break;
            case SLOTS_AVAILABLE:
                sortBySlotAvailable();
                break;
            default:
                break;
        }
    }
    private void showFilterDialog(){
        if(dialog == null){
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBinding = SeachFilterDialogBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());



            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            initiateBehaviorAndValues();
        }

        dialog.show();

        dialog.setOnDismissListener(dialog -> applyFilters());
    }

    private String getDisplayActiveDistance(){
        return filter.distanceRange +" m";
    }
    private void initiateBehaviorAndValues() {
        dialogBinding.rangeInput.setMax(filter.maxDistanceRange);
        dialogBinding.activeDistance.setText(getDisplayActiveDistance());
        changeButtonColor();
        dialogBinding.sortByAvailableSlots.setOnClickListener(v -> {
            changeButtonColor(dialogBinding.sortByAvailableSlots);
            filter.sortBy = SortBy.SLOTS_AVAILABLE;
        });
        dialogBinding.sortByDistance.setOnClickListener(v -> {
            changeButtonColor(dialogBinding.sortByDistance);
            filter.sortBy = SortBy.DISTANCE;
        });
        dialogBinding.sortByLowerPrice.setOnClickListener(v -> {
            changeButtonColor(dialogBinding.sortByLowerPrice);
            filter.sortBy = SortBy.LOWER_PRICE;
        });
        dialogBinding.resetButton.setOnClickListener(v -> {
            filter.reset();
            dialog.dismiss();
            searchEditText.setText("");
            changeButtonColor();
        });
        dialogBinding.applyFilter.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.rangeInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               filter.distanceRange = progress;
               Log.d(TAG, "DistanceFromRange:" + progress);
                dialogBinding.activeDistance.setText(getDisplayActiveDistance());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { /*This method is called when the user starts dragging the slider.*/}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {/*This method is called when the user stops dragging the slider.*/}
        });

    }
    @SuppressLint("NotifyDataSetChanged")
    private void applyFilters(){
        runFilter();
        garageAdapter.notifyDataSetChanged();
    }
    private void changeMaterialButtonToLight(MaterialButton activeButton){
        activeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        activeButton.setTextColor(ContextCompat.getColor(this, R.color.main));
    }
    private void changeButtonColor(){
        changeButtonColor(null);
    }
    private void changeButtonColor(MaterialButton activeButton){
        if(activeButton == null){
            changeMaterialButtonToLight(dialogBinding.sortByDistance);
            changeMaterialButtonToLight(dialogBinding.sortByLowerPrice);
            changeMaterialButtonToLight(dialogBinding.sortByAvailableSlots);
            return;
        }
        activeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.main));
        activeButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        if(activeButton != dialogBinding.sortByDistance){
            changeMaterialButtonToLight(dialogBinding.sortByDistance);
        }
        if(activeButton != dialogBinding.sortByLowerPrice){
            changeMaterialButtonToLight(dialogBinding.sortByLowerPrice);
        }
        if(activeButton != dialogBinding.sortByAvailableSlots){
            changeMaterialButtonToLight(dialogBinding.sortByAvailableSlots);
        }

    }

    private void sortByDistance(){
        garageToDisplay.sort(Comparator.comparingInt(Garage::getDistanceInMeter));
    }
    private void sortByLowerPrice(){
        garageToDisplay.sort(Comparator.comparingDouble(Garage::getHourFees));
    }
    private void sortBySlotAvailable(){
        garageToDisplay.sort((o1, o2) -> o2.getAvailableSlotsValue() - o1.getAvailableSlotsValue());
    }

    enum SortBy{DISTANCE, SLOTS_AVAILABLE, LOWER_PRICE, NO_SORT}
    static class Filters{
        public SortBy sortBy;
        public int distanceRange;
        public int maxDistanceRange;
        public String searchString;
        public Filters(){
            reset();
        }
        public void reset(){
            sortBy = SortBy.NO_SORT;
            maxDistanceRange = 2000;
            distanceRange = maxDistanceRange;
            searchString = "";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}