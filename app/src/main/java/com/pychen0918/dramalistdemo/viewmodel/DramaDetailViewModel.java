package com.pychen0918.dramalistdemo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.pychen0918.dramalistdemo.R;
import com.pychen0918.dramalistdemo.model.DataRepository;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.model.db.AppDatabase;

public class DramaDetailViewModel extends AndroidViewModel {
    private final DataRepository mDataRepository;

    public DramaDetailViewModel(@NonNull Application application) {
        super(application);
        mDataRepository = DataRepository.getInstance(AppDatabase.getInstance(application), application.getResources().getString(R.string.data_source_url));
    }

    public LiveData<Drama> getDrama(final String pathId, final Integer id){
        return mDataRepository.getDrama(pathId, id);
    }
}
