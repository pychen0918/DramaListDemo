package com.pychen0918.dramalistdemo.view;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.view.adapter.DramaListRecyclerViewAdapter;
import com.pychen0918.dramalistdemo.viewmodel.DramaListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DramaListRecyclerViewAdapter mDramaListRecyclerViewAdapter;
    private SearchView mSearchView;
    private MenuItem mSearchViewMenuItem;
    private SharedPreferences mSharedPreferences;
    private DramaListViewModel mDramaListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            getSupportActionBar().setTitle(R.string.title_main);
        }

        // Initial shared preference
        mSharedPreferences = getSharedPreferences("drama_list_demo_pref", MODE_PRIVATE);

        // Initial ViewModel, and start to observe drama list changes
        String pathId = this.getString(R.string.data_source_path);
        String queryString = mSharedPreferences.getString("query", "");
        mDramaListViewModel = ViewModelProviders.of(this).get(DramaListViewModel.class);
        mDramaListViewModel.setPathId(pathId);
        mDramaListViewModel.setQueryString(queryString);
        mDramaListViewModel.getFilteredDramaList().observe(this, new Observer<List<Drama>>() {
            @Override
            public void onChanged(@Nullable List<Drama> dramaData) {
                if(dramaData != null && mDramaListRecyclerViewAdapter != null){
                    mDramaListRecyclerViewAdapter.update(dramaData);
                }
            }
        });

        // Initial recyclerview and adapter
        RecyclerView dramaListRecyclerView = findViewById(R.id.drama_list_recycler_view);
        dramaListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dramaListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDramaListRecyclerViewAdapter = new DramaListRecyclerViewAdapter(new ArrayList<Drama>());
        mDramaListRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dramaId = (int) view.getTag();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", dramaId);
                startActivity(intent);
            }
        });
        dramaListRecyclerView.setAdapter(mDramaListRecyclerViewAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mSharedPreferences.edit().putString("query", mSearchQuery).apply();
        mSharedPreferences.edit().putString("query", mDramaListViewModel.getQueryString()).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchViewMenuItem = menu.findItem(R.id.action_search_filter);
        mSearchView = (SearchView) mSearchViewMenuItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        // Set search view title
        String queryString = mDramaListViewModel.getQueryString();
        if(queryString != null && !queryString.isEmpty()){
            mSearchViewMenuItem.expandActionView();
            mSearchView.setQuery(queryString, false);
            mSearchView.clearFocus();
        }

        // Search text changes
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mDramaListViewModel.setQueryString(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search_filter:
                return true;
        }

        // The home click event
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Collapse searchview if it is expanded
        if(mSearchView!=null && !mSearchView.isIconified()){
            if(mSearchViewMenuItem!=null)
                mSearchViewMenuItem.collapseActionView();
            return;
        }

        super.onBackPressed();
    }
}
