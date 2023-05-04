package com.example.mangakomi.model;



import android.media.Image;

import java.util.List;

 public class MangaContent {
    private String prev;
    private List<String> image;
    private String next;

     public String getPrev() {
         return prev;
     }

     public void setPrev(String prev) {
         this.prev = prev;
     }

     public List<String> getImage() {
         return image;
     }

     public void setImage(List<String> image) {
         this.image = image;
     }

     public String getNext() {
         return next;
     }

     public void setNext(String next) {
         this.next = next;
     }
 }
