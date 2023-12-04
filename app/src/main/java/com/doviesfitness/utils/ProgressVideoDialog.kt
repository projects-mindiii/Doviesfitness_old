package com.doviesfitness.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import android.view.View
import android.view.Window
import com.doviesfitness.R
import kotlinx.android.synthetic.main.dialog_progress.*

class ProgressVideoDialog(context1: Context) : Dialog(context1) {
    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_progress)
        tv_video.visibility = View.VISIBLE
        loader.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context1, R.color.colorGray12),android.graphics.PorterDuff.Mode.SRC_IN)
    }

}