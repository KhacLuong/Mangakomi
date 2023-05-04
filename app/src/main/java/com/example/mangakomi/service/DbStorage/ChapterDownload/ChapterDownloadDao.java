package com.example.mangakomi.service.DbStorage.ChapterDownload;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mangakomi.model.ChapterDownload;

import java.util.List;
@Dao
public interface ChapterDownloadDao {
    @Insert
    void insertChapter(ChapterDownload chapter);

    @Query("SELECT * FROM chapter_download")
    List<ChapterDownload> getAllChapter();


    @Query("SELECT * FROM chapter_download WHERE name_chapter = :name")
    ChapterDownload getChapterByName(String name);

    @Query("SELECT * FROM chapter_download WHERE id = :id")
    List<ChapterDownload> getChaptersByMangaId(int id);

    @Delete
    void deleteChapter(ChapterDownload chapter);

    @Update
    void updateChapter(ChapterDownload chapter);

    @Query("Delete from chapter_download WHERE id = :id")
    void deleteChapter(int id);

    @Query("Delete from chapter_download WHERE manga_id = :manga_id")
    void deleteChapterByMangaId(int manga_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void  insert(ChapterDownload chapterDownload);

}
