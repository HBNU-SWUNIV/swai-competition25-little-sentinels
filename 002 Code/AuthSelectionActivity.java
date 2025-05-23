package com.example.growvision;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;

public class AuthSelectionActivity extends AppCompatActivity {

    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authselection_activity_main);

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(AuthSelectionActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(AuthSelectionActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }
}
