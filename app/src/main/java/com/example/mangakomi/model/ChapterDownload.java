package com.example.mangakomi.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;



@Entity(tableName = "chapter_download")
public class ChapterDownload implements Serializable, Cloneable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "link_chapter")
    public String link_chapter;

    @ColumnInfo(name = "name_chapter")
    public String name_chapter;

    @ColumnInfo(name = "release_date")
    public String release_date;

    @ColumnInfo(name = "manga_id")
    public int manga_id;

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();

    }

    public ChapterDownload() {
    }

    public ChapterDownload(int id, String link_chapter, String name_chapter, String release_date, int manga_id) {
        this.id = id;
        this.link_chapter = link_chapter;
        this.name_chapter = name_chapter;
        this.release_date = release_date;
        this.manga_id = manga_id;
    }

    public ChapterDownload(String link_chapter, String name_chapter, String release_date, int manga_id) {
        this.link_chapter = link_chapter;
        this.name_chapter = name_chapter;
        this.release_date = release_date;
        this.manga_id = manga_id;
    }

    public int getManga_id() {
        return manga_id;
    }

    public void setManga_id(int manga_id) {
        this.manga_id = manga_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink_chapter() {
        return link_chapter;
    }

    public void setLink_chapter(String link_chapter) {
        this.link_chapter = link_chapter;
    }

    public String getName_chapter() {
        return name_chapter;
    }

    public void setName_chapter(String name_chapter) {
        this.name_chapter = name_chapter;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
