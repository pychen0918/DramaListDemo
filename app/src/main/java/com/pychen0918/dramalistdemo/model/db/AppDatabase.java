package com.pychen0918.dramalistdemo.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.pychen0918.dramalistdemo.model.data.Drama;

import static android.arch.persistence.room.RoomDatabase.JournalMode.TRUNCATE;

@Database(entities = {Drama.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    private static final String DATABASE_NAME = "drama_list_demo_db";

    public abstract DramaDao dramaDao();

    public static AppDatabase getInstance(final Context context){
        if(sInstance == null){
            synchronized (AppDatabase.class){
                if(sInstance == null){
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .setJournalMode(TRUNCATE)
                .build();
    }
}
