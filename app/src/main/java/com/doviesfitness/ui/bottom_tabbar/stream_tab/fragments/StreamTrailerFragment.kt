package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.databinding.StreamTrailerFragmentBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamWorkoutDetailModel
import com.doviesfitness.ui.multipleQuality.IntentUtil
import com.doviesfitness.ui.multipleQuality.StreamVideoPlayUrlActivityTemp
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class StreamTrailerFragment : BaseFragment() {
    lateinit var binding: StreamTrailerFragmentBinding
    lateinit var overViewTrailerData: StreamWorkoutDetailModel.Settings.Data
    private val preferExtensionDecodersMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.stream_trailer_fragment, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        binding.topLayout.setOnClickListener(View.OnClickListener {

            if (overViewTrailerData != null && overViewTrailerData.stream_workout_trailer != null && !overViewTrailerData.stream_workout_trailer.isEmpty()) {
                downloadVideo(overViewTrailerData)
            }
        })
    }

    public fun setData(TrailerData: StreamWorkoutDetailModel.Settings.Data) {
        overViewTrailerData = TrailerData
        if (overViewTrailerData != null && overViewTrailerData.stream_workout_trailer != null && !overViewTrailerData.stream_workout_trailer.isEmpty()) {
            binding.topLayout.visibility = View.VISIBLE
            binding.seperatorLine.visibility = View.VISIBLE
            binding.txtNoDataFound.visibility = View.GONE
            if (overViewTrailerData.trailer_title!=null && !overViewTrailerData.trailer_title.isEmpty()){
                binding.name.text=""+overViewTrailerData.trailer_title
                binding.nameLayout.visibility=View.VISIBLE
            }
            else{
                binding.nameLayout.visibility=View.GONE
            }

            if (overViewTrailerData.stream_workout_trailer_image != null && !overViewTrailerData.stream_workout_trailer_image.isEmpty())
                Glide.with(context!!).load("" + overViewTrailerData.stream_workout_trailer_image).into(
                    binding.videoThumb
                )
        } else {
            binding.seperatorLine.visibility = View.GONE
            binding.topLayout.visibility = View.GONE
            binding.txtNoDataFound.visibility = View.VISIBLE
        }
    }

    fun downloadVideo(overViewTrailerData: StreamWorkoutDetailModel.Settings.Data) {
        val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
            stream_video = overViewTrailerData.stream_workout_trailer,
            stream_video_description = overViewTrailerData.stream_workout_description,
            stream_video_id = overViewTrailerData.stream_workout_id,
            video_duration = "",
            stream_video_image = overViewTrailerData.stream_workout_trailer_image,
            stream_video_image_url = overViewTrailerData.stream_workout_image_url,
//            stream_video_name = overViewTrailerData.stream_workout_name,
            stream_video_name = overViewTrailerData.trailer_title,
            stream_video_subtitle = overViewTrailerData.stream_workout_subtitle,
            order =1,
            Progress = 0,
            MaxProgress = 100, seekTo = 5,
            isPlaying = false,
            isComplete = false,
            isRestTime = false,
            downLoadUrl = "",
            hls_video = null,
            mp4_video = null,is_workout =  "no",
                view_type = ""
        )
        var workoutid = StreamDetailActivity.overViewTrailerData!!.stream_workout_id
        var strList = ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>()
        strList.add(tempListModal)
        if (strList != null && strList.size > 0) {
           /* val intent = Intent(activity, StreamVideoPlayUrlActivity::class.java)
            intent.putExtra("videoList", strList)
            intent.putExtra("workout_id", "" + workoutid)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "yes")
            intent.putExtra("position", 0)
            activity!!.startActivity(intent)
       */
                //  if (streamWorkoutId.equals("70") || streamWorkoutId.equals("71")) {
                    loadSample(
                        strList, "" + workoutid, 0,
                        strList)

        }
    }

    private class PlaylistHolder(title: String, mediaItems: List<MediaItem>) {
        val title: String
        val mediaItems: List<MediaItem>

        init {
            Assertions.checkArgument(!mediaItems.isEmpty())
            this.title = title
            this.mediaItems = Collections.unmodifiableList(
                java.util.ArrayList(mediaItems)
            )
        }
    }

    private fun loadSample(strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>, workoutid: String, pos: Int, videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>) {
        val loaderTask: SampleListLoader = SampleListLoader(this, strList, workoutid, pos, videoList)
        loaderTask.execute("")
    }
    private fun isNonNullAndChecked(menuItem: MenuItem?): Boolean {
        return menuItem != null && menuItem.isChecked
    }

    private class SampleListLoader(
        streamVideoFragmentNew: StreamTrailerFragment, strList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>,
        workoutid: String, pos: Int, videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>
    ) : AsyncTask<String?, Void?,
            PlaylistHolder?>() {
        private var sawError = false
        var streamVideoFragmentNew = streamVideoFragmentNew
        var strList = strList
        var workoutid = workoutid
        var pos = pos
        var videoList = videoList

        override fun doInBackground(vararg uris: String?): PlaylistHolder? {
            var result: PlaylistHolder? = null
            try {
                result = readEntry(videoList)
            } catch (e: Exception) {
                com.google.android.exoplayer2.util.Log.e(
                    "error",
                    "Error loading sample list: $e"
                )
                sawError = true
            } finally {
            }
            return result
        }

        override fun onPostExecute(result: PlaylistHolder?) {
            // onPlaylistGroups(result, sawError)
//            val intent = Intent(streamVideoFragmentNew.mContext, StreamPlayerActivity::class.java)
            val intent = Intent(streamVideoFragmentNew.getActivity(), StreamVideoPlayUrlActivityTemp::class.java)

            intent.putExtra(
                IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA,
                streamVideoFragmentNew.isNonNullAndChecked(streamVideoFragmentNew.preferExtensionDecodersMenuItem)
            )
            intent.putExtra("videoList", strList)
            intent.putExtra("workout_id", "" + workoutid)
            intent.putExtra("local", "no")
            intent.putExtra("trailer", "yes")
            intent.putExtra("media_name", ""+ StreamDetailActivity.mediaName)
            intent.putExtra("position", pos)
            IntentUtil.addToIntent(result!!.mediaItems, intent)

            streamVideoFragmentNew.startActivity(intent)
        }

        @Throws(IOException::class)
        private fun readEntry(videoList: ArrayList<StreamWorkoutDetailModel.Settings.Data.Video>): PlaylistHolder {
            var mediaList = arrayListOf<MediaItem>()

            var vlist = arrayListOf<String>()
            for (i in 0..videoList?.size!!-1) {

                var uri: Uri? = null
                var extension: String? = null
                var title: String? = null
                val mediaItem = MediaItem.Builder()
                title = "Dovies video"
                uri = Uri.parse(videoList.get(i)?.stream_video)
                extension = "mp4"
                //     uri = Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0")
                //     extension = "mpd"

/*
                if (i == videoList.size) {
                    uri =
                        Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0")
                    extension = "mpd"
                } else {
                    uri = Uri.parse(videoList.get(i)?.hlsVideo?.vHlsMasterPlaylist)
                    extension = "m3u8"
                }
*/


                val adaptiveMimeType =
                    Util.getAdaptiveMimeTypeForContentType(
                        Util.inferContentType(
                            uri,
                            extension
                        )
                    )
                mediaItem
                    .setUri(uri)
                    .setVideoUrl("setVideoUrl")
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(title).build()
                    )
                    .setMimeType(adaptiveMimeType)

                mediaList.add(mediaItem.build())
            }
            //   return PlaylistHolder(title, listOf(mediaItem.build()))
            return PlaylistHolder("Dovies", mediaList)
        }
    }

}