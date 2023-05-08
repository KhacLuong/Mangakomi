package com.example.mangakomi.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemChapterLatestDetailBinding;
import com.example.mangakomi.model.ChapterDownload;
import com.example.mangakomi.model.MangaDetail;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.service.DbStorage.ChapterDownload.ChapterDownloadDb;
import com.example.mangakomi.service.DbStorage.MangaDownload.MangaDownloadDb;
import com.example.mangakomi.service.DbStorage.MangaHistory.MangaHistoryDb;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterLatestViewHolder> {

    private List<MangaDetail.Chapter> chapterList;
    private MangaDetail mangaDetail;
    public final ChapterAdapter.IOnClickChapterItemListener iOnClickChapterItemListener;

    public void setData(List<MangaDetail.Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    public ChapterAdapter(MangaDetail mangaDetail, List<MangaDetail.Chapter> chapterList, ChapterAdapter.IOnClickChapterItemListener iOnClickChapterItemListener) {
        this.mangaDetail = mangaDetail;
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
            final int  index = position;
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
            MangaDownload mangaDownload = MangaDownloadDb.getInstance(holder.itemView.getContext()).mangaDownloadDao().getMangaByName(mangaDetail.getTitle_manga());
            if (mangaDownload==null)
                return;
            ChapterDownload chapterDownload = ChapterDownloadDb.getInstance(holder.itemView.getContext()).chapterDownloadDao().getChaptersByNameAndMangaId(chapter.getName_chapter(), mangaDownload.getId());
            if(chapterDownload!=null && holder.itemChapterLatestDetailBinding.tvChapter.getText().toString().trim().equals(chapterDownload.getName_chapter().trim())){
                holder.itemChapterLatestDetailBinding.btnDownChapter.setImageResource(R.drawable.ic_baseline_file_download_done_24);
            }else {
                holder.itemChapterLatestDetailBinding.btnDownChapter.setImageResource(R.drawable.ic_baseline_download_24);
            }


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
