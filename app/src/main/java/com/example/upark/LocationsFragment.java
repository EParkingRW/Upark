package com.example.upark;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment implements OnMapReadyCallback, LocationListener, LocationSource {
    private FusedLocationProviderClient fusedLocationClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private GoogleMap mMap;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

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


//        binding = ActivityMapsBinding.inflate(inflater);
//        this.requireActivity().setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Async map
        mapFragment.getMapAsync(this);


        return view;
    }

    @Override @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setLocationSource(this);


        locationManager = (LocationManager) this.requireActivity().getSystemService(LOCATION_SERVICE);

        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
            }
            else
            {
                //Show an error dialog that GPS is disabled.
            }
        }
        else
        {
            //Show a generic error dialog since LocationManager is null for some reason
        }








        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( this.requireActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());

                        Log.d("GPSTag", "the use location gotten ");
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
                    }
                });

        googleMap.setOnMapClickListener(latLng -> {
            // When clicked on map
            // Initialize marker options
            MarkerOptions markerOptions=new MarkerOptions();
            // Set position of marker
            markerOptions.position(latLng);
            // Set title of marker
            markerOptions.title(latLng.latitude+" : "+latLng.longitude);
            // Remove all marker
            googleMap.clear();
            // Animating to zoom the marker
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            // Add marker on map
            googleMap.addMarker(markerOptions);
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if( mListener != null )
        {
            mListener.onLocationChanged( location );

            //Move the camera to the user's location once it's available!
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        }
    }

    @Override
    public void activate(@NonNull OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }
    @Override
    public void onProviderDisabled(String provider)
    {
        // TODO Auto-generated method stub
        Toast.makeText(this.requireActivity(), "provider disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        // TODO Auto-generated method stub
        Toast.makeText(this.requireActivity(), "provider enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        // TODO Auto-generated method stub
        Toast.makeText(this.requireActivity(), "status changed", Toast.LENGTH_SHORT).show();
    }
}