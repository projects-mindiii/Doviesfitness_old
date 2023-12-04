package com.doviesfitness.ui.new_player.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Chronometer
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.menu.EcerciseChngeDialogFragment
import com.doviesfitness.allDialogs.menu.ExchangDialogMenu
import com.doviesfitness.allDialogs.menu.ExerciseMarkAsDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityNewPlayerViewBinding
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetailResponce
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.AddExerciseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.AddNotesActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.WorkoutCompleteActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetAndRepsModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetSModel
import com.doviesfitness.ui.new_player.adapter.NewPlayerExerciseSetAndRepsAdapter
import com.doviesfitness.ui.new_player.adapter.PlayerRoundAdapter
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.google.android.exoplayer2.util.Log
import kotlinx.android.synthetic.main.activity_new_player_view.forword_previous_play_pause
import kotlinx.android.synthetic.main.activity_new_player_view.llTopBar
import kotlinx.android.synthetic.main.activity_new_player_view.ll_mark_as_ungroup_delete
import kotlinx.android.synthetic.main.activity_new_player_view.ll_selectAll
import kotlinx.android.synthetic.main.activity_new_player_view.selected_count
import net.khirr.library.foreground.Foreground
import java.util.concurrent.TimeUnit

class NewPlayerView : BaseActivity(), OnStartDragListener,
    EcerciseChngeDialogFragment.DialogEventListener {
    private var program_plan_id = ""
    private var positionForAddNotes = -1
    private lateinit var countDownTimer: CountDownTimer
    private var noteForExercise = ""
    private var isAllExerciseSelected: Boolean = false
    private var exerciseType = ""
    private var roundPositionForAddExercise: Int = -1
    lateinit var roundList: java.util.ArrayList<SetAndRepsModel>
    lateinit var binding: ActivityNewPlayerViewBinding
    var ReplaceParentPsotion = -1
    var repalceChildPosition = -1

    private val countdownhandler = Handler()
    private var isTimerRunning = false
    private var elapsedTime = 0L
    private val handler = Handler()
    private val mInterval = 1000 // 1 second in this case
    private var mHandler: Handler? = null
    private var timeInSeconds = 0L
    private var startButtonClicked = false
    lateinit var WDetail: WorkoutDetail
    private lateinit var exerciseDataofItem: ExerciseListingResponse.Data
    private lateinit var tempList: java.util.ArrayList<ExerciseListingResponse.Data>
    private var itemPosition: Int = 0
    lateinit var playerRoundAdapter: PlayerRoundAdapter
    private var ReplaceExerciseId = ""
    var mainViewTotalHeight = 0
    lateinit var complete_workoutDetail: WorkoutDetailResponce
    var isAdmin = "No"
    private lateinit var chronometer: Chronometer

    val foregroundListener = object : Foreground.Listener {
        override fun background() {
            android.util.Log.e("background", "Go to background")
        }

        override fun foreground() {
            Constant.requestAudioFocusForMyApp(getActivity())
            android.util.Log.e("Foreground", "Go to foreground")
        }
    }

    override fun onBackPressed() {
        Foreground.removeListener(foregroundListener)

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_player_view)
        roundList = ArrayList()

        isAdmin = getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        Foreground.init(application)
        Foreground.addListener(foregroundListener)
        roundList.addAll(intent.getSerializableExtra("setAndRepsExerciseList") as java.util.ArrayList<SetAndRepsModel>)
        roundList.get(0).isRoundSelectedForPlayer = true
        roundList.get(0).arrSets.forEach {
            if (it.isSelected) {
                it.exerciseList.forEach { it.isRoundSelectedForPlayer = true }
            }
        }

        if (intent.getSerializableExtra("WDetail") != null) {
            WDetail = intent.getSerializableExtra("WDetail") as WorkoutDetail
            if (intent.hasExtra("from_ProgramPlan")) {
                if (intent.getStringExtra("from_ProgramPlan") != null) {
                    program_plan_id = intent.getStringExtra("from_ProgramPlan")!!
                }
            }
        }

        if (intent.getSerializableExtra("complete_workoutDetail") != null) {
            complete_workoutDetail =
                (intent.getSerializableExtra("complete_workoutDetail") as WorkoutDetailResponce?)!!
        }


        if (intent.getStringExtra("workoutType") != null) {
            val type = intent.getStringExtra("workoutType")
            if (type == "Set & Reps")
                exerciseType = "SetAndReps"
            else
                exerciseType = type!!
        }

        setAdapter()

        binding.rlPlayerNote.setOnClickListener {
            Constant.requestAudioFocusClose(applicationContext)
            startActivityForResult(
                Intent(
                    this@NewPlayerView, AddNotesActivity::class.java
                ).putExtra(
                    "notesFor", "CompleteWorkout"
                ).putExtra("notesText", noteForExercise),
                501
            )
        }
        binding.rlMusic.setOnClickListener {
            music()
        }
        binding.previousButon.setOnClickListener {
            try {
                var position = -1
                if (!roundList[0].isRoundSelectedForPlayer) {
                    roundList.forEachIndexed { index, setAndRepsModel ->
                        if (setAndRepsModel.isRoundSelectedForPlayer) {
                            position = index - 1
                            setAndRepsModel.isRoundSelectedForPlayer = false
                            setAndRepsModel.arrSets.forEach {
                                if (it.isSelected) {
                                    it.exerciseList.forEach { it.isRoundSelectedForPlayer = false }
                                }
                            }
                            playerRoundAdapter.notifyItemChanged(index)
                        }
                    }
                    if (position >= 0) {
                        roundList[position].isRoundSelectedForPlayer = true
                        roundList.forEach {
                            if (it.isRoundSelectedForPlayer) {
                                it.arrSets.forEach {
                                    if (it.isSelected) {
                                        it.exerciseList.forEach {
                                            it.isRoundSelectedForPlayer = true
                                        }
                                    }
                                }
                            }
                        }
                        playerRoundAdapter.notifyItemChanged(position)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        binding.forwordButton.setOnClickListener {
            try {
                var position = -1
                if (!roundList[roundList.size - 1].isRoundSelectedForPlayer) {
                    roundList.forEachIndexed { index, setAndRepsModel ->
                        if (setAndRepsModel.isRoundSelectedForPlayer) {
                            position = index + 1
                            setAndRepsModel.isRoundSelectedForPlayer = false
                            setAndRepsModel.arrSets.forEach {
                                if (it.isSelected) {
                                    it.exerciseList.forEach { it.isRoundSelectedForPlayer = false }
                                }
                            }
                            playerRoundAdapter.notifyItemChanged(index)
                        }
                    }
                    if (position < roundList.size - 1) {
                        roundList[position].isRoundSelectedForPlayer = true
                        roundList.forEach {
                            if (it.isRoundSelectedForPlayer) {
                                it.arrSets.forEach {
                                    if (it.isSelected) {
                                        it.exerciseList.forEach {
                                            it.isRoundSelectedForPlayer = true
                                        }
                                    }
                                }
                            }
                        }
                        playerRoundAdapter.notifyItemChanged(position)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        binding.playPauseMusic.setOnClickListener {
            binding.llEndWorkout.visibility = View.VISIBLE
            binding.forwordPreviousPlayPause.visibility = View.GONE
        }

        binding.ivPlayButton.setOnClickListener {
            binding.forwordPreviousPlayPause.visibility = View.VISIBLE
            binding.llEndWorkout.visibility = View.GONE
        }
        binding.tvMarkAs.setOnClickListener {
            val tempList = java.util.ArrayList<ExerciseListingResponse.Data>()
            for (i in 0 until roundList.size) {
                tempList.addAll(roundList[i].arrSets[0].exerciseList.filter {
                    it.isSelectedExercise
                })
            }


            var dialogFragment = ExerciseMarkAsDialogFragment(tempList.size) {
                if (it == 1) {//SetAndReps   Left & Right
                    creatLeftAndRightAndSuperset("Left & Right", tempList)
                } else if (it == 2) {
                    creatLeftAndRightAndSuperset("Superset", tempList)
                }
            }
            dialogFragment.show(supportFragmentManager!!, "forSelectPlan")
        }

        binding.llEndWorkout.setOnClickListener {
            // Constant.requestAudioFocusClose(this)
            //countdownhandler.removeCallbacks(countdownRunnable!!)
            //  mCountDownTimer!!.cancel()
            // if (restTimeCountDownTimer != null)
            //     restTimeCountDownTimer!!.cancel()

            startActivity(
                Intent(getActivity(), WorkoutCompleteActivity::class.java)
                    .putExtra(
                        "WDetail",
                        WDetail
                    ).putExtra(
                        "complete_workoutDetail",
                        complete_workoutDetail
                    )
                     .putParcelableArrayListExtra("RoundsList", roundList)
                    .putExtra("from_ProgramPlan", program_plan_id)
             )
            finish()
        }
        binding.tvDeleteFromRounds.setOnClickListener {
            deletingExerciseFromRounds()
        }
        binding.tvUngroup.setOnClickListener {
            unGroupSelectedExercise()
        }


        binding.selectSelectDeselectAll.setOnClickListener {
            if (isAllExerciseSelected) {
                isAllExerciseSelected = false
                roundList.forEach {
                    it.arrSets.forEach { it2 ->
                        it2.exerciseList.forEach { it3 ->
                            it3.isSelectedExercise = false
                        }
                    }
                }
                playerRoundAdapter.notifyDataSetChanged()
                //adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
                Glide.with(this).load(R.drawable.deselected_create_workout)
                    .into(binding.selectSelectDeselectAll)
                selected_count.text = "Select"

                ll_mark_as_ungroup_delete.visibility = View.GONE
                ll_selectAll.visibility = View.GONE
            } else {/*selecting all exercises*/
                isAllExerciseSelected = true
                var count = 0
                roundList.forEach {
                    it.arrSets.forEach { it2 ->
                        it2.exerciseList.forEach { it3 ->
                            it3.isSelectedExercise = true
                        }
                    }
                }
                roundList.forEach {
                    count += it.arrSets[0].exerciseList.size
                }
                playerRoundAdapter.notifyDataSetChanged()
                Glide.with(this).load(R.drawable.selected_circle_blue_)
                    .into(binding.selectSelectDeselectAll)
                selected_count.text = "$count Selected"
            }
            if (ll_selectAll.visibility == View.GONE) {
                forword_previous_play_pause.visibility = View.VISIBLE
            }


        }


        binding.addExerciseBtn.setOnClickListener {
             var intent = Intent(getActivity(), AddExerciseActivity::class.java)
            intent.putExtra("workout_Type", exerciseType)
            startActivityForResult(intent, 7)
        }
        startTimer()
     }

    /**================================== mark as functionality =================================================*/

    private fun creatLeftAndRightAndSuperset(
        roundName: String, tempList: java.util.ArrayList<ExerciseListingResponse.Data>
    ) {
        var targetReps = ""
        var targetSets = ""

        roundList.forEachIndexed { index1, setAndRepsModel ->
            setAndRepsModel.arrSets.forEachIndexed { index2, setSModel ->
                setSModel.exerciseList.forEachIndexed { inde3, exercieses ->
                    if (targetSets.isEmpty()) {
                        targetReps = setAndRepsModel.strTargetReps
                        targetSets = setAndRepsModel.strTargetSets
                    }
                }
            }
        }

        val setsList = java.util.ArrayList<SetSModel>()
        for (i in 1..targetSets.toInt()) {
            var flag = false
            if (i == 1) flag = true
            val newArrayList = java.util.ArrayList<ExerciseListingResponse.Data>()
            tempList.forEach { newArrayList.add(CommanUtils.addDuplicateExercise1(it)) }

            setsList.add(
                SetSModel(
                    setName = "SET $i",
                    exerciseList = newArrayList,
                    strExerciseType = roundName,
                    isSelected = flag
                )
            )

        }

        roundList.add(
            0,
            SetAndRepsModel(
                strRoundCounts = "",
                strRoundName = roundName,
                strTargetSets = "$targetSets",
                strTargetReps = "$targetReps",
                strExerciseType = roundName,
                strSetsCounts = "",
                arrSets = setsList
            )
        )
        roundList.forEach { it.isRoundSelectedForPlayer = false }
        roundList.get(0).isRoundSelectedForPlayer = true

        try {
            for (i in 0 until roundList.size) {
                for (j in 0 until roundList[i].arrSets.size) {
                    roundList[i].arrSets[j].exerciseList.removeAll { it.isSelectedExercise }
                }
                if (roundList[i].arrSets[0].exerciseList.isEmpty()) {
                    roundList.removeAt(i)

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        playerRoundAdapter.notifyadapters()
        playerRoundAdapter.notifyDataSetChanged()
        ll_mark_as_ungroup_delete.visibility = View.GONE
        llTopBar.visibility = View.VISIBLE
        ll_selectAll.visibility = View.GONE

    }


    /**=========================ungrouping selected device======================================================*/
    private fun unGroupSelectedExercise() {
        var tempList = java.util.ArrayList<ExerciseListingResponse.Data>()
        for (i in 0 until roundList.size) {
          tempList.addAll(roundList[i].arrSets[0].exerciseList.filter { it.isSelectedExercise })
        }
        createNewRoundWithSelectedExercise(tempList)
    }


    private fun createNewRoundWithSelectedExercise(tempList: java.util.ArrayList<ExerciseListingResponse.Data>) {
        var targetReps = ""
        var targetSets = ""

        roundList.forEachIndexed { index1, setAndRepsModel ->
            setAndRepsModel.arrSets.forEachIndexed { index2, setSModel ->
                setSModel.exerciseList.forEachIndexed { inde3, exercieses ->
                    if (targetSets.isEmpty()) {
                        targetReps = setAndRepsModel.strTargetReps
                        targetSets = setAndRepsModel.strTargetSets
                    }
                }
            }
        }
        for (i in 1..tempList.size) {
            roundList.add(
                SetAndRepsModel(
                    strRoundCounts = "",
                    strRoundName = "",
                    strTargetSets = "$targetSets",
                    strTargetReps = "$targetReps",
                    strExerciseType = "",
                    strSetsCounts = "",
                    arrSets = getSets(
                        targetSets.toInt(), tempList[i - 1], ""
                    )
                )
            )
        }



        for (i in 0 until roundList.size) {
            try {
                for (j in 0 until roundList[i].arrSets.size) {
                    roundList[i].arrSets[j].exerciseList.removeAll { it.isSelectedExercise }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (roundList[i].arrSets[0].exerciseList.isEmpty()) {
                    roundList.removeAt(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        playerRoundAdapter.notifyadapters()
        playerRoundAdapter.notifyDataSetChanged()
        ll_mark_as_ungroup_delete.visibility = View.GONE
        ll_selectAll.visibility = View.GONE
        forword_previous_play_pause.visibility = View.VISIBLE
        binding.llTopBar.visibility = View.VISIBLE
        binding.llSelectAll.visibility = View.GONE
    }


    private fun getSets(
        targetSets: Int, list: ExerciseListingResponse.Data, roundName: String
    ): java.util.ArrayList<SetSModel> {


        val setsList = java.util.ArrayList<SetSModel>()
        for (i in 1..targetSets) {
            var list1 = java.util.ArrayList<ExerciseListingResponse.Data>()
            list1.add(CommanUtils.addDuplicateExercise1(list))
            println("IndexToPrintRefrence  : ${System.identityHashCode(list1)}")
            var flag = false
            if (i == 1) flag = true
            setsList.add(
                SetSModel(
                    setName = "SET $i",
                    exerciseList = list1,
                    strExerciseType = roundName,
                    isSelected = flag
                )
            )
        }
        return setsList
    }
    /**=====================================================================================================================*/
    /** delete selected exercise from round*/
    private fun deletingExerciseFromRounds() {

        CommanUtils.deletePopupForSetAndReps(
            this@NewPlayerView, "Are you sure you want to delete these exercises?"
        ) {


            for (i in 0 until roundList.size) {
                for (j in 0 until roundList[i].arrSets.size) {
                    roundList[i].arrSets[j].exerciseList.removeAll { it.isSelectedExercise }
                }

                if (roundList[i].arrSets[0].exerciseList.isEmpty()) {
                    roundList.removeAt(i)
                }
            }

            playerRoundAdapter.notifyadapters()
            playerRoundAdapter.notifyDataSetChanged()
            forword_previous_play_pause.visibility = View.VISIBLE
            binding.llTopBar.visibility = View.VISIBLE
            binding.llSelectAll.visibility = View.GONE
            //  ll_mark_as_ungroup_delete.visibility = View.GONE
            //ll_addExerciseView.visibility = View.VISIBLE
            // topBlurView1.visibility = View.VISIBLE
            //   ll_selectAll.visibility = View.GONE
            it.dismiss()
        }

    }

    /**=========================================================================================================================*/

    override fun onResume() {
        super.onResume()
        Constant.releaseAudioFocusForMyApp(getActivity())
        binding.svMain.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // This code will be executed when the layout is ready.
                mainViewTotalHeight = binding.svMain.getChildAt(0).height // Get the total height
                // Ensure you remove the listener to avoid multiple calls if layout changes again
                binding.svMain.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // Now, you have the total height of the ScrollView in 'totalHeight'
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        Constant.requestAudioFocusForMyApp(getActivity())
        Constant.releaseAudioFocusForMyApp(getActivity())

    }

    private fun startTimer() {
        mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }

    fun setAdapter() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x
        val screenWidth = size.x / 320
        val videowidth = 120 + (160 * screenWidth)

        playerRoundAdapter =
            PlayerRoundAdapter(
                this, roundList,
                object : PlayerRoundAdapter.OnItemClick {
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
                        exercisePos: Int,
                        exercisesList: ArrayList<ExerciseListingResponse.Data>,
                        roundPos: Int
                    ) {
                        showCreatePlanAndWorkOutDialog(
                            exerciseData,
                            exercisePos,
                            exercisesList,
                            roundPos
                        )
                    }
                },
                videowidth,
                object : PlayerRoundAdapter.SelectTime {
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

                    override fun selectExercise(parentPosition: Int, Childpos: Int) {
                        if (exerciseType == "SetAndReps") {
                            roundList[parentPosition].arrSets.forEach {
                                for (u in 0 until it.exerciseList.size) {
                                    if (u == Childpos) {
                                        it.exerciseList[u].isSelectedExercise =
                                            !it.exerciseList[u].isSelectedExercise
                                    }
                                }
                            }
                            var count = 0
                            var exerciseListSize = 0
                            roundList.forEach {
                                var list =
                                    it.arrSets[0].exerciseList.filter { it1 -> it1.isSelectedExercise }
                                count += list.size
                                exerciseListSize += it.arrSets[0].exerciseList.size
                            }
                            if (count > 0) {
                                selected_count.text = "$count Selected"

                                ll_mark_as_ungroup_delete.visibility = View.VISIBLE
                                //  ll_addExerciseView.visibility = View.GONE
                                llTopBar.visibility = View.GONE
                                forword_previous_play_pause.visibility = View.GONE
                                ll_selectAll.visibility = View.VISIBLE

                            } else {
                                selected_count.text = "Select"


                                ll_mark_as_ungroup_delete.visibility = View.GONE
                                //   ll_addExerciseView.visibility = View.VISIBLE
                                llTopBar.visibility = View.VISIBLE
                                ll_selectAll.visibility = View.GONE
                            }
                            updatingViewAccordingToSelections(count, exerciseListSize)
                        }
                    }

                    override fun replaceExercise(parentPosition: Int, childPosition: Int) {

                    }

                    override fun addExerciseInRound(parentPosition: Int) {
                        roundPositionForAddExercise = parentPosition
                        var intent = Intent(getActivity(), AddExerciseActivity::class.java)
                        intent.putExtra("workout_Type", exerciseType)
                        intent.putExtra("isAddingInRound", "Yes")
                        startActivityForResult(intent, 11)
                    }

                    override fun addNotesInRound(parentPosition: Int) {

                        positionForAddNotes = parentPosition
                        startActivityForResult(
                            Intent(
                                this@NewPlayerView, AddNotesActivity::class.java
                            ).putExtra(
                                "notesFor", "OnParticularRound"
                            ).putExtra(
                                "notesText",
                                roundList[parentPosition].noteForExerciseInRound
                            ), 501
                        )
                    }
                },
                this,
                object : PlayerRoundAdapter.StopScroll {
                    override fun stopScrolling(isScroll: Boolean) {


                    }

                    override fun scrollToPosition(position: Int, y: Float) {

                        Handler().postDelayed({
                            if (((y * 100) / mainViewTotalHeight) >= 80) {
                                binding.svMain.smoothScrollBy(
                                    0,
                                    ((position + (((y * 100) / mainViewTotalHeight) * 2)).toInt())
                                )
                            } else if (((y * 100) / mainViewTotalHeight) >= 75) {
                                binding.svMain.smoothScrollBy(
                                    0,
                                    ((position + (((y * 100) / mainViewTotalHeight) * 1.2)).toInt())
                                )
                            } else if (((y * 100) / mainViewTotalHeight) <= 75 && ((y * 100) / mainViewTotalHeight) >= 55) {
                                binding.svMain.smoothScrollBy(
                                    0, position
//                                ((position + (((y  100) / mainViewTotalHeight)  0.5)).toInt())
                                )
                            }
                            android.util.Log.d(
                                "TAG",
                                "scrollToPosition: ${((y * 100) / mainViewTotalHeight)}"
                            )
                        }, 1000)
                    }
                }, supportFragmentManager
            )
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvNewPlayer.layoutManager = layoutManager
        binding.rvNewPlayer.adapter = playerRoundAdapter
        /*  playerRoundAdapter =
                PlayerRoundAdapter(
                    getActivity(), PlayerRoundAdapter, object :
                        PlayerRoundAdapter.OnItemClick {
                        override fun videoPlayClick(
                            isScroll: Boolean,
                            data: ExerciseListingResponse.Data,
                            position: Int,
                            view: SelectedExerciseAdapterSetAndReps.ExerciseView,
                            isLoad: Boolean
                        ) {
                            // Toast.makeText(this@CreateWorkoutActivity,"sdgbdgd",Toast.LENGTH_SHORT).show()
                        }

                        override fun forExchangeItem(
                            exerciseData: ExerciseListingResponse.Data,
                            exerPos: Int,
                            exercisesList: ArrayList<ExerciseListingResponse.Data>,
                            Rpoundpos: Int
                        ) {
                            Log.d("sgdskjgdks", "forExchangeItem: " + "123")
                            showCreatePlanAndWorkOutDialog(
                                exerciseData,
                                exerPos,
                                exercisesList,
                                Rpoundpos
                            )
                        }
                    },
                    videowidth,
                    object : SetAndRepsAdapter.SelectTime {
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

                        override fun addNotesInRound(pos: Int) {
                            positionForAddNotes = pos
                            startActivityForResult(
                                Intent(
                                    this@CreateWorkoutActivity,
                                    AddNotesActivity::class.java
                                ).putExtra(
                                    "notesFor",
                                    "OnParticularRound"
                                ).putExtra("notesText", SetsAndRepsList[pos].noteForExerciseInRound),
                                501
                            )
                        }

                        override fun addExerciseInRound(pos: Int) {

                        }

                        override fun selectExercise(pos: Int, Childpos: Int) {
                            if (exerciseType == "SetAndReps") {

                                SetsAndRepsList[pos].arrSets.forEach {

                                    for (u in 0 until it.exerciseList.size) {
                                        if (u == Childpos) {
                                            it.exerciseList[u].isSelectedExercise =
                                                !it.exerciseList[u].isSelectedExercise
                                        }
                                    }
                                }

                                var count = 0
                                var exerciseListSize = 0
                                SetsAndRepsList.forEach {
                                    var list =
                                        it.arrSets[0].exerciseList.filter { it1 -> it1.isSelectedExercise }
                                    count += list.size
                                    exerciseListSize += it.arrSets[0].exerciseList.size
                                }
                                if (count > 0) {
                                    selected_count.text = "$count Selected"
                                    Toast.makeText(
                                        this@CreateWorkoutActivity,
                                        "selected count is $count",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    ll_mark_as_ungroup_delete.visibility = View.VISIBLE
                                    ll_addExerciseView.visibility = View.GONE
                                    topBlurView1.visibility = View.GONE
                                    ll_selectAll.visibility = View.VISIBLE

                                } else {
                                    selected_count.text = "Select"
                                    Toast.makeText(
                                        this@CreateWorkoutActivity,
                                        "no exercise is selected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    ll_mark_as_ungroup_delete.visibility = View.GONE
                                    ll_addExerciseView.visibility = View.VISIBLE
                                    topBlurView1.visibility = View.VISIBLE
                                    ll_selectAll.visibility = View.GONE
                                }
                                updatingViewAccordingToSelections(count, exerciseListSize)
                            }

                        }

                        override fun replaceExercise(parentPosition: Int, childPosition: Int) {

                        }


                    },
                    this,object : SetAndRepsAdapter.StopScroll {
                        override fun stopScrolling(isScroll: Boolean) {
                            Log.d("TAG", "stopScrolling: ${isScroll}")
                            //binding.svMain.setScrollingEnabled(isScroll)
                        }
                        override fun scrollToPosition(position: Int, y: Float) {
                            Handler().postDelayed({
                                if (((y * 100) / mainViewTotalHeight) >= 80) {
                                    binding.svMain.smoothScrollBy(
                                        0,
                                        ((position + (((y  *100) / mainViewTotalHeight)  *2)).toInt())
                                    )
                                } else if (((y * 100) / mainViewTotalHeight) >= 75) {
                                    binding.svMain.smoothScrollBy(
                                        0,
                                        ((position + (((y  *100) / mainViewTotalHeight)  *1.2)).toInt())
                                    )
                                } else if (((y  *100) / mainViewTotalHeight) <= 75 && ((y * 100) / mainViewTotalHeight) >= 55) {
                                    binding.svMain.smoothScrollBy(
                                        0,position
    //                                ((position + (((y  100) / mainViewTotalHeight)  0.5)).toInt())
                                    )
                                }
                                Log.d("TAG", "scrollToPosition: ${((y * 100) / mainViewTotalHeight)}")
                            }, 1000)
                        }
                    },
                    supportFragmentManager
                )

            val callback = SimpleItemTouchHelperCallbackForWorkout(adapterSetAndRepsAddedExerciseAdapter, binding.svMain)
            mItemTouchHelper = ItemTouchHelper(callback)
            val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
            binding.exerciseRv.layoutManager = layoutManager
            mItemTouchHelper.attachToRecyclerView(binding.exerciseRv)
            binding.exerciseRv.adapter = adapterSetAndRepsAddedExerciseAdapter
            saperator()*/
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
    }

    fun updatingViewAccordingToSelections(count: Int, exerciseListSize: Int) {
        val filteredList =
            roundList.forEach { it1 -> it1.arrSets.forEach { it2 -> it2.exerciseList.filter { it.isSelectedExercise } } }
        if (roundList.isEmpty()) {
            isAllExerciseSelected = false
            Glide.with(this).load(R.drawable.deselected_create_workout)
                .into(binding.selectSelectDeselectAll)
        } else if (count < exerciseListSize) {
            isAllExerciseSelected = false
            Glide.with(this).load(R.drawable.deselected_create_workout)
                .into(binding.selectSelectDeselectAll)
        } else if (count == exerciseListSize) {
            isAllExerciseSelected = true
            Glide.with(this).load(R.drawable.selected_circle_blue_)
                .into(binding.selectSelectDeselectAll)
        }

        if (count == 1) {
            binding.tvUngroup.isEnabled = false
            binding.tvMarkAs.isEnabled = false
            binding.tvDeleteFromRounds.isEnabled = true

            binding.tvUngroup.setTextColor(
                ContextCompat.getColor(
                    this@NewPlayerView, R.color.deselect_color
                )
            )

            binding.tvMarkAs.setTextColor(
                ContextCompat.getColor(
                    this@NewPlayerView, R.color.deselect_color
                )
            )

        } else if (count >= 2) {
            binding.tvUngroup.isEnabled = true
            binding.tvMarkAs.isEnabled = true
            binding.tvDeleteFromRounds.isEnabled = true


            binding.tvUngroup.setTextColor(
                ContextCompat.getColor(
                    this@NewPlayerView, R.color.colorWhite
                )
            )

            binding.tvMarkAs.setTextColor(
                ContextCompat.getColor(
                    this@NewPlayerView, R.color.colorWhite
                )
            )
        }
    }


    private fun showCreatePlanAndWorkOutDialog(
        exerciseData: ExerciseListingResponse.Data,
        esercisePos: Int,
        exercisesList: java.util.ArrayList<ExerciseListingResponse.Data>,
        Roundpos: Int
    ) {

        ReplaceParentPsotion = Roundpos
        repalceChildPosition = esercisePos
        exerciseDataofItem = exerciseData

        tempList = java.util.ArrayList()

        var getExerciseData = exercisesList.get(esercisePos)

        var replaceModalData = ExerciseListingResponse.Data(
            exercise_access_level = getExerciseData.exercise_access_level,
            exercise_amount = getExerciseData.exercise_amount,
            exercise_amount_display = getExerciseData.exercise_amount_display,
            exercise_body_parts = getExerciseData.exercise_body_parts,
            exercise_description = getExerciseData.exercise_description,
            exercise_equipments = getExerciseData.exercise_equipments,
            exercise_id = getExerciseData.exercise_id,
            exercise_image = getExerciseData.exercise_image,
            exercise_is_favourite = getExerciseData.exercise_is_favourite,
            exercise_level = getExerciseData.exercise_level,
            exercise_name = getExerciseData.exercise_name,
            exercise_share_url = getExerciseData.exercise_share_url,
            exercise_tags = getExerciseData.exercise_tags,
            exercise_video = getExerciseData.exercise_video,
            is_liked = getExerciseData.is_liked,
            isPlaying = getExerciseData.isPlaying,
            isSelected = getExerciseData.isSelected,
            isReps = getExerciseData.isReps,
            exercise_timer_time = exerciseDataofItem.exercise_timer_time,
            exercise_reps_time = exerciseDataofItem.exercise_reps_time,
            exercise_reps_number = exerciseDataofItem.exercise_reps_number,
            exercise_rest_time = exerciseDataofItem.exercise_rest_time,
            isValid = exerciseDataofItem.isValid,
            showLoader = getExerciseData.showLoader,
            isClicked = getExerciseData.isClicked,
            videoLength = getExerciseData.videoLength
        )
        tempList.add(replaceModalData)
        if (tempList.size != 0 && tempList.size > 1) {
            val menus = mutableListOf<ExchangDialogMenu>()
            menus.add(ExchangDialogMenu("Change this exercise only", "2"))
            menus.add(
                ExchangDialogMenu(
                    "Change all of the repeated exercise of this demonstration",
                    "2"
                )
            )
            val dialogFragment = EcerciseChngeDialogFragment.newInstance("forSelectPlan")
            dialogFragment.addMenu(menus)
            dialogFragment.addDialogEventListener(this)
            dialogFragment.show(supportFragmentManager!!, "forSelectPlan")
        } else {
            val menus = mutableListOf<ExchangDialogMenu>()
            menus.add(ExchangDialogMenu("Change this exercise only", "2"))
            menus.add(
                ExchangDialogMenu(
                    "Change all of the repeated exercise of this demonstration",
                    "1"
                )
            )
            val dialogFragment = EcerciseChngeDialogFragment.newInstance("forSelectPlan")
            dialogFragment.addMenu(menus)
            dialogFragment.addDialogEventListener(this)
            dialogFragment.show(supportFragmentManager!!, "forSelectPlan")
        }
    }


    override fun onItemClicked(mCategoryTag: String, mMenuTag: String, position: Int) {
        if (mMenuTag == "Change this exercise only") {
            if (itemPosition != -1) {
                var intent = Intent(getActivity(), AddExerciseActivity::class.java)
                intent.putExtra("forReplace", "forReplace")
                intent.putExtra("workout_Type", exerciseType)
                startActivityForResult(intent, 111)
            }

        } else if (mMenuTag == "Change all of the repeated exercise of this demonstration") {

            tempList.clear()

            var exerciseModel =
                roundList[ReplaceParentPsotion].arrSets[0].exerciseList[repalceChildPosition]
            roundList.forEach {
                it.arrSets[0].exerciseList.forEach { exer ->
                    if (exerciseModel.exercise_id == exer.exercise_id) {
                        ReplaceExerciseId = exerciseModel.exercise_id
                        tempList.add(exer)
                    }
                }
            }
            if (tempList != null && tempList.size != 0 && tempList.size > 1) {
                var intent = Intent(this@NewPlayerView, AddExerciseActivity::class.java)
                intent.putExtra("forReplace", "forReplace")
                intent.putExtra("workout_Type", exerciseType)
                startActivityForResult(intent, 112)
            }
        }
    }

    override fun onDialogDismiss() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            // for replacing multiple duplicates
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>
            for (i in 0 until tempSelectedExerciseList.size) {
                var replaceExercisesModal = tempSelectedExerciseList[i]
                if (replaceExercisesModal.isSelected) {
                    val exerciseData =
                        roundList[ReplaceParentPsotion].arrSets[0].exerciseList[repalceChildPosition]

                    roundList.forEach { round ->
                        val setsListInRound = round.arrSets
                        setsListInRound.forEach { it ->
                            for (i in 0 until it.exerciseList.size) {
                                if (it.exerciseList[i].exercise_id == exerciseData.exercise_id) {
                                    it.exerciseList.removeAt(i)
                                    it.exerciseList.add(
                                        i,
                                        ExerciseListingResponse.Data(
                                            exercise_access_level = replaceExercisesModal.exercise_access_level,
                                            exercise_amount = replaceExercisesModal.exercise_amount,
                                            exercise_amount_display = replaceExercisesModal.exercise_amount_display,
                                            exercise_body_parts = replaceExercisesModal.exercise_body_parts,
                                            exercise_description = replaceExercisesModal.exercise_description,
                                            exercise_equipments = replaceExercisesModal.exercise_equipments,
                                            exercise_id = replaceExercisesModal.exercise_id,
                                            exercise_image = replaceExercisesModal.exercise_image,
                                            exercise_is_favourite = replaceExercisesModal.exercise_is_favourite,
                                            exercise_level = replaceExercisesModal.exercise_level,
                                            exercise_name = replaceExercisesModal.exercise_name,
                                            exercise_share_url = replaceExercisesModal.exercise_share_url,
                                            exercise_tags = replaceExercisesModal.exercise_tags,
                                            exercise_video = replaceExercisesModal.exercise_video,
                                            is_liked = replaceExercisesModal.is_liked,
                                            isPlaying = replaceExercisesModal.isPlaying,
                                            isSelected = replaceExercisesModal.isSelected,
                                            isReps = exerciseDataofItem.isReps,
                                            exercise_timer_time = exerciseDataofItem.exercise_timer_time,
                                            exercise_reps_time = exerciseDataofItem.exercise_reps_time,
                                            exercise_reps_number = exerciseDataofItem.exercise_reps_time!!,
                                            exercise_rest_time = exerciseDataofItem.exercise_rest_time,
                                            isValid = exerciseDataofItem.isValid,
                                            showLoader = replaceExercisesModal.showLoader,
                                            isClicked = replaceExercisesModal.isClicked,
                                            videoLength = replaceExercisesModal.videoLength,
                                            isRoundSelectedForPlayer = roundList[ReplaceParentPsotion].isRoundSelectedForPlayer
                                        )
                                    )
                                }
                            }
                        }
                    }
                    if (tempList.isEmpty())
                        binding.divider.visibility = View.VISIBLE
                    else
                        binding.divider.visibility = View.GONE
                }
            }

            roundList[ReplaceParentPsotion].isRoundSelectedForPlayer = false
            playerRoundAdapter.notifyDataSetChanged()

        } else if (requestCode == 111 && resultCode == Activity.RESULT_OK) {// code for replace single exercise
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>
            try {
                for (i in 0 until tempSelectedExerciseList.size) {
                    var replaceExercisesModal = tempSelectedExerciseList[i]
                    if (replaceExercisesModal.isSelected) {
                        var replaceModalData = ExerciseListingResponse.Data(
                            exercise_access_level = replaceExercisesModal.exercise_access_level,
                            exercise_amount = replaceExercisesModal.exercise_amount,
                            exercise_amount_display = replaceExercisesModal.exercise_amount_display,
                            exercise_body_parts = replaceExercisesModal.exercise_body_parts,
                            exercise_description = replaceExercisesModal.exercise_description,
                            exercise_equipments = replaceExercisesModal.exercise_equipments,
                            exercise_id = replaceExercisesModal.exercise_id,
                            exercise_image = replaceExercisesModal.exercise_image,
                            exercise_is_favourite = replaceExercisesModal.exercise_is_favourite,
                            exercise_level = replaceExercisesModal.exercise_level,
                            exercise_name = replaceExercisesModal.exercise_name,
                            exercise_share_url = replaceExercisesModal.exercise_share_url,
                            exercise_tags = replaceExercisesModal.exercise_tags,
                            exercise_video = replaceExercisesModal.exercise_video,
                            is_liked = replaceExercisesModal.is_liked,
                            isPlaying = replaceExercisesModal.isPlaying,
                            isSelected = replaceExercisesModal.isSelected,
                            isReps = exerciseDataofItem.isReps,
                            exercise_timer_time = exerciseDataofItem.exercise_timer_time,
                            exercise_reps_time = exerciseDataofItem.exercise_reps_time,
                            exercise_reps_number = exerciseDataofItem.exercise_reps_time!!,
                            exercise_rest_time = exerciseDataofItem.exercise_rest_time,
                            isValid = exerciseDataofItem.isValid,
                            showLoader = replaceExercisesModal.showLoader,
                            isClicked = replaceExercisesModal.isClicked,
                            videoLength = replaceExercisesModal.videoLength,
                            isRoundSelectedForPlayer = roundList[ReplaceParentPsotion].isRoundSelectedForPlayer
                        )


                        val data = roundList[ReplaceParentPsotion].arrSets
                        for (i in 0 until data.size) {
                            data[i].exerciseList.removeAt(repalceChildPosition)
                            data[i].exerciseList.add(repalceChildPosition, replaceModalData)
                        }

                        roundList[ReplaceParentPsotion].arrSets = data
                        roundList[ReplaceParentPsotion].isRoundPositionPopupOIsVisible = false


                    }
                }
                playerRoundAdapter.notifyItemChanged(ReplaceParentPsotion)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        /** Adding note in workout and every round individually */
        else if (requestCode == 501 && resultCode == Activity.RESULT_OK) {
            var notes = data!!.getStringExtra("NotesFor")!!
            var notesText = data.getStringExtra("NoteText")!!
            try {
                if (notes == "CompleteWorkout") {
                    noteForExercise = notesText
                } else if (notes == "OnParticularRound") {
                    roundList[positionForAddNotes].noteForExerciseInRound = notesText
                    playerRoundAdapter.notifyItemChanged(positionForAddNotes)
                    positionForAddNotes = -1
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        /**adding exercise in round*/
        else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            var flag = roundList[roundPositionForAddExercise].isRoundSelectedForPlayer
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>

            for (i in 0 until tempSelectedExerciseList.size) {
                var getExerciseData = tempSelectedExerciseList.get(i)

                val replaceModalData = ExerciseListingResponse.Data(
                    exercise_access_level = getExerciseData.exercise_access_level,
                    exercise_amount = getExerciseData.exercise_amount,
                    exercise_amount_display = getExerciseData.exercise_amount_display,
                    exercise_body_parts = getExerciseData.exercise_body_parts,
                    exercise_description = getExerciseData.exercise_description,
                    exercise_equipments = getExerciseData.exercise_equipments,
                    exercise_id = getExerciseData.exercise_id,
                    exercise_image = getExerciseData.exercise_image,
                    exercise_is_favourite = getExerciseData.exercise_is_favourite,
                    exercise_level = getExerciseData.exercise_level,
                    exercise_name = getExerciseData.exercise_name,
                    exercise_share_url = getExerciseData.exercise_share_url,
                    exercise_tags = getExerciseData.exercise_tags,
                    exercise_video = getExerciseData.exercise_video,
                    is_liked = getExerciseData.is_liked,
                    isPlaying = getExerciseData.isPlaying,
                    isSelected = getExerciseData.isSelected,
                    isReps = getExerciseData.isReps,
                    exercise_timer_time = getExerciseData.exercise_timer_time,
                    exercise_reps_time = getExerciseData.exercise_reps_time,
                    exercise_reps_number = getExerciseData.exercise_reps_number,
                    exercise_rest_time = getExerciseData.exercise_rest_time,
                    isValid = getExerciseData.isValid,
                    showLoader = getExerciseData.showLoader,
                    isClicked = getExerciseData.isClicked,
                    videoLength = getExerciseData.videoLength,
                    isExercisePopupVisible = false,
                    isRoundSelectedForPlayer = flag
                )
                // selectedExerciseList.set(j, replaceModalData)
                val roundData = roundList[roundPositionForAddExercise].arrSets
                roundData.forEach {
                    it.exerciseList.add(replaceModalData)
                }
            }
            if (roundList[roundPositionForAddExercise].arrSets[0].exerciseList.size == 1) {
                roundList[roundPositionForAddExercise].strRoundName = ""
            } else if (roundList[roundPositionForAddExercise].arrSets[0].exerciseList.size == 2) {
                if (roundList[roundPositionForAddExercise].strRoundName.isEmpty()) roundList[roundPositionForAddExercise].strRoundName =
                    "Superset"
            } else if (roundList[roundPositionForAddExercise].arrSets[0].exerciseList.size > 2) {
                roundList[roundPositionForAddExercise].strRoundName = "Superset"
            }
            roundList[roundPositionForAddExercise].isRoundPositionPopupOIsVisible = false
            playerRoundAdapter.notifyItemChanged(roundPositionForAddExercise)
            playerRoundAdapter.notifyadapters()
            roundPositionForAddExercise = -1
            /**blow old code is commented*/

        } else if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            //equipment=exercise_equipments
            //Good for=exercise_body_parts

            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as java.util.ArrayList<ExerciseListingResponse.Data>
            if (tempSelectedExerciseList.size > 0) {
                for (i in 0 until tempSelectedExerciseList.size) {
                    tempSelectedExerciseList[i].exercise_reps_number =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_NUMBER_OF_REPS)!!
                    tempSelectedExerciseList[i].exercise_reps_time =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_REPS_FINISH_TIME)!!
                    tempSelectedExerciseList[i].exercise_timer_time =
                        getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_EXERCISE_TIME)!!
                    tempSelectedExerciseList[i].exercise_rest_time = "00:00"
                    tempSelectedExerciseList[i].isValid = true
                }
            }
            var targetReps = ""
            var targetSets = ""
            if (exerciseType == "SetAndReps") {
                targetReps =
                    getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)
                        .toString()
                targetSets =
                    getDataManager().getStringData(AppPreferencesHelper.PREF_KEY_NUMBER_OF_SETS_SETANDREPS)
                        .toString()
                //  val targetReps = binding.repsTimerTxt1.text.toString().trim()
                //  val targetSets = binding.repetetionTxt1.text.toString().toInt()
                var roundName = ""
                if (tempSelectedExerciseList[0].leftAndRightOrSuperSetOrAddExercise == "LeftAndRight") roundName =
                    "Left & Right"
                else roundName = tempSelectedExerciseList[0].leftAndRightOrSuperSetOrAddExercise!!


                if (roundName.isEmpty()) {
                    var list = java.util.ArrayList<ExerciseListingResponse.Data>()
                    tempSelectedExerciseList.forEach { list.add(CommanUtils.addDuplicateExercise1(it)) }

                    if (roundPositionForAddExercise >= 0) {
                        roundList[roundPositionForAddExercise].arrSets.forEach {
                            it.exerciseList.addAll(list)
                        }
                        roundPositionForAddExercise = -1
                    } else {

                        for (i in 1..tempSelectedExerciseList.size) {
                            roundList.add(
                                SetAndRepsModel(
                                    strRoundCounts = "",
                                    strRoundName = roundName,
                                    strTargetSets = "$targetSets",
                                    strTargetReps = "$targetReps",
                                    strExerciseType = roundName,
                                    strSetsCounts = "",
                                    arrSets = getSets(
                                        targetSets.toInt(),
                                        tempSelectedExerciseList[i - 1],
                                        roundName
                                    )
                                )
                            )
                        }
                    }


                } else {
                    val setsList = java.util.ArrayList<SetSModel>()
                    for (i in 1..targetSets.toInt()) {
                        var flag = false
                        if (i == 1) flag = true
                        val newArrayList = java.util.ArrayList<ExerciseListingResponse.Data>()
                        tempSelectedExerciseList.forEach {
                            newArrayList.add(
                                CommanUtils.addDuplicateExercise1(
                                    it
                                )
                            )
                        }
                        println("IndexToPrintRefrence : ${System.identityHashCode(newArrayList)}")

                        setsList.add(
                            SetSModel(
                                setName = "SET $i",
                                exerciseList = newArrayList,
                                strExerciseType = roundName,
                                isSelected = flag
                            )
                        )
                    }


                    if (roundPositionForAddExercise >= 0) {
                        val newArrayList = java.util.ArrayList<ExerciseListingResponse.Data>()
                        tempSelectedExerciseList.forEach {
                            newArrayList.add(
                                CommanUtils.addDuplicateExercise1(
                                    it
                                )
                            )
                        }
                        roundList[roundPositionForAddExercise].arrSets.forEach {
                            it.exerciseList.addAll(
                                newArrayList
                            )
                        }
                        roundPositionForAddExercise = -1
                    } else {
                        roundList.add(
                            SetAndRepsModel(
                                strRoundCounts = "",
                                strRoundName = roundName,
                                strTargetSets = "$targetSets",
                                strTargetReps = "$targetReps",
                                strExerciseType = roundName,
                                strSetsCounts = "",
                                arrSets = setsList
                            )
                        )
                    }


                }

                playerRoundAdapter.notifyDataSetChanged()


            }


        }

    }


    /*    private fun blink() {
            val duration = 60 * 60 * 1000L
            var timer = 1
            // Create a CountDownTimer with the specified duration and interval
            countDownTimer = object : CountDownTimer(duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timer++
                    // Convert the remaining time to minutes and seconds
                    val minutes = (millisUntilFinished / 1000) / 60
                    val seconds = (millisUntilFinished / 1000) % 60

                    // Format the time and update the TextView
                    val time = String.format("%02d:%02d", minutes, seconds)
                    binding.timer.text = time
                  */
    /*  val minutes = timer / 60000
                val seconds = (timer % 60000) / 1000

                // Format the time and update the TextView
                val time = String.format("%02d:%02d", minutes, seconds)
                binding.timer.text = time*/
    /*
            }

            override fun onFinish() {
                // Handle the timer finishing (if needed)
                binding.timer.text = "00:00"
            }
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }


    private val runnable = object : Runnable {
        override fun run() {
            elapsedTime += 1000
            val hours = (elapsedTime / 3600000).toInt()
            val minutes = ((elapsedTime % 3600000) / 60000).toInt()
            val seconds = ((elapsedTime % 60000) / 1000).toInt()

            binding.timer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

            handler.postDelayed(this, 1000)
        }
    }

    /*   private fun startTimerNew() {
           if (!isTimerRunning) {
               binding.timer.base = System.currentTimeMillis() - elapsedTime
               binding.timer.start()
              // binding.timer.text= (System.currentTimeMillis() - elapsedTime).toString()
               handler.postDelayed(runnable, 1000)
               isTimerRunning = true
           }
       }

       private fun pauseTimer() {
           if (isTimerRunning) {
               binding.timer.stop()
               handler.removeCallbacks(runnable)

               isTimerRunning = false
           }
       }

       private fun resumeTimer() {
           if (!isTimerRunning) {
            binding.timer.base = System.currentTimeMillis() - elapsedTime
               binding.timer.start()
               handler.postDelayed(runnable, 1000)
               isTimerRunning = true
           }
       }*/


    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds += 1
                updateStopWatchView(timeInSeconds)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler!!.postDelayed(this, mInterval.toLong())
            }
        }
    }

    fun music() {
        CommanUtils.lastClick()
        try {
            /*val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
            startActivity(intent)*/
            if (isMusicPlayerInstalled()) {
                launchDefaultMusicPlayer()
            }

        } catch (e: Exception) {
            //launchDefaultMusicPlayer()
            e.printStackTrace()
        }
    }

    private fun isMusicPlayerInstalled(): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.type = "audio/*"
        val packageManager: PackageManager = packageManager
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        return activities.isNotEmpty()
    }

    private fun launchDefaultMusicPlayer() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.type = "audio/*"
        startActivity(intent)
    }


    private fun updateStopWatchView(timeInSeconds: Long) {
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        Log.e("formattedTime", formattedTime)
        binding?.timer?.text = formattedTime
    }

    private fun stopTimer() {
        mHandler?.removeCallbacks(mStatusChecker)
    }

    fun getFormattedStopWatch(ms: Long): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }///*"${if (hours < 10) "0" else ""}$hours:" +*/
}