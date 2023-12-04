package com.doviesfitness.ui.showDietPlanDetail.fragment

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.createAndEditDietPlan.viewPagerAdapters.WeeklyAdapter
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekFourFragment
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekOneFragment
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekThreeFragment
import com.doviesfitness.ui.showDietPlanDetail.weeklyFragments.ShowWeekTwoFragment
import java.lang.Exception

class ShowPagerWorkOutPlanFragment : BaseFragment(), ShowDietPlanDetailActivity.ForLOadFragmentLitener {

    private var whenWeekOne: Boolean = false
    private lateinit var weeklyAdapter: WeeklyAdapter
    private lateinit var binding: com.doviesfitness.databinding.FragmentShowPagerWorkOutPlanBinding
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    private lateinit var showWeekOneFragment: ShowWeekOneFragment
    private lateinit var showWeekTwoFragment: ShowWeekTwoFragment
    private lateinit var showWeekThreeFragment: ShowWeekThreeFragment
    private lateinit var showWeekFourFragment: ShowWeekFourFragment
    private lateinit var showDietPlanDetailActivity: ShowDietPlanDetailActivity
    private var weekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_pager_work_out_plan, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        showWeekOneFragment = ShowWeekOneFragment()
        showWeekTwoFragment = ShowWeekTwoFragment()
        showWeekThreeFragment = ShowWeekThreeFragment()
        showWeekFourFragment = ShowWeekFourFragment()

        //list of fragment to in  viewpager
        fragments.add(showWeekOneFragment)
        fragments.add(showWeekTwoFragment)
        fragments.add(showWeekThreeFragment)
        fragments.add(showWeekFourFragment)

        weeklyAdapter = WeeklyAdapter(childFragmentManager, fragments)
        binding.weekViewPager.setAdapter(weeklyAdapter)
        binding.weekViewPager.setOffscreenPageLimit(fragments.size)
        binding.weekTablayout.setupWithViewPager(binding.weekViewPager)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // get instance of activity
        showDietPlanDetailActivity = context as ShowDietPlanDetailActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showDietPlanDetailActivity != null) {
            showDietPlanDetailActivity.forFragmentLoadListenr(this)
        }
    }


    override fun forLoadFragment(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts) {
        try {
            fragments.clear()
            weekData = allWeekData
            weekData?.let {

                //list of fragment to in  viewpager

                if (it.Week4 != null) {
                    whenWeekOne = false
                    fragments.add(showWeekOneFragment)
                    fragments.add(showWeekTwoFragment)
                    fragments.add(showWeekThreeFragment)
                    fragments.add(showWeekFourFragment)
                }

                else if (it.Week3 != null) {
                    whenWeekOne = false
                    fragments.add(showWeekTwoFragment)
                    fragments.add(showWeekThreeFragment)
                    fragments.add(showWeekFourFragment)
                }
                else if (it.Week2 != null) {
                    whenWeekOne = false
                    fragments.add(showWeekThreeFragment)
                    fragments.add(showWeekFourFragment)
                } else {
                    whenWeekOne = true
                    fragments.add(showWeekFourFragment)
                }

                if(it.Week1 != null){
                    if(whenWeekOne == true){
                        binding.weekTablayout.visibility = View.GONE
                        weeklyAdapter = WeeklyAdapter(childFragmentManager, fragments)
                        binding.weekViewPager.setAdapter(weeklyAdapter)
                        binding.weekViewPager.setOffscreenPageLimit(fragments.size)
                        binding.weekTablayout.setupWithViewPager(binding.weekViewPager)
                    }else{
                        weeklyAdapter = WeeklyAdapter(childFragmentManager, fragments)
                        binding.weekViewPager.setAdapter(weeklyAdapter)
                        binding.weekViewPager.setOffscreenPageLimit(fragments.size)
                        binding.weekTablayout.setupWithViewPager(binding.weekViewPager)
                    }
                }
            }
        }
        catch (ex:Exception){
            ex.printStackTrace()
        }

    }
}
