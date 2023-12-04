package com.doviesfitness.ui.profile.inbox.addapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.data.model.UserInfoBean
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data1
import kotlinx.android.synthetic.main.comments_view.view.*

class NotificationCommentsAdapter(
    context: Context,
    commetsList: ArrayList<Data1>,
    listener: CommentOnClick,
    userDataInfo: UserInfoBean,
    whichScreen: String
) :

    RecyclerView.Adapter<NotificationCommentsAdapter.MyViewHolder>() {
    var mContext: Context
    var commetsList: ArrayList<Data1>
    var listener: CommentOnClick
    var userDataInfo: UserInfoBean
    var whichScreen: String

    init {
        this.mContext = context
        this.commetsList = commetsList
        this.listener = listener
        this.userDataInfo = userDataInfo
        this.whichScreen = whichScreen
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        //return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comments_view, parent, false))
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_notification_comment_view, parent, false))
    }

    override fun getItemCount(): Int {
        if(whichScreen.equals("NotFull")){
            if(commetsList.size < 3){
                return commetsList.size
            }else{
                return 3
            }
        }else{
            return commetsList.size
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = commetsList.get(position)
        holder.tvComment.text = comment.news_comment
        holder.tvTime.text = comment.news_comment_posted_days
        if (comment.news_comment_delete_access.equals("1")) {
            holder.tvUsername.text = userDataInfo.customer_full_name
            Glide.with(holder.ivProfile.context).load(userDataInfo.customer_profile_image)
                .into(holder.ivProfile)
        } else {
            holder.tvUsername.text = comment.customer_name
            Glide.with(holder.ivProfile.context).load(comment.customer_profile_image).placeholder(R.drawable.new_user_place).into(holder.ivProfile);
        }

        holder.ivMore.setOnClickListener {
            listener.moreOnClick(comment, holder.adapterPosition)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
