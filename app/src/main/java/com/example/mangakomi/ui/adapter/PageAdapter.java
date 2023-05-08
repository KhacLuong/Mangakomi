package com.example.mangakomi.ui.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.R;
import com.example.mangakomi.databinding.ItemPaginationBinding;

import java.util.List;

public class PageAdapter  extends  RecyclerView.Adapter<PageAdapter.PaginationViewHolder>{
    private final List<Integer> integerList;
    public final IOnClickMangaItemListener iOnClickMangaItemListener;
    int indexCurrentPage;

    public PageAdapter(List<Integer> integerList, IOnClickMangaItemListener iOnClickMangaItemListener) {
        this.integerList = integerList;
        this.iOnClickMangaItemListener = iOnClickMangaItemListener;
        indexCurrentPage =0;
    }
    public interface IOnClickMangaItemListener {
        void onClickItemPage(Integer page, int indexOlderItem);

    }

    @NonNull
    @Override
    public PaginationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaginationBinding itemPaginationBinding = ItemPaginationBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaginationViewHolder(itemPaginationBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PaginationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(indexCurrentPage==position){
            holder.itemPaginationBinding.layoutItem.setBackgroundResource(R.drawable.bg_indicator_pagination_orange);
        }else {
            holder.itemPaginationBinding.layoutItem.setBackgroundResource(R.drawable.bg_indicator_pagination);
        }
        holder.itemPaginationBinding.tvPage.setText(String.valueOf(position+1));
        holder.itemPaginationBinding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickMangaItemListener.onClickItemPage( position+1, indexCurrentPage);
                v.setBackgroundResource(R.drawable.bg_indicator_pagination_orange);
                indexCurrentPage = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == integerList?0:integerList.size();
    }
    public static class  PaginationViewHolder extends RecyclerView.ViewHolder{
        private final ItemPaginationBinding itemPaginationBinding;
        public PaginationViewHolder(ItemPaginationBinding itemPaginationBinding) {
            super(itemPaginationBinding.getRoot());
            this.itemPaginationBinding = itemPaginationBinding;
        }
    }
}
