package com.example.growvision;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.*;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, verifyPasswordInput, nicknameInput;
    private TextView passwordMatchStatus;
    private Button signupConfirmButton;
    private FirebaseAuth mAuth;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        verifyPasswordInput = findViewById(R.id.verifyPasswordInput);
        nicknameInput = findViewById(R.id.nicknameInput);
        passwordMatchStatus = findViewById(R.id.passwordMatchStatus);
        signupConfirmButton = findViewById(R.id.signupConfirmButton);

        mAuth = FirebaseAuth.getInstance();

        verifyPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!passwordInput.getText().toString().equals(verifyPasswordInput.getText().toString())) {
                    passwordMatchStatus.setText("비밀번호가 일치하지 않습니다.");
                } else {
                    passwordMatchStatus.setText("");
                }
            }
        });

        // ① 로그인 링크 바인딩
        loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(v -> {
            // 로그인 화면으로 이동
            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            finish();
        });

        signupConfirmButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String verifyPassword = verifyPasswordInput.getText().toString().trim();
            String nickname = nicknameInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(verifyPassword) || TextUtils.isEmpty(nickname)) {
                Toast.makeText(this, "모든 항목을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(verifyPassword)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates =
                                        new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nickname)
                                                .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                                                // 언어 선택 화면으로 이동
                                                Intent intent = new Intent(this, LanguageSelectActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(this, "회원가입 실패: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
