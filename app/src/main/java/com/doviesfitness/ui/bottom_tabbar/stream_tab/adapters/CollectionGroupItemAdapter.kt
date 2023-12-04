package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.CollectionDetailModel
import kotlinx.android.synthetic.main.collection_group_item_new_view.view.*

class CollectionGroupItemAdapter(context: Context, workout_list: List<CollectionDetailModel.Settings.Data.Workout>, listener:popularStreamWorkoutAdapter.OnItemCLick):
    androidx.recyclerview.widget.RecyclerView.Adapter<CollectionGroupItemAdapter.MyViewHolder>(){
    private var context:Context
    private var workout_list: List<CollectionDetailModel.Settings.Data.Workout>
      var listener:popularStreamWorkoutAdapter.OnItemCLick
    init {
        this.context=context
        this.workout_list=workout_list
        this.listener=listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.exerciseName.text=workout_list.get(pos).stream_workout_name
        holder.tvLevel.visibility=View.VISIBLE
        holder.tvLevel.text=workout_list.get(pos).stream_workout_subtitle
        if((workout_list.get(pos).stream_workout_name.isEmpty())&&(workout_list.get(pos).stream_workout_subtitle.isEmpty()))
        {
        }else{
            holder.rlGradient.setBackgroundResource(R.drawable.stream_workout_gradient_bg)

        }

       /* var WT:String=""
        if (workout_list.get(pos).workout_total_work_time.contains("minutes",true))
            WT =workout_list.get(pos).workout_total_work_time.replace("minutes","Min - ",true)
        else if (workout_list.get(pos).workout_total_work_time.contains("seconds",true))
            WT =workout_list.get(pos).workout_total_work_time.replace("seconds","Sec - ",true)
        else WT=workout_list.get(pos).workout_total_work_time+" "
        WT=workout_list.get(pos).workout_total_work_time
        holder.tvLevel.text=WT+" "+workout_list.get(pos).workout_level*/

      //  holder.tvLevel.text=workout_list.get(pos).workout_total_work_time+" "+workout_list.get(pos).workout_level
        if (workout_list.get(pos).stream_workout_image!=null)
        Glide.with(context)
//            .load(workout_list.get(pos).stream_workout_image_url+"medium/"+workout_list.get(pos).stream_workout_image)
            .load(workout_list.get(pos).stream_workout_image_url+workout_list.get(pos).stream_workout_image)
            .error(ContextCompat.getDrawable(context,R.drawable.app_icon))
            .into(holder.img)



/*
        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
            ||"OPEN".equals(workout_list.get(pos).stream_workout_access_level, true) ) {
            holder.lockIcon.visibility = View.GONE
        }
        else {
            holder.lockIcon.visibility = View.VISIBLE

        }
*/


        if (workout_list.get(pos).display_new_tag.equals("YES",true)){
            holder.tvNewstream.visibility = View.VISIBLE
            holder.tvNewstream.text=""+workout_list.get(pos).display_new_tag_text
        }
        else{
            holder.tvNewstream.visibility = View.GONE
        }

        holder.itemLayout.setOnClickListener(){
            listener.onItemCLick(pos,"collection",workout_list.get(pos).stream_workout_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate( R.layout.collection_group_item_new_view,parent,false))

    }
    override fun getItemCount(): Int {
        return workout_list.size
    }
    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  img = view.iv_featured
        val  exerciseName = view.tv_featured
        val  itemLayout = view.item_layout
        val  tvLevel = view.tv_level
        val  lockIcon = view.lock_icon
        val  tvNewstream = view.tv_new_stream
        val  tvNew = view.tv_new
        val  rlGradient = view.rl_gradient
    }

}



