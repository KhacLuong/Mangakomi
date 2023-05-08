package com.example.mangakomi.ui.fragment;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mangakomi.ui.activity.MainActivity;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.adapter.MangaLatestUpdateAdapter;
import com.example.mangakomi.ui.adapter.PageAdapter;
import com.example.mangakomi.service.api.ApiService;
import com.example.mangakomi.databinding.FragmentLatestBinding;
import com.example.mangakomi.model.MangaLatest;
import com.example.mangakomi.ui.myCustom.MyDialog;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.example.mangakomi.service.api.Server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestFragment extends Fragment {
    private FragmentLatestBinding fragmentLatestBinding;
    private List<MangaLatest> mangaList;
    private MangaLatestUpdateAdapter mangaLatestUpdateAdapter;
    private PageAdapter pageAdapter;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentLatestBinding = FragmentLatestBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        mangaList = new ArrayList<>();
        mainActivity.kProgressHUD.show();
        setDataPagination();
        displayListMangaLatest(mangaList);
        getListMangaLatest(1);




        return fragmentLatestBinding.getRoot();
    }

    private void getListMangaLatest(int page) {
        if (!mainActivity.kProgressHUD.isShowing()) {
            mainActivity.kProgressHUD.show();
        }
        ApiService.apiService.getListMangaLatest(Server.HEADER_LATEST_START + page + Server.HEADER_LATEST_END).enqueue(new Callback<List<MangaLatest>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<MangaLatest>> call, Response<List<MangaLatest>> response) {
                mangaList = response.body();
                mangaLatestUpdateAdapter.setData(mangaList);
                mangaLatestUpdateAdapter.notifyDataSetChanged();
                if (mainActivity.kProgressHUD.isShowing()) {
                    mainActivity.kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<MangaLatest>> call, Throwable t) {
                if (mainActivity.kProgressHUD.isShowing()) {
                    mainActivity.kProgressHUD.dismiss();
                }
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

    private void setDataPagination() {
        List<Integer> integerList = new ArrayList<>();
        for (Integer i = 1; i <= 999; i++) {
            integerList.add(i);
        }
        displayPaginate(integerList);
    }

    private void displayPaginate(List<Integer> integerList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fragmentLatestBinding.rcvPagination.setLayoutManager(linearLayoutManager);
        pageAdapter = new PageAdapter(integerList, this::goToPage);
        fragmentLatestBinding.rcvPagination.setAdapter(pageAdapter);
    }

    private void goToPage(Integer page, int indexOlderItem) {
        mainActivity.kProgressHUD.show();
        fragmentLatestBinding.tvCurrentPage.setText(String.valueOf("page " + page));
        pageAdapter.notifyItemChanged(page - 1);
        pageAdapter.notifyItemChanged(indexOlderItem);
//        fragmentLatestBinding.rcvPagination.getChildAt(4).setBackgroundResource(R.drawable.bg_indicator_pagination_orange);
        getListMangaLatest(page);
    }

    private void displayListMangaLatest(List<MangaLatest> mangaList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        fragmentLatestBinding.rcvMangaLatest.setLayoutManager(gridLayoutManager);
        mangaLatestUpdateAdapter = new MangaLatestUpdateAdapter(mangaList, this::goToMangaDetail);
        fragmentLatestBinding.rcvMangaLatest.setAdapter(mangaLatestUpdateAdapter);
    }

    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(getActivity(), MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mainActivity.kProgressHUD.isShowing()) {
            mainActivity.kProgressHUD.dismiss();
        }
    }
}
