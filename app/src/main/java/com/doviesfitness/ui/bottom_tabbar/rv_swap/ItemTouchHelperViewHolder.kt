package com.doviesfitness.ui.bottom_tabbar.rv_swap

interface ItemTouchHelperViewHolder {
    /**
     * Implementations should update the item view to indicate it's active state.
     */
    fun onItemSelected()


    /**
     * state should be cleared.
     */
    fun onItemClear()
}