package com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.DownloadsStreamActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import kotlinx.android.synthetic.main.collection_group_item_new_view.view.*

class DownLoadStreamWorkOutAdapter(
    context: Context,
    fromWhich: Boolean,/* favStreamList: ArrayList<LocalStreamVideoDataModal>,*/
    downloadedList: ArrayList<DownloadedModal>,
    onWorkOutClickListener: OnWorkOutClickListener,
    admin: String,
    subListener: IsSubscribed
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<DownLoadStreamWorkOutAdapter.MyViewHolder>() {
    private var context: Context
   // var downloadList: ArrayList<LocalStreamVideoDataModal>
    var downloadedList: ArrayList<DownloadedModal>
    var onWorkOutClickListener: OnWorkOutClickListener
    var fromWhich: Boolean = false
    var admin=""
    var subListener:IsSubscribed

    init {
        this.context = context
      //  this.downloadList = favStreamList
        this.onWorkOutClickListener = onWorkOutClickListener
        this.fromWhich = fromWhich
        this.admin=admin
        this.downloadedList=downloadedList
        this.subListener=subListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

      val videoModal = downloadedList.get(pos)

        if(fromWhich){
            holder.deleteIcon.visibility = View.VISIBLE
            holder.lockIcon.visibility=View.GONE
        }
        else{
            holder.deleteIcon.visibility = View.GONE
            if ("Yes".equals(admin) ) {
                holder.lockIcon.visibility=View.GONE
            }
            else {

                if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                        "0"
                    )){
                    holder.lockIcon.visibility=View.GONE
                }
                else {
                    holder.lockIcon.visibility=View.VISIBLE
                }
            }

        }

        /*if ("Yes".equals(admin) ) {
           holder.lockIcon.visibility=View.GONE
        }
        else {

            if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                    "0"
                )){
                holder.lockIcon.visibility=View.GONE
            }
            else {
                holder.lockIcon.visibility=View.VISIBLE
            }
        }*/

        holder.exerciseName.text = videoModal.stream_workout_name
        if (videoModal.stream_workout_image_url != null && videoModal.stream_workout_image != null)
            Glide.with(context)
//                .load(videoModal.stream_workout_image_url + "medium/" + videoModal.stream_workout_image)
                .load(videoModal.stream_workout_image_url +  videoModal.stream_workout_image)
                .error(ContextCompat.getDrawable(context, R.drawable.app_icon))
                .into(holder.img)

        holder.img.setOnClickListener {

            if ("Yes".equals(admin) ) {
                onWorkOutClickListener.onClickWorkOut("notDelete",pos)
            }
            else {

                if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                        "0"
                    )){
                    onWorkOutClickListener.onClickWorkOut("notDelete",pos)
                }
                else {
                    subListener.isSubscribed()
                }
            }



        }

        holder.deleteIcon.setOnClickListener {
            onWorkOutClickListener.onDeleteWorkOut(pos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(
            //LayoutInflater.from(context).inflate(R.layout.stream_collection_workout_item_view, parent, false)
            LayoutInflater.from(context).inflate(R.layout.collection_download_group_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return downloadedList.size
    }

    class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val img = view.iv_featured
        val exerciseName = view.tv_featured
        val itemLayout = view.item_layout
        val tvLevel = view.tv_level
        val tvNewStream = view.tv_new_stream
        val deleteIcon = view.delete_icon
        val lockIcon = view.lock_icon
    }


     interface OnWorkOutClickListener{
        fun onClickWorkOut(  status : String,pos:Int)
        fun onDeleteWorkOut( pos:Int)
    }
}
