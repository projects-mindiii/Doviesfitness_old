package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.FavStreamModel
import kotlinx.android.synthetic.main.workout_group_item_view.view.*

class DeleteStreamAdapter(context: Context): androidx.recyclerview.widget.RecyclerView.Adapter<DeleteStreamAdapter.MyViewHolder>(){
    private var context:Context

    init {
        this.context=context
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.stream_video_item_layout,parent,false))

    }
    override fun getItemCount(): Int {
        return 6
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    }


}



