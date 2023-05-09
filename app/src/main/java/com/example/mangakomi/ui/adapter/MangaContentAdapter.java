package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemContentChapterBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaContentAdapter extends RecyclerView.Adapter<MangaContentAdapter.ContentChapterViewHolder> {
    public List<String> chapterContentList;

    public void setData(List<String> chapterContentList) {
        this.chapterContentList = chapterContentList;
    }

    public MangaContentAdapter(List<String> chapterContentList) {
        this.chapterContentList = chapterContentList;
    }

    @NonNull
    @Override
    public ContentChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContentChapterBinding itemContentChapterBinding = ItemContentChapterBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentChapterViewHolder(itemContentChapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentChapterViewHolder holder, int position) {
        try{
            String chapterContent = chapterContentList.get(position).trim();
            if(chapterContent==null||chapterContent.isEmpty()||chapterContent.equals("")){
                holder.itemContentChapterBinding.imgContent.setImageResource(R.drawable.img_no_image);
            }else {
                Picasso.get().load(chapterContent)
                        .placeholder(R.drawable.loading_gif)
                        .error(R.drawable.img_no_image)
                        .into(holder.itemContentChapterBinding.imgContent);
            }
        }catch (Exception ignored){
        }


    }

    @Override
    public int getItemCount() {
        return null == chapterContentList?0:chapterContentList.size();
    }

    public static class  ContentChapterViewHolder extends RecyclerView.ViewHolder{
        private final ItemContentChapterBinding itemContentChapterBinding;
        public ContentChapterViewHolder(ItemContentChapterBinding itemContentChapterBinding) {
            super(itemContentChapterBinding.getRoot());
            this.itemContentChapterBinding = itemContentChapterBinding;
        }
    }
}
