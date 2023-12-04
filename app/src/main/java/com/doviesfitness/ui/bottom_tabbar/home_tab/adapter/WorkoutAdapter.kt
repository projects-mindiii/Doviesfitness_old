package com.doviesfitness.ui.bottom_tabbar.home_tab.adapter

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.utils.PermissionAll
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.workout_view.view.*
import java.io.File

class WorkoutAdapter(
    context: WorkOutDetailActivity,
    commetsList: ArrayList<WorkoutExercise>,
    listener: WorkOutItemClickListener,
    videowidth: Int,
subListener:IsSubscribed
) :

    androidx.recyclerview.widget.RecyclerView.Adapter<WorkoutAdapter.MyViewHolder>() {
    var mContext: WorkOutDetailActivity
    var workoutList: ArrayList<WorkoutExercise>
    var listener: WorkOutItemClickListener
    val videowidth: Int

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    var subListener: IsSubscribed


    init {
        this.mContext = context
        this.workoutList = commetsList
        this.listener = listener
        this.videowidth = videowidth
        this.subListener = subListener
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val workoutData: WorkoutExercise = workoutList.get(position)
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.workout_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val exercise = workoutList.get(position)
        //  Log.d("rest time....","rest time..."+exercise.workout_exercise_break_time)
        holder.restTimeTv.text = exercise.workout_exercise_break_time
        Glide.with(holder.iv_exercise.context).load(exercise.workout_exercise_image)
            .into(holder.iv_exercise)
        holder.tv_exercise.text = exercise.workout_exercise_name

        if (!exercise.workout_exercise_level.isNullOrBlank())
        holder.tv_level.text = exercise.workout_exercise_level.replace("|", ",")

        if (!exercise.workout_exercise_equipments.isNullOrBlank())
        holder.tv_equipment.text = exercise.workout_exercise_equipments.replace("|", ",")

        if (!exercise.workout_exercise_body_parts.isNullOrBlank())
        holder.tv_goal.text = exercise.workout_exercise_body_parts.replace("|", ",")

        if (position == workoutList.size - 1) {
            holder.bottomLayout.visibility = View.VISIBLE
        } else {
            holder.bottomLayout.visibility = View.GONE
        }

        if (exercise.workout_exercise_detail.contains("secs") || exercise.workout_exercise_detail.contains(
                "sec"
            )
        ) {
            if (exercise.workout_exercise_detail.contains("01")) {
                holder.tv_exercise_time.text = "01 sec"
            } else {
                holder.tv_exercise_time.text = exercise.workout_exercise_detail
            }
        } else if (exercise.workout_exercise_detail.contains("mins") || exercise.workout_exercise_detail.contains(
                "min"
            )
        ) {
            if (exercise.workout_exercise_detail.contains("01")) {
                holder.tv_exercise_time.text = "01 min"
            } else {
                holder.tv_exercise_time.text = exercise.workout_exercise_detail
            }
        } else {
            holder.tv_exercise_time.text =
                exercise.workout_exercise_detail + " " + mContext.getString(R.string.reps)
        }

        if (position == workoutList.size - 1) {
            holder.RestTimelayout.visibility = View.GONE
            holder.restTimeTv.text = ""
        } else {
            if (exercise.workout_exercise_break_time == "00:00:00") {
                holder.RestTimelayout.visibility = View.GONE
                holder.restTimeTv.text = ""
            } else {
                holder.RestTimelayout.visibility = View.VISIBLE
                var BRTime: String = exercise.workout_exercise_break_time
                var Str: List<String>
                Str = BRTime.split(":")
                var timeStr: String = ""
                if (!Str.get(0).equals("00")) {
                    timeStr = Str.get(0) + " Hour"
                } else if (!Str.get(1).equals("00")) {
                    if (Str.get(1).equals("01")) {
                        if (!Str.get(2).equals("00")) {
                            timeStr = Str.get(1) + ":" + Str.get(2) + " Minutes"
                        } else
                            timeStr = Str.get(1) + " Minute"
                    } else {
                        if (!Str.get(2).equals("00")) {
                            timeStr = Str.get(1) + ":" + Str.get(2) + " Minutes"
                        } else
                            timeStr = Str.get(1) + " Minutes"
                    }

                } else {
                    if (Str.get(2).equals("01")) {
                        timeStr = Str.get(2) + " Second"
                    } else {
                        timeStr = Str.get(2) + " Seconds"
                    }
                }
                holder.restTimeTv.text = timeStr
            }
        }

        /*@param : exercise_access_level == Lock : "Show lock img"
        * @param : exercise_access_level == OPEN : "Show Down arrow img"
        * */

        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
        ) {
            holder.iv_lock.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_down_button
                )
            )
        } else {
            if (exercise.exercise_access_level.equals("LOCK")) {
                holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_lock_balck
                    )
                )
            } else if (exercise.exercise_access_level.equals("OPEN")) {
                holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_down_button
                    )
                )
            }
        }

        //---------Allow User-------------------------

        if(WorkOutDetailActivity.isalloweduser==true || WorkOutDetailActivity.Parentisalloweduser==true)
        {

            holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                            mContext,
                            R.drawable.ic_down_button
                    )
            )
        }


        //----------------------------------------------

        if (exercise.workout_exercise_is_favourite.equals("0")) {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star))
        } else {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_active))
        }

        if (!exercise.isPlaying) {
            holder.fl_vv.removeAllViews()
            holder.iv_lock.rotation = 0f
        } else {

        }

        holder.ivFav.setOnClickListener() {
            if (exercise.workout_exercise_is_favourite.equals("0")) {
                holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_active))
                listener.setFavUnfav(exercise, position, holder.ivFav)
            } else {
                holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_star))
                listener.setFavUnfav(exercise, position, holder.ivFav)
            }
        }
        holder.ivShare.setOnClickListener() {
            listener.shareURL(exercise)
        }

        holder.rl_exercise.setOnClickListener {
            /* @param : exercise_access_level == OPEN : "So Add video view or play video"
            *  @param : exercise_access_level == LOCK : "Access denied"
            * */
            if(WorkOutDetailActivity.isalloweduser==true || WorkOutDetailActivity.Parentisalloweduser==true) {
                isDownloadedVideo(exercise.workout_exercise_video, position, holder)
            }else

            if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                    "Yes"
                )
            ) {
                isDownloadedVideo(exercise.workout_exercise_video, position, holder)
            } else {
                if (mContext.workoutAccessLvel.equals("OPEN",true)) {

                    if (exercise.exercise_access_level.equals("OPEN")) {
                        isDownloadedVideo(exercise.workout_exercise_video, position, holder)
                    } else {
                        subListener.isSubscribed()
                    }
                } else {
                    subListener.isSubscribed()
                }
            }
        }

    }

    fun isDownloadedVideo(videoUrl: String, position: Int, holder: MyViewHolder) {
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            // var subpath = "/Dovies//$downloadFileName.mp4"
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    mContext.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            Log.e("download file path", "file path..." + f.absolutePath)
            //  val encryptedPath = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"


            if (!f.exists()) {
                var uri = Uri.parse(videoUrl)
                intializePlayer(uri, position, holder, true)
            } else {
                var uri = Uri.parse(path)
                intializePlayer(uri, position, holder, false)
            }
        }
    }

    fun intializePlayer(news_video: Uri, position: Int, holder: MyViewHolder, isload: Boolean) {
        var playerView: PlayerView
        playerView = PlayerView(mContext)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        if (!workoutList.get(position).isPlaying) {
            holder.fl_vv.visibility = View.VISIBLE

            holder.iv_lock.rotation = 180f
            holder.fl_vv.addView(playerView)
            holder.fl_vv.requestFocus()
            playerView.setOnClickListener {
                holder.rl_exercise.performClick()
            }
            playerView.layoutParams.height = videowidth
            playerView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT

            trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            mediaDataSourceFactory =
                DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "mediaPlayerSample"))


            val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(news_video)

            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector!!)

            with(player) {
                prepare(mediaSource, false, false)
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
                setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);//repeat play video
            }

            playerView.setShutterBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBlack))
            playerView.player = player
            playerView.requestFocus()
          //  holder.ivHideControllerButton.visibility = View.GONE
            if (isload)
                playerView.setShowBuffering(true)
/*
            holder.ivHideControllerButton.setOnClickListener {
                // playerView.hideController()
            }
*/

            lastSeenTrackGroupArray = null
            listener.videoPlayClick(true, workoutList.get(position), position, holder, isload)


        } else {
            holder.fl_vv.visibility = View.GONE
            listener.videoPlayClick(false, workoutList.get(position), position, holder, false)
            player.release()
            workoutList[position].isPlaying = false
            holder.iv_lock.rotation = 0f
            holder.fl_vv.removeAllViews()

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
        val ivFav = view.iv_fav
        val ivShare = view.iv_share
        val restTimeTv = view.rest_time_tv
        val RestTimelayout = view.time_layout
        val bottomLayout = view.bottom_layout
    }

    interface WorkOutItemClickListener {
        fun videoPlayClick(
            isScroll: Boolean,
            data: WorkoutExercise,
            position: Int,
            view: MyViewHolder,
            isLoad: Boolean
        )

        fun shareURL(data: WorkoutExercise)
        fun setFavUnfav(data: WorkoutExercise, position: Int, view: ImageView)
    }
}

