package com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import kotlinx.android.synthetic.main.diet_plan_category_view.view.*

class DietPlanCategoryAdapter(context: Context, dietPlancategoryList: ArrayList<DietPlanCateGoriesModal.Data.DietPlanCategory>, dietPlanCategoryListener: DietPlanCategoryListener
) :

    androidx.recyclerview.widget.RecyclerView.Adapter<DietPlanCategoryAdapter.MyViewHolder>() {
    private var context: Context
    var dietPlancategoryList: ArrayList<DietPlanCateGoriesModal.Data.DietPlanCategory>
    var dietPlanCategoryListener: DietPlanCategoryListener

    init {
        this.context = context
        this.dietPlancategoryList = dietPlancategoryList
        this.dietPlanCategoryListener = dietPlanCategoryListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val dietPlancategoryModal = dietPlancategoryList.get(pos);
        holder.dietPlanName.setText(dietPlancategoryModal.diet_plan_category_name)

        if (!dietPlancategoryModal.diet_plan_category_image.isEmpty()) {
            Glide.with(holder.dietPlanImage).load(dietPlancategoryModal.diet_plan_category_image)
                .into(holder.dietPlanImage)
        }

        holder.diet_container.setOnClickListener {
            dietPlanCategoryListener.getDietCategoryDetailsInfo(dietPlancategoryModal, pos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.diet_plan_category_view, parent, false))
    }

    override fun getItemCount(): Int {
        return dietPlancategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val dietPlanName = view.diet_plan_name
        val dietPlanImage = view.dietPlan_img
        var diet_container = view.diet_container
    }


    interface DietPlanCategoryListener {
        fun getDietCategoryDetailsInfo(data: DietPlanCateGoriesModal.Data.DietPlanCategory, position: Int)
    }
}



