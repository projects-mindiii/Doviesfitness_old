package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.VideoListModal
import com.doviesfitness.ui.room_db.MyVideoList
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.download_workout_video.view.*
import java.io.File

class DownLoadStreamWorkOutVideoAdapter(
    context: Context,
    favStreamList: ArrayList<DownloadedModal.ProgressModal>,
    onWorkOutVideoClick: OnWorkOutVideoClick
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<DownLoadStreamWorkOutVideoAdapter.MyViewHolder>() {
    private var context: Context
    private var downloadList: ArrayList<DownloadedModal.ProgressModal>
    private var onWorkOutVideoClick: OnWorkOutVideoClick

    init {
        this.context = context
        this.downloadList = favStreamList
        this.onWorkOutVideoClick = onWorkOutVideoClick
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val videoData = downloadList.get(pos)

        //val lastIndex = videoData.stream_video!!.lastIndexOf("/")
        //val downloadFileName = videoData.stream_video.substring(lastIndex + 1)
        if (videoData.isAddedQueue!=null&&videoData.isAddedQueue){
            holder.downloadIcon.visibility=View.VISIBLE
            holder.downloadIcon.setImageDrawable(context.resources.getDrawable(R.drawable.download_wait))
        }
        else{
            holder.downloadIcon.visibility=View.VISIBLE
            holder.downloadIcon.setImageDrawable(context.resources.getDrawable(R.drawable.complete_downloaded))
        }
      //  holder.downloadIcon.setImageResource(R.drawable.complete_downloaded)
        holder.name.text = videoData.stream_video_name
        holder.episode.text = videoData.stream_video_subtitle
        holder.description.text = videoData.stream_video_description
        holder.timeDuration.text = Constant.getStreamDuration(videoData.video_duration)

        Glide.with(context)
            .load(videoData.stream_video_image_url + "thumb/" + videoData.stream_video_image)
            .into(holder.videoThumb)
        holder.videoThumb.setOnClickListener {
            onWorkOutVideoClick.getWorkoutVideoClick( pos)
        }
        holder.mcdeletePost.setOnClickListener {
            onWorkOutVideoClick.deleteVideo( pos)
        }

      /*  val pathget =
            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + videoData.time_stemp_url
        val f = File(pathget)
        if (f.exists()) {
            holder.downloadIcon.setImageResource(R.drawable.abc_radio_check)
            holder.name.text = videoData.stream_video_name
            holder.episode.text = videoData.stream_video_subtitle
            holder.description.text = videoData.stream_video_description
            holder.timeDuration.text = videoData.video_duration
            Glide.with(context)
                .load(videoData.stream_video_image_url + "thumb/" + videoData.stream_video_image)
                .into(holder.videoThumb)

        } else {
            holder.mainView.visibility = View.GONE
        }

        holder.mcdeletePost.setOnClickListener {
            onWorkOutVideoClick.getWorkoutVideoClick(videoData, pos, "Delete")
        }

        holder.videoThumb.setOnClickListener {
            onWorkOutVideoClick.getWorkoutVideoClick(videoData, pos, "not")
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.download_workout_video,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return downloadList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val videoThumb = view.video_thumb
        val name = view.name
        val episode = view.episode
        val description = view.description
        val timeDuration = view.time_duration
        val downloadIcon = view.download_icon
        val mainView = view.main_view
        val mcdeletePost = view.mcdelete_post
        val loader = view.loader
    }

    interface OnWorkOutVideoClick {
        fun getWorkoutVideoClick( pos: Int)
        fun deleteVideo( pos: Int)
    }
}