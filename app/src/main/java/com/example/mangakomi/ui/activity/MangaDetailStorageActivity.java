package com.example.mangakomi.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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

public class MangaDetailStorageActivity extends AppCompatActivity {

    public ActivityMangaDetailStorageBinding activityMangaDetailStorageBinding;
    public  static  int mangaId;
    public int indexCurrentChapter;
    public MangaDownload mangaDownload;


    public ChapterDownload currentChapter;
    public KProgressHUD kProgressHUD;
    public int countAds;
    private InterstitialAd mInterstitialAd;


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
        countAds=0;
        loadAdsInter();

        activityMangaDetailStorageBinding.viewpager2MangaDetail.setUserInputEnabled(false);
        MangaViewPagerStorageAdapter mangaViewPagerAdapter = new MangaViewPagerStorageAdapter(this);
        activityMangaDetailStorageBinding.viewpager2MangaDetail.setAdapter(mangaViewPagerAdapter);
        getDataIntent();

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
                            mInterstitialAd.show(MangaDetailStorageActivity.this);
                        } else {
                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


    }

    @Override
    protected void onResume() {
        loadAds();
        super.onResume();
    }

    private void  loadAds(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(getResources().getString(R.string.AdUnitId_banner_footer));
        adView = findViewById(R.id.adView);
        @SuppressLint("VisibleForTests") AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (adView.isLoading()){
            activityMangaDetailStorageBinding.layoutAds.setVisibility(View.VISIBLE);
        }else {
            activityMangaDetailStorageBinding.layoutAds.setVisibility(View.GONE);
        }

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
        mInterstitialAd = null;
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