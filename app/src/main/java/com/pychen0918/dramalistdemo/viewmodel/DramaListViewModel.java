package com.pychen0918.dramalistdemo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.DataRepository;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.model.db.AppDatabase;

import java.util.List;

public class DramaListViewModel extends AndroidViewModel {
    private final DataRepository mDataRepository;

    public DramaListViewModel(@NonNull Application application) {
        super(application);
        mDataRepository = DataRepository.getInstance(AppDatabase.getInstance(application), application.getResources().getString(R.string.data_source_url));
    }

    public LiveData<List<Drama>> getDramaList(final String pathId){
        return mDataRepository.getDramaList(pathId);
    }
}
