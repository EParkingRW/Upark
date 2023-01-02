package com.example.upark;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import com.example.upark.helpers.B;
import com.example.upark.helpers.ImageLoadTask;
import com.example.upark.helpers.InfoWindowAdapter;
import com.example.upark.models.Garage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;

    // TODO: Rename and change types of parameters
    private GoogleMap mMap;
    private Marker myMarker;

    // dialog variables
    private Dialog dialog;
    private Button close_btn;
    private Button showDetails;
    private TextView garage_name;
    private TextView garage_address;
    private ImageView garage_image_view;

    public LocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationsFragment newInstance() {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
    }

    @Override @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Async map
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return view;
    }
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(getActivity()));

        try {
            mMap.setMyLocationEnabled(true);
        }catch (Exception e){e.printStackTrace();}
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( this.requireActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                        Log.d("location", "your location is "+ sydney);
                    }
                });

        B.getInstance().onGarageReady(garage -> requireActivity().runOnUiThread(()-> showGarage(garage)));
    }
    private void showGarageDialog(Garage garage) {


        if(dialog == null){
            dialog = new Dialog(this.requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.details_dialog);

            close_btn = dialog.findViewById(R.id.close_btn);
            showDetails = dialog.findViewById(R.id.details_btn);
            garage_name = dialog.findViewById(R.id.garage_name);
            garage_address = dialog.findViewById(R.id.garage_address);
            garage_image_view = dialog.findViewById(R.id.garage_image_view);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        garage_name.setText(garage.getName());
        garage_address.setText(garage.getAddress());
        try {
            Consumer<Bitmap> onImageReady = (image) -> garage_image_view.setImageBitmap(image);
            new ImageLoadTask(garage.getImageURL(), onImageReady).execute();
        }catch (Exception e){e.printStackTrace();}

        close_btn.setOnClickListener(listener -> dialog.dismiss());
        showDetails.setOnClickListener(view -> {
            try {
                dialog.dismiss();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, ParkingPlaceDatails.newInstance(garage)).commit();
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        dialog.show();

    }

    public void showGarage(Garage garage){
        if(garage == null){
            return;
        }
        LatLng sydney = new LatLng(garage.getLatitude(), garage.getLongitude());

        Log.d("GPSTag", "the use location gotten ");
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("Marker in Sydney")
                // below line is use to add custom marker on our map.
                .icon(BitmapFromVector(this.requireContext(), R.drawable.ic_baseline_local_parking_24));
        myMarker = mMap.addMarker(markerOptions);
        mMap.setOnMarkerClickListener(marker -> {
            if(marker.equals(myMarker)){
                showGarageDialog(garage);
            }
            return false;
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
    }
}