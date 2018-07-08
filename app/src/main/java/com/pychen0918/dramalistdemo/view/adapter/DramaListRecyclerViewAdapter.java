package com.pychen0918.dramalistdemo.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.databinding.ItemDramaBinding;
import com.pychen0918.dramalistdemo.model.data.Drama;

import java.util.List;

public class DramaListRecyclerViewAdapter extends RecyclerView.Adapter<DramaListRecyclerViewAdapter.ViewHolder> {
    private List<Drama> mDramaList;
    private View.OnClickListener mOnClickListener;

    public DramaListRecyclerViewAdapter(@NonNull List<Drama> dramaList) {
        this.mDramaList = dramaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDramaBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_drama, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drama item = mDramaList.get(position);
        if(item != null){
            if(mOnClickListener!=null){
                holder.itemView.setOnClickListener(mOnClickListener);
            }
            holder.itemView.setTag(item.getDramaId());
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return mDramaList.size();
    }

    public void update(List<Drama> dramaList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DramaDiffCallback(dramaList, this.mDramaList));
        diffResult.dispatchUpdatesTo(this);
        this.mDramaList = dramaList;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mOnClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDramaBinding binding;

        ViewHolder(ItemDramaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Drama data){
            binding.setDrama(data);
            binding.executePendingBindings();
        }
    }
}
