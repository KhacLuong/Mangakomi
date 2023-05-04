package com.example.mangakomi.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.service.DbStorage.MangaHistory.MangaHistoryDb;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.adapter.MangaHistoryAdapter;
import com.example.mangakomi.util.callback.ItemTouchHelperListener;
import com.example.mangakomi.databinding.FragmentBookmarkedBinding;
import com.example.mangakomi.util.event.ReloadListBookMark;
import com.example.mangakomi.util.event.ReloadListDataHistory;
import com.example.mangakomi.model.MangaHistory;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class BookMarkedFragment extends Fragment implements ItemTouchHelperListener {
    private FragmentBookmarkedBinding fragmentBookmarkedBinding;
    private List<MangaHistory> mangaList;
    private MangaHistoryAdapter mangaHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBookmarkedBinding = FragmentBookmarkedBinding.inflate(inflater, container, false);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mangaList = new ArrayList<>();
        initUi(mangaList);
        getListMangaBookMark();

        return fragmentBookmarkedBinding.getRoot();
    }

    private void initUi(List<MangaHistory> mangaList) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentBookmarkedBinding.rcvBookmark.setLayoutManager(gridLayoutManager);
        mangaHistoryAdapter = new MangaHistoryAdapter(mangaList ,this::goToMangaDetail);
        fragmentBookmarkedBinding.rcvBookmark.setAdapter(mangaHistoryAdapter);
    }
    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(getActivity(), MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getListMangaBookMark() {
        mangaList.clear();
        mangaList = MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getListMangaBookMarks();

        if (mangaList==null||mangaList.isEmpty()){
            fragmentBookmarkedBinding.tvNoData.setVisibility(View.VISIBLE);
            return;
        }
        mangaHistoryAdapter.setData(mangaList);
        mangaHistoryAdapter.notifyDataSetChanged();
        fragmentBookmarkedBinding.tvNoData.setVisibility(View.GONE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListBookMark event ){
        getListMangaBookMark();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    //     Xử lý xóa và undo
    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof MangaHistoryAdapter.MangaHistoryViewHolder){
            MangaHistory manga= mangaList.get(viewHolder.getAbsoluteAdapterPosition());
            int indexDelete = viewHolder.getBindingAdapterPosition();
            deleteHistory(manga);
//            remove
//            mangaHistoryAdapter.removeItem(indexDelete);

            Snackbar snackbar = Snackbar.make(fragmentBookmarkedBinding.layoutRoot, manga.getName() +"removed", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    undoHistory(manga);
                    if((indexDelete==0||indexDelete==mangaList.size())){
                        fragmentBookmarkedBinding.rcvBookmark.scrollToPosition(indexDelete);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void deleteHistory(MangaHistory mangaHistory){
        if(mangaHistory==null||mangaHistory.getId()==0){
            return;
        }
        MangaHistory manga =  MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaBookMarkByName(mangaHistory.getName().trim());
        if(manga == null){
            return;
        }else  {
            if (manga.getStatusHistory()==0){
                MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().deleteMangaHistory(manga);
            }else {
                manga.setStatusBookMark(0);
                MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().updateMangaHistory(manga);

            }
            EventBus.getDefault().post(new ReloadListDataHistory());
        }
    }
    private void undoHistory(MangaHistory manga){
        if(manga==null||manga.getName()==null){
            return;
        }
        MangaHistory mangaHistory =  MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaByName(manga.getName().trim());

        if(mangaHistory == null) {

            MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().insertMangaHistory(manga);
        }else {

            manga.setStatusHistory(1);
            MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().updateMangaHistory(manga);
        }

        EventBus.getDefault().post(new ReloadListDataHistory());
    }

    //    end  Xử lý xóa và undo
}
