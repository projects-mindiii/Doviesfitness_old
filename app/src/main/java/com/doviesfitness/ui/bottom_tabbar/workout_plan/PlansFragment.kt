package com.doviesfitness.ui.bottom_tabbar.workout_plan


import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import com.google.android.material.tabs.TabLayout
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentPlansBinding
import com.doviesfitness.databinding.FragmentWorkoutBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.DietPlanFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.DietPCategoryDetailFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter.PlansPagerAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.CreateWorkoutActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.FilterExerciseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.SearchExerciseFragment
import com.doviesfitness.ui.profile.inbox.activity.NotificationExerciesActivity
import com.doviesfitness.utils.Constant
import kotlinx.android.synthetic.main.fragment_workout.*
import java.lang.Exception


class PlansFragment : BaseFragment(), View.OnClickListener {
    private var FromFavWorkout: Boolean = false
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPlansBinding
    private lateinit var pagerAdapter: PlansPagerAdapter
    private var mLastClickTime: Long = 0
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plans, container, false);

        fragments.add(WorkoutPlanFragment())
        fragments.add(DietPlanFragment())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialisation()
    }


    private fun initialisation() {

      //  filter_icon.setOnClickListener(this)
       // search_img.setOnClickListener(this)
       // create_workout.setOnClickListener(this)
        pagerAdapter = PlansPagerAdapter(childFragmentManager, context,fragments)
        binding.viewpager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewpager)

        binding.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                 //   binding.filterIcon.visibility = View.VISIBLE
                 //   binding.searchImg.visibility = View.VISIBLE
                } else {
                //    binding.filterIcon.visibility = View.GONE
               //     binding.searchImg.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        if (HomeTabActivity.whichScreen!=null && !HomeTabActivity.whichScreen.isEmpty()
            && HomeTabActivity.whichScreen.equals("fromMyDietPlanActvity")){
            binding.tabLayout!!.getTabAt(1)!!.select();
            }

        arguments?.let {
            val module_id = arguments!!.getString("module_id")
            val froWhich_Screen = arguments!!.getString("froWhich_Screen")
            val fromDeepLinking = arguments!!.getString("fromDeepLinking")


            var fromMyDietPlanActvity = ""
            try {
                fromMyDietPlanActvity = arguments!!.getString("whichScreen")!!
            }
            catch (ex:Exception){
                ex.printStackTrace()
            }
            FromFavWorkout = arguments!!.getBoolean("FromFavWorkout")

            if(FromFavWorkout){
                binding.tabLayout!!.getTabAt(1)!!.select();
            }

            if ("DIET_PLAN".equals(froWhich_Screen,true)||fromMyDietPlanActvity.equals("fromMyDietPlanActvity",true)) {
                if (!module_id!!.isEmpty()) {

                        val dietPsubcategory = DietPCategoryDetailFragment()
                        val args = Bundle()
                        args.putString("module_id", module_id)
                    dietPsubcategory.arguments = args
                        getBaseActivity()?.addFragment(dietPsubcategory, R.id.container_id1, true)

                }

            } else if ("PROGRAM".equals(froWhich_Screen)) {
                if (!module_id!!.isEmpty()) {

                    val workoutPlanFragment = WorkoutPlanFragment()
                    val args = Bundle()
                    args.putString("module_id", module_id)
                    args.putString("fromDeepLinking", fromDeepLinking)
                    workoutPlanFragment.arguments = args
                    getBaseActivity()?.addFragment(workoutPlanFragment, R.id.container_id1, true)
                }
            }


/*
            if ("WORKOUT".equals(froWhich_Screen)) {
                if (!module_id!!.isEmpty()) {
                    val intent = Intent(context, WorkOutDetailActivity::class.java)
                    intent.putExtra("PROGRAM_DETAIL", module_id)
                    intent.putExtra("isMyWorkout", "no")
                    intent.putExtra("fromDeepLinking", fromDeepLinking)
                    startActivity(intent)
                }

            } else if ("EXERCISE".equals(froWhich_Screen)) {
                if (!module_id!!.isEmpty()) {
                    val intent = Intent(context, NotificationExerciesActivity::class.java)
                    intent.putExtra("category_id", module_id)
                    intent.putExtra("name", "Exercies")
                    startActivity(intent)
                }
            }
*/
        }

    }

    // to get data  when we come from notification
    fun newInstance(
        module_id: String,
        froWhichScreen: String,
        isFavWorkOut: Boolean,
        fromDeepLinking: String,whichScreen:String
    ): PlansFragment {
        val myFragment = PlansFragment()
        val args = Bundle()
        args.putString("module_id", module_id)
        args.putString("froWhich_Screen",froWhichScreen)
        args.putString("fromDeepLinking",fromDeepLinking)
        args.putBoolean("FromFavWorkout",isFavWorkOut)
        if (whichScreen!=null)
            args.putString("whichScreen",whichScreen)


        myFragment.arguments = args

        return myFragment
    }


    override fun onClick(v: View?) {
        when (v?.id) {

           /* R.id.create_workout -> {
                startActivity(Intent(mContext, CreateWorkoutActivity::class.java).putExtra("edit","create")
                    .putExtra("fromDeepLinking", ""))
            }

            R.id.search_img -> {
                getBaseActivity()?.addFragment(SearchExerciseFragment(), R.id.container_id1, true)
            }
            R.id.filter_icon -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime();
                }
                startActivity(Intent(getActivity(), FilterExerciseActivity::class.java).putExtra("category_id", "")
                    .putExtra("create", "")
                )
            }*/

        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivity!!.window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorBlack))
        }
    }
}
