package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemLatestUpdateBinding;
import com.example.mangakomi.databinding.ItemMangaLatestBinding;
import com.example.mangakomi.model.MangaLatest;

import java.util.List;

public class MangaGenresAdapter extends RecyclerView.Adapter<MangaGenresAdapter.MangaGenresViewHolder>{
    private List<MangaLatest> mangaList;
    public final MangaGenresAdapter.IOnClickMangaItemListener iOnClickMangaItemListener;

    public void setData(List<MangaLatest> mangaList) {
        this.mangaList = mangaList;
    }

    public MangaGenresAdapter(List<MangaLatest> mangaList, MangaGenresAdapter.IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }

    @NonNull
    @Override
    public MangaGenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLatestUpdateBinding itemMangaLatestBinding = ItemLatestUpdateBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaGenresViewHolder(itemMangaLatestBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaGenresViewHolder holder, int position) {
        try {
            MangaLatest manga = mangaList.get(position);
            if (manga==null)
                return;
            if(manga.getPoster_manga()==null||manga.getPoster_manga().isEmpty()){
                holder.itemMangaLatestBinding.imgManga.setImageResource(R.drawable.img_no_image);
            }else {
                Glide.with(holder.itemMangaLatestBinding.imgManga.getContext())
                        .load(manga.getPoster_manga())
                        .error(R.drawable.img_no_image)
                        .into(holder.itemMangaLatestBinding.imgManga);
            }
            holder.itemMangaLatestBinding.tvNameManga.setText(manga.getTitle_manga());
            holder.itemMangaLatestBinding.tvNameManga.setMaxLines(2);
            holder.itemMangaLatestBinding.tvNameManga.setMinLines(2);
            String strChapter =  manga.getLastest_chapter_manga().trim();
            if (!strChapter.equals("")){
                for (int i=strChapter.length()-2; i>=0;i-- ){
                    String chartAt = String.valueOf(strChapter.charAt(i));
                    if (chartAt.equals("/")){
                        String  chapter = strChapter.substring(i+1,strChapter.length()-1 );
                        holder.itemMangaLatestBinding.tvChapter.setText(chapter);
                        break;
                    }

                }
            }else {
                holder.itemMangaLatestBinding.tvChapter.setVisibility(View.GONE);
            }

            holder.itemMangaLatestBinding.tvTimeUpdate.setText(manga.getRelease_date_manga());


            holder.itemMangaLatestBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickMangaItemListener.onClickItemManga(manga.getLink_manga());
                }
            });
        }catch (Exception ignored){
        }
    }

    @Override
    public int getItemCount() {
        return null == mangaList?0:mangaList.size();
    }

    public interface IOnClickMangaItemListener {

        void onClickItemManga(String mangaLink);

    }


    public static class  MangaGenresViewHolder extends RecyclerView.ViewHolder{
        private final ItemLatestUpdateBinding itemMangaLatestBinding;
        public MangaGenresViewHolder(ItemLatestUpdateBinding itemMangaLatestBinding) {
            super(itemMangaLatestBinding.getRoot());
            this.itemMangaLatestBinding = itemMangaLatestBinding;
        }
    }
}
