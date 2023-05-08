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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.service.DbStorage.MangaHistory.MangaHistoryDb;
import com.example.mangakomi.ui.activity.MangaDetailActivity;
import com.example.mangakomi.ui.activity.SecondaryActivity;
import com.example.mangakomi.ui.adapter.MangaHistoryAdapter;
import com.example.mangakomi.util.callback.ItemTouchHelperListener;
import com.example.mangakomi.util.callback.RecyclerviewItemTouchHelper;
import com.example.mangakomi.databinding.FragmentGenresBinding;
import com.example.mangakomi.databinding.FragmentHistoriesBinding;
import com.example.mangakomi.databinding.FragmentHomeBinding;
import com.example.mangakomi.util.event.ReloadListDataHistory;
import com.example.mangakomi.model.MangaHistory;
import com.example.mangakomi.util.GlobalFunction;
import com.example.mangakomi.util.IConstant;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HistoriesFragment extends Fragment implements ItemTouchHelperListener {
    private FragmentHistoriesBinding fragmentHistoriesBinding;
    private List<MangaHistory> mangaList;
    private MangaHistoryAdapter mangaHistoryAdapter;
    private SecondaryActivity secondaryActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHistoriesBinding = FragmentHistoriesBinding.inflate(inflater, container, false);
        secondaryActivity = (SecondaryActivity) getActivity();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        mangaList = new ArrayList<>();
        initUi(mangaList);
        getListMangaHistory();
        eventListener();

        return fragmentHistoriesBinding.getRoot();
    }

    private void eventListener() {
        ItemTouchHelper.SimpleCallback  simpleCallback= new RecyclerviewItemTouchHelper(0, ItemTouchHelper.LEFT,this );
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(fragmentHistoriesBinding.rcvHistory);
    }

    private void initUi(List<MangaHistory> mangaList) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL, false);
        fragmentHistoriesBinding.rcvHistory.setLayoutManager(gridLayoutManager);
        mangaHistoryAdapter = new MangaHistoryAdapter(mangaList ,this::goToMangaDetail);
        fragmentHistoriesBinding.rcvHistory.setAdapter(mangaHistoryAdapter);
    }
    private void goToMangaDetail(String manga_link) {
        GlobalFunction.startActivity(getActivity(), MangaDetailActivity.class, IConstant.MANGA_LINK, manga_link);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListMangaHistory() {
        mangaList.clear();
        mangaList = MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getListMangaHistory();

        if (mangaList==null||mangaList.isEmpty()){
            fragmentHistoriesBinding.tvNoData.setVisibility(View.VISIBLE);

            mangaHistoryAdapter.setData(mangaList);
            mangaHistoryAdapter.notifyDataSetChanged();
            return;
        }
        Collections.reverse(mangaList);
        mangaHistoryAdapter.setData(mangaList);
        mangaHistoryAdapter.notifyDataSetChanged();
        fragmentHistoriesBinding.tvNoData.setVisibility(View.GONE);


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListDataHistory event ){
        getListMangaHistory();
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
            MangaHistory mangaHistory= MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaHistoryByName(manga.getName());
            Snackbar snackbar = Snackbar.make(fragmentHistoriesBinding.layoutRoot, manga.getName() +"removed", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoHistory(manga);
                    if((indexDelete==0||indexDelete==mangaList.size())){
                        fragmentHistoriesBinding.rcvHistory.scrollToPosition(indexDelete);
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
        MangaHistory manga =  MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().getMangaHistoryByName(mangaHistory.getName().trim());
        if(manga == null){
            return;
        }else  {
            if (manga.getStatusBookMark()==0){
                MangaHistoryDb.getInstance(getActivity()).mangaHistoryDAO().deleteMangaHistory(manga);
            }else {
                manga.setStatusHistory(0);
                manga.setChapter("");
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
