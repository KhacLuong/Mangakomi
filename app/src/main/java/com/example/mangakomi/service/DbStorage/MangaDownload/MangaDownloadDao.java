package com.example.mangakomi.service.DbStorage.MangaDownload;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mangakomi.model.MangaChapterDownload;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.model.MangaHistory;

import java.util.List;
@Dao
public interface MangaDownloadDao {

    @Insert
    void insertManga(MangaDownload manga);

    @Query("SELECT * FROM manga_download")
    List<MangaDownload> getAllManga();


    @Query("SELECT * FROM manga_download WHERE title_manga = :name")
    MangaDownload getMangaByName(String name);


    @Query("SELECT * FROM manga_download WHERE id = :id")
    MangaDownload getMangaById(int id);
    @Delete
    void deleteManga(MangaDownload manga);

    @Update
    void updateManga(MangaDownload manga);

    @Query("Delete from manga_download WHERE id = :id")
    void deleteManga(int id);

    @Insert
    void  insert(MangaDownload mangaDownload);


}
