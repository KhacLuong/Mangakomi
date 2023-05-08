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
import com.example.mangakomi.ui.adapter.MangaNewAdapter;
import com.example.mangakomi.ui.adapter.PageAdapter;
import com.example.mangakomi.service.api.ApiService;
import com.example.mangakomi.databinding.FragmentMangaNewBinding;
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

public class NewMangaFragment extends Fragment {

    private FragmentMangaNewBinding fragmentMangaNewBinding;
    private List<MangaLatest> mangaList;
    private MangaNewAdapter mangaNewAdapter;
    private PageAdapter pageAdapter;
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMangaNewBinding = FragmentMangaNewBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();
        mangaList = new ArrayList<>();
        mainActivity.kProgressHUD.show();
        setDataPagination();
        displayListMangaNew(mangaList);
        getListMangaNew(1);

        return fragmentMangaNewBinding.getRoot();
    }

    private void getListMangaNew(int page) {
        if (!mainActivity.kProgressHUD.isShowing()) {
            mainActivity.kProgressHUD.show();
        }
        ApiService.apiService.getListMangaNew(Server.HEADER_MANGA_NEW_START + page + Server.HEADER_MANGA_NEW_END).enqueue(new Callback<List<MangaLatest>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<MangaLatest>> call, Response<List<MangaLatest>> response) {
                List<MangaLatest> mangaList = response.body();
                mangaNewAdapter.setData(mangaList);
                mangaNewAdapter.notifyDataSetChanged();
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
        dialog.setOnButtonClickListener(() -> getListMangaNew(page), () -> {

        });
        dialog.show();

    }




    private void setDataPagination(){
        List<Integer> integerList = new ArrayList<>();
        for (Integer i = 1; i <=999; i++) {
            integerList.add(i);
        }
        displayPaginate(integerList);
    }
    private void displayPaginate(List<Integer> integerList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        fragmentMangaNewBinding.rcvPagination.setLayoutManager(linearLayoutManager);
        pageAdapter = new PageAdapter(integerList ,this::goToPage);
        fragmentMangaNewBinding.rcvPagination.setAdapter(pageAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void goToPage(int page, int indexOlderItem) {
        mainActivity.kProgressHUD.show();
        fragmentMangaNewBinding.tvCurrentPage.setText("page "+page);
        pageAdapter.notifyItemChanged(page - 1);
        pageAdapter.notifyItemChanged(indexOlderItem);
        getListMangaNew(page);

    }


    private void displayListMangaNew(List<MangaLatest> mangaList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.VERTICAL, false);
        fragmentMangaNewBinding.rcvMangaLatest.setLayoutManager(gridLayoutManager);
        mangaNewAdapter = new MangaNewAdapter(mangaList ,this::goToMangaDetail);
        fragmentMangaNewBinding.rcvMangaLatest.setAdapter(mangaNewAdapter);

    }

    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(getActivity(), MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      mainActivity.kProgressHUD.dismiss();
    }
}
