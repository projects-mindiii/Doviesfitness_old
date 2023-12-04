package com.doviesfitness.ui.bottom_tabbar.home_tab.adapter

 import android.content.Context
 import android.util.Log

 import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
 import com.bumptech.glide.Glide
 import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.FeaturedNews
 import com.doviesfitness.utils.Utility
 import kotlinx.android.synthetic.main.featured_view.view.*

class FeatureAdapter(context: Context, featureList: ArrayList<FeaturedNews>,listener : FeaturedItemClick) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FeatureAdapter.MyViewHolder>() {
    var mContext: Context
    var featureList: ArrayList<FeaturedNews>
    var listener : FeaturedItemClick

    init {
        this.mContext = context
        this.featureList = featureList
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.featured_view, parent, false))


    }

    override fun getItemCount(): Int {
        return featureList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        val featuredNewsData = featureList[i]

      /*  val screenWidth = mContext.resources.displayMetrics.widthPixels
        val partWidth = screenWidth / 3.8
        holder.fl_featured.layoutParams.width= partWidth.toInt();
        holder.fl_featured.layoutParams.height = (partWidth*1.5).toInt();*/



       /* var width = Utility.getScreenWidthDivideIntoThreeAndHlf(mContext)
        holder.fl_featured.layoutParams.width = (width /.7  ).toInt()
        holder.fl_featured.layoutParams.height = (width *1.5).toInt()*/
///.65
       /*
        TODO devendra

        val screenWidth = mContext.resources.displayMetrics.widthPixels
        val partWidth = screenWidth / 3.7
        holder.fl_featured.layoutParams.width= partWidth.toInt();
        holder.fl_featured.layoutParams.height = (partWidth*1.4).toInt();
*/
        Glide.with(mContext).load(featuredNewsData.news_image).into(holder.ivFeatured)
        holder.tvFeatured.text = featuredNewsData.news_title


        if (featuredNewsData.is_new == "1"){
            holder.tvNew.visibility = View.VISIBLE
        }else{
            holder.tvNew.visibility = View.GONE
        }

        holder.ivFeatured.setOnClickListener{

            listener.featureItemOnClick(featuredNewsData)
        }

    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val  ivFeatured = view.iv_featured
        val  tvFeatured = view.tv_featured
        val  tvNew = view.tv_new
        val  mainLayout = view.main_layout
        val  fl_featured = view.fl_featured


      //  static let cellPadding : CGFloat = 60.0
     //   static let cellWidth : CGFloat = (ScreenSize.width - NewsFeedViewController.cellPadding)/2

    }

    interface FeaturedItemClick{
        fun featureItemOnClick(featuredNews: FeaturedNews)

    }

}
