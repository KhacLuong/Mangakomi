package com.example.mangakomi.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.databinding.ItemChapterLatestDetailBinding;
import com.example.mangakomi.databinding.ItemChapterStorageBinding;
import com.example.mangakomi.model.ChapterDownload;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.service.DbStorage.ChapterDownload.ChapterDownloadDb;

import java.util.List;

public class ChapterStorageAdapter extends RecyclerView.Adapter<ChapterStorageAdapter.ChapterStorageViewHolder>{
    private List<ChapterDownload> chapterList;
    public final ChapterStorageAdapter.IOnClickChapterItemListener iOnClickChapterItemListener;

    public void setData(List<ChapterDownload> chapterList) {
        this.chapterList = chapterList;
    }

    public ChapterStorageAdapter(List<ChapterDownload> chapterList, IOnClickChapterItemListener iOnClickChapterItemListener) {
        this.chapterList = chapterList;
        this.iOnClickChapterItemListener = iOnClickChapterItemListener;
    }
    public interface IOnClickChapterItemListener {
        void onClickItemChapter(ChapterDownload chapterDownload);

    }
    @NonNull
    @Override
    public ChapterStorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterStorageBinding itemChapterStorageBinding = ItemChapterStorageBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChapterStorageViewHolder(itemChapterStorageBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChapterStorageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            ChapterDownload chapter = chapterList.get(position);
            if (chapter==null)
                return;
            if (chapter.getRelease_date()==null||chapter.getRelease_date().isEmpty()||chapter.getRelease_date().equals("")){
                holder.itemChapterStorageBinding.tvTimeRelease.setText("NEW");
                holder.itemChapterStorageBinding.tvTimeRelease.setTextColor(Color.parseColor("#E90505"));
            }else {
                holder.itemChapterStorageBinding.tvTimeRelease.setText(chapter.getRelease_date());
                holder.itemChapterStorageBinding.tvTimeRelease.setTextColor(Color.parseColor("#FF000000"));
            }

            holder.itemChapterStorageBinding.tvChapter.setText(chapter.name_chapter);


            holder.itemChapterStorageBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickChapterItemListener.onClickItemChapter(chapter);
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }



    }



    @Override
    public int getItemCount() {
        return null == chapterList?0:chapterList.size();
    }

    public static class  ChapterStorageViewHolder extends RecyclerView.ViewHolder{
        private final ItemChapterStorageBinding itemChapterStorageBinding;
        public ChapterStorageViewHolder(ItemChapterStorageBinding itemChapterStorageBinding) {
            super(itemChapterStorageBinding.getRoot());
            this.itemChapterStorageBinding = itemChapterStorageBinding;
        }
    }
}
