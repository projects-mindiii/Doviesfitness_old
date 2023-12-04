package com.doviesfitness.utils.double_click

import android.view.View

interface DoubleClickListener {

    fun onSingleClick(view: View)

    /**
     * Called when the user make a double click.
     */
    fun onDoubleClick(view: View)
}
