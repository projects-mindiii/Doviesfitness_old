package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ProgressPicsModel
import kotlinx.android.synthetic.main.progress_pic_item_layout.view.*

class ProgressPicAdapter(
    context: Context,
    listener: selectImage,
    picList: ArrayList<ProgressPicsModel>?
): androidx.recyclerview.widget.RecyclerView.Adapter<ProgressPicAdapter.MyViewHolder>() {

    lateinit var context:Context
    lateinit var listener:selectImage
    lateinit var picList: ArrayList<ProgressPicsModel>

    init {
        this.context=context
        this.listener=listener
        this.picList= picList!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.progress_pic_item_layout,parent,false))
    }
    override fun getItemCount(): Int {
        return picList.size+1
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int){
        if (pos==0){
            holder.deleteImg.visibility=View.GONE
            holder.selectPic.visibility=View.VISIBLE
            holder.selectPicLayout.visibility=View.VISIBLE
            holder.progressPic.visibility=View.GONE
        }else{
            if (picList.get(pos-1).from.equals("edit"))
                Glide.with(context).load(picList.get(pos-1).imageUrl).into(holder.progressPic)
            else
                Glide.with(context).load(picList.get(pos-1).fileName).into(holder.progressPic)

            holder.selectPic.visibility=View.GONE
            holder.selectPicLayout.visibility=View.GONE
            holder.progressPic.visibility=View.VISIBLE
            holder.deleteImg.visibility=View.VISIBLE
        }

        holder.itemView.setOnClickListener(){
            if (pos==0&&picList.size<4){
                listener.selectImage()
            }
        }
        holder.deleteImg.setOnClickListener(){
            if (pos!=0){
                listener.deleteImg(pos-1)
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    class  MyViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val progressPic=   view.progress_pic
        val deleteImg=   view.delete_img
        val selectPic=   view.select_pic
        val selectPicLayout=   view.select_pic_layout

    }

    interface selectImage{
        public fun selectImage()
        public fun deleteImg(pos:Int)
    }
}