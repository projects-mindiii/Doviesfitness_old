package com.doviesfitness.ui.bottom_tabbar.workout_plan.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.ExerciseLibrary
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.WorkoutCollection

class PlansPagerAdapter : FragmentStatePagerAdapter {
    private var context: Context?
    var fragments: MutableList<Fragment>?

    constructor(fm: FragmentManager, context: Context?, fragments: MutableList<Fragment>) : super(fm) {
        this.context = context
        this.fragments=fragments
    }
    override fun getItem(position: Int):Fragment {
        return fragments!!.get(position)
    }
    override fun getCount(): Int {
        return fragments!!.size
    }
    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return context?.getString(R.string.workout_plan)
        } else {
            return context?.getString(R.string.diet_plan)
        }
    }
}