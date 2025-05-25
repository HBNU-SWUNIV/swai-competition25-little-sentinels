package com.example.growvision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LanguageSelectActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private Button nextButton;
    private String selectedLanguage = "Korean";
    private String nickname, number;
    private int task;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        languageSpinner = findViewById(R.id.languageSpinner);
        nextButton = findViewById(R.id.nextButton);

        task = getIntent().getIntExtra("task", 0);
        if (task == 0) {
            nickname = getIntent().getStringExtra("nickname");
            number = getIntent().getStringExtra("number");
            db = FirebaseFirestore.getInstance();
        }

        String[] languages = {"Korean", "English", "Chinese", "Vietnam"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = languages[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        nextButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
            prefs.edit().putString("language", selectedLanguage).apply();

            if (task == 0) add_personalInfo_to_firebase();

            Intent intent = new Intent(LanguageSelectActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    void add_personalInfo_to_firebase() {
        Map<String,Object> person = new HashMap<>();
        person.put("name", nickname);
        person.put("language", selectedLanguage);
        person.put("number", number);
        db.collection("member")
                .document(nickname)
                .set(person);
    }
}
