package com.example.upark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upark.helpers.backend.SocketHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, com.example.upark.LocationsFragment.newInstance()).commit();

        Intent serviceIntent = new Intent(this, BackendService.class);
        startService(serviceIntent);
//        Thread socketBackendRunner = new Thread(new SocketBackend());
//        socketBackendRunner.start();
    }
}