package com.example.mangakomi.service.DbStorage.ChapterDownload;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mangakomi.model.ChapterDownload;
import com.example.mangakomi.model.MangaDownload;


@Database(entities = {ChapterDownload.class}, version = 1)
public abstract class ChapterDownloadDb extends RoomDatabase {
    private static final String DATABASE_NAME = "chapter_download.db";

    private static ChapterDownloadDb instance;
    public static synchronized ChapterDownloadDb getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ChapterDownloadDb.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract ChapterDownloadDao chapterDownloadDao();
}
