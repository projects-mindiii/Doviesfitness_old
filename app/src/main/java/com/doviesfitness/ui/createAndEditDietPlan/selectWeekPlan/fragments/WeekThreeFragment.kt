package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.fragments

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentWeekThreeBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.DietPlanFragment
import com.doviesfitness.ui.createAndEditDietPlan.activity.CreateWorkOutPlanActivty
import com.doviesfitness.ui.createAndEditDietPlan.adapter.WeekAdapter
import com.doviesfitness.ui.createAndEditDietPlan.fragment.PagerWorkOutPlanFragment
import com.doviesfitness.ui.createAndEditDietPlan.modal.ShowWorkoutDetailModel
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.activity.SelectWorkOutPlanActivity
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.modal.WorkoutCollectionCategoryModel
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import java.util.ArrayList

class WeekThreeFragment : BaseFragment(), WeekAdapter.WeekListener, CreateWorkOutPlanActivty.WeekThreeLinstenr {

    private lateinit var workoutColllectionCategory: WorkoutCollectionCategoryModel.Data
    private lateinit var myWorkOutData: WorkOutListModal.Data
    private lateinit var binding: FragmentWeekThreeBinding
    lateinit var adapter: WeekAdapter
    private var weekPlanList = ArrayList<WorkOutListModal.Data>()
    private lateinit var createWorkOutPlanActivty: CreateWorkOutPlanActivty

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_week_three, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {

        val spacingInPixels1 = resources.getDimensionPixelSize(R.dimen._10sdp)
        binding.week3Rv.addItemDecoration(DietPlanFragment.MySpacesItemDecoration(spacingInPixels1))

        for (i in 0..6) {
            //val j = i + 1
            //val myWorkout = WorkOutListModal.Data("", "", "", "", "", "", "", "", "", "", "")

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

            // myWorkout.forDay = "" + j
            weekPlanList.add(myWorkOutData)
        }

        adapter = WeekAdapter(binding.week3Rv.context, weekPlanList, this)
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.week3Rv.layoutManager = layoutManager1
        binding.week3Rv.adapter = adapter
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // get instance of activity
        createWorkOutPlanActivty = context as CreateWorkOutPlanActivty
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (createWorkOutPlanActivty != null) {
            createWorkOutPlanActivty.setWeekThreeDataListenr(this)
        }
    }

    override fun getDietCategoryDetailsInfo(data: WorkOutListModal.Data, position: Int, whichClick: String) {
        hideKeyboard()
        // add 1 in position becoze indexing is in 0...6
        val addOnePos = position + 1
        val pos = addOnePos.toString()
        //////////////////////////////////////////

        if (whichClick.equals("Add")) {
            val intent = Intent(context, SelectWorkOutPlanActivity::class.java)
            intent.putExtra("position", "" + position)
            startActivityForResult(intent, 101)

        } else if (whichClick.equals("Edit")) {
            val intent = Intent(context, SelectWorkOutPlanActivity::class.java)
            intent.putExtra("position", "" + position)
            startActivityForResult(intent, 101)

        } else if (whichClick.equals("Delete")) {
            // i am not delete this field but set blank at this position
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
                forDay = (pos.toInt() -1).toString(),
                whichWeek = "3"
            )

            weekPlanList.set(position, myWorkOutData)
            adapter.notifyDataSetChanged()

            if(parentFragment is PagerWorkOutPlanFragment){
                (parentFragment as PagerWorkOutPlanFragment).setWeekTwoData(weekPlanList)
            }
        }
    }

    // Call get back data fro MyWorkout Fragment on Done Button
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 101
        if (requestCode == 101 && data != null) {

            Log.v("data", "" + data)

            if (data.hasExtra("SelectMyWorkOutCollection")) {
                workoutColllectionCategory = data.getSerializableExtra("SelectMyWorkOutCollection") as WorkoutCollectionCategoryModel.Data
                val position = data.getStringExtra("position")!! as String
                val pos = position.toInt()



                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = workoutColllectionCategory.workout_name,
                    workout_category = workoutColllectionCategory.workout_category,
                    workout_time = workoutColllectionCategory.workout_time,
                    workout_image = workoutColllectionCategory.workout_image,
                    workout_access_level = "",
                    workout_fav_status = "",
                    workout_id = workoutColllectionCategory.workout_id,
                    workout_share_url = "",
                    workout_time1 = "",
                    isSelected = "",
                    forDay = position,
                    whichWeek = "3"
                )

                weekPlanList.set(pos, myWorkOutData)
                adapter.notifyDataSetChanged()
                /* if(parentFragment is PagerWorkOutPlanFragment){
                     (parentFragment as PagerWorkOutPlanFragment).setWeekThreeData(weekPlanList)
                 }*/
            }

            if (data.hasExtra("SelectMyWorkOut")) {
                myWorkOutData = data.getSerializableExtra("SelectMyWorkOut") as WorkOutListModal.Data
                val position = data.getStringExtra("position")!! as String
                val pos = position.toInt()
                myWorkOutData.forDay = position
                myWorkOutData.whichWeek = "3"
                weekPlanList.set(pos, myWorkOutData)
                adapter.notifyDataSetChanged()
            }


            if (parentFragment is PagerWorkOutPlanFragment) {
                (parentFragment as PagerWorkOutPlanFragment).setWeekThreeData(weekPlanList)
            }
        }
    }


    override fun getWeekThreeData(allWeekData: ShowWorkoutDetailModel.Data.GetProgramWorkouts) {

        val weekOneDataArray = allWeekData.Week3

        if (weekOneDataArray != null) {
            weekPlanList.clear()
            for (i in weekOneDataArray.indices) {
                val weekDataObject = weekOneDataArray.get(i)
                val myWorkOutData = WorkOutListModal.Data(
                    workout_name = weekDataObject.program_workout_name,
                    workout_category = weekDataObject.program_workout_good_for,
                    workout_time = weekDataObject.program_workout_time,
                    workout_image = weekDataObject.program_workout_image,
                    workout_access_level = "",
                    workout_fav_status = weekDataObject.program_workout_status,
                    workout_id = weekDataObject.workout_id,
                    workout_share_url = weekDataObject.program_workout_flag,
                    workout_time1 = weekDataObject.program_workout_time,
                    isSelected = "",
                    forDay = weekDataObject.program_day_number,
                    whichWeek = weekDataObject.program_week_number
                )

                weekPlanList.add(myWorkOutData)
            }
            adapter.notifyDataSetChanged()
        }

        if (parentFragment is PagerWorkOutPlanFragment) {
            (parentFragment as PagerWorkOutPlanFragment).setWeekThreeData(weekPlanList)
        }
    }
}
