package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.os.SystemClock
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.FavStreamModel
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.stream_list_item_view.view.*
import java.util.ArrayList

class StreamListAdapter(context: Context,exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,listener:OnVideoClick): androidx.recyclerview.widget.RecyclerView.Adapter<StreamListAdapter.MyViewHolder>(){
    private var context:Context
   var listener:OnVideoClick
    var exerciseList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    private var mLastClickTime: Long = 0


    init {
        this.context=context
        this.listener=listener
        this.exerciseList=exerciseList
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        Glide.with(context).load(exerciseList.get(pos).stream_video_image_url+"thumb/"+exerciseList.get(pos).stream_video_image).into(holder.videoThumb)
        holder.name.text=exerciseList.get(pos).stream_video_name
        holder.episode.text=exerciseList.get(pos).stream_video_subtitle
        holder.progress.visibility=View.GONE

        holder.description.text = exerciseList.get(pos).stream_video_description
        holder.timeDuration.text = Constant.getStreamDuration(exerciseList.get(pos).video_duration)

        /* if (exerciseList.get(pos).seekTo==0L)
         {
             holder.progress.visibility=View.GONE
         }
         else{
             holder.progress.visibility=View.VISIBLE
         }
         holder.progress.max=exerciseList.get(pos).MaxProgress*1000
         holder.progress.progress= exerciseList.get(pos).seekTo.toInt()*/

        holder.mainLayout.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                }else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                    listener.onVideoClick(pos)
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.stream_list_item_view,parent,false))

    }
    override fun getItemCount(): Int {
        return exerciseList.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var mainLayout=view.main_layout
        var videoThumb=view.video_thumb
        var name=view.name
        var episode=view.episode
        var progress=view.progress
        val description = view.description
        val timeDuration = view.time_duration

    }

    interface OnVideoClick{
       public fun onVideoClick(pos: Int)
    }

}



