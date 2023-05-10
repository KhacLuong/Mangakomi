package com.example.mangakomi.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.mangakomi.R;
import com.example.mangakomi.ui.adapter.SecondaryViewPagerAdapter;
import com.example.mangakomi.databinding.ActivityMainBinding;
import com.example.mangakomi.databinding.ActivitySecondaryBinding;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayoutMediator;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class SecondaryActivity extends AppCompatActivity {
    public ActivitySecondaryBinding activitySecondaryBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        activitySecondaryBinding = ActivitySecondaryBinding.inflate(getLayoutInflater());
        setContentView(activitySecondaryBinding.getRoot());
        activitySecondaryBinding.viewpager2.setUserInputEnabled(false);
        SecondaryViewPagerAdapter secondaryViewPagerAdapter = new SecondaryViewPagerAdapter(this);
        activitySecondaryBinding.viewpager2.setAdapter(secondaryViewPagerAdapter);

        onPageChangeViewPager();
        getDataIntent();
        eventListener();


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
            activitySecondaryBinding.layoutAds.setVisibility(View.VISIBLE);
        }else {
            activitySecondaryBinding.layoutAds.setVisibility(View.GONE);
        }

    }

    private void eventListener() {

        activitySecondaryBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(SecondaryActivity.this, MangaGenresActivity.class, IConstant.ACTION, IConstant.ACTION_SEARCH) ;
            }
        });
        activitySecondaryBinding.toolbar.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(SecondaryActivity.this, MangaStorageActivity.class) ;
            }
        });

    }

    private void onPageChangeViewPager() {
        new TabLayoutMediator(activitySecondaryBinding.tabLayout, activitySecondaryBinding.viewpager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Genres");
                    break;
                case 1:
                    tab.setText("History");
                    break;

                case 2:
                    tab.setText("Bookmarked");
                    break;

            }
        }).attach();

    }
    private void getDataIntent() {

        String action = getIntent().getStringExtra(IConstant.ACTION);

        switch (action){
            case IConstant.ACTION_GENRES:
                activitySecondaryBinding.viewpager2.setCurrentItem(0);
                break;
            case IConstant.ACTION_HISTORY:
                activitySecondaryBinding.viewpager2.setCurrentItem(1);
                break;
            case IConstant.ACTION_BOOKMARKED:
                activitySecondaryBinding.viewpager2.setCurrentItem(2);
                break;
            default:
                activitySecondaryBinding.viewpager2.setCurrentItem(0);
        }
    }


}
