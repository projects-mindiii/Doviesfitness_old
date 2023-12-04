package com.doviesfitness.ui.bottom_tabbar.home_tab.adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.CommentsActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data1
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.SocialMediaType
import kotlinx.android.synthetic.main.comments_view.view.*
import kotlinx.android.synthetic.main.links_layout_.view.iv_links_icons

class SocialMediaLinksAdapter(
    context: Activity, commetsList: ArrayList<SocialMediaType>, listener: SocialMediaLinkClick) :
    RecyclerView.Adapter<SocialMediaLinksAdapter.MyViewHolder>() {
    var mContext: Activity
    var commetsList: ArrayList<SocialMediaType>
    var listener: SocialMediaLinkClick

    init {
        this.mContext = context
        this.commetsList = commetsList
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.links_layout_, parent, false))
    }

    override fun getItemCount(): Int {
        return commetsList.size
    }
/*   "created_by": {
            "dg_devios_guest_id": "28",
            "creator_name": "Doviesfitness",
            "creator_profile_image": " https://s3.us-east-2.amazonaws.com/dovies-fitness-testing/customer_profile/xSC1UJfwN8X6e2I5.jpg" ,
            "sub_title": "testing.",
            "social_media_type": [
                {
                    "media_type": "Tiktok",
                    "media_user_name": "tktk",
                    "social_media_url": " https://www.tiktok.com/@tktk "
                },
                {
                    "media_type": "Facebook",
                    "media_user_name": "fb",
                    "social_media_url": " https://www.facebook.com/fb "
                },
                {
                    "media_type": "Instagram",
                    "media_user_name": "asaa",
                    "social_media_url": " https://www.instagram.com/asaa "
                }
            ]
        }
    }*/
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = commetsList.get(position)

        if (comment.media_type=="Tiktok"||comment.media_type=="tiktok"){
            Glide.with(mContext)
                .load(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.social_ico__details
                    )
                )
                .into(holder.iv_links_icons)

        }else if (comment.media_type=="Facebook"||comment.media_type=="facebook"){
            Glide.with(mContext)
                .load(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.facebook_ico__1_
                    )
                )
                .into(holder.iv_links_icons)
        }else if(comment.media_type=="Instagram"||comment.media_type=="instagram"){
            Glide.with(mContext)
                .load(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.instagram_ico_det

                    )
                )
                .into(holder.iv_links_icons)
        }else if(comment.media_type=="Twitter"||comment.media_type=="twitter"){
            Glide.with(mContext)
                .load(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.twitter_section__details
                    )
                )
                .into(holder.iv_links_icons)
        }else if(comment.media_type=="threeFit"||comment.media_type=="ThreeFit"){
            Glide.with(mContext)
                .load(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.threefitstyle_details
                    )
                )
                .into(holder.iv_links_icons)
        }else if(comment.media_type=="Youtube"||comment.media_type=="youtube"){
            Glide.with(mContext)
                .load(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.youtube_ico_details
                    )
                )
                .into(holder.iv_links_icons)
        }



        holder.itemView.setOnClickListener {
            listener.OnLinkClick(commetsList[position].social_media_url)

        }

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
var iv_links_icons=view.iv_links_icons
    }

    interface SocialMediaLinkClick {
        fun  OnLinkClick(url:String)
    }
}
