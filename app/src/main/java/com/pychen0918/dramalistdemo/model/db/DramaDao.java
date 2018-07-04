package com.pychen0918.dramalistdemo.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pychen0918.dramalistdemo.model.data.Drama;

import java.util.List;

@Dao
public interface DramaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Drama> data);

    @Query("SELECT * FROM drama")
    LiveData<List<Drama>> getAll();
}
