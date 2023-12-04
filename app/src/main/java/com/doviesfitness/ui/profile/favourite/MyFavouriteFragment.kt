package com.doviesfitness.ui.profile.favourite

import androidx.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doviesfitness.R
import com.doviesfitness.databinding.FragmentMyFavouriteBinding
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.HomeTabFragment
import com.doviesfitness.ui.profile.favourite.fragment.ExerciseFragment
import com.doviesfitness.ui.profile.favourite.fragment.FeedsFragment
import com.doviesfitness.ui.profile.favourite.fragment.WorkOutFragment
import com.doviesfitness.ui.profile.favourite.fragment.WorkOutPlanFragment
import com.doviesfitness.ui.profile.favourite.pagerAdapter.FavoriteAdapter
import eightbitlab.com.blurview.RenderScriptBlur

class MyFavouriteFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentMyFavouriteBinding
    private var fragments = mutableListOf<androidx.fragment.app.Fragment>()
    private lateinit var exerciseFragment: ExerciseFragment
    private lateinit var workOutFragment: WorkOutFragment
    private lateinit var workOutPlanFragment: WorkOutPlanFragment
    private lateinit var feedsFragment: FeedsFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  activity!!.window.decorView
        view.systemUiVisibility = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_favourite, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        fragments.clear()
        val windowBackground =  activity!!.window.decorView.background
        binding.topBlurView.setupWith(binding.containerId)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(mContext))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        exerciseFragment = ExerciseFragment()
        workOutFragment = WorkOutFragment()
        workOutPlanFragment = WorkOutPlanFragment()
        feedsFragment = FeedsFragment()
       // feedsFragment = HomeTabFragment()

        //list of fragment to india  viewpager

        fragments.add(exerciseFragment)
        fragments.add(workOutFragment)
        fragments.add(workOutPlanFragment)
        fragments.add(feedsFragment)

        val favoriteAdapter = FavoriteAdapter(childFragmentManager, fragments)
        binding.favViewPager.setAdapter(favoriteAdapter)
        binding.favViewPager.setOffscreenPageLimit(4)
        binding.favTablayout.setupWithViewPager(binding.favViewPager)

        setOnClick(binding.ivBack, binding.containerId)
    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.iv_back -> {
                activity!!.onBackPressed()
            }

            R.id.container_id -> {
                hideNavigationBar()
                val view =  activity!!.window.decorView
                view.systemUiVisibility = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }
}
