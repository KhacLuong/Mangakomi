package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemMangaGridBinding;
import com.example.mangakomi.util.callback.IOnClickMangaItemListener;
import com.example.mangakomi.model.Manga;

import java.util.List;

public class MangaGridAdapter extends RecyclerView.Adapter<MangaGridAdapter.MangaGridViewHolder>{
    private final List<Manga> mangaList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;

    public MangaGridAdapter(List<Manga> mangaList, IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }
    @NonNull
    @Override
    public MangaGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMangaGridBinding itemMangaGridBinding = ItemMangaGridBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaGridViewHolder(itemMangaGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaGridViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        if (manga==null)
            return;
        if(manga.getPoster_manga()==null||manga.getPoster_manga().isEmpty()){
            holder.itemMangaGridBinding.imgManga.setImageResource(R.drawable.img_no_image);
        }else {
//            Glide.with(holder.itemMangaGridBinding.imgManga.getContext())
//                    .load(manga.getPoster_manga())
//                    .error(R.drawable.img_no_image)
//                    .dontAnimate()
//                    .into(holder.itemMangaGridBinding.imgManga);
        }
        holder.itemMangaGridBinding.mangaName.setText(manga.getName_manga());
        holder.itemMangaGridBinding.tvTimeUpdate.setText(manga.getTime_update());


    }

    @Override
    public int getItemCount() {
        return null == mangaList?0:mangaList.size() ;
    }

    public static class  MangaGridViewHolder extends RecyclerView.ViewHolder{
        private final ItemMangaGridBinding itemMangaGridBinding;


        public MangaGridViewHolder(ItemMangaGridBinding itemMangaGridBinding) {
            super(itemMangaGridBinding.getRoot());
            this.itemMangaGridBinding = itemMangaGridBinding;
        }
    }
}
