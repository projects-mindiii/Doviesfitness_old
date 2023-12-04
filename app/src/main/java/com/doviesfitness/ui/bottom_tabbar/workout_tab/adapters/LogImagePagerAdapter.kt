package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.LogImageModel


class LogImagePagerAdapter(
    context:Context,
    imageList: ArrayList<LogImageModel>
) : PagerAdapter() {
  var context:Context
    var imageList:ArrayList<LogImageModel>


    init {
        this.context = context
        this.imageList = imageList
    }


    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as FrameLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =  LayoutInflater.from(context).inflate(com.doviesfitness.R.layout.log_slider_image_item,container ,false)

        val imageView = itemView.findViewById(R.id.log_image) as ImageView
        val loader = itemView.findViewById(R.id.loader) as ProgressBar
  //      Glide.with(context).load(imageList.get(position).image).into(imageView)
        showImage(imageView,position,loader)

        container.addView(itemView)

        //listening to image click
/*
        imageView.setOnClickListener(object : View.OnClickListener() {
            fun onClick(v: View) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG)
                    .show()
            }
        })
*/

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }

    fun showImage(imageView:ImageView,position:Int,progress:ProgressBar){
        progress.visibility=View.VISIBLE
        Glide.with(context)
            .load(imageList.get(position).image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                    progress.visibility=View.GONE
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?,  p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    Log.d("image","OnResourceReady")
                    progress.visibility=View.GONE
                    return false
                }
            })
            .into(imageView)

    }
}


