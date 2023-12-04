package com.doviesfitness.temp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.databinding.DownloadWorkoutVideoBinding
import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.utils.Constant
import com.google.android.exoplayer2.ui.PlayerNotificationManager

/**Created by Yashank Rathore on 18,December,2020 yashank.mindiii@gmail.com**/

abstract class TempDownLoadStreamWorkOutVideoAdapter(val videoList: MutableList<DownloadedVideoModal>) :
    RecyclerView.Adapter<TempDownLoadStreamWorkOutVideoAdapter.VideoHolder>() {
    private lateinit var context: Context

    inner class VideoHolder(val binding: DownloadWorkoutVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.videoThumb.setOnClickListener {
                onWorkOutVideoClick(adapterPosition)
            }
            binding.mcdeletePost.setOnClickListener {
                deleteVideo(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        context = parent.context
        val binding = DataBindingUtil.inflate<DownloadWorkoutVideoBinding>(
            LayoutInflater.from(context),
            R.layout.download_workout_video,
            parent,
            false
        )
        return VideoHolder(binding)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoHolder, position: Int){
        val videoData = videoList[position]
        //holder.binding.swipe.
        if (videoData.isAddedQueue){
            holder.binding.downloadIcon.visibility= View.VISIBLE
            holder.binding.downloadIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.download_wait))
        }
        else{
            holder.binding.downloadIcon.visibility= View.VISIBLE
            holder.binding.loader.visibility = View.GONE
            holder.binding.downloadIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.complete_downloaded))
        }

        Log.d("fankfnas", "onBindViewHolder: ${videoData.progress}")
        if(videoData.progress !=0 && videoData.progress !=100){
            holder.binding.loader.progress = videoData.progress
            holder.binding.downloadIcon.visibility = View.GONE
            holder.binding.loader.visibility = View.VISIBLE
        }

        holder.binding.name.text = videoData.stream_video_name
        holder.binding.episode.text = videoData.stream_video_subtitle
        holder.binding.description.text = videoData.stream_video_description
        holder.binding.timeDuration.text = Constant.getStreamDuration(videoData.video_duration)

        Glide.with(context)
            .load(videoData.stream_video_image_url + "thumb/" + videoData.stream_video_image)
            .into(holder.binding.videoThumb)

    }
    public fun setDownloadProgress(itemPosition: Int){
        notifyItemChanged(itemPosition)
    }

    fun refreshList(list: ArrayList<DownloadedVideoModal>){
        videoList.addAll(list)
        notifyDataSetChanged()
    }
    abstract fun onWorkOutVideoClick(itemPos: Int)
    abstract fun deleteVideo(itemPos: Int)
}