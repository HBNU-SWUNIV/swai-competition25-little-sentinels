package com.example.growvision;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView profileIcon;

    // Drawer 안의 뷰들
    private TextView userEmail, userNickname, languageTextView;
    private View logoutButton, changeLanguageBtn;

    static FirebaseFirestore db;
    static ArrayList<HarvestArea> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) DrawerLayout 및 햄버거 아이콘 참조
        drawerLayout = findViewById(R.id.main);
        profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 2) Drawer 내부 뷰 참조
        userEmail        = findViewById(R.id.userEmail);
        userNickname     = findViewById(R.id.userNickname);
        languageTextView = findViewById(R.id.languageTextView);
        logoutButton     = findViewById(R.id.logoutButton);
        changeLanguageBtn= findViewById(R.id.changeLanguageBtn);

        // 3) 로그인된 사용자 및 언어 표시
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail.setText("Email: " + user.getEmail());
            userNickname.setText("Nickname: " + user.getDisplayName());
        }
        String lang = getSharedPreferences("AppSettings", MODE_PRIVATE)
                .getString("language", "한국어");
        languageTextView.setText("Language: " + lang);

        // 4) 드로어 안의 버튼 처리
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            drawerLayout.closeDrawer(GravityCompat.END);
            startActivity(new Intent(this, AuthSelectionActivity.class));
            finish();
        });
        changeLanguageBtn.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            startActivity(new Intent(this, LanguageSelectActivity.class));
        });

        // 5) Firestore 인스턴스 및 데이터 로드
        db = FirebaseFirestore.getInstance();
        get_data_from_firebase();
    }

    /** XML onClick 속성으로 연결된 메서드들 **/
    public void onFarmStatClicked(View view) {
        startActivity(new Intent(this, FarmStatActivity.class));
    }

    public void onZoneManageClicked(View view) {
        startActivity(new Intent(this, AreaManageActivity.class));
    }

    /** Firestore에서 수확 구역 데이터를 불러와 dataset에 저장 */
    static void get_data_from_firebase() {
        dataset = new ArrayList<>();
        db.collection("harvest_area")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String area_name  = doc.getString("area_name");
                            long total_num    = doc.getLong("total_num");
                            long ripe_num     = doc.getLong("ripe_num");
                            long unripe_num   = doc.getLong("unripe_num");
                            String rail_code  = doc.getString("rail_code");
                            String rail_path  = doc.getString("rail_path");
                            dataset.add(new HarvestArea(
                                    area_name, total_num, ripe_num, unripe_num, rail_code, rail_path
                            ));
                        }
                    }
                });
    }

    /** 새로운 수확 구역 데이터를 Firestore에 추가 */
    static void add_data_to_firebase(String s1, String s2, String s3) {
        Map<String,Object> area = new HashMap<>();
        area.put("area_name",  s1);
        area.put("total_num",  0L);
        area.put("ripe_num",   0L);
        area.put("unripe_num", 0L);
        area.put("rail_code",  s2);
        area.put("rail_path",  s3);
        db.collection("harvest_area")
                .document(s1)
                .set(area);
    }
}
