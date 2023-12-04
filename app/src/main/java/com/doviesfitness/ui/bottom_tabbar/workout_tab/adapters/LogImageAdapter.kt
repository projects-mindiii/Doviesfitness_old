package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.LogImageModel
import kotlinx.android.synthetic.main.log_image_item_view.view.*

class LogImageAdapter(
    context: Context, imageList: ArrayList<LogImageModel>,
    listener:OnSelectImage ):
    androidx.recyclerview.widget.RecyclerView.Adapter<LogImageAdapter.MyViewHolder>() {
    private var context: Context
    private var imageList = ArrayList<LogImageModel>()
    var listener:OnSelectImage
    init {
        this.context = context
        this.imageList = imageList
        this.listener=listener
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.progressPic.visibility=View.VISIBLE
        Glide.with(context).load(imageList.get(pos).image).into(holder.progressPic)
        if (imageList.get(pos).isSelected) {
            holder.borderLayout.visibility=View.VISIBLE
        }
        else{
            holder.borderLayout.visibility=View.GONE
        }
        holder.progressPic.setOnClickListener(){
            listener.onSelectImage(pos)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.log_image_item_view, parent, false))
    }

    override fun getItemCount(): Int {
         return imageList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val progressPic=   view.progress_pic
        val borderLayout=   view.border_layout
    }

    interface OnSelectImage{
        public fun onSelectImage(pos:Int);
    }
}
