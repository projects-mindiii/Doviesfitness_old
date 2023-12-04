package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import kotlinx.android.synthetic.main.workout_listview.view.iv_workout
import kotlinx.android.synthetic.main.workout_listview.view.rltv_container
import kotlinx.android.synthetic.main.workout_listview.view.tv_categoury
import kotlinx.android.synthetic.main.workout_listview.view.tv_heading
import kotlinx.android.synthetic.main.workout_listview.view.tv_time
import kotlinx.android.synthetic.main.workout_select_listview.view.*

class SelectWorkoutListAdapter(
    context: Context, workoutList: ArrayList<WorkOutListModal.Data>,
    myWorkOutDeletAndRedirectListener: MyWorkOutDeletAndRedirectListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<SelectWorkoutListAdapter.MyViewHolder>() {
    private var context: Context
    var workoutList: ArrayList<WorkOutListModal.Data>
    var myWorkOutDeletAndRedirectListener: MyWorkOutDeletAndRedirectListener

    init {
        this.context = context
        this.workoutList = workoutList
        this.myWorkOutDeletAndRedirectListener = myWorkOutDeletAndRedirectListener

    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val workoutModal = workoutList.get(pos);
        holder.tvHeading.setText(workoutModal.workout_name)
        holder.tvCategoury.setText(workoutModal.workout_category)
        holder.tvTime.setText(workoutModal.workout_time)

        /* if(!workoutModal.workout_access_level.equals("OPEN")){
             holder.ivRedirect.setImageResource(R.drawable.lock_ico_bg);
         }
        */

        if ("Yes".equals(workoutModal.isSelected)) {
            holder.ivSelectPlan.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_check_ico))
        } else {
            holder.ivSelectPlan.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_gray_circle))
        }

        if (!workoutModal.workout_image.isEmpty()) {
            Glide.with(holder.ivWorkout_pf).load(workoutModal.workout_image)
                .into(holder.ivWorkout_pf)
        }

        holder.rltvContainer.setOnClickListener {
            for (i in workoutList.indices) {
                if (i!=pos)
                workoutList.get(i).isSelected = "No"
                else{}
            }
            if("Yes".equals(workoutModal.isSelected)){
                workoutModal.isSelected = "No"
                holder.ivSelectPlan.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_gray_circle))
            }else{
                workoutModal.isSelected = "Yes"
                holder.ivSelectPlan.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_check_ico))
                myWorkOutDeletAndRedirectListener.getWorkOutData(workoutModal, "1", pos)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.workout_select_listview, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {


        val ivWorkout_pf = view.iv_workout
        val tvTime = view.tv_time
        val tvHeading = view.tv_heading
        val tvCategoury = view.tv_categoury
        val ivSelectPlan = view.iv_select_plan
        val rltvContainer = view.rltv_container
    }

    interface MyWorkOutDeletAndRedirectListener {
        fun getWorkOutData(data: WorkOutListModal.Data, whichClick: String, pos: Int)
    }
}