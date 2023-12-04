package com.doviesfitness.ui.splash

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SliderOfflineImage(var activity: Activity,var introSliderLayouts: IntArray?)
   : androidx.viewpager.widget.PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            //  if (setData=="offline"){
          val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
         val view = layoutInflater.inflate(introSliderLayouts!![position], container, false)
       container.addView(view)
            return view



        }

        override fun getCount(): Int {

             return introSliderLayouts!!.size


        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj //as RelativeLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
            //  container.removeView(`object` as RelativeLayout)
        }
    }

