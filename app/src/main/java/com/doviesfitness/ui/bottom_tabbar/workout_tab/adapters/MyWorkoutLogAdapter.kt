package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.base.FooterLoader
import com.doviesfitness.ui.bottom_tabbar.home_tab.activity.WorkOutDetailActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ViewAndSaveLogImageActvity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.MyWorkoutLogModel
import com.doviesfitness.utils.ConvertUnits
import kotlinx.android.synthetic.main.my_workout_log_list_item.view.*

class MyWorkoutLogAdapter(
    context: Context,
    workoutList: ArrayList<MyWorkoutLogModel.Data>,
    listener: OnNoteClick,
    unitStr: String
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var context: Context
    var workoutList: ArrayList<MyWorkoutLogModel.Data>
    var listener: OnNoteClick
    var unitStr: String

    private var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2


    init {
        this.context = context
        this.workoutList = workoutList
        this.listener = listener
        this.unitStr = unitStr
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == workoutList.size - 1) {
            if (showLoader) VIEWTYPE_LOADER else VIEWTYPE_ITEM
        } else VIEWTYPE_ITEM
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (viewType) {
            VIEWTYPE_ITEM -> {
                return ViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.my_workout_log_list_item,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return FooterLoader(
                    LayoutInflater.from(context).inflate(
                        R.layout.new_pagination_view,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }


    override fun onBindViewHolder(
        rvHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        pos: Int
    ) {
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

        val workoutModal = workoutList.get(pos);
        holder.tvHeading.setText(workoutModal.workout_name)


        if (!unitStr!!.equals("") && unitStr!!.equals("Imperial")) {
            if (workoutModal.customer_weight.equals("0 Kg", true))
                holder.weight.setText("NA")
            else {

                var str = workoutModal.customer_weight
                var WArray = str.split(" ")
                //   var Wlbs= WArray[0].toInt()/0.45
                val inkgsFronLbs = ConvertUnits.getKgsToLbs(WArray[0].toInt())
                holder.weight.setText("" + inkgsFronLbs.toString() + " Lbs")
            }
        } else {
            if (workoutModal.customer_weight.equals("0 Kg", true))
                holder.weight.setText("NA")
            else
                holder.weight.setText(workoutModal.customer_weight)
        }
        holder.quality.setText(workoutModal.feedback_status)
        holder.date.setText(workoutModal.log_date)
        if (workoutModal.customer_calorie.isNotEmpty())
            holder.caloriText.setText(workoutModal.customer_calorie)
        else
            holder.caloriText.setText("NA")

        holder.workout_time.setText(workoutModal.workout_total_time)
        if (!workoutModal.workout_log_images.toString().isEmpty() && workoutModal.workout_log_images.toString().length > 0) {
            var  list= workoutModal.workout_log_images as ArrayList<LinkedHashMap<String, String>>
            if (list.size > 0)
            holder.images.setTextColor(context.resources.getColor(R.color.colorWhite))
        } else {
            holder.images.setTextColor(context.resources.getColor(R.color.colorGray11))
        }
        if (!workoutModal.workout_note.isEmpty()) {
            holder.notes.setTextColor(context.resources.getColor(R.color.colorWhite))
        } else {
            holder.notes.setTextColor(context.resources.getColor(R.color.colorGray11))

        }
        //  isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        if (!workoutModal.workout_image.isEmpty()) {
            Glide.with(holder.ivWorkout_pf).load(workoutModal.workout_image)
                .into(holder.ivWorkout_pf)
        }
/*
        holder.deleteLog.setOnClickListener {
            listener.onDelete(pos)
        }
*/

        holder.rltvContainer.setOnClickListener {
            listener.getWorkOutLogData(workoutModal, "1", pos)
        }

        holder.editDeleteIcon.setOnClickListener {
            listener.onEditDeleteClick( pos)
        }


        holder.notesLayout.setOnClickListener {
            if (workoutList.size > 0) {
                if (!workoutList.get(pos).workout_note.isEmpty()) {
                    listener.onNoteClick(workoutList.get(pos).workout_note)
                } else {
                    val intent = Intent(context, WorkOutDetailActivity::class.java)
                    intent.putExtra("PROGRAM_DETAIL", workoutList.get(pos).workout_id)
                    intent.putExtra("From_WORKOUTPLAN", "")
                    intent.putExtra("isMyWorkout", "yes")
                    intent.putExtra("fromDeepLinking", "")
                    context.startActivity(intent)
                }
            }
        }

        holder.galaryLayout.setOnClickListener {
            if (!workoutModal.workout_log_images.toString().isEmpty() && workoutModal.workout_log_images.toString().length > 0) {
               var  list= workoutModal.workout_log_images as ArrayList<LinkedHashMap<String, String>>
                if (list.size > 0) {
                    val intent = Intent(context, ViewAndSaveLogImageActvity::class.java)
                    intent.putExtra("image_list", workoutModal)
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(0, 0)
                }
            }else {
                val intent = Intent(context, WorkOutDetailActivity::class.java)
                intent.putExtra("PROGRAM_DETAIL", workoutModal.workout_id)
                intent.putExtra("isMyWorkout", "yes")
                intent.putExtra("fromDeepLinking", "")
                context.startActivity(intent)
            }
        }
    }


    fun showLoading(b: Boolean) {
        this.showLoader = b
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivWorkout_pf = view.iv_workout
        val tvHeading = view.tv_heading
        val weight = view.weight
        val workout_time = view.workout_time
        val quality = view.quality
        val images = view.images
        val date = view.date
        val notes = view.notes
        val notesLayout = view.notes_layout
        val galaryLayout = view.galary_layout
        //  val deleteLog = view.Mcdelete_post
        val rltvContainer = view.rltv_container
        //   val swipe = view.swipe
        val ivLock = view.iv_lock
        val editDeleteIcon = view.edit_delete_icon
        val caloriText = view.calori_text
    }

    interface OnNoteClick {
        public fun onNoteClick(workoutDescription: String)
        public fun onDelete(pos: Int)
        public fun onEditDeleteClick(pos: Int)
        public fun getWorkOutLogData(data: MyWorkoutLogModel.Data, whichClick: String, pos: Int)
    }
}