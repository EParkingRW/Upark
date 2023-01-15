package com.example.upark;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upark.helpers.S;

public class NotificationActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        S.addActivity(this);
        setContentView(R.layout.activity_notification);

    }
}