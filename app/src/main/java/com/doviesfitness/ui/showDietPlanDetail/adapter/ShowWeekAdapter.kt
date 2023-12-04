package com.doviesfitness.ui.showDietPlanDetail.adapter

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import com.doviesfitness.utils.CommanUtils
import kotlinx.android.synthetic.main.show_week_view_layout.view.*
import kotlinx.android.synthetic.main.week_view_layout.view.iv_workout
import kotlinx.android.synthetic.main.week_view_layout.view.rr_rest_day_view
import kotlinx.android.synthetic.main.week_view_layout.view.show_main_view
import kotlinx.android.synthetic.main.week_view_layout.view.tv_categoury
import kotlinx.android.synthetic.main.week_view_layout.view.tv_day
import kotlinx.android.synthetic.main.week_view_layout.view.tv_day_count
import kotlinx.android.synthetic.main.week_view_layout.view.tv_heading
import kotlinx.android.synthetic.main.week_view_layout.view.tv_time_1
import java.util.regex.Pattern


class ShowWeekAdapter(
    context: Context,
    dietPlancategoryList: ArrayList<WorkOutListModal.Data>,
    changeStatusOfWorkout: ChangeStatusOfWorkout
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ShowWeekAdapter.MyViewHolder>() {
    private var context: Context
    var dietPlancategoryList: ArrayList<WorkOutListModal.Data>
    var changeStatusOfWorkout: ChangeStatusOfWorkout

    init {
        this.context = context
        this.dietPlancategoryList = dietPlancategoryList
        this.changeStatusOfWorkout = changeStatusOfWorkout
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val dietPlancategoryModal = dietPlancategoryList.get(pos)

        // when show two view at a time that why i manage like this
        if (!dietPlancategoryModal.workout_id.isEmpty() && !dietPlancategoryModal.workout_id.equals(
                "0"
            )
        ) {

            holder.showMainView.visibility = View.VISIBLE
            holder.rrRestDayView.visibility = View.GONE

            val workout_name = CommanUtils.capitalize(dietPlancategoryModal.workout_name)
            holder.tvHeading.setText(workout_name)
            holder.tvCategoury.setText(dietPlancategoryModal.workout_category)
            // holder.tvTime.setText(dietPlancategoryModal.workout_time)

            if ("1".equals(dietPlancategoryModal.workout_fav_status)) {
                holder.ivWorkoutDone.visibility = View.VISIBLE
            } else {
                holder.ivWorkoutDone.visibility = View.GONE
            }

            if (dietPlancategoryModal.workout_time.isEmpty() != null && !dietPlancategoryModal.workout_time.isEmpty()) {
                val separated = dietPlancategoryModal.workout_time.split(":")
                val time = separated[0] // this will contain "Fruit"
                holder.tvTime.setText(time + " min")
            }

            val dayCount = dietPlancategoryModal.forDay!!.toInt() + 1
            holder.tvDayCount.setText("Day " + dayCount)

            if (!dietPlancategoryModal.workout_image.isEmpty()) {
                Glide.with(holder.ivWorkout_pf).load(dietPlancategoryModal.workout_image)
                    .into(holder.ivWorkout_pf)
            }

        } else {
            holder.showMainView.visibility = View.GONE
            holder.rrRestDayView.visibility = View.VISIBLE

            val builder = SpannableStringBuilder()

            if (!dietPlancategoryModal.forDay.equals("")) {
                val dayCount = dietPlancategoryModal.forDay!!.toInt() + 1
                val red = "Day " + dayCount + ""
                val redSpannable = SpannableString(red)
                redSpannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            R.color.colorGray8
                        )
                    ), 0, red.length, 0
                )
                builder.append(redSpannable)

                holder.tvDay.setText(builder, TextView.BufferType.SPANNABLE)
            }

        }

        holder.ivWorkoutDone.setOnClickListener {
            // useing method
            changeStatusOfWorkout.changeStatus(dietPlancategoryModal, pos, "UpdateStatus")

        }

        holder.showMainView.setOnClickListener {
            if (!dietPlancategoryModal.workout_id.isEmpty() && !dietPlancategoryModal.workout_id.equals(
                    "0"
                )
            ) {
                // useing method
                changeStatusOfWorkout.changeStatus(dietPlancategoryModal, pos, "forCompleteWorkout")
                /* val intent = Intent(context, WorkOutDetailActivity::class.java)
                 intent.putExtra("PROGRAM_DETAIL", dietPlancategoryModal.workout_id)
                 intent.putExtra("from_ProgramPlan", dietPlancategoryModal.program_WorkOut_id)
                 intent.putExtra("isMyWorkout", "no")
                 intent.putExtra("fromDeepLinking", "")
                 context.startActivity(intent)*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.show_week_view_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dietPlancategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var tvDay = view.tv_day
        var tvDayCount = view.tv_day_count
        var showMainView = view.show_main_view
        val ivWorkout_pf = view.iv_workout
        val tvTime = view.tv_time_1
        val tvHeading = view.tv_heading
        val tvCategoury = view.tv_categoury
        val rrRestDayView = view.rr_rest_day_view
        val ivWorkoutDone = view.iv_workout_done
    }

    interface ChangeStatusOfWorkout {
        fun changeStatus(data: WorkOutListModal.Data, pos: Int, status: String)
    }

}