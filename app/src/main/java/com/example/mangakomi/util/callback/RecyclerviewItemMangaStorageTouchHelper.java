package com.example.mangakomi.util.callback;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangakomi.ui.adapter.MangaHistoryAdapter;
import com.example.mangakomi.ui.adapter.MangaStorageAdapter;

public class RecyclerviewItemMangaStorageTouchHelper extends ItemTouchHelper.SimpleCallback{
    private ItemTouchHelperListener listener;

    public RecyclerviewItemMangaStorageTouchHelper(int dragDirs, int swipeDirs, ItemTouchHelperListener listener) {

        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener!=null){
            listener.onSwipe(viewHolder);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){
            View foreGround = ((MangaStorageAdapter.MangaStorageViewHolder) viewHolder).foreGround;
            getDefaultUIUtil().onSelected(foreGround);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGround = ((MangaStorageAdapter.MangaStorageViewHolder) viewHolder).foreGround;
        getDefaultUIUtil().onDraw(c, recyclerView,foreGround,dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGround = ((MangaStorageAdapter.MangaStorageViewHolder) viewHolder).foreGround;
        getDefaultUIUtil().onDrawOver(c, recyclerView,foreGround,dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreGround = ((MangaStorageAdapter.MangaStorageViewHolder) viewHolder).foreGround;
        getDefaultUIUtil().clearView(foreGround);
    }
}
