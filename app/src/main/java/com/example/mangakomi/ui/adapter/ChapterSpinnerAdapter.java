package com.example.mangakomi.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.databinding.ItemChapterSpinnerBinding;
import com.example.mangakomi.databinding.ItemMangaPopularBinding;

import java.util.List;

public class ChapterSpinnerAdapter extends RecyclerView.Adapter<ChapterSpinnerAdapter.ChapterSpinnerViewHolder>{
    public List<String> chapterList;
    public final IOnClickChapterItemListener iOnClickChapterItemListener;

    public void setData(List<String> chapterList) {
        this.chapterList = chapterList;
    }

    public ChapterSpinnerAdapter(List<String> chapterList, IOnClickChapterItemListener iOnClickChapterItemListener) {
        this.chapterList = chapterList;
        this.iOnClickChapterItemListener = iOnClickChapterItemListener;
    }
    public interface IOnClickChapterItemListener {
        void onClickChapter(int index);

    }


    @NonNull
    @Override
    public ChapterSpinnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterSpinnerBinding itemChapterSpinnerBinding = ItemChapterSpinnerBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChapterSpinnerAdapter.ChapterSpinnerViewHolder(itemChapterSpinnerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterSpinnerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String chapter = chapterList.get(position);
        if (chapter!=null){
            holder.itemContentChapterBinding.tvChapter.setText(chapter);
            holder.itemContentChapterBinding.tvChapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickChapterItemListener.onClickChapter(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == chapterList?0:chapterList.size();
    }

    public static class  ChapterSpinnerViewHolder extends RecyclerView.ViewHolder{
        private final ItemChapterSpinnerBinding itemContentChapterBinding;
        public ChapterSpinnerViewHolder(ItemChapterSpinnerBinding itemContentChapterBinding) {
            super(itemContentChapterBinding.getRoot());
            this.itemContentChapterBinding = itemContentChapterBinding;
        }
    }
}
