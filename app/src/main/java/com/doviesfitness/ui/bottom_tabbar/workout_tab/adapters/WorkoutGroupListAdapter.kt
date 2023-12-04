package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutGroupListResponse
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.workout_grouplist_item_view.view.group_rv
import kotlinx.android.synthetic.main.workout_grouplist_item_view.view.level_name
import kotlinx.android.synthetic.main.workout_grouplist_item_view.view.view_all

class WorkoutGroupListAdapter(
    context: Context,
    workoutGroupList: ArrayList<WorkoutGroupListResponse.Data>,
    listener: GroupItemAdapter.OnWorkoutClick
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<WorkoutGroupListAdapter.MyViewHolder>() {
    private var context: Context
    var workoutGroupList: ArrayList<WorkoutGroupListResponse.Data>
    var listener: GroupItemAdapter.OnWorkoutClick

    init {
        this.context = context
        this.workoutGroupList = workoutGroupList
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        if (workoutGroupList.get(pos).workout_count.toInt() == 0)
            holder.levelName.text = workoutGroupList.get(pos).level_name + " - " + "Coming soon"
        else if (workoutGroupList.get(pos).workout_count.toInt() == 1) {
            if (workoutGroupList.get(pos).level_name == "All")
                holder.levelName.text =
                    workoutGroupList.get(pos).level_name + " Level" + " - " + workoutGroupList.get(
                        pos
                    ).workout_count + " Workout"
            else
                holder.levelName.text =
                    workoutGroupList.get(pos).level_name + " - " + workoutGroupList.get(
                        pos
                    ).workout_count + " Workout"
        } else {
            if (workoutGroupList.get(pos).level_name == "All")
                holder.levelName.text =
                    workoutGroupList.get(pos).level_name + " Levels" + " - " + workoutGroupList.get(
                        pos
                    ).workout_count + " Workouts"
            else
                holder.levelName.text =
                    workoutGroupList.get(pos).level_name + " - " + workoutGroupList.get(pos).workout_count + " Workouts"
        }

        var layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3);
        holder.groupRV.layoutManager = layoutManager
        val GroupList = ArrayList<WorkoutGroupListResponse.Data.Workout>()
        if (workoutGroupList.get(pos).workout_list != null && workoutGroupList.get(pos).workout_list.size > 0) {
            for (i in 0..(workoutGroupList.get(pos).workout_list.size - 1)) {
                if (i < 3) {
                    GroupList.add(workoutGroupList.get(pos).workout_list.get(i))
                } else {
                    break
                }
            }
            if (workoutGroupList.get(pos).workout_list.size > 3) {
                holder.viewAll.text = "View All"
            } else {
                holder.viewAll.text = ""
            }
        } else {

        }
        holder.groupRV.adapter = GroupItemAdapter(context, GroupList, listener)
        workoutGroupList.get(pos).viewAll = true

        holder.viewAll.setOnClickListener() {
            if (workoutGroupList.get(pos).viewAll) {
                holder.viewAll.text = "Hide All"

                var layoutManager= androidx.recyclerview.widget.GridLayoutManager(context, 3);
              // var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


                holder.groupRV.layoutManager = layoutManager
                holder.groupRV.adapter =
                    GroupItemAdapter(context, workoutGroupList.get(pos).workout_list, listener)
                workoutGroupList[pos].viewAll = false
            } else {
                holder.viewAll.text = "View All"
                var layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3);
                holder.groupRV.layoutManager = layoutManager
                val GroupList = ArrayList<WorkoutGroupListResponse.Data.Workout>()
                if (workoutGroupList.get(pos).workout_list != null && workoutGroupList.get(pos).workout_list.size > 0) {
                    for (i in 0..workoutGroupList.get(pos).workout_list.size) {
                        if (i < 3) {
                            GroupList.add(workoutGroupList.get(pos).workout_list.get(i))
                        } else {
                            break
                        }
                    }

                } else {

                }
                holder.groupRV.adapter = GroupItemAdapter(context, GroupList, listener)
                workoutGroupList.get(pos).viewAll = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.workout_grouplist_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return workoutGroupList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val levelName = view.level_name
        val viewAll = view.view_all
        val groupRV = view.group_rv
    }
}



