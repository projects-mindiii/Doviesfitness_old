package com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.WorkOutPlanModal
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.dietPlan_img
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.diet_container
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.diet_plan_name
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.dollor_img
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.item_layout
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.iv_gredient
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.lock_img
import kotlinx.android.synthetic.main.workout_plan_view_copy.view.txt_week_count

class NewWorkOutPlanAdapter(
    context: Context,
    workOutPlanList: ArrayList<WorkOutPlanModal.Data.GetAllProgram>,
    workOutPlanListener: WorkOutPlanistener,
    isAdmin: String
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>()
{
    private var context: Context
    var workOutPlanList: ArrayList<WorkOutPlanModal.Data.GetAllProgram>
    var workOutPlanListener: WorkOutPlanistener
    var isAdmin: String

    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2

    init {
        this.context = context
        this.workOutPlanList = workOutPlanList
        this.workOutPlanListener = workOutPlanListener
        this.isAdmin = isAdmin
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == workOutPlanList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                val view = LayoutInflater.from(context).inflate(
                    R.layout.workout_plan_view_copy, parent, false
                )/*val getGridLayoutManager = view.layoutParams as GridLayoutManager.LayoutParams
                getGridLayoutManager.height = ((parent.measuredWidth/2)*1.34).toInt()
                view.layoutParams = getGridLayoutManager*/

                /*val getNewWidth= view.item_layout.layoutParams.width/2
                val getNewHeight = getNewWidth * 1.34
                view.item_layout.layoutParams.height = getNewHeight.toInt()*/

                return ViewHolder(view)
            }

            else -> {
                return FooterLoader(
                    LayoutInflater.from(context).inflate(
                        R.layout.new_pagination_view, parent, false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return workOutPlanList.size
    }

    override fun onBindViewHolder(
        rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int
    ) {

        if (rvHolder is FooterLoader) {
            val loaderViewHolder = rvHolder
            if (showLoader) {
                loaderViewHolder.mProgressBar.visibility = View.VISIBLE
            } else {
                loaderViewHolder.mProgressBar.visibility = View.GONE
            }
            return
        }
        val holder = rvHolder as ViewHolder


//        val params = holder.diet_container.getLayoutParams() as androidx.recyclerview.widget.RecyclerView.LayoutParams
//        val dpWidth = CommanUtils.getWidthAndHeight(context as Activity)
//        params.height = dpWidth - 50
//        Log.v("dpWidth", "" + dpWidth + "--------" + params.height)
//        holder.diet_container.setLayoutParams(params)


        val workOutPlanModal = workOutPlanList.get(pos)
        holder.dietPlanName.text = workOutPlanModal.program_name
        holder.txtWeekCount.text = workOutPlanModal.program_sub_title

        if (!workOutPlanModal.program_image.isEmpty()) {
            Glide.with(holder.dietPlanImage).load(workOutPlanModal.program_portrait_image)
                .into(holder.dietPlanImage)
        }

        /* holder.diet_container.setOnClickListener {
             workOutPlanListener.getWorkoutPlanInfo(workOutPlanModal, pos)
         }*/
        holder.diet_container.setOnClickListener {
//            if (workOutPlanModal.program_access_level == "LOCK") {
//                if (workOutPlanModal.access_level == "Exclusive") {
//                    val url = "https://www.doviesfitness.com/"
//                    exclusive_navigation(url)
//                } else if (workOutPlanModal.access_level == "Paid") {
//                    if (getDataManager()
//                            .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!! == "0"
//                    ) {
//                        val url = "https://www.doviesfitness.com/"
//                        exclusive_navigation(url)
//                    } else workOutPlanListener.getWorkoutPlanInfo(workOutPlanModal, pos)
//
//                }
//                if ("Yes".equals(isAdmin)) {
//                    holder.lockImg.visibility = View.GONE
//                } else {
//                    holder.lockImg.visibility = View.VISIBLE
//                }
//            } else
            workOutPlanListener.getWorkoutPlanInfo(workOutPlanModal, pos)
        }


        /*Manage Visibility Lock & Doller Icon*/
        if (workOutPlanModal.program_access_level == "LOCK") {
            if ("Yes" == isAdmin) {
                holder.lockImg.visibility = View.GONE
                holder.dollor_img.visibility = View.GONE
            } else {
                if ("Paid" == workOutPlanModal.access_level) {
                    if (getDataManager().getUserStringData(
                            AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED
                        )!! != "0"
                    ) {
                        holder.lockImg.visibility = View.GONE
                        holder.dollor_img.visibility = View.GONE
                    } else {
                        holder.lockImg.visibility = View.GONE
                        holder.dollor_img.visibility = View.VISIBLE
                    }
                } else if ("Exclusive" == workOutPlanModal.access_level) {
                    holder.lockImg.visibility = View.GONE
                    holder.dollor_img.visibility = View.VISIBLE
                } else {
                    holder.lockImg.visibility = View.VISIBLE
                    holder.dollor_img.visibility = View.GONE
                }
            }
        } else {
            holder.lockImg.visibility = View.GONE
            holder.dollor_img.visibility = View.GONE
        }

        if (workOutPlanModal.show_in_app.equals("No")) {
            holder.dietPlanName.visibility = View.GONE
            holder.txtWeekCount.visibility = View.GONE
            holder.ivGredient.visibility = View.GONE
        } else {
            holder.dietPlanName.visibility = View.VISIBLE
            holder.txtWeekCount.visibility = View.VISIBLE
            holder.ivGredient.visibility = View.VISIBLE
        }
        if (workOutPlanModal.program_name.isEmpty()) {
            if (workOutPlanModal.program_sub_title.isEmpty()) {
                holder.dietPlanName.visibility = View.GONE
                holder.txtWeekCount.visibility = View.GONE
                holder.ivGredient.visibility = View.GONE
            } else {
                holder.dietPlanName.visibility = View.VISIBLE
                holder.txtWeekCount.visibility = View.VISIBLE
                holder.ivGredient.visibility = View.VISIBLE
            }
        }
    }

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val dietPlanName = view.diet_plan_name
        val dietPlanImage = view.dietPlan_img
        var diet_container = view.diet_container
        var txtWeekCount = view.txt_week_count
        var lockImg = view.lock_img
        var dollor_img = view.dollor_img
        var ivGredient = view.iv_gredient
        var item_layout = view.item_layout

    }

    interface WorkOutPlanistener {
        fun getWorkoutPlanInfo(data: WorkOutPlanModal.Data.GetAllProgram, position: Int)
    }

    private fun exclusive_navigation(url: String) {
        val uri = Uri.parse(url)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        ContextCompat.startActivity(context, goToMarket, null)


    }

}
