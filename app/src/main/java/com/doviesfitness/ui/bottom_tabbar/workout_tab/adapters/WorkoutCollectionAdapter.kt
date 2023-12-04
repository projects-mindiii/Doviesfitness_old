package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.featured_view.view.*
import kotlinx.android.synthetic.main.item_workout_collection.view.*

class WorkoutCollectionAdapter(context: Context, workoutList: ArrayList<WorkoutCollectionResponse.Data>,listener:OnItemClick) :
    androidx.recyclerview.widget.RecyclerView.Adapter<WorkoutCollectionAdapter.MyViewHolder>() {

    var mContext: Context
    lateinit var workoutList: ArrayList<WorkoutCollectionResponse.Data>
    lateinit var listener:OnItemClick

    init {
        this.mContext = context
        this.workoutList=workoutList;
        this.listener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_workout_collection, parent, false))
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        if (workoutList.size > 0) {

            if (pos==1){
                if (workoutList.get(pos).isTrainerListIsEmpty == false){
                    holder.txt_all_workout_collection2.visibility=View.VISIBLE
                }else{
                    holder.txt_all_workout_collection2.visibility=View.GONE
                }
            }else
                holder.txt_all_workout_collection2.visibility=View.GONE

            holder.RitemView.layoutParams.width= Utility.getScreenWidthDivideIntoTwo(mContext)
            holder.RitemView.layoutParams.height = (Utility.getScreenWidthDivideIntoTwo(mContext)*1.28).toInt();

           /* if (pos/2==0){
                val layoutParams = RelativeLayout.LayoutParams(
                    (Utility.getScreenWidthDivideIntoTwo(context)/1.1).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()
                )
                layoutParams.setMargins(20, 15, 0, 20)
                holder.rltvContainer.layoutParams = layoutParams
            }else{
                val layoutParams = RelativeLayout.LayoutParams(
                    (Utility.getScreenWidthDivideIntoTwo(context)/1.1).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()
                )
                layoutParams.setMargins(20, 15, 0, 20)
                holder.rltvContainer.layoutParams = layoutParams
            }*/


            holder.tvFeatured.text = workoutList.get(pos).workout_group_name
            if (workoutList[pos].group_workout_count.toInt() == 0)
                holder.levelName.text = workoutList.get(pos).workout_group_level + " - " + "Coming soon"
            else if (workoutList[pos].group_workout_count.toInt() == 1)
                holder.levelName.text = workoutList.get(pos).workout_group_level + " - " + workoutList.get(pos).group_workout_count + " Workout"
            else
                holder.levelName.text = workoutList.get(pos).workout_group_level + " - " + workoutList.get(pos).group_workout_count + " Workouts"
            Glide.with(holder.ivFeatured).load(workoutList.get(pos).workout_group_image)
                .into(holder.ivFeatured)
            holder.RitemView.setOnClickListener() {
                if (workoutList.size>0)
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
        val  txt_all_workout_collection2= view.txt_all_workout_collection2

    }

    interface OnItemClick{
        public fun onItemClick(data:WorkoutCollectionResponse.Data,pos:Int);
    }

}
