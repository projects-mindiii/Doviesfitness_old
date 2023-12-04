package com.doviesfitness.ui.showDietPlanDetail.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.MyDietPlan
import com.doviesfitness.utils.CommanUtils
import kotlinx.android.synthetic.main.show_diet_plan_view.view.*

class ShowAddDietPlanAdapter(context: Context, dietPSubCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>, myDietPlanListenner : MyDietPlanLIstener) : androidx.recyclerview.widget.RecyclerView.Adapter<ShowAddDietPlanAdapter.MyViewHolder>() {
    private var context: Context
    var dietPSubCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>
    var myDietPlanListenner: MyDietPlanLIstener

    init {
        this.context = context
        this.dietPSubCategoryList = dietPSubCategoryList
        this.myDietPlanListenner = myDietPlanListenner
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        val params =
            holder.rltvContainer.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.height = dpWidth / 2 - context.getResources().getDimension(R.dimen._25sdp).toInt()
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.rltvContainer.setLayoutParams(params)


        val dietPlanSubcategoryModal = dietPSubCategoryList.get(pos);
        holder.dpSubcatName.setText(dietPlanSubcategoryModal.diet_plan_title)

        if (!dietPlanSubcategoryModal.diet_plan_image.isEmpty()) {
            Glide.with(holder.dpSubImg).load(dietPlanSubcategoryModal.diet_plan_image)
                .into(holder.dpSubImg)
        }

        holder.rltvContainer.setOnClickListener {
            myDietPlanListenner.getDietPlanInfo(dietPlanSubcategoryModal, pos, "0")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.show_diet_plan_view, parent, false))
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false))
    }

    override fun getItemCount(): Int {
        return dietPSubCategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val dpSubImg = view.dp_sub_img
        val dpSubcatName = view.dp_subcat_name
        val rltvContainer = view.rltv_container
    }

    interface MyDietPlanLIstener {
        fun getDietPlanInfo(data: DietPlanSubCategoryModal.Data.DietListing, position: Int, whichClick: String)
    }

}
