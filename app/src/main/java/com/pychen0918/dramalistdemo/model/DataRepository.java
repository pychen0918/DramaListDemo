package com.pychen0918.dramalistdemo.model;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.pychen0918.dramalistdemo.model.api.DramaWebApi;
import com.pychen0918.dramalistdemo.model.data.Drama;
import com.pychen0918.dramalistdemo.model.db.AppDatabase;
import com.pychen0918.dramalistdemo.model.db.DramaDao;
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

    private final DramaDao mDramaDao;
    private final Executor mExecutor = Executors.newFixedThreadPool(2);
    private final DramaWebApi mDramaWebApi;

    private DataRepository(final AppDatabase appDatabase, final String baseUrl){
        // Database
        mDramaDao = appDatabase.dramaDao();

        // Web API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        mDramaWebApi = retrofit.create(DramaWebApi.class);
    }

    /**
     * Get data repository instance. Always use this instead of constructor (singleton)
     * @param appDatabase the database instance
     * @param baseUrl url to data source on the web
     * @return instance of the data repository
     */
    public static DataRepository getInstance(final AppDatabase appDatabase, final String baseUrl){
        /*
        * Singleton pattern
        */
        if(sInstance == null){
            synchronized (DataRepository.class){
                if(sInstance == null){
                    sInstance = new DataRepository(appDatabase, baseUrl);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get Drama List from either web or database
     * @param pathId the path string in the url
     * @return LiveData of the drama list
     */
    public LiveData<List<Drama>> getDramaList(final String pathId){
        /*
         * By now, we always update the database with the information received from the internet
         * 1. Return drama data in DB
         * 2. Update DB with the data from internet
         * 3. UI will receive the event once DB is updated
         */
        mDramaWebApi.getDramaList(pathId).enqueue(new Callback<DramaDataContainer>() {
            @Override
            public void onResponse(@NonNull Call<DramaDataContainer> call, @NonNull Response<DramaDataContainer> response) {
                DramaDataContainer data = response.body();
                if(data!=null){
                    List<DramaData> dramaDataList = data.getData();
                    List<Drama> dramaList = new ArrayList<>();
                    for(DramaData item : dramaDataList){
                        dramaList.add(new Drama(item));
                    }
                    insertDramaData(dramaList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DramaDataContainer> call, @NonNull Throwable t) {
                Log.e("DataRepository", "Fail to get drama list: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return mDramaDao.getAll();
    }

    /**
     * Get single drama by Id from either web or database
     * @param pathId the path string in the url
     * @param id the drama id
     * @return LiveData of the drama
     */
    public LiveData<Drama> getDrama(final String pathId, final Integer id) {
        /*
         * By now, we always update the database with the information received from the internet
         * 1. Return drama data in DB
         * 2. Update DB with the data from internet
         * 3. UI will receive the event once DB is updated
         */
        mDramaWebApi.getDramaList(pathId).enqueue(new Callback<DramaDataContainer>() {
            @Override
            public void onResponse(@NonNull Call<DramaDataContainer> call, @NonNull Response<DramaDataContainer> response) {
                DramaDataContainer data = response.body();
                if(data!=null){
                    List<DramaData> dramaDataList = data.getData();
                    List<Drama> dramaList = new ArrayList<>();
                    for(DramaData item : dramaDataList){
                        dramaList.add(new Drama(item));
                    }
                    insertDramaData(dramaList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DramaDataContainer> call, @NonNull Throwable t) {
                Log.e("DataRepository", "Fail to get drama list: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return mDramaDao.get(id);
    }

    /**
     * Insert drama list into database
     * @param dramaList the list of dramas
     */
    private void insertDramaData(final List<Drama> dramaList){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDramaDao.insert(dramaList);
            }
        });
    }
}
