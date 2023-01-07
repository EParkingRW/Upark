package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.databinding.Observable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upark.databinding.ParkingLotDetailsBinding;
import com.example.upark.databinding.ParkingMarkerBinding;
import com.example.upark.helpers.B;
import com.example.upark.helpers.ImageLoadTask;
import com.example.upark.models.Garage;

import java.util.Objects;

public class GarageDetails extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    ParkingLotDetailsBinding binding;
    Garage garage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ParkingLotDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button close_btn = findViewById(R.id.close_btn);

        Intent intent = getIntent();
        try {
            String garageId = intent.getStringExtra("garageId");
            garage =  B.getInstance().quickFindGarage(garageId);

            setFieldValues(garage);

        }catch (Exception e){
            Log.d(this.getClass().getSimpleName(), "error: "+e.getMessage());
        }
        close_btn.setOnClickListener(v ->finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Objects.requireNonNull(garage).updateAvailableSlots(this::runOnUiThread);
        }catch (Exception e){Log.e(TAG, "Error: "+ e.getMessage());}

    }

    public void setFieldValues(Garage garage){
        Runnable fillData = () ->{
            try {

                binding.garageName.setText(garage.getName());
                binding.garageAddress.setText(garage.getAddress());
                binding.distance.setText(R.string._kg);
                binding.workingRange.setText(garage.getWorkingTime());
                binding.garageDescription.setText(garage.getDescription());
                binding.pricePerHour.setText(garage.getHourFeesDisplay());
                binding.availableSlots.setText(String.valueOf(garage.getAvailableSlots().getValue()));
                binding.totalSlots.setText(String.valueOf(garage.getSlots()));


                Consumer<Bitmap> onImageRead = (image) -> binding.garageImageView.setImageBitmap(image);
                new ImageLoadTask(garage.getImageURL(), onImageRead).execute();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "Error: "+e.getMessage());

            }
        };

        garage.getAvailableSlots().observe(this, integer -> {
            Log.d(TAG, "Available slots: " + integer);
            binding.availableSlots.setText(String.valueOf(integer));
        });
        fillData.run();

    }
}