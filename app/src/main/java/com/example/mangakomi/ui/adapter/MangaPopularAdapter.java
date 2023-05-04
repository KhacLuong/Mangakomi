package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.R;
import com.example.mangakomi.util.callback.IOnClickMangaItemListener;
import com.example.mangakomi.databinding.ItemMangaPopularBinding;
import com.example.mangakomi.model.MangaPopular;
import com.squareup.picasso.Picasso;


import java.util.List;

public class MangaPopularAdapter extends RecyclerView.Adapter<MangaPopularAdapter.MangaPopularViewHolder>{
    private final List<MangaPopular> mangaList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;

    public MangaPopularAdapter(List<MangaPopular> mangaList, IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }


    @NonNull
    @Override
    public MangaPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMangaPopularBinding itemMangaPopularBinding = ItemMangaPopularBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaPopularViewHolder(itemMangaPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaPopularViewHolder holder, int position) {

        try{
            MangaPopular manga = mangaList.get(position);
            if (manga==null)
                return;
            if(manga.getPoster_manga()==null||manga.getPoster_manga().isEmpty()){
                holder.itemMangaPopularBinding.imgMangaPopular.setImageResource(R.drawable.img_no_image);
            }else {
                Picasso.get().load(manga.getPoster_manga()).fit()
                        .placeholder(R.drawable.img_no_image)
                        .error(R.drawable.img_no_image)
                        .into(holder.itemMangaPopularBinding.imgMangaPopular);
            }
            holder.itemMangaPopularBinding.nameManga.setText(manga.getTitle_manga());
            holder.itemMangaPopularBinding.nameManga.setMaxLines(2);



            if (manga.getLastest_chapter_manga()!=null&& !manga.getLastest_chapter_manga().equals("")) {
                String strChapter =  manga.getLastest_chapter_manga().trim();
                for (int i = strChapter.length() - 2; i >= 0; i--) {
                    String chartAt = String.valueOf(strChapter.charAt(i));
                    if (chartAt.equals("/")) {
                        String chapter = strChapter.substring(i + 1, strChapter.length() - 1);
                        holder.itemMangaPopularBinding.tvChapter.setText(chapter);
                        break;
                    }
                }
            }

//        xu ly danh gia sao


            holder.itemMangaPopularBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickMangaItemListener.onClickItemManga(manga.getLink_manga());
                }
            });
        }catch (Exception e){


        }
    }

    @Override
    public int getItemCount() {
        return null == mangaList?0:mangaList.size();
    }

    public static class  MangaPopularViewHolder extends RecyclerView.ViewHolder{
        private final ItemMangaPopularBinding itemMangaPopularBinding;
        public MangaPopularViewHolder(ItemMangaPopularBinding itemMangaPopularBinding) {
            super(itemMangaPopularBinding.getRoot());
            this.itemMangaPopularBinding = itemMangaPopularBinding;
        }
    }

}
