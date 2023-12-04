package com.doviesfitness.ui.splash

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.diet_plan.adapter.NewDietPlanCategoryAdapter
import com.doviesfitness.ui.bottom_tabbar.diet_plan.modal.DietPlanCateGoriesModal
import com.doviesfitness.ui.splash.model.Data
import java.util.*
import kotlin.collections.ArrayList

class SliderOnlineImage(var imageList: ArrayList<Data>,var  context: Activity) :
    androidx.viewpager.widget.PagerAdapter() {



    override fun instantiateItem(container: ViewGroup, position: Int): Any {


        val LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = LayoutInflater.inflate(R.layout.slider_image_layout, container, false)

        val imageView: ImageView = view.findViewById<View>(R.id.image_view) as ImageView

        val currentImg = imageList[position]

        Glide.with(context).load(currentImg.mobileImage).into(imageView)


        Objects.requireNonNull(container).addView(view)
        return view


    }

    override fun getCount(): Int {


        return imageList!!.size


    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as RelativeLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as RelativeLayout)
    }
}

