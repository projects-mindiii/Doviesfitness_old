package com.doviesfitness.ui.profile.favourite.pagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FavoriteAdapter(fm: androidx.fragment.app.FragmentManager, var fragments:List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        var title: String? = null
        if (position == 0) {
            title = "Exercise"
        } else if (position == 1) {
            title = "Workout"
        } else if (position == 2) {
            title = "Workout Plan"
        }else if (position == 3) {
            title = "Feeds"
        }
        return title
    }
}
