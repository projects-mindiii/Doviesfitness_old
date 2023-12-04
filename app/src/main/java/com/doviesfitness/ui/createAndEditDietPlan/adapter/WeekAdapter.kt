package com.doviesfitness.ui.createAndEditDietPlan.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView.BufferType
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import kotlinx.android.synthetic.main.week_view_layout.view.*
import java.util.*

class WeekAdapter(
    context: Context,
    dietPlancategoryList: ArrayList<WorkOutListModal.Data>,
    weekListener: WeekListener
) :

    androidx.recyclerview.widget.RecyclerView.Adapter<WeekAdapter.MyViewHolder>() {
    private var context: Context
    var dietPlancategoryList: ArrayList<WorkOutListModal.Data>
    var weekListener: WeekListener

    init {
        this.context = context
        this.dietPlancategoryList = dietPlancategoryList
        this.weekListener = weekListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val dietPlancategoryModal = dietPlancategoryList.get(pos)

        // when show two view at a time that why i manage like this
        if (!dietPlancategoryModal.workout_id.isEmpty() && !dietPlancategoryModal.workout_id.equals("0")) {

            holder.showMainView.visibility = View.VISIBLE
            holder.rrRestDayView.visibility = View.GONE

            holder.tvHeading.setText(dietPlancategoryModal.workout_name)
            holder.tvCategoury.setText(dietPlancategoryModal.workout_category)

            if (dietPlancategoryModal.workout_time.isEmpty() != null && !dietPlancategoryModal.workout_time.isEmpty()) {
                val separated = dietPlancategoryModal.workout_time.split(":")
                val time = separated[0] // this will contain "Fruit"

                if (time.contains("min")) {
                    holder.tvTime.setText(time)
                } else {
                    holder.tvTime.setText(time + " min")
                }

            }

            val dayCount = dietPlancategoryModal.forDay!!.toInt() + 1
            holder.tvDayCount.setText("Day " + dayCount)


            if (!dietPlancategoryModal.workout_image.isEmpty()) {
                Glide.with(holder.ivWorkout_pf).load(dietPlancategoryModal.workout_image)
                    .placeholder(R.drawable.place_holder_of_plan)
                    .into(holder.ivWorkout_pf)
            }

        } else {
            holder.showMainView.visibility = View.GONE
            holder.rrRestDayView.visibility = View.VISIBLE

            val builder = SpannableStringBuilder()

            val dayCount = dietPlancategoryModal.forDay!!.toInt() + 1
            val red = "Day " + dayCount + ""
            val redSpannable = SpannableString(red)
            redSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorGray8)),
                0,
                red.length,
                0
            )
            builder.append(redSpannable)

            val white = "\n" + "Rest Day"
            val whiteSpannable = SpannableString(white)
            whiteSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.col_hint)),
                0,
                white.length,
                0
            )
            builder.append(whiteSpannable)

            holder.tvDay.setText(builder, BufferType.SPANNABLE)

            //image only get when you come from edit this worout otherwise no image show
            if(!dietPlancategoryModal.workout_image.isEmpty()){
                Glide.with(holder.ivWorkout_no_image).load(dietPlancategoryModal.workout_image).placeholder(R.drawable.place_holder_of_plan)
                    .into(holder.ivWorkout_no_image)
            }
        }

        holder.ivAdd.setOnClickListener {
            weekListener.getDietCategoryDetailsInfo(dietPlancategoryModal, pos, "Add")
        }

        holder.ivEditWork.setOnClickListener {
            weekListener.getDietCategoryDetailsInfo(dietPlancategoryModal, pos, "Edit")
        }

        holder.ivDeleteWork.setOnClickListener {
            weekListener.getDietCategoryDetailsInfo(dietPlancategoryModal, pos, "Delete")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.week_view_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dietPlancategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var ivAdd = view.iv_add
        var tvDay = view.tv_day
        var tvDayCount = view.tv_day_count
        var showMainView = view.show_main_view
        val ivWorkout_pf = view.iv_workout
        val ivWorkout_no_image = view.iv_workout2
        val tvTime = view.tv_time_1
        val ivEditWork = view.iv_edit_work
        val ivDeleteWork = view.iv_delete_work
        val tvHeading = view.tv_heading
        val tvCategoury = view.tv_categoury
        val rrRestDayView = view.rr_rest_day_view
    }

    interface WeekListener {
        fun getDietCategoryDetailsInfo(data: WorkOutListModal.Data, position: Int, whichClick: String)
    }
}



