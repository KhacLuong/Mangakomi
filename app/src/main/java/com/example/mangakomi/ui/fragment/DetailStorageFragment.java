package com.example.mangakomi.ui.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mangakomi.R;
import com.example.mangakomi.databinding.FragmentDetailStorageBinding;
import com.example.mangakomi.model.ChapterDownload;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.service.DbStorage.ChapterDownload.ChapterDownloadDb;
import com.example.mangakomi.service.DbStorage.MangaDownload.MangaDownloadDb;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.activity.MangaDetailStorageActivity;
import com.example.mangakomi.ui.activity.MangaGenresActivity;
import com.example.mangakomi.ui.adapter.ChapterStorageAdapter;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.example.mangakomi.util.event.ReloadListDataContentMangaEvent;
import com.example.mangakomi.util.event.ReloadListDataContentMangaStorageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class DetailStorageFragment extends Fragment {
    private FragmentDetailStorageBinding fragmentDetailStorageBinding;

    private MangaDetail mangaDetail;
    private boolean checkMaxLineContent;
    private ChapterStorageAdapter chapterStorageAdapter;
    private MangaDetailStorageActivity mangaDetailStorageActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDetailStorageBinding = FragmentDetailStorageBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.black));
        mangaDetailStorageActivity = (MangaDetailStorageActivity) getActivity();
        mangaDetailStorageActivity.showProgressHUD();
        getData();
//        assert mangaDetailActivity != null;
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//        try {
//            getMangaDetail();
//            eventListener();
//
//        } catch (Exception e) {
//            getActivity().onBackPressed();
//        }
            initListener();
        return fragmentDetailStorageBinding.getRoot();
    }

    private void initListener() {
        fragmentDetailStorageBinding.toolbar.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(getActivity(), MangaDetailStorageActivity.class) ;
            }
        });
        fragmentDetailStorageBinding.toolbar.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFunction.startActivity(getActivity(), MangaGenresActivity.class, IConstant.ACTION, IConstant.ACTION_SEARCH) ;
            }
        });
    }

    private void getData() {
        MangaDownload mangaDownload = MangaDownloadDb.getInstance(getActivity()).mangaDownloadDao().getMangaById(MangaDetailStorageActivity.mangaId);
        mangaDetailStorageActivity.mangaDownload = mangaDownload;
        if (mangaDownload == null) {

//            quay lai trang cu
        } else {
            displayManga(mangaDownload);

        }
        mangaDetailStorageActivity.hideKProgressHUD();
    }
    private void displayManga(MangaDownload mangaDownload) {
        checkMaxLineContent = false;
        if (mangaDownload.getTitle_manga() != null) {
            fragmentDetailStorageBinding.tvName.setText(mangaDownload.getTitle_manga());
        }

        fragmentDetailStorageBinding.tvAlternative.setText(null != mangaDownload.getAlternative_manga() ? mangaDownload.getAlternative_manga() : "");
        fragmentDetailStorageBinding.tvAlternative.setMaxLines(2);
        fragmentDetailStorageBinding.tvRank.setText(null != mangaDownload.getRank_manga() ? mangaDownload.getRank_manga() : "");
        fragmentDetailStorageBinding.tvRating.setText(null != mangaDownload.getRating_manga() ? mangaDownload.getRating_manga() : "");
        fragmentDetailStorageBinding.tvGenreName.setText(null != mangaDownload.getGenre_manga() ? mangaDownload.getGenre_manga() : "");
        fragmentDetailStorageBinding.tvGenreName.setMaxLines(2);
        fragmentDetailStorageBinding.tvStatus.setText(null != mangaDownload.getStatus_manga() ? mangaDownload.getStatus_manga() : "");
        fragmentDetailStorageBinding.tvType.setText(null != mangaDownload.getType_manga() ? mangaDownload.getType_manga() : "");
        fragmentDetailStorageBinding.tvContent.setText(null != mangaDownload.getSummary_manga() ? mangaDownload.getSummary_manga() : "");
        fragmentDetailStorageBinding.tvContent.setMaxLines(2);

//        String filePath = mangaDetailStorageActivity.getApplicationContext().getCacheDir().getPath() + "/image/" +mangaDownload.getTitle_manga().trim()+"/poster.jpg";
        File cacheDir = getActivity().getApplicationContext().getCacheDir();
        String filePath = cacheDir.getPath() + "/image/" + mangaDownload.getTitle_manga().trim()+"/poster.jpg";

//        String filePath = Environment.getExternalStorageDirectory() + "/Komi/"+mangaDownload.getTitle_manga().trim()+"/poster.jpg";

// Load ảnh từ tệp tin vào ImageView sử dụng Glide
        Glide.with(mangaDetailStorageActivity)
                .load(new File(filePath))
                .placeholder(R.drawable.img_no_image)
                .into(fragmentDetailStorageBinding.imgManga);
        displayMangaListChapter();

    }

    private void displayMangaListChapter() {
        List<ChapterDownload> chapterDownloads = ChapterDownloadDb.getInstance(getActivity()).chapterDownloadDao().getChaptersByMangaId(MangaDetailStorageActivity.mangaId);
        if (chapterDownloads==null){
            return;
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentDetailStorageBinding.rcvChapterLatest.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mangaDetailStorageActivity.getApplication(), DividerItemDecoration.VERTICAL);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xFFFFFFFF, 0xFFFFFFFF});
        drawable.setSize(0, 3);
        dividerItemDecoration.setDrawable(drawable);
        fragmentDetailStorageBinding.rcvChapterLatest.addItemDecoration(dividerItemDecoration);
        chapterStorageAdapter = new ChapterStorageAdapter(chapterDownloads, new ChapterStorageAdapter.IOnClickChapterItemListener() {
            @Override
            public void onClickItemChapter(ChapterDownload chapterDownload) {
                mangaDetailStorageActivity.kProgressHUD.show();
                goToChapter(chapterDownload);
            }
        });
        fragmentDetailStorageBinding.rcvChapterLatest.setAdapter(chapterStorageAdapter);
    }


    private void goToChapter(ChapterDownload chapter) {
        mangaDetailStorageActivity.currentChapter = chapter;
        mangaDetailStorageActivity.activityMangaDetailStorageBinding.viewpager2MangaDetail.setCurrentItem(1);
        EventBus.getDefault().post(new ReloadListDataContentMangaStorageEvent());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mangaDetailStorageActivity.hideKProgressHUD();
    }
}