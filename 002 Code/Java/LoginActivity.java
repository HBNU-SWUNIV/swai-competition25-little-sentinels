package com.example.growvision;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;                           // <-- 추가
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar; // <-- 추가
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginConfirmButton;
    private TextView signupLink;
    private FirebaseAuth mAuth;
    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rootLayout = findViewById(R.id.rootLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginConfirmButton = findViewById(R.id.loginConfirmButton);
        signupLink = findViewById(R.id.signupLink);

        mAuth = FirebaseAuth.getInstance();

        loginConfirmButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                // 여전히 간단한 입력 체크만 Snackbar로 표시
                Snackbar.make(rootLayout,
                                "Please enter your email address and password.",
                                Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(
                                    LoginActivity.this,
                                    MainActivity.class
                            ));
                            finish();
                        } else {
                            Snackbar.make(rootLayout,
                                            "Login failed: " + task.getException().getMessage(),
                                            Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        signupLink.setOnClickListener(view -> {
            startActivity(new Intent(this, CreateAccountActivity.class));
            finish();
        });
    }
}
