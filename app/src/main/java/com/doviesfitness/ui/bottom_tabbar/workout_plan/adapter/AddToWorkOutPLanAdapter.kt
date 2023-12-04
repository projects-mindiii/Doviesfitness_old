package com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamDataModel
import com.doviesfitness.ui.bottom_tabbar.workout_plan.modal.AddToWorkOutPLanModal
import kotlinx.android.synthetic.main.add_to_workout_plan_view.view.setting_img
import kotlinx.android.synthetic.main.add_to_workout_plan_view_copy.view.diet_container
import kotlinx.android.synthetic.main.add_to_workout_plan_view_copy.view.diet_plan_name
import kotlinx.android.synthetic.main.add_to_workout_plan_view_copy.view.empty_view
import kotlinx.android.synthetic.main.add_to_workout_plan_view_copy.view.iv_gredient
import kotlinx.android.synthetic.main.add_to_workout_plan_view_copy.view.txt_week_count
import kotlinx.android.synthetic.main.diet_plan_category_view.view.dietPlan_img
import kotlinx.android.synthetic.main.workout_plan_view.view.lock_img

class AddToWorkOutPLanAdapter(
    context: Context,
    workOutPlanList: ArrayList<AddToWorkOutPLanModal.Data>,
    addToWorkOutPlanistener: AddToWorkOutPlanistener
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context
    var workOutPlanList: ArrayList<AddToWorkOutPLanModal.Data>
    var addToWorkOutPlanistener: AddToWorkOutPlanistener
    var isAdmin: String = ""

    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2


    init {
        this.context = context
        this.workOutPlanList = workOutPlanList
        this.addToWorkOutPlanistener = addToWorkOutPlanistener
        this.isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == workOutPlanList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return ViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.add_to_workout_plan_view_copy, parent, false)
                )
            }

            else -> {
                return FooterLoader(
                    LayoutInflater.from(context)
                        .inflate(R.layout.new_pagination_view, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return workOutPlanList.size
    }


    override fun onBindViewHolder(
        rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        pos: Int
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

        if (pos == workOutPlanList.lastIndex) {
            holder.empty_view.visibility = View.VISIBLE
        } else {
            holder.empty_view.visibility = View.GONE
        }

        val workOutPlanModal = workOutPlanList.get(pos)

        if (workOutPlanModal.show_in_app.equals("No")) {
            holder.dietPlanName.visibility = View.GONE
        } else {
            holder.dietPlanName.visibility = View.VISIBLE
        }
        if (workOutPlanModal.program_name.isEmpty()) {
            if (workOutPlanModal.program_sub_title.isEmpty()) {
                holder.iv_gredient.visibility = View.GONE
            } else {
                holder.iv_gredient.visibility = View.VISIBLE
            }
        }

        if (workOutPlanModal.program_name.isEmpty()) {
            if (workOutPlanModal.program_sub_title.isNotEmpty()) {
                if (workOutPlanModal.show_in_app.equals("No")) {
                    holder.iv_gredient.visibility = View.GONE
                } else {
                    holder.iv_gredient.visibility = View.VISIBLE
                }
            }
        }
        if (workOutPlanModal.program_name.isEmpty()) {
            if (workOutPlanModal.program_sub_title.isNotEmpty()) {
                if (workOutPlanModal.show_in_app.equals("No")) {
                    holder.iv_gredient.visibility = View.GONE
                } else {
                    holder.iv_gredient.visibility = View.VISIBLE
                }
            }
        }
        if (workOutPlanModal.program_name.isNotEmpty()) {
            if (workOutPlanModal.program_sub_title.isEmpty()) {
                if (workOutPlanModal.show_in_app.equals("No")) {
                    holder.iv_gredient.visibility = View.GONE
                } else {
                    holder.iv_gredient.visibility = View.VISIBLE
                }
            }
        }

        holder.dietPlanName.text = workOutPlanModal.program_name
        holder.txtWeekCount.text = workOutPlanModal.program_sub_title

        if (workOutPlanModal.plan_type == "stream_workout") {
            Glide.with(holder.dietPlanImage).load(workOutPlanModal.program_portrait_image)
                .into(holder.dietPlanImage)
        } else {
            if (!workOutPlanModal.program_image.isEmpty()) {
                Glide.with(holder.dietPlanImage).load(workOutPlanModal.program_portrait_image)
                    .into(holder.dietPlanImage)
            }
        }

        holder.diet_container.setOnClickListener {
            if (workOutPlanModal.plan_type == "stream_workout") {
                val intent = Intent(context, StreamDetailActivity::class.java)
                val data = StreamDataModel.Settings.Data.RecentWorkout(
                    "",
                    "",
                    "",
                    "",
                    "",
                    workOutPlanModal.program_id,
                    "",
                    workOutPlanModal.program_image,
                    "",
                    "",
                    "",
                    "",
                    ""
                )
                intent.putExtra("data", data)
                intent.putExtra("from", "recent")
                context.startActivity(intent)
            } else {
                addToWorkOutPlanistener.getAddWorkoutPlanInfo(workOutPlanModal, pos, "container")
            }
        }

        holder.settingImg.setOnClickListener {
            addToWorkOutPlanistener.getAddWorkoutPlanInfo(workOutPlanModal, pos, "setting")
        }

        if (workOutPlanModal.program_access_level.equals("LOCK")) {
            if ("Yes".equals(isAdmin)) {
                holder.lockImg.visibility = View.GONE
            } else {
                holder.lockImg.visibility = View.VISIBLE
            }
        } else {
            holder.lockImg.visibility = View.GONE
        }

        if (workOutPlanModal.plan_type == "stream_workout") {
            holder.settingImg.visibility = View.GONE
        } else {
            holder.settingImg.visibility = View.VISIBLE
        }
    }

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var dietPlanName = view.diet_plan_name
        var dietPlanImage = view.dietPlan_img
        var diet_container = view.diet_container
        var txtWeekCount = view.txt_week_count
        var settingImg = view.setting_img
        var lockImg = view.lock_img
        var empty_view = view.empty_view
        var iv_gredient = view.iv_gredient
    }

    interface AddToWorkOutPlanistener {
        fun getAddWorkoutPlanInfo(data: AddToWorkOutPLanModal.Data, position: Int, onClick: String)
    }
}