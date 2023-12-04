package com.doviesfitness.ui.profile.favourite.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.MyDietPlan
import com.doviesfitness.ui.profile.favourite.modal.FavWorkoutModel
import com.doviesfitness.ui.profile.favourite.modal.FavWorkoutPlanModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.fav_workout_plan_view.view.*

class MyFavWorkPlanAdapter(
    context: Context,
    workoutPlanList: ArrayList<FavWorkoutPlanModel.Data>,
    myWorkOutPlanListener: MyWorkOutPlanListener
) : RecyclerView.Adapter<MyFavWorkPlanAdapter.MyViewHolder>() {
    private var context: Context
    var workoutPlanList: ArrayList<FavWorkoutPlanModel.Data>
    var myWorkOutPlanListener: MyWorkOutPlanListener

    init {
        this.context = context
        this.workoutPlanList = workoutPlanList
        this.myWorkOutPlanListener = myWorkOutPlanListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
      /*  val params = holder.rltvContainer.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.height = dpWidth / 2
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.rltvContainer.setLayoutParams(params)*/
        holder.rltvContainer.layoutParams.width= Utility.getScreenWidthDivideIntoTwo(context)
        holder.rltvContainer.layoutParams.height = (Utility.getScreenWidthDivideIntoTwo(context)*1.29).toInt();

        if (pos/2==0){
            val layoutParams = RelativeLayout.LayoutParams(
                (Utility.getScreenWidthDivideIntoTwo(context)).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.29).toInt()
            )
            layoutParams.setMargins( 0, 0,  0, -14)

            holder.rltvContainer.layoutParams = layoutParams

        }else{
            val layoutParams = RelativeLayout.LayoutParams(
                (Utility.getScreenWidthDivideIntoTwo(context)).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.29).toInt()
            )
            layoutParams.setMargins( 0, 0,  0, -14)

            holder.rltvContainer.layoutParams = layoutParams

        }


        val workoutPlanModal = workoutPlanList[pos];
       // holder.dpSubcatName.setText(workoutPlanModal.program_name)

        if (!workoutPlanModal.program_image.isEmpty()) {
            Glide.with(holder.dpSubImg).load(workoutPlanModal.program_image)
                .into(holder.dpSubImg)
        }

        holder.rltvContainer.setOnClickListener {
            myWorkOutPlanListener.getFavWorkPlanInfo(workoutPlanModal, pos, "0")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false))
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.fav_workout_plan_view, parent, false))
    }

    override fun getItemCount(): Int {
        return workoutPlanList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val dpSubImg = view.dp_sub_img
        val dpSubcatName = view.dp_subcat_name
        val rltvContainer = view.rltv_container
    }

    interface MyWorkOutPlanListener {
        fun getFavWorkPlanInfo(data: FavWorkoutPlanModel.Data, position: Int, whichClick: String)
    }
}