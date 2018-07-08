package com.pychen0918.dramalistdemo.view;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public final class DramaBindingAdapter {
    private DramaBindingAdapter() {}

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
