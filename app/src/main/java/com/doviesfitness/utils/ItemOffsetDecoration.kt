package com.doviesfitness.utils

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.annotation.DimenRes
import androidx.annotation.NonNull
import android.view.View


class ItemOffsetDecoration(private val mItemOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(
        context.getResources().getDimensionPixelSize(
            itemOffsetId
        )
    )

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView,
        state: androidx.recyclerview.widget.RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}