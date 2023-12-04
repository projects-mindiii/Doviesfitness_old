package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.workout_video_view.view.*

class WorkoutVideoPlayAdapter(
    context: Context,
    exerciseListing: ArrayList<WorkoutExercise>,
    listener: OnItemClick
) : androidx.recyclerview.widget.RecyclerView.Adapter<WorkoutVideoPlayAdapter.MyViewHolder>() {

    private var context: Context;
    private var exerciseListing = ArrayList<WorkoutExercise>()
    var listener: OnItemClick

    init {
        this.context = context
        this.exerciseListing = exerciseListing

        this.listener = listener
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutVideoPlayAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.workout_video_view,
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

    override fun onBindViewHolder(rvHolder: WorkoutVideoPlayAdapter.MyViewHolder, pos: Int) {

        val exercise: WorkoutExercise = exerciseListing.get(pos)
        rvHolder.exerciseName.text = exercise.workout_exercise_name

        if (exercise.workout_exercise_type.equals("Repeat")) {
            rvHolder.repetetion.visibility = View.VISIBLE
            var str=exercise.workout_repeat_text
            if (str.equals("01")||str.equals("1"))
            rvHolder.repetetion.text = exercise.workout_repeat_text + " " + "Rep"
            else
            rvHolder.repetetion.text = exercise.workout_repeat_text + " " + "Reps"
        } else {
            rvHolder.repetetion.visibility = View.GONE
        }

        if (exercise.isComplete == true) {
            Glide.with(context).load(R.drawable.ic_check_mark).into(rvHolder.completeIcon)
        } else {
            Glide.with(context).load(R.drawable.ic_circle_img).into(rvHolder.completeIcon)
        }

        if (exercise.Progress == 0) {
            rvHolder.progress.visibility = View.GONE
            rvHolder.bottomView.visibility = View.VISIBLE
            rvHolder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.colorGray12))
            rvHolder.repetetion.setTextColor(ContextCompat.getColor(context, R.color.colorGray12))
            rvHolder.itemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorBlack
                )
            )

        } else {
            rvHolder.progress.visibility = View.VISIBLE
            rvHolder.bottomView.visibility = View.GONE
            rvHolder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            rvHolder.repetetion.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            rvHolder.itemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.splash_screen_color
                )
            )
            // rvHolder.timeLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.c))

        }
        rvHolder.progress.max = exercise.MaxProgress * 1000
        rvHolder.progress.progress = exercise.Progress

        val mProgress = (exercise.MaxProgress - (exercise.Progress / 1000))

        if (mProgress >= 60) {
            var min = "00"
            var sec = "00"
            if ((mProgress / 60) < 10) {
                min = (mProgress / 60).toString()
            } else {
                min = (mProgress / 60).toString()
            }
            if ((mProgress % 60) < 10) {
                sec = "0" + (mProgress % 60).toString()
            } else {
                sec = (mProgress % 60).toString()
            }
            rvHolder.time.text = min + " : " + sec
        } else {
            if (mProgress > -1) {
                if (mProgress < 10)
                    rvHolder.time.text = "0:0" + (mProgress).toString()
                else
                    rvHolder.time.text = "0:" + (mProgress).toString()
            }
        }
        if (exercise.workout_exercise_break_time != "00:00:00" && exercise.Progress == 0 && exercise.isRestTime) {

            rvHolder.timeLayout.visibility = View.VISIBLE
            rvHolder.timeLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.splash_screen_color
                )
            )

            val tokens = Constant.getRestTimeToken(exercise.workout_exercise_break_time)
            if (tokens[0].equals("00")) {
                if (tokens[1].equals("00")) {
                    rvHolder.restTimeTv.text = tokens[2] + " Seconds"
                } else {
                    rvHolder.restTimeTv.text = tokens[1] + ":" + tokens[2] + " Minutes"
                }
            } else {
                rvHolder.restTimeTv.text = tokens[0] + ":" + tokens[1] + ":" + tokens[2] + " Hours"
            }
        } else if (!exercise.workout_exercise_break_time.equals("00:00:00")) {

            val tokens = Constant.getRestTimeToken(exercise.workout_exercise_break_time)
            if (tokens[0].equals("00")) {
                if (tokens[1].equals("00")) {
                    rvHolder.restTimeTv.text = tokens[2] + " Seconds"
                } else {
                    rvHolder.restTimeTv.text = tokens[1] + " : " + tokens[2] + " Minutes"
                }
            } else {
                rvHolder.restTimeTv.text =
                    tokens[0] + " : " + tokens[1] + " : " + tokens[2] + " Hours"
            }
            rvHolder.timeLayout.visibility = View.VISIBLE
            rvHolder.timeLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorBlack
                )
            )

        } else {
            rvHolder.timeLayout.visibility = View.GONE
            rvHolder.timeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlack))
        }
        rvHolder.itemLayout.setOnClickListener() {
            listener.onLayoutClick(pos)
        }
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val exerciseName = view.exercise_name
        val itemLayout = view.item_layout
        val time = view.time
        val progress = view.progress
        val timeLayout = view.time_layout
        val restTimeTv = view.rest_time_tv
        val repetetion = view.repetetion
        val bottomView = view.bottom_view
        val completeIcon = view.complete_icon
    }

    public interface OnItemClick {
        fun onLayoutClick(pos: Int)
    }
}



