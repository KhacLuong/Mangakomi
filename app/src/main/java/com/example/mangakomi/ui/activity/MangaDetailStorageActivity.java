package com.example.mangakomi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ActivityMangaDetailBinding;
import com.example.mangakomi.databinding.ActivityMangaDetailStorageBinding;
import com.example.mangakomi.model.ChapterDownload;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.ui.adapter.MangaViewPagerAdapter;
import com.example.mangakomi.ui.adapter.MangaViewPagerStorageAdapter;
import com.example.mangakomi.util.IConstant;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailStorageActivity extends AppCompatActivity {

    public ActivityMangaDetailStorageBinding activityMangaDetailStorageBinding;
    public  static  int mangaId;
    public int indexCurrentChapter;
    public MangaDownload mangaDownload;

    public List<String> chapterNameList;
    public ChapterDownload currentChapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        activityMangaDetailStorageBinding = ActivityMangaDetailStorageBinding.inflate(getLayoutInflater());
        setContentView(activityMangaDetailStorageBinding.getRoot());
        chapterNameList = new ArrayList<>();

        activityMangaDetailStorageBinding.viewpager2MangaDetail.setUserInputEnabled(true);
        MangaViewPagerStorageAdapter mangaViewPagerAdapter = new MangaViewPagerStorageAdapter(this);
        activityMangaDetailStorageBinding.viewpager2MangaDetail.setAdapter(mangaViewPagerAdapter);
        getDataIntent();
    }

    private void getDataIntent() {
        try {
            mangaId = Integer.parseInt(getIntent().getStringExtra(IConstant.KEY_VALUE));
        }catch (Exception e){
            mangaId = 0;
        }
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