package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamPlayedHistoryModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.stream_history_item_layout.view.*

class StreamHistoryAdapter(context: Context, historyList: ArrayList<StreamPlayedHistoryModel.Data>, listener:OnViewClick) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(){
    private var context: Context
    var historyList: ArrayList<StreamPlayedHistoryModel.Data>
    private var mLastClickTime: Long = 0
    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2
    var listener:OnViewClick
    init {
        this.context = context
        this.historyList = historyList
        this.listener=listener
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == historyList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        if (rvHolder is FooterLoader) {
            val loaderViewHolder = rvHolder as FooterLoader
            if (showLoader) {
                loaderViewHolder.mProgressBar.visibility = View.VISIBLE
            } else {
                loaderViewHolder.mProgressBar.visibility = View.GONE
            }
            return
        }

        val holder = rvHolder as MyViewHolder
          val historyData = historyList.get(pos)

          var name= CommanUtils.capitaliseName(historyData.video_title)
        holder.name.text = ""+name


        /*if (historyData.can_create_log.equals("Yes",true)){
         holder.editDeleteIcon.visibility=View.VISIBLE
        }
        else{
            holder.editDeleteIcon.visibility=View.GONE
        }*/
        holder.editDeleteIcon.visibility=View.VISIBLE
          var episode= CommanUtils.capitaliseName(historyData.video_subtitle)
        rvHolder.episode.text= ""+episode

        holder.editDeleteIcon.setOnClickListener {
            listener.onEditDeleteClick(pos)
        }
        holder.videoThumbLayout.setOnClickListener {
            listener.onCellClick(pos)
        }

        holder.description.text = historyData.video_description
          holder.dateTxt.text = historyData.created_date
          holder.timeDuration.text = Constant.getStreamDuration(historyData.video_duration)
          Glide.with(context).load(historyData.video_image ).into(holder.videoThumb)
      }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return MyViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.stream_history_item_layout,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return FooterLoader(
                    LayoutInflater.from(context).inflate(
                        R.layout.new_pagination_view,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val name = view.name
        val videoThumb = view.video_thumb
        val episode = view.episode
        val dateTxt = view.date_txt
        val description = view.description
        val timeDuration = view.time_duration
        val mainLayout = view.main_layout
        val editDeleteIcon = view.edit_delete_icon
        val videoThumbLayout = view.video_thumb_layout
    }

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }
    interface OnViewClick {
        public fun onEditDeleteClick(pos: Int)
        public fun onCellClick(pos: Int)
    }

}



