package com.doviesfitness.ui.createAndEditDietPlan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SelectWorkPlanAdapter(fm: androidx.fragment.app.FragmentManager, var fragments:List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        /*var fragment: Fragment? = null
        when (position) {
            *//*0 -> fragment = PagerWorkOutPlanFragment()
            1 -> fragment = PagerDietPlanFragment()
            2 -> fragment = PagerPlanOverViewFragment()*//*
        }

        return fragment*/

        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        var title: String? = null
        if (position == 0) {
            title = "My Workouts"
        } else if (position == 1) {
            title = "Workout Collection"
        }
        return title
    }
}
