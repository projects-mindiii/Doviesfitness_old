package com.doviesfitness.ui.bottom_tabbar.home_tab.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.ExerciseTransData
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.fl_vv
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.iv_exercise
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.iv_fav
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.iv_lock
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.iv_share
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.rl_exercise
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.tv_equipment
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.tv_exercise
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.tv_goal
import kotlinx.android.synthetic.main.workout_exercise_set_and_reps_iteview_ui.view.tv_level

class WorkoutDetailExerciseSetAndRepsAdapter(
    context: WorkOutDetailActivity,
    commetsList: ArrayList<ExerciseTransData>,
    listener: WorkOutItemClickListener,
    videowidth: Int,
    subListener: IsSubscribed
) :

    androidx.recyclerview.widget.RecyclerView.Adapter<WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder>() {
    var mContext: WorkOutDetailActivity
    var workoutList: ArrayList<ExerciseTransData>
    var listener: WorkOutItemClickListener
    val videowidth: Int
    var tempPos = -1

    private var player: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    var subListener: IsSubscribed
    var tempExerciseItem: ExerciseListingResponse.Data? = null

    var tempRoundPos = -1

    init {
        this.mContext = context
        this.workoutList = commetsList
        this.listener = listener
        this.videowidth = videowidth
        this.subListener = subListener
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val workoutData: ExerciseTransData = workoutList[position]
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.workout_exercise_set_and_reps_iteview_ui,
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

        Glide.with(holder.iv_exercise.context).load(exercise.workout_exercise_image)
            .into(holder.iv_exercise)

        try {
            holder.tv_exercise.text = exercise.workout_exercise_name

            holder.tv_level.text = exercise.workout_exercise_level.replace("|", ",")

            if (exercise.workout_exercise_equipments != null)
                holder.tv_equipment.text = exercise.workout_exercise_equipments.replace("|", ",")

            holder.tv_goal.text = exercise.workout_exercise_body_parts.replace("|", ",")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        /**below code is commented by santosh need to check after detail work complete */
        /* if (exercise.workout_exercise_detail.contains("secs") || exercise.workout_exercise_detail.contains(
                 "sec"
             )
         ) {
             if (exercise.workout_exercise_detail.contains("01")) {
                 holder.tv_exercise_time.text = "01 sec"
             }
             else {
                 holder.tv_exercise_time.text = exercise.workout_exercise_detail
             }
         }
         else if (exercise.workout_exercise_detail.contains("mins") || exercise.workout_exercise_detail.contains(
                 "min"
             )
         ) {
             if (exercise.workout_exercise_detail.contains("01")) {
                 holder.tv_exercise_time.text = "01 min"
             } else {
                 holder.tv_exercise_time.text = exercise.workout_exercise_detail
             }
         } else
         {
             holder.tv_exercise_time.text =
                 exercise.workout_exercise_detail + " " + mContext.getString(R.string.reps)
         }

         if (position == workoutList.size - 1) {
             holder.RestTimelayout.visibility = View.GONE
             holder.restTimeTv.text = ""
         } else {
             if (exercise.workout_exercise_break_time.equals("00:00:00")) {
                 holder.RestTimelayout.visibility = View.GONE
                 holder.restTimeTv.text = ""
             }
             else {
                 holder.RestTimelayout.visibility = View.VISIBLE
                // var BRTime: String = exercise.workout_exercise_break_time
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
         }*/

        /*@param : exercise_access_level == Lock : "Show lock img"
        * @param : exercise_access_level == OPEN : "Show Down arrow img"
        * */

        if (Doviesfitness.getDataManager()
                .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
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
            if (exercise.exercise_access_level == "LOCK") {
                holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_lock_balck
                    )
                )
            } else if (exercise.exercise_access_level == "OPEN") {
                holder.iv_lock.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_down_button
                    )
                )
            }
        }

        //---------Allow User-------------------------

        if (WorkOutDetailActivity.isalloweduser || WorkOutDetailActivity.Parentisalloweduser) {

            holder.iv_lock.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_down_button
                )
            )
        }


        //----------------------------------------------

        if (exercise.workout_exercise_is_favourite == "0") {
            holder.ivFav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star))
        } else {
            holder.ivFav.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_star_active
                )
            )
        }

        if (!exercise.isPlaying) {
            holder.fl_vv.removeAllViews()
            holder.iv_lock.rotation = 0f
        } else {

        }

        holder.ivFav.setOnClickListener() {
            if (exercise.workout_exercise_is_favourite.equals("0")) {
                holder.ivFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_star_active
                    )
                )
                listener.setFavUnfav(exercise, position, holder.ivFav)
            } else {
                holder.ivFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ic_star
                    )
                )
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
            // listener.videoPlayClick(false, workoutList[position], position, holder, false,player!!)
           // exercise.isPlaying = !exercise.isPlaying
        //    notifyDataSetChanged()
            val locationOnScreen = IntArray(2)

            holder.iv_lock.getLocationOnScreen(locationOnScreen)

            //Convert Position to Px
            val yInDp = locationOnScreen[1].toFloat() // Replace with your Y position in dp
            val yPositionOnScreen: Float
            val scale: Float = mContext.resources.displayMetrics.density
            yPositionOnScreen = yInDp * scale

            Handler().postDelayed(
                {
                    if (WorkOutDetailActivity.isalloweduser || WorkOutDetailActivity.Parentisalloweduser) {
                        listener.isDownloadedVideo(
                            exercise.workout_exercise_video,
                            position,
                            holder,
                            yPositionOnScreen
                        )
                    } else

                        if (Doviesfitness.getDataManager()
                                .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!! == "Yes"
                        ) {
                            listener.isDownloadedVideo(
                                exercise.workout_exercise_video,
                                position,
                                holder,
                                yPositionOnScreen
                            )
                        } else {
                            if (mContext.workoutAccessLvel.equals("OPEN", true)) {

                                if (exercise.exercise_access_level == "OPEN") {
                                    listener.isDownloadedVideo(
                                        exercise.workout_exercise_video,
                                        position,
                                        holder,
                                        yPositionOnScreen
                                    )
                                } else {
                                    subListener.isSubscribed()
                                }
                            } else {
                                subListener.isSubscribed()
                            }
                        }
                }, 1000
            )

        }

    }


    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivHideControllerButton: LinearLayout by lazy {
            view.findViewById<LinearLayout>(R.id.controller)
        }
        val tv_exercise = view.tv_exercise
        val iv_exercise = view.iv_exercise

        //   val tv_exercise_time = view.tv_exercise_time
        val tv_level = view.tv_level
        val tv_equipment = view.tv_equipment
        val tv_goal = view.tv_goal
        val fl_vv = view.fl_vv
        val rl_exercise = view.rl_exercise
        val iv_lock = view.iv_lock
        val ivFav = view.iv_fav
        val ivShare = view.iv_share
        /*val restTimeTv = view.rest_time_tv
        val RestTimelayout = view.time_layout*/
        // val bottomLayout = view.bottom_layout
    }

    interface WorkOutItemClickListener {
        fun videoPlayClick(
            isScroll: Boolean,
            data: ExerciseTransData,
            position: Int,
            view: MyViewHolder,
            isLoad: Boolean,
            player: Player?
        )

        fun shareURL(data: ExerciseTransData)
        fun hidePlayer(
            data: ExerciseTransData,
            position: Int,
            view: MyViewHolder,
            isLoad: Boolean,
            player: Player?
        )

        fun setFavUnfav(data: ExerciseTransData, position: Int, view: ImageView)
        fun isDownloadedVideo(
            workoutExerciseVideo: String,
            position: Int,
            holder: MyViewHolder,
            yPositionOnScreen: Float
        )
    }


}

