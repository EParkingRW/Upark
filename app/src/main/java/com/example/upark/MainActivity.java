package com.example.upark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upark.auth.LetUInActivity;
import com.example.upark.helpers.B;
import com.example.upark.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, com.example.upark.LocationsFragment.newInstance()).commit();

            Intent serviceIntent = new Intent(this, BackendService.class);
            startService(serviceIntent);










            addListeners();
        }catch (Exception e){
            Log.e(TAG, "Error: "+ e.getMessage());
        }
    }

    private void addListeners() {
        AtomicReference<Intent> letUInIntent = new AtomicReference<>(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.homeMenuItem){
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, com.example.upark.LocationsFragment.newInstance()).commit();
            }else if(id == R.id.meMenuItem){
                if(B.getInstance().getToken() == null){
                    if(letUInIntent.get() == null){
                        letUInIntent.set(new Intent(this, LetUInActivity.class));
                    }
                    startActivity(letUInIntent.get());
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, com.example.upark.auth.ProfileFragment.newInstance(new User())).commit();
                }
            }
            return true;
        });
    }
}