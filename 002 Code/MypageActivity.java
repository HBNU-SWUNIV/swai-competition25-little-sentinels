package com.example.growvision;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MypageActivity extends AppCompatActivity {

    private TextView userEmail, userNickname, languageTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        userEmail = findViewById(R.id.userEmail);
        userNickname = findViewById(R.id.userNickname);
        languageTextView = findViewById(R.id.languageTextView);
        logoutButton = findViewById(R.id.logoutButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail.setText("Email: " + user.getEmail());
            userNickname.setText("Nickname: " + user.getDisplayName());
        }

        String selectedLanguage = getSharedPreferences("AppSettings", MODE_PRIVATE)
                .getString("language", "한국어");

        languageTextView.setText("Language: " + selectedLanguage);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MypageActivity.this, LoginActivity.class));
            finish();
        });
    }
}
