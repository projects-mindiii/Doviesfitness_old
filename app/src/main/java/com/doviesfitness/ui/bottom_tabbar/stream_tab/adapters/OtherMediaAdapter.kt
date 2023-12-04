package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDetailModel
import kotlinx.android.synthetic.main.workout_group_item_view.view.*

class OtherMediaAdapter(context: Context, otherMediaList: ArrayList<StreamDetailModel.Settings.Data.OtherMedia>, listener:MediaClick):
    androidx.recyclerview.widget.RecyclerView.Adapter<OtherMediaAdapter.MyViewHolder>(){
    private var context:Context
    private var listener:MediaClick
    private var otherMediaList: List<StreamDetailModel.Settings.Data.OtherMedia>
    init {
        this.context = context
        this.otherMediaList = otherMediaList
        this.listener = listener
    }

        override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
            if (otherMediaList.get(pos).media_name != null)
                holder.exerciseName.text = "" + otherMediaList.get(pos).media_name
            holder.tvLevel.visibility = View.VISIBLE

            if (otherMediaList.get(pos).media_level != null)
                holder.tvLevel.text = "" + otherMediaList.get(pos).media_level



            if (otherMediaList.get(pos).media_image != null)
                Glide.with(context)
                    .load(otherMediaList.get(pos).media_image)
                    .error(ContextCompat.getDrawable(context, R.drawable.app_icon))
                    .into(holder.img)

           // holder.tvNewstream.visibility = View.GONE
            holder.itemLayout.setOnClickListener({
                listener.onMediaClick(pos)
            })

        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.other_media_stream_item_view,
                    parent,
                    false
                )
            )

        }

        override fun getItemCount(): Int {
            return otherMediaList.size
        }

        class MyViewHolder(view: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
            val img = view.exercise_img
            val exerciseName = view.exercise_name
            val itemLayout = view.item_layout
            val tvLevel = view.tv_level
            val lockIcon = view.lock_icon
            //val tvNewstream = view.tv_new_stream
            val tvNew = view.tv_new
        }

        interface MediaClick {
            public fun onMediaClick(pos:Int)
        }


}



