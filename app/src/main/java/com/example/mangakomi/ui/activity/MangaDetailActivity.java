package com.example.mangakomi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.example.mangakomi.R;
import com.example.mangakomi.ui.adapter.MangaViewPagerAdapter;

import com.example.mangakomi.databinding.ActivityMangaDetailBinding;

import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.util.IConstant;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailActivity extends AppCompatActivity  {

    public ActivityMangaDetailBinding activityMangaBinding;
    public String mangaLink;
    public int indexCurrentChapter;
    public String manga_name;
    public String chapter_name;
    public List<String> chapterNameList;
    public MangaDetail mangaDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        activityMangaBinding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMangaBinding.getRoot());
        chapterNameList = new ArrayList<>();

        activityMangaBinding.viewpager2MangaDetail.setUserInputEnabled(true);
        MangaViewPagerAdapter mangaViewPagerAdapter = new MangaViewPagerAdapter(this);
        activityMangaBinding.viewpager2MangaDetail.setAdapter(mangaViewPagerAdapter);
        getDataIntent();
    }

    private void getDataIntent() {
         mangaLink = getIntent().getStringExtra(IConstant.MANGA_LINK);
    }



//    public void changeFragment(){
//
//        activityMangaBinding.viewpager2MangaDetail.setCurrentItem(0);
//
//
////        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////        fragmentTransaction.replace(viewId, fragment, "tag");
////        fragmentTransaction.addToBackStack(null);
////        fragmentTransaction.commit();
//    }

}