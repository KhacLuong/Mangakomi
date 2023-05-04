package com.example.mangakomi.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.databinding.ItemChapterLatestDetailBinding;
import com.example.mangakomi.model.MangaDetail;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterLatestViewHolder> {

    private List<MangaDetail.Chapter> chapterList;
    public final ChapterAdapter.IOnClickChapterItemListener iOnClickChapterItemListener;

    public void setData(List<MangaDetail.Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    public ChapterAdapter(List<MangaDetail.Chapter> chapterList, ChapterAdapter.IOnClickChapterItemListener iOnClickChapterItemListener) {
        this.chapterList = chapterList;
        this.iOnClickChapterItemListener = iOnClickChapterItemListener;
    }
    public interface IOnClickChapterItemListener {
        void onClickDownChapter(int index, String nameChapter);
        void onClickItemChapter(int index);

    }
    @NonNull
    @Override
    public ChapterLatestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterLatestDetailBinding itemChapterLatestDetailBinding = ItemChapterLatestDetailBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChapterLatestViewHolder(itemChapterLatestDetailBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChapterLatestViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            MangaDetail.Chapter chapter = chapterList.get(position);
            if (chapter==null)
                return;
            if (chapter.getRelease_date()==null||chapter.getRelease_date().isEmpty()||chapter.getRelease_date().equals("")){
                holder.itemChapterLatestDetailBinding.tvTimeRelease.setText("NEW");
                holder.itemChapterLatestDetailBinding.tvTimeRelease.setTextColor(Color.parseColor("#E90505"));
            }else {
                holder.itemChapterLatestDetailBinding.tvTimeRelease.setText(chapter.getRelease_date());
                holder.itemChapterLatestDetailBinding.tvTimeRelease.setTextColor(Color.parseColor("#FF000000"));
            }

            holder.itemChapterLatestDetailBinding.tvChapter.setText(chapter.name_chapter);


            holder.itemChapterLatestDetailBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickChapterItemListener.onClickItemChapter(position);
                }
            });
            holder.itemChapterLatestDetailBinding.btnDownChapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickChapterItemListener.onClickDownChapter(position, chapter.name_chapter);
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

    public static class  ChapterLatestViewHolder extends RecyclerView.ViewHolder{
        private final ItemChapterLatestDetailBinding itemChapterLatestDetailBinding;
        public ChapterLatestViewHolder(ItemChapterLatestDetailBinding itemChapterLatestDetailBinding) {
            super(itemChapterLatestDetailBinding.getRoot());
            this.itemChapterLatestDetailBinding = itemChapterLatestDetailBinding;
        }
    }
}
