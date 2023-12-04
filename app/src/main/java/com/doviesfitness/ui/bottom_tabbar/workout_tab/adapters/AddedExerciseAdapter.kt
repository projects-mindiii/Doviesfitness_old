package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_EXERCISE_TIME
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperAdapter
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperViewHolder
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
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
import kotlinx.android.synthetic.main.create_exercise_item_view.view.*
import java.io.File
import java.util.*

class AddedExerciseAdapter(
    val context: Context,
    val exerciseListing: ArrayList<ExerciseListingResponse.Data>,
    val listener: OnItemClick,
    val videowidth: Int,
    val timeListener: SelectTime,
    val stopScroll: StopScroll,
    val mDragStartListener: OnStartDragListener
) : RecyclerView.Adapter<AddedExerciseAdapter.MyView>(),
    ItemTouchHelperAdapter {

    /*init {
        setHasStableIds(true)
    }*/

    private  var player: SimpleExoPlayer?=null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    var tempPos = -1
    private var mLastClickTime: Long = 0
    lateinit var tempHolder: MyView

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(exerciseListing, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
    }

    override fun onItemDismiss(position: Int) {
        //TODO To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyView {
        return MyView(
            LayoutInflater.from(p0.context!!).inflate(
                R.layout.create_exercise_item_view,
                p0,
                false
            )
        )
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getItemCount(): Int {
        return exerciseListing.size
    }

    override fun onBindViewHolder(rvHolder: MyView, pos: Int) {
        val holder = rvHolder
        val exercise = exerciseListing.get(pos)
        if (exercise.exercise_timer_time != null && !exercise.exercise_timer_time!!.isEmpty()) {
            holder.timeExerciseTxt.text = exercise.exercise_timer_time
        }
        if (exercise.exercise_reps_number != null && !exercise.exercise_reps_number!!.isEmpty()) {
            holder.repetetionTxt.text = exercise.exercise_reps_number
        }
        if (exercise.exercise_reps_time != null && !exercise.exercise_reps_time!!.isEmpty()) {
            holder.repsTimerTxt.text = exercise.exercise_reps_time
        }
        if (exercise.exercise_rest_time != null && !exercise.exercise_rest_time!!.isEmpty()) {
            holder.restTimeTxt.text = exercise.exercise_rest_time
        }
        holder.tv_exercise.text = exercise.exercise_name
        Glide.with(holder.iv_exercise.context).load(exercise.exercise_image)
            .into(holder.iv_exercise)

        if (pos == exerciseListing.size - 1) {
            holder.rest_time_layout.visibility = View.GONE
            holder.margin_layout.visibility = View.GONE
        } else {
            holder.rest_time_layout.visibility = View.VISIBLE
            holder.margin_layout.visibility = View.GONE
        }
        if (exerciseListing.get(pos).isValid) {
            holder.frame_layout.setBackgroundResource(0);
            holder.select_time_txt.text = context.resources.getString(R.string.select_time)
            holder.select_reps_number_txt.text =
                context.resources.getString(R.string.select_number_of_reps)
            holder.reps_finish_time_txt.text =
                context.resources.getString(R.string.time_to_finish_reps)
        }
        else {
           // holder.frame_layout.setBackgroundResource(R.drawable.border_red);

            if (exercise.isReps) {
                holder.select_time_txt.text = context.resources.getString(R.string.select_time)
                if (exercise.exercise_reps_number!!.isEmpty() || exercise.exercise_reps_number.equals(
                        "00"
                    )
                ) {
                    holder.select_reps_number_txt.text =
                        Html.fromHtml(context.resources.getString(R.string.select_number_of_reps_error))
                    holder.reps_finish_time_txt.text =
                        context.resources.getString(R.string.time_to_finish_reps)

                } else {
                    holder.reps_finish_time_txt.text =
                        Html.fromHtml(context.resources.getString(R.string.time_to_finish_reps_error))
                    holder.select_reps_number_txt.text =
                        context.resources.getString(R.string.select_number_of_reps)
                }
            } else {
                holder.select_time_txt.text =
                    Html.fromHtml(context.resources.getString(R.string.select_time_error))
                holder.select_reps_number_txt.text =
                    context.resources.getString(R.string.select_number_of_reps)
                holder.reps_finish_time_txt.text =
                    context.resources.getString(R.string.time_to_finish_reps)
            }

        }


        /*updating view select or deselect*/
        if (exercise.isSelectedExercise){
            Glide.with(context).load(R.drawable.selected_circle_blue_).into( holder.deselect_workout_exercise)
        }else{
            Glide.with(context).load(R.drawable.deselected_create_workout).into( holder.deselect_workout_exercise)
        }


        if (exercise.isReps == true) {
            Glide.with(context).load(R.drawable.ic_active_radio_ico).into(holder.reps_check)
            Glide.with(context).load(R.drawable.abc_radio_uncheck).into(holder.time_check)

            holder.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            holder.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

            holder.timeLayout.visibility = View.GONE
            holder.repsNumberLayout.visibility = View.VISIBLE
            holder.repFinishTimeLayout.visibility = View.VISIBLE
        } else {
            Glide.with(context).load(R.drawable.ic_active_radio_ico).into(holder.time_check)
            Glide.with(context).load(R.drawable.abc_radio_uncheck).into(holder.reps_check)

            holder.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            holder.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

            holder.timeLayout.visibility = View.VISIBLE
            holder.repsNumberLayout.visibility = View.GONE
            holder.repFinishTimeLayout.visibility = View.GONE
        }

//        if (!exercise.isPlaying) {
//            holder.fl_vv.removeAllViews()
//            holder.iv_lock.rotation = 0f
//        } else {
//
//        }


        holder.main_layout.setOnClickListener {
            if (tempPos != -1) {
                removeVideoView(tempPos, holder)
            }
        }


        holder.exchange_img.setOnClickListener {
            listener.forExchangeItem(exercise, pos)
        }

    }

    fun isDownloadedVideo(videoUrl: String, position: Int, holder: AddedExerciseAdapter.MyView, yPositionOnScreen: Float) {
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            Log.e("download file path", "file path..." + f.absolutePath)
            if (!f.exists()) {
                var uri = Uri.parse(videoUrl)
                intializePlayer(uri, position, holder, true, yPositionOnScreen)
            } else {
                var uri = Uri.parse(path)
                intializePlayer(uri, position, holder, false, yPositionOnScreen)
                //Toast.makeText(mContext, "already downloaded...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun intializePlayer(
        news_video: Uri,
        position: Int,
        holder: AddedExerciseAdapter.MyView,
        isLoad: Boolean,  yPositionOnScreen: Float
    ) {
        var playerView: PlayerView
        playerView = PlayerView(context)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        if (exerciseListing.size != 0 && exerciseListing.size != -1) {
            if (!exerciseListing.get(position).isPlaying) {
                holder.iv_lock.rotation = 180f
                holder.fl_vv.addView(playerView)
                holder.fl_vv.requestFocus()
                playerView.setOnClickListener {
                    holder.iv_lock.performClick()
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
                with(player!!) {
                    prepare(mediaSource, false, false)
                    playWhenReady = true
                    repeatMode =
                        com.google.android.exoplayer2.Player.REPEAT_MODE_ONE //repeat play video
                    setVideoScalingMode(com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                playerView.setShutterBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorBlack
                    )
                )
                playerView.player = player
                playerView.requestFocus()
                if (isLoad)
                    playerView.setShowBuffering(true)
                lastSeenTrackGroupArray = null
                listener.videoPlayClick(
                    true,
                    exerciseListing.get(position),
                    position,
                    holder,
                    isLoad
                )

                stopScroll.scrollToPosition(playerView.layoutParams.height, yPositionOnScreen)
                stopScroll.stopScrolling(false)
            }
            else {
                if (player != null) {
                    holder.fl_vv.removeAllViews()
                    player?.release()
                    holder.iv_lock.rotation = 0f
                }
            }
        }

        exerciseListing.get(position).isPlaying=!exerciseListing.get(position).isPlaying
    }

    fun removeVideoView(pos: Int, holder: MyView) {
        if (player != null) {
            listener.videoPlayClick(false, exerciseListing.get(pos), pos, holder, false)
            player?.release()
            exerciseListing.get(pos).isPlaying = false
            holder.iv_lock.rotation = 0f
            holder.fl_vv.removeAllViews()
            notifyItemChanged(pos)
        }
    }

    inner class MyView(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {
        val ivHideControllerButton: LinearLayout by lazy {
            view.findViewById<LinearLayout>(R.id.controller)
        }
        val tv_exercise = view.tv_exercise
        val iv_exercise = view.iv_exercise
        val timeLayout = view.time_layout
        val repsNumberLayout = view.reps_number_layout
        val repFinishTimeLayout = view.rep_finish_time_layout
        val fl_vv = view.fl_vv
        val rl_exercise = view.rl_exercise
        val iv_lock = view.iv_lock
        val radioGroup = view.radioGroup
        val time = view.time
        val reps = view.reps
        val timeExerciseTxt = view.time_exercise_txt
        val timerExerciseLayout = view.timer_exercise_layout
        val repetetionLayout = view.repetetion_layout
        val repsTimerLayout = view.reps_timer_layout
        val restTimerLayout = view.rest_timer_layout
        val repsTimerTxt = view.reps_timer_txt
        val restTimeTxt = view.rest_time_txt
        val repetetionTxt = view.repetetion_txt
        val rest_time_layout = view.rest_time_layout

        ////////
        val exchange_img = view.exchange_img
        val time_txt = view.time_txt
        val time_check = view.time_check
        val reps_txt = view.reps_txt
        val reps_check = view.reps_check
        val timeCheckLayout = view.time_check_layout
        val repsCheckLayout = view.reps_check_layout
        val delete_img = view.delete_img
        val copy_img = view.copy_img
        val margin_layout = view.margin_layout
        val main_layout = view.main_layout
        val select_time_txt = view.select_time_txt
        val select_reps_number_txt = view.select_reps_number_txt
        val reps_finish_time_txt = view.reps_finish_time_txt
        val frame_layout = view.frame_layout
        val deselect_workout_exercise = view.deselect_workout_exercise

        override fun onItemSelected() {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onItemClear() {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        init {
            itemView.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    mDragStartListener.onStartDrag(this@MyView)
                    return false;
                }
            })

            timerExerciseLayout.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, tempHolder)

                } else {
                    var str = exerciseListing.get(adapterPosition).exercise_timer_time!!.split(":")
                    timeListener.selectTime("time", adapterPosition, str[0], str[1])
                }
            }

            iv_lock.setOnClickListener {

                val locationOnScreen = IntArray(2)
                iv_lock.getLocationOnScreen(locationOnScreen)
                //Convert Position to Px
                val yInDp = locationOnScreen[1].toFloat() // Replace with your Y position in dp
                val yPositionOnScreen: Float
                val scale: Float = context.resources.displayMetrics.density
                yPositionOnScreen = yInDp * scale

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                        "Yes"
                    )
                ) {
                    playPauseVideo(adapterPosition, this,yPositionOnScreen)
                } else {
                    if (exerciseListing.get(adapterPosition).exercise_access_level.equals("OPEN")) {
                        playPauseVideo(adapterPosition, this,yPositionOnScreen)
                    }
                }
            }
            deselect_workout_exercise.setOnClickListener {
                exerciseListing[adapterPosition].isSelectedExercise =
                    !exerciseListing[adapterPosition].isSelectedExercise
                notifyDataSetChanged()

                timeListener.selectExercise(adapterPosition)
            }
            timeCheckLayout.setOnClickListener {

                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, tempHolder)

                } else {

                    exerciseListing.get(adapterPosition).exercise_timer_time =
                        getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)!!
                    this.timeExerciseTxt.text =
                        getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)

                    exerciseListing.get(adapterPosition).isReps = false
                    Glide.with(context).load(R.drawable.ic_active_radio_ico).into(this.time_check)
                    Glide.with(context).load(R.drawable.abc_radio_uncheck).into(this.reps_check)

                    this.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                    this.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

                    this.timeLayout.visibility = View.VISIBLE
                    this.repsNumberLayout.visibility = View.GONE
                    this.repFinishTimeLayout.visibility = View.GONE
                }
            }
            repsCheckLayout.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, tempHolder)

                } else {

                    exerciseListing.get(adapterPosition).exercise_reps_number =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_NUMBER_OF_REPS)!!
                    this.repetetionTxt.text =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_NUMBER_OF_REPS)

                    exerciseListing.get(adapterPosition).exercise_reps_time =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_REPS_FINISH_TIME)!!
                    this.repsTimerTxt.text =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_REPS_FINISH_TIME)



                    exerciseListing.get(adapterPosition).isReps = true
                    Glide.with(context).load(R.drawable.ic_active_radio_ico).into(this.reps_check)
                    Glide.with(context).load(R.drawable.abc_radio_uncheck).into(this.time_check)

                    this.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
                    this.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

                    this.timeLayout.visibility = View.GONE
                    this.repsNumberLayout.visibility = View.VISIBLE
                    this.repFinishTimeLayout.visibility = View.VISIBLE
                }
            }
            delete_img.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, this)
                } else {
                    if (adapterPosition != -1) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

                        } else {
                            mLastClickTime = SystemClock.elapsedRealtime()
                            timeListener.deleteExercise(adapterPosition)
                        }
                    }
                }
            }
            copy_img.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, tempHolder)
                } else {
                    Log.d("TAG", "adapter position....$adapterPosition")
                    if (adapterPosition != -1)
                        timeListener.copyExercise(adapterPosition)
                }

            }

            repetetionLayout.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, this)
                } else {
                    timeListener.selectRepetition(
                        "reps number",
                        adapterPosition,
                        exerciseListing.get(adapterPosition).exercise_reps_number!!
                    )
                }
            }
            repsTimerLayout.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, this)
                } else {
                    var str = exerciseListing.get(adapterPosition).exercise_reps_time!!.split(":")
                    timeListener.selectTime("reps time", adapterPosition, str[0], str[1])
                }
            }
            restTimerLayout.setOnClickListener {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying) {
                    removeVideoView(tempPos, this)
                } else {
                    var str = exerciseListing.get(adapterPosition).exercise_rest_time!!.split(":")
                    timeListener.selectTime("rest time", adapterPosition, str[0], str[1])
                }
            }
        }
    }

    private fun playPauseVideo(
        adapterPosition: Int,
        myView: MyView, y: Float
    ) {
        if (adapterPosition != tempPos) {
            if (tempPos != -1) {
                if (tempPos != -1 && exerciseListing.get(tempPos).isPlaying == true) {
                    removeVideoView(tempPos, tempHolder)
                    isDownloadedVideo(
                        exerciseListing.get(adapterPosition).exercise_video,
                        adapterPosition,
                        myView,y
                    )
                } else {
                    isDownloadedVideo(
                        exerciseListing.get(adapterPosition).exercise_video,
                        adapterPosition,
                        myView,y
                    )
                }
                tempPos = adapterPosition
                tempHolder = myView
            } else {
                tempPos = adapterPosition
                tempHolder = myView
                isDownloadedVideo(
                    exerciseListing.get(adapterPosition).exercise_video,
                    adapterPosition,
                    myView,y
                )
            }
        } else {
            tempPos = adapterPosition
            tempHolder = myView
            isDownloadedVideo(
                exerciseListing.get(adapterPosition).exercise_video,
                adapterPosition,
                myView,y
            )
        }

    }

    interface OnItemClick {
        fun videoPlayClick(isScroll: Boolean, data: ExerciseListingResponse.Data, position: Int, view: MyView, isLoad: Boolean)
        fun forExchangeItem(exerciseData: ExerciseListingResponse.Data, pos: Int)
    }

    interface SelectTime {
        public fun selectTime(timing: String, pos: Int, s: String, s1: String)
        public fun selectRepetition(timing: String, pos: Int, exerciseRepsNumber: String)
        public fun deleteExercise(pos: Int)
        public fun copyExercise(pos: Int)
        public fun selectExercise(pos: Int)
    }

    interface StopScroll {
        fun stopScrolling(isScroll: Boolean)
        fun scrollToPosition(position: Int, y: Float)
    }
}