package com.example.mangakomi.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mangakomi.ui.fragment.DetailFragment;
import com.example.mangakomi.ui.fragment.ContentMangaFragment;

public class MangaViewPagerAdapter extends FragmentStateAdapter {
    public MangaViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DetailFragment();
            case 1:
                return new ContentMangaFragment();
            default:
                return new DetailFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
