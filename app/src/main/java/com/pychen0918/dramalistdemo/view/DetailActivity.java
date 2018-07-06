package com.pychen0918.dramalistdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.viewmodel.DramaDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int id = -1;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getInt("id");
        }

        // Initial ViewModel, and start to observe drama list changes
        String pathId = this.getString(R.string.data_source_path);
        DramaDetailViewModel dramaDetailViewModel = ViewModelProviders.of(this).get(DramaDetailViewModel.class);
        dramaDetailViewModel.getDrama(pathId, id).observe(this, new Observer<Drama>() {
            @Override
            public void onChanged(@Nullable Drama drama) {
                // TODO: display loading screen and empty page
                if(drama!=null){
                    Glide.with(DetailActivity.this).load(drama.getThumb()).into((ImageView) findViewById(R.id.img_preview));
                    ((TextView) findViewById(R.id.tv_name)).setText(drama.getName());
                    ((TextView) findViewById(R.id.tv_rating_text)).setText(drama.getDisplayRating());
                    ((RatingBar) findViewById(R.id.rating_bar)).setRating(drama.getRating());
                    ((TextView) findViewById(R.id.tv_total_view)).setText(drama.getDisplayTotalViews());
                    ((TextView) findViewById(R.id.tv_created_time)).setText(drama.getDisplayCreatedTime());
                }
            }
        });
    }
}
