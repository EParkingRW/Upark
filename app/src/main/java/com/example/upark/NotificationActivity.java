package com.example.upark;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NotificationActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        try {
            Toolbar toolbar = findViewById(R.id.appBar);
            TextView toolBarTitle = toolbar.findViewById(R.id.title);
            toolBarTitle.setText(R.string.notification);


            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }catch (Exception e){
            Log.e(TAG, "Error: " + e.getMessage());
        }

    }
}