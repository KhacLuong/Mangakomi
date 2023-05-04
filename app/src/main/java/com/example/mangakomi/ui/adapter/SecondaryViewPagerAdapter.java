package com.example.mangakomi.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mangakomi.ui.fragment.BookMarkedFragment;
import com.example.mangakomi.ui.fragment.GenresFragment;
import com.example.mangakomi.ui.fragment.HistoriesFragment;


public class SecondaryViewPagerAdapter extends FragmentStateAdapter {
    public SecondaryViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new GenresFragment();
            case 1:
                return new HistoriesFragment();
            case 2:
                return new BookMarkedFragment();
            default:
                return new GenresFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
