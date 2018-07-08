package com.pychen0918.dramalistdemo.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.pychen0918.dramalistdemo.model.data.Drama;

import java.util.List;

public class DramaDiffCallback extends DiffUtil.Callback {
    private List<Drama> newList;
    private List<Drama> oldList;

    public DramaDiffCallback(@NonNull List<Drama> newList, @NonNull List<Drama> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldList.get(oldItemPosition).getDramaId().equals(newList.get(newItemPosition).getDramaId()));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
