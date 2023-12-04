package com.doviesfitness.ui.showDietPlanDetail.fragment

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentShowPagerPlanOverViewBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity

class ShowPagerPlanOverViewFragment : BaseFragment(), ShowDietPlanDetailActivity.GetOverViewLitener {

    lateinit var binding: FragmentShowPagerPlanOverViewBinding
    private lateinit var showDietPlanDetailActivity: ShowDietPlanDetailActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_pager_plan_over_view, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showDietPlanDetailActivity = activity as ShowDietPlanDetailActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showDietPlanDetailActivity != null) {
            showDietPlanDetailActivity.setOverViewListenr(this)
        }
    }

    override fun getDietPlanData(overView: ShowWorkoutDetailModel.Data.GetCustomerProgramDetail) {
        binding.tvGoodFor.setText(overView.goodforMasterName.replace(" |", ","))
        binding.tvEquipment.setText(overView.equipmentMasterName.replace(" |", ","))
        binding.tvOverview.setText(overView.program_description.replace(" |", ","))
    }

}
