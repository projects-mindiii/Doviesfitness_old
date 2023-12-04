package com.doviesfitness.ui.bottom_tabbar.exercise_listing

import android.content.Context
import android.os.SystemClock
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import kotlinx.android.synthetic.main.exercise_listing_item_view.view.*

class ExerciseListAdapter(context: Context, exerciseListing: ArrayList<ExerciseListingResponse.Data>, listener: ExerciseVideoPlayAdapter.OnItemClick,
                          stopScroll:  StopScroll,         from: String, subsListener: IsSubscribed) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context;
    private var exerciseListing = ArrayList<ExerciseListingResponse.Data>()
    var listener: ExerciseVideoPlayAdapter.OnItemClick
//    val videowidth: Int
    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2
    private var isCheck: Boolean = false
    var subsListener: IsSubscribed
    var from = ""
    var isOpenPos=-1
    var  stopScroll:  StopScroll
    private var mLastClickTime: Long = 0

    init {
        this.context = context
        this.exerciseListing = exerciseListing
       // this.videowidth = videowidth
        this.listener = listener
        this.stopScroll = stopScroll
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
                        R.layout.exercise_listing_item_view,
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
            rvHolder.mProgressBar.visibility = View.VISIBLE
         }
        else {
            val holder = rvHolder as MyViewHolder
            val exercise = exerciseListing.get(pos)
            holder.exerciseVideoRv.visibility=View.GONE
            holder.bottomLayout.visibility = View.GONE
           /* if (pos == exerciseListing.size - 1) {
                holder.bottomLayout.visibility = View.VISIBLE
            } else {
                holder.bottomLayout.visibility = View.GONE
            }*/
            if (from.equals("create")) {
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
            if (exercise.isSelected == true) {
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
            Glide.with(holder.iv_exercise.context).load(exercise.exercise_image)
                .fitCenter()
                .into(holder.iv_exercise)

            holder.tv_exercise.text = exercise.exercise_name
            if (exercise.exercise_level.equals("All", true))
                holder.tv_level.text = exercise.exercise_level.replace("|", ", ") + " level"
            else
                holder.tv_level.text = exercise.exercise_level.replace("|", ", ")

            holder.tv_equipment.text = exercise.exercise_equipments.replace("|", ", ")
            holder.tv_goal.text = exercise.exercise_body_parts.replace("|", ", ")
            holder.tv_exercise_time.visibility = View.GONE

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")) {
                holder.iv_lock.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_down_button))
            }
            else {
                if (exercise.exercise_access_level.equals("LOCK")) {
                    holder.iv_lock.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock_balck))
                } else if (exercise.exercise_access_level.equals("OPEN")) {
                    holder.iv_lock.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_down_button
                        )
                    )
                }
            }

            holder.main_layout.setOnClickListener {
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")) {
                    isDownloadedVideo(exercise.exercise_video, pos, holder)
                } else {
                    if (exercise.exercise_access_level.equals("OPEN")) {
                        isDownloadedVideo(exercise.exercise_video, pos, holder)
                    } else {
                        subsListener.isSubscribed()
                    }
                }
            }

            holder.selectExerciseIcon.setOnClickListener {
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")) {
                    if (exercise.isSelected == true) {
                        exercise.isSelected = false
                        holder.selectExerciseIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_circle_img
                            )
                        )
                       // listener.setSelected(pos, false, exercise)
                    } else {
                        exercise.isSelected = true
                        holder.selectExerciseIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_check_mark
                            )
                        )
                       // listener.setSelected(pos, true, exercise)
                    }

                } else {
                    if (exercise.exercise_access_level.equals("OPEN")) {
                        if (exercise.isSelected == true) {
                            exercise.isSelected = false
                            holder.selectExerciseIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_circle_img
                                )
                            )
                          //  listener.setSelected(pos, false, exercise)
                        } else {
                            exercise.isSelected = true
                            holder.selectExerciseIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_check_mark
                                )
                            )
                         //   listener.setSelected(pos, true, exercise)
                        }
                    }
                }
            }


            if (exercise.exercise_is_favourite.equals("0")) {
                holder.iv_fav.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_star
                    )
                )
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
                  //  listener.setFavUnfavForExercies(exercise, pos, holder.iv_fav)
                } else {
                    isCheck = true
                    holder.iv_fav.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_star
                        )
                    )
                 //   listener.setFavUnfavForExercies(exercise, pos, holder.iv_fav)
                }
            }

            holder.iv_share.setOnClickListener() {
              //  listener.shareURL(exercise)
            }
        }
    }

    interface StopScroll{
        public fun stopScrolling( view1: ExerciseListAdapter.MyViewHolder);
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
        val rl_exercise = view.rl_exercise
        val iv_lock = view.iv_lock
        val iv_share = view.iv_share
        val hideView = view.hide_view
        val selectExerciseIcon = view.select_exercise_icon
        val iv_fav = view.iv_fav
        val main_layout = view.main_layout
        val bottomLayout = view.bottom_layout
        val exerciseVideoRv = view.exercise_video_rv
    }

    fun isDownloadedVideo(videoUrl: String, position: Int, holder: MyViewHolder) {

        if (isOpenPos!=-1){
            stopVideo(holder,isOpenPos)
        }
        else{

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return
            } else {
                mLastClickTime = SystemClock.elapsedRealtime()
                var list=ArrayList<ExerciseListingResponse.Data>()
                list.add(exerciseListing.get(position))
                isOpenPos=position
                holder.exerciseVideoRv.visibility=View.VISIBLE
                var myAdapter=ExerciseVideoPlayAdapter(context,list,listener,holder,position)
                var myLayoutManager=LinearLayoutManager(context)
                holder.exerciseVideoRv.layoutManager = myLayoutManager
                holder.exerciseVideoRv.adapter = myAdapter
                holder.exerciseVideoRv.requestFocus()
            }

        }

    }
    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    fun stopVideo(holder:MyViewHolder,Pos:Int){
        var myLayoutManager=LinearLayoutManager(context)
        holder.exerciseVideoRv.layoutManager = myLayoutManager
        holder.exerciseVideoRv.visibility=View.GONE
        holder.exerciseVideoRv.adapter = null
        notifyItemChanged(Pos)
        isOpenPos=-1
        stopScroll.stopScrolling(holder)
    }

}
