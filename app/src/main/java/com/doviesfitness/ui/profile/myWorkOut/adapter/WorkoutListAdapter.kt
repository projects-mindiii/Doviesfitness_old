package com.doviesfitness.ui.profile.myWorkOut.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import kotlinx.android.synthetic.main.workout_listview.view.*

class WorkoutListAdapter(context: Context, workoutList: ArrayList<WorkOutListModal.Data>,
                         myWorkOutDeletAndRedirectListener: MyWorkOutDeletAndRedirectListener) : androidx.recyclerview.widget.RecyclerView.Adapter<WorkoutListAdapter.MyViewHolder>() {
    private var context: Context
    var workoutList: ArrayList<WorkOutListModal.Data>
    var myWorkOutDeletAndRedirectListener : MyWorkOutDeletAndRedirectListener
    var isAdmin = "No"
    init {
        this.context = context
        this.workoutList = workoutList
        this.myWorkOutDeletAndRedirectListener = myWorkOutDeletAndRedirectListener
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val workoutModal = workoutList.get(pos);
        holder.tvHeading.setText(workoutModal.workout_name)
        holder.tvCategoury.setText(workoutModal.workout_category)
        holder.tvTime.setText(workoutModal.workout_time)
        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        if (isAdmin.equals("Yes", true)) {
            holder.publishIcon.visibility = View.VISIBLE

        } else {
            holder.publishIcon.visibility = View.GONE
            if(!workoutModal.workout_access_level.equals("OPEN")){
                holder.ivLock.visibility = View.VISIBLE
                holder.ivRedirect.visibility = View.GONE
            }else{
                holder.ivLock.visibility = View.GONE
                holder.ivRedirect.visibility = View.VISIBLE
            }
        }

        if (!workoutModal.workout_image.isEmpty()) {
            Glide.with(holder.ivWorkout_pf).load(workoutModal.workout_image)
                .into(holder.ivWorkout_pf)
        }

        holder.publishIcon.setOnClickListener{
            myWorkOutDeletAndRedirectListener.publishWorkout(pos)
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

   inner class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){

        val ivWorkout_pf = view.iv_workout
        val tvTime = view.tv_time
        val tvHeading = view.tv_heading
        val tvCategoury = view.tv_categoury
        val ivRedirect = view.iv_redirect
     //   val McdeletePost = view.Mcdelete_post
        val rltvContainer = view.rltv_container
        val ivLock = view.iv_lock
        val publishIcon=view.publish_icon

    }

    interface MyWorkOutDeletAndRedirectListener {
        fun getWorkOutData(data: WorkOutListModal.Data, whichClick : String, pos : Int)
        fun publishWorkout( pos : Int)
    }
}