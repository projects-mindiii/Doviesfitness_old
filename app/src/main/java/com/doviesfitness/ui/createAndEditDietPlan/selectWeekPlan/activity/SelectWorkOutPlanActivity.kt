package com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.activity

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import android.view.View
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.createAndEditDietPlan.adapter.SelectWorkPlanAdapter
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.fragments.MyWorkoutSelectPlanFragment
import com.doviesfitness.ui.createAndEditDietPlan.selectWeekPlan.fragments.WorkoutCollectionSelectPlan
import com.doviesfitness.ui.profile.myWorkOut.modal.WorkOutListModal
import eightbitlab.com.blurview.RenderScriptBlur


class SelectWorkOutPlanActivity : BaseActivity(), View.OnClickListener {

    private var position: String = ""
    private var data: WorkOutListModal.Data? = null
    private lateinit var binding: com.doviesfitness.databinding.ActivitySelectWorkOutPlanBinding
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_work_out_plan)
        inItView()
    }

    private fun inItView() {

        if (intent.getStringExtra("position") != null) {
            position = intent.getStringExtra("position") as String
        }
        hideNavigationBar()

        val windowBackground =  window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(getActivity()))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        //list of fragment to in  viewpager
        fragments.add(MyWorkoutSelectPlanFragment())

        // Set position in fragemnt for set data on that position
        val workoutCollectionSelectPlan = WorkoutCollectionSelectPlan()
        val args = Bundle()
        args.putString("position", position)
        workoutCollectionSelectPlan.setArguments(args)
        fragments.add(workoutCollectionSelectPlan)

        val selectWorkPlanAdapter = SelectWorkPlanAdapter(supportFragmentManager, fragments)
        binding.workoutPlanViewPager.setAdapter(selectWorkPlanAdapter)
        binding.workoutPlanViewPager.setOffscreenPageLimit(2)
        binding.selectWorkoutPlanTablayout.setupWithViewPager(binding.workoutPlanViewPager)


        binding.selectWorkoutPlanTablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding.txtDone.visibility = View.VISIBLE
                } else {
                    binding.txtDone.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        setOnClick(binding.ivBack,binding.txtDone)
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {

            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.txt_done -> {

                    data?.let {

                        val intent = Intent()
                        intent.putExtra("SelectMyWorkOut", it)
                        intent.putExtra("position", position)
                        setResult(101, intent)
                        finish()
                    }
            }
        }
    }

    // getWorkout Data from  Fragment{MyWorkoutSelectPlanFragment} to  activity for {Done} button which is on this activity
    public fun getMyWorkOutData(data: WorkOutListModal.Data) {
        this.data = data
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
