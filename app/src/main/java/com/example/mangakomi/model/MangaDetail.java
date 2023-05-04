package com.example.mangakomi.model;

import java.io.Serializable;
import java.util.List;

public class MangaDetail implements Serializable, Cloneable{

    public String poster_manga;
    public String title_manga ;
    public String rating_manga;
    public String rank_manga;
    public String alternative_manga;
    public String genre_manga;
    public String type_manga;
    public String status_manga;
    public String summary_manga;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public List<Chapter> list_chapter;



    public class Chapter implements Serializable {
        public String link_chapter;
        public String name_chapter;
        public String release_date;

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

    public String getPoster_manga() {
        return poster_manga;
    }

    public void setPoster_manga(String poster_manga) {
        this.poster_manga = poster_manga;
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

    public List<Chapter> getList_chapter() {
        return list_chapter;
    }

    public void setList_chapter(List<Chapter> list_chapter) {
        this.list_chapter = list_chapter;
    }

}
