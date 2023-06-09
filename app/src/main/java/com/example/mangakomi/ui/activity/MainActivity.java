package com.example.mangakomi.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mangakomi.R;
import com.example.mangakomi.ui.adapter.MainViewPagerAdapter;
import com.example.mangakomi.databinding.ActivityMainBinding;
import com.example.mangakomi.ui.myCustom.MyBottomDialog;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mActivityMainBinding;
    MyBottomDialog bottomSheetDialog;
    public KProgressHUD kProgressHUD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());


        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        mActivityMainBinding.viewpager2.setUserInputEnabled(false);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mActivityMainBinding.viewpager2.setAdapter(mainViewPagerAdapter);

        onPageChangeViewPager();
        onClickBottomNavigation();
        OpenMenuHome();
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
            mActivityMainBinding.layoutAds.setVisibility(View.VISIBLE);
        }else {
            mActivityMainBinding.layoutAds.setVisibility(View.GONE);
        }

    }


    private void eventListener() {
        mActivityMainBinding.btnCloseAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityMainBinding.layoutAds.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if( mActivityMainBinding.layoutAds.getVisibility()==View.GONE)
                    mActivityMainBinding.layoutAds.setVisibility(View.VISIBLE);
                }, 30000);
            }
        });

        mActivityMainBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(MainActivity.this, MangaGenresActivity.class, IConstant.ACTION, IConstant.ACTION_SEARCH) ;
            }
        });
        mActivityMainBinding.toolbar.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(MainActivity.this, MangaStorageActivity.class) ;

            }
        });
    }

    public void onPageChangeViewPager(){
        mActivityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;

                    case 1:
                        mActivityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_latest).setChecked(true);
                        break;

                    case 2:
                        mActivityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_hot_manga).setChecked(true);
                        break;


                    case 3:
                        mActivityMainBinding.bottomNav.bottomNavigation.getMenu().findItem(R.id.nav_new_manga).setChecked(true);
                        break;
                }
            }
        });
    }

    public void onClickBottomNavigation(){
        mActivityMainBinding.bottomNav.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        mActivityMainBinding.viewpager2.setCurrentItem(0);
                        mActivityMainBinding.toolbar.title.setText("");
                        break;
                    case R.id.nav_latest:
                        mActivityMainBinding.viewpager2.setCurrentItem(1);
                        mActivityMainBinding.toolbar.title.setText("Latest Updates");
                        break;
                    case R.id.nav_hot_manga:
                        mActivityMainBinding.viewpager2.setCurrentItem(2);
                        mActivityMainBinding.toolbar.title.setText("Hot Popular");
                        break;
                    case R.id.nav_new_manga:
                        mActivityMainBinding.viewpager2.setCurrentItem(3);
                        mActivityMainBinding.toolbar.title.setText("New Manga");
                        break;
                }
                return true;
            }
        });
    }

    public void OpenMenuHome(){
        mActivityMainBinding.bottomNav.btnMenuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_menu_home, null);
                bottomSheetDialog = new MyBottomDialog(MainActivity.this);
                bottomSheetDialog.setContentView(viewDialog);
                LinearLayout btn_genres = viewDialog.findViewById(R.id.btn_genres);
                LinearLayout btn_history = viewDialog.findViewById(R.id.btn_history);
                LinearLayout btn_bookmarked = viewDialog.findViewById(R.id.btn_bookmarked);

                btn_genres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalFunction.startActivity(getApplication(), SecondaryActivity.class,IConstant.ACTION, IConstant.ACTION_GENRES);
                    }
                });

                btn_history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalFunction.startActivity(getApplication(), SecondaryActivity.class,IConstant.ACTION, IConstant.ACTION_HISTORY);
                    }
                });
                btn_bookmarked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalFunction.startActivity(getApplication(), SecondaryActivity.class,IConstant.ACTION, IConstant.ACTION_BOOKMARKED);
                    }
                });
                bottomSheetDialog.show();


            }
        });
    }

    @Override
    protected void onDestroy() {
        if(bottomSheetDialog!=null&&bottomSheetDialog.isShowing()){
            bottomSheetDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if(bottomSheetDialog!=null&&bottomSheetDialog.isShowing()){
            bottomSheetDialog.dismiss();
        }
        super.onStop();
    }
}