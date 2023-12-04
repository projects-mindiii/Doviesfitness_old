package com.doviesfitness.ui.createAndEditDietPlan.viewPagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class WeeklyAdapter (fm: androidx.fragment.app.FragmentManager, var fragments:List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        var title: String? = null
        if (position == 0) {
            title = "Week 1"
        } else if (position == 1) {
            title = "Week 2"
        } else if (position == 2) {
            title = "Week 3"
        }else if (position == 3) {
            title = "Week 4"
        }
        return title
    }
}
