package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import kotlinx.android.synthetic.main.stream_horizontal_view.view.*

class DynamicStreamWorkoutAdapter(
    context: Context,
    workoutList: ArrayList<StreamDataModel.Settings.Data.PopularCollectionWorkouts.Workout>,
    listener: OnDynamicWorkoutCLick,
    collectionPosition: Int
) :
    RecyclerView.Adapter<DynamicStreamWorkoutAdapter.MyViewHolder>() {

    var mContext: Context
    var listener: OnDynamicWorkoutCLick
    var workoutList: ArrayList<StreamDataModel.Settings.Data.PopularCollectionWorkouts.Workout>
    var collectionPosition:Int
    init {
        this.mContext = context
        this.workoutList = workoutList
        this.listener = listener
        this.collectionPosition = collectionPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.stream_horizontal_view,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int {
        return   workoutList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.tvFeatured.setText(workoutList.get(pos).stream_workout_name)
        holder.tvLevel.visibility = View.VISIBLE
        holder.tvLevel.text=workoutList.get(pos).stream_workout_subtitle

//        Glide.with(holder.ivFeatured).load(workoutList.get(pos).stream_workout_image_url+"medium/"+workoutList.get(pos).stream_workout_image)
        Glide.with(holder.ivFeatured).load(
            workoutList[pos].stream_workout_image_url + workoutList.get(
                pos
            ).stream_workout_image
        )
            .into(holder.ivFeatured)

        if((!workoutList.get(pos).stream_workout_name.isEmpty())||(!workoutList.get(pos).stream_workout_subtitle.isEmpty()))
        {
            holder.rlGradient.setBackgroundResource(R.drawable.stream_workout_gradient_bg)
        }


        if (workoutList.get(pos).display_new_tag.equals("YES", true)){
            holder.tvNew.visibility= View.VISIBLE
            holder.tvNew.text=""+workoutList.get(pos).display_new_tag_text
        }
        else{
            holder.tvNew.visibility= View.GONE

        }
        holder.itemView.setOnClickListener {
            if (workoutList.size > 0) {
                if (pos<workoutList.size)
                    listener.onDynamicWorkoutCLick(collectionPosition, pos)
            }

        }

    }
    interface OnDynamicWorkoutCLick{
        public fun onDynamicWorkoutCLick(listPosition: Int, itemPosition: Int)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFeatured = view.iv_featured
        val tvFeatured = view.tv_featured
        val tvLevel = view.tv_level
        val lockIcon = view.lock_icon
        val tvNew = view.tv_new_stream
        val rlGradient=view.rl_gradient
    }
}
