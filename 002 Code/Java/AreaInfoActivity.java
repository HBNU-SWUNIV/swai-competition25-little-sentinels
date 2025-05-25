package com.example.growvision;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.growvision.databinding.AreaInfoBinding;

import java.util.ArrayList;

public class AreaInfoActivity extends Activity {
    private AreaInfoBinding areaInfoBinding;
    private ArrayList<HarvestArea> dataset;
    private int pos;
    private int len_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaInfoBinding = AreaInfoBinding.inflate(getLayoutInflater());
        setContentView(areaInfoBinding.getRoot());

        // 데이터와 포지션 초기화
        dataset = MainActivity.dataset;
        pos = getIntent().getIntExtra("pos", 0);
        len_data = dataset.size();

        // 최초 화면 세팅
        set_info(pos);

        // 뒤로가기 버튼
        areaInfoBinding.back.setOnClickListener(view -> finish());

        // 이전/다음 버튼
        areaInfoBinding.previousBtn.setOnClickListener(view -> {
            pos = (pos - 1 + len_data) % len_data;
            set_info(pos);
        });
        areaInfoBinding.nextBtn.setOnClickListener(view -> {
            pos = (pos + 1) % len_data;
            set_info(pos);
        });

        // AI 버튼
        areaInfoBinding.aiBtn.setOnClickListener(view -> callGemini(dataset, pos));
    }

    private void set_info(int pos) {
        HarvestArea area = dataset.get(pos);
        areaInfoBinding.areaName.setText(area.getArea_name());
        areaInfoBinding.railCode.setText(area.getRail_code());
        areaInfoBinding.railPath.setText(area.getRail_path());
        areaInfoBinding.total.setText(String.valueOf(area.getTotal_num()));
        areaInfoBinding.ripe.setText(String.valueOf(area.getRipe_num()));
        areaInfoBinding.unripe.setText(String.valueOf(area.getUnripe_num()));
    }

    private void callGemini(ArrayList<HarvestArea> dataset, int pos) {
        Intent intent = new Intent(AreaInfoActivity.this, GeminiActivity.class);
        HarvestArea area = dataset.get(pos);
        intent.putExtra("AreaName", area.getArea_name());
        intent.putExtra("RailCode", area.getRail_code());
        intent.putExtra("RailPath", area.getRail_path());
        intent.putExtra("TotalNum", area.getTotal_num());
        intent.putExtra("RipeNum", area.getRipe_num());
        intent.putExtra("UnripeNum", area.getUnripe_num());
        intent.putExtra("Task", 1);
        startActivity(intent);
    }
}
