package com.doviesfitness.subscription

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.success_story_item.view.*

class SuccessStoryAdapter(
    context: Context,
    successStoryList: ArrayList<PackageListModel.Data.GetSuccessStory>
) : RecyclerView.Adapter<SuccessStoryAdapter.MyViewHolder>() {
    var mContext: Context
   var successStoryList: ArrayList<PackageListModel.Data.GetSuccessStory>
    init {
        this.mContext = context
        this.successStoryList=successStoryList
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.success_story_item, parent, false))
    }
    override fun getItemCount(): Int {
        return successStoryList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
       Picasso.with(mContext).load(successStoryList.get(pos).image).into(holder.image)
    }
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val  image = view.image


    }
}
