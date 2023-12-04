package com.doviesfitness.temp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.CollectionDownloadGroupItemViewBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.temp.modal.VideoCategoryModal
import kotlinx.android.synthetic.main.collection_group_item_new_view.view.*

/**Created by Yashank Rathore on 18,December,2020 yashank.mindiii@gmail.com **/

abstract class TempDownLoadStreamWorkOutAdapter(
    val videoCategoryList: MutableList<VideoCategoryModal>,
    var fromWhich: Boolean,
    val subListener: IsSubscribed
) :
    RecyclerView.Adapter<TempDownLoadStreamWorkOutAdapter.CategoryVideoHolder>() {
    private lateinit var context: Context
    val isAdmin by lazy {
        Doviesfitness.getDataManager()
            .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
    }
    val isSubscribed by lazy {
        Doviesfitness.getDataManager()
            .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)
    }

    inner class CategoryVideoHolder(val binding: CollectionDownloadGroupItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivFeatured.setOnClickListener {
                if ("Yes".equals(isAdmin)) {
                    onClickWorkOut("notDelete", adapterPosition)
                } else {
                    if (!isSubscribed.equals(
                            "0"
                        )
                    ) {
                        onClickWorkOut("notDelete", adapterPosition)
                    } else {
                        subListener.isSubscribed()
                    }
                }
            }

            binding.deleteIcon.setOnClickListener {
                onDeleteWorkOut(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVideoHolder {
        context = parent.context
        val binding = DataBindingUtil.inflate<CollectionDownloadGroupItemViewBinding>(
            LayoutInflater.from(context),
            R.layout.collection_download_group_item_view, parent, false
        )
        return CategoryVideoHolder(binding)
    }

    override fun getItemCount() = videoCategoryList.size

    override fun onBindViewHolder(holder: CategoryVideoHolder, position: Int) {
        val modal = videoCategoryList[position]
        if (fromWhich) {
            holder.binding.deleteIcon.visibility = View.VISIBLE
            holder.binding.lockIcon.visibility = View.GONE
        } else {
            holder.binding.deleteIcon.visibility = View.GONE
            if ("Yes".equals(isAdmin)) {
                holder.binding.lockIcon.visibility = View.GONE
            } else {
                if (!Doviesfitness.getDataManager()
                        .getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                        .equals(
                            "0"
                        )
                ) {
                    holder.binding.lockIcon.visibility = View.GONE
                } else {
                    holder.binding.lockIcon.visibility = View.VISIBLE
                }
            }
        }
        if((!modal.stream_workout_name.isEmpty())) {
            holder.binding.rlGrdient.setBackgroundResource(R.drawable.stream_workout_gradient_bg)
        }
            holder.binding.tvFeatured.text = modal.stream_workout_name
        if (modal.stream_workout_image_url != null && modal.stream_workout_image != null)
            Glide.with(context)
//                .load(modal.stream_workout_image_url + "medium/" + modal.stream_workout_image)
                .load(modal.stream_workout_image_url + modal.stream_workout_image)
                .error(ContextCompat.getDrawable(context, R.drawable.app_icon))
                .into(holder.binding.ivFeatured)
    }

    fun refreshList(list: List<VideoCategoryModal>?) {
        list?.let {
            if(videoCategoryList.size>0) videoCategoryList.clear()
            videoCategoryList.addAll(list)
            Log.d("fasnkfnkas", "refreshList: ${list.size} ATER ${videoCategoryList.size} ")
        }
        notifyDataSetChanged()
    }

    public fun showDeleteIcon(fromWhich: Boolean){
        this.fromWhich = fromWhich
        notifyDataSetChanged()
    }
    fun deleteItem(itemPos: Int){
        videoCategoryList.removeAt(itemPos)
        notifyItemChanged(itemPos)
    }

    abstract fun onClickWorkOut(status: String, pos: Int)
    abstract fun onDeleteWorkOut(pos: Int)
}