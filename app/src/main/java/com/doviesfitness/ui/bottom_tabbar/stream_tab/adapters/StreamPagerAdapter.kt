package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class StreamPagerAdapter(
    var fm: FragmentManager,
    var fragments: List<Fragment>,
    var nameList: ArrayList<String>
) :
    androidx.fragment.app.FragmentPagerAdapter(fm) {

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
            title = nameList.get(0)
        } else if (position == 1) {
            title = nameList.get(1)
        } else if (position == 2) {
            title = nameList.get(2)
        } else if (position == 3) {
            title = nameList.get(3)
        }
        return title
    }
}
