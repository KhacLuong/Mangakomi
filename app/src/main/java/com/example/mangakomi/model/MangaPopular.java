package com.example.mangakomi.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MangaPopular  implements Serializable {
    @SerializedName("link_manga")
    private String link_manga;

    @SerializedName("title_manga")
    private String title_manga;

    @SerializedName("poster_manga")
    private String poster_manga;

    @SerializedName("rating_manga")
    private String rating_manga;

    @SerializedName("lastest_chapter_manga")
    private String lastest_chapter_manga;

    @SerializedName("release_date_manga")
    private String release_date_manga;

    public MangaPopular() {
    }

    public MangaPopular(String link_manga, String title_manga, String poster_manga, String rating_manga, String lastest_chapter_manga, String release_date_manga) {
        this.link_manga = link_manga;
        this.title_manga = title_manga;
        this.poster_manga = poster_manga;
        this.rating_manga = rating_manga;
        this.lastest_chapter_manga = lastest_chapter_manga;
        this.release_date_manga = release_date_manga;
    }

    public String getLink_manga() {
        return link_manga;
    }

    public void setLink_manga(String link_manga) {
        this.link_manga = link_manga;
    }

    public String getTitle_manga() {
        return title_manga;
    }

    public void setTitle_manga(String title_manga) {
        this.title_manga = title_manga;
    }

    public String getPoster_manga() {
        return poster_manga;
    }

    public void setPoster_manga(String poster_manga) {
        this.poster_manga = poster_manga;
    }

    public String getRating_manga() {
        return rating_manga;
    }

    public void setRating_manga(String rating_manga) {
        this.rating_manga = rating_manga;
    }

    public String getLastest_chapter_manga() {
        return lastest_chapter_manga;
    }

    public void setLastest_chapter_manga(String lastest_chapter_manga) {
        this.lastest_chapter_manga = lastest_chapter_manga;
    }

    public String getRelease_date_manga() {
        return release_date_manga;
    }

    public void setRelease_date_manga(String release_date_manga) {
        this.release_date_manga = release_date_manga;
    }
}
