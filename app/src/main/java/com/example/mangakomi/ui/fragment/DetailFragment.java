package com.example.mangakomi.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.mangakomi.model.ChapterDownload;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.service.DbStorage.ChapterDownload.ChapterDownloadDb;
import com.example.mangakomi.service.DbStorage.MangaDownload.MangaDownloadDb;
import com.example.mangakomi.service.DbStorage.MangaHistory.MangaHistoryDb;
import com.example.mangakomi.R;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.adapter.ChapterAdapter;
import com.example.mangakomi.service.api.ApiService;
import com.example.mangakomi.databinding.FramentMangaDetailBinding;
import com.example.mangakomi.util.event.ReloadIconBookMark;
import com.example.mangakomi.util.event.ReloadListBookMark;
import com.example.mangakomi.util.event.ReloadListDataContentMangaEvent;
import com.example.mangakomi.util.event.ReloadListDataHistory;
import com.example.mangakomi.model.MangaContent;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.model.MangaHistory;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private static final int REQUEST_CODE = 100;
    private FramentMangaDetailBinding framentMangaDetailBinding;
    private boolean checkMaxLineContent;
    private ChapterAdapter chapterAdapter;
    private MangaDetailActivity mangaDetailActivity;
    private MangaDetail mangaDetail;
    Bitmap bitmap;
    OutputStream outputFile1;
    private List<Bitmap> bitmapList;
    private MangaContent mangaContent;

    private String nameChapter;
    private int index = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        framentMangaDetailBinding = FramentMangaDetailBinding.inflate(inflater, container, false);
        mangaDetailActivity = (MangaDetailActivity) getActivity();
        assert mangaDetailActivity != null;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        try {
            getMangaDetail();
            eventListener();

        } catch (Exception e) {
            getActivity().onBackPressed();
        }

        return framentMangaDetailBinding.getRoot();
    }


    private void getMangaDetail() {
        ApiService.apiService.getMangaDetail(mangaDetailActivity.mangaLink).enqueue(new Callback<MangaDetail>() {
            @Override
            public void onResponse(Call<MangaDetail> call, Response<MangaDetail> response) {

                mangaDetail = response.body();
                try {
                    mangaDetailActivity.mangaDetail = (MangaDetail) mangaDetail.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                displayManga(mangaDetail);
                setDataAutoCompleteChapter(mangaDetail.getList_chapter());
            }

            @Override
            public void onFailure(Call<MangaDetail> call, Throwable t) {
                Toast.makeText(getActivity(), "err", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setDataAutoCompleteChapter(List<MangaDetail.Chapter> chapterList) {
        mangaDetailActivity.chapterNameList.clear();
        for (MangaDetail.Chapter chapter : chapterList) {
            if (chapter.getName_chapter() != null && !chapter.getName_chapter().isEmpty()) {
                mangaDetailActivity.chapterNameList.add(chapter.getName_chapter());
            }
        }
    }

    private void displayManga(MangaDetail mangaDetail) {


        checkMaxLineContent = false;
        if (mangaDetail.getTitle_manga() != null) {
            framentMangaDetailBinding.tvName.setText(mangaDetail.getTitle_manga());
        }

        framentMangaDetailBinding.tvAlternative.setText(null != mangaDetail.getAlternative_manga() ? mangaDetail.getAlternative_manga() : "");
        framentMangaDetailBinding.tvAlternative.setMaxLines(2);
        framentMangaDetailBinding.tvRank.setText(null != mangaDetail.getRank_manga() ? mangaDetail.getRank_manga() : "");
        framentMangaDetailBinding.tvRating.setText(null != mangaDetail.getRating_manga() ? mangaDetail.getRating_manga() : "");
        framentMangaDetailBinding.tvGenreName.setText(null != mangaDetail.getGenre_manga() ? mangaDetail.getGenre_manga() : "");
        framentMangaDetailBinding.tvGenreName.setMaxLines(2);
        framentMangaDetailBinding.tvStatus.setText(null != mangaDetail.getStatus_manga() ? mangaDetail.getStatus_manga() : "");
        framentMangaDetailBinding.tvType.setText(null != mangaDetail.getType_manga() ? mangaDetail.getType_manga() : "");
        framentMangaDetailBinding.tvContent.setText(null != mangaDetail.getSummary_manga() ? mangaDetail.getSummary_manga() : "");
        framentMangaDetailBinding.tvContent.setMaxLines(2);

        Picasso.get().load(mangaDetail.getPoster_manga()).fit()
                .placeholder(R.drawable.img_no_image)
                .error(R.drawable.img_no_image)
                .into(framentMangaDetailBinding.imgManga);
        displayMangaListChapter(mangaDetail.getList_chapter());

        setStatusBookMark(mangaDetail.title_manga);

    }

    private void setStatusBookMark(String mangaName) {
        if (mangaName == null || Objects.equals(mangaName, ""))
            return;
        MangaHistory mangaHistory = MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaBookMarkByName(mangaName);
        if (mangaHistory == null) {
            framentMangaDetailBinding.imgBookmark.setImageResource(R.drawable.bookmark_dark);
        } else {
            framentMangaDetailBinding.imgBookmark.setImageResource(R.drawable.bookmark_light);
        }
    }


    private void displayMangaListChapter(List<MangaDetail.Chapter> list_chapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        framentMangaDetailBinding.rcvChapterLatest.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mangaDetailActivity.getApplication(), DividerItemDecoration.VERTICAL);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xFFFFFFFF, 0xFFFFFFFF});
        drawable.setSize(0, 3);
        dividerItemDecoration.setDrawable(drawable);
        framentMangaDetailBinding.rcvChapterLatest.addItemDecoration(dividerItemDecoration);
        chapterAdapter = new ChapterAdapter(list_chapter, new ChapterAdapter.IOnClickChapterItemListener() {
            @Override
            public void onClickDownChapter(int index, String nameChapter) {
                Log.d("Test", "test" + index);
                Toast.makeText(getActivity(), "Test" + index, Toast.LENGTH_SHORT).show();
                downChapter(index, nameChapter);
            }

            @Override
            public void onClickItemChapter(int index) {
                goToChapter(index);
            }
        });
        framentMangaDetailBinding.rcvChapterLatest.setAdapter(chapterAdapter);
    }

    private void goToChapter(int index) {
        changeFragment(index);

    }

    //    download Chapter
    private void downChapter(int index, String nameChapter) {
        this.index = index;
        String chapterLink = mangaDetail.getList_chapter().get(index).getLink_chapter();

        ApiService.apiService.getContentChapter(chapterLink).enqueue(new Callback<MangaContent>() {
            @Override
            public void onResponse(Call<MangaContent> call, Response<MangaContent> response) {
                mangaContent = response.body();
                if (mangaContent == null || mangaContent.getImage() == null || mangaContent.getImage().size() == 0)
                    return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        if (getActivity()!=null && ContextCompat.checkSelfPermission(mangaDetailActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            savePoster();
                        }else {
                            askPermission2();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                saveManGaDownload(setMangaDownload(),mangaDetail.getList_chapter().get(index));

                            }
                        }).start();

                        for (String url : mangaContent.getImage()){
                            getData(url , nameChapter);
                        }

                    }
                }).start();

//            save data in to SQLite;




            }

            @Override
            public void onFailure(Call<MangaContent> call, Throwable t) {
            }
        });
    }

    private void savePoster() {
        Drawable drawable = framentMangaDetailBinding.imgManga.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        String folderPath = requireActivity().getApplicationContext().getCacheDir().getPath() + "/image/" +mangaDetail.getTitle_manga().trim();
        File myFolder = new File(folderPath);
        if (!myFolder.exists()) {
            myFolder.mkdirs();
        }
        File imageFile = new File(myFolder,  "/poster.jpg");
        if (!imageFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void getData(String path, String nameChapter) {
        InputStream inputStream = null;
        int responseCode = -1;
        try {
            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                inputStream = con.getInputStream();
                this.bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
             if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


//            Save Image into cache
                 new Thread(new Runnable() {
                     @Override
                     public void run() {
                         saveImage(bitmap, nameChapter);
                     }
                 }).start();

             }else {
                 this.nameChapter = nameChapter;
                 askPermission();
             }
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
    }

    private void saveManGaDownload(MangaDownload mangaDownload, MangaDetail.Chapter chapter) {
        MangaDownload manga;
        if(mangaDownload== null||mangaDownload.getTitle_manga()==null||mangaDownload.getTitle_manga().isEmpty()){
            return;
        }
        manga = MangaDownloadDb.getInstance(getActivity()).mangaDownloadDao().getMangaByName(mangaDownload.getTitle_manga());

        if (manga == null) {
            MangaDownloadDb.getInstance(getActivity()).mangaDownloadDao().insert(mangaDownload);
             MangaDownload manga2 = MangaDownloadDb.getInstance(getActivity()).mangaDownloadDao().getMangaByName(mangaDownload.getTitle_manga());
            Log.d("test list chapter", manga2.genre_manga);

            if (manga2!=null){
                   ChapterDownload chapterDownload = new ChapterDownload(chapter.getLink_chapter(), chapter.getName_chapter(), chapter.getRelease_date(), manga2.getId());
                   ChapterDownloadDb.getInstance(getActivity()).chapterDownloadDao().insert(chapterDownload);
                    List<ChapterDownload> chapterD = ChapterDownloadDb.getInstance(getActivity()).chapterDownloadDao().getAllChapter();
                   Log.d("test list chapter", chapterD.size()+"");

               }
        }
        else {
            ChapterDownload chap = ChapterDownloadDb.getInstance(getActivity()).chapterDownloadDao().getChapterByName(chapter.getName_chapter());
            if (chap==null){
                ChapterDownload chapterDownload = new ChapterDownload(chapter.getLink_chapter(), chapter.getName_chapter(), chapter.getRelease_date(), manga.getId());
                ChapterDownloadDb.getInstance(getActivity()).chapterDownloadDao().insert(chapterDownload);
                manga.setRating_manga(mangaDownload.getRating_manga());
                manga.setRank_manga(mangaDownload.getRank_manga());
                manga.setAlternative_manga(mangaDownload.getAlternative_manga());
                MangaDownloadDb.getInstance(getActivity()).mangaDownloadDao().updateManga(manga);
                List<ChapterDownload> chapterD = ChapterDownloadDb.getInstance(getActivity()).chapterDownloadDao().getAllChapter();

            }

//            EventBus.getDefault().post(new ReloadIconBookMark());
//            EventBus.getDefault().post(new ReloadListBookMark());
//            EventBus.getDefault().post(new ReloadListDataHistory());
        }
    }

    private MangaDownload setMangaDownload(){


        MangaDownload mangaDownload = new MangaDownload(mangaDetail.getTitle_manga(), mangaDetail.getRating_manga(), mangaDetail.getRank_manga(),mangaDetail.getAlternative_manga()
                ,mangaDetail.getGenre_manga(), mangaDetail.getType_manga(), mangaDetail.getStatus_manga(), mangaDetail.getSummary_manga());

        return mangaDownload;
    }


    private void askPermission() {
        ActivityCompat.requestPermissions(mangaDetailActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }
    private void askPermission2() {
        ActivityCompat.requestPermissions(mangaDetailActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==200){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    savePoster();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(mangaDetailActivity, "Please provide the required permissions", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    saveImage(bitmap, this.nameChapter);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(mangaDetailActivity, "Please provide the required permissions", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage(Bitmap bitmap, String nameChapter) {
        try {

            String folderPath = requireActivity().getApplicationContext().getCacheDir().getPath() + "/image/" +mangaDetail.getTitle_manga().trim()+"/"+nameChapter.trim();

            File myFolder = new File(folderPath);
            if (!myFolder.exists()) {
                myFolder.mkdirs();
            }
            File imageFile = new File(myFolder, System.currentTimeMillis() + ".jpg");
            FileOutputStream outputFile  = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputFile);
            outputFile.flush();
            outputFile.close();
        } catch (Exception e) {
        }
    }

    private void eventListener() {
        //        Click Read  more
        framentMangaDetailBinding.btnImgReadMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (checkMaxLineContent) {

                    framentMangaDetailBinding.tvContent.setMaxLines(2);
                    framentMangaDetailBinding.tvBtnReadmore.setText("read more");
                    framentMangaDetailBinding.btnImgReadMore.setImageResource(R.drawable.ic_baseline_arrow_drop_down_36);
                    checkMaxLineContent = false;
                } else {
                    framentMangaDetailBinding.tvContent.setMaxLines(Integer.MAX_VALUE);
                    framentMangaDetailBinding.tvBtnReadmore.setText("collapse");
                    framentMangaDetailBinding.btnImgReadMore.setImageResource(R.drawable.ic_baseline_arrow_drop_up_34);
                    checkMaxLineContent = true;
                }
            }
        });


        //        Click Read  first
        framentMangaDetailBinding.btnReadFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mangaDetail != null && mangaDetailActivity != null) {
                    mangaDetailActivity.manga_name = mangaDetail.getTitle_manga();
                    mangaDetailActivity.indexCurrentChapter = mangaDetail.getList_chapter().size() - 1;
                    mangaDetailActivity.chapter_name = mangaDetail.list_chapter.get(mangaDetailActivity.indexCurrentChapter).getName_chapter();
                    mangaDetailActivity.activityMangaBinding.viewpager2MangaDetail.setCurrentItem(1);
                }
            }
        });

        //     Click bookmarks
        framentMangaDetailBinding.imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaBookMarkByName(mangaDetail.getTitle_manga().trim()) == null) {
                    MangaHistory mangaHistory = new MangaHistory(mangaDetailActivity.mangaDetail.getTitle_manga().trim(), mangaDetailActivity.mangaLink.trim(), mangaDetailActivity.mangaDetail.getRank_manga().trim(), "", mangaDetailActivity.mangaDetail.rating_manga.trim(), mangaDetailActivity.mangaDetail.getPoster_manga(), 0, 1, 0);
                    updateHistory(mangaHistory);
                }
            }
        });

    }

    private void changeFragment(int indexChapter) {
        mangaDetailActivity.manga_name = mangaDetail.getTitle_manga();
        mangaDetailActivity.indexCurrentChapter = indexChapter;
        mangaDetailActivity.activityMangaBinding.viewpager2MangaDetail.setCurrentItem(1);
        EventBus.getDefault().post(new ReloadListDataContentMangaEvent());
    }


    private void updateHistory(MangaHistory mangaHistory) {
        if (mangaHistory == null || mangaHistory.getName() == null || mangaHistory.getName().trim().isEmpty()) {
            return;
        }
        MangaHistory manga = MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaByName(mangaHistory.getName().trim());
        if (manga == null) {
            MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().insertMangaHistory(mangaHistory);
        } else {
            manga.setLink(mangaHistory.getLink());
            manga.setStatusBookMark(1);
            manga.setIndexChapter(mangaHistory.getIndexChapter());
            manga.setChapter(mangaHistory.getChapter());
            manga.setRating(mangaHistory.getRating());
            manga.setRanking(manga.getRanking());
            MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().updateMangaHistory(manga);
            EventBus.getDefault().post(new ReloadIconBookMark());
            EventBus.getDefault().post(new ReloadListBookMark());
            EventBus.getDefault().post(new ReloadListDataHistory());
        }
    }

    //    EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadIconBookMark event) {
        setStatusBookMark(mangaDetailActivity.mangaDetail.title_manga);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


}
