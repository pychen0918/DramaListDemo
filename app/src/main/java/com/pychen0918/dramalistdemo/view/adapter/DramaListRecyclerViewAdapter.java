package com.pychen0918.dramalistdemo.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pychen0918.dramalistdemo.model.gson.DramaData;

import java.util.List;

public class DramaListRecyclerViewAdapter extends RecyclerView.Adapter<DramaListRecyclerViewAdapter.ViewHolder>{
    private List<DramaData> mDramaList;

    public DramaListRecyclerViewAdapter(@NonNull List<DramaData> dramaList) {
        this.mDramaList = dramaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DramaData item = mDramaList.get(position);
        if(item != null){
            holder.title.setText(item.getName());
            holder.itemView.setTag(item.getDramaId());
        }
    }

    @Override
    public int getItemCount() {
        return mDramaList.size();
    }

    public void update(List<DramaData> dramaData) {
        // TODO: rewrite with DiffUtil
        this.mDramaList = dramaData;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(android.R.id.text1);
        }
    }
}
