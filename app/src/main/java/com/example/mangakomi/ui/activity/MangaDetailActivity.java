package com.example.mangakomi.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.mangakomi.R;
import com.example.mangakomi.ui.adapter.MangaViewPagerAdapter;

import com.example.mangakomi.databinding.ActivityMangaDetailBinding;

import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.util.IConstant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MangaDetailActivity extends AppCompatActivity {

    public ActivityMangaDetailBinding activityMangaBinding;
    public String mangaLink;
    public int indexCurrentChapter;
    public String manga_name;
    public String chapter_name;
    public List<String> chapterNameList;
    public MangaDetail mangaDetail;
    public KProgressHUD kProgressHUD;
    private InterstitialAd mInterstitialAd;
    public int countAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        activityMangaBinding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMangaBinding.getRoot());
        chapterNameList = new ArrayList<>();
        countAds=0;
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        loadAdsInter();

        activityMangaBinding.viewpager2MangaDetail.setUserInputEnabled(false);
        MangaViewPagerAdapter mangaViewPagerAdapter = new MangaViewPagerAdapter(this);
        activityMangaBinding.viewpager2MangaDetail.setAdapter(mangaViewPagerAdapter);
        getDataIntent();
    }


    @Override
    protected void onResume() {
        loadAds();
        super.onResume();
    }

    private void loadAdsInter() {
        countAds++;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });


        @SuppressLint("VisibleForTests") AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getResources().getString(R.string.AdUnitId_interstitial_full_screen), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(MangaDetailActivity.this);
                        } else {
                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


    }

    private void loadAds() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });


        @SuppressLint("VisibleForTests") AdRequest adRequest = new AdRequest.Builder().build();

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(getResources().getString(R.string.AdUnitId_banner_footer));
        adView = findViewById(R.id.adView);

        adView.loadAd(adRequest);

        if (adView.isLoading()) {
            activityMangaBinding.layoutAds.setVisibility(View.VISIBLE);
        } else {
            activityMangaBinding.layoutAds.setVisibility(View.GONE);
        }


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
        if (kProgressHUD.isShowing())
            kProgressHUD.dismiss();

    }

    public void showProgressHUD() {
        if (!kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKProgressHUD();
        mInterstitialAd = null;
        
    }

}