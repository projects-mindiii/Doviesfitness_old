package com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.diet_plan_category_view.view.*


class NewDietPlanCategoryAdapter(context: Context, dietPlancategoryList: ArrayList<DietPlanCateGoriesModal.Data.DietPlanCategory>,dietPlanCategoryListener: DietPlanCategoryListener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context
    var dietPlancategoryList: ArrayList<DietPlanCateGoriesModal.Data.DietPlanCategory>
    var dietPlanCategoryListener: DietPlanCategoryListener

    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2

    init {
        this.context = context
        this.dietPlancategoryList = dietPlancategoryList
        this.dietPlanCategoryListener = dietPlanCategoryListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dietPlancategoryList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.diet_plan_category_view, parent, false))
            }
            else -> {
                return FooterLoader(LayoutInflater.from(context).inflate(R.layout.new_pagination_view, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return dietPlancategoryList.size
    }

    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        if (rvHolder is FooterLoader) {
            val loaderViewHolder = rvHolder as FooterLoader
            if (showLoader) {
                loaderViewHolder.mProgressBar.visibility = View.VISIBLE
            } else {
                loaderViewHolder.mProgressBar.visibility = View.GONE
            }
            return
        }
        val holder = rvHolder as ViewHolder

      /*  val params = holder.diet_container.getLayoutParams()
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.width = dpWidth
        params.height = dpWidth - 50
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.diet_container.setLayoutParams(params)
*/

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

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val dietPlanName = view.diet_plan_name
        val dietPlanImage = view.dietPlan_img
        var diet_container = view.diet_container
    }


    interface DietPlanCategoryListener {
        fun getDietCategoryDetailsInfo(data: DietPlanCateGoriesModal.Data.DietPlanCategory, position: Int)
    }
}
