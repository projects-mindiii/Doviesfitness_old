package com.doviesfitness.ui.profile.favourite.adapter

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.profile.favourite.modal.FavFeedDataModal
import com.doviesfitness.utils.double_click.DoubleClick
import com.doviesfitness.utils.double_click.DoubleClickListener
import kotlinx.android.synthetic.main.fav_feed_view.view.*

class FavFeedAdapter(context: Context, feedList:MutableList<FavFeedDataModal.Data>, feedListOnClick: FavFeedListOnClick) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    var mContext: Context
    var feedList: MutableList<FavFeedDataModal.Data>
    var feedListOnClick: FavFeedListOnClick
    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2
    var flag: Boolean = true
    private lateinit var description: SpannableString

    /* lateinit var featuredDatatemp: AllOtherThenFeatured
      var adapterPositionTemp:Int=0
      //////////
      val delay: Long = 1000 // 1 seconds after user stops typing
      val last_text_edit = longArrayOf(0)
      val handler = Handler()
      lateinit var input_finish_checker :Runnable*/

    init {
        this.mContext = context
        this.feedList = feedList
        this.feedListOnClick = feedListOnClick
       // setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == feedList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fav_feed_view, parent, false))
            }
            else -> {
                return FooterLoader(
                    LayoutInflater.from(mContext).inflate(
                        R.layout.pagination_item_loader,
                        parent,
                        false
                    )
                )

            }
        }
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        if (rvHolder is FooterLoader) {
            val loaderViewHolder = rvHolder as FooterLoader
            if (showLoader) {
                loaderViewHolder.mProgressBar.visibility = View.VISIBLE
            } else {
                loaderViewHolder.mProgressBar.visibility = View.GONE
            }
            return
        }
        val holder = rvHolder as ViewHolder

        val featuredData = feedList.get(p1)

        if(p1 == feedList.size - 1){
            holder.bottomView.visibility = View.VISIBLE
        }else{
            holder.bottomView.visibility = View.GONE
        }

        /*holder.binding().setVariable(BR.featuredData,featuredData)
        holder.binding().executePendingBindings()*/
        holder.tvComment.text = featuredData.news_creator_name
        holder.tvUserName.text = featuredData.news_creator_name
        holder.tvTime.text = featuredData.news_posted_days

        Glide.with(holder.ivProfile.context).load(featuredData.news_creator_profile_image).into(holder.ivProfile)
        Glide.with(holder.ivFeed.context).load(featuredData.news_image).into(holder.ivFeed)

        //"""""""news feed comments""""""""""""//
        if (featuredData.news_comment_allow.equals("1")) {
            holder.tvComment.visibility = View.VISIBLE
            if (featuredData.feed_comments_count.equals("0")) {
                holder.tvComment.text = mContext.getString(R.string.write_a_comment)
            } else {
                if (featuredData.feed_comments_count.equals("1")) {
                    holder.tvComment.text = "View " + featuredData.feed_comments_count + " comment"
                } else holder.tvComment.text = "View all " + featuredData.feed_comments_count + " comments"
            }
        } else {
            holder.ivComments.visibility = View.GONE
            holder.tvComment.visibility = View.GONE
        }

        /*news Feed likes*/
        if (featuredData.customer_likes.equals("0") || featuredData.customer_likes.equals("1")) {
            holder.tvLikes.text = featuredData.customer_likes + " " + mContext.getString(R.string.like)
        } else {
            holder.tvLikes.text = featuredData.customer_likes + " " + mContext.getString(R.string.likes)
        }


        /*News feed Description*/
        if (!featuredData.news_description.isEmpty()) {
            holder.rlDescription.visibility = View.VISIBLE
            //  holder.tvDescription.text =
            changeFontFamily(featuredData, holder.adapterPosition, holder.tvDescription)

        } else holder.rlDescription.visibility = View.GONE


        if (featuredData.news_fav_status.equals("0")) {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star))

        } else holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_active))


        if (featuredData.news_like_status.equals("0")) {
            holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite))
        }
        else holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_active))


        if (featuredData.is_new.equals("1")) {
            holder.tvNew.visibility = View.VISIBLE
        } else {
            holder.tvNew.visibility = View.GONE
        }

        if (featuredData.news_media_type.equals("Video")) {
            holder.iv_play.visibility = View.VISIBLE
        } else holder.iv_play.visibility = View.GONE

        /*ONClick*/
        holder.tvComment.setOnClickListener {
            feedListOnClick.tvCommentsOnclick(featuredData, holder.adapterPosition)
        }
        holder.ivComments.setOnClickListener {
            feedListOnClick.tvCommentsOnclick(featuredData, holder.adapterPosition)
        }


        holder.ivHeart.setOnClickListener {
            ////
            ///////
            if (featuredData.news_like_status.equals("0")) {
                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_active))

            } else {
                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite))

            }

            /* last_text_edit[0] = System.currentTimeMillis()

             if (featuredData.news_like_status.equals("0")) {
                 featuredData.news_like_status = "1"
                 val likeCount = featuredData.customer_likes.toInt() + 1
                 featuredData.customer_likes = likeCount.toString()
                 holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_active))
             }
             else{
                 featuredData.news_like_status = "0"
                 val likeCount = featuredData.customer_likes.toInt() -1
                 featuredData.customer_likes = likeCount.toString()
                 holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite))
             }
             startStopHandler(featuredData,holder.adapterPosition)*/
            //   featuredDatatemp=featuredData
            //   adapterPositionTemp=holder.adapterPosition
            ///////
            feedListOnClick.ivLikeOnclick(featuredData, p1, holder.ivHeart)
        }


        holder.ivFav.setOnClickListener {
            feedListOnClick.ivFavOnclick(featuredData, holder.adapterPosition, holder.ivFav)
        }

        holder.ivShare.setOnClickListener {
            feedListOnClick.ivShareOnclick(featuredData, holder.adapterPosition)
        }

/*
        holder.tvDescription.setOnClickListener {
            if (holder.tvDescription.text.trim().length > 160) {
              //  if (flag) {
                    feedListOnClick.tvDescriptioOnclick(featuredData, holder.adapterPosition)
                  //  flag=false
             //   }
            }
        }
*/

        holder.ivFeed.setOnClickListener(DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View) {
                if (featuredData.news_media_type.equals("Video")) {
                    //   if (flag) {
                    feedListOnClick.tvDescriptioOnclick(featuredData, holder.adapterPosition)
                    //  flag=false
                    //  }
                }
                // Constant.showCustomToast(mContext, "Single: " + featuredData.news_id)
            }

            /*double tap on news feed image news feed like api will call
             * if already image like nothing will happen
              * */
            override fun onDoubleClick(view: View) {
                if (featuredData.news_like_status.equals("0"))
                    feedListOnClick.ivLikeOnclick(featuredData, holder.adapterPosition, holder.ivHeart)
            }
        }))
    }
/*
    fun startStopHandler(data:AllOtherThenFeatured,pos:Int){

        input_finish_checker = Runnable {
            if (System.currentTimeMillis() > last_text_edit[0] + delay - 500) {
                Log.d("india other thread","india other thread...."+data.news_like_status+".......pos..."+pos)
                handler.removeCallbacks(input_finish_checker)
                feedListOnClick.ivLikeOnclick(data, pos, holder.ivHeart)


                //  adapter.notifyDataSetChanged();
            }
        }
        handler.postDelayed(input_finish_checker, delay)

    }
*/

    fun changeFontFamily(favFeedDataModal: FavFeedDataModal.Data, adapterPosition: Int, tvDescription: TextView
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        val name = SpannableString(favFeedDataModal.news_creator_name)
        name.setSpan(StyleSpan(Typeface.BOLD), 0, name.length, 0)
        builder.append(name)
        builder.append(" ")
        if ((builder.length + favFeedDataModal.news_description.length) > 150) {


            if(favFeedDataModal.news_description.length > 150){
                description = SpannableString(favFeedDataModal.news_description.substring(0, 150))
            }else{
                description = SpannableString(favFeedDataModal.news_description.substring(0, favFeedDataModal.news_description.length -1))
            }


           // val description = SpannableString(favFeedDataModal.news_description.substring(0, 150))
            builder.append(description)
            builder.append("...")
            val more = SpannableString(" more")

            /* more.setSpan(object : ClickableSpan() {
                 override fun onClick(widget: View) {showCustomToast(mContext,"Terms of use") }
             }, 0, more.length,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)*/

            more.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorGray5)), 0, more.length, 0)
            builder.append(more)


            var ss = SpannableString(builder)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    Log.d("spanable clicked", "spanable clicked")
                    feedListOnClick.tvDescriptioOnclick(favFeedDataModal, adapterPosition)
                    //   feedListOnClick.tvDescriptioOnclick(featuredData, adapterPosition)

                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setUnderlineText(false);
                    ds.color = ContextCompat.getColor(mContext, R.color.colorGray5)
                }
            }
            //ss.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorGray5)), builder.toString().length-5,builder.toString().length, 0)
            ss.setSpan(clickableSpan, builder.length - 5, builder.length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvDescription.setText(ss);
            tvDescription.setMovementMethod(LinkMovementMethod.getInstance());

        } else {
            val description = SpannableString(favFeedDataModal.news_description)
            builder.append(description)
            tvDescription.setText(builder);
        }
        return builder
    }


/*
    override fun getItemId(position: Int): Long {
        val featuredData = feedList.get(position)
        return featuredData.news_id.toLong()
    }
*/

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {
       // var binding1: ViewDataBinding

        val tvComment = view.tv_comments
        val ivProfile = view.iv_profile
        val ivFeed = view.iv_feed
        val tvUserName = view.tv_user_name
        val tvTime = view.tv_time
        val ivComments = view.iv_comment
        val ivShare = view.iv_share
        val ivFav = view.iv_fav
        val ivHeart = view.iv_heart
        val tvLikes = view.tv_likes
        val rlDescription = view.rl_description
        val tvDescription = view.tv_description
        val tvNew = view.tv_new
        val iv_play = view.iv_play
        val bottomView = view.bottom_view

/*
        init {
            binding1 = DataBindingUtil.bind(view)!!
        }
*/

        override fun onClick(v: View?) {

        }

/*
        fun binding(): ViewDataBinding {
            return binding1
        }
*/
    }

    interface FavFeedListOnClick {
        fun tvCommentsOnclick(featuredData: FavFeedDataModal.Data, pos: Int)
        fun ivLikeOnclick(featuredData: FavFeedDataModal.Data, pos: Int, ivHeart: ImageView)
        fun ivShareOnclick(featuredData: FavFeedDataModal.Data, pos: Int)
        fun ivFavOnclick(featuredData: FavFeedDataModal.Data, pos: Int, view: ImageView)
        fun tvDescriptioOnclick(featuredData: FavFeedDataModal.Data, pos: Int)
    }
}