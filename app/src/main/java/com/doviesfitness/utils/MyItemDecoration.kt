package com.doviesfitness.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class MyItemDecoration(space: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
    private val space: Int

    init {
        this.space = space / 2
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || position == 1) {
        } else {
            outRect.top = space * 2
        }
        if (position % 2 == 0) {
            outRect.right = space
        } else {
            outRect.left = space
        }

/*
        if (position == parent.getAdapter()!!.getItemCount() - 1 || position == parent.getAdapter()!!.getItemCount() - 2) {
            outRect.bottom = 10
        } else {
            outRect.bottom = 0
        }
*/

    }
}