package com.example.mangakomi.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "manga_download")
public class MangaDownload implements Serializable, Cloneable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title_manga")
    public String title_manga ;

    @ColumnInfo(name = "rating_manga")
    public String rating_manga;

    @ColumnInfo(name = "rank_manga")
    public String rank_manga;

    @ColumnInfo(name = "alternative_manga")
    public String alternative_manga;

    @ColumnInfo(name = "genre_manga")
    public String genre_manga;

    @ColumnInfo(name = "type_manga")
    public String type_manga;

    @ColumnInfo(name = "status_manga")
    public String status_manga;

    @ColumnInfo(name = "summary_manga")
    public String summary_manga;


    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();

    }

    public MangaDownload() {
    }

    public MangaDownload( String title_manga, String rating_manga, String rank_manga, String alternative_manga, String genre_manga, String type_manga, String status_manga, String summary_manga) {
        this.title_manga = title_manga;
        this.rating_manga = rating_manga;
        this.rank_manga = rank_manga;
        this.alternative_manga = alternative_manga;
        this.genre_manga = genre_manga;
        this.type_manga = type_manga;
        this.status_manga = status_manga;
        this.summary_manga = summary_manga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle_manga() {
        return title_manga;
    }

    public void setTitle_manga(String title_manga) {
        this.title_manga = title_manga;
    }

    public String getRating_manga() {
        return rating_manga;
    }

    public void setRating_manga(String rating_manga) {
        this.rating_manga = rating_manga;
    }

    public String getRank_manga() {
        return rank_manga;
    }

    public void setRank_manga(String rank_manga) {
        this.rank_manga = rank_manga;
    }

    public String getAlternative_manga() {
        return alternative_manga;
    }

    public void setAlternative_manga(String alternative_manga) {
        this.alternative_manga = alternative_manga;
    }

    public String getGenre_manga() {
        return genre_manga;
    }

    public void setGenre_manga(String genre_manga) {
        this.genre_manga = genre_manga;
    }

    public String getType_manga() {
        return type_manga;
    }

    public void setType_manga(String type_manga) {
        this.type_manga = type_manga;
    }

    public String getStatus_manga() {
        return status_manga;
    }

    public void setStatus_manga(String status_manga) {
        this.status_manga = status_manga;
    }

    public String getSummary_manga() {
        return summary_manga;
    }

    public void setSummary_manga(String summary_manga) {
        this.summary_manga = summary_manga;
    }

}
