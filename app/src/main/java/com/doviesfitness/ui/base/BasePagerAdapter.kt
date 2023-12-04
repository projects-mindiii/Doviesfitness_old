package com.doviesfitness.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doviesfitness.data.model.FragmentPagerModel

class BasePagerAdapter(fm: androidx.fragment.app.FragmentManager, context : Context?, fragments: ArrayList<FragmentPagerModel>)
    : androidx.fragment.app.FragmentPagerAdapter(fm) {
    var context : Context ?= null
    var fragment : ArrayList<FragmentPagerModel> ? = null
    init {
        this.context = context
        this.fragment = fragments
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return fragment!!.get(position).fragment
    }

    override fun getCount(): Int {
        return  fragment!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragment?.get(position)?.fragmentName
    }
}