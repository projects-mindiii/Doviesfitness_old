package com.doviesfitness.ui.createAndEditDietPlan.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanSubCategoryModal
import com.doviesfitness.utils.CommanUtils
import kotlinx.android.synthetic.main.my_diet_plan.view.*

class AddDietPlanAdapter(
    context: Context,
    dietPSubCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>, addDietPlanLIstener : AddDietPlanLIstener
) : androidx.recyclerview.widget.RecyclerView.Adapter<AddDietPlanAdapter.MyViewHolder>() {
    private var context: Context
    var dietPSubCategoryList: ArrayList<DietPlanSubCategoryModal.Data.DietListing>
    var addDietPlanLIstener: AddDietPlanLIstener

    init {
        this.context = context
        this.dietPSubCategoryList = dietPSubCategoryList
        this.addDietPlanLIstener = addDietPlanLIstener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        val params =
            holder.rltvContainer.getLayoutParams() as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
        params.height = dpWidth / 2 - context.getResources().getDimension(R.dimen._25sdp).toInt()
        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
        holder.rltvContainer.setLayoutParams(params)

        val dietPlanSubcategoryModal = dietPSubCategoryList.get(pos)

        holder.ivDelete.visibility  =View.VISIBLE
        holder.dpSubcatName.visibility  =View.GONE
        holder.dpSubcatName.setText(dietPlanSubcategoryModal.diet_plan_title)

        if (!dietPlanSubcategoryModal.diet_plan_image.isEmpty()) {
            Glide.with(holder.dpSubImg).load(dietPlanSubcategoryModal.diet_plan_image)
                .into(holder.dpSubImg)
        }

        holder.rltvContainer.setOnClickListener {
            addDietPlanLIstener.getAddDietPlanInfo(dietPlanSubcategoryModal, pos, "0")
        }

        holder.ivDelete.setOnClickListener {
           /* dietPSubCategoryList.removeAt(pos)
            notifyDataSetChanged()*/
            addDietPlanLIstener.getAddDietPlanInfo(dietPlanSubcategoryModal, pos, "1")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.add_diet_plan, parent, false))
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_diet_plan, parent, false))
    }

    override fun getItemCount(): Int {
        return dietPSubCategoryList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val dpSubImg = view.dp_sub_img
        val dpSubcatName = view.dp_subcat_name
        val ivDelete = view.iv_delete
        val rltvContainer = view.rltv_container
    }

    interface AddDietPlanLIstener {
        fun getAddDietPlanInfo(data: DietPlanSubCategoryModal.Data.DietListing, position: Int, whichClick: String)
    }
}
