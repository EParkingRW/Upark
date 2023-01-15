package com.example.upark;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upark.adapters.GarageAdapter1;
import com.example.upark.customViews.CustomEditText;
import com.example.upark.helpers.GarageSearch;
import com.example.upark.helpers.S;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private CustomEditText searchEditText;

//    dialog variables

    public SearchActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        S.addActivity(this);
        try {
            searchEditText = findViewById(R.id.editTextSearch);


            Toolbar toolbar = findViewById(R.id.appBar);
            RecyclerView garageListRecycler = findViewById(R.id.HistoryListRecycler);

            toolbar.setNavigationOnClickListener(v -> finish());
            GarageAdapter1 garageAdapter1 = new GarageAdapter1(new ArrayList<>());
            GarageSearch garageSearch = new GarageSearch(this, searchEditText, garageAdapter1);

//        garageListRecycler.setHasFixedSize(true);
            garageListRecycler.setLayoutManager(new LinearLayoutManager(this));
            garageListRecycler.setVerticalScrollBarEnabled(true);
            garageListRecycler.setAdapter(garageSearch.getGarageAdapter());
        }catch (Exception e){Log.e(TAG, "Error: "+e.getMessage());}
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}