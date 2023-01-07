package com.example.upark;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upark.helpers.B;
import com.example.upark.models.Garage;

import java.util.function.Consumer;

public class LearnVolley extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_volley);
        getData();
    }

    public void getData() {
        Consumer<Garage> onGarage = (garage -> {
            Log.d("garage_message", garage.toString());
        });
        B.getInstance().initialLoadGarages(this,onGarage);

    }
}