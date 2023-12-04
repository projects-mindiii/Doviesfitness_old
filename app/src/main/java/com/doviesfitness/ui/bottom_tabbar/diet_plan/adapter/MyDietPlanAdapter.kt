package com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter

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
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.MyDietPlan
import com.doviesfitness.utils.CommanUtils
import kotlinx.android.synthetic.main.diet_p_sub_category_view.view.dp_sub_img
import kotlinx.android.synthetic.main.diet_p_sub_category_view.view.dp_subcat_name
import kotlinx.android.synthetic.main.diet_p_sub_category_view.view.lock_img
import kotlinx.android.synthetic.main.my_diet_plan.view.*

class MyDietPlanAdapter(
    context: Context,
    dietPSubCategoryList: ArrayList<MyDietPlan.Data>,
    myDietPlanListener: MyDietPlanLIstener
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context
    var dietPSubCategoryList: ArrayList<MyDietPlan.Data>
    var myDietPlanListener: MyDietPlanLIstener
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2
    private var showLoader: Boolean = false


    init {
        this.context = context
        this.dietPSubCategoryList = dietPSubCategoryList
        this.myDietPlanListener = myDietPlanListener
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == dietPSubCategoryList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER
            else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
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

        val holder = rvHolder as MyViewHolder

        val params = holder.rltvContainer.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.height = dpWidth / 2
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.rltvContainer.setLayoutParams(params)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        // return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false))

        when (viewType) {
            VIEWTYPE_ITEM -> {
                return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false))
            }
            else -> {
                return FooterLoader(
                    LayoutInflater.from(context).inflate(
                        R.layout.pagination_item_loader,
                        parent,
                        false
                    )
                )
            }
        }
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

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }


}