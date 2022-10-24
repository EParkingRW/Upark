package com.example.upark.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.upark.R;

public class LetUInActivity extends AppCompatActivity {

    private Button sign_in;
    private Button sign_up;
    private Button sign_with_google;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        sign_in = findViewById(R.id.login_btn);
        sign_up = findViewById(R.id.sign_up_btn);
        sign_with_google = findViewById(R.id.continue_with_google);

        addListeners();
    }

    private void addListeners() {
        try {
            final Intent[] signInIntent = {null};
            sign_in.setOnClickListener(view -> {
                if(signInIntent[0] == null){
                    signInIntent[0] = new Intent(this, LoginActivity.class);
                }
                this.startActivity(signInIntent[0]);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}