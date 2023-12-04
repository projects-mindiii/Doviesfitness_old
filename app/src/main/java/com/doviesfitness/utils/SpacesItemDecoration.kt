package com.doviesfitness.utils

import androidx.recyclerview.widget.RecyclerView
import android.R.attr.top
import android.R.attr.bottom
import android.R.attr.right
import android.R.attr.left
import android.graphics.Rect
import android.view.View


class SpacesItemDecoration(space: Int): androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
    private val space: Int
    init {
        this.space=space
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
       /* outRect.top = space
        outRect.right = space
        outRect.bottom = space*/
        val position = parent.getChildAdapterPosition(view)
        // Add top margin only for the first item to avoid double space between items
      /*  if (parent.getChildLayoutPosition(view) == 0) {
            outRect.right = space
        }
        else*/
        if (position == parent.getAdapter()!!.getItemCount() - 1) {
            outRect.right = space
        } else {
            outRect.right = 0
        }
    }
}