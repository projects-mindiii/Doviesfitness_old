package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.WorkoutFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.FavoriteExerciseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments.WorkoutCollection

class AddExercisePagerAdapter : FragmentStatePagerAdapter {
    private var context: Context?
    private var fragments: List<Fragment>? = null

    constructor(fm: FragmentManager, context: Context?, fragments :List<Fragment> ) : super(fm) {
        this.context = context
        this.fragments = fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments!![position]/*
        if (position == 0)
            return ExerciseLibrary.newInstance("create");
        else

            return FavoriteExerciseFragment()
    */}

    override fun getCount(): Int {
        return fragments!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return context?.getString(R.string.exercises_library)
        } else {

            return context?.getString(R.string.favorite_exercises)
        }
    }

}