package com.example.upark.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.upark.MainActivity;
import com.example.upark.MapsActivity;
import com.example.upark.R;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login_btn);

        addListeners();
    }

    private void addListeners() {
        try {
            final Intent[] loginIntent = {null};
            login_btn.setOnClickListener(view -> {
                if(loginIntent[0] == null){
                    loginIntent[0] = new Intent(this, MainActivity.class);
                }
                this.startActivity(loginIntent[0]);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}