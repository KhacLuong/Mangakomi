package com.example.mangakomi.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.FragmentContentStorageBinding;

import com.example.mangakomi.ui.activity.MangaDetailStorageActivity;
import com.example.mangakomi.ui.activity.MangaGenresActivity;
import com.example.mangakomi.ui.adapter.MangaContentAdapter;
import com.example.mangakomi.ui.adapter.MangaContentStorageAdapter;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.example.mangakomi.util.event.ReloadListDataContentMangaEvent;
import com.example.mangakomi.util.event.ReloadListDataContentMangaStorageEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class ContentStorageFragment extends Fragment {

    private FragmentContentStorageBinding fragmentContentBinding;
    private MangaDetailStorageActivity mangaDetailStorageActivity;
    private List<Bitmap> bitmapList;
    private MangaContentStorageAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private InterstitialAd mInterstitialAd;
    private Timer timer ;
    private TimerTask timerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentContentBinding = FragmentContentStorageBinding.inflate(inflater, container, false);


        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.black));
        mangaDetailStorageActivity = (MangaDetailStorageActivity) getActivity();
        mangaDetailStorageActivity.showProgressHUD();
        bitmapList = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        try {
            initUi();
            initRcvManga();
            getData();
            initListener();
        } catch (Exception e) {
            requireActivity().onBackPressed();
        }

        return fragmentContentBinding.getRoot();
    }

    private void loadAds() {

        MobileAds.initialize(getActivity(), initializationStatus -> {
        });


        @SuppressLint("VisibleForTests") AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getActivity(), getResources().getString(R.string.AdUnitId_interstitial_full_screen), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        } else {
                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


    }

    private void initListener() {
        ScaleAnimation scaleAnimationHide = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f);
        scaleAnimationHide.setDuration(500);

        ScaleAnimation scaleAnimationShow = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        scaleAnimationShow.setDuration(500);
        fragmentContentBinding.rcvManga.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 20 && linearLayoutManager.findFirstVisibleItemPosition()>0 ) {
                    fragmentContentBinding.btnFloating.hide();
                    if (fragmentContentBinding.toolbar.layoutToolbar.getVisibility()!=View.GONE){
                        fragmentContentBinding.toolbar.layoutToolbar.startAnimation(scaleAnimationHide);
                        fragmentContentBinding.toolbar.layoutToolbar.setVisibility(View.GONE);

                    }
                    if (fragmentContentBinding.layoutHeader.getVisibility()!=View.GONE){
                        fragmentContentBinding.layoutHeader.startAnimation(scaleAnimationHide);
                        fragmentContentBinding.layoutHeader.setVisibility(View.GONE);
                    }

                } else if(dy<0){
                    fragmentContentBinding.btnFloating.show();
                }
                if(linearLayoutManager.findFirstVisibleItemPosition() == 0 && dy<0&&linearLayoutManager.findFirstCompletelyVisibleItemPosition()<1){
                    if (fragmentContentBinding.layoutHeader.getVisibility()==View.GONE){
                        fragmentContentBinding.layoutHeader.startAnimation(scaleAnimationShow);
                        fragmentContentBinding.layoutHeader.setVisibility(View.VISIBLE);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        fragmentContentBinding.btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentContentBinding.rcvManga.smoothScrollToPosition(0);
                if (fragmentContentBinding.layoutHeader.getVisibility()==View.GONE){
                    fragmentContentBinding.layoutHeader.startAnimation(scaleAnimationShow);
                    fragmentContentBinding.layoutHeader.setVisibility(View.VISIBLE);
                }
            }
        });

        fragmentContentBinding.btnBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangaDetailStorageActivity.activityMangaDetailStorageBinding.viewpager2MangaDetail.setCurrentItem(0);
            }
        });

        fragmentContentBinding.toolbar.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(getActivity(), MangaDetailStorageActivity.class) ;
            }
        });
        fragmentContentBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(getActivity(), MangaGenresActivity.class, IConstant.ACTION, IConstant.ACTION_SEARCH) ;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        bitmapList.clear();
//        try {

//            File cacheDir = requireActivity().getApplicationContext().getCacheDir();
//            File imageFile = new File(cacheDir, "/image/"+mangaDetailStorageActivity.mangaDownload.getTitle_manga()+"/"+mangaDetailStorageActivity.currentChapter.getName_chapter());
//            FileInputStream fileInputStream = new FileInputStream(imageFile);
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//
//            List<String> imagePathList = (List<String>) objectInputStream.readObject();
//            for (String imagePath : imagePathList) {
//                FileInputStream fileInputStream1 = new FileInputStream(new File(cacheDir, imagePath));
//                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream1);
//                bitmapList.add(bitmap);
//            }
//
//            objectInputStream.close();
//            fileInputStream.close();
//
//            adapter.setData(bitmapList);
//            adapter.notifyDataSetChanged();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        loadAds();
//        String filePath = mangaDetailStorageActivity.getApplicationContext().getCacheDir().getPath() + "/image/" +mangaDetailStorageActivity.mangaDownload.getTitle_manga().trim();
        File cacheDir = getActivity().getApplicationContext().getCacheDir();
        String filePath = cacheDir.getPath() + "/image/" + mangaDetailStorageActivity.mangaDownload.getTitle_manga().trim() + "/" + mangaDetailStorageActivity.currentChapter.getName_chapter().trim();
//        String filePath = Environment.getExternalStorageDirectory() + "/Komi/"+mangaDetailStorageActivity.mangaDownload.getTitle_manga().trim()+"/"+mangaDetailStorageActivity.currentChapter.getName_chapter().trim();
        File imageFile = new File(filePath);
        File[] imageFiles = imageFile.listFiles();
        if (imageFiles != null) {
            for (File file : imageFiles) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                    bitmapList.add(bitmap);
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            adapter.setData(bitmapList);
            adapter.notifyDataSetChanged();
            mangaDetailStorageActivity.hideKProgressHUD();
        }
    }

    private void initUi() {

        if (mangaDetailStorageActivity.currentChapter != null) {
            fragmentContentBinding.tvNameManga.setText(mangaDetailStorageActivity.mangaDownload.getTitle_manga());
            fragmentContentBinding.tvChapter.setText(mangaDetailStorageActivity.currentChapter.getName_chapter());
        }
    }

    private void initRcvManga() {
        bitmapList = new ArrayList<>();
         linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentContentBinding.rcvManga.setLayoutManager(linearLayoutManager);
        adapter = new MangaContentStorageAdapter();
        fragmentContentBinding.rcvManga.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListDataContentMangaStorageEvent event) {
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mangaDetailStorageActivity.hideKProgressHUD();
    }






}