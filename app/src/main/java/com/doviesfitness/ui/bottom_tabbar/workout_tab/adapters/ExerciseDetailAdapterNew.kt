package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ExerciseDetailListActivityNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.utils.PermissionAll
import com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.stream_exo_playback_control_view.view.*
import kotlinx.android.synthetic.main.workout_view.view.*
import java.io.File
import java.net.URL

class ExerciseDetailAdapterNew(context: Context, ifReplaceExerciese :Boolean, exerciseListing: ArrayList<ExerciseListingResponse.Data>, listener: OnItemClick, videowidth: Int, from: String, subsListener: IsSubscribed) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context;
    private var exerciseListing = ArrayList<ExerciseListingResponse.Data>()
    var listener: OnItemClick
    val videowidth: Int
    var ifReplaceExerciese: Boolean = false
    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var isCheck: Boolean = false
    var subsListener: IsSubscribed
    var from = ""
    var isLoading = false
    var tempPosition = -1

    init {
        this.context = context
        this.ifReplaceExerciese = ifReplaceExerciese
        this.exerciseListing = exerciseListing
        this.videowidth = videowidth
        this.listener = listener
        this.from = from
        this.subsListener = subsListener
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return MyViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.workout_view,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return FooterLoader(
                    LayoutInflater.from(context).inflate(
                        R.layout.pagination_item_loader,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return exerciseListing.size;
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == exerciseListing.size - 1) {
            if (showLoader) VIEWTYPE_LOADER
            else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
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
        val exercise = exerciseListing.get(pos)

        if (pos == exerciseListing.size - 1) {
            holder.bottomLayout.visibility = View.VISIBLE
        } else {
            holder.bottomLayout.visibility = View.GONE
        }

        if (from == "create") {
            holder.selectExerciseIcon.visibility = View.VISIBLE
            holder.hideView.visibility = View.INVISIBLE
            holder.iv_share.visibility = View.GONE
            holder.iv_fav.visibility = View.GONE
        } else {
            holder.selectExerciseIcon.visibility = View.GONE
            holder.hideView.visibility = View.GONE
            holder.iv_share.visibility = View.VISIBLE
            holder.iv_fav.visibility = View.VISIBLE
        }

        if (exercise.isSelected) {
            holder.selectExerciseIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_check_mark
                )
            )
        } else {
            holder.selectExerciseIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_circle_img
                )
            )
        }

        Log.d("exercise_video...", "exercise.exercise_video..." + exercise.exercise_video)
        Glide.with(holder.iv_exercise.context).load(exercise.exercise_image)
            .into(holder.iv_exercise)
        holder.tv_exercise.text = exercise.exercise_name
        if (exercise.exercise_level.equals("All", true))
            holder.tv_level.text = exercise.exercise_level.replace("|", ",") + " level"
        else
            holder.tv_level.text = exercise.exercise_level.replace("|", ",")
        holder.tv_equipment.text = exercise.exercise_equipments.replace("|", ",")
        holder.tv_goal.text = exercise.exercise_body_parts.replace("|", ",")
        holder.tv_exercise_time.visibility = View.GONE
        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
        ) {
            holder.iv_lock.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_down_button
                )
            )
        } else {
            if (exercise.exercise_access_level.equals("LOCK")) {
                holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_lock_balck
                    )
                )
            } else if (exercise.exercise_access_level.equals("OPEN")) {
                holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_down_button
                    )
                )
            }
        }

        if (!exercise.isPlaying) {
            holder.fl_vv.removeAllViews()
            ExerciseDetailListActivityNew.flag = false
            holder.iv_lock.rotation = 0f
            holder.loaderLayout.visibility = View.GONE
        } else {
            if (!exercise.showLoader) {
                holder.loaderLayout.visibility = View.GONE
            }
        }

        if (exercise.isClicked) {
            holder.loaderLayout.visibility = View.VISIBLE
            //  ExerciseDetailListActivityNew.flag=true
            exercise.isClicked = false

        } else {
            holder.loaderLayout.visibility = View.GONE
        }


        holder.main_layout.setOnClickListener {
            /* @param : exercise_access_level == OPEN : "So Add video view or play video"
            *  @param : exercise_access_level == LOCK : "Access denied"
            * */

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                    "Yes"
                )
            ) {
                isDownloadedVideo(exercise.exercise_video, pos, holder)
            } else {
                if (exercise.exercise_access_level.equals("OPEN")) {

                    val permissin = PermissionAll()
                    //  permissin.RequestReadWritePermission(context as Activity?)
                    isDownloadedVideo(exercise.exercise_video, pos, holder)
                    // intializePlayer(exercise.exercise_video, pos, holder)
                } else {
                    subsListener.isSubscribed()

                }
            }
        }

        /*holder.selectExerciseIcon.setOnClickListener {

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")) {
                if (exercise.isSelected == true) {
                    exercise.isSelected = false
                    holder.selectExerciseIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_img))
                    listener.setSelected(pos, false, exercise)
                } else {
                    exercise.isSelected = true
                    holder.selectExerciseIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_mark))
                    listener.setSelected(pos, true, exercise)
                }
            } else {
                if (exercise.exercise_access_level.equals("OPEN")) {
                    if (exercise.isSelected == true) {
                        exercise.isSelected = false
                        holder.selectExerciseIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_img))
                        listener.setSelected(pos, false, exercise)
                    } else {
                        exercise.isSelected = true
                        holder.selectExerciseIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_mark))
                        listener.setSelected(pos, true, exercise)
                    }
                }
            }
        }*/

        holder.selectExerciseIcon.setOnClickListener {

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")) {

                if (ifReplaceExerciese) {
                    if (pos != -1) {
                        for (i in 0..exerciseListing.size - 1) {
                            exerciseListing.get(i).isSelected = false
                        }
                        exerciseListing.get(pos).isSelected = true
                        notifyDataSetChanged()
                    }
                    listener.setSelected(pos, true, exercise)
                }
                else {
                    if (exercise.isSelected == true) {
                        exercise.isSelected = false
                        holder.selectExerciseIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_circle_img
                            )
                        )
                        listener.setSelected(pos, false, exercise)
                    } else {
                        exercise.isSelected = true
                        holder.selectExerciseIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_check_mark
                            )
                        )
                        listener.setSelected(pos, true, exercise)
                    }
                }
            }
            else {
                if (ifReplaceExerciese) {
                    if (exercise.exercise_access_level.equals("OPEN")) {
                        if (pos != -1) {
                            for (i in 0..exerciseListing.size - 1) {
                                exerciseListing.get(i).isSelected = false
                            }
                            exerciseListing.get(pos).isSelected = true
                            notifyDataSetChanged()
                        }
                    }
                }
                else {
                    if (exercise.exercise_access_level.equals("OPEN")) {
                        if (exercise.isSelected == true) {
                            exercise.isSelected = false
                            holder.selectExerciseIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_img))
                            listener.setSelected(pos, false, exercise)
                        } else {
                            exercise.isSelected = true
                            holder.selectExerciseIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_mark))
                            listener.setSelected(pos, true, exercise)
                        }
                    }
                }
            }
        }

        if (exercise.exercise_is_favourite.equals("0")) {
            holder.iv_fav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star))
        } else {
            holder.iv_fav.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_star_active
                )
            )
        }

        holder.iv_fav.setOnClickListener() {
            if (isCheck) {
                isCheck = false
                holder.iv_fav.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_star_active
                    )
                )
                listener.setFavUnfavForExercies(exercise, pos, holder.iv_fav)
            } else {
                isCheck = true
                holder.iv_fav.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_star
                    )
                )
                listener.setFavUnfavForExercies(exercise, pos, holder.iv_fav)
            }
        }

        holder.iv_share.setOnClickListener() {
            listener.shareURL(exercise)
        }
    }


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivHideControllerButton: LinearLayout by lazy {
            view.findViewById<LinearLayout>(R.id.controller)
        }
        val tv_exercise = view.tv_exercise
        val iv_exercise = view.iv_exercise
        val tv_exercise_time = view.tv_exercise_time
        val tv_level = view.tv_level
        val tv_equipment = view.tv_equipment
        val tv_goal = view.tv_goal
        val fl_vv = view.fl_vv
        val rl_exercise = view.rl_exercise
        val iv_lock = view.iv_lock
        val iv_share = view.iv_share
        val hideView = view.hide_view
        val selectExerciseIcon = view.select_exercise_icon
        val iv_fav = view.iv_fav
        val main_layout = view.main_layout
        val bottomLayout = view.bottom_layout
        val loaderLayout = view.loader_layout
    }

    fun isDownloadedVideo(videoUrl: String, position: Int, holder: MyViewHolder) {
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            Log.e("download file path", "file path..." + f.absolutePath)
            if (!f.exists()) {
                var uri = Uri.parse(videoUrl)
                downloadVideo(videoUrl, position, holder)
                ExerciseDetailListActivityNew.flag = true
            } else {
                var uri = Uri.parse(path)
                listener.videoDownload(true, exerciseListing.get(position), holder, position, false)
                if (exerciseListing.size != 0 && exerciseListing.size != -1) {
                    if (!exerciseListing.get(position).isPlaying) {
                        ExerciseDetailListActivityNew.flag = true
                        if (getDataManager().getStringData(f.absolutePath) != null &&
                            !getDataManager().getStringData(f.absolutePath)!!.isEmpty()
                            && f.length().toString().equals(getDataManager().getStringData(f.absolutePath))
                        ) {
                            if (!isLoading) {
                                intializePlayer(uri, position, holder)
                            } else {
                                exerciseListing.get(tempPosition).showLoader = false
                                notifyItemChanged(tempPosition)
                                tempPosition = position
                            }
                        } else {
                            if (!isLoading) {
                                isLoading = true
                                tempPosition = position
                                someTask(
                                    videoUrl,
                                    uri,
                                    position,
                                    holder,
                                    f,
                                    exerciseListing.get(position)
                                ).execute()
                            } else {
                                if (tempPosition != -1) {
                                    exerciseListing.get(tempPosition).showLoader = false
                                    notifyItemChanged(tempPosition)
                                    tempPosition = position
                                }
                            }
                        }
                    } else {
                        listener.videoPlayClick(
                            false,
                            exerciseListing.get(position),
                            position,
                            holder,
                            false
                        )
                        if (player != null)
                            player.release()
                        exerciseListing.get(position).isPlaying = false
                        holder.iv_lock.rotation = 0f
                        holder.fl_vv.removeAllViews()
                        ExerciseDetailListActivityNew.flag = false
                    }
                }
            }

        }
    }

    private fun downloadVideo(videoUrl: String, position: Int, holder: MyViewHolder) {

        if (exerciseListing.size != 0 && exerciseListing.size != -1) {
            if (!exerciseListing.get(position).isPlaying) {
                holder.loaderLayout.visibility = View.VISIBLE
                listener.videoDownload(true, exerciseListing.get(position), holder, position, true)

            } else {
                holder.loaderLayout.visibility = View.GONE
                listener.videoDownload(true, exerciseListing.get(position), holder, position, false)
                listener.videoPlayClick(
                    false,
                    exerciseListing.get(position),
                    position,
                    holder,
                    false
                )
                player.release()
                exerciseListing.get(position).isPlaying = false
                holder.iv_lock.rotation = 0f
                holder.fl_vv.removeAllViews()
                ExerciseDetailListActivityNew.flag = false
            }
        }
    }


    public fun intializePlayer(news_video: Uri, position: Int, holder: MyViewHolder) {
        holder.loaderLayout.visibility = View.GONE
        var playerView: PlayerView
        playerView = PlayerView(context)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        if (exerciseListing.size != 0 && exerciseListing.size != -1) {
            if (!exerciseListing.get(position).isPlaying) {
                ExerciseDetailListActivityNew.flag = false
                holder.iv_lock.rotation = 180f
                holder.fl_vv.addView(playerView)
                holder.fl_vv.requestFocus()
                playerView.setOnClickListener {
                    Log.d("video click fl_vv", "video click fl_vv...")
                    holder.main_layout.performClick()
                }
                playerView.layoutParams.height = videowidth
                playerView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
                mediaDataSourceFactory =
                    DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, "mediaPlayerSample")
                    )
                val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(news_video)
                player = ExoPlayerFactory.newSimpleInstance(context, trackSelector!!)
                with(player) {
                    prepare(mediaSource, false, false)
                    playWhenReady = true
                    repeatMode =
                        com.google.android.exoplayer2.Player.REPEAT_MODE_ONE //repeat play video
                    setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                playerView.setShutterBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorBlack
                    )
                )

                playerView.player = player
                playerView.requestFocus()
            //    holder.ivHideControllerButton.visibility = View.GONE
                playerView.setShowBuffering(false)

/*
                holder.ivHideControllerButton.setOnClickListener {
                    //  playerView.hideController()
                }
*/
                lastSeenTrackGroupArray = null
                listener.videoPlayClick(
                    true,
                    exerciseListing.get(position),
                    position,
                    holder,
                    false
                )
            } else {
                listener.videoPlayClick(
                    false,
                    exerciseListing.get(position),
                    position,
                    holder,
                    false
                )
                player.release()
                exerciseListing.get(position).isPlaying = false
                holder.iv_lock.rotation = 0f
                holder.fl_vv.removeAllViews()
                ExerciseDetailListActivityNew.flag = false
            }
        }
    }

    public interface OnItemClick {
        fun videoPlayClick(
            isScroll: Boolean,
            data: ExerciseListingResponse.Data,
            position: Int,
            view: MyViewHolder,
            isLoad: Boolean
        )

        fun videoDownload(
            isScroll: Boolean,
            data: ExerciseListingResponse.Data,
            view: MyViewHolder,
            position: Int,
            isLoad: Boolean

        )

        fun shareURL(data: ExerciseListingResponse.Data)
        fun setFavUnfavForExercies(
            data: ExerciseListingResponse.Data,
            position: Int,
            view: ImageView
        )

        fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView)
        fun setSelected(position: Int, isSelected: Boolean, exercise: ExerciseListingResponse.Data)
    }

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    ////////////
    inner class someTask(
        exercise_video: String,
        uri: Uri,
        position: Int,
        holder: MyViewHolder,
        f: File,
        exercise: ExerciseListingResponse.Data
    ) : AsyncTask<Void, Void, Boolean>() {
        var exercise_video1 = exercise_video
        var uri = uri
        var position = position
        var holder = holder
        var file = f
        var exercise = exercise

        override fun doInBackground(vararg params: Void?): Boolean? {
            var url = URL(exercise_video1);
            var urlConnection = url.openConnection();
            urlConnection.connect();
            var file_size = urlConnection.getContentLength();
            Log.e("video size", "video size url..." + file_size)
            Log.e("video size", "video size file..." + file.length())

            getDataManager().setStringData(file.absolutePath, file_size.toString())
            //  exercise.videoLength=file_size.toLong()
            if (file_size.toLong() == file.length()) {
                return true
            } else {
                return false
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            holder.loaderLayout.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            isLoading = false

            if (result!!) {
                holder.loaderLayout.visibility = View.GONE
                if (tempPosition == position)
                    intializePlayer(uri, position, holder)
            } else {
                file.delete()
                downloadVideo(exercise_video1, position, holder)
            }
        }
    }
}
