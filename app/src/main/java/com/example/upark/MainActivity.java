package com.example.upark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.upark.auth.LetUInActivity;
import com.example.upark.auth.ProfileFragment;
import com.example.upark.helpers.B;
import com.example.upark.helpers.S;
import com.example.upark.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private final String TAG = this.getClass().getSimpleName();
    LocationsFragment locationsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        S.addActivity(this);
        try {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);
            locationsFragment = com.example.upark.LocationsFragment.newInstance();

            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, locationsFragment).commit();

            Intent serviceIntent = new Intent(this, BackendService.class);
            startService(serviceIntent);










            addListeners();
        }catch (Exception e){
            Log.e(TAG, "Error: "+ e.getMessage());
        }
    }

    private void addListeners() {
        AtomicReference<HistoryFragment> historyFragment = new AtomicReference<>(null);
        AtomicReference<ProfileFragment> profileFragment = new AtomicReference<>(null);

        AtomicReference<Intent> letUInIntent = new AtomicReference<>(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.homeMenuItem){
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, locationsFragment).commit();
            }else if(id == R.id.meMenuItem){
                if(B.getInstance().getToken() == null){
                    if(letUInIntent.get() == null){
                        letUInIntent.set(new Intent(this, LetUInActivity.class));
                    }
                    startActivity(letUInIntent.get());
                } else {
                    if (profileFragment.get() == null){
                        profileFragment.set(com.example.upark.auth.ProfileFragment.newInstance(new User()));
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment.get()).commit();
                }
            }else if(id == R.id.historyMenuItem){
                if(historyFragment.get() == null){
                    historyFragment.set(com.example.upark.HistoryFragment.newInstance());
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, historyFragment.get()).commit();
            }
            return true;
        });
    }
}