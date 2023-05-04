package com.example.mangakomi.ui.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemContentChapterBinding;


import java.util.List;

public class MangaContentStorageAdapter extends RecyclerView.Adapter<MangaContentStorageAdapter.ContentChapterViewHolder> {
    public List<Bitmap> bitmapList;

    public void setData(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public MangaContentStorageAdapter() {

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
            Bitmap bitmap = bitmapList.get(position);
            if(bitmap==null||!bitmap.isRecycled()){
                holder.itemContentChapterBinding.imgContent.setImageResource(R.drawable.img_no_image);
            }else {
                Glide.with(holder.itemView.getContext())
                        .asBitmap()
                        .load(bitmapList)
                        .into(holder.itemContentChapterBinding.imgContent);
            }
        }catch (Exception ignored){
        }


    }

    @Override
    public int getItemCount() {
        return null == bitmapList?0:bitmapList.size();
    }

    public static class  ContentChapterViewHolder extends RecyclerView.ViewHolder{
        private final ItemContentChapterBinding itemContentChapterBinding;
        public ContentChapterViewHolder(ItemContentChapterBinding itemContentChapterBinding) {
            super(itemContentChapterBinding.getRoot());
            this.itemContentChapterBinding = itemContentChapterBinding;
        }
    }
}
