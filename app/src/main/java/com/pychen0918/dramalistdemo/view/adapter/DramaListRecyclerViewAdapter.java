package com.pychen0918.dramalistdemo.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.data.Drama;

import java.util.List;

public class DramaListRecyclerViewAdapter extends RecyclerView.Adapter<DramaListRecyclerViewAdapter.ViewHolder>{
    private List<Drama> mDramaList;

    public DramaListRecyclerViewAdapter(@NonNull List<Drama> dramaList) {
        this.mDramaList = dramaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drama, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drama item = mDramaList.get(position);
        if(item != null){
            holder.itemView.setTag(item.getDramaId());

            Glide.with(holder.itemView.getContext()).load(item.getThumb()).into(holder.thumb);
            holder.name.setText(item.getName());
            holder.ratingText.setText(item.getDisplayRating());
            holder.ratingBar.setRating(item.getRating());
            holder.createdTime.setText(item.getDisplayCreatedTime());
        }
    }

    @Override
    public int getItemCount() {
        return mDramaList.size();
    }

    public void update(List<Drama> dramaList) {
        // TODO: rewrite with DiffUtil
        this.mDramaList = dramaList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView name;
        private TextView ratingText;
        private RatingBar ratingBar;
        private TextView createdTime;

        ViewHolder(View itemView) {
            super(itemView);
            this.thumb = itemView.findViewById(R.id.img_thumbnail);
            this.name = itemView.findViewById(R.id.tv_name);
            this.ratingText = itemView.findViewById(R.id.tv_rating_text);
            this.ratingBar = itemView.findViewById(R.id.rating_bar);
            this.createdTime = itemView.findViewById(R.id.tv_created_time);
        }
    }
}
