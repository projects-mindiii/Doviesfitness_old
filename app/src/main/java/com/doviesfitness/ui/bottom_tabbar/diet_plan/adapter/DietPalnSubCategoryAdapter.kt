package com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Utility
import kotlinx.android.synthetic.main.diet_p_sub_category_view.view.*

class DietPalnSubCategoryAdapter(
    context: Context,
    dietPSubCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>,
    dietPSubCategoryListener: DietPSubCategoryListener,
    isAdmin: String
) : androidx.recyclerview.widget.RecyclerView.Adapter<DietPalnSubCategoryAdapter.MyViewHolder>() {
    private var context: Context
    var dietPSubCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>
    var dietPSubCategoryListener: DietPSubCategoryListener
    var isAdmin: String

    init {
        this.context = context
        this.dietPSubCategoryList = dietPSubCategoryList
        this.dietPSubCategoryListener = dietPSubCategoryListener
        this.isAdmin = isAdmin
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {


     /*   val params = holder.rltvContainer.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.height = dpWidth / 2 - context.getResources().getDimension(R.dimen._25sdp).toInt()
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.rltvContainer.setLayoutParams(params)*/

        var width = Utility.getScreenWidthDivideIntoTwo(context)
        holder.rltvContainer.layoutParams.width = ( width/1.09).toInt()
         holder.rltvContainer.layoutParams.height = (width * 1.23).toInt()
        if (pos/2==0){
            val layoutParams = RelativeLayout.LayoutParams(
                (Utility.getScreenWidthDivideIntoTwo(context)/1.09).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()
            )
            layoutParams.setMargins( 0, 36,  0, -6)

            holder.rltvContainer.layoutParams = layoutParams

        }else{
            val layoutParams = RelativeLayout.LayoutParams(
                (Utility.getScreenWidthDivideIntoTwo(context)/1.09).toInt(), (Utility.getScreenWidthDivideIntoTwo(context)*1.23).toInt()
            )
            layoutParams.setMargins( 0, 36,  0, -6)

            holder.rltvContainer.layoutParams = layoutParams

        }



        val dietPlanSubcategoryModal = dietPSubCategoryList.get(pos);
        holder.dpSubcatName.setText(dietPlanSubcategoryModal.diet_plan_title)

        if (!dietPlanSubcategoryModal.diet_plan_full_image.isEmpty()) {
            Glide.with(holder.dpSubImg).load(dietPlanSubcategoryModal.diet_plan_full_image)
                .placeholder(R.drawable.rounded_black_opacity_bg)
                .into(holder.dpSubImg)
        }

        // show lock icon when user { "diet_plan_access_level" = LOCK; }

        // lock icon visiblity
        if (dietPlanSubcategoryModal.diet_plan_access_level.equals("LOCK")) {
            if ("Yes".equals(isAdmin)) {
                holder.lockImg.visibility = View.GONE
            } else {
                holder.lockImg.visibility = View.VISIBLE
            }
        } else {
            holder.lockImg.visibility = View.GONE
        }


        holder.dpSubImg.setOnClickListener {
            dietPSubCategoryListener.getDietPCategoryDetailsInfo(dietPlanSubcategoryModal, pos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            //LayoutInflater.from(context).inflate(R.layout.diet_p_sub_category_view, parent, false)
            LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dietPSubCategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val dpSubImg = view.dp_sub_img
        val dpSubcatName = view.dp_subcat_name
        val lockImg = view.lock_img
        val rltvContainer = view.rltv_container
    }

    interface DietPSubCategoryListener {
        fun getDietPCategoryDetailsInfo(
            data: DietPlanSubCategoryModal.Data.DietListing,
            position: Int
        )
    }
}