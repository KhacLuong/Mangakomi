package com.example.mangakomi.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mangakomi.ui.activity.MangaGenresActivity;
import com.example.mangakomi.ui.adapter.GenresAdapter;
import com.example.mangakomi.databinding.FragmentGenresBinding;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenresFragment extends Fragment {
    private FragmentGenresBinding fragmentGenresBinding;
    private GenresAdapter genresAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentGenresBinding = FragmentGenresBinding.inflate(inflater, container, false);
        buildData();


        return fragmentGenresBinding.getRoot();
    }

    private void buildData() {
        List<String> genresList = new ArrayList<>();
        genresList.addAll(Arrays.asList("4-koma", "Action","Action Adventure","Action Fantasy","Adaptation","Adventure", "ANIMALS", "Another chance",
                "ANTHOLOGY", "Cheat", "Cheat Ability", "Childhood Friends", "College life", "Comedy", "Comic", "Coming Soon",
                "Cooking", "Crime","Cultivation","Demon", "Demons", "Doujinshi", "Drama", "Dungeon"));

        setData(genresList);

    }

    private void setData(List<String> genresList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4,GridLayoutManager.VERTICAL, false);
        fragmentGenresBinding.rcvGenres.setLayoutManager(gridLayoutManager);
        genresAdapter = new GenresAdapter(genresList ,this::goToMangaDetail);
        fragmentGenresBinding.rcvGenres.setAdapter(genresAdapter);
    }

    private void goToMangaDetail(String genres) {
        GlobalFunction.startActivity(getActivity(), MangaGenresActivity.class, IConstant.ACTION, IConstant.ACTION_GENRES, IConstant.KEY_VALUE, genres);
    }
}
