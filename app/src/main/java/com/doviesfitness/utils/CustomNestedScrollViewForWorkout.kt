package com.doviesfitness.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class CustomNestedScrollViewForWorkout : NestedScrollView {
    private var isScrollingEnabled = true

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    fun setEnableScrolling(enabled: Boolean) {
        isScrollingEnabled = enabled
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isScrollingEnabled) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        return if (isScrollingEnabled) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }

    }
}
