package com.example.mangakomi.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;

import com.example.mangakomi.R;
import com.example.mangakomi.ui.adapter.SecondaryViewPagerAdapter;
import com.example.mangakomi.databinding.ActivityMainBinding;
import com.example.mangakomi.databinding.ActivitySecondaryBinding;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.google.android.material.tabs.TabLayoutMediator;

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

    private void eventListener() {

        activitySecondaryBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(SecondaryActivity.this, MangaGenresActivity.class, IConstant.ACTION, IConstant.ACTION_SEARCH) ;
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
