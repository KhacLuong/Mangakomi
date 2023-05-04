package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemMangaLatestBinding;
import com.example.mangakomi.databinding.ItemMangaPopularBinding;
import com.example.mangakomi.model.MangaLatest;

import java.util.List;


public class MangaLatestAdapter  extends RecyclerView.Adapter<MangaLatestAdapter.MangaLatestViewHolder>{
    private final List<MangaLatest> mangaList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;

    public MangaLatestAdapter(List<MangaLatest> mangaList, IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }
    public interface IOnClickMangaItemListener {
        void onClickItemManga(String manga_link);
    }


    @NonNull
    @Override
    public MangaLatestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMangaLatestBinding itemMangaLatestBinding = ItemMangaLatestBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaLatestViewHolder(itemMangaLatestBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MangaLatestViewHolder holder, int position) {

        try {
            MangaLatest manga = mangaList.get(position);
            if (manga==null)
                return;
            if(manga.getPoster_manga()==null||manga.getPoster_manga().isEmpty()){
                holder.itemMangaLatestBinding.imageView.setImageResource(R.drawable.img_no_image);
            }else {
                Glide.with(holder.itemMangaLatestBinding.imageView.getContext())
                        .load(manga.getPoster_manga())
                        .error(R.drawable.img_no_image)
                        .into(holder.itemMangaLatestBinding.imageView);
            }
            holder.itemMangaLatestBinding.tvContent.setText(manga.getTitle_manga());
            holder.itemMangaLatestBinding.tvContent.setMaxLines(2);
            holder.itemMangaLatestBinding.tvContent.setMinLines(2);

            if (manga.getLastest_chapter_manga()!=null&& !manga.getLastest_chapter_manga().equals("")){
                String strChapter =  manga.getLastest_chapter_manga().trim();
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
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return null == mangaList?0:mangaList.size();
    }

    public static class  MangaLatestViewHolder extends RecyclerView.ViewHolder{
        private final ItemMangaLatestBinding itemMangaLatestBinding;
        public MangaLatestViewHolder(ItemMangaLatestBinding itemMangaLatestBinding) {
            super(itemMangaLatestBinding.getRoot());
            this.itemMangaLatestBinding = itemMangaLatestBinding;
        }
    }
}
