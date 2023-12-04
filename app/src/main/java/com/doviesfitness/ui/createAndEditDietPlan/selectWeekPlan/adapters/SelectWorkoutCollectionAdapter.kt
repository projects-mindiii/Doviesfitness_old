package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import kotlinx.android.synthetic.main.featured_view.view.*
import kotlinx.android.synthetic.main.item_workout_collection.view.*

class SelectWorkoutCollectionAdapter(context: Context, workoutList: ArrayList<WorkoutCollectionResponse.Data>, listener:OnItemClick) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SelectWorkoutCollectionAdapter.MyViewHolder>() {

    var mContext: Context
    var workoutList: ArrayList<WorkoutCollectionResponse.Data>
    var listener:OnItemClick

    init {
        this.mContext = context
        this.workoutList=workoutList;
        this.listener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_workout_collection, parent, false))
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

      /*  val params = holder.itemView.getLayoutParams() as GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(mContext as Activity)
        params.height = dpWidth / 2
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.itemView.setLayoutParams(params)*/

        if (workoutList.size > 0) {
            holder.tvFeatured.setText(workoutList.get(pos).workout_group_name)
            if (workoutList.get(pos).group_workout_count.toInt() == 0)
                holder.levelName.setText("Comming soon")
            else if (workoutList.get(pos).group_workout_count.toInt() == 1)
                holder.levelName.setText(workoutList.get(pos).group_workout_count + " Workout")
            else
                holder.levelName.setText(workoutList.get(pos).group_workout_count + " Workouts")
            Glide.with(holder.ivFeatured).load(workoutList.get(pos).workout_group_image)
                .into(holder.ivFeatured)
            holder.RitemView.setOnClickListener() {
                listener.onItemClick(workoutList.get(pos), pos)
            }
        }
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  ivFeatured = view.exercise_img
        val  tvFeatured = view.exercise_name
        val  levelName = view.level_name
        val  tvNew = view.tv_new
        val  RitemView= view.item_view
    }

    interface OnItemClick{
        public fun onItemClick(data: WorkoutCollectionResponse.Data, pos:Int);
    }

}
