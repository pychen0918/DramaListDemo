package com.pychen0918.dramalistdemo.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.databinding.ItemDramaBinding;
import com.pychen0918.dramalistdemo.model.data.Drama;

import java.util.ArrayList;
import java.util.List;

public class DramaListRecyclerViewAdapter extends RecyclerView.Adapter<DramaListRecyclerViewAdapter.ViewHolder>
        implements Filterable{
    private List<Drama> mDramaList;
    private List<Drama> mDramaListFiltered;
    private View.OnClickListener mOnClickListener;

    public DramaListRecyclerViewAdapter(@NonNull List<Drama> dramaList) {
        this.mDramaList = dramaList;
        this.mDramaListFiltered = dramaList;
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
        Drama item = mDramaListFiltered.get(position);
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
        return mDramaListFiltered.size();
    }

    public void update(List<Drama> dramaList) {
        // TODO: rewrite with DiffUtil
        this.mDramaList = dramaList;
        this.mDramaListFiltered = dramaList;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mOnClickListener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String string = charSequence.toString();
                if(string.isEmpty()){
                    mDramaListFiltered = mDramaList;
                }
                else{
                    List<Drama> list = new ArrayList<>();
                    for(Drama item : mDramaList){
                        if(item.getName().toLowerCase().contains(string.toLowerCase())) {
                            list.add(item);
                        }
                    }
                    mDramaListFiltered = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDramaListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDramaListFiltered = (List<Drama>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDramaBinding binding;
        private ImageView thumb;

        ViewHolder(ItemDramaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.thumb = binding.getRoot().findViewById(R.id.img_thumbnail);
        }

        void bind(Drama data){
            binding.setDrama(data);
            binding.executePendingBindings();
        }
    }
}
