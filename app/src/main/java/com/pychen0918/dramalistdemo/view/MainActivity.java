package com.pychen0918.dramalistdemo.view;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.view.adapter.DramaListRecyclerViewAdapter;
import com.pychen0918.dramalistdemo.viewmodel.DramaListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG_SEARCH_QUERY = "search_query";
    private DramaListRecyclerViewAdapter mDramaListRecyclerViewAdapter;
    private SearchView mSearchView;
    private MenuItem mSearchViewMenuItem;
    private String mSearchQuery = "";
    private SharedPreferences mSharedPreferences;

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

        // Initial ViewModel, and start to observe drama list changes
        String pathId = this.getString(R.string.data_source_path);
        DramaListViewModel dramaListViewModel = ViewModelProviders.of(this).get(DramaListViewModel.class);
        dramaListViewModel.getDramaList(pathId).observe(this, new Observer<List<Drama>>() {
            @Override
            public void onChanged(@Nullable List<Drama> dramaData) {
                if(dramaData != null && mDramaListRecyclerViewAdapter != null){
                    mDramaListRecyclerViewAdapter.update(dramaData);
                    mDramaListRecyclerViewAdapter.getFilter().filter(mSearchQuery);
                }
            }
        });

        // Initial recyclerview and adapter
        RecyclerView dramaListRecyclerView = findViewById(R.id.drama_list_recycler_view);
        dramaListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dramaListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDramaListRecyclerViewAdapter = new DramaListRecyclerViewAdapter(new ArrayList<Drama>());
        dramaListRecyclerView.setAdapter(mDramaListRecyclerViewAdapter);

        mSharedPreferences = getSharedPreferences("drama_list_demo_pref", MODE_PRIVATE);

        // Load query string from save instance for configuration change
        if(savedInstanceState != null){
            mSearchQuery = savedInstanceState.getString(TAG_SEARCH_QUERY);
        }
        else {
            mSearchQuery = mSharedPreferences.getString("query", "");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save query string into save instance for configuration change
        outState.putString(TAG_SEARCH_QUERY, mSearchView.getQuery().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSharedPreferences.edit().putString("query", mSearchQuery).apply();
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

        if(mSearchQuery != null && !mSearchQuery.isEmpty()){
            mSearchViewMenuItem.expandActionView();
            mSearchView.setQuery(mSearchQuery, false);
            mSearchView.clearFocus();
            mDramaListRecyclerViewAdapter.getFilter().filter(mSearchQuery);
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
                mDramaListRecyclerViewAdapter.getFilter().filter(query);
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
        if(mSearchView!=null && !mSearchView.isIconified()){
            if(mSearchViewMenuItem!=null)
                mSearchViewMenuItem.collapseActionView();
            return;
        }

        super.onBackPressed();
    }
}
