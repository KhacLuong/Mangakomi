package com.example.mangakomi.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.mangakomi.R;
import com.example.mangakomi.ui.adapter.MangaGenresAdapter;
import com.example.mangakomi.ui.adapter.PageAdapter;
import com.example.mangakomi.service.api.ApiService;
import com.example.mangakomi.service.api.Server;
import com.example.mangakomi.databinding.ActivityMangaDetailBinding;
import com.example.mangakomi.databinding.ActivityMangaGenresBinding;
import com.example.mangakomi.databinding.FragmentMangaHotBinding;
import com.example.mangakomi.util.event.ReloadUiSearch;
import com.example.mangakomi.model.MangaGenres;
import com.example.mangakomi.model.MangaLatest;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaGenresActivity extends AppCompatActivity {
    private ActivityMangaGenresBinding activityMangaGenresBinding;
    private List<MangaLatest> mangaList;
    private int totalPage;
    private MangaGenresAdapter mangaGenresAdapter;
    private PageAdapter pageAdapter;
    private String action;
    private String keyword;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        activityMangaGenresBinding = ActivityMangaGenresBinding.inflate(getLayoutInflater());
        setContentView(activityMangaGenresBinding.getRoot());

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        mangaList = new ArrayList<>();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        getDataIntent();
        initListManga(mangaList);
        getManga(1);
        eventListener();


    }

    private void intUi() {
        activityMangaGenresBinding.toolbar.edtSearch.setVisibility(View.VISIBLE);
        activityMangaGenresBinding.toolbar.imgLogo.setVisibility(View.GONE);
    }

    private void eventListener() {
        activityMangaGenresBinding.toolbar.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    MangaGenresActivity.this.searchManga();
                    return true;
                }
                return false;
            }
        });
        activityMangaGenresBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchManga();
            }
        });

    }


    public void getManga(int page) {
        if (action.equals(IConstant.ACTION_GENRES)) {
            getMangaRenges(page);

        } else {
            activityMangaGenresBinding.tvCurrentPage.setVisibility(View.GONE);
            activityMangaGenresBinding.tvLastPage.setVisibility(View.GONE);
            // Search Manga
            eventSearch();
        }
    }


    private void eventSearch() {
        intUi();
        activityMangaGenresBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchManga();
            }
        });
    }

    private void searchManga() {

        if(activityMangaGenresBinding.toolbar.edtSearch.getVisibility() ==View.GONE){
            EventBus.getDefault().post(new ReloadUiSearch());
        }else {
            activityMangaGenresBinding.rcvPagination.setVisibility(View.GONE);
            activityMangaGenresBinding.tvCurrentPage.setVisibility(View.GONE);
            activityMangaGenresBinding.tvLastPage.setVisibility(View.GONE);
            String keySearch = activityMangaGenresBinding.toolbar.edtSearch.getText().toString().trim();
            GlobalFunction.hideSoftKeyboard(this);
            getMangaSearch(GlobalFunction.getTextSearch(keySearch));
        }

    }

    private void getMangaRenges(int page) {
        keyword = getIntent().getStringExtra(IConstant.KEY_VALUE).trim().toLowerCase(Locale.ROOT);
        activityMangaGenresBinding.toolbar.title.setText(keyword);
        String link = Server.HEADER_MANGA_GENRES + keyword.replace(" ", "-") + "/page/" + page;
        ApiService.apiService.getMangaGenres(link).enqueue(new Callback<MangaGenres>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<MangaGenres> call, @NonNull Response<MangaGenres> response) {
                mangaList.clear();
                MangaGenres mangaGenres = response.body();

                List<Object> objectList = mangaGenres.getCate_manga();
                Gson gson = new Gson();
                String json = gson.toJson(objectList);
                MangaLatest[] mangaArray = gson.fromJson(json, MangaLatest[].class);
                for (int i = 1; i < mangaArray.length; i++) {
                    MangaLatest mangaLatest = (MangaLatest) mangaArray[i];

                    mangaList.add(mangaLatest);
                }
                try {
                    totalPage = mangaGenres.getPage().getPage_last();
                    setDataPagination();
                } catch (Exception e) {
                    totalPage = 1;
                    setDataPagination();
                }
                mangaGenresAdapter.setData(mangaList);
                mangaGenresAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MangaGenres> call, Throwable t) {
            }
        });
    }

    private void getMangaSearch(String keySearch) {
        if (!kProgressHUD.isShowing()){
            kProgressHUD.show();
        }

        String link = Server.HEADER_MANGA_SEARCH_START + keySearch.trim() + Server.HEADER_MANGA_SEARCH_END;
        ApiService.apiService.getSearch(link).enqueue(new Callback<List<MangaLatest>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<MangaLatest>> call, @NonNull Response<List<MangaLatest>> response) {
                List<MangaLatest> mangaLatests = response.body();
                mangaGenresAdapter.setData(mangaLatests);
                mangaGenresAdapter.notifyDataSetChanged();
                if (kProgressHUD.isShowing()){
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<MangaLatest>> call, Throwable t) {
                if (kProgressHUD.isShowing()){
                    kProgressHUD.dismiss();
                }
            }
        });
    }

    private void initListManga(List<MangaLatest> mangaList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        activityMangaGenresBinding.rcvManga.setLayoutManager(gridLayoutManager);
        mangaGenresAdapter = new MangaGenresAdapter(mangaList, this::goToMangaDetail);
        activityMangaGenresBinding.rcvManga.setAdapter(mangaGenresAdapter);
    }

    private void setDataPagination() {
        if (totalPage>=10){
            activityMangaGenresBinding.tvLastPage.setVisibility(View.VISIBLE);
        }
        List<Integer> integerList = new ArrayList<>();
        for (Integer i = 1; i <= totalPage; i++) {
            integerList.add(i);
        }
        displayPaginate(integerList);
    }

    private void displayPaginate(List<Integer> integerList) {
        if (pageAdapter==null){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            activityMangaGenresBinding.rcvPagination.setLayoutManager(linearLayoutManager);
            pageAdapter = new PageAdapter(integerList, this::goToPage);
            activityMangaGenresBinding.rcvPagination.setAdapter(pageAdapter);
        }
    }

    private void goToPage(Integer page, int indexOlderItem) {
        activityMangaGenresBinding.tvCurrentPage.setText(String.valueOf("page " + page + "/" + totalPage));

        pageAdapter.notifyItemChanged(indexOlderItem);
        pageAdapter.notifyItemChanged(page-1 );

        getManga(page);

    }

    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(this, MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);
    }

    private void getDataIntent() {
        action = getIntent().getStringExtra(IConstant.ACTION);

    }

    //    EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadUiSearch event ){
        intUi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        activityMangaGenresBinding.toolbar.imgLogo.setVisibility(View.VISIBLE);
//    }
}