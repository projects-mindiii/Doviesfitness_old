package com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.MyDietPlan
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.my_diet_plan.view.*


class MyDietPlanAdapterExample(
    context: Context,
    dietPSubCategoryList: ArrayList<MyDietPlan.Data>,
    myDietPlanListener: MyDietPlanLIstener
) : androidx.recyclerview.widget.RecyclerView.Adapter<MyDietPlanAdapterExample.MyViewHolder>() {
    private var context: Context
    var dietPSubCategoryList: ArrayList<MyDietPlan.Data>
    var myDietPlanListener: MyDietPlanLIstener

    init {
        this.context = context
        this.dietPSubCategoryList = dietPSubCategoryList
        this.myDietPlanListener = myDietPlanListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        /*val params = holder.rltvContainer.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.height = dpWidth / 2 - context.getResources().getDimension(R.dimen._25sdp).toInt()
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.rltvContainer.setLayoutParams(params)*/

        //TODO devendra



        holder.rltvContainer.layoutParams.width= (Utility.getScreenWidthDivideIntoTwo(context)/1.1).toInt()
        holder.rltvContainer.layoutParams.height = (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()

        if (pos/2==0){
            val layoutParams = RelativeLayout.LayoutParams(
                (Utility.getScreenWidthDivideIntoTwo(context)/1.1).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()
            )
            layoutParams.setMargins(20, 15, 0, 18)
            holder.rltvContainer.layoutParams = layoutParams
        }else{
            val layoutParams = RelativeLayout.LayoutParams(
                (Utility.getScreenWidthDivideIntoTwo(context)/1.1).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()
            )
            layoutParams.setMargins(20, 15, 0, 18)
            holder.rltvContainer.layoutParams = layoutParams
        }

        holder.settingImg.visibility = View.VISIBLE

        val dietPlanSubcategoryModal = dietPSubCategoryList.get(pos);
        holder.dpSubcatName.setText(dietPlanSubcategoryModal.diet_plan_title)

        if (!dietPlanSubcategoryModal.diet_plan_image.isEmpty()) {
            Glide.with(holder.dpSubImg).load(dietPlanSubcategoryModal.diet_plan_image)
                .into(holder.dpSubImg)
        }

        // show lock icon when user { "diet_plan_access_level" = LOCK; }
        if (dietPlanSubcategoryModal.diet_plan_access_level.equals("LOCK")) {
            holder.lockImg.visibility = View.VISIBLE
        } else {
            holder.lockImg.visibility = View.GONE
        }

        holder.rltvContainer.setOnClickListener {
            myDietPlanListener.getDietPlanInfo(dietPlanSubcategoryModal, pos, "0")
        }

        holder.settingImg.setOnClickListener {
            myDietPlanListener.getDietPlanInfo(dietPlanSubcategoryModal, pos, "1")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false))
    }

    override fun getItemCount(): Int {
        return dietPSubCategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val dpSubImg = view.dp_sub_img
        val dpSubcatName = view.dp_subcat_name
        val lockImg = view.lock_img
        val settingImg = view.setting_img
        val rltvContainer = view.rltv_container
    }

    interface MyDietPlanLIstener {
        fun getDietPlanInfo(data: MyDietPlan.Data, position: Int, whichClick: String)
    }
}