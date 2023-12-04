package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import kotlinx.android.synthetic.main.stream_horizontal_view.view.*
//New Releases adapter
class RecentWorkoutAdapter(
    context: Context,
    workoutList: List<StreamDataModel.Settings.Data.RecentWorkout>, listener: popularStreamWorkoutAdapter.OnItemCLick
    ) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RecentWorkoutAdapter.MyViewHolder>() {

    var mContext: Context
    var listener: popularStreamWorkoutAdapter.OnItemCLick
    lateinit var workoutList: ArrayList<StreamDataModel.Settings.Data.RecentWorkout>

    init {
        this.mContext = context
        this.workoutList = workoutList as ArrayList<StreamDataModel.Settings.Data.RecentWorkout>
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.stream_horizontal_view_1,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.tvFeatured.setText(workoutList.get(pos).stream_workout_name)
        holder.tvLevel.visibility = View.VISIBLE
        holder.tvLevel.text=workoutList.get(pos).stream_workout_subtitle



//        Glide.with(holder.ivFeatured).load(workoutList.get(pos).stream_workout_image_url+"medium/"+workoutList.get(pos).stream_workout_image)
        Glide.with(holder.ivFeatured).load(workoutList.get(pos).stream_workout_image_url+workoutList.get(pos).stream_workout_image)
            .into(holder.ivFeatured)
/*
        if (Doviesfitness.getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                "Yes"
            )
            ||"OPEN".equals(workoutList.get(pos).stream_workout_access_level, true) ) {
            holder.lockIcon.visibility = View.GONE
        }
        else {
            holder.lockIcon.visibility = View.VISIBLE

        }
*/
        if((workoutList.get(pos).stream_workout_name.isEmpty())&&(workoutList.get(pos).stream_workout_subtitle.isEmpty()))
        {


//            stream_workout_gradient_bg
        }else{
            holder.rlGradient.setBackgroundResource(R.drawable.stream_workout_gradient_bg)
        }

        if (workoutList.get(pos).display_new_tag.equals("YES",true)){
            holder.tvNewStream.visibility=View.VISIBLE
            holder.tvNewStream.text=""+workoutList.get(pos).display_new_tag_text
        }
        else{
            holder.tvNewStream.visibility=View.GONE

        }
        holder.itemView.setOnClickListener {
            if (workoutList.size > 0) {
               if (pos<workoutList.size)
               listener.onItemCLick(pos,"recent",workoutList.get(pos).stream_workout_id)
            }

        }

    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivFeatured = view.iv_featured
        val tvFeatured = view.tv_featured
        val tvLevel = view.tv_level
        val lockIcon = view.lock_icon
        val tvNew = view.tv_new
        val tvNewStream = view.tv_new_stream
        val rlGradient = view.rl_gradient
        //   val  itemLayout = view.item_layout
    }
}
