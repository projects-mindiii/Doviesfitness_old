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
import com.doviesfitness.R.color.*
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.trainers_item_view_new.view.*

class TrainersChoiceAdapter(
    context: Context,
    workoutList: List<WorkoutCollectionResponse.Data.Workout>,
    listener: GroupItemAdapter.OnWorkoutClick
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TrainersChoiceAdapter.MyViewHolder>() {

    var mContext: Context
    var listener: GroupItemAdapter.OnWorkoutClick
    lateinit var workoutList: ArrayList<WorkoutCollectionResponse.Data.Workout>

    init {
        this.mContext = context
        this.workoutList = workoutList as ArrayList<WorkoutCollectionResponse.Data.Workout>;
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.trainers_item_view_new,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.tvFeatured.setText(workoutList.get(pos).workout_name)
        holder.tvLevel.visibility = View.VISIBLE


        var WT: String = ""
        if (workoutList.get(pos).workout_total_work_time.contains("minutes", true))
            WT = workoutList.get(pos).workout_total_work_time.replace("minutes", "Min - ", true)
        else if (workoutList.get(pos).workout_total_work_time.contains("seconds", true))
            WT = workoutList.get(pos).workout_total_work_time.replace("seconds", "Sec - ", true)
        else WT = workoutList.get(pos).workout_total_work_time + " "
        WT = workoutList.get(pos).workout_total_work_time
        holder.tvLevel.text = WT + " - " + workoutList.get(pos).workout_level
        //   holder.tvLevel.text=workoutList.get(pos).workout_group_level
        //   holder.tvFeatured.setTextColor(ContextCompat.getColor(mContext,R.color.description_color))
        Glide.with(holder.ivFeatured).load(workoutList.get(pos).workout_image)
            .into(holder.ivFeatured)
        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
        ) {
            holder.lockIcon.visibility = View.GONE
        } else {
            if (workoutList.get(pos).workout_access_level.equals("OPEN"))
                holder.lockIcon.visibility = View.GONE
            else
                holder.lockIcon.visibility = View.VISIBLE
        }


      /*
       TODo Devendra

       val screenWidth = mContext.resources.displayMetrics.widthPixels
        val partWidth = screenWidth / 3.7
        holder.itemLayout.layoutParams.width= partWidth.toInt();
        holder.itemLayout.layoutParams.height = (partWidth*1.4).toInt();
*/


        if (workoutList.get(pos).is_new.equals("1")) {
            holder.tvNew.visibility = View.VISIBLE
        } else {
            holder.tvNew.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            if (workoutList.size > 0) {
                if (pos<workoutList.size)
                listener.onWorkoutClick(workoutList.get(pos).workout_id, pos)
            }

        }

    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivFeatured = view.iv_featured
        val tvFeatured = view.tv_featured
        val tvLevel = view.tv_level
        val lockIcon = view.lock_icon
        val tvNew = view.tv_new
           val  itemLayout = view.item_layout
    }
}
