package com.doviesfitness.ui.bottom_tabbar.workout_tab.swipe_items;

/**
 * Created by Shivangi on 27-Feb-16.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

