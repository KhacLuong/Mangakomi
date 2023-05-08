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

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MangaDetailActivity extends AppCompatActivity  {

    public ActivityMangaDetailBinding activityMangaBinding;
    public String mangaLink;
    public int indexCurrentChapter;
    public String manga_name;
    public String chapter_name;
    public List<String> chapterNameList;
    public MangaDetail mangaDetail;
    public KProgressHUD kProgressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        activityMangaBinding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMangaBinding.getRoot());
        chapterNameList = new ArrayList<>();

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

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
    public void hideKProgressHUD() {
        if(kProgressHUD.isShowing())
            kProgressHUD.dismiss();

    }
    public void showProgressHUD() {
        if(!kProgressHUD.isShowing()){
            kProgressHUD.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     hideKProgressHUD();

    }
}