package com.example.mangakomi.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mangakomi.databinding.FragmentContentStorageBinding;

import com.example.mangakomi.ui.activity.MangaDetailStorageActivity;
import com.example.mangakomi.ui.adapter.MangaContentAdapter;
import com.example.mangakomi.ui.adapter.MangaContentStorageAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ContentStorageFragment extends Fragment {

    private FragmentContentStorageBinding fragmentContentBinding;
    private MangaDetailStorageActivity mangaDetailStorageActivity;
    private List<Bitmap> bitmapList;
    private MangaContentStorageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentContentBinding = FragmentContentStorageBinding.inflate(inflater, container, false);
        mangaDetailStorageActivity = (MangaDetailStorageActivity) getActivity();
        bitmapList = new ArrayList<>();

        try {

            initUi();
            initRcvManga();
            getData();
        }catch (Exception e){
            requireActivity().onBackPressed();
        }

        return fragmentContentBinding.getRoot();
    }

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


        String filePath = mangaDetailStorageActivity.getApplicationContext().getCacheDir().getPath() + "/image/" +mangaDetailStorageActivity.mangaDownload.getTitle_manga().trim();
        File imageFile = new File(filePath, mangaDetailStorageActivity.currentChapter.getName_chapter().trim());
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
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
            fragmentContentBinding.rcvManga.setLayoutManager(linearLayoutManager);
            adapter = new MangaContentStorageAdapter();
            fragmentContentBinding.rcvManga.setAdapter(adapter);
    }



}