package com.pychen0918.dramalistdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.databinding.ActivityDetailBinding;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.viewmodel.DramaDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = -1;
        Bundle bundle = getIntent().getExtras();
        Uri deeplink = getIntent().getData();
        if(deeplink!=null){
            id = parseDeepLink(deeplink);
        }
        else if(bundle!=null){
            id = bundle.getInt("id");
        }

        // Initial ViewModel, and start to observe drama list changes
        String pathId = this.getString(R.string.data_source_path);
        DramaDetailViewModel dramaDetailViewModel = ViewModelProviders.of(this).get(DramaDetailViewModel.class);
        dramaDetailViewModel.getDrama(pathId, id).observe(this, new Observer<Drama>() {
            @Override
            public void onChanged(@Nullable Drama drama) {
                if(drama!=null){
                    ActivityDetailBinding binding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.activity_detail);
                    binding.setDrama(drama);
                }
                else{
                    setContentView(R.layout.empty_page);
                }
            }
        });
    }

    private int parseDeepLink(Uri deeplink) {
        String path = deeplink.getPath();
        int id = -1;
        if(path.startsWith("/dramas")){
            id = Integer.valueOf(deeplink.getLastPathSegment());
        }
        return id;
    }
}
