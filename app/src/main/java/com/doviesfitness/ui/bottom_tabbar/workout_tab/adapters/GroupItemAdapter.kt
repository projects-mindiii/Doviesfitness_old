package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutGroupListResponse
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.workout_group_item_view.view.*

class GroupItemAdapter(context: Context, workout_list: List<WorkoutGroupListResponse.Data.Workout>,listener:OnWorkoutClick):
    androidx.recyclerview.widget.RecyclerView.Adapter<GroupItemAdapter.MyViewHolder>(){
    private var context:Context
    private var workout_list: List<WorkoutGroupListResponse.Data.Workout>
      var listener:OnWorkoutClick
    init {
        this.context=context
        this.workout_list=workout_list
        this.listener=listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.exerciseName.text=workout_list.get(pos).workout_name
        holder.tvLevel.visibility=View.VISIBLE

        holder.itemLayout.layoutParams.width=
            Utility.getScreenWidthDivideIntoThree(context).toInt();
        holder.itemLayout.layoutParams.height=
            (Utility.getScreenWidthDivideIntoThree(context)*1.23).toInt();




        var WT:String=""
        if (workout_list.get(pos).workout_total_work_time.contains("minutes",true))
            WT =workout_list.get(pos).workout_total_work_time.replace("minutes","Min - ",true)
        else if (workout_list.get(pos).workout_total_work_time.contains("seconds",true))
            WT =workout_list.get(pos).workout_total_work_time.replace("seconds","Sec - ",true)
        else WT=workout_list.get(pos).workout_total_work_time+" "
        WT=workout_list.get(pos).workout_total_work_time
        holder.tvLevel.text=WT+" "+workout_list.get(pos).workout_level

      //  holder.tvLevel.text=workout_list.get(pos).workout_total_work_time+" "+workout_list.get(pos).workout_level
        if (workout_list.get(pos).workout_image!=null)
        Glide.with(context)
            .load(workout_list.get(pos).workout_image)
            .error(ContextCompat.getDrawable(context,R.drawable.app_icon))
            .into(holder.img)

        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("Yes")){
            holder.lockIcon.visibility = View.GONE
        }
        else {
            if (workout_list.get(pos).workout_access_level.equals("OPEN"))
                holder.lockIcon.visibility = View.GONE
            else
                holder.lockIcon.visibility = View.VISIBLE
        }

        if (workout_list.get(pos).is_new.equals("1")){
            holder.tvNew.visibility = View.VISIBLE
        }else{
            holder.tvNew.visibility = View.GONE
        }

        holder.itemLayout.setOnClickListener(){
            listener.onWorkoutClick(workout_list.get(pos).workout_id,pos)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.workout_group_item_view,parent,false))

    }
    override fun getItemCount(): Int {
        return workout_list.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  img = view.exercise_img
        val  exerciseName = view.exercise_name
        val  itemLayout = view.item_layout
        val  tvLevel = view.tv_level
        val  lockIcon = view.lock_icon
        val  tvNew = view.tv_new
    }

    interface OnWorkoutClick{
        public fun onWorkoutClick(workoutID:String,pos:Int)
    }
}



