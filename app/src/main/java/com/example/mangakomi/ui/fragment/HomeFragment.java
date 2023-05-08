package com.example.mangakomi.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mangakomi.model.MangaHistory;
import com.example.mangakomi.ui.activity.MainActivity;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.adapter.MangaLatestAdapter;
import com.example.mangakomi.ui.adapter.MangaPopularAdapter;
import com.example.mangakomi.service.api.ApiService;
import com.example.mangakomi.databinding.FragmentHomeBinding;
import com.example.mangakomi.model.MangaLatest;
import com.example.mangakomi.model.MangaPopular;
import com.example.mangakomi.ui.myCustom.MyDialog;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.example.mangakomi.service.api.Server;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    private List<MangaPopular> mangaList;

    private MangaPopularAdapter mangaPopularAdapter;
    private MangaLatestAdapter mangaLatestAdapter;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.kProgressHUD.show();
        getListMangaPopular();
        getListMangaLatest(1);


        return fragmentHomeBinding.getRoot();
    }

    private void getListMangaPopular() {
        if (!mainActivity.kProgressHUD.isShowing()) {
            mainActivity.kProgressHUD.show();
        }
        ApiService.apiService.getListMangaPopular().enqueue(new Callback<List<MangaPopular>>() {
            @Override
            public void onResponse(Call<List<MangaPopular>> call, Response<List<MangaPopular>> response) {
                    mangaList = response.body();
                    displayListMangaPopular(mangaList);
            }

            @Override
            public void onFailure(Call<List<MangaPopular>> call, Throwable t) {
                openDialogReload();

            }
        });
    }
    private void getListMangaLatest(int page) {
        if (!mainActivity.kProgressHUD.isShowing()) {
            mainActivity.kProgressHUD.show();
        }
        ApiService.apiService.getListMangaLatest(Server.HEADER_LATEST_START + page + Server.HEADER_LATEST_END).enqueue(new Callback<List<MangaLatest>>() {
            @Override
            public void onResponse(Call<List<MangaLatest>> call, Response<List<MangaLatest>> response) {
               List<MangaLatest> mangaList = response.body();
                displayListMangaLatest(mangaList);
            }

            @Override
            public void onFailure(Call<List<MangaLatest>> call, Throwable t) {
                openDialogReload(page);

            }
        });
    }
    private void openDialogReload(int page) {
        MyDialog dialog = new MyDialog(getActivity(),1, "Confirm", "Error, would you like to wait to reload", Gravity.CENTER);
        dialog.setOnButtonClickListener(() -> getListMangaLatest(page), () -> {

        });
        dialog.show();
    }
    private void openDialogReload() {
        MyDialog dialog = new MyDialog(getActivity(),1, "Confirm", "Error, would you like to wait to reload", Gravity.CENTER);
        dialog.setOnButtonClickListener(() -> getListMangaPopular(), () -> {

        });
        dialog.show();

    }

    private void displayListMangaLatest(List<MangaLatest> mangaList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.HORIZONTAL, false);
        fragmentHomeBinding.rcvMangaLatest.setLayoutManager(gridLayoutManager);
        mangaLatestAdapter = new MangaLatestAdapter(mangaList ,this::goToMangaDetail);
        fragmentHomeBinding.rcvMangaLatest.setAdapter(mangaLatestAdapter);
        if (mainActivity.kProgressHUD.isShowing()){
            mainActivity.kProgressHUD.dismiss();
        }

    }

    private void displayListMangaPopular(List<MangaPopular> mangaList) {

        fragmentHomeBinding.viewpager2.setOffscreenPageLimit(3);
        fragmentHomeBinding.viewpager2.setClipToPadding(false);
        fragmentHomeBinding.viewpager2.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        fragmentHomeBinding.viewpager2.setPageTransformer(compositePageTransformer);

        mangaPopularAdapter = new MangaPopularAdapter(mangaList, this::goToMangaDetail);
        fragmentHomeBinding.viewpager2.setAdapter(mangaPopularAdapter);
    }

    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(getActivity(), MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mainActivity.kProgressHUD.isShowing()){
            mainActivity.kProgressHUD.dismiss();
        }
    }
}
