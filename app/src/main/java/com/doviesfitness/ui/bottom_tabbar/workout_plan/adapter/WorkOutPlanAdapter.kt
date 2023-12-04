package com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanModal
import kotlinx.android.synthetic.main.diet_plan_category_view.view.*
import kotlinx.android.synthetic.main.diet_plan_category_view.view.dietPlan_img
import kotlinx.android.synthetic.main.diet_plan_category_view.view.diet_container
import kotlinx.android.synthetic.main.diet_plan_category_view.view.diet_plan_name
import kotlinx.android.synthetic.main.workout_plan_view.view.*

class WorkOutPlanAdapter(context: Context, workOutPlanList: ArrayList<WorkOutPlanModal.Data.GetAllProgram>, workOutPlanListener: WorkOutPlanistener, isAdmin : String) :

    androidx.recyclerview.widget.RecyclerView.Adapter<WorkOutPlanAdapter.MyViewHolder>() {
    private var context: Context
    var workOutPlanList: ArrayList<WorkOutPlanModal.Data.GetAllProgram>
    var workOutPlanListener: WorkOutPlanistener
    var isAdmin: String

    init {
        this.context = context
        this.workOutPlanList = workOutPlanList
        this.workOutPlanListener = workOutPlanListener
        this.isAdmin = isAdmin
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val workOutPlanModal = workOutPlanList.get(pos);
        holder.dietPlanName.setText(workOutPlanModal.program_name)

        if (!workOutPlanModal.program_image.isEmpty()) {
            Glide.with(holder.dietPlanImage).load(workOutPlanModal.program_image)
                .into(holder.dietPlanImage)
        }

        holder.diet_container.setOnClickListener {
            workOutPlanListener.getWorkoutPlanInfo(workOutPlanModal, pos)
        }



        if("Yes".equals(isAdmin)){
            if (workOutPlanModal.program_access_level.equals("LOCK")) {
                holder.lockImg.visibility = View.VISIBLE
            } else {
                holder.lockImg.visibility = View.GONE
            }
        }else{
            //when user is free and no admin
            if (workOutPlanModal.program_access_level.equals("LOCK")) {
                holder.lockImg.visibility = View.VISIBLE
            } else {
                holder.lockImg.visibility = View.GONE
            }
        }

        if(workOutPlanModal.program_week_count.equals("0")){
            holder.txtWeekCount.text = "Coming soon"
            holder.txtWeekCount.visibility = View.VISIBLE
        }else if(workOutPlanModal.program_week_count.equals("1")){
            holder.txtWeekCount.text = workOutPlanModal.program_week_count+ " Week plan"
            holder.txtWeekCount.visibility = View.VISIBLE
        }else if(workOutPlanModal.program_week_count.equals("")){
            holder.txtWeekCount.visibility = View.GONE
        }else{
            holder.txtWeekCount.text = workOutPlanModal.program_week_count+ " Week plan"
            holder.txtWeekCount.visibility = View.VISIBLE
        }



       /* "program_id":"809",
        "program_name":"Mindii repeat exercise plan",
        "program_image":"https:\/\/s3.us-east-2.amazonaws.com\/dovies-fitness-dev\/program_images\/oR32JiLugybZDmAn.png",
        "program_share_url":"https:\/\/dev.doviesfitness.com\/dl\/825adb76bc2912cff505cb4921facb72",
        "program_access_level":"LOCK",
        "new_tag":"Yes",
        "program_week_count":"4"*/


    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_plan_view, parent, false))
    }

    override fun getItemCount(): Int {
        return workOutPlanList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val dietPlanName = view.diet_plan_name
        val dietPlanImage = view.dietPlan_img
        var diet_container = view.diet_container
        var txtWeekCount = view.txt_week_count
        var lockImg = view.lock_img
    }

    interface WorkOutPlanistener {
        fun getWorkoutPlanInfo(data: WorkOutPlanModal.Data.GetAllProgram, position: Int)
    }
}



