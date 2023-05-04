package com.example.mangakomi.model;

public class Manga {
         private String full_manga;
         private String name_manga;
         private String poster_manga;
         private String time_update;



    public Manga(String full_manga, String name_manga, String poster_manga, String time_update) {
        this.full_manga = full_manga;
        this.name_manga = name_manga;
        this.poster_manga = poster_manga;
        this.time_update = time_update;

    }

    public Manga() {
    }

    public String getFull_manga() {
        return full_manga;
    }

    public void setFull_manga(String full_manga) {
        this.full_manga = full_manga;
    }

    public String getName_manga() {
        return name_manga;
    }

    public void setName_manga(String name_manga) {
        this.name_manga = name_manga;
    }

    public String getPoster_manga() {
        return poster_manga;
    }

    public void setPoster_manga(String poster_manga) {
        this.poster_manga = poster_manga;
    }

    public String getTime_update() {
        return time_update;
    }

    public void setTime_update(String time_update) {
        this.time_update = time_update;
    }
}
