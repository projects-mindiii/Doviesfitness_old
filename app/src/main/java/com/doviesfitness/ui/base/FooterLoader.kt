package com.doviesfitness.ui.base

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.doviesfitness.R

class FooterLoader(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var mProgressBar: ProgressBar
    var mTransview: View=itemView.findViewById(R.id.vw_trans)
    init {
        mProgressBar = itemView.findViewById(R.id.progressbar)
//        mTransview = itemView.findViewById(R.id.vw_trans)
    }
}