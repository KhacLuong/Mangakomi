package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemHistoryBinding;
import com.example.mangakomi.databinding.ItemLatestUpdateBinding;
import com.example.mangakomi.databinding.ItemMangaLatestBinding;
import com.example.mangakomi.model.MangaHistory;

import java.util.List;

public class MangaHistoryAdapter extends RecyclerView.Adapter<MangaHistoryAdapter.MangaHistoryViewHolder> {

    private List<MangaHistory> mangaList;
    public final MangaHistoryAdapter.IOnClickMangaItemListener iOnClickMangaItemListener;

    public void setData(List<MangaHistory> mangaList) {
        this.mangaList = mangaList;
    }

    public MangaHistoryAdapter(List<MangaHistory> mangaList, MangaHistoryAdapter.IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }

    public interface IOnClickMangaItemListener {
        void onClickItemManga(String mangaLink);
    }

    @NonNull
    @Override
    public MangaHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding itemHistoryBinding = ItemHistoryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaHistoryViewHolder(itemHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaHistoryViewHolder holder, int position) {

        try {
            MangaHistory manga = mangaList.get(position);
            if (manga==null)
                return;
            if(manga.getPoster()==null||manga.getPoster().isEmpty()){
                holder.itemHistoryBinding.imgManga.setImageResource(R.drawable.img_no_image);
            }else {
                Glide.with(holder.itemHistoryBinding.imgManga.getContext())
                        .load(manga.getPoster())
                        .error(R.drawable.img_no_image)
                        .into(holder.itemHistoryBinding.imgManga);
            }
            holder.itemHistoryBinding.nameManga.setText(manga.getName());
            holder.itemHistoryBinding.nameManga.setMaxLines(2);

            if (manga.getChapter()!=null&& !manga.getChapter().equals("")){
                String strChapter =  manga.getChapter().trim();
                for (int i=strChapter.length()-2; i>=0;i-- ){
                    String chartAt = String.valueOf(strChapter.charAt(i));
                    if (chartAt.equals("/")){
                        String  chapter = strChapter.substring(i+1,strChapter.length()-1 );
                        holder.itemHistoryBinding.tvChapter.setText(chapter);
                        break;
                    }
                }
            }else {
                holder.itemHistoryBinding.tvChapter.setVisibility(View.GONE);
            }
            holder.itemHistoryBinding.tvRanking.setText(manga.getRanking());

            holder.itemHistoryBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickMangaItemListener.onClickItemManga(manga.getLink());
                }
            });
            if(manga.getStatusBookMark()==1){
                holder.itemHistoryBinding.iconBookmark.setImageResource(R.drawable.bookmark_light);
            }else {
                holder.itemHistoryBinding.iconBookmark.setImageResource(R.drawable.bookmark_dark);
            }
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return null == mangaList?0:mangaList.size();
    }

    public static class  MangaHistoryViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout foreGround;
        public final ItemHistoryBinding itemHistoryBinding;
        public MangaHistoryViewHolder(ItemHistoryBinding itemHistoryBinding) {
            super(itemHistoryBinding.getRoot());
            this.itemHistoryBinding = itemHistoryBinding;
            foreGround = itemHistoryBinding.layoutForeGround;
        }
    }

    public void removeItem(int index){
        mangaList.remove(index);
        notifyItemRemoved(index);
    }
    public void undoItem(MangaHistory manga, int index){
        mangaList.add(index, manga);
        notifyItemInserted(index);
    }
}
