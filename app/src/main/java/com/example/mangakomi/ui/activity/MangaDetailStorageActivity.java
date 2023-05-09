package com.example.mangakomi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Toast;

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

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MangaDetailStorageActivity extends AppCompatActivity {

    public ActivityMangaDetailStorageBinding activityMangaDetailStorageBinding;
    public  static  int mangaId;
    public int indexCurrentChapter;
    public MangaDownload mangaDownload;


    public ChapterDownload currentChapter;
    public KProgressHUD kProgressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMangaDetailStorageBinding = ActivityMangaDetailStorageBinding.inflate(getLayoutInflater());
        setContentView(activityMangaDetailStorageBinding.getRoot());
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        activityMangaDetailStorageBinding.viewpager2MangaDetail.setUserInputEnabled(false);
        MangaViewPagerStorageAdapter mangaViewPagerAdapter = new MangaViewPagerStorageAdapter(this);
        activityMangaDetailStorageBinding.viewpager2MangaDetail.setAdapter(mangaViewPagerAdapter);
        getDataIntent();

    }

    private void getDataIntent() {
            mangaId = getIntent().getIntExtra(IConstant.KEY_VALUE, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (kProgressHUD.isShowing()){
            kProgressHUD.dismiss();
        }
    }


    public void hideKProgressHUD() {
        if(kProgressHUD.isShowing())
            kProgressHUD.dismiss();

    }
    public void showProgressHUD() {
        if(!kProgressHUD.isShowing())
            kProgressHUD.show();

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