package com.example.mangakomi.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mangakomi.service.DbStorage.MangaHistory.MangaHistoryDb;
import com.example.mangakomi.R;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.adapter.ChapterSpinnerAdapter;
import com.example.mangakomi.ui.adapter.MangaContentAdapter;

import com.example.mangakomi.service.api.ApiService;

import com.example.mangakomi.databinding.FragmentContentBinding;
import com.example.mangakomi.util.event.ReloadListDataContentMangaEvent;
import com.example.mangakomi.util.event.ReloadListDataHistory;
import com.example.mangakomi.model.MangaContent;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.model.MangaHistory;
import com.example.mangakomi.util.GlobalFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentMangaFragment extends Fragment {

    private FragmentContentBinding fragmentContentBinding;
    private MangaDetailActivity mangaDetailActivity;
    private MangaContentAdapter mangaContentAdapter;
    private List<String> imageList;
    private MangaContent mangaContent;
    private MangaDetail.Chapter currentChapter;
    private ArrayAdapter<String> adapterChapter;

    private ChapterSpinnerAdapter spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentContentBinding = FragmentContentBinding.inflate(inflater, container, false);
        mangaDetailActivity = (MangaDetailActivity) getActivity();
        if(mangaDetailActivity.chapterNameList!=null){
            adapterChapter = new ArrayAdapter<String>(getActivity(), R.layout.item_list_choice_chapter, mangaDetailActivity.chapterNameList);
        }


        try {
            imageList = new ArrayList<>();

            if(!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }

            setData(mangaDetailActivity.indexCurrentChapter);
            initRcvManga();
            setAutoComplete();
            eventListener();
//            setSpinner();
        }catch (Exception e){
            requireActivity().onBackPressed();
        }

        return fragmentContentBinding.getRoot();
    }

    private void eventListener() {
        fragmentContentBinding.btnBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangaDetailActivity.activityMangaBinding.viewpager2MangaDetail.setCurrentItem(0);
            }
        });

        fragmentContentBinding.btnImgNextChapter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(mangaDetailActivity.indexCurrentChapter>=1){
                    setData(--mangaDetailActivity.indexCurrentChapter);
//                    if(mangaActivity.indexCurrentChapter==1){
//                        fragmentContentBinding.btnImgNextChapter.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_36);
//                        fragmentContentBinding.btnImgNextChapter.setBackground(getActivity().);
//
//                    }
                }else {
                    Toast.makeText(getActivity(), "The end story", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fragmentContentBinding.btnImgBackChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangaDetailActivity.mangaDetail.getList_chapter()!=null&&mangaDetailActivity.indexCurrentChapter<= mangaDetailActivity.mangaDetail.getList_chapter().size()-2){
                    setData(++mangaDetailActivity.indexCurrentChapter);
                }else {
                    Toast.makeText(getActivity(), "Start story", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAutoComplete() {

        if(mangaDetailActivity.chapterNameList!=null){
            fragmentContentBinding.autoCompleteChapter.setAdapter(adapterChapter);
            fragmentContentBinding.autoCompleteChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i< mangaDetailActivity.mangaDetail.getList_chapter().size(); i++){
                        MangaDetail.Chapter chapter = mangaDetailActivity.mangaDetail.getList_chapter().get(i);
                        if (chapter!=null&&chapter.getName_chapter().trim().equals(item.trim())){
                            setData(i);
                            GlobalFunction.hideSoftKeyboard(getActivity());
                            mangaDetailActivity.indexCurrentChapter = i;
                            getMangaContent(chapter.getLink_chapter());
                            Toast.makeText(getActivity(),"Item: "+item,Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }


    }
    private void setSpinner(){
        if(mangaDetailActivity.chapterNameList!=null) {
            spinnerAdapter = new ChapterSpinnerAdapter(mangaDetailActivity.chapterNameList, new ChapterSpinnerAdapter.IOnClickChapterItemListener() {
                @Override
                public void onClickChapter(int index) {
                    fragmentContentBinding.autoCompleteChapter.setText(adapterChapter.getItem(index), true);
                    mangaDetailActivity.indexCurrentChapter = index;
                    MangaDetail.Chapter chapter = mangaDetailActivity.mangaDetail.getList_chapter().get(index);
                    getMangaContent(chapter.getLink_chapter());
                }
            });
            fragmentContentBinding.spinnerChapter.setAdapter((SpinnerAdapter) spinnerAdapter);

            fragmentContentBinding.layoutSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragmentContentBinding.layoutSpinner.getWidth()<200){
                        fragmentContentBinding.layoutSpinner.setMinimumWidth(300);
                        fragmentContentBinding.spinnerChapter.setMinimumWidth(300);
                    }
                }
            });

        }
    }

    private void resetInfoShow(MangaDetail.Chapter chapter) {
        mangaDetailActivity.chapter_name = chapter.getName_chapter();
        mangaDetailActivity.mangaLink = chapter.getLink_chapter();


    }

    private void setData(int currentIndexChapter) {
        try {
            if (mangaDetailActivity.mangaDetail!=null&& mangaDetailActivity.mangaDetail.getList_chapter().get(currentIndexChapter)!=null){
                currentChapter = mangaDetailActivity.mangaDetail.getList_chapter().get(currentIndexChapter);
            }
            try {
                fragmentContentBinding.tvNameManga.setText(mangaDetailActivity.mangaDetail.getTitle_manga().trim());
            }catch (Exception e){
                fragmentContentBinding.tvNameManga.setText("");
            }
            try {
                fragmentContentBinding.tvNameManga.setText(mangaDetailActivity.mangaDetail.getTitle_manga().trim());
            }catch (Exception e){
                fragmentContentBinding.tvNameManga.setText("");

            }
            fragmentContentBinding.autoCompleteChapter.setText(currentChapter.getName_chapter().trim());
            getMangaContent(currentChapter.getLink_chapter());
            MangaHistory mangaHistory = new MangaHistory(mangaDetailActivity.mangaDetail.getTitle_manga().trim(),mangaDetailActivity.mangaLink.trim(), mangaDetailActivity.mangaDetail.getRank_manga().trim(), currentChapter.getName_chapter().trim(), mangaDetailActivity.mangaDetail.rating_manga.trim(),mangaDetailActivity.mangaDetail.getPoster_manga(), 1, 0, currentIndexChapter);
            updateHistory(mangaHistory);
        }catch (Exception e){
            requireActivity().onBackPressed();
        }

    }
    private void initRcvManga(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        fragmentContentBinding.rcvManga.setLayoutManager(linearLayoutManager);
        mangaContentAdapter = new MangaContentAdapter(imageList );
        fragmentContentBinding.rcvManga.setAdapter(mangaContentAdapter);
    }


    private void getMangaContent(String link) {
//        MangaDetail.Chapter  chapter  =  mangaActivity.currentChapterList;
//        String link = chapter.getLink_chapter();
//        Log.d("abcd", link );

        ApiService.apiService.getContentChapter(link).enqueue(new Callback<MangaContent>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<MangaContent> call, Response<MangaContent> response) {
                 mangaContent = response.body();
                mangaContentAdapter.setData(mangaContent.getImage());
                mangaContentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MangaContent> call, Throwable t) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

            }
        });
    }

//    EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListDataContentMangaEvent event ){
        setData(mangaDetailActivity.indexCurrentChapter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void updateHistory(MangaHistory mangaHistory){
        if(mangaHistory==null){
            return;
        }
       MangaHistory manga =  MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaByName(mangaHistory.getName().trim());
        if(manga == null){
            MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().insertMangaHistory(mangaHistory);
        }else {

            manga.setStatusHistory(1);
            manga.setIndexChapter(mangaHistory.getIndexChapter());
            manga.setChapter(mangaHistory.getChapter());
            manga.setRating(mangaHistory.getRating());
            manga.setRanking(manga.getRanking());
            MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().updateMangaHistory(manga);
        }
        EventBus.getDefault().post(new ReloadListDataHistory());

    }

}
