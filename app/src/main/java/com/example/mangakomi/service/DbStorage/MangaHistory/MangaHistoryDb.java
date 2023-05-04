package com.example.mangakomi.service.DbStorage.MangaHistory;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mangakomi.model.MangaHistory;

@Database(entities = {MangaHistory.class}, version = 1)
public abstract class MangaHistoryDb extends RoomDatabase {
    private static final String DATABASE_NAME = "manga_history.db";

    private static MangaHistoryDb instance;
    public static synchronized MangaHistoryDb getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MangaHistoryDb.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract MangaHistoryDAO mangaHistoryDAO();
}
