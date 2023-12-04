package com.doviesfitness.temp

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.WatchOrDownloadVideoLayoutBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.isalloweduser
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.videoList
import com.doviesfitness.ui.bottom_tabbar.stream_tab.temp.ExclusiveInterface
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant

/**Created by Yashank Rathore on 16,December,2020 yashank.mindiii@gmail.com **/

abstract class TempStreamVideoAdapter(
    val subListener: IsSubscribed,
    var execlusive: ExclusiveInterface
)
    : RecyclerView.Adapter<TempStreamVideoAdapter.SteamHolder>() {
    lateinit var context: Context
    private var mLastClickTime: Long = 0
    val isAdmin by lazy {
        Doviesfitness.getDataManager()
            .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)
    }

    inner class SteamHolder(val binding: WatchOrDownloadVideoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.downloadLayout.setOnClickListener {
                Log.d("vcvxcxcxcx", ": downloadLayout")
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                onVideoClickDownloadInpxl(adapterPosition)
            }

            binding.loader.setOnClickListener {
                Log.d("vcvxcxcxcx", "setOnClickListener: progressLayout")


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    Log.d("fnaknfa", "setOnClickListener: $mLastClickTime")
                    return@setOnClickListener
                }
                Log.d("fnaknfa", "setOnClickListener: INNER ")
                mLastClickTime = SystemClock.elapsedRealtime()
                if (!"Yes".equals(isAdmin)) {
                    if ("LOCK" == StreamDetailActivity.overViewTrailerData?.stream_workout_access_level &&
                        "Exclusive" == StreamDetailActivity.overViewTrailerData?.access_level
                    ) {
                        execlusive.isExclusive(StreamDetailActivity.overViewTrailerData?.program_redirect_url)
                    } else if ("LOCK" == StreamDetailActivity.overViewTrailerData?.stream_workout_access_level &&
                        "Paid" == StreamDetailActivity.overViewTrailerData?.access_level
                    ) {
                        execlusive.isExclusive(StreamDetailActivity.overViewTrailerData?.program_redirect_url)
                    } else {
                        onDownloadingClick(adapterPosition)
                    }
                }else{
                    onDownloadingClick(adapterPosition)
                }
            }

            binding.videoThumbLayout.setOnClickListener {
                Log.d("vcvxcxcxcx", "setOnClickListener: videoThumbLayout")
                //check app is admin or not
                if (!"Yes".equals(isAdmin)) {
                if ("LOCK" == StreamDetailActivity.overViewTrailerData?.stream_workout_access_level &&
                    "Exclusive" == StreamDetailActivity.overViewTrailerData?.access_level) {
                    execlusive.isExclusive(StreamDetailActivity.overViewTrailerData?.program_redirect_url)
                } else if ("LOCK" == StreamDetailActivity.overViewTrailerData?.stream_workout_access_level &&
                    "Paid" == StreamDetailActivity.overViewTrailerData?.access_level) {
                    execlusive.isExclusive(StreamDetailActivity.overViewTrailerData?.program_redirect_url)
                }else{
                    if ("Yes".equals(isAdmin) || !Doviesfitness.getDataManager()
                            .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)
                            .equals(
                                "0"
                            ) || "OPEN".equals(
                            StreamDetailActivity.overViewTrailerData?.stream_workout_access_level,
                            true
                        )|| StreamDetailActivity.isalloweduser==true
                    ) {
                        onVideoPreviewClick(adapterPosition, "forPlay")
                    } else {
                        subListener.isSubscribed()
                    }
                }}else{
                    onVideoPreviewClick(adapterPosition, "forPlay")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SteamHolder {
        context = parent.context
        val binding = DataBindingUtil.inflate<WatchOrDownloadVideoLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.watch_or_download_video_layout,
            parent,
            false
        )
        return SteamHolder(binding)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: SteamHolder, position: Int) {

        val videoData = videoList.get(position)

        holder.binding.name.text = CommanUtils.capitalize(videoData.stream_video_name)
        holder.binding.episode.text = CommanUtils.capitalize(videoData.stream_video_subtitle)
        holder.binding.description.text = videoData.stream_video_description
        holder.binding.timeDuration.text = Constant.getStreamDuration(videoData.video_duration)
        Glide.with(context)
            .load(videoData.stream_video_image_url + "thumb/" + videoData.stream_video_image)
            .into(holder.binding.videoThumb)



    if (videoData.isAddedToQueue) {
        holder.binding.downloadLayout.visibility = View.VISIBLE
        holder.binding.downloadIcon.visibility = View.VISIBLE
        holder.binding.loader.visibility = View.GONE
        Log.d("fnaslfnlasfas", "onBindViewHolder: AddedToQueue ${videoData.isAddedToQueue} -- ${isAdmin}")
        holder.binding.downloadIcon.setImageDrawable(context.resources.getDrawable(R.drawable.download_wait))
    } else {
        holder.binding.downloadLayout.visibility = View.VISIBLE
        holder.binding.downloadIcon.visibility = View.VISIBLE
        holder.binding.progressLayout.visibility = View.GONE
        holder.binding.loader.visibility = View.GONE
        Log.d("fnaslfnlasfas", "onBindViewHolder: AddedToQueue ELSEE ${videoData.isAddedToQueue}")

        if(videoData.view_type.equals("1"))
        {
            holder.binding.downloadIcon.visibility = View.VISIBLE
        } else{
            holder.binding.downloadIcon.visibility = View.GONE
        }
        if(videoData.view_type.equals("2"))
        {
            holder.binding.playVideo.visibility = View.VISIBLE

        }else{
            holder.binding.playVideo.visibility = View.GONE
        }
        if(videoData.view_type.equals("3"))
        {
            holder.binding.playVideo.visibility = View.VISIBLE
            holder.binding.downloadIcon.visibility = View.VISIBLE

        }

    }

        if (videoData.Progress != 0 && videoData.Progress != 100) {
            holder.binding.progressLayout.visibility = View.VISIBLE
            holder.binding.loader.visibility = View.VISIBLE
            holder.binding.downloadLayout.visibility = View.GONE
            holder.binding.downloadIcon.visibility = View.GONE
            holder.binding.loader.max = 100
            holder.binding.loader.progress = videoData.Progress
            Log.d("fnaslfnlasfas", "onBindViewHolder: Progress IF ${videoData.Progress}")
        } else {
            holder.binding.progressLayout.visibility = View.GONE
            holder.binding.loader.visibility = View.GONE
            holder.binding.downloadLayout.visibility = View.VISIBLE
            Log.d("fnaslfnlasfas", "onBindViewHolder: PROGRESS ELSE  ${videoData.Progress}")
            if (videoData.Progress == 100) {
                holder.binding.downloadIcon.visibility = View.VISIBLE
                holder.binding.downloadIcon.setImageResource(R.drawable.complete_downloaded)
                holder.binding.playVideo.visibility = View.VISIBLE
                Log.d("Complete download", "Complete download adapter..." + videoData.Progress)
            }
        }

        if ("Yes".equals(isAdmin) || !Doviesfitness.getDataManager()
                .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                .equals(
                    "0"
                )|| isalloweduser==true
        ) {
            holder.binding.lockImg.visibility = View.GONE
            holder.binding.downloadLayout.visibility = View.VISIBLE
            //   binding.btnStatus.text = "Add to my plan"
        } else {
            if ("OPEN".equals(
                    StreamDetailActivity.overViewTrailerData?.stream_workout_access_level,
                    true
                )
            ) {
                holder.binding.lockImg.visibility = View.GONE
                holder.binding.downloadLayout.visibility = View.GONE
            } else {
                holder.binding.lockImg.visibility = View.GONE
                holder.binding.downloadLayout.visibility = View.GONE
            }
        }
    }

    public fun setStartDownloadIcon(itemPos: Int){
        videoList[itemPos].isAddedToQueue = true
        notifyItemChanged(itemPos)
    }

    public fun setDownloadProgress(itemPosition: Int){
        notifyItemChanged(itemPosition)
    }

    public fun refreshSpecificItem(itemPosition: Int){
        notifyItemChanged(itemPosition)
        Log.d("fanjkfnkasfa", "refreshSpecificItem: ${itemPosition}")
    }



    abstract fun onVideoClickDownloadInpxl(itemPos: Int)
    abstract fun onDownloadingClick(itemPos: Int)
    abstract fun onVideoPreviewClick(itemPos: Int, status: String)

}