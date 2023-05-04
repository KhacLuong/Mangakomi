package com.example.mangakomi.ui.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ActivityMangaStorageBinding;
import com.example.mangakomi.util.event.ReloadListDataMangaStorage;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.service.DbStorage.ChapterDownload.ChapterDownloadDb;
import com.example.mangakomi.service.DbStorage.MangaDownload.MangaDownloadDb;
import com.example.mangakomi.ui.adapter.MangaStorageAdapter;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.example.mangakomi.util.callback.ItemTouchHelperListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MangaStorageActivity extends AppCompatActivity implements ItemTouchHelperListener {
    public ActivityMangaStorageBinding activityMangaStorageBinding;
    private List<MangaDownload> mangaList;
    private MangaStorageAdapter mangaStorageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow2));
        activityMangaStorageBinding = ActivityMangaStorageBinding.inflate(getLayoutInflater());
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        setContentView(activityMangaStorageBinding.getRoot());
        mangaList = new ArrayList<MangaDownload>();
        initUi(mangaList);
        getData();



    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        mangaList.clear();
        mangaList = MangaDownloadDb.getInstance(getApplication()).mangaDownloadDao().getAllManga();
        if (mangaList == null || mangaList.isEmpty()) {
            activityMangaStorageBinding.tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        mangaStorageAdapter.setData(mangaList);
        mangaStorageAdapter.notifyDataSetChanged();
        activityMangaStorageBinding.tvNoData.setVisibility(View.GONE);
    }

    private void initUi(List<MangaDownload> mangaList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false);
        activityMangaStorageBinding.rcvStorage.setLayoutManager(linearLayoutManager);
        mangaStorageAdapter = new MangaStorageAdapter(mangaList ,this::goToMangaDetail);
        activityMangaStorageBinding.rcvStorage.setAdapter(mangaStorageAdapter);
    }

    private void goToMangaDetail(int id) {
        GlobalFunction.startActivity(MangaStorageActivity.this,MangaDetailStorageActivity.class, IConstant.KEY_VALUE, id+"");
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof MangaStorageAdapter.MangaStorageViewHolder){
            MangaDownload manga= mangaList.get(viewHolder.getAbsoluteAdapterPosition());
            int indexDelete = viewHolder.getBindingAdapterPosition();
            deleteStorageDb(manga);
            deleteStorageFile(manga);
//            remove
            mangaStorageAdapter.removeItem(indexDelete);

//            Snackbar snackbar = Snackbar.make(fragmentBookmarkedBinding.layoutRoot, manga.getName() +"removed", Snackbar.LENGTH_LONG);
//            snackbar.setAction("Undo", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    undoHistory(manga);
//                    if((indexDelete==0||indexDelete==mangaList.size())){
//                        fragmentBookmarkedBinding.rcvBookmark.scrollToPosition(indexDelete);
//                    }
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }

    private void deleteStorageFile(MangaDownload mangaDownload) {
        File cacheDir = getCacheDir();
        File imageDir = new File(cacheDir, "image");
        File fileToDelete = new File(imageDir, mangaDownload.getTitle_manga().trim());

        if (fileToDelete.delete()) {
            // File deleted successfully
        } else {
            // Failed to delete file
        }

    }

    private void deleteStorageDb(MangaDownload mangaDownload){
        if(mangaDownload==null||mangaDownload.getId()==0){
            return;
        }
        MangaDownload manga =  MangaDownloadDb.getInstance(this).mangaDownloadDao().getMangaById(mangaDownload.getId());
        if(manga == null){
            return;
        }else  {

            MangaDownloadDb.getInstance(this).mangaDownloadDao().deleteManga(mangaDownload.getId());
            ChapterDownloadDb.getInstance(this).chapterDownloadDao().deleteChapterByMangaId(mangaDownload.getId());
             EventBus.getDefault().post(new ReloadListDataMangaStorage());
        }



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListDataMangaStorage event ){
        getData();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    //get image
//    private void getImageFromCache(String path) {
//        File cacheDir = getCacheDir();
//
//        File myFolder = new File(cacheDir, "myFolder");
//
//        if (myFolder.isDirectory()) {
//            File[] files = myFolder.listFiles(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String name) {
//                    return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpeg");
//                }
//            });
//
//            // Files chứa danh sách các tệp ảnh
//            List<Bitmap> bitmaps = new ArrayList<>();
//            for (File file : files) {
//                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                bitmaps.add(bitmap);
//            }
//            if (!bitmaps.isEmpty()) {
//
//                activityMangaStorageBinding.image.setImageBitmap(bitmaps.get(0));
//            }
//        }



//        File[] files = cacheDir.listFiles(new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
//            }
//        });
//        List<Bitmap> bitmaps = new ArrayList<>();
//        for (File file : files) {
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            bitmaps.add(bitmap);
//        }
//        if (!bitmaps.isEmpty()) {
//
//            activityMangaStorageBinding.image.setImageBitmap(bitmaps.get(0));
//        }




}
