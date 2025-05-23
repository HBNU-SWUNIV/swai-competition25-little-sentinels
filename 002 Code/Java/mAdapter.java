package com.example.growvision;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HarvestArea> dataset;
    int category;

    public mAdapter(ArrayList<HarvestArea> dataset, int category) {
        this.dataset = dataset;
        this.category = category;
    }

    @Override
    public int getItemViewType(int position) {
        if (category == 1)
            return 1;
        else if (category == 2)
            return 2;
        else if (category == 3)
            return 3;
        else
            return 4;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        if (category == 4) {
            v = li.inflate(R.layout.area_recycler_item, parent, false);
            return new AreaManageVH(v);
        }
        else {
            v = li.inflate(R.layout.stat_recycler_item, parent, false);
            return new FarmStatVH(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (category == 4) {
            AreaManageVH holder2 = (AreaManageVH) holder;
            holder2.btn.setText(dataset.get(position).getArea_name());
            holder2.btn.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), AreaInfoActivity.class);
                intent.putExtra("pos", holder.getAdapterPosition());
                view.getContext().startActivity(intent);
            });
        }
        else {
            FarmStatVH holder2 = (FarmStatVH) holder;
            String area = dataset.get(position).getArea_name() + ":";
            String value = null;

            if (category == 1)
                value = String.valueOf(dataset.get(position).getTotal_num());
            else if (category == 2)
                value = String.valueOf(dataset.get(position).getRipe_num());
            else if (category == 3)
                value = String.valueOf(dataset.get(position).getUnripe_num());

            holder2.textView1.setText(area);
            holder2.textView2.setText(value);
        }
    }

    @Override
    public int getItemCount() {
        if (dataset != null)
            return dataset.size();
        else
            return 0;
    }
    static class FarmStatVH extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        public FarmStatVH(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.stat_name_tv);
            textView2 = itemView.findViewById(R.id.stat_value_tv);
        }
    }
    static class AreaManageVH extends RecyclerView.ViewHolder {
        Button btn;
        public AreaManageVH(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.select_area_btn);
        }
    }

}
