package com.doviesfitness.ui.new_player.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
 import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperAdapter
import com.doviesfitness.ui.bottom_tabbar.rv_swap.ItemTouchHelperViewHolder
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetAndRepsModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetSModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.WorkoutLevelDialog
import com.doviesfitness.utils.WrapsDialog
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.player_set_and_reps_exercise_item_view.view.*
import java.io.File

class PlayerRoundAdapter(
    val context: Context,
    val SetsAndRepsRoundList: ArrayList<SetAndRepsModel>,
    val listener: OnItemClick,
    val videowidth: Int,
    val timeListener: SelectTime,
    val mDragStartListener: OnStartDragListener,
    val stopScroll: StopScroll,
    val supportFragmentManager: FragmentManager
) : RecyclerView.Adapter<PlayerRoundAdapter.MyView>(),
    ItemTouchHelperAdapter {
    init {
        setHasStableIds(true)
    }

    private var selectedPosition = -1
    var exerciseListing = java.util.ArrayList<ExerciseListingResponse.Data>()
    lateinit var mItemTouchHelper: ItemTouchHelper
    var tempPos = -1
    lateinit var tempHolder: NewPlayerExerciseSetAndRepsAdapter.ExerciseView
    lateinit var setsAdapter: PlayerSetsAdapter
    var flag = true
    private var player: SimpleExoPlayer? = null
    var adapter: NewPlayerExerciseSetAndRepsAdapter? = null
    var list = ArrayList<ExerciseListingResponse.Data>()
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
    var tempRoundPos = -1
    var tempExerciseItem: ExerciseListingResponse.Data? = null
    var exerciseList = ArrayList<ExerciseListingResponse.Data>()
    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    override fun onItemDismiss(position: Int) {

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyView {
        return MyView(
            LayoutInflater.from(p0.context!!).inflate(
                R.layout.player_set_and_reps_exercise_item_view,
                p0,
                false
            )
        )
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getItemCount(): Int {
        return SetsAndRepsRoundList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(rvHolder: MyView, pos: Int) {
        val exercise = SetsAndRepsRoundList[pos]
        try {
            if (exercise.isRoundLogSetCompletedForPlayer) {
                rvHolder.tv_logStatus.text = "Set Completed"
                rvHolder.tv_logStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGray6));

                //  rvHolder.ic_set_completed_tick.visibility = View.VISIBLE
            } else {
                rvHolder.tv_logStatus.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

                rvHolder.tv_logStatus.text = "Log Set"
                // rvHolder.ic_set_completed_tick.visibility = View.GONE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (exercise.isRoundSelectedForPlayer) {
            rvHolder.main_root_layout.setBackgroundColor(Color.parseColor("#242424"));
            rvHolder.rl_logset.visibility = View.VISIBLE
           /* Glide.with(rvHolder.play_paus_icon.context).load(R.drawable.ic_pause_ico)
                .into(rvHolder.play_paus_icon)*/
            rvHolder.play_paus_icon.rotation = 0f
            rvHolder.llTargetSetAndRepsCount.visibility=View.VISIBLE
            rvHolder.tv_set_and_reps_count.visibility=View.GONE
            rvHolder.play_paus_icon.visibility=View.GONE
            rvHolder.tv_set_and_reps_count.text =""
        } else {
            rvHolder.llTargetSetAndRepsCount.visibility=View.GONE
            rvHolder.tv_set_and_reps_count.visibility=View.VISIBLE
            rvHolder.play_paus_icon.visibility=View.VISIBLE
            rvHolder.tv_set_and_reps_count.text =
                "${exercise.strTargetSets} Sets Of ${exercise.strTargetReps} Reps"
            rvHolder.main_root_layout.setBackgroundColor(Color.parseColor("#000000"));
            rvHolder.rl_logset.visibility = View.GONE
            Glide.with(rvHolder.play_paus_icon.context).load(R.drawable.down_arrow_ico_fill)
                .into(rvHolder.play_paus_icon)
            rvHolder.play_paus_icon.rotation = 270.0f
        }


        if (  SetsAndRepsRoundList[pos].arrSets[0].exerciseList.size ==2) {
            rvHolder.angularIcon1.visibility = View.VISIBLE
            rvHolder.angularIcon2.visibility = View.VISIBLE
        } else {
            rvHolder.angularIcon1.visibility = View.GONE
            rvHolder.angularIcon2.visibility = View.GONE
        }


        rvHolder.tv_reounds.text = "Round ${pos + 1}"
        if (exercise.strRoundName.isNotEmpty())
            rvHolder.tv_exercise_collection_type.text = "- ${exercise.strRoundName}"
        else
            rvHolder.tv_exercise_collection_type.text = ""

        rvHolder.tv_sets_text.text = "${exercise.strTargetSets} Set"
        rvHolder.repetetion_txt.text = "${exercise.strTargetReps}"

        if (exercise.isRoundPositionPopupOIsVisible) {
            rvHolder.llRoundPopup.visibility = View.VISIBLE
        } else {
            rvHolder.llRoundPopup.visibility = View.GONE
        }

        if (exercise.isRoundSelectedForPlayer) {
            rvHolder.down_arrow_icon_sets.visibility = View.VISIBLE
            rvHolder.down_arrow_icon_reps.visibility = View.VISIBLE

            rvHolder.arrow_icon_sets.background = ContextCompat.getDrawable(context, R.drawable.edit_text_bg_gray_selected_round);
            rvHolder.arrow_icon_reps.background = ContextCompat.getDrawable(context, R.drawable.edit_text_bg_gray_selected_round);


        } else {
             rvHolder.arrow_icon_sets.background = null
             rvHolder.arrow_icon_reps.background = null

            //rvHolder.arrow_icon_sets.background = ContextCompat.getDrawable(context, R.drawable.edit_text_bg_gray_selected_round);
           // rvHolder.arrow_icon_reps.background = ContextCompat.getDrawable(context, R.drawable.edit_text_bg_gray_selected_round);

            rvHolder.down_arrow_icon_sets.visibility = View.GONE
            rvHolder.down_arrow_icon_reps.visibility = View.GONE

        }

        if (exercise.arrSets.size > 4)
            rvHolder.iv_nextSetIcon.visibility = View.VISIBLE
        else
            rvHolder.iv_nextSetIcon.visibility = View.GONE

        rvHolder.iv_nextSetIcon.setOnClickListener {
            try {
                if (exercise.arrSets.isNotEmpty()) {
                    rvHolder.iv_PreviousSetIcon.visibility = View.VISIBLE
                    val layoutManager = rvHolder.rv_sets.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition() + 1
                    val remainingObjcts = (exercise.arrSets.size) - lastVisibleItemPosition
                    if (remainingObjcts > 4) {
                        rvHolder.rv_sets.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition() + 3)
                    } else
                        try {
                            rvHolder.iv_nextSetIcon.visibility = View.GONE
                            rvHolder.rv_sets.smoothScrollToPosition(exercise.arrSets.size)
                        } catch (e: Exception) {
                            rvHolder.iv_nextSetIcon.visibility = View.GONE
                            e.printStackTrace()
                        }
                }
                showHideNextButton(rvHolder, exercise)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        rvHolder.rl_logset.setOnClickListener {
            if (exercise.isRoundLogSetCompletedForPlayer) {
                exercise.isRoundLogSetCompletedForPlayer = false
                exercise.arrSets.forEachIndexed { index, setSModel ->
                    if (setSModel.isSelected) {
                        setSModel.isRoundCompleted = false
                        // rvHolder.tv_logStatus.text="Log Set"
                        setSModel.exerciseList.forEach { it.isRoundIsComplete = false }
                    }
                }
            } else {
                exercise.isRoundLogSetCompletedForPlayer = true
                exercise.arrSets.forEachIndexed { index, setSModel ->
                    if (setSModel.isSelected) {
                        //    rvHolder.tv_logStatus.text="Set Completed"
                        setSModel.exerciseList.forEach { it.isRoundIsComplete = true }
                        setSModel.isRoundCompleted = true
                    }
                }

            }

            notifyadapters()
            notifyItemChanged(pos)
        }

        rvHolder.iv_PreviousSetIcon.setOnClickListener {
            if (exercise.arrSets.isNotEmpty()) {
                rvHolder.iv_nextSetIcon.visibility = View.VISIBLE
                val layoutManager = rvHolder.rv_sets.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition =
                    layoutManager.findFirstCompletelyVisibleItemPosition()

                try {
                    if (firstVisibleItemPosition < 4) {
                        rvHolder.rv_sets.smoothScrollToPosition(0)
                        rvHolder.iv_PreviousSetIcon.visibility = View.GONE

                    } else if (firstVisibleItemPosition > 4) {
                        rvHolder.iv_PreviousSetIcon.visibility = View.VISIBLE
                        rvHolder.rv_sets.smoothScrollToPosition(firstVisibleItemPosition - 4)
                    } else {
                        rvHolder.rv_sets.smoothScrollToPosition(0)
                        rvHolder.iv_PreviousSetIcon.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    rvHolder.rv_sets.smoothScrollToPosition(0)
                    rvHolder.iv_PreviousSetIcon.visibility = View.GONE
                }
                showHideNextButton(rvHolder, exercise)
            }
        }

        /**-------------------------------------------------------------------------------------------------------*/
        /** setting sets adapter */

        setsAdapter =
            PlayerSetsAdapter(context, exercise.arrSets, object : () -> Unit {
                override fun invoke() {

                }
            }) {
                try {
                    for (j in 0 until SetsAndRepsRoundList.size) {
                        SetsAndRepsRoundList[j].arrSets.forEach {
                            CommanUtils.notifyExerciseListForPlayer(
                                adapter,
                                it.exerciseList,
                                SetsAndRepsRoundList
                            )
                        }
                        if (SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible) {
                            SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible = false
                            notifyItemChanged(j, SetsAndRepsRoundList[j])
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                selectedPosition = it
                setExercsieAdapterAccordingToSet(
                    SetsAndRepsRoundList[pos].arrSets[it].exerciseList,
                    rvHolder.rv_exercise_selected,
                    pos,
                    SetsAndRepsRoundList[pos]
                )
            }

        val layoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvHolder.rv_sets.layoutManager = layoutManager1
        rvHolder.rv_sets.adapter = setsAdapter
        rvHolder.rv_sets.isEnabled = false

        setExercsieAdapterAccordingToSet(
            exercise.arrSets[0].exerciseList,
            rvHolder.rv_exercise_selected,
            pos,
            SetsAndRepsRoundList[pos]
        )

        /**-----------------------------------------------------------------------------------------------------*/

        showHidePreviousButton(rvHolder, exercise)
        /**this code is used for show and hide menu in round level*/
        rvHolder.option_menu_icon.setOnClickListener {
            var flag = exercise.isRoundPositionPopupOIsVisible
            SetsAndRepsRoundList.forEachIndexed { index, model ->
                model.arrSets.forEach { it1 ->
                    try {
                        for (j in 0 until it1.exerciseList.size) {
                            val model1 = it1.exerciseList[j]
                            if (model1.isExercisePopupVisible) {
                                model1.isExercisePopupVisible = false
                                adapter?.notifyItemChanged(j)
                                if (index != pos)
                                    notifyItemChanged(index)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                if (model.isRoundPositionPopupOIsVisible) {
                    model.isRoundPositionPopupOIsVisible = false
                    notifyItemChanged(index)
                }

                if (index == pos) {
                    model.isRoundPositionPopupOIsVisible = !flag
                    notifyItemChanged(index)
                }
            }
        }
        hideViewWhenClickOutOfView(rvHolder.itemView)

        if (SetsAndRepsRoundList.size == 1)
            rvHolder.suffle_complete_item.visibility = View.GONE
        else
            rvHolder.suffle_complete_item.visibility = View.VISIBLE
    }

    fun setExercsieAdapterAccordingToSet(
        exercise: ArrayList<ExerciseListingResponse.Data>,
        rvExerciseSelected: RecyclerView,
        pos: Int,
        exercise1: SetAndRepsModel
    ) {
        /**  setting selected adapter exercise adapter here  */
        var exerciseList = ArrayList<ExerciseListingResponse.Data>()
        exerciseList.clear()
        exerciseList.addAll(exercise)
        exerciseList.forEach { it.isRoundSelectedForPlayer=exercise1.isRoundSelectedForPlayer }

        adapter = NewPlayerExerciseSetAndRepsAdapter(
            context, exerciseList, object :
                NewPlayerExerciseSetAndRepsAdapter.OnItemClick {
                override fun videoPlayClick(
                    isScroll: Boolean,
                    data: ExerciseListingResponse.Data,
                    position: Int,
                    view: NewPlayerExerciseSetAndRepsAdapter.ExerciseView,
                    isLoad: Boolean
                ) {


                 }

                override fun forExchangeItem(
                    exerciseData: ExerciseListingResponse.Data,
                    exercisePosition: Int
                ) {
                    listener.forExchangeItem(
                        exerciseData,
                        exercisePosition,
                        exercise1.arrSets[0].exerciseList,
                        pos
                    )
                }

                override fun buttonPlayPauseVideo(
                    adapterPosition1: Int,
                    exerciseView: NewPlayerExerciseSetAndRepsAdapter.ExerciseView,
                    exerciseListing2: ArrayList<ExerciseListingResponse.Data>,
                    yPositionOnScreen: Float
                ) {
                    exerciseListing.clear()
                    exerciseListing.addAll(exerciseListing2)
                    if (exerciseListing.isNotEmpty()) {
                        exerciseView.fl_vv.removeAllViews()
                        if (player != null)
                            player?.release()
                        playPauseVideo(adapterPosition1, exerciseView, pos, yPositionOnScreen)
                    }
                }
            },
            videowidth,
            object : NewPlayerExerciseSetAndRepsAdapter.SelectTime {
                override fun selectTime(timing: String, pos: Int, s: String, s1: String) {

                }

                override fun selectRepetition(
                    timing: String,
                    pos: Int,
                    exerciseRepsNumber: String
                ) {
                 }

                override fun deleteExercise(pos: Int) {
                 }

                override fun copyExercise(pos: Int) {
                 }

                override fun selectExercise(Childpos: Int) {
                    SetsAndRepsRoundList[pos].arrSets.forEach {
                        for (j in 0 until it.exerciseList.size) {
                            if (!it.isSelected) {
                                if (Childpos == j) {
                                    it.exerciseList[j].isSelectedExercise =
                                        !it.exerciseList[j].isSelectedExercise
                                }
                            }
                        }
                    }
                    timeListener.selectExercise(pos, Childpos)
                }

                override fun replaceExercise(childPos: Int) {
                    timeListener.replaceExercise(pos, childPos)
                }

                override fun checkRoundStatus(adapterPosition: Int, operation: String) {
                    if (operation == "delete") {
                        if (exercise.size == 1 || exercise.size == 0) {
                            SetsAndRepsRoundList.removeAt(pos)
                        } else if (exercise.size >= 2) {
                            exercise1.arrSets.forEach { it.exerciseList.removeAt(adapterPosition) }
                            exercise1.strRoundName = ""
                        }
                    } else if (operation == "duplicate") {
                        if (adapterPosition >= 0) {
                            val data = exerciseList[adapterPosition]
                            data.isExercisePopupVisible = false

                            SetsAndRepsRoundList[pos].arrSets.forEach {
                                it.exerciseList.add(CommanUtils.addDuplicateExercise1(data))
                            }

                            exercise1.strRoundName = "Superset"
                        }
                    }
                    notifyadapters()
                    notifyDataSetChanged()
                }

                override fun checkUncheckExerciseForLogSet(pos1: Int) {
                    exercise1.arrSets.forEach { setSModel ->
                        if (setSModel.isSelected) {
                            var selectedExerciseList =
                                setSModel.exerciseList.filter { it.isRoundIsComplete }

                            if (selectedExerciseList.size == setSModel.exerciseList.size) {
                                exercise1.isRoundLogSetCompletedForPlayer = true
                                setSModel.isRoundCompleted = true
                            } else {
                                exercise1.isRoundLogSetCompletedForPlayer = false
                                setSModel.isRoundCompleted = false
                            }
                        }
                    }
                    notifyadapters()
                    notifyItemChanged(pos)
                }
            },
            object : OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                 //   Toast.makeText(context, "onStartDrag", Toast.LENGTH_SHORT).show()
                }
            }, supportFragmentManager
        ) {
            try {
                for (j in 0 until SetsAndRepsRoundList.size) {

                    SetsAndRepsRoundList[j].arrSets.forEach { setmode ->
                        try {
                            for (k in 0 until setmode.exerciseList.size) {
                                val model = setmode.exerciseList[k]
                                if (model.isExercisePopupVisible && k != it.second) {
                                    model.isExercisePopupVisible = false
                                    adapter?.notifyItemChanged(k)
                                    notifyItemChanged(j)
                                }
                                if (k == it.second) {

                                 }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if (SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible) {
                        SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible = false
                        notifyItemChanged(j)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
     /*       try {
                for (j in 0 until SetsAndRepsRoundList.size) {
                    SetsAndRepsRoundList[j].arrSets.forEach { setmode ->
                        try {
                            for (k in 0 until setmode.exerciseList.size) {
                                val model = setmode.exerciseList[k]
                                if (model.isExercisePopupVisible && k != it.second) {
                                    model.isExercisePopupVisible = false
                                    adapter?.notifyItemChanged(k)
                                    notifyItemChanged(j)
                                }
                                if (k == it.second) {

                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if (SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible) {
                        SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible = false
                        notifyItemChanged(j)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvExerciseSelected.layoutManager = layoutManager
        rvExerciseSelected.adapter = adapter


    }

    /** updating code and reflecting change related to touch out of popup for hiding them etc..*/
    fun performActions() {

    }

    fun showHidePreviousButton(rvHolder: MyView, exercise: SetAndRepsModel) {

        val layoutManager = rvHolder.rv_sets.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (firstVisibleItemPosition > 0)
            rvHolder.iv_PreviousSetIcon.visibility = View.VISIBLE
        else
            rvHolder.iv_PreviousSetIcon.visibility = View.GONE

    }

    fun showHideNextButton(rvHolder: MyView, exercise: SetAndRepsModel) {
        val layoutManager = rvHolder.rv_sets.layoutManager as LinearLayoutManager
        val lastPosition = layoutManager.findLastVisibleItemPosition() + 1
        if (lastPosition == exercise.arrSets.size) {
            rvHolder.iv_nextSetIcon.visibility = View.GONE
        } else if (lastPosition < exercise.arrSets.size - 1) {
            rvHolder.iv_nextSetIcon.visibility = View.VISIBLE
        }
    }


    fun hideViewWhenClickOutOfView(overlayview_: View) {

        overlayview_.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (j in 0 until SetsAndRepsRoundList.size) {

                        SetsAndRepsRoundList[j].arrSets.forEach {
                            CommanUtils.notifyExerciseListForPlayer(
                                adapter,
                                it.exerciseList,
                                SetsAndRepsRoundList
                            )
                            //   notifyItemChanged(j, SetsAndRepsRoundList[j])
                        }
                        if (SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible) {
                            SetsAndRepsRoundList[j].isRoundPositionPopupOIsVisible = false
                            notifyItemChanged(j, SetsAndRepsRoundList[j])
                        }
                    }

                    true // Consume the touch event
                }

                else -> false
            }
        }
    }

    fun notifyadapters() {
        if (this::setsAdapter.isInitialized) {
            setsAdapter.notifyDataSetChanged()

        }



        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
        }
    }

    inner class MyView(view: View) : RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {
        val ivHideControllerButton: LinearLayout by lazy {
            view.findViewById<LinearLayout>(R.id.controller)
        }

        var rv_exercise_selected = itemView.rv_exercise_selected
        var rv_sets = itemView.rv_sets
        var arrow_icon_reps = itemView.repetetion_layout
        var arrow_icon_sets = itemView.llTargetSets
        var iv_nextSetIcon = itemView.iv_nextSetIcon
        var suffle_complete_item = itemView.suffle_complete_item
        var option_menu_icon = itemView.option_menu_icon
        var tv_exercise_collection_type = itemView.tv_exercise_collection_type
        var tv_sets_text = itemView.tv_sets_text
        var repetetion_txt = itemView.repetetion_txt
        var tv_reounds = itemView.tv_reounds
        var iv_PreviousSetIcon = itemView.iv_PreviousSetIcon
        var top_divider = itemView.top_divider
        var llRoundPopup = itemView.llRoundPopup


        //TODO devendra
        var iv_note = itemView.ll_note
        var iv_add = itemView.ll_add_exercise
        var iv_duplicate = itemView.ll_duplicate_round
        var iv_delete = itemView.ll_delete
        var main_root_layout = itemView.main_root_layout
        var rl_logset = itemView.rl_logset
        var play_paus_icon = itemView.play_paus_icon
        var tv_logStatus = itemView.tv_logStatus
        var down_arrow_icon_sets = itemView.arrow_icon_sets
        var down_arrow_icon_reps = itemView.arrow_icon_reps
        var angularIcon1 = itemView.angularIcon1
        var angularIcon2 = itemView.angularIcon2
        var llTargetSetAndRepsCount = itemView.llTargetSetAndRepsCount
        var tv_set_and_reps_count = itemView.tv_set_and_reps_count


        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }

        init {
            itemView.setOnLongClickListener {
                mDragStartListener.onStartDrag(this@MyView)
                false
            }

            play_paus_icon.setOnClickListener {

                SetsAndRepsRoundList.forEachIndexed { index, setAndRepsModel ->
                    setAndRepsModel.isRoundSelectedForPlayer=false
                    setAndRepsModel.arrSets.forEach {
                        if (it.isSelected){
                            it.exerciseList.forEach { it.isRoundSelectedForPlayer=false }
                        }
                    }
                     notifyItemChanged(index)
                }
                 SetsAndRepsRoundList.forEachIndexed { index, setAndRepsModel ->
                    if (index==adapterPosition){
                        setAndRepsModel.isRoundSelectedForPlayer=true
                        setAndRepsModel.arrSets.forEach {
                            if (it.isSelected){
                                it.exerciseList.forEach {
                                    it.isRoundSelectedForPlayer=true
                                }
                            }
                        }
                        notifyItemChanged(index)
                    }
                 }
            }


            arrow_icon_reps.setOnClickListener {
                if (SetsAndRepsRoundList[adapterPosition].isRoundSelectedForPlayer) {
                    var str = ArrayList<String>()
                    if (SetsAndRepsRoundList[adapterPosition].strTargetReps.contains("Reps")) {
                        str.add(SetsAndRepsRoundList[adapterPosition].strTargetReps.split(" ")[0])
                        str.add(SetsAndRepsRoundList[adapterPosition].strTargetReps.split(" ")[0])
                    } else if (SetsAndRepsRoundList[adapterPosition].strTargetReps.contains(" To ") || SetsAndRepsRoundList[adapterPosition].strTargetReps.contains(
                            " to "
                        )
                    ) {
                        if (SetsAndRepsRoundList[adapterPosition].strTargetReps.contains(" To "))
                            str =
                                SetsAndRepsRoundList[adapterPosition].strTargetReps.split(" To ") as ArrayList<String>
                        else if (SetsAndRepsRoundList[adapterPosition].strTargetReps.contains(" to "))
                            str =
                                SetsAndRepsRoundList[adapterPosition].strTargetReps.split(" to ") as ArrayList<String>

                    } else {
                        str.add(SetsAndRepsRoundList[adapterPosition].strTargetReps)
                        str.add(SetsAndRepsRoundList[adapterPosition].strTargetReps)
                    }
                    WrapsDialog.newInstance1(
                        CommanUtils.getValue(),
                        object : WrapsDialog.HeightWeightCallBack {
                            override fun timeOnClick(
                                index: Int,
                                value: String,
                                index1: Int,
                                value1: String
                            ) {
                                /** need to do more like store locally */
                                if (index == index1) {
                                    SetsAndRepsRoundList[adapterPosition].strTargetReps =
                                        "$index Reps"
                                    repetetion_txt.text = "$index Reps"
                                } else {
                                    SetsAndRepsRoundList[adapterPosition].strTargetReps =
                                        ("$value To $value1").toString()
                                    repetetion_txt.text = ("$index To $value1").toString()
                                }
                            }
                        },
                        context,
                        str[0],
                        str[1],
                        "repsInSetAndReps"
                    ).show(supportFragmentManager)
                }

            }
            suffle_complete_item.setOnClickListener {
             //   Toast.makeText(context, "shuffle_complete_item", Toast.LENGTH_SHORT).show()
            }

            iv_note.setOnClickListener {
                SetsAndRepsRoundList[adapterPosition].isRoundPositionPopupOIsVisible = false
                notifyItemChanged(adapterPosition)
                timeListener.addNotesInRound(adapterPosition)

            }
            iv_add.setOnClickListener {
                timeListener.addExerciseInRound(adapterPosition)
            }
            iv_duplicate.setOnClickListener {

                val copyData = SetsAndRepsRoundList[adapterPosition].deepCpoy()
                SetsAndRepsRoundList.add(makeDuplicateObject(copyData).deepCpoy())
                SetsAndRepsRoundList[adapterPosition].isRoundPositionPopupOIsVisible = false
                notifyadapters()
                notifyDataSetChanged()
                Log.d("ghjdsghkhk", "${copyData.strRoundName} ")
            }
            iv_delete.setOnClickListener {

                CommanUtils.deletePopupForSetAndReps(
                    context,
                    "Are you sure you want to delete this round?"
                ) {
                    SetsAndRepsRoundList.removeAt(adapterPosition)
                    notifyDataSetChanged()
                    it.dismiss()
                }

            }
            arrow_icon_sets.setOnClickListener {
                if (SetsAndRepsRoundList.get(adapterPosition).isRoundSelectedForPlayer) {
                    var reps = tv_sets_text.text.toString().trim().split(" ")[0]

                    WorkoutLevelDialog.newInstance(
                        getRepValue1(),
                        object : WorkoutLevelDialog.HeightWeightCallBack {
                            override fun levelOnClick(index: Int, str: String) {

                                SetsAndRepsRoundList[adapterPosition].strTargetSets = str.toString()
                                if ((index + 1) > SetsAndRepsRoundList[adapterPosition].arrSets.size) {
                                    /**Increasing sets in round according to selected from dialog*/
                                    val renge =
                                        index + 1 - SetsAndRepsRoundList[adapterPosition].arrSets.size
                                    val tempList = ArrayList<SetSModel>()
                                    for (i in 1..renge) {
                                        tempList.add(
                                            SetSModel(
                                                setName = "SET ${SetsAndRepsRoundList[adapterPosition].arrSets.size + i}",
                                                exerciseList = getExerciseList(SetsAndRepsRoundList[adapterPosition].arrSets[0].exerciseList),
                                                strExerciseType = SetsAndRepsRoundList[adapterPosition].strRoundName,
                                                isSelected = false
                                            )
                                        )
                                    }
                                    SetsAndRepsRoundList[adapterPosition].arrSets.addAll(tempList)
                                } else if ((index + 1) == SetsAndRepsRoundList[adapterPosition].arrSets.size) {

                                } else if ((index + 1) < SetsAndRepsRoundList[adapterPosition].arrSets.size) {

                                    val numberOfElementsToRemove =
                                        SetsAndRepsRoundList[adapterPosition].arrSets.size - (index + 1)
                                    if (numberOfElementsToRemove > 0 && numberOfElementsToRemove <= SetsAndRepsRoundList[adapterPosition].arrSets.size) {
                                        val startIndexToRemove =
                                            SetsAndRepsRoundList[adapterPosition].arrSets.size - numberOfElementsToRemove
                                        SetsAndRepsRoundList[adapterPosition].arrSets.subList(
                                            startIndexToRemove,
                                            SetsAndRepsRoundList[adapterPosition].arrSets.size
                                        ).clear()
                                    }
                                }
                                notifyDataSetChanged()
                            }
                        },
                        context,
                        reps.toInt()
                    ).show(supportFragmentManager)
                }

            }

        }

    }

    /**making new exercise list for adding in set when increasing sets number in round*/
    private fun getExerciseList(exerciseList: ArrayList<ExerciseListingResponse.Data>): java.util.ArrayList<ExerciseListingResponse.Data> {
        var newList = ArrayList<ExerciseListingResponse.Data>()

        newList.addAll(exerciseList.clone() as Collection<ExerciseListingResponse.Data>)
        return newList
    }


    interface OnItemClick {
        fun videoPlayClick(
            isScroll: Boolean,
            data: ExerciseListingResponse.Data,
            position: Int,
            view: NewPlayerExerciseSetAndRepsAdapter.ExerciseView,
            isLoad: Boolean
        )

        fun forExchangeItem(
            exerciseData: ExerciseListingResponse.Data,
            exercisePos: Int,
            exercisesList: ArrayList<ExerciseListingResponse.Data>,
            roundPos: Int
        )
    }

    interface SelectTime {
        fun selectTime(timing: String, pos: Int, s: String, s1: String)
        fun selectRepetition(timing: String, pos: Int, exerciseRepsNumber: String)
        fun deleteExercise(pos: Int)
        fun copyExercise(pos: Int)
        fun selectExercise(parentPosition: Int, Childpos: Int)
        fun replaceExercise(parentPosition: Int, childPosition: Int)
        fun addExerciseInRound(parentPosition: Int)
        fun addNotesInRound(parentPosition: Int)
    }


    /**making duplicate object of sets*/
    fun makeDuplicateObject(setAndRepsModel: SetAndRepsModel): SetAndRepsModel {

        val listNewSets = ArrayList<SetSModel>()
        setAndRepsModel.arrSets.forEach { sets ->
            val tempExerciseList = ArrayList<ExerciseListingResponse.Data>()
            sets.exerciseList.forEach {
                tempExerciseList.add(CommanUtils.addDuplicateExercise(it.deepCopy()))
            }

            val model = SetSModel(
                sets.setName,
                sets.exerciseList.map { it.deepCopy() } as ArrayList<ExerciseListingResponse.Data>,
                sets.strExerciseType,
                sets.isSelected,
                sets.weightForExercise,
                sets.repsForExercise
            )
            listNewSets.add(model.deepCopy())

        }

        return SetAndRepsModel(
            setAndRepsModel.strRoundCounts,
            setAndRepsModel.strRoundName,
            setAndRepsModel.strTargetSets,
            setAndRepsModel.strTargetReps,
            setAndRepsModel.strExerciseType,
            setAndRepsModel.strRoundCounts,
            listNewSets,
            false
        ).deepCpoy()
    }


    private fun getRepValue1(): ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 1..10) {
            if (i < 10) {
                values.add("0" + i)
            } else {
                values.add("" + i)
            }

        }
        return values
    }

    private fun playPauseVideo(
        adapterPosition: Int,
        myView: NewPlayerExerciseSetAndRepsAdapter.ExerciseView,
        pos: Int,
        y: Float
    ) {
        if (adapterPosition != tempPos || pos != tempRoundPos) {
            if (tempPos != -1 || tempRoundPos != -1) {
                if (tempExerciseItem != null) {
                    if (tempPos != -1 && tempExerciseItem!!.isPlaying || tempRoundPos != -1) {
                        //try {
                        removeVideoView(tempPos, tempHolder)

                        //For Open new Video Cell
                        tempPos = adapterPosition
                        tempHolder = myView
                        tempRoundPos = pos
                        tempExerciseItem = exerciseListing[adapterPosition]
                        isDownloadedVideo(
                            exerciseListing[adapterPosition].exercise_video,
                            adapterPosition,
                            myView, y
                        )
//                        } catch (error: Exception) {
//                            Log.d("TAG", "playPauseVideo: ${error}")
//                        }
                    } else {
                        isDownloadedVideo(
                            exerciseListing[adapterPosition].exercise_video,
                            adapterPosition,
                            myView,
                            y
                        )
                    }
                    tempPos = adapterPosition
                    tempHolder = myView
                    tempRoundPos = pos
                    tempExerciseItem = exerciseListing[adapterPosition]

                }
            } else {
                tempPos = adapterPosition
                tempHolder = myView
                tempRoundPos = pos
                tempExerciseItem = exerciseListing[adapterPosition]
                isDownloadedVideo(
                    exerciseListing[adapterPosition].exercise_video, adapterPosition, myView, y
                )

            }
        } else {
            tempPos = adapterPosition
            tempHolder = myView
            tempRoundPos = pos
            tempExerciseItem = exerciseListing[adapterPosition]
            isDownloadedVideo(
                exerciseListing[adapterPosition].exercise_video, adapterPosition, myView, y
            )

        }
    }

    fun intializePlayer(
        news_video: Uri,
        position: Int,
        myView: NewPlayerExerciseSetAndRepsAdapter.ExerciseView,
        isLoad: Boolean,
        yPositionOnScreen: Float
    ) {
        var playerView: PlayerView = PlayerView(context)
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        if (exerciseListing.size != 0 && exerciseListing.size != -1) {
            if (!exerciseListing[position].isPlaying) {
                myView.iv_lock.rotation = 180f
                myView.fl_vv.addView(playerView)
                myView.fl_vv.requestFocus()
                playerView.setOnClickListener {
                    myView.iv_lock.performClick()
                }
                playerView.layoutParams.height = videowidth
                //playerView.layoutParams.height = 500
                playerView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
                mediaDataSourceFactory =
                    DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, "mediaPlayerSample")
                    )
                val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(news_video)
                player = ExoPlayerFactory.newSimpleInstance(context, trackSelector!!)
                with(player!!) {
                    prepare(mediaSource, false, false)
                    playWhenReady = true
                    repeatMode =
                        Player.REPEAT_MODE_ONE //repeat play video
                    videoScalingMode =
                        Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                }
                playerView.setShutterBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorBlack
                    )
                )
                playerView.player = player
                playerView.requestFocus()

                if (isLoad)
                    playerView.setShowBuffering(true)
                lastSeenTrackGroupArray = null

                stopScroll.scrollToPosition(playerView.layoutParams.height, yPositionOnScreen)
                stopScroll.stopScrolling(false)

            } else {
                if (player != null) {
                    myView.fl_vv.removeAllViews()
                    player?.release()
                    myView.iv_lock.rotation = 0f

                }
            }
        }
        exerciseListing[position].isPlaying = !exerciseListing[position].isPlaying
    }


    fun isDownloadedVideo(
        videoUrl: String,
        position: Int,
        holder: NewPlayerExerciseSetAndRepsAdapter.ExerciseView,
        yPositionOnScreen: Float
    ) {
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    context.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            Log.e("download file path", "file path..." + f.absolutePath)
            if (!f.exists()) {
                var uri = Uri.parse(videoUrl)
                intializePlayer(uri, position, holder, true, yPositionOnScreen)
            } else {
                var uri = Uri.parse(path)
                intializePlayer(uri, position, holder, false, yPositionOnScreen)
            }
        }
    }

    fun removeVideoView(pos: Int, holder: NewPlayerExerciseSetAndRepsAdapter.ExerciseView) {

        if (player != null) {
            player?.release()
            tempExerciseItem!!.isPlaying = false
            holder.iv_lock.rotation = 0f
            holder.fl_vv.removeAllViews()
            notifyadapters()
        }
    }


    interface StopScroll {
        fun stopScrolling(isScroll: Boolean)
        fun scrollToPosition(position: Int, y: Float)
    }

}