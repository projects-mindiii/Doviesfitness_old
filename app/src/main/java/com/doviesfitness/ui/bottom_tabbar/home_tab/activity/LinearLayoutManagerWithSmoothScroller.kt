package com.doviesfitness.ui.bottom_tabbar.home_tab.activity

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import android.graphics.PointF
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics


class LinearLayoutManagerWithSmoothScroller(context: Context, orientation: Int, reverseLayout: Boolean) :
    androidx.recyclerview.widget.LinearLayoutManager(context, orientation, reverseLayout) {
    private val MILLISECONDS_PER_INCH = 10f
    constructor(context: Context): this(context,
        androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

    override fun smoothScrollToPosition(recyclerView: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State?, position: Int) {
        val smoothScroller = TopSnappedSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private inner class TopSnappedSmoothScroller(context: Context) : androidx.recyclerview.widget.LinearSmoothScroller(context) {


        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@LinearLayoutManagerWithSmoothScroller
                .computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            displayMetrics?.let {
                return MILLISECONDS_PER_INCH/displayMetrics.densityDpi
            }
            return super.calculateSpeedPerPixel(displayMetrics)
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
}