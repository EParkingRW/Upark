package com.example.upark;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import com.example.upark.auth.LetUInActivity;
import com.example.upark.auth.ProfileFragment;
import com.example.upark.components.GoDialog;
import com.example.upark.databinding.DetailsDialogBinding;
import com.example.upark.helpers.B;
import com.example.upark.helpers.ImageLoadTask;
import com.example.upark.helpers.InfoWindowAdapter;
import com.example.upark.helpers.S;
import com.example.upark.models.Garage;
import com.example.upark.models.User;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private BottomNavigationView bottomNavigationView;
    private final String TAG = this.getClass().getSimpleName();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoDialog goDialog;
    Marker DestinationMarker;
    Marker hiddenGarageMarker;
    private Garage correspondingGarageForHiddenMarker;

    // TODO: Rename and change types of parameters
    private GoogleMap mMap;
//    private Marker myMarker;

    // dialog variables
    private Dialog dialog;
    private DetailsDialogBinding dialogBinding;

    //others
    private final Map<Marker, Garage> markerGarageMap;
    private final Map<Garage, MarkerOptions> markerOptionsMap;

    public LocationsFragment() {
        // Required empty public constructor
        markerGarageMap = new HashMap<>();
        markerOptionsMap = new HashMap<>();
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

        final ImageButton[] notificationBtn = {view.findViewById(R.id.notification_btn)};

        final Intent[] notificationIntent = {null};
        final Intent[] searchIntent = {null};

        notificationBtn[0].setOnClickListener(l ->{
            if(notificationIntent[0] == null){
                notificationIntent[0] = new Intent(this.requireActivity(), NotificationActivity.class);
            }
            requireActivity().startActivity(notificationIntent[0]);
        });
        ImageButton searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(l ->{
            if(searchIntent[0] == null){
                searchIntent[0] = new Intent(this.requireActivity(), SearchActivity.class);
            }
            startActivity(searchIntent[0]);
        });

        try {
            bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);











            addListeners();
        }catch (Exception e){
            Log.e(TAG, "Error: "+ e.getMessage());
        }



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

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
    protected void promptUserToChangeSetting(Task<LocationSettingsResponse> task){
        promptUserToChangeSetting(task, ()->{});
    }
    protected void promptUserToChangeSetting(Task<LocationSettingsResponse> task, Runnable onGPSReady){
        task.addOnSuccessListener(this.requireActivity(), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            Log.d(TAG, "task.addOnSuccessListener runs");
            onGPSReady.run();
        });

        task.addOnFailureListener(requireActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(requireActivity(),
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }
    @Override @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(requireActivity()));

        try {
            mMap.setMyLocationEnabled(true);
        }catch (Exception e){e.printStackTrace();}
        try {
            LocationRequest locationRequest = createLocationRequest();
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(this.requireActivity());
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            promptUserToChangeSetting(task, this::addListenerOnUserLocationChange);

        }catch (Exception e){Log.e(TAG, "Error: "+e.getMessage());}
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( this.requireActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                        S.setMyLocation(sydney);
                        Log.d("location", "your location is "+ sydney);
                    }
                });

        B.getInstance().onGarageReady(garage -> requireActivity().runOnUiThread(()-> {
            try {
                showGarage(garage);
            }catch (Exception exception){
                Log.e(this.getClass().getSimpleName(), exception.getMessage() + "");
            }

        }));
        mMap.setOnInfoWindowClickListener(marker -> {
            Garage garage;
            if(Objects.equals(marker, DestinationMarker)){
                garage = correspondingGarageForHiddenMarker;
            }else{
                garage = markerGarageMap.get(marker);
            }
            if(garage == null){
                return;
            }
            showGarageDialog(garage);
        });
    }
    private void showGarageDialog(Garage garage) {


        if(dialog == null){
            dialog = new Dialog(this.requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBinding = DetailsDialogBinding.inflate(getLayoutInflater());

            dialog.setContentView(dialogBinding.getRoot());


            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialogBinding.garageName.setText(garage.getName());
        dialogBinding.garageAddress.setText(garage.getAddress());
        dialogBinding.availableSlots.setText(String.valueOf(garage.getAvailableSlots().getValue()));
        dialogBinding.garagePrice.setText(String.valueOf(garage.getHourFees()));
        try {
            Consumer<Bitmap> onImageReady = (image) -> dialogBinding.garageImageView.setImageBitmap(image);
            new ImageLoadTask(garage.getImageURL(), onImageReady).execute();
        }catch (Exception e){e.printStackTrace();}

        dialogBinding.closeBtn.setOnClickListener(listener -> dialog.dismiss());
        Intent intent = new Intent(this.requireActivity(), GarageDetails.class);
        dialogBinding.detailsBtn.setOnClickListener(view -> {
            try {
                dialog.dismiss();
                intent.putExtra("garageId", garage.getId());
                requireActivity().startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        dialog.show();

    }
    public void addListenerOnUserLocationChange(){
        try {
            mMap.setOnMyLocationChangeListener(location -> {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                S.setMyLocation(latLng);
                String value = location.toString();
                Log.d(TAG, "new user Location : "+ value);
            });
        }catch (Exception e){
            Log.e(TAG, "Error: " + e.getMessage());
        }

    }
    public void showGarage(Garage garage){
        if(garage == null){
            return;
        }
        LatLng sydney = new LatLng(garage.getLatitude(), garage.getLongitude());

        Log.d("GPSTag", "the use location gotten ");
        MarkerOptions markerOptions = new MarkerOptions()
                .position(sydney).title(garage.getId())
                .icon(BitmapFromVector(this.requireContext(), R.drawable.ic_baseline_local_parking_24));
        Marker myMarker = mMap.addMarker(markerOptions);
        markerGarageMap.put(myMarker, garage);
        markerOptionsMap.put(garage, markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
        if(myMarker != null){
            myMarker.showInfoWindow();
        }
    }
    private void addListeners() {
        AtomicReference<ProfileFragment> profileFragment = new AtomicReference<>(null);

        AtomicReference<Intent> letUInIntent = new AtomicReference<>(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.meMenuItem){
                if(B.getInstance().getToken() == null){
                    if(letUInIntent.get() == null){
                        letUInIntent.set(new Intent(this.requireActivity(), LetUInActivity.class));
                    }
                    startActivity(letUInIntent.get());
                } else {
                    if (profileFragment.get() == null){
                        profileFragment.set(com.example.upark.auth.ProfileFragment.newInstance(new User()));
                    }
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment.get()).commit();
                }
            }else if(id == R.id.goMenuItem){
                if(goDialog == null){
                    goDialog = new GoDialog(this.requireActivity(), this::startRouting);
                }
                goDialog.show();
            }
            return true;
        });
    }
    public void startRouting(Garage garage){
        goDialog.close();
        if(hiddenGarageMarker != null){
            hiddenGarageMarker.setVisible(true);

        }
        if(DestinationMarker != null){
            DestinationMarker.remove();
        }
        markerGarageMap.forEach((marker, garageM) ->{
            if(Objects.equals(garage, garageM)){
                marker.setVisible(false);
                correspondingGarageForHiddenMarker = garageM;
                hiddenGarageMarker = marker;
            }
        });
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(garage.getLatitude(), garage.getLongitude())).title("Destination");
        markerOptions.icon(BitmapFromVector(this.requireContext(), R.drawable.ic_baseline_my_location_36));
        DestinationMarker = mMap.addMarker(markerOptions);
        try {
            Objects.requireNonNull(DestinationMarker).setTitle(garage.getId());
        }catch (Exception e){
            Log.e(TAG, "Error: "+e.getMessage());
        }
    }
}