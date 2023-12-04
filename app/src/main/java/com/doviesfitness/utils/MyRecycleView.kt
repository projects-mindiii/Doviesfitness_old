package com.doviesfitness.utils

import android.content.Context
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent


class MyRecycleView : androidx.recyclerview.widget.RecyclerView {


    private var verticleScrollingEnabled = true

    fun enableVersticleScroll(enabled: Boolean) {
        verticleScrollingEnabled = enabled
    }

    fun isVerticleScrollingEnabled(): Boolean {
        return verticleScrollingEnabled
    }

    override fun computeVerticalScrollRange(): Int {

        return if (isVerticleScrollingEnabled()) super.computeVerticalScrollRange() else 0
    }


    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {

        return if (isVerticleScrollingEnabled()) super.onInterceptTouchEvent(e) else false

    }

    constructor(mContext: Context, attrs: AttributeSet?) : super(mContext, attrs) {
    }

    constructor(mContext: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(mContext, attrs, defStyleAttr) {

    }

}