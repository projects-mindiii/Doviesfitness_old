package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Html
import android.text.InputType
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Scroller
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.allDialogs.ErrorDialog
import com.doviesfitness.allDialogs.menu.EcerciseChngeDialogFragment
import com.doviesfitness.allDialogs.menu.ExchangDialogMenu
import com.doviesfitness.allDialogs.menu.ExerciseMarkAsDialogFragment
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_APP_IS_ADMIN
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_EXERCISE_REST_TIME
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_EXERCISE_REST_TIME_SETANDREPS
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_EXERCISE_TIME
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_EXERCISE_TIME_SETANDREPS
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_NUMBER_OF_REPS
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_NUMBER_OF_SETS_SETANDREPS
import com.doviesfitness.data.local.AppPreferencesHelper.Companion.PREF_KEY_REPS_FINISH_TIME
import com.doviesfitness.data.remote.Webservice
import com.doviesfitness.databinding.ActivityCreateWorkoutBinding
import com.doviesfitness.ui.authentication.signup.dialog.ImagePickerDialog
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.ExerciseTransData
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetailResponce
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutGroupsData
import com.doviesfitness.ui.bottom_tabbar.rv_swap.OnStartDragListener
import com.doviesfitness.ui.bottom_tabbar.rv_swap.SimpleItemTouchHelperCallbackForWorkout
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.AddedExerciseAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SelectedExerciseAdapterSetAndReps
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.SetAndRepsAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.CustomerListModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetAndRepsModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetSModel
import com.doviesfitness.utils.*
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_create_work_out_plan_activty.equipmemnts
import kotlinx.android.synthetic.main.activity_create_work_out_plan_activty.good_for
import kotlinx.android.synthetic.main.activity_create_work_out_plan_activty.txt_good_for
import kotlinx.android.synthetic.main.activity_create_workout.*
import kotlinx.android.synthetic.main.activity_privacypolicy_and_tc.*
import kotlinx.android.synthetic.main.filter_fitness_dialog_view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class CreateWorkoutActivity : BaseActivity(), View.OnClickListener,
    WorkoutLevelDialog.HeightWeightCallBack, TimerDialog.HeightWeightCallBack,
    AddedExerciseAdapter.OnItemClick, AddedExerciseAdapter.SelectTime, SetAndRepsAdapter.SelectTime,
    SetAndRepsAdapter.OnItemClick, FinishActivityDialog.IsDelete, OnStartDragListener,
    SaveEditWorkoutDialog.CommentCallBack, ErrorDialog.IsOK,
    EcerciseChngeDialogFragment.DialogEventListener,
    ChangeExersiseTypeDialog.IsYesClickedForTypeChanged {

    override fun isOk() {
        //gfhjkll
    }

    override fun overwriteClick(type: String) {
        /// overwrite current workout
        editWorkout(exerciseArray.toString())
    }

    override fun textOnClick1(type: String) {
        ///save as new workout
        createWorkout(exerciseArray.toString())
    }

    /**changing for multiple delete */
    override fun isDelete() {
        selectedExerciseList.removeAll { it.isSelectedExercise }// removing all items which are selected
        adapter.notifyItemRemoved(itemPos)
        adapter.notifyDataSetChanged()
        selected_count.text = "0 Selected"
        addGoodForAndEquipment(SetsAndRepsList)
        if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility = View.VISIBLE
        else binding.seperatorLine.visibility = View.GONE
        updatingViewAccordingToSelections(0, 0)
    }

    private lateinit var eqpmntList: ArrayList<String>
    private lateinit var arrayListGoodFor: ArrayList<String>
    var positionForAddNotes = -1
    private var exercise_equipments = ArrayList<String>()
    private var exercise_body_parts = ArrayList<String>()
    private var ReplaceExerciseId = ""
    private var roundPositionForAddExercise = -1
    var ReplaceParentPsotion = -1
    var repalceChildPosition = -1

    private var exerciseType: String = ""
    private var selectedExerciseequipmentList: String = ""
    private var selectedExerciseGoodForList: String = ""

    private var isAllExerciseSelected = false
    private var isSetRepsSelected: Boolean = true
    private var isAlongSelected: Boolean = true
    private lateinit var tempList: ArrayList<ExerciseListingResponse.Data>
    private lateinit var exerciseDataofItem: ExerciseListingResponse.Data
    private var itemPosition: Int = 0
    private var Rpoundpos: Int = 0
    private var allowed_user_id: String = ""
    lateinit var binding: ActivityCreateWorkoutBinding
    var exerciseList = ArrayList<FilterExerciseResponse.Data>()
    var goodForList = ArrayList<FilterExerciseResponse.Data.X>()
    var selectedExerciseList = ArrayList<ExerciseListingResponse.Data>()

    var tempAllowedUserList = ArrayList<CustomerListModel.Data.User>()
    var editTempAllowedUserList = ArrayList<CustomerListModel.Data.User>()
    var equipmentList = ArrayList<String>()
    var userImageFile: File? = null
    var value = ""
    var min = ""
    var sec = ""
    var selectedIndex = 0
    var accessLevelValue = ""
    var createTime = ""
    var equipmentIdStr = ""
    var goodForIdStr = ""
    var createByStr = ""
    var createById = ""
    var accessLevelStr = ""
    var displayNewsfeedstr = ""
    var displayNewsfeedValue = ""
    var allowNotificationstr = ""
    var allowNotificationvalue = ""
    var userImageUrl = ""
    var isAdmin = "No"
    var itemPos = 0
    lateinit var userImgFile: File
    var exerciseArray = JSONArray()
    private var width: Int = 0
    lateinit var adapter: AddedExerciseAdapter
    lateinit var adapterSetAndRepsAddedExerciseAdapter: SetAndRepsAdapter
    var SetsAndRepsList = ArrayList<SetAndRepsModel>()
    lateinit var dialog: Dialog

    private var refid: Long = 0
    internal var list = ArrayList<Long>()
    private var downloadManager: DownloadManager? = null
    lateinit var mItemTouchHelper: ItemTouchHelper
    lateinit var workerDetail: WorkoutDetailResponce
    var isEdit = ""
    var isMyWorkout = ""
    var myWorkoutFragment = ""
    var noteForExercise = ""
    var totalWorkoutTime = ""
    lateinit var WDetail: WorkoutDetail
    var mainViewTotalHeight = 0

    /* var resultLauncherAddres =
         registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
             if (result.resultCode == Activity.RESULT_OK) {

             }

         }*/

    var mFollowAlongList = ArrayList<ExerciseListingResponse.Data>()
    var mSetAndRepsList = ArrayList<SetAndRepsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_workout)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        initialization()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialization() {
        if (getDataManager().getStringData(PREF_KEY_REPS_FINISH_TIME)!!.isEmpty()) {
            getDataManager().setStringData(PREF_KEY_REPS_FINISH_TIME, "00:30")
            binding.repsTimerTxt.text = "00:30"
        } else {
            binding.repsTimerTxt.text =
                getDataManager().getStringData(PREF_KEY_REPS_FINISH_TIME)
        }
        if (getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)!!
                .isEmpty()
        ) {
            getDataManager().setStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS, "10 To 12")
            binding.repsTimerTxt1.text = "10 To 12"
        } else if (getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)!!
                .contains(" Reps")
        ) {
            binding.repsTimerTxt1.text =
                getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)
        } else {
            binding.repsTimerTxt1.text =
                getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)
        }

        if (getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)!!.isEmpty()) {
            getDataManager().setStringData(PREF_KEY_EXERCISE_TIME, "00:30")
            binding.timeExerciseTxt.text = "00:30"
        } else {
            binding.timeExerciseTxt.text =
                getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)
        }

        if (getDataManager().getStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS)!!.isEmpty()) {
            getDataManager().setStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS, "00:30")
            binding.timeExerciseTxt1.text = "00:30"
        } else {
            binding.timeExerciseTxt1.text =
                getDataManager().getStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS)
        }

        if (getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME)!!.isEmpty()) {
            getDataManager().setStringData(PREF_KEY_EXERCISE_REST_TIME, "00:30")
        } else {
        }

        if (getDataManager().getStringData(PREF_KEY_NUMBER_OF_REPS)!!.isEmpty()) {
            getDataManager().setStringData(PREF_KEY_NUMBER_OF_REPS, "10")
            binding.repetetionTxt.text = "10"
        } else {
            binding.repetetionTxt.text = getDataManager().getStringData(PREF_KEY_NUMBER_OF_REPS)

        }
        if (getDataManager().getStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS)!!.isEmpty()) {
            getDataManager().setStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS, "10")
            binding.repetetionTxt1.text = "10"
        } else {
            binding.repetetionTxt1.text =
                getDataManager().getStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS)

        }

        /*    if (getDataManager().getStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS)!!.isEmpty()) {
                getDataManager().setStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS, "10")
                binding.repetetionTxt1.text = "10"
            } else {
                binding.repetetionTxt1.text =
                    getDataManager().getStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS)

            }

            if (getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)!!.isEmpty()) {
                getDataManager().setStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS, "10 To 12")
                binding.repsTimerTxt1.text = "10 To 12"
            } else if (getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)!!
                    .contains(" Reps")
            ) {
                binding.repsTimerTxt1.text =
                    getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)
            } else {
                binding.repsTimerTxt1.text =
                    getDataManager().getStringData(PREF_KEY_EXERCISE_REST_TIME_SETANDREPS)
            }


            if (getDataManager().getStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS)!!.isEmpty()) {
                getDataManager().setStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS, "00:30")
                binding.timeExerciseTxt1.text = "00:30"
            } else {
                binding.timeExerciseTxt1.text =
                    getDataManager().getStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS)
            }*/

        hideNavigationBar()
        // SetsAndRepsList = ArrayList()
        val windowBackground = window.decorView.background
        binding.topBlurView1.setupWith(binding.containerId).setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this)).setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)

        isAdmin = getDataManager().getUserStringData(PREF_KEY_APP_IS_ADMIN)!!
        isAdminUser()
        binding.overview.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.overview.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.overview.movementMethod = ScrollingMovementMethod()

        binding.overview.setOnTouchListener { v, event ->
            if (binding.overview.hasFocus()) {
                binding.svMain.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        binding.svMain.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                }
            }
            false
        }

        binding.overview.setScroller(Scroller(this))
        binding.overview.isVerticalScrollBarEnabled = true
        binding.overview.movementMethod = ScrollingMovementMethod()

        binding.overview.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    binding.overview.isFocusable = false
                    binding.overview.isFocusableInTouchMode = false
                    hideNavigationBar()
                    return true
                }
                return false
            }
        })

        isEdit = intent.getStringExtra("edit")!!


        binding.levelLayout.setOnClickListener(this)
        binding.containerId.setOnClickListener(this)
        binding.goodForLayout.setOnClickListener(this)
        binding.equipmentLayout.setOnClickListener(this)
        binding.createdByLayout.setOnClickListener(this)
        binding.allowedUsersLayout.setOnClickListener(this)
        binding.accessLevelLayout.setOnClickListener(this)
        binding.displayNewsfeedLayout.setOnClickListener(this)
        binding.allowNotificationLayout.setOnClickListener(this)
        binding.imgLayout.setOnClickListener(this)
        binding.timerExerciseLayout.setOnClickListener(this)
        binding.timerExerciseLayoutSetsReps.setOnClickListener(this)
        binding.repsTimerLayout.setOnClickListener(this)
        binding.repsTimerLayoutSetsReps.setOnClickListener(this)
        binding.repetetionLayout.setOnClickListener(this)
        binding.repetetionLayoutSetsReps.setOnClickListener(this)
        binding.showHideView.setOnClickListener(this)
        binding.showHideViewSetsReps.setOnClickListener(this)
        binding.addExerciseBtn.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
        binding.infoIcon.setOnClickListener(this)
        binding.progressLayout.setOnClickListener(this)
        binding.totalExerciseTime.setOnClickListener(this)
        binding.doneBtn.setOnClickListener(this)

        binding.viewTransParancy.setOnClickListener(this)
        binding.selectSetsRepsIcon.setOnClickListener(this)
        binding.followAlongIcon.setOnClickListener(this)
        binding.deleteIconCreateExercise.setOnClickListener(this)
        binding.DoneCreateWorkoutt.setOnClickListener(this)
        binding.selectSelectDeselectAll.setOnClickListener(this)
        binding.duplicateIcon.setOnClickListener(this)
        binding.tvMarkAs.setOnClickListener(this)
        binding.tvUngroup.setOnClickListener(this)
        binding.tvDeleteFromRounds.setOnClickListener(this)
        binding.llAddNote.setOnClickListener(this)
        binding.llDelete.setOnClickListener(this)
        binding.llDuplicate.setOnClickListener(this)

        val displaymetrics = DisplayMetrics()
        getActivity().windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        width = displaymetrics.widthPixels

        getFilterWorkoutData()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320
        val videowidth = 120 + (160 * screenWidth)

        selectedExerciseList = ArrayList<ExerciseListingResponse.Data>()

        if (intent.hasExtra("myWorkoutFragment")) myWorkoutFragment =
            intent.getStringExtra("myWorkoutFragment")!!


        if (isEdit.equals("edit")) {
            //  binding.progressLayout.visibility = View.VISIBLE
            binding.workoutName.isFocusable = true
            binding.workoutName.isFocusableInTouchMode = true
            isMyWorkout = intent.getStringExtra("isMyWorkout")!!
            WDetail = intent.getSerializableExtra("workoutDetail") as WorkoutDetail

            if (intent.getSerializableExtra("workoutExerciseList") != null) {
                var editExerciseList: ArrayList<WorkoutExercise> =
                    intent.getSerializableExtra("workoutExerciseList") as ArrayList<WorkoutExercise>
                for (i in 0..editExerciseList.size - 1) {
                    var workoutExercise: WorkoutExercise? = null
                    workoutExercise = editExerciseList.get(i)
                    var isReps = false
                    var repsTime = ""
                    var repsText = ""
                    var ExerciseTime = ""
                    var restTimetoken = workoutExercise.workout_exercise_break_time.split(":")
                    var exerciseTimeToken = workoutExercise.workout_exercise_time.split(":")
                    if (workoutExercise.workout_exercise_type.equals("Repeat", true)) {
                        isReps = true
                        repsTime = "" + exerciseTimeToken[1] + ":" + exerciseTimeToken[2]
                        ExerciseTime = getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)!!
                        repsText = workoutExercise.workout_repeat_text

                    } else {
                        isReps = false
                        ExerciseTime = "" + exerciseTimeToken[1] + ":" + exerciseTimeToken[2]
                        repsTime = getDataManager().getStringData(PREF_KEY_REPS_FINISH_TIME)!!
                        repsText = getDataManager().getStringData(PREF_KEY_NUMBER_OF_REPS)!!
                    }

                    var copyItem: ExerciseListingResponse.Data? = null
                    copyItem = ExerciseListingResponse.Data(
                        workoutExercise.exercise_access_level,
                        "",
                        "",
                        workoutExercise.workout_exercise_body_parts ?: "",
                        workoutExercise.workout_exercise_description,
                        workoutExercise.workout_exercise_equipments?: "",
                        workoutExercise.workout_exercises_id,
                        workoutExercise.workout_exercise_image,
                        workoutExercise.workout_exercise_is_favourite,
                        workoutExercise.workout_exercise_level,
                        workoutExercise.workout_exercise_name,
                        workoutExercise.exercise_share_url,
                        "",
                        workoutExercise.workout_exercise_video,
                        workoutExercise.is_liked,
                        workoutExercise.isPlaying,
                        false,
                        isReps,
                        ExerciseTime,
                        repsTime,
                        repsText,
                        "" + restTimetoken[1] + ":" + restTimetoken[2],
                        "",
                        workoutExercise.workout_exercise_break_time.takeLast(5),
                        true
                    )
                    selectedExerciseList.add(copyItem)
                }
                follow_along_icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@CreateWorkoutActivity, R.drawable.ic_check_mark
                    )
                )
                isAlongSelected = true
                binding.exerciseRv.visibility = View.VISIBLE
                adapter = AddedExerciseAdapter(
                    this@CreateWorkoutActivity,
                    selectedExerciseList,
                    this,
                    videowidth,
                    this,
                    object : AddedExerciseAdapter.StopScroll {
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
                                Log.d(
                                    "TAG",
                                    "scrollToPosition: ${((y * 100) / mainViewTotalHeight)}"
                                )
                            }, 1000)
                        }
                    },
                    this
                )
                val callback = SimpleItemTouchHelperCallbackForWorkout(adapter, binding.svMain)
                mItemTouchHelper = ItemTouchHelper(callback)
                val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
                binding.exerciseRv.layoutManager = layoutManager
                mItemTouchHelper.attachToRecyclerView(binding.exerciseRv)
                binding.exerciseRv.adapter = adapter
                //adapter set follow along
            }

            //setAndRepsExerciseList
          else  if (intent.getSerializableExtra("setAndRepsExerciseList") != null) {
                try {
                    var localList =
                        intent.getSerializableExtra("setAndRepsExerciseList") as ArrayList<SetAndRepsModel>
                    SetsAndRepsList.addAll(localList)
                    /*  localList.forEach {
                        var roundName = ""
                        if (it.iGroupType == "2") roundName = "Left & Right"
                        else if (it.iGroupType == "1") roundName = "Superset"
                        else roundName = ""


                        SetsAndRepsList.add(
                            SetAndRepsModel(
                                strExerciseType = "",
                                strRoundCounts = "",
                                strRoundName = "${roundName}",
                                strTargetSets = "${it.iTargetSets}",
                                strTargetReps = "${it.iTargetReps}",
                                strSetsCounts = "",
                                arrSets = getSetModel(it),
                                isRoundPositionPopupOIsVisible = false,
                                noteForExerciseInRound = "",
                                isPostnotifiedExerciseAdapter = false
                            )
                        )
                    }*/

                    SetsAndRepsList.forEach {
                        it.arrSets[0].isSelected = true
                        it.strSetsCounts = (it.arrSets.size).toString()
                    }
                    setAdapterSetAndReps()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("sdghdsk", "initialization: ${e.localizedMessage}")
                }

                /*sets and resp view*/
                setPadding(total_exercise_time, 15, 15, 15, 15)
                setAdapterSetAndReps()
                tv_add_note_total_time.text = "ADD NOTE"
                tv_title_sets.setTextColor(
                    ContextCompat.getColor(
                        this@CreateWorkoutActivity, R.color.colorWhite
                    )
                )
                //isSetRepsSelected = false
                select_sets_reps_icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@CreateWorkoutActivity, R.drawable.ic_check_mark
                    )
                )
                total_exercise_time.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@CreateWorkoutActivity, R.drawable.add_notes_icon
                    )
                )/*Along view*/
                tv_title_along.setTextColor(
                    ContextCompat.getColor(
                        this@CreateWorkoutActivity, R.color.deselect_color
                    )
                )

                exerciseType = "SetAndReps"
                isAlongSelected = false
                follow_along_icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@CreateWorkoutActivity, R.drawable.ic_circle_img
                    )
                )

            }

            binding.workoutName.hint = (Html.fromHtml(getString(R.string.workout_name)))
            binding.tvEquipment.text = getSpanningString(WDetail.workout_equipment!!)
            binding.equipmemnts.visibility = View.VISIBLE
            binding.goodFor.text = getSpanningString(WDetail.workout_good_for!!)
            binding.txtGoodFor.visibility = View.VISIBLE
            binding.workoutName.setText("" + WDetail.workout_name)
            binding.overview.setText("" + WDetail.workout_description)
            userImageUrl = WDetail.workout_image

            Glide.with(getActivity()).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL).load(WDetail.workout_image)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.image.setImageBitmap(resource)
                        userImageFile = File(cacheDir, "dovies")
                        userImageFile!!.createNewFile()
                        var bitmap = resource
                        var bos = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        var bitmapdata = bos.toByteArray()
                        var fos = FileOutputStream(userImageFile)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()
                        return true
                    }
                }).into(binding.image)

            binding.image1.visibility = View.GONE
            binding.imgTxt.visibility = View.GONE
            binding.levelName.text = getSpanningString(WDetail.workout_level)
            binding.goodLevel.visibility = View.VISIBLE
            if (isAdmin.equals("Yes", true)) {
                binding.allowedUsersTxt.text = getSpanningString(WDetail.allowedUsersName)
                allowed_user_id = WDetail.allowed_users
                getUsersIds(WDetail.allowed_users)
                binding.txtAllowed.visibility = View.VISIBLE

                var creator_name = ""
                if (intent.getStringExtra("creator_name") != null) creator_name =
                    intent.getStringExtra("creator_name")!!

                var dg_devios_guest_id = ""
                if (intent.getStringExtra("creator_name") != null) dg_devios_guest_id =
                    intent.getStringExtra("dg_devios_guest_id")!!

                if (creator_name.isEmpty()) creator_name = WDetail.creator_name

                if (dg_devios_guest_id.isEmpty()) dg_devios_guest_id = WDetail.creator_id


                createById = dg_devios_guest_id

                val builder = SpannableStringBuilder()
                var str1 = "Created by \n"
                builder.append(str1)
                val spanStr = SpannableString(creator_name)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(), R.color.created_by_color
                        )
                    ), 0, spanStr.length, 0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.createBy.text = ss
                var displayValue = getPickerValue("Access Level \n", WDetail.access_level)
                binding.accessLevel.text = displayValue
                value = ""
                accessLevelValue = WDetail.access_level

                displayValue = getPickerValue("Allow Notification \n", WDetail.allow_notification)
                binding.allowNotification.text = displayValue
                allowNotificationvalue = WDetail.allow_notification

                displayValue = getPickerValue("Display in Newsfeed? \n", WDetail.is_featured)
                binding.displayNewsfeed.text = displayValue
                displayNewsfeedValue = WDetail.is_featured
            }


        } else {
            binding.goodFor.hint = (Html.fromHtml(getString(R.string.good_for_str)))
            binding.workoutName.hint = (Html.fromHtml(getString(R.string.workout_name)))
            binding.levelName.hint = (Html.fromHtml(getString(R.string.level_str)))
            binding.tvEquipment.hint = (Html.fromHtml(getString(R.string.equipments_str)))
            binding.createBy.hint = (Html.fromHtml(getString(R.string.created_by)))
            binding.accessLevel.hint = (Html.fromHtml(getString(R.string.access_level)))
            // binding.accessLevel.hint = (Html.fromHtml(getString(R.string.access_level)));
            binding.displayNewsfeed.hint = (Html.fromHtml(getString(R.string.display_in_newsfeed)))
            binding.allowNotification.hint = (Html.fromHtml(getString(R.string.allow_notification)))




            if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility = View.VISIBLE
            else binding.seperatorLine.visibility = View.GONE

            val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
            binding.exerciseRv.layoutManager = layoutManager
            adapter = AddedExerciseAdapter(
                getActivity(),
                selectedExerciseList,
                this,
                videowidth,
                this,
                object : AddedExerciseAdapter.StopScroll {
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
                            Log.d("TAG", "scrollToPosition: ${((y * 100) / mainViewTotalHeight)}")
                        }, 1000)
                    }
                },
                this
            )

            val callback = SimpleItemTouchHelperCallbackForWorkout(adapter, binding.svMain)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper.attachToRecyclerView(binding.exerciseRv)
            binding.exerciseRv.adapter = adapter

            binding.workoutName.setOnTouchListener { v, event ->
                binding.workoutName.isFocusable = true
                binding.workoutName.isFocusableInTouchMode = true
                false
            }
            /**follow along button default selection*/
            defaultExerciseTypeSelection()



            /* val textSize = getResources().getDimensionPixelSize(R.dimen._9sdp);
             textView.setTextSize(textSize)*/

            saperator()
            hideViewWhenClickOutOfView(container_id)
            hideViewWhenClickOutOfView(sv_main)
            hideViewWhenClickOutOfView(exercise_rv)
        }
    }


    private fun getSetModel(setAndRepModel: WorkoutGroupsData): ArrayList<SetSModel> {
        var stesList = ArrayList<SetSModel>()

        setAndRepModel.groupSetsData.forEachIndexed { index, setsModel ->

            var flag = false
            if (index == 0) flag = true
            stesList.add(
                SetSModel(
                    setName = "SET ${index + 1}",
                    exerciseList = getExerciseListLatest(setsModel.exerciseTransData),
                    strExerciseType = "",
                    isSelected = flag
                )
            )
        }
        return stesList
    }

    fun getExerciseListLatest(exerciseTransData: List<ExerciseTransData>): ArrayList<ExerciseListingResponse.Data> {
        var list = ArrayList<ExerciseListingResponse.Data>()
        exerciseTransData.forEach { transeData ->
            list.add(CommanUtils.addDuplicateExercise2(transeData))
        }

        return list
    }


    /**ungrouping selected device*/
    private fun unGroupSelectedExercise() {
        var tempList = ArrayList<ExerciseListingResponse.Data>()

        for (i in 0 until SetsAndRepsList.size) {
            tempList.addAll(SetsAndRepsList[i].arrSets[0].exerciseList.filter { it.isSelectedExercise })
        }


        createNewRoundWithSelectedExercise(tempList)
    }

    private fun createNewRoundWithSelectedExercise(tempList: ArrayList<ExerciseListingResponse.Data>) {
        var targetReps = binding.repsTimerTxt1.text.toString().trim()
        val targetSets = binding.repetetionTxt1.text.toString().toInt()

        for (i in 1..tempList.size) {
            SetsAndRepsList.add(
                SetAndRepsModel(
                    strRoundCounts = "",
                    strRoundName = "",
                    strTargetSets = "$targetSets",
                    strTargetReps = "$targetReps",
                    strExerciseType = "",
                    strSetsCounts = "",
                    arrSets = getSets(
                        targetSets, tempList[i - 1], ""
                    )
                )
            )
        }


        for (i in 0 until SetsAndRepsList.size) {
            for (j in 0 until SetsAndRepsList[i].arrSets.size) {
                SetsAndRepsList[i].arrSets[j].exerciseList.removeAll { it.isSelectedExercise }
            }

            if (SetsAndRepsList[i].arrSets[0].exerciseList.isEmpty()) {
                SetsAndRepsList.removeAt(i)
            }
        }

        adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
        adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
        ll_mark_as_ungroup_delete.visibility = View.GONE
        ll_addExerciseView.visibility = View.VISIBLE
        topBlurView1.visibility = View.VISIBLE
        ll_selectAll.visibility = View.GONE
    }

    fun hideViewWhenClickOutOfView(overlayview_: View) {
        overlayview_.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (this::adapterSetAndRepsAddedExerciseAdapter.isInitialized) {
                        if (SetsAndRepsList.isNotEmpty()) {
                            adapterSetAndRepsAddedExerciseAdapter.notifyUiToReflectChanges()
                        }
                    }
                    true // Consume the touch event
                }

                else -> false
            }
        }
    }

    fun setAdapterSetAndReps() {
        binding.seperatorLine.visibility = View.VISIBLE
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320
        val videowidth = 120 + (160 * screenWidth)

        adapterSetAndRepsAddedExerciseAdapter = SetAndRepsAdapter(
            getActivity(),
            SetsAndRepsList,
            object : SetAndRepsAdapter.OnItemClick {
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
                        exerciseData, exerPos, exercisesList, Rpoundpos
                    )
                }
            },
            videowidth,
            object : SetAndRepsAdapter.SelectTime {
                override fun selectTime(timing: String, pos: Int, s: String, s1: String) {

                }

                override fun selectRepetition(
                    timing: String, pos: Int, exerciseRepsNumber: String
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
                            this@CreateWorkoutActivity, AddNotesActivity::class.java
                        ).putExtra(
                            "notesFor", "OnParticularRound"
                        ).putExtra("notesText", SetsAndRepsList[pos].noteForExerciseInRound), 501
                    )
                }

                override fun addExerciseInRound(pos: Int) {
                    roundPositionForAddExercise = pos
                    var intent = Intent(getActivity(), AddExerciseActivity::class.java)
                    intent.putExtra("workout_Type", exerciseType)
                    intent.putExtra("isAddingInRound", "Yes")
                    startActivityForResult(intent, 11)
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
                            ll_mark_as_ungroup_delete.visibility = View.VISIBLE
                            ll_addExerciseView.visibility = View.GONE
                            topBlurView1.visibility = View.GONE
                            ll_selectAll.visibility = View.VISIBLE

                        } else {
                            selected_count.text = "Select"
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
            this,
            object : SetAndRepsAdapter.StopScroll {
                override fun stopScrolling(isScroll: Boolean) {
                    Log.d("TAG", "stopScrolling: ${isScroll}")
                    //binding.svMain.setScrollingEnabled(isScroll)
                }

                override fun scrollToPosition(position: Int, y: Float) {
                    Handler().postDelayed({
                        if (((y * 100) / mainViewTotalHeight) >= 80) {
                            binding.svMain.smoothScrollBy(
                                0, ((position + (((y * 100) / mainViewTotalHeight) * 2)).toInt())
                            )
                        } else if (((y * 100) / mainViewTotalHeight) >= 75) {
                            binding.svMain.smoothScrollBy(
                                0, ((position + (((y * 100) / mainViewTotalHeight) * 1.2)).toInt())
                            )
                        } else if (((y * 100) / mainViewTotalHeight) <= 75 && ((y * 100) / mainViewTotalHeight) >= 55) {
                            binding.svMain.smoothScrollBy(
                                0, position
//                                ((position + (((y  100) / mainViewTotalHeight)  0.5)).toInt())
                            )
                        }
                        Log.d("TAG", "scrollToPosition: ${((y * 100) / mainViewTotalHeight)}")
                    }, 1000)
                }
            },
            supportFragmentManager
        )

        val callback = SimpleItemTouchHelperCallbackForWorkout(
            adapterSetAndRepsAddedExerciseAdapter, binding.svMain
        )
        mItemTouchHelper = ItemTouchHelper(callback)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
        binding.exerciseRv.layoutManager = layoutManager
        mItemTouchHelper.attachToRecyclerView(binding.exerciseRv)
        binding.exerciseRv.adapter = adapterSetAndRepsAddedExerciseAdapter
        saperator()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()

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

    private fun getIds(workout_equipment_ids: String, pos: Int): String {
        var idStr = ""
        var tempList = workout_equipment_ids.split(",")
        for (k in 0..tempList.size - 1) {
            for (i in 0..exerciseList.get(pos).list.size - 1) {
                if (tempList.get(k).equals(exerciseList.get(pos).list.get(i).id)) {
                    exerciseList.get(pos).list.get(i).isCheck = true
                    idStr = idStr + tempList.get(k) + ","
                }
            }
        }
        if (idStr.length > 0) idStr = idStr.substring(0, idStr.length - 1)
        return idStr
    }

    private fun getUsersIds(allowedUserIds: String) {
        tempAllowedUserList.clear()
        var tempList = allowedUserIds.split(",")
        for (k in tempList.indices) {
            tempAllowedUserList.add(CustomerListModel.Data.User(tempList[k], "", "", true))
        }
    }

    private fun getSpanningString(workout_good_for: String): SpannableString {
        val builder = SpannableStringBuilder()
        val spanStr = SpannableString(workout_good_for)
        spanStr.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.created_by_color)),
            0,
            spanStr.length,
            0
        )
        builder.append(spanStr)
        var ss = SpannableString(builder)
        return ss
    }

    private fun isAdminUser() {
        if (isAdmin.equals("Yes", true)) {
            binding.adminLayout.visibility = View.VISIBLE

        } else {
            binding.adminLayout.visibility = View.GONE
        }
    }

    /**need to work here */
    override fun selectTime(timing: String, pos: Int, s: String, s1: String) {
        createTime = timing
        itemPos = pos
        var values = arrayListOf<String>()
        values = getValue()
        val openDialog =
            TimerDialog.newInstance(values, this, getActivity(), s, s1).show(supportFragmentManager)

    }

    /**need to work here also*/
    override fun deleteExercise(pos: Int) {
        itemPos = pos
        val dialog =
            FinishActivityDialog.newInstance("Delete", "No", "Are you sure you want to delete?")
        dialog.setListener(this)
        dialog.show(supportFragmentManager)
        //   deleteDialog()
    }

    override fun onStartDrag(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        mItemTouchHelper.startDrag(viewHolder)
    }

    override fun copyExercise(pos: Int) {

        /** duplicating multiple exercise simultaneously*/
        var temp = ArrayList<ExerciseListingResponse.Data>()
        for (i in 0 until selectedExerciseList.size) {
            var item = selectedExerciseList[i]
            if (item.isSelectedExercise) {
                item.isSelectedExercise = false

                temp.add(item)
                var copyItem: ExerciseListingResponse.Data? = null
                copyItem = ExerciseListingResponse.Data(
                    item.exercise_access_level,
                    item.exercise_amount,
                    item.exercise_amount_display,
                    item.exercise_body_parts,
                    item.exercise_description,
                    item.exercise_equipments,
                    item.exercise_id,
                    item.exercise_image,
                    item.exercise_is_favourite,
                    item.exercise_level,
                    item.exercise_name,
                    item.exercise_share_url,
                    item.exercise_tags,
                    item.exercise_video,
                    item.is_liked,
                    item.isPlaying,
                    item.isSelected,
                    item.isReps,
                    item.exercise_timer_time,
                    item.exercise_reps_time,
                    item.exercise_reps_number,
                    item.exercise_reps_number,
                    item.selected_exercise_reps_number,
                    item.selected_exercise_weight_number,
                    true,
                    isSelectedExercise = false
                )
                // selectedExerciseList.add((pos + 1), copyItem)
                temp.add(copyItem)
            } else {
                temp.add(item)
            }
        }

        selectedExerciseList.clear()
        selectedExerciseList.addAll(temp)
        temp.clear()
        // var item = selectedExerciseList.get(pos)
        adapter.notifyDataSetChanged()

    }

    override fun selectExercise(pos: Int) {
        var count = 0
        var list = selectedExerciseList.filter { it1 -> it1.isSelectedExercise }
        count += list.size
        if (count > 0) {
            selected_count.text = "$count Selected"
            ll_duplicate_delete.visibility = View.VISIBLE
            ll_addExerciseView.visibility = View.GONE
            topBlurView1.visibility = View.GONE
            ll_selectAll.visibility = View.VISIBLE

        } else {
            selected_count.text = "Select"
            ll_duplicate_delete.visibility = View.GONE
            ll_addExerciseView.visibility = View.VISIBLE
            topBlurView1.visibility = View.VISIBLE
            ll_selectAll.visibility = View.GONE
        }
        if (count != selectedExerciseList.size) {
            Glide.with(this).load(R.drawable.deselected_create_workout)
                .into(binding.selectSelectDeselectAll)
        } else {
            Glide.with(this).load(R.drawable.selected_circle_blue_)
                .into(binding.selectSelectDeselectAll)
        }
    }

    /***this method is used for select and deselect exercise*/
    override fun selectExercise(pos: Int, Childpos: Int) {
        if (exerciseType == "SetAndReps") {

            // Toast.makeText(this@CreateWorkoutActivity, "show selection", Toast.LENGTH_SHORT).show()
        } else {

            selectedExerciseList[pos].isSelectedExercise =
                !selectedExerciseList[pos].isSelectedExercise
            adapter.notifyDataSetChanged()

            var flag = false
            var count = 0
            for (i in 0 until selectedExerciseList.size) {
                if (selectedExerciseList.get(i).isSelectedExercise) {
                    flag = true
                    count++
                }
            }
            selected_count.text = "$count  Selected"


            if (flag) {
// show bottom and top view
                ll_mark_as_ungroup_delete.visibility = View.VISIBLE
                ll_addExerciseView.visibility = View.GONE
                topBlurView1.visibility = View.GONE
                ll_selectAll.visibility = View.VISIBLE

            } else {
                ll_mark_as_ungroup_delete.visibility = View.GONE
                ll_addExerciseView.visibility = View.VISIBLE
                topBlurView1.visibility = View.VISIBLE
                ll_selectAll.visibility = View.GONE
            }

            /*  val filteredList = selectedExerciseList.filter { !it.isSelectedExercise }
              Log.d("aaaaa", "selectExercise: $filteredList")
              if (filteredList.isNotEmpty()) {
                  isAllExerciseSelected = false
                  Glide.with(this).load(R.drawable.deselected_create_workout)
                      .into(binding.selectSelectDeselectAll)
              } else if (filteredList.isEmpty()) {
                  isAllExerciseSelected = true
                  Glide.with(this).load(R.drawable.selected_circle_blue_)
                      .into(binding.selectSelectDeselectAll)
              }
      */
            // updatingViewAccordingToSelections(count, exerciseListSize)
        }

    }

    override fun replaceExercise(parentPosition: Int, childPosition: Int) {

    }

    override fun addExerciseInRound(parentPosition: Int) {
    }

    override fun addNotesInRound(parentPosition: Int) {

    }

    fun updatingViewAccordingToSelections(count: Int, exerciseListSize: Int) {
        val filteredList =
            SetsAndRepsList.forEach { it1 -> it1.arrSets.forEach { it2 -> it2.exerciseList.filter { it.isSelectedExercise } } }
        if (SetsAndRepsList.isEmpty()) {
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
                    this@CreateWorkoutActivity, R.color.deselect_color
                )
            )

            binding.tvMarkAs.setTextColor(
                ContextCompat.getColor(
                    this@CreateWorkoutActivity, R.color.deselect_color
                )
            )

        } else if (count >= 2) {
            binding.tvUngroup.isEnabled = true
            binding.tvMarkAs.isEnabled = true
            binding.tvDeleteFromRounds.isEnabled = true


            binding.tvUngroup.setTextColor(
                ContextCompat.getColor(
                    this@CreateWorkoutActivity, R.color.colorWhite
                )
            )

            binding.tvMarkAs.setTextColor(
                ContextCompat.getColor(
                    this@CreateWorkoutActivity, R.color.colorWhite
                )
            )
        }
    }

    fun setPadding(imageView: ImageView, left: Int, top: Int, right: Int, bottom: Int) {
        val layoutParams = imageView.layoutParams as LinearLayout.LayoutParams
        layoutParams.setMargins(left, top, right, bottom)
        imageView.layoutParams = layoutParams
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.container_id -> {
                hideNavigationBar()
                hideKeyboard()
            }

            R.id.duplicate_icon -> {
                copyExercise(0)
            }

            R.id.tv_delete_from_rounds -> {
                deletingExerciseFromRounds()
            }

            R.id.tv_ungroup -> {
                unGroupSelectedExercise()
            }

            R.id.tv_mark_as -> {
                val tempList = ArrayList<ExerciseListingResponse.Data>()
                for (i in 0 until SetsAndRepsList.size) {
                    tempList.addAll(SetsAndRepsList[i].arrSets[0].exerciseList.filter { it.isSelectedExercise })
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

            R.id.Done_create_workoutt -> {
                selectedExerciseList.forEach { it.isSelectedExercise = false }
                adapter.notifyDataSetChanged()

                ll_mark_as_ungroup_delete.visibility = View.GONE
                ll_addExerciseView.visibility = View.VISIBLE
                topBlurView1.visibility = View.VISIBLE
                ll_selectAll.visibility = View.GONE
            }

            R.id.delete_icon_create_exercise -> {
                // itemPos = pos
                val filteredList = selectedExerciseList.filter { it.isSelectedExercise }
                if (filteredList.isNotEmpty()) {
                    val dialog = FinishActivityDialog.newInstance(
                        "Delete", "No", "Are you sure you want to delete?"
                    )
                    dialog.setListener(this)
                    dialog.show(supportFragmentManager)
                }

            }

            R.id.select_select_deselect_all -> {
                if ("FollowAlong" == exerciseType) {
                    if (isAllExerciseSelected) {
                        isAllExerciseSelected = false
                        selectedExerciseList.forEach {
                            it.isSelectedExercise = false
                        }
                        adapter.notifyDataSetChanged()
                        Glide.with(this).load(R.drawable.deselected_create_workout)
                            .into(binding.selectSelectDeselectAll)
                        selected_count.text = "Select"
                        ll_mark_as_ungroup_delete.visibility = View.GONE
                        ll_duplicate_delete.visibility = View.GONE
                        ll_addExerciseView.visibility = View.VISIBLE
                        topBlurView1.visibility = View.VISIBLE
                        ll_selectAll.visibility = View.GONE
                    } else {
                        isAllExerciseSelected = true
                        selectedExerciseList.forEach {
                            it.isSelectedExercise = true
                        }
                        var count = selectedExerciseList.size
                        adapter.notifyDataSetChanged()
                        Glide.with(this).load(R.drawable.selected_circle_blue_)
                            .into(binding.selectSelectDeselectAll)

                        selected_count.text = "$count Selected"
                    }
                } else {
                    if (isAllExerciseSelected) {
                        isAllExerciseSelected = false
                        SetsAndRepsList.forEach {
                            it.arrSets.forEach { it2 ->
                                it2.exerciseList.forEach { it3 ->
                                    it3.isSelectedExercise = false
                                }
                            }
                        }
                        adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
                        adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
                        Glide.with(this).load(R.drawable.deselected_create_workout)
                            .into(binding.selectSelectDeselectAll)
                        selected_count.text = "Select"

                        ll_mark_as_ungroup_delete.visibility = View.GONE
                        ll_addExerciseView.visibility = View.VISIBLE
                        topBlurView1.visibility = View.VISIBLE
                        ll_selectAll.visibility = View.GONE
                    } else {/*selecting all exercises*/
                        isAllExerciseSelected = true
                        var count = 0
                        SetsAndRepsList.forEach {
                            it.arrSets.forEach { it2 ->
                                it2.exerciseList.forEach { it3 ->
                                    it3.isSelectedExercise = true
                                }
                            }
                        }
                        SetsAndRepsList.forEach {
                            count += it.arrSets[0].exerciseList.size
                        }
                        adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
                        adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
                        Glide.with(this).load(R.drawable.selected_circle_blue_)
                            .into(binding.selectSelectDeselectAll)
                        selected_count.text = "$count Selected"
                    }
                }
            }

            R.id.select_sets_reps_icon -> {/*sets and resp view*/
                if ("SetAndReps" != exerciseType) {
                    if (!selectedExerciseList.isEmpty()) {
                        val dialog = ChangeExersiseTypeDialog.newInstance(
                            "Yes",
                            "No",
                            "Attention: Changing the workout type will reset your current configuration to the default settings for each workout type. Any custom sets, reps, or timer duration you've entered will be lost. Do you wish to proceed?",
                            "Set&Reps"
                        )
                        dialog.setListener(this)
                        dialog.show(supportFragmentManager)
                    } else {
                        mClickedOnSetAndReps()
                    }
                }
            }

            R.id.follow_along_icon -> {
                if ("FollowAlong" != exerciseType) {
                    if (!SetsAndRepsList.isEmpty()) {
                        val dialog = ChangeExersiseTypeDialog.newInstance(
                            "Yes",
                            "No",
                            "Attention: Changing the workout type will reset your current configuration to the default settings for each workout type. Any custom sets, reps, or timer duration you've entered will be lost. Do you wish to proceed?",
                            "FollowAlong"
                        )
                        dialog.setListener(this)
                        dialog.show(supportFragmentManager)
                    } else {
                        mClickedOnFollowAlong()
                    }
                }
            }

            R.id.view_trans_parancy -> {
                binding.viewTransParancy.visibility = View.GONE
                if (dialog != null) {
                    dialog.dismiss()
                }
            }

            R.id.good_for_layout -> {
                if (exerciseList.size > 0) {
                    var sendingList = exerciseList.get(2).list
                    var tempList = good_for.text.toString().trim().split(" | ")
                    var originalList = ArrayList<String>()
                    for (i in 0 until tempList.size) {
                        if (tempList.get(i).contains("|")) {
                            var text = tempList.get(i).split("|")
                            text.forEach {
                                if (!it.contains("|") && it.isNotEmpty()) {
                                    originalList.add(it)
                                }
                            }
                        } else {
                            if (tempList.get(i).isNotEmpty()) originalList.add(tempList.get(i))
                        }
                    }

                    for (i in 0 until sendingList.size) {
                        for (j in tempList.indices) {
                            if (originalList.isNotEmpty()) if (sendingList[i].display_name.equals(
                                    originalList[j].trim(), true
                                )
                            ) {//Lower Abs
                                sendingList[i].isCheck = true
                            }
                        }
                    }
                    var intent = Intent(getActivity(), ActivityGoodFor::class.java)
                    intent.putExtra("itemList", sendingList /*exerciseList.get(4).list*/)
                    intent.putExtra("from", "")
                    intent.putExtra("SelectedExerciseGoodFor", selectedExerciseGoodForList)
                    startActivityForResult(intent, 1)
                }
            }

            R.id.level_layout -> {
                value = "level"
                var values = arrayListOf<String>()
                values.add("Advance")
                values.add("Basic")
                values.add("Moderate")
                values.add("All Levels")
                values.toTypedArray()
                if (!binding.levelName.text.toString().trim().isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (binding.levelName.text.toString().trim().equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }
                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)

            }

            R.id.equipment_layout -> {
                if (exerciseList.size > 0) {
                    var sendingList = exerciseList[1].list
                    var tempList = tv_equipment.text.toString().trim().split(" | ")
                    var originalList = ArrayList<String>()
                    for (i in 0 until tempList.size) {
                        if (tempList.get(i).contains("|")) {
                            var text = tempList.get(i).split("|")
                            text.forEach {
                                if (!it.contains("|") && it.isNotEmpty()) {
                                    originalList.add(it.trim())
                                }
                            }
                        } else {
                            if (tempList.get(i).isNotEmpty()) originalList.add(
                                tempList.get(i).trim()
                            )
                        }
                    }
                    for (i in 0 until sendingList.size) {
                        for (j in originalList.indices) {
                            if (originalList.isNotEmpty()) if (sendingList[i].display_name.equals(
                                    originalList[j].trim(), true
                                )
                            ) {
                                sendingList[i].isCheck = true
                            }
                        }
                    }

                    var intent = Intent(getActivity(), ActivityGoodFor::class.java)
                    intent.putExtra("itemList", sendingList/*exerciseList.get(1).list*/)
                    intent.putExtra("from", "")
                    startActivityForResult(intent, 2)
                }
            }

            R.id.created_by_layout -> {
                var intent = Intent(getActivity(), ActivityGoodFor::class.java)
                intent.putExtra("from", "created")
                intent.putExtra("createById", createById)
                startActivityForResult(intent, 4)
            }

            R.id.allowed_users_layout -> {
                var intent = Intent(getActivity(), ActivityGoodFor::class.java)
                intent.putExtra("from", "users")
                intent.putExtra("itemList", tempAllowedUserList)
                startActivityForResult(intent, 3)
            }

            R.id.access_level_layout -> {
                value = "access"
                var values = arrayListOf<String>()
                values.add("Free")
                values.add("Subscribers")
                values.toTypedArray()
                if (!accessLevelValue.isEmpty()) {
                    for (i in 0 until values.size) {
                        if (accessLevelValue == values[i]) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }
                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }

            R.id.display_newsfeed_layout -> {
                value = "news"
                var values = arrayListOf<String>()
                values.add("Yes")
                values.add("No")
                values.toTypedArray()

                if (!displayNewsfeedValue.isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (displayNewsfeedValue.equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }
                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }

            R.id.allow_notification_layout -> {
                value = "notification"
                var values = arrayListOf<String>()
                values.add("Yes")
                values.add("No")
                values.toTypedArray()
                if (!allowNotificationvalue.isEmpty()) {
                    for (i in 0..values.size - 1) {
                        if (allowNotificationvalue.equals(values.get(i))) {
                            selectedIndex = i
                        }
                    }
                } else {
                    selectedIndex = 0
                }
                val openDialog =
                    WorkoutLevelDialog.newInstance(values, this, getActivity(), selectedIndex)
                        .show(supportFragmentManager)
            }

            R.id.img_layout -> {
                ImagePickerDialog.newInstance(this).show(supportFragmentManager)
            }

            R.id.timer_exercise_layout -> {
                value = "timer"
                var values = arrayListOf<String>()
                values = getValue()
                var str = binding.timeExerciseTxt.text.toString().split(":")
                val openDialog =
                    TimerDialog.newInstance(values, this, getActivity(), str[0], str[1])
                        .show(supportFragmentManager)
            }

            R.id.timer_exercise_layout_Sets_reps -> {
                value = "timer_Sets_reps"
                var values = arrayListOf<String>()
                values = getValue()
                var str = binding.timeExerciseTxt1.text.toString().split(":")
                val openDialog =
                    TimerDialog.newInstance(values, this, getActivity(), str[0], str[1])
                        .show(supportFragmentManager)
            }

            R.id.repetetion_layout -> {
                value = "reps"
                var values = arrayListOf<String>()
                values = getRepValue()
                val openDialog = WorkoutLevelDialog.newInstance(
                    values, this, getActivity(), binding.repetetionTxt.text.toString().toInt()
                ).show(supportFragmentManager)
            }

            R.id.repetetion_layout_Sets_reps -> {
                value = "reps_Sets_reps"
                var values = arrayListOf<String>()
                values = getSetValue()
                val openDialog = WorkoutLevelDialog.newInstance(
                    values, this, getActivity(), binding.repetetionTxt1.text.toString().toInt()
                ).show(supportFragmentManager)
            }

            R.id.reps_timer_layout -> {
                value = "reps_time"
                var values = arrayListOf<String>()
                values = getValue()
                var str = binding.repsTimerTxt.text.toString().split(":")
                val openDialog =
                    TimerDialog.newInstance(values, this, getActivity(), str[0], str[1])
                        .show(supportFragmentManager)
            }

            R.id.reps_timer_layout_Sets_reps -> {
                value = "reps_time_Sets_reps"
                var values = arrayListOf<String>()
                values = getValue()
                var str = ArrayList<String>()
                if (binding.repsTimerTxt1.text.toString().trim().contains("To")) str =
                    binding.repsTimerTxt1.text.toString().split(" To ") as ArrayList<String>
                else if (binding.repsTimerTxt1.text.toString().trim().contains(" Reps")) {
                    str.add(binding.repsTimerTxt1.text.toString().trim().split(" ")[0])
                    str.add(binding.repsTimerTxt1.text.toString().trim().split(" ")[0])
                } else {
                    str.add(binding.repsTimerTxt1.text.toString().trim())
                    str.add(binding.repsTimerTxt1.text.toString().trim())
                }

                WrapsDialog.newInstance1(values, object : WrapsDialog.HeightWeightCallBack {
                    override fun timeOnClick(
                        index: Int, value: String, index1: Int, value1: String
                    ) =
                        /** need to do more like store locally */
                        if (index == index1) {
                            binding.repsTimerTxt1.text = "$index Reps"

                            getDataManager().setStringData(
                                PREF_KEY_EXERCISE_REST_TIME_SETANDREPS, (index.toString()) + " Reps"
                            )
                        } else {
                            binding.repsTimerTxt1.text = ("$index To $value1").toString()
                            getDataManager().setStringData(
                                PREF_KEY_EXERCISE_REST_TIME_SETANDREPS, "$index To $value1"
                            )
                        }
                }, getActivity(), str[0], str[1], "repsInSetAndReps").show(supportFragmentManager)
            }

            R.id.show_hide_view -> {
                if (binding.showHideLayout.visibility == View.VISIBLE) {
                    binding.showHideLayout.visibility = View.GONE
                    Glide.with(getActivity()).load(R.drawable.ic_top_arrow)
                        .into(binding.showHideView)
                } else {
                    binding.showHideLayout.visibility = View.VISIBLE
                    Glide.with(getActivity()).load(R.drawable.ic_down_arrow)
                        .into(binding.showHideView)
                }
            }

            R.id.show_hide_view_Sets_reps -> {
                if (binding.showHideLayout1.visibility == View.VISIBLE) {
                    binding.showHideLayout1.visibility = View.GONE
                    Glide.with(getActivity()).load(R.drawable.ic_top_arrow)
                        .into(binding.showHideViewSetsReps)


                } else {
                    binding.showHideLayout1.visibility = View.VISIBLE
                    Glide.with(getActivity()).load(R.drawable.ic_down_arrow)
                        .into(binding.showHideViewSetsReps)

                }
            }

            R.id.add_exercise_btn -> {/*var exerciseType=""
                   if (isAlongSelected)
                       exerciseType="FollowAlong"
                   else if (isSetRepsSelected)
                       exerciseType="SetAndReps"*/

                if (exerciseType.isNotEmpty()) {
                    var intent = Intent(getActivity(), AddExerciseActivity::class.java)
                    intent.putExtra("workout_Type", exerciseType)
                    startActivityForResult(intent, 7)
                } else {
                    Toast.makeText(
                        this, "Please select exercise type to continue", Toast.LENGTH_SHORT
                    ).show()
                }

            }

            R.id.cancel_button -> {
                hideKeyboard()
                hideNavigationBar()
                if (isAnyValue()) {
                    finish()
                } else {
                    FinishActivityDialog.newInstance(
                        "Yes",
                        "No",
                        "All changes done to the workout will be lost. Are you sure you want to cancel?"
                    ).show(supportFragmentManager)
                }
            }

            R.id.info_icon -> {
                filter_WorkOut_and_doviesFitness_dialog("INFORMATION", "info")
            }

            R.id.done_btn -> {
                CommanUtils.lastClick()
                if (CommanUtils.isNetworkAvailable(this)) {
                    if (isValidData()) {
                        if (exerciseType == "SetAndReps") getSetAndRepsExerciseArray()
                        else {
                            if (CommanUtils.isNetworkAvailable(this)) {
                                if (isValidData()) {
                                    getExerciseArray()
                                }
                            } else {
                                Constant.showInternetConnectionDialog(this)
                            }
                        }
                    }
                } else {
                    Constant.showInternetConnectionDialog(this)
                }
            }

            R.id.ll_addNote -> {
                var text = tv_add_note_total_time.text.toString().trim()
                if (text == "ADD NOTE" || text == "Add Note") {
                    startActivityForResult(
                        Intent(this, AddNotesActivity::class.java).putExtra(
                            "notesFor", "CompleteWorkout"
                        ).putExtra("notesText", "$noteForExercise"), 501
                    )

                } else {
                    var TTime = 0
                    var DisplayTime = 0
                    var DisplayStr = ""
                    if (selectedExerciseList != null && selectedExerciseList.size > 0) {
                        for (i in 0..selectedExerciseList.size - 1) {
                            if (selectedExerciseList.get(i).isReps != null && !selectedExerciseList.get(
                                    i
                                ).isReps
                            ) {
                                if (selectedExerciseList.get(i).exercise_timer_time != null && !selectedExerciseList.get(
                                        i
                                    ).exercise_timer_time!!.isEmpty()
                                ) {
                                    var TSeconds =
                                        getTotalTimeinSeconds(selectedExerciseList.get(i).exercise_timer_time!!)
                                    TTime += TSeconds
                                }
                            }
                            if (selectedExerciseList.get(i).isReps != null && selectedExerciseList.get(
                                    i
                                ).isReps
                            ) {
                                if (selectedExerciseList.get(i).exercise_reps_time != null && !selectedExerciseList.get(
                                        i
                                    ).exercise_reps_time!!.isEmpty()
                                ) {
                                    var TSeconds =
                                        getTotalTimeinSeconds(selectedExerciseList.get(i).exercise_reps_time!!)
                                    TTime += TSeconds
                                }
                            }
                            if (i != selectedExerciseList.size - 1 && selectedExerciseList.get(i).exercise_rest_time != null && !selectedExerciseList.get(
                                    i
                                ).exercise_rest_time!!.isEmpty()
                            ) {
                                var TSeconds =
                                    getTotalTimeinSeconds(selectedExerciseList.get(i).exercise_rest_time!!)
                                TTime += TSeconds
                            }
                        }
                        if (TTime / 60 == 0) {
                            DisplayTime = TTime % 60
                            DisplayStr = "Seconds"
                        } else if (TTime / 60 > 0) {
                            DisplayTime = TTime / 60
                            if (TTime % 60 < 30) {

                            } else {
                                DisplayTime = DisplayTime + 1
                            }
                            if (DisplayTime == 1) DisplayStr = "Minute"
                            else DisplayStr = "Minutes"
                        }
                    }
                    createWorkoutTimeDialog(DisplayTime, DisplayStr)
                }
            }

            R.id.llDelete -> {
                var count = 0
                var list = selectedExerciseList.filter { it1 -> it1.isSelectedExercise }
                count += list.size

                var message = "Are you sure you want to delete these exercises?"
                if (count > 1) {
                    message = "Are you sure you want to delete these exercises?"
                } else {
                    message = "Are you sure you want to delete this exercise?"
                }
                CommanUtils.deletePopupForSetAndReps(
                    this@CreateWorkoutActivity, message
                ) {
                    selectedExerciseList.removeAll { it.isSelectedExercise }
                    adapter.notifyDataSetChanged()
                    ll_mark_as_ungroup_delete.visibility = View.GONE
                    ll_duplicate_delete.visibility = View.GONE
                    ll_addExerciseView.visibility = View.VISIBLE
                    topBlurView1.visibility = View.VISIBLE
                    ll_selectAll.visibility = View.GONE
                    it.dismiss()
                }
            }

            R.id.llDuplicate -> {
                copyExercise(0)
                ll_mark_as_ungroup_delete.visibility = View.GONE
                ll_duplicate_delete.visibility = View.GONE
                ll_addExerciseView.visibility = View.VISIBLE
                topBlurView1.visibility = View.VISIBLE
                ll_selectAll.visibility = View.GONE
            }
        }
    }

    override fun isYesClicked(type: String?) {
        if ("FollowAlong" == type) {
            mClickedOnFollowAlong()
        } else {
            mClickedOnSetAndReps()
        }
    }

    private fun getExerciseArray() {
        var isValid = false
        exerciseArray = JSONArray()

        if (selectedExerciseList != null && selectedExerciseList.size > 0) {
            for (i in 0..selectedExerciseList.size - 1) {
                var exerciseObject = JSONObject()
                if (selectedExerciseList.get(i).isReps) {
                    if (selectedExerciseList.get(i).exercise_reps_number!!.isEmpty() || selectedExerciseList.get(
                            i
                        ).exercise_reps_number.equals("00")
                    ) {
                        ErrorDialog.newInstance("", "Ok", "Please select reps")
                            .show(supportFragmentManager)
                        selectedExerciseList.get(i).isValid = false
                        isValid = false
                        adapter.notifyItemChanged(i)
                        break
                    } else if (selectedExerciseList.get(i).exercise_reps_time!!.isEmpty() || selectedExerciseList.get(
                            i
                        ).exercise_reps_time.equals(
                            "00:00"
                        )
                    ) {
                        ErrorDialog.newInstance("", "Ok", "Please select total time")
                            .show(supportFragmentManager)
                        selectedExerciseList.get(i).isValid = false
                        isValid = false
                        adapter.notifyItemChanged(i)
                        break

                    } else {
                        exerciseObject.put(
                            "exercise_repeat_text", selectedExerciseList.get(i).exercise_reps_number
                        )
                        exerciseObject.put(
                            "exercise_time", "00:" + selectedExerciseList.get(i).exercise_reps_time
                        )

                        exerciseObject.put("exercise_type", "Repeat")
                        selectedExerciseList.get(i).isValid = true
                        adapter.notifyItemChanged(i)
                        isValid = true
                    }

                    /*if (selectedExerciseList.get(i).exercise_reps_time.isEmpty()||selectedExerciseList.get(i).exercise_reps_time.equals("00:00"))
                    ErrorDialog.newInstance("", "OK", "Please select Total time").show(supportFragmentManager)
*/
                } else {
                    if (selectedExerciseList.get(i).exercise_timer_time!!.isEmpty() || selectedExerciseList.get(
                            i
                        ).exercise_timer_time.equals(
                            "00:00"
                        )
                    ) {
                        ErrorDialog.newInstance("", "Ok", "Please select time")
                            .show(supportFragmentManager)
                        selectedExerciseList.get(i).isValid = false
                        isValid = false
                        adapter.notifyItemChanged(i)
                        break
                    } else {
                        exerciseObject.put(
                            "exercise_time", "00:" + selectedExerciseList.get(i).exercise_timer_time
                        )
                        exerciseObject.put("exercise_type", "Time")
                        exerciseObject.put("exercise_repeat_text", "")
                        selectedExerciseList.get(i).isValid = true
                        adapter.notifyItemChanged(i)
                        isValid = true
                    }
                }
                exerciseObject.put("exercise_id", selectedExerciseList.get(i).exercise_id)
                exerciseObject.put(
                    "break_time", "00:" + selectedExerciseList.get(i).exercise_rest_time
                )
                exerciseObject.put("sequence_number", i + 1)
                exerciseArray.put(exerciseObject)
            }
        } else {
            ErrorDialog.newInstance("", "Ok", "Please select exercise").show(supportFragmentManager)
            isValid = false
        }
        if (isValid) {
            if (isEdit.equals("edit", true)) {
                if (isMyWorkout.equals("yes")) {
                    SaveEditWorkoutDialog.newInstance("edit", this).show(supportFragmentManager)
                } else if (intent.getStringExtra("fromDeepLinking") != null && intent.getStringExtra(
                        "fromDeepLinking"
                    ).equals("fromDeepLinking")
                ) {
                    createWorkout(exerciseArray.toString())
                } else {
                    createWorkout(exerciseArray.toString())/* if (isAdmin.equals("Yes", true)) {
                         SaveEditWorkoutDialog.newInstance("edit", this).show(supportFragmentManager)
                     } else {
                         editWorkout(exerciseArray.toString())
                     }*/
                }

            } else {
                createWorkout(exerciseArray.toString())
            }
        }
    }

    private fun mClickedOnSetAndReps() {
        ll_mark_as_ungroup_delete.visibility = View.GONE
        ll_addExerciseView.visibility = View.VISIBLE
        topBlurView1.visibility = View.VISIBLE
        ll_selectAll.visibility = View.GONE
        ll_duplicate_delete.visibility = View.GONE
        //Prepration of lists for interchange on exersise type
        val targetReps = binding.repsTimerTxt1.text.toString().trim()
        val targetSets = binding.repetetionTxt1.text.toString().toInt()
        mSetAndRepsList.clear()
        for (i in 1..selectedExerciseList.size) {
            selectedExerciseList[i - 1].isSelected = false
            selectedExerciseList[i - 1].isSelectedExercise = false
            mSetAndRepsList.add(
                SetAndRepsModel(
                    strRoundCounts = "",
                    strRoundName = "",
                    strTargetSets = "$targetSets",
                    strTargetReps = "$targetReps",
                    strExerciseType = "",
                    strSetsCounts = "",
                    arrSets = getSets(
                        targetSets, selectedExerciseList[i - 1], ""
                    )
                )
            )
        }
        SetsAndRepsList.clear()
        SetsAndRepsList.addAll(mSetAndRepsList)
        setAdapterSetAndReps()/*-------------------------*/
        setPadding(total_exercise_time, 15, 15, 15, 15)

        tv_add_note_total_time.text = "ADD NOTE"
        tv_title_sets.setTextColor(
            ContextCompat.getColor(
                this@CreateWorkoutActivity, R.color.colorWhite
            )
        )
        select_sets_reps_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_check_mark
            )
        )
        total_exercise_time.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.add_notes_icon
            )
        )/*Along view*/
        tv_title_along.setTextColor(
            ContextCompat.getColor(
                this@CreateWorkoutActivity, R.color.deselect_color
            )
        )
        exerciseType = "SetAndReps"
        isAlongSelected = false
        follow_along_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_circle_img
            )
        )
    }

    private fun mClickedOnFollowAlong() {
        ll_mark_as_ungroup_delete.visibility = View.GONE
        ll_addExerciseView.visibility = View.VISIBLE
        topBlurView1.visibility = View.VISIBLE
        ll_selectAll.visibility = View.GONE
        ll_duplicate_delete.visibility = View.GONE

        //Prepration of lists for interchange on exersise type
        val targetReps = binding.repsTimerTxt1.text.toString().trim()
        val targetSets = binding.repetetionTxt1.text.toString().toInt()
        mFollowAlongList.clear()
        for (i in 1..SetsAndRepsList.size) {
            for (j in 1..SetsAndRepsList[i - 1].arrSets[0].exerciseList.size) {
                SetsAndRepsList[i - 1].arrSets[0].exerciseList[j - 1].isSelectedExercise = false
                SetsAndRepsList[i - 1].arrSets[0].exerciseList[j - 1].isSelected = false
                mFollowAlongList.add(SetsAndRepsList[i - 1].arrSets[0].exerciseList[j - 1])
            }
        }
        selectedExerciseList.clear()
        selectedExerciseList.addAll(mFollowAlongList)
        selectedExerciseList.forEachIndexed { index, data ->

            if (data.exercise_timer_time!!.isEmpty()|| data.exercise_timer_time=="0")
            data.exercise_timer_time= Doviesfitness.getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)

            if (data.exercise_rest_time!!.isEmpty()|| data.exercise_rest_time=="0")
            data.exercise_rest_time= "00:00"
        }




        if (this::adapter.isInitialized) {
            binding.exerciseRv.adapter = adapter
            adapter.notifyDataSetChanged()
        } else {

            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y
            Doviesfitness.height = size.y
            Doviesfitness.weight = size.x

            val screenWidth = size.x / 320
            val videowidth = 120 + (160 * screenWidth)

            val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
            binding.exerciseRv.layoutManager = layoutManager
            adapter = AddedExerciseAdapter(
                getActivity(),
                selectedExerciseList,
                this,
                videowidth,
                this,
                object : AddedExerciseAdapter.StopScroll {
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
                            Log.d("TAG", "scrollToPosition: ${((y * 100) / mainViewTotalHeight)}")
                        }, 1000)
                    }
                },
                this
            )

            val callback = SimpleItemTouchHelperCallbackForWorkout(adapter, binding.svMain)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper.attachToRecyclerView(binding.exerciseRv)
            binding.exerciseRv.adapter = adapter
        }
        isAlongSelected = true
        setPadding(total_exercise_time, 1, 1, 1, 1)
        tv_add_note_total_time.text = "Total Time"/*along view*/
        tv_title_along.setTextColor(
            ContextCompat.getColor(
                this@CreateWorkoutActivity, R.color.colorWhite
            )
        )
        //  isAlongSelected = false
        follow_along_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_check_mark
            )
        )
        exerciseType = "FollowAlong"/*sets and reps view*/
        tv_title_sets.setTextColor(
            ContextCompat.getColor(
                this@CreateWorkoutActivity, R.color.deselect_color
            )
        )
        select_sets_reps_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_circle_img
            )
        )
        total_exercise_time.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_time_popup_new
            )
        )
    }

    private fun creatLeftAndRightAndSuperset(
        roundName: String, tempList: ArrayList<ExerciseListingResponse.Data>
    ) {
        var targetReps = binding.repsTimerTxt1.text.toString().trim()
        val targetSets = binding.repetetionTxt1.text.toString().toInt()
        val setsList = ArrayList<SetSModel>()
        for (i in 1..targetSets) {
            var flag = false
            if (i == 1) flag = true
            val newArrayList = ArrayList<ExerciseListingResponse.Data>()
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

        SetsAndRepsList.add(
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

        for (i in 0 until SetsAndRepsList.size) {
            for (j in 0 until SetsAndRepsList[i].arrSets.size) {
                SetsAndRepsList[i].arrSets[j].exerciseList.removeAll { it.isSelectedExercise }
            }
            if (SetsAndRepsList[i].arrSets[0].exerciseList.isEmpty()) {
                SetsAndRepsList.removeAt(i)

            }
        }


        adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
        adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
        ll_mark_as_ungroup_delete.visibility = View.GONE
        ll_addExerciseView.visibility = View.VISIBLE
        topBlurView1.visibility = View.VISIBLE
        ll_selectAll.visibility = View.GONE

    }

    private fun deletingExerciseFromRounds() {
        CommanUtils.deletePopupForSetAndReps(
            this@CreateWorkoutActivity, "Are you sure you want to delete these exercises?"
        ) {
            for (i in 0 until SetsAndRepsList.size) {
                for (j in 0 until SetsAndRepsList[i].arrSets.size) {
                    SetsAndRepsList[i].arrSets[j].exerciseList.removeAll { it.isSelectedExercise }
                }

                if (SetsAndRepsList[i].arrSets[0].exerciseList.isEmpty()) {
                    SetsAndRepsList.removeAt(i)
                }
            }

            adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
            adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
            ll_mark_as_ungroup_delete.visibility = View.GONE
            ll_addExerciseView.visibility = View.VISIBLE
            topBlurView1.visibility = View.VISIBLE
            ll_selectAll.visibility = View.GONE
            it.dismiss()
        }

    }

    /** here setting follow along button default selected*/
    fun defaultExerciseTypeSelection() {
        tv_title_along.setTextColor(
            ContextCompat.getColor(
                this@CreateWorkoutActivity, R.color.colorWhite
            )
        )
        follow_along_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_check_mark
            )
        )
        exerciseType = "FollowAlong"/*sets and reps view*/
        tv_title_sets.setTextColor(
            ContextCompat.getColor(
                this@CreateWorkoutActivity, R.color.deselect_color
            )
        )
        select_sets_reps_icon.setImageDrawable(
            ContextCompat.getDrawable(
                this@CreateWorkoutActivity, R.drawable.ic_circle_img
            )
        )
    }

    private fun isAnyValue(): Boolean {
        var isAnyValue = false

        if (isAdmin.equals("Yes", true)) {
            if (binding.workoutName.text.toString()
                    .isEmpty() && userImageFile == null && binding.levelName.text.toString()
                    .isEmpty() && binding.goodFor.text.toString()
                    .isEmpty() && binding.tvEquipment.text.toString()
                    .isEmpty() && binding.createBy.text.toString()
                    .isEmpty() && binding.accessLevel.text.toString()
                    .isEmpty() && binding.displayNewsfeed.text.toString()
                    .isEmpty() && binding.allowNotification.text.toString()
                    .isEmpty() && selectedExerciseList.size == 0
            ) {
                isAnyValue = true
            }
        } else {
            if (binding.workoutName.text.toString()
                    .isEmpty() && userImageFile == null && binding.levelName.text.toString()
                    .isEmpty() && binding.goodFor.text.toString()
                    .isEmpty() && binding.tvEquipment.text.toString()
                    .isEmpty() && selectedExerciseList.size == 0
            ) {
                isAnyValue = true
            }
        }
        return isAnyValue
    }

    private fun getSetAndRepsExerciseArray() {
        var isValid = false
        exerciseArray = JSONArray()
        if (SetsAndRepsList.isNotEmpty()) {
            exerciseArray = makeJsonArrayOfRoundsAndExercises()
            /**making exercise json for adding in workout */
            Log.d("exerciseArray", "createWorkout: $exerciseArray")

            isValid = true
        } else {
            /** if no exercise is selected then this popup will get visible*/
            ErrorDialog.newInstance("cancel", "Ok", "Please select exercise")
                .show(supportFragmentManager)
            isValid = false
        }
        /**Below code for edit workout */
        if (isValid) {
            if (isEdit.equals("edit", true)) {
                if (isMyWorkout == "yes") {
                    SaveEditWorkoutDialog.newInstance("edit", this).show(supportFragmentManager)
                } else if (intent.getStringExtra("fromDeepLinking") != null && intent.getStringExtra(
                        "fromDeepLinking"
                    ).equals("fromDeepLinking")
                ) {
                    createWorkout(exerciseArray.toString())
                } else {
                    createWorkout(exerciseArray.toString())/* if (isAdmin.equals("Yes", true)) {
                         SaveEditWorkoutDialog.newInstance("edit", this).show(supportFragmentManager)
                     } else {
                         editWorkout(exerciseArray.toString())
                     }*/
                }

            } else {
                // goodForIdStr = getGoodForIds(exerciseList[4].list)
                //  equipmentIdStr = getEquipmentsIds(exerciseList[1].list)
                //getGFANDEQP()
                getIdForGoodFor()
                createWorkout(exerciseArray.toString())
            }
        }
    }

    private fun isValidData(): Boolean {
        var isValid = false

        if (binding.workoutName.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please enter workout name")
                .show(supportFragmentManager)
            isValid = false
        } else if (userImageFile == null && !isEdit.equals("edit", true)) {
            ErrorDialog.newInstance("", "Ok", "Please select workout image")
                .show(supportFragmentManager)
            isValid = false
        } else if (userImageUrl == null && userImageUrl.isEmpty() && isEdit.equals("edit", true)) {
            ErrorDialog.newInstance("", "Ok", "Please select workout image")
                .show(supportFragmentManager)
            isValid = false
        } else if (binding.levelName.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please select workout level")
                .show(supportFragmentManager)
            isValid = false
        } else if (binding.goodFor.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please select workout good for")
                .show(supportFragmentManager)
            isValid = false
        } else if (binding.tvEquipment.text.toString().isEmpty()) {
            ErrorDialog.newInstance("", "Ok", "Please select workout equipments")
                .show(supportFragmentManager)
            isValid = false
        } else {
            if (isAdmin.equals("Yes", true)) {
                createByStr = binding.createBy.text.toString()
                accessLevelStr = binding.accessLevel.text.toString()
                displayNewsfeedstr = binding.displayNewsfeed.text.toString()
                allowNotificationstr = binding.allowNotification.text.toString()
                if (createByStr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select Created by")
                        .show(supportFragmentManager)
                    isValid = false
                } else if (accessLevelStr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select access level")
                        .show(supportFragmentManager)
                    isValid = false
                } else if (displayNewsfeedstr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select display in Newsfeed")
                        .show(supportFragmentManager)
                    isValid = false
                } else if (allowNotificationstr.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please select allow Notification")
                        .show(supportFragmentManager)
                    isValid = false
                }/* else if (noteForExercise.isEmpty()) {
                    ErrorDialog.newInstance("", "Ok", "Please add notes for workout")
                        .show(supportFragmentManager)
                    isValid = false
                }*/ else {
                    createByStr = "Admin"
                    isValid = true
                }
            } else {
                createByStr = "User"
                accessLevelStr = ""
                displayNewsfeedstr = ""
                allowNotificationstr = ""
                isValid = true
            }
        }
        return isValid
    }

    /**need to work here for changing functionality according to new changes*/
    override fun videoPlayClick(
        isScroll: Boolean,
        data: ExerciseListingResponse.Data,
        position: Int,
        view: SelectedExerciseAdapterSetAndReps.ExerciseView,
        isLoad: Boolean
    ) {
        // Toast.makeText(this@CreateWorkoutActivity, "sdgbdgd", Toast.LENGTH_SHORT).show()
        val arr = IntArray(2)
        view.rl_exercise.getLocationOnScreen(arr)
        if (selectedExerciseList[position].isPlaying) {
            binding.hideShowView.visibility = View.GONE
            selectedExerciseList[position].isPlaying = false
            binding.svMain.setEnableScrolling(!isScroll)
        } else {
            if (arr[1] > 800) {
                binding.hideShowView.visibility = View.VISIBLE
            }
            selectedExerciseList[position].isPlaying = true
            binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)
            binding.svMain.setEnableScrolling(!isScroll)
        }

        if (isLoad) {
            downloadExercise(data.exercise_video)
        }
    }

    override fun videoPlayClick(
        isScroll: Boolean,
        data: ExerciseListingResponse.Data,
        position: Int,
        view: AddedExerciseAdapter.MyView,
        isLoad: Boolean
    ) {
        // Toast.makeText(this@CreateWorkoutActivity, "sdgbdgd", Toast.LENGTH_SHORT).show()
    }

    override fun forExchangeItem(exerciseData: ExerciseListingResponse.Data, pos: Int) {
        //Log.d("sgdskjgdks", "forExchangeItem: " + "12345")
        showCreatePlanAndWorkOutDialog(exerciseData, pos, selectedExerciseList, Rpoundpos)
    }

    fun downloadExercise(videoUrl: String) {
        list.clear()
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            var subpath = "/Dovies//$downloadFileName"
            val path =
                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            var Download_Uri = Uri.parse(videoUrl)
            val request = DownloadManager.Request(Download_Uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("Dovies Downloading .mp4")
            request.setDescription("Downloading .mp4")
            request.setVisibleInDownloadsUi(false)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            request.setDestinationInExternalFilesDir(
                getActivity(), "/." + Environment.DIRECTORY_DOWNLOADS, subpath
            )
            refid = downloadManager!!.enqueue(request)
            list.add(refid)
        }

    }

    fun getTotalTimeinSeconds(timeStr: String): Int {
        var TSeconds = 0
        var str = timeStr
        var tokens = str.split(":")
        if (tokens[0].equals("00")) {
            if (tokens[1].equals("00")) {

            } else {
                TSeconds = TSeconds + (tokens[1].toInt())
            }
        } else {
            TSeconds = TSeconds + (tokens[0].toInt() * 60)
            if (tokens[1].equals("00")) {

            } else {
                TSeconds = TSeconds + (tokens[1].toInt())
            }
        }

        return TSeconds
    }

    private fun getValue(): ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 0..59) {
            if (i < 10) {
                values.add("0" + i)
            } else {
                values.add("" + i)
            }

        }
        return values
    }

    private fun getRepValue(): ArrayList<String> {
        var values = arrayListOf<String>()
        for (i in 0..50) {
            if (i < 10) {
                values.add("0" + i)
            } else {
                values.add("" + i)
            }

        }
        return values
    }

    private fun getSetValue(): ArrayList<String> {
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

    override fun onBackPressed() {

        if (isAnyValue() == true) {
            finish()
        } else {
            FinishActivityDialog.newInstance(
                "Yes",
                "No",
                "All changes done to the workout will be lost. Are you sure you want to cancel?"
            ).show(supportFragmentManager)
        }

        Handler().postDelayed(Runnable { hideNavigationBar() }, 500)

    }

    override fun selectRepetition(timing: String, pos: Int, exerciseRepsNumber: String) {
        createTime = timing
        itemPos = pos
        var values = arrayListOf<String>()
        values = getRepValue()
        val openDialog =
            WorkoutLevelDialog.newInstance(values, this, getActivity(), exerciseRepsNumber.toInt())
                .show(supportFragmentManager)
    }

    override fun levelOnClick(index: Int, str: String) {
        selectedIndex = index
        if (createTime.equals("reps number")) {
            selectedExerciseList.get(itemPos).exercise_reps_number = str
            createTime = ""
            adapter.notifyItemChanged(itemPos)
        } else {
            if (value.equals("level")) {
                val builder = SpannableStringBuilder()
                // var str1 = "Level \n"
                // builder.append(str1)
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(), R.color.created_by_color
                        )
                    ), 0, spanStr.length, 0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.levelName.text = ss

                if (!ss.isEmpty()) {
                    binding.goodLevel.visibility = View.VISIBLE
                } else {
                    binding.goodLevel.visibility = View.GONE
                }

                value = ""
            }

            if (value.equals("access")) {
                accessLevelValue = str
                var displayValue = getPickerValue("Access Level \n", str)
                binding.accessLevel.text = displayValue
                value = ""
            }


            if (value.equals("news")) {
                displayNewsfeedValue = str
                val builder = SpannableStringBuilder()
                var str1 = "Display in Newsfeed? \n"
                builder.append(str1)
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(), R.color.created_by_color
                        )
                    ), 0, spanStr.length, 0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.displayNewsfeed.text = ss
                value = ""
            }
            if (value.equals("notification")) {
                allowNotificationvalue = str
                val builder = SpannableStringBuilder()
                var str1 = "Allow Notification \n"
                builder.append(str1)
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(), R.color.created_by_color
                        )
                    ), 0, spanStr.length, 0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.allowNotification.text = ss

                value = ""
            }
            if (value.equals("reps")) {
                binding.repetetionTxt.text = str
                getDataManager().setStringData(PREF_KEY_NUMBER_OF_REPS, str)

                value = ""
            }
            if (value.equals("reps_Sets_reps")) {
                binding.repetetionTxt1.text = str
                getDataManager().setStringData(PREF_KEY_NUMBER_OF_SETS_SETANDREPS, str)

                value = ""
            }
        }
    }

    fun getPickerValue(title: String, level: String): SpannableString {
        val builder = SpannableStringBuilder()
        builder.append(title)
        val spanStr = SpannableString(level)
        spanStr.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.created_by_color)),
            0,
            spanStr.length,
            0
        )
        builder.append(spanStr)
        var ss = SpannableString(builder)
        return ss
    }

    override fun timeOnClick(index: Int, str: String, index1: Int, value1: String) {
        if (value.equals("timer")) {
            min = str
            sec = value1
            binding.timeExerciseTxt.text = "$str:$value1"
            getDataManager().setStringData(PREF_KEY_EXERCISE_TIME, "$str:$value1")

            value = ""
        }
        if (value.equals("timer_Sets_reps")) {
            min = str
            sec = value1
            binding.timeExerciseTxt1.text = "$str:$value1"
            getDataManager().setStringData(PREF_KEY_EXERCISE_TIME_SETANDREPS, "$str:$value1")

            value = ""
        }
        if (value.equals("reps_time")) {
            min = str
            sec = value1
            binding.repsTimerTxt.text = "$str:$value1"
            getDataManager().setStringData(PREF_KEY_REPS_FINISH_TIME, "$str:$value1")


            value = ""
        }
        if (createTime.equals("time")) {
            min = str
            sec = value1
            selectedExerciseList.get(itemPos).exercise_timer_time = ("$str:$value1")
            createTime = ""
            adapter.notifyItemChanged(itemPos)
        }

        /*
         if (createTime.equals("reps number")){
              selectedExerciseList.get(itemPos).exercise_reps_number=(str + ":" + value1)
              createTime=""
              adapter.notifyItemChanged(itemPos)
          }
          */

        if (createTime.equals("reps time")) {
            min = str
            sec = value1
            selectedExerciseList.get(itemPos).exercise_reps_time = ("$str:$value1")
            createTime = ""
            adapter.notifyItemChanged(itemPos)
        }
        if (createTime.equals("rest time")) {
            min = str
            sec = value1
            selectedExerciseList.get(itemPos).exercise_rest_time = ("$str:$value1")
            createTime = ""
            adapter.notifyItemChanged(itemPos)
        }
    }

    private fun getFilterWorkoutData() {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.module_type, "")
        param.put(StringConstant.auth_customer_id, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().filterWorkoutApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("TAG", "filter response...." + response!!.toString(4))
                    val jsonObject: JSONObject? = response.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val exerciseData = getDataManager().mGson?.fromJson(
                            response.toString(), FilterExerciseResponse::class.java
                        )
                        exerciseList.addAll(exerciseData!!.data)
                        if (isEdit == "edit") {
                            equipmentIdStr = getIds(WDetail.workout_equipment_ids, 1)
                            goodForIdStr = getIds(WDetail.workout_good_for_ids, 2)
                        }

                    } else Constant.showCustomToast(getActivity(), "fail...$message")
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                    Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.GALLERY && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                tmpUri = data.data!!
                val selectedImage = data.data
                var time = System.currentTimeMillis()
                var str = time.toString()
                var destinatiomPath = str + "dovies.jpg"
                val options = UCrop.Options()
                options.setHideBottomControls(true)
                UCrop.of(tmpUri, Uri.fromFile(File(cacheDir, destinatiomPath)))
                    .withAspectRatio(1f, 1f).withOptions(options).start(getActivity())
            } else {

            }

        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                handleCropResult(data)
            }
        } else if (requestCode == Constant.CAMERA && resultCode == Activity.RESULT_OK) {
            val imageFile = getTemporalFile(this)
            val photoURI = Uri.fromFile(imageFile)
            var bm = Constant.getImageResized(this, photoURI) ///Image resizer
            val rotation = ImageRotator.getRotation(this, photoURI, true)
            bm = ImageRotator.rotate(bm, rotation)
            var time = System.currentTimeMillis()
            var str = time.toString()
            var destinatiomPath = str + "dovies.jpg"
            val options = UCrop.Options()
            options.setHideBottomControls(true)
            UCrop.of(photoURI, Uri.fromFile(File(cacheDir, destinatiomPath)))
                .withAspectRatio(1f, 1f).withOptions(options).start(getActivity())
        }
        /** Adding note in workout and every round individually */
        else if (requestCode == 501 && resultCode == Activity.RESULT_OK) {
            var notes = data!!.getStringExtra("NotesFor")!!
            var notesText = data.getStringExtra("NoteText")!!
            try {
                if (notes == "CompleteWorkout") {
                    noteForExercise = notesText
                } else if (notes == "OnParticularRound") {
                    SetsAndRepsList.get(positionForAddNotes).noteForExerciseInRound = notesText
                    adapterSetAndRepsAddedExerciseAdapter.notifyItemChanged(positionForAddNotes)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.d("fskfhdjksg", "onActivityResult: notes $notes nText $notesText")
            positionForAddNotes = -1
        }
        /** good for field update when comming after selecting from goodfor list screen*/
        else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            var tempList =
                data!!.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
            val builder = SpannableStringBuilder()
            // var str1 = "Good For \n"
            //builder.append(str1)
            var str = ""
            goodForIdStr = ""
            if (tempList != null && tempList.size > 0) {
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).group_name.equals(tempList.get(0).group_name)) {

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            exerciseList.get(i).list.get(j).isCheck = false
                        }

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            for (k in 0..tempList.size - 1) {
                                if (tempList.get(k).id.equals(exerciseList.get(i).list.get(j).id)) {
                                    exerciseList.get(i).list.get(j).isCheck = true
                                    str = str + tempList.get(k).display_name + ", "
                                    goodForIdStr = goodForIdStr + tempList.get(k).id + ","

                                }
                            }
                        }
                    }
                }
                if (!str.isEmpty()) {
                    str = str.substring(0, str.length - 2)
                    Log.d("dfghksdgjdkgh", "onActivityResult: $str")
                    goodForIdStr = goodForIdStr.substring(0, goodForIdStr.length - 1)
                    Log.d("dfghksdgjdkgh", "onActivityResult1: $str")

                }
                var idsArray = goodForIdStr.split(",")

                try {
                    var eqp = ""
                    str.split(", ").forEach {
                        if (eqp.isEmpty()) eqp = "$it"
                        else eqp += " | $it"
                    }
                    binding.goodFor.text = eqp
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (!str.isEmpty()) {
                    binding.txtGoodFor.visibility = View.VISIBLE
                } else {
                    binding.txtGoodFor.visibility = View.GONE
                }

            } else {
                binding.txtGoodFor.visibility = View.GONE
                binding.goodFor.text = ""
                binding.goodFor.hint = (Html.fromHtml(getString(R.string.good_for_str)))
                for (j in 0..exerciseList.get(2).list.size - 1) {
                    exerciseList.get(2).list.get(j).isCheck = false
                }
            }
        }
        /** equipment  field update when comming after selecting from equipment list screen*/
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            var tempList =
                data!!.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
            val builder = SpannableStringBuilder()
            //var str1 = "Equipments \n"
            // builder.append(str1)
            var str = ""
            equipmentIdStr = ""
            if (tempList != null && tempList.size > 0) {

                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).group_name.equals(tempList.get(0).group_name)) {

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            exerciseList.get(i).list.get(j).isCheck = false
                        }

                        for (j in 0..exerciseList.get(i).list.size - 1) {
                            for (k in 0..tempList.size - 1) {
                                if (tempList.get(k).id.equals(exerciseList.get(i).list.get(j).id)) {
                                    exerciseList.get(i).list.get(j).isCheck = true
                                    equipmentIdStr = equipmentIdStr + tempList.get(k).id + ","
                                    str = str + exerciseList.get(i).list.get(j).display_name + ", "
                                }
                            }
                        }
                    }
                }
                if (!str.isEmpty()) {
                    str = str.substring(0, str.length - 2)
                    equipmentIdStr = equipmentIdStr.substring(0, equipmentIdStr.length - 1)
                }

                try {
                    var eqp = ""
                    str.split(", ").forEach {
                        if (eqp.isEmpty()) eqp = "$it"
                        else eqp += " | $it"
                    }
                    binding.tvEquipment.text = eqp
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                if (!str.isEmpty()) {
                    binding.equipmemnts.visibility = View.VISIBLE
                } else {
                    binding.equipmemnts.visibility = View.GONE
                }

            } else {
                binding.equipmemnts.visibility = View.GONE
                binding.tvEquipment.text = ""
                binding.tvEquipment.hint = (Html.fromHtml(getString(R.string.equipments_str)))
                for (j in 0 until exerciseList[1].list.size) {
                    exerciseList[1].list[j].isCheck = false
                }

            }
        }
        /***allowed user list */
        else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            tempAllowedUserList.clear()
            tempAllowedUserList =
                data!!.getSerializableExtra("list") as ArrayList<CustomerListModel.Data.User>
            val builder = SpannableStringBuilder()
            //var str1 = "Allowed Users \n"
            //builder.append(str1)
            var str = ""
            var str_id = ""
            if (tempAllowedUserList != null && tempAllowedUserList.size > 0) {
                for (i in 0..tempAllowedUserList.size - 1) str =
                    str + tempAllowedUserList.get(i).vName + " | "
            }

            if (tempAllowedUserList != null && tempAllowedUserList.size > 0) {
                for (j in 0..tempAllowedUserList.size - 1) {
                    if (str_id.isEmpty()) str_id = tempAllowedUserList.get(j).iCustomerId
                    str_id += " | " + tempAllowedUserList.get(j).iCustomerId
                }
            }

            if (!str.isEmpty()) {
                str = str.substring(0, str.length - 2)
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(), R.color.created_by_color
                        )
                    ), 0, spanStr.length, 0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.allowedUsersTxt.text = ss
                allowed_user_id = str_id

                if (ss.isNotEmpty()) {
                    binding.txtAllowed.visibility = View.VISIBLE
                } else {
                    binding.txtAllowed.visibility = View.GONE
                }
            } else {
                binding.txtAllowed.visibility = View.GONE
                binding.allowedUsersTxt.text = ""
                allowed_user_id = ""
            }


        }
        /**created by list*/
        else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            var tempList =
                data!!.getSerializableExtra("list") as ArrayList<CustomerListModel.Data.DoviesGuest>
            val builder = SpannableStringBuilder()
            var str1 = "Created by \n"
            builder.append(str1)
            var str = ""
            if (tempList != null && tempList.size > 0) {
                for (i in 0 until tempList.size) {
                    str = str + tempList.get(i).vGuestName + " | "
                    createById = tempList.get(i).iDeviosGuestId
                }
            }
            if (!str.isEmpty()) {
                str = str.substring(0, str.length - 2)
                val spanStr = SpannableString(str)
                spanStr.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            getActivity(), R.color.created_by_color
                        )
                    ), 0, spanStr.length, 0
                )
                builder.append(spanStr)
                var ss = SpannableString(builder)
                binding.createBy.text = ss
            } else {
                binding.createBy.text = ""
            }

        }
        /**comming here after selecting exercise for rounds*/
        else if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as ArrayList<ExerciseListingResponse.Data>
            if (tempSelectedExerciseList.size > 0) {
                for (i in 0 until tempSelectedExerciseList.size) {
                    tempSelectedExerciseList[i].exercise_reps_number =
                        getDataManager().getStringData(PREF_KEY_NUMBER_OF_REPS)!!
                    tempSelectedExerciseList[i].exercise_reps_time =
                        getDataManager().getStringData(PREF_KEY_REPS_FINISH_TIME)!!
                    tempSelectedExerciseList[i].exercise_timer_time =
                        getDataManager().getStringData(PREF_KEY_EXERCISE_TIME)!!
                    tempSelectedExerciseList[i].exercise_rest_time = "00:00"
                    tempSelectedExerciseList[i].isValid = true
                }
            }

            if (exerciseType == "SetAndReps") {
                val targetReps = binding.repsTimerTxt1.text.toString().trim()
                val targetSets = binding.repetetionTxt1.text.toString().toInt()
                var roundName = ""
                if (tempSelectedExerciseList[0].leftAndRightOrSuperSetOrAddExercise == "LeftAndRight") roundName =
                    "Left & Right"
                else roundName = tempSelectedExerciseList[0].leftAndRightOrSuperSetOrAddExercise!!

                if (roundName.isEmpty()) {
                    var list = ArrayList<ExerciseListingResponse.Data>()
                    tempSelectedExerciseList.forEach { list.add(CommanUtils.addDuplicateExercise1(it)) }
                    if (roundPositionForAddExercise >= 0) {
                        SetsAndRepsList[roundPositionForAddExercise].arrSets.forEach {
                            it.exerciseList.addAll(list)
                        }
                        roundPositionForAddExercise = -1
                    } else {
                        for (i in 1..tempSelectedExerciseList.size) {
                            SetsAndRepsList.add(
                                SetAndRepsModel(
                                    strRoundCounts = "",
                                    strRoundName = roundName,
                                    strTargetSets = "$targetSets",
                                    strTargetReps = "$targetReps",
                                    strExerciseType = roundName,
                                    strSetsCounts = "",
                                    arrSets = getSets(
                                        targetSets, tempSelectedExerciseList[i - 1], roundName
                                    )
                                )
                            )
                        }
                    }
                } else {
                    val setsList = ArrayList<SetSModel>()
                    for (i in 1..targetSets) {
                        var flag = false
                        if (i == 1) flag = true
                        val newArrayList = ArrayList<ExerciseListingResponse.Data>()
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
                        val newArrayList = ArrayList<ExerciseListingResponse.Data>()
                        tempSelectedExerciseList.forEach {
                            newArrayList.add(
                                CommanUtils.addDuplicateExercise1(
                                    it
                                )
                            )
                        }
                        SetsAndRepsList[roundPositionForAddExercise].arrSets.forEach {
                            it.exerciseList.addAll(
                                newArrayList
                            )
                        }
                        roundPositionForAddExercise = -1
                    } else {
                        SetsAndRepsList.add(
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
                adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
            } else {
                selectedExerciseList.addAll(tempSelectedExerciseList)
                adapter.notifyDataSetChanged()
            }
            seperator_line.visibility = View.GONE
            addGoodForAndEquipment(SetsAndRepsList)
            getGFANDEQP()
            if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility = View.VISIBLE
            else binding.seperatorLine.visibility = View.GONE

        }/*............................end.............................................*/
        else if (requestCode == 8 && resultCode == Activity.RESULT_OK) {
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as ArrayList<ExerciseListingResponse.Data>

            for (i in 0 until tempSelectedExerciseList.size) {
                var replaceExercisesModal = tempSelectedExerciseList.get(i)
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
                        videoLength = replaceExercisesModal.videoLength
                    )
                    if (itemPosition < selectedExerciseList.size) {
                        selectedExerciseList[itemPosition] = replaceModalData
                        adapter.notifyDataSetChanged()
                    }
                    addGoodForAndEquipment(SetsAndRepsList)
                    getGFANDEQP()
                    if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility =
                        View.VISIBLE
                    else binding.seperatorLine.visibility = View.GONE
                }
            }
        } else if (requestCode == 111 && resultCode == Activity.RESULT_OK) {// code for replace single exercise
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as ArrayList<ExerciseListingResponse.Data>
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
                            videoLength = replaceExercisesModal.videoLength
                        )

                        if ("FollowAlong" == exerciseType) {
                            selectedExerciseList.removeAt(repalceChildPosition)
                            selectedExerciseList.add(repalceChildPosition, replaceModalData)
                            adapter.notifyItemChanged(repalceChildPosition)
                            if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility =
                                View.VISIBLE
                            else binding.seperatorLine.visibility = View.GONE
                        } else {
                            val data = SetsAndRepsList[ReplaceParentPsotion].arrSets
                            for (i in 0 until data.size) {
                                data[i].exerciseList.removeAt(repalceChildPosition)
                                data[i].exerciseList.add(repalceChildPosition, replaceModalData)
                            }
                            SetsAndRepsList[ReplaceParentPsotion].arrSets = data
                            if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility =
                                View.VISIBLE
                            else binding.seperatorLine.visibility = View.GONE
                        }
                    }
                }
                adapterSetAndRepsAddedExerciseAdapter.notifyItemChanged(ReplaceParentPsotion)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as ArrayList<ExerciseListingResponse.Data>

            for (i in 0 until tempSelectedExerciseList.size) {
                var getExerciseData = tempSelectedExerciseList.get(i)

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
                    exercise_timer_time = getExerciseData.exercise_timer_time,
                    exercise_reps_time = getExerciseData.exercise_reps_time,
                    exercise_reps_number = getExerciseData.exercise_reps_number,
                    exercise_rest_time = getExerciseData.exercise_rest_time,
                    isValid = getExerciseData.isValid,
                    showLoader = getExerciseData.showLoader,
                    isClicked = getExerciseData.isClicked,
                    videoLength = getExerciseData.videoLength,
                    isExercisePopupVisible = false
                )
                val roundData = SetsAndRepsList[ReplaceParentPsotion].arrSets
                roundData.forEach {
                    it.exerciseList.removeAt(repalceChildPosition)
                    it.exerciseList.add(repalceChildPosition, replaceModalData)
                }
            }
            adapterSetAndRepsAddedExerciseAdapter.notifyItemChanged(ReplaceParentPsotion)
            getGFANDEQP()

        } else if (requestCode == 112 && resultCode == Activity.RESULT_OK) {// for replacing multiple duplicates
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as ArrayList<ExerciseListingResponse.Data>
            for (i in 0 until tempSelectedExerciseList.size) {
                var replaceExercisesModal = tempSelectedExerciseList[i]
                if (replaceExercisesModal.isSelected) {
                    if ("FollowAlong" == exerciseType) {
                        val exerciseData = selectedExerciseList[repalceChildPosition]

                        for (i in 0 until selectedExerciseList.size) {
                            if (selectedExerciseList[i].exercise_id == exerciseData.exercise_id) {
                                selectedExerciseList.removeAt(i)
                                selectedExerciseList.add(
                                    i, ExerciseListingResponse.Data(
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
                                        videoLength = replaceExercisesModal.videoLength
                                    )
                                )
                            }
                        }

                        /*   selectedExerciseList.forEachIndexed { index, it ->
                               if (it.exercise_id == exerciseData.exercise_id) {
                                   selectedExerciseList.removeAt(index)
                                   selectedExerciseList.add(
                                       index, ExerciseListingResponse.Data(
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
                                           videoLength = replaceExercisesModal.videoLength
                                       )
                                   )
                               }
                           }*/
                        if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility =
                            View.VISIBLE
                        else binding.seperatorLine.visibility = View.GONE

                        adapter.notifyDataSetChanged()
                    } else {
                        val exerciseData =
                            SetsAndRepsList[ReplaceParentPsotion].arrSets[0].exerciseList[repalceChildPosition]
                        SetsAndRepsList.forEach { round ->
                            val setsListInRound = round.arrSets
                            setsListInRound.forEach { it ->
                                for (i in 0 until it.exerciseList.size) {
                                    if (it.exerciseList[i].exercise_id == exerciseData.exercise_id) {
                                        it.exerciseList.removeAt(i)
                                        it.exerciseList.add(
                                            i, ExerciseListingResponse.Data(
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
                                                videoLength = replaceExercisesModal.videoLength
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        if (selectedExerciseList.isEmpty()) binding.seperatorLine.visibility =
                            View.VISIBLE
                        else binding.seperatorLine.visibility = View.GONE


                        adapterSetAndRepsAddedExerciseAdapter.notifyDataSetChanged()
                    }
                }
            }


            //adapterSetAndRepsAddedExerciseAdapter.notifyItemChanged(ReplaceParentPsotion)
            getGFANDEQP()
        }
        /**adding exercise in round*/
        else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            var tempSelectedExerciseList =
                data!!.getSerializableExtra("list") as ArrayList<ExerciseListingResponse.Data>

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
                    isExercisePopupVisible = false
                )
                // selectedExerciseList.set(j, replaceModalData)
                val roundData = SetsAndRepsList[roundPositionForAddExercise].arrSets
                roundData.forEach {
                    it.exerciseList.add(replaceModalData)
                }
            }
            if (SetsAndRepsList[roundPositionForAddExercise].arrSets[0].exerciseList.size == 1) {
                SetsAndRepsList[roundPositionForAddExercise].strRoundName = ""
            } else if (SetsAndRepsList[roundPositionForAddExercise].arrSets[0].exerciseList.size == 2) {
                if (SetsAndRepsList[roundPositionForAddExercise].strRoundName.isEmpty()) SetsAndRepsList[roundPositionForAddExercise].strRoundName =
                    "Superset"
            } else if (SetsAndRepsList[roundPositionForAddExercise].arrSets[0].exerciseList.size > 2) {
                SetsAndRepsList[roundPositionForAddExercise].strRoundName = "Superset"
            }
            adapterSetAndRepsAddedExerciseAdapter.notifyItemChanged(roundPositionForAddExercise)
            adapterSetAndRepsAddedExerciseAdapter.notifyadapters()
            roundPositionForAddExercise = -1
            /**blow old code is commented*//*  for (i in 0..tempSelectedExerciseList.size - 1) {
                  var getExerciseData = tempSelectedExerciseList.get(i)

                  if (getExerciseData.isSelected) {
                      for (j in 0..selectedExerciseList.size - 1) {
                          if (selectedExerciseList.get(j).exercise_id.equals(exerciseDataofItem.exercise_id)) {
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
                                  isReps = selectedExerciseList.get(j).isReps,
                                  exercise_timer_time = selectedExerciseList.get(j).exercise_timer_time,
                                  exercise_reps_time = selectedExerciseList.get(j).exercise_reps_time,
                                  exercise_reps_number = selectedExerciseList.get(j).exercise_reps_number,
                                  exercise_rest_time = selectedExerciseList.get(j).exercise_rest_time,
                                  isValid = selectedExerciseList.get(j).isValid,
                                  showLoader = getExerciseData.showLoader,
                                  isClicked = getExerciseData.isClicked,
                                  videoLength = getExerciseData.videoLength
                              )
                              // if (j<selectedExerciseList.size)
                              try {
                                  selectedExerciseList.set(j, replaceModalData)
                              } catch (ex: Exception) {
                                  ex.printStackTrace()
                              }
                          }

                          // }
                          adapter.notifyDataSetChanged()

                          addGoodForAndEquipment(selectedExerciseList)

                          if (selectedExerciseList.isEmpty())
                              binding.seperatorLine.visibility = View.VISIBLE
                          else
                              binding.seperatorLine.visibility = View.GONE
                      }
                  }
              }*/
            getGFANDEQP()
        }
    }

    private fun getSets(
        targetSets: Int, list: ExerciseListingResponse.Data, roundName: String
    ): ArrayList<SetSModel> {
        val setsList = ArrayList<SetSModel>()
        for (i in 1..targetSets) {
            var list1 = ArrayList<ExerciseListingResponse.Data>()
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

    fun saperator() {

    }

    fun addGoodForAndEquipment(SetsAndRepsList: ArrayList<SetAndRepsModel>) {

        if (SetsAndRepsList.isNotEmpty()) {
            var selectedExerciseList = ArrayList<ExerciseListingResponse.Data>()
            repeat(SetsAndRepsList.size) {
                if (SetsAndRepsList[it].arrSets[0].exerciseList.isNotEmpty()) {
                    selectedExerciseList.addAll(SetsAndRepsList[it].arrSets[0].exerciseList)
                }
            }
            try {
                var equipment = ""
                equipmentList.clear()
                var goodFor1 = ""
                equipmemnts.visibility = View.VISIBLE
                txt_good_for.visibility = View.VISIBLE
                selectedExerciseList.forEach {
                    if (goodFor1.isEmpty()) goodFor1 += it.exercise_body_parts
                    else goodFor1 += " | " + it.exercise_body_parts


                    if (it.exercise_equipments != "None" && it.exercise_equipments != "none") {
                        if (equipment.isEmpty()) {
                            if (it.exercise_equipments.contains("|")) {
                                var eqp = it.exercise_equipments.split("|")
                                eqp.forEach {
                                    if (!equipmentList.contains("$it")) {
                                        equipmentList.add(it)
                                    }

                                    if (equipment.isEmpty()) equipment += "$it"
                                    else equipment += " | $it"
                                }
                            } else {
                                equipment = " | " + it.exercise_equipments
                                if (!equipmentList.contains(it.exercise_equipments)) equipmentList.add(
                                    it.exercise_equipments
                                )
                            }
                        } else {
                            if (it.exercise_equipments.contains("|")) {
                                var eqp = it.exercise_equipments.split("|")
                                eqp.forEach {
                                    if (!equipmentList.contains(it)) equipmentList.add(it)

                                    if (equipment.isEmpty()) equipment += "$it"
                                    else equipment += " | $it"
                                }
                            } else {
                                equipment += " | " + it.exercise_equipments
                                if (!equipmentList.contains(it.exercise_equipments)) equipmentList.add(
                                    it.exercise_equipments
                                )
                            }

                        }
                    }

                }

                selectedExerciseGoodForList = goodFor1
                selectedExerciseequipmentList = equipment

                var eqpText =
                    ""//Glutes | Quadriceps | Hamstrings | Glutes | Quadriceps | Hamstrings
                if (tv_equipment.text.toString().trim().isNotEmpty()) {
                    eqpText = tv_equipment.text.toString().trim()

                    eqpText.split(" | ").forEach {
                        equipmentList.add(it.toString())
                    }
                }


                var eqp = ""
                for (i in 0 until equipmentList.size) {
                    if (eqp.isEmpty()) {
                        eqp = "${equipmentList[i].trim()}"
                    } else {
                        eqp += " | ${equipmentList[i].trim()}"
                    }
                }

                tv_equipment.text = eqp
                var goodForText =
                    ""//Glutes | Quadriceps | Hamstrings | Glutes | Quadriceps | Hamstrings
                if (good_for.text.toString().trim().isNotEmpty()) {
                    goodForText = good_for.text.toString().trim()

                }
                goodFor1 += " $goodForText"
                var gdfr = (goodFor1.split(" | ")).distinct()
                var gfor = ""
                gdfr.forEach {
                    if (gfor.isEmpty()) {
                        if (it.isNotEmpty()) gfor = "$it"
                    } else {
                        if (it.isNotEmpty()) gfor += " | $it"
                    }
                }
                good_for.text = gfor

                var gdfr1 = (equipment.split(" | ")).distinct()
                var gfor1 = ""
                gdfr1.forEach {
                    var data = "$it"
                    if (it.contains("|")) {
                        data = it.split("|")[0]
                    }


                    if (gfor1.isEmpty()) {
                        if (data.isNotEmpty()) gfor1 = "$data"
                    } else {
                        if (data.isNotEmpty()) if (data.contains(" | ")) gfor1 += " $data"
                        else gfor1 += " | $data"
                    }
                }


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


    }

    fun getGFANDEQP() {

        try {
            var addedEqp = tv_equipment.text.toString().trim().split(" | ")
            var addedGoodfor = good_for.text.toString().trim().split(" | ")

            arrayListGoodFor = ArrayList()
            eqpmntList = ArrayList()
            SetsAndRepsList.forEach { model ->
                model.arrSets[0].exerciseList.forEach {

                    if (it.exercise_body_parts.trim() != "None") {
                        if (it.exercise_body_parts.contains(" | ")) {
                            arrayListGoodFor.addAll(it.exercise_body_parts.split(" | "))
                        } else {
                            if (it.exercise_body_parts.contains(" ")) arrayListGoodFor.addAll(
                                it.exercise_body_parts.split(
                                    " "
                                )
                            )
                            else arrayListGoodFor.add(it.exercise_body_parts)
                        }
                    }
                    if (it.exercise_equipments.trim() != "None") {
                        if (it.exercise_equipments.contains(" | ")) {
                            eqpmntList.addAll(it.exercise_equipments.split(" | "))
                        } else {
                            if (it.exercise_equipments.contains(" ")) eqpmntList.addAll(
                                it.exercise_equipments.split(
                                    " "
                                )
                            )
                            else eqpmntList.add(it.exercise_equipments)
                        }
                    }
                }

                addedGoodfor.forEach { arrayListGoodFor.add(it) }
                addedEqp.forEach { eqpmntList.add(it) }
                var eqp = ""
                var eqp1 = ""
                eqpmntList.forEach {
                    if (it.isNotEmpty() && !eqp.contains(it)) {
                        if (eqp.isEmpty()) {
                            eqp = "$it"
                            eqp1 = "$it"
                        } else {
                            eqp += " | $it"
                            eqp1 += ",$it"
                        }
                    }

                }
                tv_equipment.text = eqp


                var gdfrList = ""
                var gdfrList1 = ""
                arrayListGoodFor.forEach {
                    if (it.isNotEmpty() && !gdfrList.contains(it)) {
                        if (gdfrList.isEmpty()) {
                            gdfrList = "$it"
                            gdfrList1 = "$it"
                        } else {
                            gdfrList += " | $it"
                            gdfrList1 += ",$it"
                        }
                    }
                }
                good_for.text = gdfrList
                // getIdForGoodFor(gdfrList1, eqp1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getIdForGoodFor() {
        goodForIdStr = ""
        equipmentIdStr = ""
        var addedEqp = tv_equipment.text.toString().trim()
            .split(" | ")//Abdominal | Bench | Abdominal Bench | Stepper Platform | Dumbbells | Flat Bench | ar | Barbell
        var addedGoodfor = good_for.text.toString().trim().split(" | ")
        var goodForUniveralList = exerciseList[2].list
        var eqpmentUniveralList = exerciseList[1].list
        //  var gList = gdfrList.split(",")
        //var eList = eqp1.split(",")

        goodForUniveralList.forEach { it1 ->
            addedGoodfor.forEach { it2 ->
                if (it1.display_name == it2) {
                    if (goodForIdStr.isEmpty()) {
                        goodForIdStr = it1.id
                    } else {
                        goodForIdStr += ",${it1.id}"
                    }
                }
            }
        }

        eqpmentUniveralList.forEach { it1 ->
            addedEqp.forEach { it2 ->
                if (it1.display_name.equals(it2, ignoreCase = true)) {
                    if (equipmentIdStr.isEmpty()) {
                        equipmentIdStr = it1.id
                    } else {
                        equipmentIdStr += ",${it1.id}"
                    }
                }
            }
        }

    }

    fun removeDuplicates(string: String): String {
        val set = string.toSet()
        return set.joinToString("")
    }

    private fun handleCropResult(@NonNull result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            Picasso.with(getActivity()).load(resultUri).into(binding.image)
            binding.image1.visibility = View.GONE
            binding.imgTxt.visibility = View.GONE
            userImageFile = File(resultUri.path)
        } else {
            Toast.makeText(getActivity(), "cannot_retrieve_cropped_image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun filter_WorkOut_and_doviesFitness_dialog(
        create_name_as_dialog_heading: String, dialog_overView_heading: String
    ) {
        dialog = Dialog(this, R.style.MyTheme_Transparent)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnDismissListener { binding.viewTransParancy.visibility = View.GONE }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_fitness_dialog_view)
        dialog.window?.setLayout(width - 30, WindowManager.LayoutParams.WRAP_CONTENT)
        val dialog_Heading = dialog.findViewById(R.id.txt_dialog_heading) as TextView
        val dialog_overViewDiscription =
            dialog.findViewById(R.id.txt_overView_discritpion) as TextView
        if (!create_name_as_dialog_heading.isEmpty()) {
            dialog_Heading.text = create_name_as_dialog_heading

            var s = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(getString(R.string.create_information), Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(getString(R.string.create_information))
            }
            dialog_overViewDiscription.text = s
            dialog.scrollView.visibility = View.VISIBLE

            var htmlData =
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + getString(R.string.create_information)
            //   dialog.mWebView.loadData(getString(R.string.create_information),"text/html","utf-8");
            dialog.mWebView.setBackgroundColor(resources.getColor(R.color.col_input_bg))

            dialog.mWebView.loadDataWithBaseURL(
                "file:///android_asset/", htmlData, "text/html", "UTF-8", null
            )

            dialog.txt_hideView.visibility = View.GONE
            binding.viewTransParancy.visibility = View.VISIBLE

        } else {
            dialog_Heading.text = "FILTER WORKOUT"
            dialog_overViewDiscription.text = ""
            dialog.txt_hideView.visibility = View.VISIBLE
            binding.viewTransParancy.visibility = View.VISIBLE
        }

        dialog.show()

        dialog.iv_cancle_dialog.setOnClickListener {
            // view transparent background of dialog
            binding.viewTransParancy.visibility = View.GONE
            dialog.dismiss()
        }
    }

    private fun createWorkoutTimeDialog(displayTime: Int, displayStr: String) {
        dialog = Dialog(this, R.style.MyTheme_Transparent)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnDismissListener { binding.viewTransParancy.visibility = View.GONE }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_workout_time)
        dialog.window?.setLayout(width - 30, WindowManager.LayoutParams.WRAP_CONTENT)

        val no_time_layout = dialog.findViewById(R.id.no_time_layout) as RelativeLayout
        val time_layout = dialog.findViewById(R.id.time_layout) as RelativeLayout
        val dialog_Heading = dialog.findViewById(R.id.txt_dialog_heading) as TextView
        val dialog_overViewDiscription =
            dialog.findViewById(R.id.txt_overView_discritpion) as TextView
        val time = dialog.findViewById(R.id.time) as TextView
        val time_txt = dialog.findViewById(R.id.time_txt) as TextView
        dialog_Heading.text = "TOTAL WORKOUT TIME"

        dialog_overViewDiscription.text =
            resources.getString(R.string.your_total_workout_time_will_display_here)

        if (displayTime == 0) {
            no_time_layout.visibility = View.VISIBLE
            time_layout.visibility = View.GONE
        } else {
            time.text = "" + displayTime
            time_txt.text = displayStr
            no_time_layout.visibility = View.GONE
            time_layout.visibility = View.VISIBLE
        }

        binding.viewTransParancy.visibility = View.VISIBLE
        dialog.show()

        dialog.iv_cancle_dialog.setOnClickListener {
            // view transparent background of dialog
            binding.viewTransParancy.visibility = View.GONE
            dialog.dismiss()
        }
    }

    private fun createWorkout(exerciseArray: String) {
        //groups  if set and reps then send array in given groups key
        /** total_time,
         * exercise_count,isFeatured,workoutType,notes */
        // goodForIdStr = getGoodForIds(exerciseList[4].list)
        // equipmentIdStr =  getEquipmentsIds(exerciseList[1].list)


        Log.d(
            "params",
            "createWorkout: " + " workout_image-$userImageFile," + "device_token-${getDataManager().getUserInfo().customer_auth_token}" + "device_id-device_id" + "device_type-${StringConstant.Android}" + "auth_customer_id - ${getDataManager().getUserInfo().customer_auth_token}" + "workout_equipment-$equipmentIdStr" + "workout_description-${
                binding.overview.text.toString().trim()
            }" + "groups-$exerciseArray" + "workoutType-0" + "notes-$noteForExercise" + "workout_name-${
                binding.workoutName.text.toString().trim()
            }" + "workout_level-${
                binding.levelName.text.toString().trim()
            }" + "addedBy-$createByStr" + "addedById-$createById" + "accessLevel-$accessLevelValue" + "allowNotification-$allowNotificationvalue" + "allowedUsers-${
                allowed_user_id.replace(
                    " | ", ","
                )
            }" + "authToken-${getDataManager().getUserInfo().customer_auth_token}"
        )

        totalWorkoutTime = "00:${binding.timeExerciseTxt1.text.toString().trim()}"
        binding.progressLayout.visibility = View.VISIBLE

        val multiPartBuilder = AndroidNetworking.upload(Webservice.CREATE_WORKOUT)
        if (userImageFile != null) {
            multiPartBuilder.addMultipartFile("workout_image", userImageFile)
        } else {
            multiPartBuilder.addMultipartParameter("workout_image", userImageFile)
        }
        multiPartBuilder.addMultipartParameter(
            StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter(StringConstant.device_id, "")
        multiPartBuilder.addMultipartParameter(StringConstant.device_type, StringConstant.Android)
        multiPartBuilder.addMultipartParameter(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter("workout_equipment", equipmentIdStr)
        multiPartBuilder.addMultipartParameter(
            "workout_description", binding.overview.text.toString().trim()
        )

        if (exerciseType == "SetAndReps") {
            multiPartBuilder.addMultipartParameter("groups", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "1")

        } else {
            multiPartBuilder.addMultipartParameter("exercise", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "0")
        }

        multiPartBuilder.addMultipartParameter("workout_good_for", goodForIdStr)
        multiPartBuilder.addMultipartParameter("notes", noteForExercise)
        multiPartBuilder.addMultipartParameter("totalWorkoutTime", totalWorkoutTime)

        multiPartBuilder.addMultipartParameter(
            "workout_name", binding.workoutName.text.toString().trim()
        )
        if (binding.levelName.text.toString().trim() == "All Levels") {
            multiPartBuilder.addMultipartParameter("workout_level", "All")
        } else multiPartBuilder.addMultipartParameter(
            "workout_level", binding.levelName.text.toString().trim()
        )
        if (isAdmin.equals("Yes", true)) {
            multiPartBuilder.addMultipartParameter("createdById", createById)

            multiPartBuilder.addMultipartParameter("addedBy", createByStr)
            multiPartBuilder.addMultipartParameter("addedById", createById)
            multiPartBuilder.addMultipartParameter("accessLevel", accessLevelValue)
            multiPartBuilder.addMultipartParameter("allowNotification", allowNotificationvalue)
            multiPartBuilder.addMultipartParameter(
                "allowedUsers", allowed_user_id.replace(" | ", ",")
            )
        }
        multiPartBuilder.addHeaders(
            StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addHeaders(StringConstant.apiKey, "HBDEV")
        multiPartBuilder.addHeaders(StringConstant.apiVersion, "1")
        Log.d("allParams", "createWorkout: $multiPartBuilder")
        multiPartBuilder.build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                binding.progressLayout.visibility = View.GONE
                Log.d("sdgsdfgdfgdgdsgdgdsgsdg", "filter response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if ("1".equals(success)) {
                    // Constant.showCustomToast(getActivity(), "success..." + message)
                    if (isEdit.equals("edit", true)) {
                        setResult(12345)
                    }

                    // removeFragment3(HomeTabActivity., R.id.container_id, true)

                    else if (myWorkoutFragment != null && !myWorkoutFragment.isEmpty() && myWorkoutFragment.equals(
                            "myWorkoutFragment"
                        )
                    ) setResult(1234, Intent().putExtra("myFrag", "myFrag"))
                    else {
                        startActivity(
                            Intent(getActivity(), ActivityMyWorkoutList::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                    }
                    finish()
                } else Constant.showCustomToast(getActivity(), "fail..." + message)
            }

            override fun onError(anError: ANError?) {
                binding.progressLayout.visibility = View.GONE
                Constant.showCustomToast(
                    this@CreateWorkoutActivity, getString(R.string.something_wrong)
                )
                if (anError != null) {
                    Constant.errorHandle(anError, this@CreateWorkoutActivity)
                }
            }
        })
    }

    /*  private fun getEquipmentsIds(list: ArrayList<FilterExerciseResponse.Data.X>): String {
          var ids = ""
          var names = tv_equipment.text.toString().trim().split(" | ")

          list.forEach { it1 ->
              names.forEach { it2 ->
                  if (it1.display_name == it2.trim()) {
                      if (ids.isEmpty()) {
                          ids = it1.id
                      } else
                          ids += ",${it1.id}"
                  }
              }


          }
          Log.d("abddf", "getEquipmentsIds: $ids")
          return ids
      }*/

    private fun editWorkout(exerciseArray: String) {
        totalWorkoutTime = binding.timeExerciseTxt1.text.toString().trim()
        binding.progressLayout.visibility = View.VISIBLE
        val multiPartBuilder = AndroidNetworking.upload(Webservice.EDIT_WORKOUT)
        if (userImageFile != null) {
            multiPartBuilder.addMultipartFile("workout_image", userImageFile)
        } else {
            multiPartBuilder.addMultipartParameter("workout_image", userImageUrl)
        }
        multiPartBuilder.addMultipartParameter(
            StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter(StringConstant.device_id, "")
        multiPartBuilder.addMultipartParameter(StringConstant.device_type, StringConstant.Android)
        multiPartBuilder.addMultipartParameter(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addMultipartParameter("workout_equipment", equipmentIdStr)
        multiPartBuilder.addMultipartParameter(
            "workout_description", binding.overview.text.toString().trim()
        )


        if (exerciseType == "SetAndReps") {
            multiPartBuilder.addMultipartParameter("groups", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "1")

        } else {
            multiPartBuilder.addMultipartParameter("exercise", exerciseArray)
            multiPartBuilder.addMultipartParameter("workoutType", "0")
        }

        multiPartBuilder.addMultipartParameter("exercise", exerciseArray)
        multiPartBuilder.addMultipartParameter("totalWorkoutTime", "00:$totalWorkoutTime")
        multiPartBuilder.addMultipartParameter("workout_good_for", goodForIdStr)
        multiPartBuilder.addMultipartParameter(
            "workout_name", binding.workoutName.text.toString().trim()
        )
        if (binding.levelName.text.toString().trim() == "All Levels") {
            multiPartBuilder.addMultipartParameter("workout_level", "All")
        } else multiPartBuilder.addMultipartParameter(
            "workout_level", binding.levelName.text.toString().trim()
        )

        if (isAdmin.equals("Yes", true)) {
            multiPartBuilder.addMultipartParameter("createdById", createById)
            multiPartBuilder.addMultipartParameter("addedBy", createByStr)
            multiPartBuilder.addMultipartParameter("addedById", createById)
            multiPartBuilder.addMultipartParameter("accessLevel", accessLevelValue)
            multiPartBuilder.addMultipartParameter("allowNotification", allowNotificationvalue)
            multiPartBuilder.addMultipartParameter(
                "allowedUsers", allowed_user_id.replace(" | ", ",")
            )

        }

        multiPartBuilder.addMultipartParameter("workout_id", "" + WDetail.workout_id)
        multiPartBuilder.addMultipartParameter("parent_workout_id", "" + WDetail.workout_id)
        multiPartBuilder.addHeaders(
            StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token
        )
        multiPartBuilder.addHeaders(StringConstant.apiKey, "HBDEV")
        multiPartBuilder.addHeaders(StringConstant.apiVersion, "1")
        multiPartBuilder.build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                binding.progressLayout.visibility = View.GONE
                Log.d("TAG", "filter response...." + response!!.toString(4))
                val jsonObject: JSONObject? = response.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if ("1".equals(success)) {
                    // Constant.showCustomToast(getActivity(), "success..." + message)
                    if (isEdit.equals("edit", true)) {
                        setResult(12345)
                    }
                    startActivity(
                        Intent(getActivity(), ActivityMyWorkoutList::class.java).setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    )
                    finish()
                } else Constant.showCustomToast(getActivity(), "fail...$message")
            }

            override fun onError(anError: ANError?) {
                binding.progressLayout.visibility = View.GONE
                Constant.showCustomToast(
                    this@CreateWorkoutActivity, getString(R.string.something_wrong)
                )
                if (anError != null) {
                    Constant.errorHandle(anError, this@CreateWorkoutActivity)
                }
            }
        })
    }

    override fun forExchangeItem(
        exerciseData: ExerciseListingResponse.Data,
        exerpos: Int,
        exercisesList: ArrayList<ExerciseListingResponse.Data>,
        roundPos: Int
    ) {
        Log.v("nameOfExecreis", "" + exerciseData.exercise_name)
        Log.d("sgdskjgdks", "forExchangeItem: " + "12345")

        showCreatePlanAndWorkOutDialog(exerciseData, exerpos, exercisesList, Rpoundpos)
    }

    private fun showCreatePlanAndWorkOutDialog(
        exerciseData: ExerciseListingResponse.Data,
        esercisePos: Int,
        exercisesList: ArrayList<ExerciseListingResponse.Data>,
        Roundpos: Int
    ) {

        ReplaceParentPsotion = Roundpos
        repalceChildPosition = esercisePos
        exerciseDataofItem = exerciseData

        tempList = ArrayList()

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
                    "Change all of the repeated exercise of this demonstration", "2"
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
                    "Change all of the repeated exercise of this demonstration", "1"
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
            if ("FollowAlong" == exerciseType) {
                tempList.clear()
                var exerciseModel = selectedExerciseList[repalceChildPosition]
                selectedExerciseList.forEach {
                    if (exerciseModel.exercise_id == it.exercise_id) {
                        ReplaceExerciseId = exerciseModel.exercise_id
                        tempList.add(it)
                    }
                }
                if (tempList != null && tempList.size != 0 && tempList.size > 1) {
                    var intent = Intent(this@CreateWorkoutActivity, AddExerciseActivity::class.java)
                    intent.putExtra("forReplace", "forReplace")
                    intent.putExtra("workout_Type", exerciseType)
                    startActivityForResult(intent, 112)
                }
            } else {
                tempList.clear()
                var exerciseModel =
                    SetsAndRepsList[ReplaceParentPsotion].arrSets[0].exerciseList[repalceChildPosition]
                SetsAndRepsList.forEach {
                    it.arrSets[0].exerciseList.forEach { exer ->
                        if (exerciseModel.exercise_id == exer.exercise_id) {
                            ReplaceExerciseId = exerciseModel.exercise_id
                            tempList.add(exer)
                        }
                    }
                }
                if (tempList != null && tempList.size != 0 && tempList.size > 1) {
                    var intent = Intent(this@CreateWorkoutActivity, AddExerciseActivity::class.java)
                    intent.putExtra("forReplace", "forReplace")
                    intent.putExtra("workout_Type", exerciseType)
                    startActivityForResult(intent, 112)
                }
            }
        }

    }

    override fun onDialogDismiss() {

    }

    /** making json for sending in api for add */
    fun makeJsonArrayOfRoundsAndExercises(): JSONArray {
        val mainJsonArray = JSONArray()


        for (i in 0 until SetsAndRepsList.size) {  // round level loop
            val main = SetsAndRepsList[i]
            var mainJsonObject = JSONObject()
            val setsJsonArray = JSONArray()
            for (j in 0 until main.arrSets.size) {  // sets level loop
                val set = main.arrSets[j]
                val setsJsonObject = JSONObject()
                val ExerciseJsonArray = JSONArray()

                for (k in 0 until set.exerciseList.size) {  // exercise level loop
                    val exercise = set.exerciseList[k]
                    var reps = "Select"
                    if (exercise.selected_exercise_reps_number != null && exercise.selected_exercise_reps_number!!.isNotEmpty() && exercise.selected_exercise_reps_number != "0") reps =
                        exercise.selected_exercise_reps_number!!

                    val ExerciseJsonObject = JSONObject()
                    ExerciseJsonObject.put("exercise_id", exercise.exercise_id)
                    ExerciseJsonObject.put("reps", reps)
                    ExerciseJsonObject.put("weight", exercise.selected_exercise_weight_number)
                    ExerciseJsonObject.put("sequence", (k + 1).toString())
                    ExerciseJsonArray.put(ExerciseJsonObject)
                }

                setsJsonObject.put("exercise", ExerciseJsonArray)
                setsJsonObject.put("set", "SET " + (j + 1))
                setsJsonArray.put(setsJsonObject)
            }

            // mainJsonObject.put("group_type", main.strRoundCounts)
            mainJsonObject.put("target_sets", main.strTargetSets)
            mainJsonObject.put("target_reps", main.strTargetReps)
            mainJsonObject.put("sequence_no", (i + 1))
            // Left & Right SetAndReps

            if (main.strRoundName == "Left & Right") mainJsonObject.put("group_type", "2")
            else if (main.strRoundName == "SuperSet") mainJsonObject.put("group_type", "1")
            else mainJsonObject.put("group_type", "0")


            mainJsonObject.put("notes", main.noteForExerciseInRound)
            mainJsonObject.put("sets", setsJsonArray)
            mainJsonArray.put(mainJsonObject)
        }



        return mainJsonArray
    }
}

