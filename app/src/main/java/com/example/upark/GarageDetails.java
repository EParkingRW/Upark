package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upark.helpers.B;
import com.example.upark.helpers.ImageLoadTask;
import com.example.upark.models.Garage;

public class GarageDetails extends AppCompatActivity {

    // from view variables

    private ImageView garage_image;
    private TextView garage_name;
    private TextView garage_address;
    private TextView garage_distance;
    private TextView working_time;
    private TextView garage_description;
    private TextView price_per_hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_lot_details);

        garage_image = findViewById(R.id.garage_image_view);
        garage_name = findViewById(R.id.garage_name);
        garage_address = findViewById(R.id.garage_address);
        garage_distance = findViewById(R.id.distance);
        working_time = findViewById(R.id.working_range);
        garage_description = findViewById(R.id.garage_description);
        price_per_hour = findViewById(R.id.price_per_hour);
        Button close_btn = findViewById(R.id.close_btn);

        Intent intent = getIntent();
        try {
            String garageId = intent.getStringExtra("garageId");
            Garage garage =  B.getInstance().quickFindGarage(garageId);

            setFieldValues(garage);

        }catch (Exception e){
            Log.d(this.getClass().getSimpleName(), "error: "+e.getMessage());
        }
        close_btn.setOnClickListener(v ->finish());
    }
    public void setFieldValues(Garage garage){
        try {
            garage_name.setText(garage.getName());
            garage_address.setText(garage.getAddress());
            garage_distance.setText(R.string._kg);
            working_time.setText(garage.getWorkingTime());
            garage_description.setText(garage.getDescription());
            price_per_hour.setText(garage.getHourFeesDisplay());



            Consumer<Bitmap> onImageRead = (image) -> garage_image.setImageBitmap(image);
            new ImageLoadTask(garage.getImageURL(), onImageRead).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}