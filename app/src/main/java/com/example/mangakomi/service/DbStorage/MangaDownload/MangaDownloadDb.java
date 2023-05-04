package com.example.mangakomi.service.DbStorage.MangaDownload;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mangakomi.model.MangaDownload;



@Database(entities = {MangaDownload.class}, version = 1)
public abstract class MangaDownloadDb extends RoomDatabase {
    private static final String DATABASE_NAME = "manga_download.db";

    private static MangaDownloadDb instance;
    public static synchronized MangaDownloadDb getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MangaDownloadDb.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract MangaDownloadDao mangaDownloadDao();
}
