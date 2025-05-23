package com.example.growvision;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growvision.databinding.AreaManageBinding;

import java.util.ArrayList;

public class AreaManageActivity extends Activity {

    AreaManageBinding areaManageBinding;
    ArrayList<HarvestArea> dataset;
    RecyclerView.Adapter adapter;
    View add_dialog;
    final int AREA = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaManageBinding = AreaManageBinding.inflate(getLayoutInflater());
        setContentView(areaManageBinding.getRoot());

        dataset = MainActivity.dataset;

        Button back_btn = areaManageBinding.back;
        back_btn.setOnClickListener(view -> {
            MainActivity.get_data_from_firebase();
            finish();
        });

        Button add_area = areaManageBinding.addArea;
        add_area.setOnClickListener(view -> showDialog());

        RecyclerView recyclerView = areaManageBinding.areaRv;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new mAdapter(dataset, AREA);
        recyclerView.setAdapter(adapter);
    }

    void showDialog() {
        add_dialog = View.inflate(AreaManageActivity.this, R.layout.add_area_dlg, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(AreaManageActivity.this);
        dlg.setView(add_dialog);

        EditText dlg_area_name = add_dialog.findViewById(R.id.area_name_et);
        EditText dlg_rail_code = add_dialog.findViewById(R.id.rail_code_et);
        EditText dlg_rail_path = add_dialog.findViewById(R.id.rail_path_et);

        dlg.setPositiveButton("Save", (dialogInterface, i) -> {
            String s1 = dlg_area_name.getText().toString();
            String s2 = dlg_rail_code.getText().toString();
            String s3 = dlg_rail_path.getText().toString();
            MainActivity.add_data_to_firebase(s1, s2, s3);
            dataset.add(new HarvestArea(s1, 0,0,0, s2, s3));
            adapter.notifyItemInserted(dataset.size()-1);

        });
        dlg.setNegativeButton("Cancel", null);
        dlg.show();
    }
}
