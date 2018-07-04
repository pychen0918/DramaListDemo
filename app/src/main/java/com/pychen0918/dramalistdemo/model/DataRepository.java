package com.pychen0918.dramalistdemo.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.pychen0918.dramalistdemo.model.api.DramaWebApi;
import com.pychen0918.dramalistdemo.model.gson.DramaData;
import com.pychen0918.dramalistdemo.model.gson.DramaDataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {
    private static DataRepository sInstance;
    private final Executor mExecutor = Executors.newFixedThreadPool(2);
    private final DramaWebApi mDramaWebApi;

    private final MutableLiveData<List<DramaData>> mDramaListLiveData;

    private DataRepository(final String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        mDramaWebApi = retrofit.create(DramaWebApi.class);

        mDramaListLiveData = new MutableLiveData<>();
        mDramaListLiveData.setValue(new ArrayList<DramaData>());
    }

    public static DataRepository getInstance(final String baseUrl){
        if(sInstance == null){
            synchronized (DataRepository.class){
                if(sInstance == null){
                    sInstance = new DataRepository(baseUrl);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<DramaData>> getDramaList(final String pathId){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDramaWebApi.getDramaList(pathId).enqueue(new Callback<DramaDataContainer>() {
                    @Override
                    public void onResponse(@NonNull Call<DramaDataContainer> call, @NonNull Response<DramaDataContainer> response) {
                        DramaDataContainer data = response.body();
                        if(data!=null){
                            mDramaListLiveData.postValue(data.getData());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DramaDataContainer> call, @NonNull Throwable t) {
                        Log.e("DataRepository", "Fail to get drama list: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        });

        return mDramaListLiveData;
    }
}
