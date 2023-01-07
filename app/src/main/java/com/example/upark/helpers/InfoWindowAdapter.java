package com.example.upark.helpers;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.example.upark.databinding.ParkingMarkerBinding;
import com.example.upark.models.Garage;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View view;
    private final String TAG = this.getClass().getSimpleName();
    private final ParkingMarkerBinding binding;
    private FragmentActivity aContext;

    @SuppressLint("InflateParams")
    public InfoWindowAdapter(FragmentActivity aContext) {
        this.aContext = aContext;
        //        LayoutInflater inflater = (LayoutInflater) aContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        view = inflater.inflate(R.layout.parking_marker, null);
        binding = ParkingMarkerBinding.inflate(aContext.getLayoutInflater());
        view = binding.getRoot();
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {

        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }else {
            marker.showInfoWindow();
        }
        return view;
    }

    @Override
    public View getInfoWindow(@NonNull final Marker marker) {
        String garageId = marker.getTitle();
        Garage garage = B.getInstance().quickFindGarage(garageId);

        try {
            garage.getAvailableSlots().observe(aContext, integer -> {
                binding.availableSpaces.setText(String.valueOf(integer));
                Log.d(TAG, "new Value" + integer);
            });
            binding.availableSpaces.setText(String.valueOf(garage.getAvailableSlots().getValue()));
        }catch (Exception e){
            Log.e(TAG, "ERROR :" +e.getMessage());
        }
        Log.d(TAG, "garageId : "+garageId);

        return view;
    }
}