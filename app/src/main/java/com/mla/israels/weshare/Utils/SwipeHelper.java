package com.mla.israels.weshare.Utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by david on 05/08/2016.
 */
public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerAllRequestsAdapter adapter;

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public SwipeHelper(RecyclerAllRequestsAdapter adapter) {
        super(ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.dismissJob(viewHolder.getAdapterPosition());
    }
}
