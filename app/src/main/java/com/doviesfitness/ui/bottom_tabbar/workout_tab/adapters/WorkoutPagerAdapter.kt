package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.WorkoutFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.ExerciseLibrary
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.WorkoutCollection

class WorkoutPagerAdapter : androidx.fragment.app.FragmentStatePagerAdapter {
    private var context: Context?

    constructor(fm: androidx.fragment.app.FragmentManager, context: Context?) : super(fm) {
        this.context = context
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        if (position == 0)
            return ExerciseLibrary();
        else
            return WorkoutCollection()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return context?.getString(R.string.exercises_library)
        } else {
            return context?.getString(R.string.workout_collection)
        }
    }

}