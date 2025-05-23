package com.example.growvision;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growvision.databinding.FarmStatBinding;

import java.util.ArrayList;

public class FarmStatActivity extends Activity {

    FarmStatBinding farmStatBinding;
    ArrayList<HarvestArea> dataset;
    final int TOTAL = 1;
    final int RIPE = 2;
    final int UNRIPE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        farmStatBinding = FarmStatBinding.inflate(getLayoutInflater());
        setContentView(farmStatBinding.getRoot());

        dataset = MainActivity.dataset;

        Button back_btn = farmStatBinding.back;
        back_btn.setOnClickListener(view -> {
            MainActivity.get_data_from_firebase();
            finish();
        });

        Button ai_btn = farmStatBinding.aiBtn;
        ai_btn.setOnClickListener(view -> callGemini());

        RecyclerView recyclerView1 = farmStatBinding.statRv1;
        RecyclerView recyclerView2 = farmStatBinding.statRv2;
        RecyclerView recyclerView3 = farmStatBinding.statRv3;

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);

        RecyclerView.Adapter adapter1 = new mAdapter(dataset, TOTAL);
        RecyclerView.Adapter adapter2 = new mAdapter(dataset, RIPE);
        RecyclerView.Adapter adapter3 = new mAdapter(dataset, UNRIPE);

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);
    }
    void callGemini() {
        Intent intent = new Intent(FarmStatActivity.this, GeminiActivity.class);
        startActivity(intent);
    }
}
