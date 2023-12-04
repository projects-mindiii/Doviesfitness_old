package com.doviesfitness.ui.showDietPlanDetail.viewPAger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ShowPlanCategoryAdapter(var fm: androidx.fragment.app.FragmentManager, var fragments:List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {

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
            title = "Workout Plan"
        } else if (position == 1) {
            title = "Diet Plan"
        } else if (position == 2) {
            title = "Plan Info"
        }
        return title
    }
}
