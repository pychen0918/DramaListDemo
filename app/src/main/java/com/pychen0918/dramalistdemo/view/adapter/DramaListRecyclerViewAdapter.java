package com.pychen0918.dramalistdemo.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.gson.DramaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DramaListRecyclerViewAdapter extends RecyclerView.Adapter<DramaListRecyclerViewAdapter.ViewHolder>{
    private List<DramaData> mDramaList;
    private final SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private final SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

    public DramaListRecyclerViewAdapter(@NonNull List<DramaData> dramaList) {
        this.mDramaList = dramaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drama, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DramaData item = mDramaList.get(position);
        if(item != null){
            holder.itemView.setTag(item.getDramaId());

            Glide.with(holder.itemView.getContext()).load(item.getThumb()).into(holder.thumb);
            holder.name.setText(item.getName());
            holder.ratingText.setText(String.format(Locale.getDefault(), "%.1f", item.getRating()));
            holder.ratingBar.setRating(item.getRating());
            String dateString = "";
            try {
                dateString = targetFormat.format(originFormat.parse(item.getCreatedAt()));
            } catch (ParseException e) {
                Log.e("DramaListAdapter", "Unable to parse created time: "+item.getCreatedAt());
                e.printStackTrace();
            }
            holder.createdTime.setText(dateString);
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
