package com.doviesfitness.ui.bottom_tabbar.home_tab.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.CommentsActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data1
import kotlinx.android.synthetic.main.comments_view.view.*

class CommentsAdapter(
    context: CommentsActivity, commetsList: ArrayList<Data1>, listener: CommentOnClick) :

    androidx.recyclerview.widget.RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {
    var mContext: CommentsActivity
    var commetsList: ArrayList<Data1>
    var listener: CommentOnClick

    init {
        this.mContext = context
        this.commetsList = commetsList
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comments_view, parent, false))
    }

    override fun getItemCount(): Int {
        return commetsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = commetsList.get(position)
        holder.tvComment.text = comment.news_comment
        holder.tvTime.text = comment.news_comment_posted_days
        if (comment.news_comment_delete_access.equals("1")) {
            holder.tvUsername.text = mContext.getDataManager().getUserInfo().customer_full_name
            Glide.with(holder.ivProfile.context).load(mContext.getDataManager().getUserInfo().customer_profile_image)
                .into(holder.ivProfile)
        } else {
            holder.tvUsername.text = comment.customer_name
            Glide.with(holder.ivProfile.context).load(comment.customer_profile_image).placeholder(R.drawable.user_img).into(holder.ivProfile)
        }

        holder.ivMore.setOnClickListener {
            listener.moreOnClick(comment, holder.adapterPosition)
        }
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val tvUsername = view.tv_username
        val tvComment = view.tv_comment
        val tvTime = view.tv_time
        val ivProfile = view.iv_profile
        val ivMore = view.iv_more
    }

    interface CommentOnClick {
        fun moreOnClick(data: Data1, position: Int)
    }
}
