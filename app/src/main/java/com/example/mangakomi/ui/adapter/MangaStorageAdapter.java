package com.example.mangakomi.ui.adapter;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemContentChapterBinding;
import com.example.mangakomi.databinding.ItemHistoryBinding;
import com.example.mangakomi.model.MangaDownload;
import com.example.mangakomi.model.MangaHistory;
import com.example.mangakomi.service.DbStorage.MangaDownload.MangaDownloadDb;

import java.io.File;
import java.util.List;

public class MangaStorageAdapter extends RecyclerView.Adapter<MangaStorageAdapter.MangaStorageViewHolder>{

    private List<MangaDownload> mangaList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;

    public void setData(List<MangaDownload> mangaList) {
        this.mangaList = mangaList;
    }

    public MangaStorageAdapter(List<MangaDownload> mangaList, MangaStorageAdapter.IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.mangaList = mangaList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
    }

    public interface IOnClickMangaItemListener {
        void onClickItemManga(int  id);
    }

    @NonNull
    @Override
    public MangaStorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding itemHistoryBinding = ItemHistoryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MangaStorageViewHolder(itemHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaStorageViewHolder holder, int position) {
        try {
            MangaDownload manga = mangaList.get(position);
            if (manga==null)
                return;
            holder.itemHistoryBinding.nameManga.setText(manga.getTitle_manga());
            holder.itemHistoryBinding.nameManga.setMaxLines(2);

            holder.itemHistoryBinding.tvChapter.setVisibility(View.GONE);
            holder.itemHistoryBinding.tvRanking.setText(manga.getRank_manga());
            holder.itemHistoryBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickMangaItemListener.onClickItemManga(manga.getId());
                }
            });

//             String filePath = Environment.getExternalStorageDirectory() + "/Komi/" +manga.getTitle_manga().trim()+"/poster.jpg";
            String filePath =holder.itemView.getContext().getCacheDir().getPath() + "/image/" + manga.getTitle_manga().trim()+"/poster.jpg";




// Load ảnh từ tệp tin vào ImageView sử dụng Glide
            Glide.with(holder.itemView.getContext())
                    .load(new File(filePath))
                    .placeholder(R.drawable.img_no_image)
                    .into(holder.itemHistoryBinding.imgManga);
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return  null == mangaList?0:mangaList.size();
    }

    public static  class MangaStorageViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout foreGround;
        public final ItemHistoryBinding itemHistoryBinding;
        public MangaStorageViewHolder( ItemHistoryBinding itemHistoryBinding) {
            super(itemHistoryBinding.getRoot());
            this.itemHistoryBinding = itemHistoryBinding;
            foreGround = itemHistoryBinding.layoutForeGround;
        }


    }
    public void removeItem(int index){
        mangaList.remove(index);
        notifyItemRemoved(index);
    }
}
