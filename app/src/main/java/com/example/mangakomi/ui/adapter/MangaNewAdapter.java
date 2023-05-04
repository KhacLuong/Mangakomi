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
import com.example.mangakomi.databinding.ItemMangaPopularBinding;
import com.example.mangakomi.model.MangaLatest;

import java.util.List;


public class MangaNewAdapter  extends RecyclerView.Adapter<MangaNewAdapter.MangaNewViewHolder>{
    private  List<MangaLatest> mangaList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;

    public void setData(List<MangaLatest> mangaList) {
        this.mangaList = mangaList;
    }

    public MangaNewAdapter(List<MangaLatest> mangaList, IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }
    public interface IOnClickMangaItemListener {
        void onClickItemManga(String mangaLink);

    }

    @NonNull
    @Override
    public MangaNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLatestUpdateBinding itemLatestUpdateBinding = ItemLatestUpdateBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaNewViewHolder(itemLatestUpdateBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaNewViewHolder holder, int position) {

        try{
            MangaLatest manga = mangaList.get(position);
            if (manga==null)
                return;
            if(manga.getPoster_manga()==null||manga.getPoster_manga().isEmpty()){
                holder.itemLatestUpdateBinding.imgManga.setImageResource(R.drawable.img_no_image);
            }else {
                Glide.with(holder.itemLatestUpdateBinding.imgManga.getContext())
                        .load(manga.getPoster_manga())
                        .error(R.drawable.img_no_image)
                        .dontAnimate()
                        .into(holder.itemLatestUpdateBinding.imgManga);
            }
            holder.itemLatestUpdateBinding.tvNameManga.setText(manga.getTitle_manga());
            holder.itemLatestUpdateBinding.tvNameManga.setMaxLines(2);


            if (manga.getLastest_chapter_manga()!=null&& !manga.getLastest_chapter_manga().equals("")){
                String strChapter =  manga.getLastest_chapter_manga().trim();
                for (int i=strChapter.length()-2; i>=0;i-- ){
                    String chartAt = String.valueOf(strChapter.charAt(i));
                    if (chartAt.equals("/")){
                        String  chapter = strChapter.substring(i+1,strChapter.length()-1 );
                        holder.itemLatestUpdateBinding.tvChapter.setText(chapter);
                        break;
                    }

                }
            }else {
                holder.itemLatestUpdateBinding.tvChapter.setVisibility(View.GONE);
            }
            holder.itemLatestUpdateBinding.tvTimeUpdate.setText(manga.getRelease_date_manga());
//            holder.itemLatestUpdateBinding.tvRating.setRating(Float.parseFloat(manga.getRating_manga()));



            holder.itemLatestUpdateBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
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

    public static class  MangaNewViewHolder extends RecyclerView.ViewHolder{
        private final ItemLatestUpdateBinding itemLatestUpdateBinding;
        public MangaNewViewHolder(ItemLatestUpdateBinding itemLatestUpdateBinding) {
            super(itemLatestUpdateBinding.getRoot());
            this.itemLatestUpdateBinding = itemLatestUpdateBinding;
        }
    }
}
