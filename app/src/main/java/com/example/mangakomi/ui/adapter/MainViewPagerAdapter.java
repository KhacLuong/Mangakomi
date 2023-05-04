package com.example.mangakomi.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.mangakomi.ui.fragment.HotMangaFragment;
import com.example.mangakomi.ui.fragment.LatestFragment;
import com.example.mangakomi.ui.fragment.HomeFragment;
import com.example.mangakomi.ui.fragment.NewMangaFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {


    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new LatestFragment();
            case 2:
                return new HotMangaFragment();
            case 3:
                return new NewMangaFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
