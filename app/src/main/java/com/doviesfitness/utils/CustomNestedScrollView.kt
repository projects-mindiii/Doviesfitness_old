package com.doviesfitness.utils

import android.content.Context
import androidx.core.widget.NestedScrollView

import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View


class CustomNestedScrollView : NestedScrollView {
    private var enableScrolling = true

    constructor(mContext: Context): super(mContext) {

    }

    constructor(mContext: Context, attrs: AttributeSet?): super(mContext,attrs) {

    }

    constructor(mContext: Context, attrs: AttributeSet?, defStyleAttr: Int): super(mContext,attrs,defStyleAttr) {

    }

    fun isEnableScrolling(): Boolean {
        return enableScrolling
    }

    fun setEnableScrolling(enableScrolling: Boolean) {
        this.enableScrolling = enableScrolling
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isEnableScrolling()) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isEnableScrolling()) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

}