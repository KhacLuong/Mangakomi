package com.example.mangakomi.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "manga_history")
public class MangaHistory implements Serializable, Cloneable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "link_manga")
    private String link;

    @ColumnInfo(name = "ranking")
    private String ranking;

    @ColumnInfo(name = "chapter")
    private String chapter;

    @ColumnInfo(name = "rating")
    private String rating;

    @ColumnInfo(name = "poster_manga")
    private String poster;


    @ColumnInfo(name = "status_history")
    private int statusHistory;

    @ColumnInfo(name = "status_bookmark")
    private int statusBookMark;
//    true =1, false =0

    @ColumnInfo(name = "index_chapter")
    private int indexChapterReverse;

    public MangaHistory(String name, String link, String ranking, String chapter, String rating, String poster, int statusHistory, int statusBookMark, int indexChapterReverse) {
        this.name = name;
        this.link = link;
        this.ranking = ranking;
        this.chapter = chapter;
        this.rating = rating;
        this.poster = poster;
        this.statusHistory = statusHistory;
        this.statusBookMark = statusBookMark;
        this.indexChapterReverse = indexChapterReverse;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();

    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndexChapterReverse() {
        return indexChapterReverse;
    }

    public void setIndexChapterReverse(int indexChapterReverse) {
        this.indexChapterReverse = indexChapterReverse;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(int statusHistory) {
        this.statusHistory = statusHistory;
    }

    public int getStatusBookMark() {
        return statusBookMark;
    }

    public void setStatusBookMark(int statusBookMark) {
        this.statusBookMark = statusBookMark;
    }
}
