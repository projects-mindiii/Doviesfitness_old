package com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperAdapter
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperViewHolder
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.WeightInKgOrLbsDialog
import com.doviesfitness.utils.WorkoutLevelDialog
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import kotlinx.android.synthetic.main.set_and_reps_view_item_design.view.*
import java.util.Collections


class SelectedExerciseAdapterSetAndReps(
    val context: Context,
    val exerciseListing: ArrayList<ExerciseListingResponse.Data>,
    val listener: OnItemClick,
    val videowidth: Int,
    val timeListener: SelectTime,
    val mDragStartListener: OnStartDragListener,
    val supportFragmentManager: FragmentManager,
    var listner: (Pair<String, Int>) -> Unit

) : RecyclerView.Adapter<SelectedExerciseAdapterSetAndReps.ExerciseView>(),
    ItemTouchHelperAdapter {

    init {
        setHasStableIds(true)
    }

    private var player: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    var tempPos = -1
    private var mLastClickTime: Long = 0
    var tempHolder: ExerciseView? = null

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(exerciseListing, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
    }

    override fun onItemDismiss(position: Int) {
        //TODO To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ExerciseView {
        return ExerciseView(
            LayoutInflater.from(p0.context!!).inflate(
                R.layout.set_and_reps_view_item_design,
                p0,
                false
            )
        )
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getItemCount(): Int {
        return exerciseListing.size
        // return 2
    }

    override fun onBindViewHolder(rvHolder: ExerciseView, pos: Int) {
        val holder = rvHolder
        val exercise = exerciseListing.get(pos)
        holder.tv_exercise.text = exerciseListing[pos].exercise_name
        Glide.with(holder.iv_exercise.context).load(exerciseListing[pos].exercise_image)
            .into(holder.iv_exercise)



        if (exercise.isSelectedExercise)
            Glide.with(holder.iv_exercise.context).load(R.drawable.selected_circle_blue_)
                .into(holder.deselect_workout_exercise)
        else
            Glide.with(holder.iv_exercise.context).load(R.drawable.deselected_create_workout)
                .into(holder.deselect_workout_exercise)



        if (exercise.isExercisePopupVisible) {
            rvHolder.llExercisePopup.visibility = View.VISIBLE
        } else {
            rvHolder.llExercisePopup.visibility = View.GONE
        }


        if (exerciseListing.get(pos).selected_exercise_reps_number != null && exerciseListing.get(
                pos
            ).selected_exercise_reps_number!!.isNotEmpty()&& exerciseListing.get(
                pos
            ).selected_exercise_reps_number!="0"
        )
            holder.timeExerciseTxt.text =
                exerciseListing.get(pos).selected_exercise_reps_number /*+ " Reps"*/
        else {
            holder.timeExerciseTxt.text = "Select"
        }



        if (exerciseListing[pos].selected_exercise_weight_number != null &&
            exerciseListing[pos].selected_exercise_weight_number!!.isNotEmpty() &&
            exerciseListing[pos].selected_exercise_weight_number!="0"
        )
            holder.repetetionTxt.text = "${exerciseListing[pos].selected_exercise_weight_number}"
        else {
            holder.repetetionTxt.text = "Select"
        }

        /**updating view select or deselect*/

        hideViewWhenClickOutOfView(rvHolder.itemView, rvHolder.llExercisePopup)
        if (exerciseListing.size == 1)
            rvHolder.scroll_top_bottom.visibility = View.GONE
        else
            rvHolder.scroll_top_bottom.visibility = View.VISIBLE


        rvHolder.iv_exercise_popup.setOnClickListener {
            var flag=exerciseListing[pos].isExercisePopupVisible
            val pair = Pair("hide", pos)
            listner(pair)
            try {
                exerciseListing.forEachIndexed { index, it ->

                    if (it.isExercisePopupVisible) {
                        it.isExercisePopupVisible = false
                        notifyItemChanged(index)
                    }
                    if (index == pos) {
                        exerciseListing[pos].isExercisePopupVisible = !flag
                        notifyItemChanged(index)
                    }

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        rvHolder.iv_duplicate.setOnClickListener {
            exerciseListing.add(addDuplicateExercise(exerciseListing[pos]))
            exerciseListing[pos].isExercisePopupVisible = false
            notifyDataSetChanged()
            timeListener.checkRoundStatus(pos, "duplicate")
        }
        rvHolder.iv_replace.setOnClickListener {
             listener.forExchangeItem(exerciseListing[pos], pos)
        }
        rvHolder.iv_delete.setOnClickListener {
            CommanUtils.deletePopupForSetAndReps(
                context,
                "Are you sure you want to delete this exercise?"
            ) {
                exerciseListing.removeAt(pos)
                notifyDataSetChanged()
                it.dismiss()
                timeListener.checkRoundStatus(pos, "delete")
            }
        }
    }


    inner class ExerciseView(view: View) : RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {
        val ivHideControllerButton: LinearLayout by lazy {
            view.findViewById<LinearLayout>(R.id.controller)
        }
        val tv_exercise = view.tv_exercise
        val iv_exercise = view.iv_exercise
        val timeLayout = view.time_layout
        val repsNumberLayout = view.reps_number_layout
        val iv_lock = view.iv_lock
        val fl_vv = view.fl_vv
        val deselect_workout_exercise = view.deselect_workout_exercise
        val timeExerciseTxt = view.time_exercise_txt
        val repetetionTxt = view.repetetion_txt
        val iv_exercise_popup = view.iv_exercise_popup
        val arrow_icon1 = view.timer_exercise_layout
        val arrow_icon2 = view.repetetion_layout
        val scroll_top_bottom = view.scroll_top_bottom
        val iv_share = view.iv_share
        val llExercisePopup = view.llExercisePopup
        val iv_duplicate = view.ll_duplicate_round
        val iv_delete = view.ll_delete
        val iv_replace = view.ll_add_exercise
        val rl_exercise = view.rl_exercise


        override fun onItemSelected() {
        }

        override fun onItemClear() {

        }

        init {
            scroll_top_bottom.setOnLongClickListener {
                mDragStartListener.onStartDrag(this@ExerciseView)
                false;
            }
            deselect_workout_exercise.setOnClickListener {
                // selecting particular exercise
                exerciseListing[adapterPosition].isSelectedExercise = !exerciseListing[adapterPosition].isSelectedExercise
                 notifyDataSetChanged()
                 timeListener.selectExercise(adapterPosition)
            }

            arrow_icon1.setOnClickListener {
                /** select reps here*/
                var reps = timeExerciseTxt.text.toString()
                var selectedReps = "0"
                if (reps.contains(" ")) {
                    selectedReps = reps.split(" ")[0]
                } else if (reps == "Select")
                    selectedReps = "01"
                else
                    selectedReps = reps
                WorkoutLevelDialog.newInstance(
                    getValueForReps(),
                    object : WorkoutLevelDialog.HeightWeightCallBack {
                        override fun levelOnClick(index: Int, str: String) {
                            if (exerciseListing.isNotEmpty() && adapterPosition >= 0) {
                                exerciseListing[adapterPosition].selected_exercise_reps_number = str
                                timeExerciseTxt.text = "$str"
                            }
                        }
                    },
                    context,
                    selectedReps.toInt()
                ).show(supportFragmentManager)


            }
            arrow_icon2.setOnClickListener {
                /** select weight here*/
                var weight=repetetionTxt.text.toString().trim()
                var sendingWeightToPopup="01"
                var untie="lbs"
                if (weight.contains(" ")){
                    sendingWeightToPopup=weight.split(" ")[0]
                    untie= weight.split(" ")[1]
                }else if (weight=="Select"){
                    sendingWeightToPopup="01"
                    untie="lbs"
                }
                WeightInKgOrLbsDialog.newInstance(getValueforWeight(), object :
                    WeightInKgOrLbsDialog.WeightInKgOrLbsDialogCallBack {
                    override fun timeOnClick(index: Int, value: String, index1: Int, value1: String) {
                        var unite=""
                        if (value1=="01"){
                            unite="lbs"
                        }else if (value1=="02"){
                            unite="Kg"
                        }
                        exerciseListing[adapterPosition].selected_exercise_weight_number = "$index $unite"
                        repetetionTxt.text="$index $unite"
                    }
                }, context, sendingWeightToPopup, untie)
                    .show(supportFragmentManager)
            }


            iv_share.setOnClickListener {
                Toast.makeText(context, "iv_share", Toast.LENGTH_SHORT).show()
            }


            iv_lock.setOnClickListener {
                val locationOnScreen = IntArray(2)

                iv_lock.getLocationOnScreen(locationOnScreen)

                //Convert Position to Px
                val yInDp = locationOnScreen[1].toFloat() // Replace with your Y position in dp
                val yPositionOnScreen: Float
                val scale: Float = context.resources.displayMetrics.density
                yPositionOnScreen = yInDp * scale

                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!! == "Yes"
                ) {
                    listener.buttonPlayPauseVideo(adapterPosition, this, exerciseListing,yPositionOnScreen)

                } else {
                    if (exerciseListing[adapterPosition].exercise_access_level == "OPEN") {
                        listener.buttonPlayPauseVideo(adapterPosition, this, exerciseListing,yPositionOnScreen)
                    }
                }
            }
        }
    }

    fun hideViewWhenClickOutOfView(overlayview_: View, llExercisePopup: LinearLayout) {

        overlayview_.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
//                    exerciseListing.forEach { it.isExercisePopupVisible = false }
//                    notifyDataSetChanged()
                    val pair = Pair("hideTouchOutside", -1)
                    listner(pair)

                 /*   exerciseListing.forEachIndexed { index, it ->
                        if (it.isExercisePopupVisible) {
                            it.isExercisePopupVisible = false
                            notifyItemChanged(index)
                        }
                        if (tempHolder != null)
                            if (index == tempHolder!!.adapterPosition) {
                                exerciseListing[tempHolder!!.adapterPosition].isExercisePopupVisible =
                                    !exerciseListing[tempHolder!!.adapterPosition].isExercisePopupVisible
                                notifyItemChanged(index)
                            }
                    }*/


                    true // Consume the touch event
                }

                else -> false
            }
        }
    }


    interface OnItemClick {
        fun videoPlayClick(
            isScroll: Boolean,
            data: ExerciseListingResponse.Data,
            position: Int,
            view: ExerciseView,
            isLoad: Boolean
        )

        fun buttonPlayPauseVideo(
            adapterPosition: Int,
            exerciseView: ExerciseView,
            exerciseListing: ArrayList<ExerciseListingResponse.Data>,
            yPositionOnScreen: Float
        )

        fun forExchangeItem(exerciseData: ExerciseListingResponse.Data, pos: Int)
    }

    fun addDuplicateExercise(data: ExerciseListingResponse.Data): ExerciseListingResponse.Data {
        var data1 = ExerciseListingResponse.Data(
            data.exercise_level,
            data.exercise_amount,
            data.exercise_amount_display,
            data.exercise_body_parts,
            data.exercise_description,
            data.exercise_equipments,
            data.exercise_id,
            data.exercise_image,
            data.exercise_is_favourite,
            data.exercise_level,
            data.exercise_name,
            data.exercise_share_url,
            data.exercise_tags,
            data.exercise_video,
            data.is_liked,
            false,
            false, false,
            data.exercise_timer_time,
            data.exercise_reps_time,
            data.exercise_reps_number,
            "",
            "",
            data.exercise_rest_time,
            false,
            false,
            false,
            0,
            false,
            data.leftAndRightOrSuperSetOrAddExercise,
            false
        )

        return data1.deepCopy()
    }

    interface SelectTime {
        public fun selectTime(timing: String, pos: Int, s: String, s1: String)
        public fun selectRepetition(timing: String, pos: Int, exerciseRepsNumber: String)
        public fun deleteExercise(pos: Int)
        public fun copyExercise(pos: Int)
        public fun selectExercise(pos: Int)
        public fun replaceExercise(pos: Int)
        public fun checkRoundStatus(adapterPosition: Int, s: String)
    }

    private fun getValueForReps(): java.util.ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 1..100) {
            if (i < 10) {
                values.add("0$i")
            } else {
                values.add("" + i)
            }
        }
        return values
    }

    private fun getValue(): java.util.ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 1..8) {
            if (i < 10) {
                values.add("0$i")
            } else {
                values.add("" + i)
            }
        }
        return values
    }

    private fun getValueforWeight(): java.util.ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 1..600) {
            if (i < 10) {
                values.add("0$i")
            } else {
                values.add("" + i)
            }
        }
        return values
    }




}