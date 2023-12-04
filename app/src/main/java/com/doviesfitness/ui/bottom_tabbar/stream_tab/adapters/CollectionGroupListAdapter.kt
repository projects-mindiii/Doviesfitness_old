package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.CollectionDetailModel
import kotlinx.android.synthetic.main.workout_grouplist_item_view.view.*

class CollectionGroupListAdapter(context: Context, workoutGroupList :ArrayList<CollectionDetailModel.Settings.Data.Workout>, listener:popularStreamWorkoutAdapter.OnItemCLick):
    androidx.recyclerview.widget.RecyclerView.Adapter<CollectionGroupListAdapter.MyViewHolder>(){
    private var context:Context
    var workoutGroupList :ArrayList<CollectionDetailModel.Settings.Data.Workout>
    var  listener:popularStreamWorkoutAdapter.OnItemCLick

    init {
        this.context=context
        this.workoutGroupList=workoutGroupList
        this.listener=listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
/*
        if(workoutGroupList.get(pos).workout_count.toInt()==0)
        holder.levelName.text=workoutGroupList.get(pos).level_name+" - "+"Coming soon"
        else if(workoutGroupList.get(pos).workout_count.toInt()==1)
        holder.levelName.text=workoutGroupList.get(pos).level_name+" - "+workoutGroupList.get(pos).workout_count+" Workout"
        else
        holder.levelName.text=workoutGroupList.get(pos).level_name+" - "+workoutGroupList.get(pos).workout_count+" Workouts"
*/
        holder.viewAll.visibility=View.GONE
        var layoutManager= androidx.recyclerview.widget.GridLayoutManager(context, 2);
        holder.groupRV.layoutManager=layoutManager
        val GroupList=ArrayList<CollectionDetailModel.Settings.Data.Workout>()
        GroupList.addAll(workoutGroupList)
/*
        if(workoutGroupList.get(pos).workout_list!=null&&workoutGroupList.get(pos).workout_list.size>0)
        {
            for (i in 0..(workoutGroupList.get(pos).workout_list.size-1)){
                if (i<2){
                    GroupList.add(workoutGroupList.get(pos).workout_list.get(i))
                }else{
                    break
                }
            }
            if(workoutGroupList.get(pos).workout_list.size>2){
                holder.viewAll.text="View All"
            }
            else{
                holder.viewAll.text=""
            }
        }
        else{

        }
*/
        holder.groupRV.adapter=CollectionGroupItemAdapter(context,GroupList,listener)
       // workoutGroupList.get(pos).viewAll=true

/*
        holder.viewAll.setOnClickListener(){
            if(workoutGroupList.get(pos).viewAll){
                holder.viewAll.text="Hide All"
                var layoutManager= androidx.recyclerview.widget.GridLayoutManager(context, 2);
                holder.groupRV.layoutManager=layoutManager
                holder.groupRV.adapter=CollectionGroupItemAdapter(context,workoutGroupList.get(pos).workout_list,listener)
                workoutGroupList.get(pos).viewAll=false
            }

            else{
                holder.viewAll.text="View All"
                var layoutManager= androidx.recyclerview.widget.GridLayoutManager(context, 2);
                holder.groupRV.layoutManager=layoutManager
                val GroupList=ArrayList<WorkoutGroupListResponse.Data.Workout>()
                if(workoutGroupList.get(pos).workout_list!=null&&workoutGroupList.get(pos).workout_list.size>0)
                {
                    for (i in 0..workoutGroupList.get(pos).workout_list.size){
                           if (i<2){
                               GroupList.add(workoutGroupList.get(pos).workout_list.get(i))
                           }else{
                              break
                           }
                    }

                }
                else{

                }
                holder.groupRV.adapter=CollectionGroupItemAdapter(context,GroupList,listener)
                workoutGroupList.get(pos).viewAll=true
            }
        }
*/
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.workout_grouplist_item_view,parent,false))
    }
    override fun getItemCount(): Int {
        return 1
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  levelName = view.level_name
        val  viewAll = view.view_all
        val  groupRV = view.group_rv
    }
}



