package com.example.mangakomi.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.adapter.MangaHotAdapter;
import com.example.mangakomi.ui.adapter.PageAdapter;
import com.example.mangakomi.service.api.ApiService;
import com.example.mangakomi.databinding.FragmentMangaHotBinding;
import com.example.mangakomi.model.MangaLatest;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.example.mangakomi.service.api.Server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotMangaFragment extends Fragment {
    private FragmentMangaHotBinding fragmentMangaHotBinding;
    private List<MangaLatest> mangaList;
    private MangaHotAdapter mangaHotAdapter;
    private PageAdapter pageAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMangaHotBinding = FragmentMangaHotBinding.inflate(inflater, container, false);
        mangaList = new ArrayList<>();
        setDataPagination();
        getListMangaHot(1);
        displayListMangaHot(mangaList);
        return fragmentMangaHotBinding.getRoot();
    }

    private void getListMangaHot(int page) {

        ApiService.apiService.getListMangaHot(Server.HEADER_MANGA_HOT_START + page + Server.HEADER_MANGA_HOT_END).enqueue(new Callback<List<MangaLatest>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<MangaLatest>> call, Response<List<MangaLatest>> response) {
                List<MangaLatest> mangaList = response.body();
                mangaHotAdapter.setData(mangaList);
                mangaHotAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<MangaLatest>> call, Throwable t) {
                Toast.makeText(getActivity(), "Call api Ree", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataPagination(){
        List<Integer>  integerList = new ArrayList<>();
        for (Integer i = 1; i <=999; i++) {
            integerList.add(i);
        }
        displayPaginate(integerList);
    }
    private void displayPaginate(List<Integer> integerList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        fragmentMangaHotBinding.rcvPagination.setLayoutManager(linearLayoutManager);
        pageAdapter = new PageAdapter(integerList ,this::goToPage);
        fragmentMangaHotBinding.rcvPagination.setAdapter(pageAdapter);
    }

    private void goToPage(int page, int indexOlderItem) {
        fragmentMangaHotBinding.tvCurrentPage.setText(String.valueOf("page "+page));
        pageAdapter.notifyItemChanged(page-1);
        pageAdapter.notifyItemChanged(indexOlderItem);
        getListMangaHot(page);
    }


    private void displayListMangaHot(List<MangaLatest> mangaList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.VERTICAL, false);
        fragmentMangaHotBinding.rcvMangaLatest.setLayoutManager(gridLayoutManager);
        mangaHotAdapter = new MangaHotAdapter(mangaList ,this::goToMangaDetail);
        fragmentMangaHotBinding.rcvMangaLatest.setAdapter(mangaHotAdapter);
    }
    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(getActivity(), MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);

    }

}
