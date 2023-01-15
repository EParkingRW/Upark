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
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        S.addActivity(this);

        try {
            Intent serviceIntent = new Intent(this, BackendService.class);
            startService(serviceIntent);
        }catch (Exception e){
            Log.e(TAG, "Error: " +e.getMessage());
        }
    }
}