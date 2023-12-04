package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperAdapter
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperViewHolder
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
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
import kotlinx.android.synthetic.main.create_exercise_item_view.view.*
import java.io.File
import java.util.*

class OldAddedExerciseAdapter(
    context: Context,
    exerciseListing: ArrayList<ExerciseListingResponse.Data>,
    listener: OnItemClick,
    videowidth: Int,
    timeListener: SelectTime,
    mDragStartListener: OnStartDragListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {


    private var context: Context;
    private var exerciseListing = ArrayList<ExerciseListingResponse.Data>()
    var listener: OnItemClick
    val videowidth: Int
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var isCheck: Boolean = false
    lateinit var timeListener: SelectTime
    val mDragStartListener: OnStartDragListener

    var from = ""

    init {
        this.context = context
        this.exerciseListing = exerciseListing
        this.videowidth = videowidth
        this.listener = listener
        this.from = from
        this.timeListener = timeListener
        this.mDragStartListener =mDragStartListener
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.create_exercise_item_view,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int {
        return exerciseListing.size
    }
    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        val holder = rvHolder as MyViewHolder
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
        Glide.with(holder.iv_exercise.context).load(exercise.exercise_image).into(holder.iv_exercise)

        if (pos == exerciseListing.size - 1) {
            holder.rest_time_layout.visibility = View.GONE
            holder.margin_layout.visibility = View.GONE
        } else {
            holder.rest_time_layout.visibility = View.VISIBLE
            holder.margin_layout.visibility = View.GONE
        }
        if (exerciseListing.get(pos).isValid) {
            /*var border = GradientDrawable();
            border.setStroke(0, ContextCompat.getColor(context, R.color.colorWhite))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                holder.frame_layout.setBackgroundDrawable(border);
            } else {
                holder.frame_layout.setBackground(border);
            }*/
            holder.frame_layout.setBackgroundResource(0);
            holder.select_time_txt.text = context.resources.getString(R.string.select_time)
            holder.select_reps_number_txt.text = context.resources.getString(R.string.select_number_of_reps)
            holder.reps_finish_time_txt.text = context.resources.getString(R.string.time_to_finish_reps)
        } else {
            /*var border = GradientDrawable();
            border.setStroke(2, ContextCompat.getColor(context, R.color.colorRed))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                holder.frame_layout.setBackgroundDrawable(border);
            } else {
                holder.frame_layout.setBackground(border);
            }*/
            holder.frame_layout.setBackgroundResource(R.drawable.border_red);

            if (exercise.isReps) {
                holder.select_time_txt.text = context.resources.getString(R.string.select_time)
                if (exercise.exercise_reps_number!!.isEmpty() || exercise.exercise_reps_number.equals("00")) {
                    holder.select_reps_number_txt.text =
                        Html.fromHtml(context.resources.getString(R.string.select_number_of_reps_error))
                    holder.reps_finish_time_txt.text = context.resources.getString(R.string.time_to_finish_reps)

                } else {
                    holder.reps_finish_time_txt.text =
                        Html.fromHtml(context.resources.getString(R.string.time_to_finish_reps_error))
                    holder.select_reps_number_txt.text = context.resources.getString(R.string.select_number_of_reps)
                }
            } else {
                holder.select_time_txt.text = Html.fromHtml(context.resources.getString(R.string.select_time_error))
                holder.select_reps_number_txt.text = context.resources.getString(R.string.select_number_of_reps)
                holder.reps_finish_time_txt.text = context.resources.getString(R.string.time_to_finish_reps)
            }

        }
        if (exercise.isReps == true) {
            Glide.with(context).load(R.drawable.ic_active_radio_ico).into(holder.reps_check)
            Glide.with(context).load(R.drawable.abc_radio_uncheck).into(holder.time_check)

            holder.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray))
            holder.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

            holder.timeLayout.visibility = View.GONE
            holder.repsNumberLayout.visibility = View.VISIBLE
            holder.repFinishTimeLayout.visibility = View.VISIBLE
        } else {
            Glide.with(context).load(R.drawable.ic_active_radio_ico).into(holder.time_check)
            Glide.with(context).load(R.drawable.abc_radio_uncheck).into(holder.reps_check)

            holder.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray))
            holder.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

            holder.timeLayout.visibility = View.VISIBLE
            holder.repsNumberLayout.visibility = View.GONE
            holder.repFinishTimeLayout.visibility = View.GONE
        }
        if (!exercise.isPlaying) {
            holder.fl_vv.removeAllViews()
            holder.iv_lock.rotation = 0f
        } else {

        }
        holder.iv_lock.setOnClickListener {
            if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")) {
                isDownloadedVideo(exercise.exercise_video, pos, holder)
            }else {
                if (exercise.exercise_access_level.equals("OPEN")) {

                    isDownloadedVideo(exercise.exercise_video, pos, holder)
                }
            }
        }
        holder.timeCheckLayout.setOnClickListener {

            if (exerciseListing.get(pos).isPlaying) {
                listener.videoPlayClick(false, exerciseListing.get(pos), pos, holder, false)
                player.release()
                exerciseListing.get(pos).isPlaying = false
                holder.iv_lock.rotation = 0f
                holder.fl_vv.removeAllViews()
            } else {

                exercise.isReps = false
                Glide.with(context).load(R.drawable.ic_active_radio_ico).into(holder.time_check)
                Glide.with(context).load(R.drawable.abc_radio_uncheck).into(holder.reps_check)

                holder.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray))
                holder.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

                holder.timeLayout.visibility = View.VISIBLE
                holder.repsNumberLayout.visibility = View.GONE
                holder.repFinishTimeLayout.visibility = View.GONE
            }
        }
        holder.repsCheckLayout.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                listener.videoPlayClick(false, exerciseListing.get(pos), pos, holder, false)
                player.release()
                exerciseListing.get(pos).isPlaying = false
                holder.iv_lock.rotation = 0f
                holder.fl_vv.removeAllViews()
            } else {
                exercise.isReps = true
                Glide.with(context).load(R.drawable.ic_active_radio_ico).into(holder.reps_check)
                Glide.with(context).load(R.drawable.abc_radio_uncheck).into(holder.time_check)

                holder.reps_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray))
                holder.time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorGray13))

                holder.timeLayout.visibility = View.GONE
                holder.repsNumberLayout.visibility = View.VISIBLE
                holder.repFinishTimeLayout.visibility = View.VISIBLE
            }
        }
        holder.delete_img.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                removeVideoView(pos, holder)
            } else {
                timeListener.deleteExercise(pos)
            }
        }
        holder.copy_img.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                removeVideoView(pos, holder)
            } else {
                timeListener.copyExercise(pos)
            }

        }
        holder.timerExerciseLayout.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                removeVideoView(pos, holder)
            } else {
                timeListener.selectTime("time", pos)
            }
        }
        holder.repetetionLayout.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                removeVideoView(pos, holder)
            } else {
                timeListener.selectRepetition("reps number", pos)
            }
        }
        holder.repsTimerLayout.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                removeVideoView(pos, holder)
            } else {
                timeListener.selectTime("reps time", pos)
            }
        }
        holder.restTimerLayout.setOnClickListener {
            if (exerciseListing.get(pos).isPlaying) {
                removeVideoView(pos, holder)
            } else {
                timeListener.selectTime("rest time", pos)
            }
        }

    }

    fun removeVideoView(pos: Int, holder: MyViewHolder) {
        if (player!=null) {
            listener.videoPlayClick(false, exerciseListing.get(pos), pos, holder, false)
            player.release()
            exerciseListing.get(pos).isPlaying = false
            holder.iv_lock.rotation = 0f
            holder.fl_vv.removeAllViews()
        }
    }


    inner class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {

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

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

        init {
            itemView.setOnLongClickListener {
                mDragStartListener.onStartDrag(this)
                true
            }
        }

    }

    fun isDownloadedVideo(videoUrl: String, position: Int, holder: MyViewHolder) {
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            // var subpath = "/Dovies//$downloadFileName.mp4"
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            Log.e("download file path", "file path..." + f.absolutePath)
            if (!f.exists()) {
                var uri = Uri.parse(videoUrl)
                intializePlayer(uri, position, holder, true)
            } else {
                var uri = Uri.parse(path)
                intializePlayer(uri, position, holder, false)
                // Toast.makeText(mContext, "already downloaded...", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun intializePlayer(news_video: Uri, position: Int, holder: MyViewHolder, isLoad: Boolean) {
        var playerView: PlayerView
        playerView = PlayerView(context)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        if (exerciseListing.size != 0 && exerciseListing.size != -1) {
            if (!exerciseListing.get(position).isPlaying) {
                holder.iv_lock.rotation = 180f
                holder.fl_vv.addView(playerView)
                holder.fl_vv.requestFocus()
                /*val animation = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in)
                   animation.duration = 1000
                   animation.startOffset = 0;
                   holder.fl_vv.startAnimation(animation);*/
                playerView.layoutParams.height = videowidth
                playerView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
                mediaDataSourceFactory =
                    DefaultDataSourceFactory(context, Util.getUserAgent(context, "mediaPlayerSample"))
                val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(news_video)
                player = ExoPlayerFactory.newSimpleInstance(context, trackSelector!!)
                with(player) {
                    prepare(mediaSource, false, false)
                    playWhenReady = true
                    repeatMode = com.google.android.exoplayer2.Player.REPEAT_MODE_ONE //repeat play video
                    setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                playerView.setShutterBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
                playerView.player = player
                playerView.requestFocus()
               // holder.ivHideControllerButton.visibility = View.GONE

                if (isLoad)
                    playerView.setShowBuffering(true)
/*
                holder.ivHideControllerButton.setOnClickListener {
                }
*/

                lastSeenTrackGroupArray = null
                listener.videoPlayClick(true, exerciseListing.get(position), position, holder, isLoad)

            } else {
                if (player!=null) {
                  /*  val animation = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out)
                    animation.duration = 1000
                    animation.startOffset = 0;
                    holder.fl_vv.startAnimation(animation);*/
                    holder.fl_vv.removeAllViews()
                    listener.videoPlayClick(false, exerciseListing.get(position), position, holder, false)
                    player.release()
                    exerciseListing.get(position).isPlaying = false
                    holder.iv_lock.rotation = 0f


                }
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
    }

    override fun onItemDismiss(position: Int) {
        /*Log.v("AddedExerciseAdapter", "Swaped: Dismiss Item");
        exerciseListing.removeAt(position)
        notifyItemRemoved(position)*/
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        /*Log.v("AddedExerciseAdapter", "Position" + fromPosition + " " + toPosition);
        if (fromPosition < exerciseListing.size && toPosition < exerciseListing.size) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(exerciseListing, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(exerciseListing, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }
        return true*/
        Collections.swap(exerciseListing, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


    interface SelectTime {
        public fun selectTime(timing: String, pos: Int)
        public fun selectRepetition(timing: String, pos: Int)
        public fun deleteExercise(pos: Int)
        public fun copyExercise(pos: Int)
    }
}

