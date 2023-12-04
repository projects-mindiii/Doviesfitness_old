package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import kotlinx.android.synthetic.main.workout_listview.view.*

class MyWorkOutAdapters(context: Context, workoutList: ArrayList<WorkOutListModal.Data>,
                        myWorkOutDeletAndRedirectListener: MyWorkOutDeletAndRedirectListener) : androidx.recyclerview.widget.RecyclerView.Adapter<MyWorkOutAdapters.MyViewHolder>() {
    private var context: Context
    var workoutList: ArrayList<WorkOutListModal.Data>
    var myWorkOutDeletAndRedirectListener : MyWorkOutDeletAndRedirectListener

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


        if(!workoutModal.workout_access_level.equals("OPEN")){
            holder.ivRedirect.setImageResource(R.drawable.lock_ico_bg);
        }

        if (!workoutModal.workout_image.isEmpty()) {
            Glide.with(holder.ivWorkout_pf).load(workoutModal.workout_image).placeholder(R.drawable.app_icon)
                .into(holder.ivWorkout_pf)
        }

/*
        holder.McdeletePost.setOnClickListener{
            myWorkOutDeletAndRedirectListener.getWorkOutData(workoutModal, "0", pos)
        }
*/

        holder.rltvContainer.setOnClickListener {
            myWorkOutDeletAndRedirectListener.getWorkOutData(workoutModal, "1", pos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.workout_listview, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){

        val ivWorkout_pf = view.iv_workout
        val tvTime = view.tv_time
        val tvHeading = view.tv_heading
        val tvCategoury = view.tv_categoury
        val ivRedirect = view.iv_redirect
      //  val McdeletePost = view.Mcdelete_post
        val rltvContainer = view.rltv_container
    }

    interface MyWorkOutDeletAndRedirectListener {
        fun getWorkOutData(data: WorkOutListModal.Data, whichClick : String, pos : Int)
    }
}