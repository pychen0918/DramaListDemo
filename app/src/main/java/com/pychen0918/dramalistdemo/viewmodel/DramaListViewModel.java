package com.pychen0918.dramalistdemo.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.DataRepository;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.model.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class DramaListViewModel extends AndroidViewModel {
    private final DataRepository mDataRepository;
    private final MutableLiveData<String> mPathId;
    private final MediatorLiveData<List<Drama>> mFilteredDramaList;
    private final MutableLiveData<String> mQuery;

    public DramaListViewModel(@NonNull Application application) {
        super(application);

        // Initial data repository
        mDataRepository = DataRepository.getInstance(AppDatabase.getInstance(application), application.getResources().getString(R.string.data_source_url));

        // Initial live data
        mPathId = new MutableLiveData<>();
        final LiveData<List<Drama>> mDramaList = Transformations.switchMap(mPathId, new Function<String, LiveData<List<Drama>>>() {
            @Override
            public LiveData<List<Drama>> apply(String pathId) {
                if (pathId == null || pathId.isEmpty()) {
                    return null;
                }

                return mDataRepository.getDramaList(pathId);
            }
        });

        /*
         * mFilteredDramaList observes two live data: query string and the raw drama list
         * It needs to update itself when either one of this changed
         */
        mQuery = new MutableLiveData<>();
        mFilteredDramaList = new MediatorLiveData<>();
        mFilteredDramaList.addSource(mQuery, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String query) {
                List<Drama> dramaList = mDramaList.getValue();
                if(query!=null && dramaList!=null){
                    if(query.isEmpty()){
                        mFilteredDramaList.setValue(dramaList);
                    }
                    else {
                        List<Drama> filteredList = new ArrayList<>();
                        for (Drama item : dramaList) {
                            if (item.getName().toLowerCase().contains(query)) {
                                filteredList.add(item);
                            }
                        }
                        mFilteredDramaList.setValue(filteredList);
                    }
                }
            }
        });
        mFilteredDramaList.addSource(mDramaList, new Observer<List<Drama>>() {
            @Override
            public void onChanged(@Nullable List<Drama> dramaList) {
                String query = mQuery.getValue();
                if(query!=null && dramaList!=null){
                    if(query.isEmpty()){
                        mFilteredDramaList.setValue(dramaList);
                    }
                    else {
                        List<Drama> filteredList = new ArrayList<>();
                        for (Drama item : dramaList) {
                            if (item.getName().toLowerCase().contains(query)) {
                                filteredList.add(item);
                            }
                        }
                        mFilteredDramaList.setValue(filteredList);
                    }
                }
            }
        });
    }

    public LiveData<List<Drama>> getFilteredDramaList(){
        return mFilteredDramaList;
    }

    public void setPathId(final String pathId){
        mPathId.setValue(pathId);
    }

    public void setQueryString(final String queryString){
        mQuery.setValue(queryString);
    }

    public String getQueryString(){
        return mQuery.getValue();
    }
}
