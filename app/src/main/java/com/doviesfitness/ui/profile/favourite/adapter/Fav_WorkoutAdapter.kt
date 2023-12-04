package com.doviesfitness.ui.profile.favourite.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.WorkoutCollectionResponse
import com.doviesfitness.ui.profile.favourite.modal.FavWorkoutModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.featured_view.view.*
import kotlinx.android.synthetic.main.item_workout_collection.view.*
import kotlinx.android.synthetic.main.item_workout_collection.view.exercise_img
import kotlinx.android.synthetic.main.item_workout_collection.view.exercise_name
import kotlinx.android.synthetic.main.item_workout_collection.view.item_view
import kotlinx.android.synthetic.main.item_workout_collection.view.level_name
import kotlinx.android.synthetic.main.select_workout_collection.view.*

class Fav_WorkoutAdapter(
    context: Context,
    workoutList: ArrayList<FavWorkoutModel.Data>,
    listener: OnFavItemClick
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<Fav_WorkoutAdapter.MyViewHolder>() {

    var mContext: Context
    var workoutList: ArrayList<FavWorkoutModel.Data>
    var listener: OnFavItemClick

    init {
        this.mContext = context
        this.workoutList = workoutList;
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        //return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_workout_collection, parent, false))
        return MyViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.workout_new_xml, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

    /*    val params =
            holder.itemView.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(mContext as Activity)
        params.height = dpWidth / 2 - mContext.getResources().getDimension(R.dimen._25sdp).toInt()
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.itemView.setLayoutParams(params)*/


        var width = Utility.getScreenWidthDivideIntoTwo(mContext)
        holder.itemView.layoutParams.width =( width/1.11 ).toInt()
        holder.itemView.layoutParams.height = (width * 1.22).toInt()//3

        val workOutModal = workoutList.get(pos)

        if (workoutList.size > 0) {
            holder.tvFeatured.setText(workOutModal.workout_name)
            holder.levelName.setText("" + workOutModal.workout_level)
            /* if (workOutModal.workout_exercise_count.toInt() == 0)
                  holder.levelName.setText("Comming soon")
              else if (workOutModal.workout_exercise_count.toInt() == 1)
                  holder.levelName.setText(workOutModal.workout_exercise_count + " Workout")
              else
                  holder.levelName.setText(workOutModal.workout_exercise_count + " Workouts")
  */

            Glide.with(holder.ivFeatured).load(workOutModal.workout_image)
                .into(holder.ivFeatured)


            holder.RitemView.setOnClickListener() {
                listener.onFavItemClick(workoutList.get(pos), pos)
            }
        }

        //btnLock.isHidden = (model.workoutAccessLevel.uppercased() == "OPEN".uppercased() || model.workoutAccessLevel.isEmpty)


        if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                .equals("yes", true) ||
            !getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                .equals("0")
        ) {
            holder.lockImg.visibility = View.GONE

        } else {
            if (workOutModal.access_level.equals(
                    "OPEN",
                    true
                ) || workOutModal.access_level.equals("")
            ) {
                holder.lockImg.visibility = View.GONE
            } else {
                holder.lockImg.visibility = View.VISIBLE
            }

        }




    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivFeatured = view.exercise_img
        val tvFeatured = view.exercise_name
        val levelName = view.level_name
        val tvNew = view.tv_new
        val RitemView = view.item_view
        val lockImg = view.lock_img
    }

    interface OnFavItemClick {
        public fun onFavItemClick(data: FavWorkoutModel.Data, pos: Int);
    }
}
