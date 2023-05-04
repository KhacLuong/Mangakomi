package com.example.mangakomi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.databinding.ItemChapterLatestDetailBinding;
import com.example.mangakomi.databinding.ItemGenresBinding;
import com.example.mangakomi.databinding.ItemLatestUpdateBinding;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> {
    private List<String> genresList;
    public final GenresAdapter.IOnClickGenresItemListener iOnClickGenresItemListener;

    public void setData(List<String> genresList) {
        this.genresList = genresList;
    }

    public GenresAdapter(List<String> chapterList, GenresAdapter.IOnClickGenresItemListener iOnClickItemGenres) {
        this.genresList = chapterList;
        this.iOnClickGenresItemListener = iOnClickItemGenres;
    }
    public interface IOnClickGenresItemListener {

        void onClickItemGenres(String genres);

    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGenresBinding itemGenresBinding = ItemGenresBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GenresViewHolder(itemGenresBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        String genres = genresList.get(position);
        if (genres==null)
            return;
        holder.itemGenresBinding.tvGenreName.setText(genres);

        holder.itemGenresBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickGenresItemListener.onClickItemGenres(genres);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == genresList?0:genresList.size();
    }

    public static class  GenresViewHolder extends RecyclerView.ViewHolder{
        private final ItemGenresBinding itemGenresBinding;
        public GenresViewHolder(ItemGenresBinding itemGenresBinding) {
            super(itemGenresBinding.getRoot());
            this.itemGenresBinding = itemGenresBinding;
        }
    }
}
