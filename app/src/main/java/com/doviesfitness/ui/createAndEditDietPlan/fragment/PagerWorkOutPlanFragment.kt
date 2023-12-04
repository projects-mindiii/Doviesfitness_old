package com.doviesfitness.ui.createAndEditDietPlan.fragment

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentPagerWorkOutPlanBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.fragments.*
import com.doviesfitness.ui.createAndEditDietPlan.viewPagerAdapters.WeeklyAdapter
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import java.util.ArrayList

class PagerWorkOutPlanFragment : BaseFragment() {

    private var completeList: ArrayList<WorkOutListModal.Data> = ArrayList<WorkOutListModal.Data>()
    private lateinit var temPWeekOneList: ArrayList<WorkOutListModal.Data>
    private lateinit var temPWeekTwoList: ArrayList<WorkOutListModal.Data>
    private lateinit var temPWeekThreeList: ArrayList<WorkOutListModal.Data>
    private lateinit var temPWeekFourList: ArrayList<WorkOutListModal.Data>
    private lateinit var binding: FragmentPagerWorkOutPlanBinding
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    private lateinit var weekOneFragment: WeekOneFragment
    private lateinit var weekTwoFragment: WeekTwoFragment
    private lateinit var weekThreeFragment: WeekThreeFragment
    private lateinit var weekFourFragment: WeekFourFragment
    private lateinit var createWorkOutPlanActivty: CreateWorkOutPlanActivty

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // get instance of activity
        createWorkOutPlanActivty = context as CreateWorkOutPlanActivty
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager_work_out_plan, container, false)
        initialization()
        return binding.root
    }


   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initialization() {

        weekOneFragment = WeekOneFragment()
        weekTwoFragment = WeekTwoFragment()
        weekThreeFragment = WeekThreeFragment()
        weekFourFragment = WeekFourFragment()

        //list of fragment to india  viewpager
        fragments.add(weekOneFragment)
        fragments.add(weekTwoFragment)
        fragments.add(weekThreeFragment)
        fragments.add(weekFourFragment)

        val WeeklyAdapter = WeeklyAdapter(childFragmentManager, fragments)
        binding.weekViewPager.setAdapter(WeeklyAdapter)
        binding.weekViewPager.setOffscreenPageLimit(4)
        binding.weekTablayout.setupWithViewPager(binding.weekViewPager)

        completeList = ArrayList()

        temPWeekOneList = ArrayList()
        temPWeekTwoList = ArrayList()
        temPWeekThreeList = ArrayList()
        temPWeekFourList = ArrayList()
    }

    public fun setWeekOneData(weekOneList: ArrayList<WorkOutListModal.Data>) {
        temPWeekOneList.clear()
        temPWeekOneList.addAll(weekOneList)
        setDataToActivtiy()
    }

    public fun setWeekTwoData(weekTwoList: ArrayList<WorkOutListModal.Data>) {
        temPWeekTwoList.clear()
        temPWeekTwoList.addAll(weekTwoList)
        setDataToActivtiy()
    }

    public fun setWeekThreeData(weekThreeList: ArrayList<WorkOutListModal.Data>) {
        temPWeekThreeList.clear()
        temPWeekThreeList.addAll(weekThreeList)
        setDataToActivtiy()
    }

    public fun setWeekFourData(weekfourList: ArrayList<WorkOutListModal.Data>) {
        temPWeekFourList.clear()
        temPWeekFourList.addAll(weekfourList)
        setDataToActivtiy()
    }

    public fun setDataToActivtiy() {
        completeList.clear()

        if (temPWeekOneList.size == 0) {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = "",
                    workout_category = "",
                    workout_time = "",
                    workout_image = "",
                    workout_access_level = "",
                    workout_fav_status = "",
                    workout_id = "0",
                    workout_share_url = "",
                    workout_time1 = "",
                    isSelected = "",
                    forDay = "" + i,
                    whichWeek = "1"
                )
                temPWeekOneList.add(myWorkOutData)
            }
            completeList.addAll(temPWeekOneList)
        } else {
            completeList.addAll(temPWeekOneList)
        }

        if (temPWeekTwoList.size == 0) {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = "",
                    workout_category = "",
                    workout_time = "",
                    workout_image = "",
                    workout_access_level = "",
                    workout_fav_status = "",
                    workout_id = "0",
                    workout_share_url = "",
                    workout_time1 = "",
                    isSelected = "",
                    forDay = "" + i,
                    whichWeek = "2"
                )
                temPWeekTwoList.add(myWorkOutData)
            }
            completeList.addAll(temPWeekTwoList)
        } else {
            completeList.addAll(temPWeekTwoList)
        }


        if (temPWeekThreeList.size == 0) {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = "",
                    workout_category = "",
                    workout_time = "",
                    workout_image = "",
                    workout_access_level = "",
                    workout_fav_status = "",
                    workout_id = "0",
                    workout_share_url = "",
                    workout_time1 = "",
                    isSelected = "",
                    forDay = "" + i,
                    whichWeek = "3"
                )
                temPWeekThreeList.add(myWorkOutData)
            }
            completeList.addAll(temPWeekThreeList)
        } else {
            completeList.addAll(temPWeekThreeList)
        }


        if (temPWeekFourList.size == 0) {
            for (i in 0..6) {
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = "",
                    workout_category = "",
                    workout_time = "",
                    workout_image = "",
                    workout_access_level = "",
                    workout_fav_status = "",
                    workout_id = "0",
                    workout_share_url = "",
                    workout_time1 = "",
                    isSelected = "",
                    forDay = "" + i,
                    whichWeek = "4"
                )
                temPWeekFourList.add(myWorkOutData)
            }
            completeList.addAll(temPWeekFourList)
        } else {
            completeList.addAll(temPWeekFourList)
        }

        createWorkOutPlanActivty.fragmentone_workoutPlan(completeList)
    }
}
