package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.stream_video_item_layout.view.*
import java.io.File

class StreamVideoAdapter(context: Context, videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,listener:OnVideoClick,subListener: IsSubscribed) :
    androidx.recyclerview.widget.RecyclerView.Adapter<StreamVideoAdapter.MyViewHolder>() {
    private var context: Context
    var videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    var listener: OnVideoClick
    private var mLastClickTime: Long = 0
    private var isAdmin: String = ""
    var subListener: IsSubscribed

    init {
        this.context = context
        this.videoList = videoList
        this.listener = listener
        this.subListener=subListener
        setHasStableIds(true)
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
    }


    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val videoData = videoList.get(pos)

      //  holder.name.text = videoData.stream_video_name
       // holder.episode.text = videoData.stream_video_subtitle


        var name= CommanUtils.capitaliseName(videoData.stream_video_name)
        holder.name.text = ""+name

        var episode= CommanUtils.capitaliseName(videoData.stream_video_subtitle)
        holder.episode.text= ""+episode



        holder.description.text = videoData.stream_video_description
        holder.timeDuration.text = Constant.getStreamDuration(videoData.video_duration)
        Glide.with(context).load(videoData.stream_video_image_url + "thumb/" + videoData.stream_video_image).into(holder.videoThumb)

       // holder.loader.visibility=View.GONE
     //   holder.downloadLayout.visibility=View.GONE
        holder.downloadLayout.setOnClickListener({

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

            } else {
                mLastClickTime = SystemClock.elapsedRealtime()
                    listener.onVideoClickDownloadInpxl(pos, videoData)
            }
        })

        holder.loader.setOnClickListener({
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

            } else {
                mLastClickTime = SystemClock.elapsedRealtime()
                listener.onDownloadingClick(pos)
            }

        })

        holder.videoThumbLayout.setOnClickListener({
           // if ("Yes".equals(isAdmin) || "OPEN".equals(StreamDetailActivity.overViewTrailerData?.stream_workout_access_level, true)) {
                if ("Yes".equals(isAdmin) || !getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                        "0"
                    ) || "OPEN".equals(StreamDetailActivity.overViewTrailerData?.stream_workout_access_level, true)) {
                listener.downloadVideo(pos, videoData, holder.downloadIcon, holder.loader, holder, "forPlay")
            }
            else{
                subListener.isSubscribed()
            }



        })

       /* val lastIndex = videoData.stream_video.lastIndexOf("/")
        if (lastIndex > -1) {
           // val downloadFileName = videoData.stream_video.substring(lastIndex + 1)
            val pathget = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/"+videoData.timeStempUrl
            val f = File(pathget)
            if (f.exists()) {
                holder.downloadIcon.setImageResource(R.drawable.abc_radio_check);
            }else{
                holder.downloadIcon.setImageResource(R.drawable.ic_white_stream_download);
            }
        }*/


        if (videoData.isAddedToQueue!=null&&videoData.isAddedToQueue){
            holder.downloadLayout.visibility=View.VISIBLE
            holder.downloadIcon.setImageDrawable(context.resources.getDrawable(R.drawable.download_wait))
        }
        else{
            holder.downloadLayout.visibility=View.VISIBLE
            holder.downloadIcon.setImageDrawable(context.resources.getDrawable(R.drawable.icon_download_new))
        }

        if (videoData.Progress!=null&&videoData.Progress!=0&&videoData.Progress!=100){
            holder.loader.visibility=View.VISIBLE
            holder.downloadLayout.visibility=View.GONE
            holder.loader.max=100
            holder.loader.progress=videoData.Progress
        }
        else
        {
            holder.loader.visibility=View.GONE
            holder.downloadLayout.visibility=View.VISIBLE
            if (videoData.Progress!=null&& videoData.Progress==100){
                holder.downloadIcon.setImageResource(R.drawable.complete_downloaded);
                Log.d("Complete download","Complete download adapter..."+videoData.Progress)


            }

        }


     //  if ("Yes".equals(isAdmin) || "OPEN".equals(StreamDetailActivity.overViewTrailerData?.stream_workout_access_level, true)) {
       if ("Yes".equals(isAdmin) || !getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
               "0"
           ) ) {


           holder.lockImg.visibility=View.GONE
           holder.downloadLayout.visibility=View.VISIBLE
         //   binding.btnStatus.text = "Add to my plan"
        } else {

           if ("OPEN".equals(StreamDetailActivity.overViewTrailerData?.stream_workout_access_level, true))
           {
               holder.lockImg.visibility=View.GONE
               holder.downloadLayout.visibility=View.GONE
           }
           else{
               holder.lockImg.visibility=View.GONE
               holder.downloadLayout.visibility=View.GONE
           }


          /* if (videoData.downLoadUrl.equals("Paid", true)) {
               // binding.btnStatus.text = "UPGRADE TO VIEW PLAN"
            } else if ("Subscribers".equals(videoData.downLoadUrl, true)) {
             //   binding.btnStatus.text = "Subscribe"
            } else if ("FREE".equals(videoData.downLoadUrl, true)) {
              //  binding.btnStatus.text = "Add to my plan"
            }*/
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.stream_video_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val name = view.name
        val videoThumb = view.video_thumb
        val episode = view.episode
        val description = view.description
        val downloadIcon = view.download_icon
        val downloadLayout = view.download_layout
        val videoThumbLayout = view.video_thumb_layout
        val timeDuration = view.time_duration
        val loader = view.loader
        val mainLayout = view.main_layout
        val lockImg = view.lock_img
    }

    interface OnViewClick {
    }
    interface OnVideoClick{
        public fun onVideoClickDownloadInpxl(pos: Int,  videModal: StreamWorkoutDetailModel.Settings.Data.Video)
        public fun onDownloadingClick(pos: Int)
        fun downloadVideo(pos: Int, videoModal: StreamWorkoutDetailModel.Settings.Data.Video, view: ImageView, loader: ProgressBar, downloadingTxt: MyViewHolder, status :String)
    }
}



