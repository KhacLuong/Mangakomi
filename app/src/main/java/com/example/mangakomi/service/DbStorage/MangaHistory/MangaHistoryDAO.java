package com.example.mangakomi.service.DbStorage.MangaHistory;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mangakomi.model.MangaHistory;

import java.util.List;

@Dao
public interface MangaHistoryDAO {
    @Insert
    void insertMangaHistory(MangaHistory manga);

    @Query("SELECT * FROM manga_history")
    List<MangaHistory> getAllMangaHistory();

    @Query("SELECT * FROM manga_history WHERE status_bookmark =1")
    List<MangaHistory> getListMangaBookMarks();

    @Query("SELECT * FROM manga_history WHERE status_history = 1")
    List<MangaHistory> getListMangaHistory();


    //    @Query("SELECT * FROM food WHERE id = :id")
//    List<Food> getFoodById(int id);
    @Query("SELECT * FROM manga_history WHERE name = :name")
    MangaHistory getMangaByName(String name);



    @Query("SELECT * FROM manga_history WHERE name = :name AND status_bookmark = 1")
    MangaHistory getMangaBookMarkByName(String name);

    @Query("SELECT * FROM manga_history WHERE name = :name AND status_history = 1")
    MangaHistory getMangaHistoryByName(String name);


    @Delete
    void deleteMangaHistory(MangaHistory manga);

    @Update
    void updateMangaHistory(MangaHistory manga);

    @Query("Delete from manga_history WHERE id = :id")
    void deleteMangaHistory(int id);
}
