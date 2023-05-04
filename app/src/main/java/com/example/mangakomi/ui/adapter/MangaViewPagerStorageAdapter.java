package com.example.mangakomi.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mangakomi.ui.fragment.ContentStorageFragment;
import com.example.mangakomi.ui.fragment.DetailStorageFragment;

public class MangaViewPagerStorageAdapter extends FragmentStateAdapter {
    public MangaViewPagerStorageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DetailStorageFragment();
            case 1:
                return new ContentStorageFragment();
            default:
                return new DetailStorageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}