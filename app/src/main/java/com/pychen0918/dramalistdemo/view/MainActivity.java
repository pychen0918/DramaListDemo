package com.pychen0918.dramalistdemo.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.gson.DramaData;
import com.pychen0918.dramalistdemo.view.adapter.DramaListRecyclerViewAdapter;
import com.pychen0918.dramalistdemo.viewmodel.DramaListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DramaListRecyclerViewAdapter mDramaListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pathId = this.getString(R.string.data_source_path);
        DramaListViewModel dramaListViewModel = ViewModelProviders.of(this).get(DramaListViewModel.class);
        dramaListViewModel.getDramaList(pathId).observe(this, new Observer<List<DramaData>>() {
            @Override
            public void onChanged(@Nullable List<DramaData> dramaData) {
                if(dramaData != null && mDramaListRecyclerViewAdapter != null){
                    mDramaListRecyclerViewAdapter.update(dramaData);
                }
            }
        });

        RecyclerView dramaListRecyclerView = findViewById(R.id.drama_list_recycler_view);
        dramaListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dramaListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDramaListRecyclerViewAdapter = new DramaListRecyclerViewAdapter(new ArrayList<DramaData>());
        dramaListRecyclerView.setAdapter(mDramaListRecyclerViewAdapter);
    }
}
