package com.doviesfitness.ui.profile.myWorkOut.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal

import kotlinx.android.synthetic.main.workout_listview.view.*

class NewWorkoutListAdapter(context: Context, workoutList: ArrayList<WorkOutListModal.Data>, myWorkOutDeletAndRedirectListener: MyWorkOutDeletAndRedirectListener, isAdmin: String) :androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>()  {
    private var context: Context
    var workoutList: ArrayList<WorkOutListModal.Data>
    var myWorkOutDeletAndRedirectListener: MyWorkOutDeletAndRedirectListener
    var isAdmin : String

    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2

    init {
        this.context = context
        this.workoutList = workoutList
        this.myWorkOutDeletAndRedirectListener = myWorkOutDeletAndRedirectListener
        this.isAdmin = isAdmin
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == workoutList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return ViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.workout_listview,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return FooterLoader(
                    LayoutInflater.from(context).inflate(
                        R.layout.new_pagination_view,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }


    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        if (rvHolder is FooterLoader) {
            val loaderViewHolder = rvHolder as FooterLoader
            if (showLoader) {
                loaderViewHolder.mProgressBar.visibility = View.VISIBLE
            } else {
                loaderViewHolder.mProgressBar.visibility = View.GONE
            }
            return
        }
        val holder = rvHolder as ViewHolder

        val workoutModal = workoutList.get(pos);
        holder.tvHeading.setText(workoutModal.workout_name)
        holder.tvCategoury.setText(workoutModal.workout_category)
        holder.tvTime.setText(workoutModal.workout_time)

        if (isAdmin.equals("Yes")) {
            holder.swipe.setSwipeEnabled(false)
            holder.publishIcon.visibility = View.VISIBLE

        } else {
            holder.swipe.setSwipeEnabled(true)
            holder.publishIcon.visibility = View.GONE
            if (!workoutModal.workout_access_level.equals("OPEN")) {
                holder.ivLock.visibility = View.VISIBLE
                holder.ivRedirect.visibility = View.GONE
                holder.publishIcon.visibility = View.GONE
            } else {
                holder.ivLock.visibility = View.GONE
                holder.ivRedirect.visibility = View.VISIBLE
            }
        }



        if (!workoutModal.workout_image.isEmpty()) {
            Glide.with(holder.ivWorkout_pf).load(workoutModal.workout_image)
                .into(holder.ivWorkout_pf)
        }

        holder.publishIcon.setOnClickListener {
            myWorkOutDeletAndRedirectListener.publishWorkout(pos, workoutModal)
        }

        holder.McdeletePost.setOnClickListener{
            myWorkOutDeletAndRedirectListener.getWorkOutData(workoutModal, "0", pos)
        }

        holder.rltvContainer.setOnClickListener {
            myWorkOutDeletAndRedirectListener.getWorkOutData(workoutModal, "1", pos)
        }
    }

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val ivWorkout_pf = view.iv_workout
        val tvTime = view.tv_time
        val tvHeading = view.tv_heading
        val tvCategoury = view.tv_categoury
        val ivRedirect = view.iv_redirect
        val McdeletePost = view.Mcdelete_post
        val rltvContainer = view.rltv_container
        val ivLock = view.iv_lock
        val publishIcon=view.publish_icon
        val swipe=view.swipe
    }

    interface MyWorkOutDeletAndRedirectListener {
        fun getWorkOutData(data: WorkOutListModal.Data, whichClick : String, pos : Int)
        fun publishWorkout(pos : Int, workoutModal : WorkOutListModal.Data)
    }
}
