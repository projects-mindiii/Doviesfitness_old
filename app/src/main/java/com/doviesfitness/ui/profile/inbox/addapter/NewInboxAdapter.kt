package com.doviesfitness.ui.profile.inbox.addapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.profile.inbox.modal.NotificationModel
import kotlinx.android.synthetic.main.diet_p_sub_category_view.view.rltv_container
import kotlinx.android.synthetic.main.inbox_view.view.*
import androidx.core.text.HtmlCompat
import com.doviesfitness.R
import kotlinx.android.synthetic.main.inbox_view.view.Mcdelete_post
import kotlinx.android.synthetic.main.inbox_view.view.tv_heading
import kotlinx.android.synthetic.main.my_workout_log_list_item.view.*


class NewInboxAdapter(context: Context, notificationList: ArrayList<NotificationModel.Data>, inboxClickListener: InboxClickListener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context
    var notificationList: ArrayList<NotificationModel.Data>
    var inboxClickListener: InboxClickListener

    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2

    init {
        this.context = context
        this.notificationList = notificationList
        this.inboxClickListener = inboxClickListener
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == notificationList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.inbox_view, parent, false))
            }
            else -> {
                return FooterLoader(LayoutInflater.from(context).inflate(R.layout.new_pagination_view, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, pos: Int) {
        if (rvHolder is FooterLoader) {
            val loaderViewHolder = rvHolder as FooterLoader
            if (showLoader) {
                loaderViewHolder.mProgressBar.visibility = View.VISIBLE
            } else {
                loaderViewHolder.mProgressBar.visibility = View.GONE
            }
            return
        }
        val holder = rvHolder as ViewHolder

        val notificationModal = notificationList.get(pos);

        holder.tvDate.setText(notificationModal.notification_date)


        if ("News_feed".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText("Feed").toString().trim()
        } else if ("Exercise".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText("Exercise").toString().trim()
        } else if ("Workout".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText("Workout").toString().trim()
        }
        else if ("Program_plan".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText("Workout Plan").toString().trim()
        }
        else if ("Stream_workout".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText("Stream Workout").toString().trim()
        }
        else if ("custom".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText(notificationModal.notification_title)
        }
        else if ("WelCome".equals(notificationModal.notification_code)) {
            holder.tvHeading.setText(notificationModal.notification_title).toString().trim()
        }
        else{
            holder.tvHeading.setText(notificationModal.notification_title).toString().trim()
        }


        if (!notificationModal.notification_image.isEmpty()) {
            Glide.with(holder.ivImageView).load(notificationModal.notification_image).into(holder.ivImageView)
        }

        if ("Unread".equals(notificationModal.notification_status)) {
            holder.txtNotificationRead.visibility = View.VISIBLE
        } else {
            holder.txtNotificationRead.visibility = View.GONE
        }

        if ("custom".equals(notificationModal.notification_code) || "WelCome".equals(notificationModal.notification_code)) {

            var notification_message  = HtmlCompat.fromHtml(notificationModal.notification_message, HtmlCompat.FROM_HTML_MODE_LEGACY);
            holder.tvDiscribtions.setText(notification_message)
        } else {
            holder.tvDiscribtions.setText(notificationModal.notification_message)
        }

        holder.rltvContainer.setOnClickListener {
            inboxClickListener.getInboxInfo(notificationModal, pos, "")
        }

        holder.deleteLog.setOnClickListener() {
            inboxClickListener.deleteInboxItem(notificationModal, pos, "Delete")
        }

    }

    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val rltvContainer = view.rltv_container
        val tvHeading = view.tv_heading
        val tvDiscribtions = view.tv_discribtions
        val tvDate = view.tv_date
        val ivImageView = view.iv_image_view
        val txtNotificationRead = view.txt_notification_read
        val deleteLog = view.Mcdelete_post
    }


    interface InboxClickListener {
        fun getInboxInfo(data: NotificationModel.Data, position: Int, whichClick: String)
        fun deleteInboxItem(data: NotificationModel.Data, position: Int, whichClick: String)
    }
}
