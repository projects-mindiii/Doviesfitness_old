package com.doviesfitness.ui.bottom_tabbar.home_tab.activity

import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityWorkOutDetailBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.SocialMediaLinksAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.WorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.WorkoutDetailExerciseSetAndRepsAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.workoutDetailsSetRepsadapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.dialog.SaveEditWorkoutDialog
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.ExerciseTransData
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.SocialMediaType
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetailResponce
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutGroupsData
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ActivityMyWorkoutList
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.CreateWorkoutActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.WorkoutVideoPlayActivityNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetAndRepsModel
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.SetSModel
import com.doviesfitness.ui.new_player.activity.NewPlayerView
import com.doviesfitness.ui.showDietPlanDetail.ShowDietPlanDetailActivity
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constant.Companion.requestAudioFocusForMyApp
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.authToken
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.activity_work_out_detail.iv_social_menu_link
import kotlinx.android.synthetic.main.activity_work_out_detail.rv_Media_links
import kotlinx.android.synthetic.main.activity_work_out_detail.transparentBlurView
import kotlinx.android.synthetic.main.dialog_delete.tv_header
import kotlinx.android.synthetic.main.filter_fitness_dialog_view.iv_cancle_dialog
import kotlinx.android.synthetic.main.filter_fitness_dialog_view.txt_hideView
import net.khirr.library.foreground.Foreground
import org.json.JSONObject
import java.io.File


class WorkOutDetailActivity : BaseActivity(), View.OnClickListener,
    WorkoutAdapter.WorkOutItemClickListener, SaveEditWorkoutDialog.CommentCallBack, IsSubscribed {
    override fun isSubscribed() {
        val intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }

    private var program_id: String = ""
    private var from_which_frament: String = ""
    private var whichScreen: String = ""
    private var program_plan_id: String? = ""
    private var create_name_as_dialog_heading: String = ""
    private var dialog_overView_heading: String = ""
    private var workOut_share_link: String = ""
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var exerciseAdapter: WorkoutAdapter
    lateinit var binding: ActivityWorkOutDetailBinding
    lateinit var exerciseList: ArrayList<WorkoutExercise>
    lateinit var setAndRepsExerciseList: ArrayList<WorkoutGroupsData>
    var isScroll: Boolean = false
    var isdownload: Boolean = true
    var moduleId: String = ""
    var isMyWorkout = ""
    var myWorkoutFragment = ""
    var workoutAccessLvel = ""
    var fromDeepLinking = ""
    private var refid: Long = 0
    internal var list = ArrayList<Long>()
    lateinit var WDetail: WorkoutDetail
    private var downloadManager: DownloadManager? = null
    internal lateinit var mBroadCastReceiver: BroadcastReceiver
    var isRecieverRegistered = false
    internal var mFinishedFilesFromNotif = ArrayList<Long>()
    lateinit var mProgressThread: Thread
    var isDownloadSuccess = false
    lateinit var f: File
    private var countdownRunnable: Runnable? = null
    private val countdownhandler = Handler()
    lateinit var dialog: Dialog
    private var width: Int = 0
    private var fromHomeTab: String = ""
    lateinit var workerDetail: WorkoutDetailResponce
    var AllowUserList = ArrayList<String>()
    var socialMediaLinks = ArrayList<SocialMediaType>()
    lateinit var roundadapter: workoutDetailsSetRepsadapter
    var mainViewTotalHeight = 0

    companion object {
        var Parentisalloweduser: Boolean = false
        var isalloweduser: Boolean = false
        var workout_access_level: String = ""
    }

    /**Social media links adapter*/
    fun setSocialMediaLinkAdapter(
        socialMediaLinks: ArrayList<SocialMediaType>, recyclerView: RecyclerView
    ) {

        val commentsAdapter = SocialMediaLinksAdapter(
            this,
            socialMediaLinks,
            object : SocialMediaLinksAdapter.SocialMediaLinkClick {
                override fun OnLinkClick(url: String) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            })
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = commentsAdapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        hideNavigationBar()
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_out_detail)

        if (intent.hasExtra("From_WORKOUTPLAN")) {
            whichScreen = intent.getStringExtra("From_WORKOUTPLAN").toString()
        }
        moduleId = intent.getStringExtra("PROGRAM_DETAIL").toString()
        isMyWorkout = intent.getStringExtra("isMyWorkout").toString()
        fromDeepLinking = intent.getStringExtra("fromDeepLinking").toString()
        if (intent.hasExtra("from_ProgramPlan")) {
            if (intent.getStringExtra("from_ProgramPlan") != null) {
                program_plan_id = intent.getStringExtra("from_ProgramPlan")
            }
        }
        if (intent.hasExtra("program_id")) {
            if (intent.getStringExtra("program_id") != null) {
                program_id = intent.getStringExtra("program_id")!!
            }
        }
        if (intent.hasExtra("myWorkoutFragment")) {
            if (intent.getStringExtra("myWorkoutFragment") != null) {
                myWorkoutFragment = intent.getStringExtra("myWorkoutFragment")!!
            }
        }
        if (intent.hasExtra("from_which_frament")) {
            if (intent.getStringExtra("from_which_frament") != null) {
                from_which_frament = intent.getStringExtra("from_which_frament")!!
            }
        }
        if (intent.hasExtra("fromHomeScreen")) {
            if (intent.getStringExtra("fromHomeScreen") != null) {
                fromHomeTab = intent.getStringExtra("fromHomeScreen")!!
            }
        }

        Parentisalloweduser = ShowDietPlanDetailActivity.isalloweduser
        binding.ivBack.setOnClickListener(this)
        binding.ivMore.setOnClickListener(this)
        binding.ivFav.setOnClickListener(this)
        binding.musicPlayerLayout.setOnClickListener(this)
        binding.overViewLayout.setOnClickListener(this)
        binding.sharingLayout.setOnClickListener(this)
        binding.sharingLayout.setOnClickListener(this)
        binding.upgradeLayout.setOnClickListener(this)
        binding.ivSocialMenuLink.setOnClickListener(this)
        binding.playVideo.setOnClickListener(this)
        Foreground.init(application)
        Foreground.addListener(foregroundListener)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        getWorkoutDetail(moduleId)
        val displaymetrics = DisplayMetrics()
        getActivity().windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        width = displaymetrics.widthPixels
        initView()
    }

    private fun initView() {
        exerciseList = ArrayList()
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x
        val screenWidth = size.x / 320
        val videowidth = 120 + (160 * screenWidth)

        /*Set Adapter*/
        exerciseAdapter = WorkoutAdapter(this, this.exerciseList, this, videowidth, this)
        layoutManager = LinearLayoutManager(this)
        binding.rvWorkout.layoutManager = layoutManager
        binding.rvWorkout.adapter = exerciseAdapter

        binding.svMain.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->

        })
    }

    fun setWorkoutAdapter() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x
        val screenWidth = size.x / 320
        val videowidth = 120 + (160 * screenWidth)
        roundadapter = workoutDetailsSetRepsadapter(
            this,
            setAndRepsExerciseList,
            object : workoutDetailsSetRepsadapter.WorkOutItemClickListener {
                override fun videoPlayClick(
                    isScroll: Boolean,
                    data: ExerciseTransData,
                    parentPos: Int,
                    childPos: Int,
                    mHolder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder,
                    isLoad: Boolean,
                    player: Player?
                ) {

                    if (isLoad) {
                        downloadExercise(data.workout_exercise_video)
                    }
                }

                override fun shareURL(data: ExerciseTransData) {
                    sharePost(data.exercise_share_url)
                }

                override fun setFavUnfav(data: ExerciseTransData, position: Int, view: ImageView) {
                    val param = HashMap<String, String>()
                    param[StringConstant.auth_customer_id] =
                        getDataManager().getUserInfo().customer_auth_token
                    param[StringConstant.module_name] = StringConstant.exercise
                    param[StringConstant.module_id] = data.workout_exercises_id
                    val header = HashMap<String, String>()
                    header[authToken] = getDataManager().getUserInfo().customer_auth_token
                    getDataManager().makeFavoriteApi(param, header)
                        ?.getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {

                                val jsonObject: JSONObject? =
                                    response?.getJSONObject(StringConstant.settings)
                                val success: String? = jsonObject?.getString(StringConstant.success)
                                if (success.equals("1")) {
                                    if (data.workout_exercise_is_favourite == "0") {
                                        data.workout_exercise_is_favourite = "1"
                                        view.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                getActivity(), R.drawable.ic_star_active
                                            )
                                        )
                                    } else {
                                        data.workout_exercise_is_favourite = "0"
                                        view.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                getActivity(), R.drawable.ic_star
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onError(anError: ANError) {
                                Constant.errorHandle(anError, getActivity())
                            }
                        })

                }
            },
            videowidth,
            this,
            object : workoutDetailsSetRepsadapter.StopScroll {
                override fun stopScrolling(isScroll: Boolean) {

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
            })
        layoutManager = LinearLayoutManager(this)
        binding.rvWorkout.layoutManager = layoutManager
        binding.rvWorkout.adapter = roundadapter


        binding.svMain.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->

        })
    }

    fun updateUi(flag: Boolean, mHolder: WorkoutDetailExerciseSetAndRepsAdapter.MyViewHolder) {

        if (flag) {
            this@WorkOutDetailActivity.isScroll = false
            binding.svMain.setEnableScrolling(true)
        } else {
            binding.rvWorkout.requestChildFocus(mHolder.itemView, mHolder.itemView)
            binding.svMain.setEnableScrolling(false)
        }


    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        countdownRunnable?.let { countdownhandler.postDelayed(it, 0) }

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

    override fun textOnClick1(type: String) {
        if (type.equals(
                "edit", true
            ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                .equals(
                    "No", true
                ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                .equals(
                    "0"
                )
        ) {
            if (isMyWorkout.equals("yes")) {
                var isLock = false
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }

                //if (this::workerDetail.isInitialized) workerDetail.data.workout_exercise_list.get(0).exercise_access_level

                if (isLock) {
                    var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                        "home", "no"
                    ).putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                } else {
                    val intent = Intent(getActivity(), CreateWorkoutActivity::class.java)
                    intent.putExtra("workoutDetail", WDetail)
                       // .putExtra("workoutExerciseList", exerciseList)
                        .putExtra("edit", "edit")
                        .putExtra("isMyWorkout", isMyWorkout)
                        .putExtra("fromDeepLinking", fromDeepLinking)

                    if ( workerDetail.data.workout_detail[0].workout_type == "1"){
                        intent.putExtra("setAndRepsExerciseList", setAndRepsExerciseList)
                    }else
                    {
                        intent.putExtra("workoutExerciseList", exerciseList)
                    }
                    startActivityForResult(intent, 12345)
                }

            } else {

                var isLock = false
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }
                if (this::workerDetail.isInitialized) workerDetail.data.workout_exercise_list.get(0).exercise_access_level
                if (isLock) {
                    var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                        "home", "no"
                    ).putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)

                } else {
                    val intent = Intent(getActivity(), CreateWorkoutActivity::class.java)
                    intent.putExtra("workoutDetail", WDetail)
                        .putExtra("workoutExerciseList", exerciseList)
                        .putExtra("edit", "edit")
                        .putExtra("isMyWorkout", isMyWorkout)
                        .putExtra("fromDeepLinking", fromDeepLinking)
                    startActivityForResult(intent, 12345)
                }

            }
        } else {

            var listOfExercises = ArrayList<SetAndRepsModel>()

            var workoutType = ""
            if (WDetail.workout_type == "1") {
                workoutType = "Set & Reps"
                setAndRepsExerciseList.forEach { setAndRepModel ->
                    var roundName = ""
                    if (setAndRepModel.iGroupType == "2") roundName = "Left & Right"
                    else if (setAndRepModel.iGroupType == "1") roundName = "Superset"
                    else roundName = ""
                    listOfExercises.add(
                        SetAndRepsModel(
                            strExerciseType = "",
                            strRoundCounts = "",
                            strRoundName = roundName,
                            strTargetSets = setAndRepModel.iTargetSets,
                            strTargetReps = setAndRepModel.iTargetReps,
                            strSetsCounts = "",
                            arrSets = getSetModel(setAndRepModel),
                            isRoundPositionPopupOIsVisible = false,
                            noteForExerciseInRound = "",
                            isPostnotifiedExerciseAdapter = false
                        )
                    )


                }

            } else {
                workoutType = "Follow Along"
            }


            val intent = Intent(getActivity(), CreateWorkoutActivity::class.java)
            intent.putExtra("workoutDetail", WDetail)
            intent.putExtra("workoutExerciseList", exerciseList)
            intent.putExtra("setAndRepsExerciseList", listOfExercises).putExtra("edit", "edit")
            intent.putExtra("isMyWorkout", isMyWorkout)
            intent.putExtra("creator_name", workerDetail.data.created_by.creator_name)
            intent.putExtra("dg_devios_guest_id", workerDetail.data.created_by.dg_devios_guest_id)
            intent.putExtra("sub_title", workerDetail.data.created_by.sub_title)
            intent.putExtra("fromDeepLinking", fromDeepLinking)
            intent.putExtra("workoutType", workoutType)
            startActivityForResult(intent, 12345)


        }
    }

    private fun getSetModel(setAndRepModel: WorkoutGroupsData): java.util.ArrayList<SetSModel> {
        var stesList = ArrayList<SetSModel>()

        setAndRepModel.groupSetsData.forEachIndexed { index, setsModel ->

            var flag = false
            if (index == 0) flag = true
            stesList.add(
                SetSModel(
                    setName = "SET ${index + 1}",
                    exerciseList = getExerciseList(setsModel.exerciseTransData),
                    strExerciseType = "",
                    isSelected = flag
                )
            )
        }
        return stesList
    }

    fun getExerciseList(exerciseTransData: List<ExerciseTransData>): java.util.ArrayList<ExerciseListingResponse.Data> {
        var list = ArrayList<ExerciseListingResponse.Data>()
        exerciseTransData.forEach { transeData ->
            list.add(CommanUtils.addDuplicateExercise2(transeData))
        }

        return list
    }

    fun getExerciseList2(exerciseTransData: List<ExerciseTransData>): java.util.ArrayList<WorkoutExercise> {
        var list = ArrayList<WorkoutExercise>()
        exerciseTransData.forEach { transeData ->
            list.add(
                WorkoutExercise(
                    exercise_access_level = "${transeData.exercise_access_level}",
                    exercise_share_url = "${transeData.exercise_share_url}",
                    iSequenceNumber = "${transeData.iSequenceNumber}",
                    is_liked = "${transeData.is_liked}",
                    workout_exercise_body_parts = "${transeData.workout_exercise_body_parts}",
                    workout_exercise_body_parts_id = "${transeData.workout_exercise_body_parts_id}",
                    workout_exercise_break_time = "0",
                    workout_exercise_description = "${transeData.workout_exercise_description}",
                    workout_exercise_detail = "${transeData.workout_exercise_detail}",
                    workout_exercise_equipments = "${transeData.workout_exercise_equipments}",
                    workout_exercise_equipments_id = "${transeData.workout_exercise_equipments_id}",
                    workout_exercise_image = "${transeData.workout_exercise_image}",
                    workout_exercise_is_favourite = "${transeData.workout_exercise_is_favourite}",
                    workout_exercise_level = "${transeData.workout_exercise_level}",
                    workout_exercise_name = "${transeData.workout_exercise_name}",
                    workout_exercise_time = "${transeData.isRestTime}",
                    workout_exercise_type = "1",
                    workout_exercise_video = "${transeData.workout_exercise_video}",
                    workout_exercises_id = "${transeData.workout_exercises_id}",
                    workout_repeat_text = "0",
                    workout_offline_video = "${transeData.workout_offline_video}",
                    Progress = transeData.Progress,
                    MaxProgress = transeData.MaxProgress,
                    isPlaying = transeData.isPlaying,
                    isComplete = transeData.isComplete,
                    isRestTime = transeData.isRestTime
                )
            )
        }

        return list
    }

    private fun getWorkoutDetail(news_module_id: String) {
        binding.myProgress.visibility = VISIBLE

        if (CommanUtils.isNetworkAvailable(this)) {
            val param = HashMap<String, String>()
            param["workout_id"] = news_module_id
            if (!program_id.isEmpty()) {
                param["program_id"] = program_id
            } else {
                param["program_id"] = ""
            }
            val header = HashMap<String, String>()
            header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
            getDataManager().workoutDetailApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                         val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        if (success.equals("1")) {
                            binding.playVideo.visibility = VISIBLE
                            binding.myProgress.visibility = GONE
                            binding.svMain.visibility = VISIBLE


                            try {
                                workerDetail = getDataManager().mGson?.fromJson(
                                    response.toString(), WorkoutDetailResponce::class.java
                                )!!


                                if (workerDetail.data.workout_detail[0].workout_type == "1") {// means set and reps exercise
                                    setAndRepsExerciseList = ArrayList()
                                    setAndRepsExerciseList.addAll(
                                        workerDetail.data.sets_reps_list!!.workoutGroupsData
                                    )
                                    setWorkoutAdapter()
                                    var count = 0
                                    workerDetail.data.sets_reps_list!!.workoutGroupsData.forEach {
                                        count += it.groupSetsData[0].exerciseTransData.size
                                    }
                                    binding.tvTotalExerciseCount.text = "$count Exercises"

                                } else {
                                    exerciseList.addAll(workerDetail.data.workout_exercise_list)
                                }
                                WDetail = workerDetail.data.workout_detail[0]

                                try {

                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(Uri.parse(WDetail.workout_image))
                                        .into(binding.ivFeatred)

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }


                                try {
                                    if (WDetail.workout_good_for != null && WDetail.workout_good_for!!.isNotEmpty()) binding.tvGoodfor.text =
                                        WDetail.workout_good_for!!.replace(" |", ",")

                                    if (WDetail.workout_equipment != null && WDetail.workout_equipment!!.isNotEmpty()) binding.tvEquipment.text =
                                        WDetail.workout_equipment!!.replace(" |", ",")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                binding.tvWorkoutName.text = "${WDetail.workout_name}"



                                if (WDetail.workout_level == "Advance") {

                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.advance_ico_client_givne).fitCenter()
                                        .into(binding.ivExerciseLevel)
                                    binding.tvWorkoutLevel.text = WDetail.workout_level
                                } else if (WDetail.workout_level == "Basic") {
                                    binding.tvWorkoutLevel.text = WDetail.workout_level
                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.beginners_ico_client_givne).fitCenter()
                                        .into(binding.ivExerciseLevel)
                                } else if (WDetail.workout_level == "Moderate") {
                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.inter_ico_client_givne).fitCenter()
                                        .into(binding.ivExerciseLevel)
                                    binding.tvWorkoutLevel.text = WDetail.workout_level
                                } else if (WDetail.workout_level == "All" || WDetail.workout_level=="ALL") {
                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.all_label_ico_other_client).fitCenter()
                                        .into(binding.ivExerciseLevel)
                                    binding.tvWorkoutLevel.text ="All Levels" //WDetail.workout_level
                                } else {
                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.advance_ico_client_givne).fitCenter()
                                        .into(binding.ivExerciseLevel)
                                    binding.tvWorkoutLevel.text = WDetail.workout_level
                                }

                                workoutAccessLvel = WDetail.workout_access_level
                                if (WDetail.workout_type == "1") {
                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.set_reps_ico_client_givne).fitCenter()
                                        .into(binding.ivWorkouType)
                                    binding.tvWorkoutType.text = "Sets & Reps"
                                } else {
                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(R.drawable.follow_along_ico_client_givne).fitCenter()
                                        .into(binding.ivWorkouType)
                                    binding.tvWorkoutType.text = "Follow Along"
                                }



                                if (WDetail.workout_name.isEmpty()) {
                                    finish()
                                    Constant.showCustomToast(
                                        this@WorkOutDetailActivity, "No Longer Available"
                                    )
                                }
                                try {
                                    val str = workerDetail.data.workout_detail[0].allowed_users
                                    val lstValues: List<String> =
                                        str.split(",").map { it -> it.trim() }
                                    lstValues.forEach { it ->
                                        Log.i("Values", "value=$it")
                                        AllowUserList.add(it)
                                        //Do Something
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }


                                for (item in AllowUserList.indices) {
                                    println("marks[$item]: " + AllowUserList[item])
                                    val userInfoBean = Doviesfitness.getDataManager().getUserInfo()
                                    isalloweduser = AllowUserList[item] == userInfoBean.customer_id
                                }
                                /**need to uncomment below code , now commenting for just checking loading issue 25 sept */
                                //workout_created_by

                                val model = workerDetail.data.created_by

                                if (workerDetail.data.workout_detail.get(0).workout_created_by == "Admin" || getDataManager().getUserStringData(
                                        AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN
                                    )!!.equals(
                                            "Yes", true
                                        )
                                ) {

                                    // if it is empty then this workout is created by Admin otherwise this workout is created by simple user

                                    binding.tvCreatorName.text = model.creator_name
                                    binding.tvWorkoutCreatorType.text = model.sub_title
                                    if (WDetail.workout_description.isNotEmpty()) {
                                        binding.llOverView.visibility=View.VISIBLE
                                        binding.tvOverview.text = WDetail.workout_description
                                    }else{
                                        binding.llOverView.visibility=View.GONE
                                    }

                                    //binding.ivExerciseLevel


                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(model.creator_profile_image).fitCenter()
                                        .into(binding.ivWorkoutCreatorImage)


                                    if (workerDetail.data.created_by.social_media_type.isNotEmpty()) {
                                        //    rv_Media_links.visibility=View.VISIBLE
                                        iv_social_menu_link.visibility = VISIBLE
                                        socialMediaLinks.addAll(workerDetail.data.created_by.social_media_type)
                                    } else {
                                        iv_social_menu_link.visibility = GONE
                                        rv_Media_links.visibility = GONE
                                    }


                                } else {

                                    iv_social_menu_link.visibility = GONE
                                    binding.tvCreatorName.text =
                                        getDataManager().getUserInfo().customer_full_name//workerDetail!!.data.workout_detail[0].creator_name
                                    binding.tvWorkoutCreatorType.text =
                                        "@" + getDataManager().getUserInfo().customer_user_name//"APP USER"

                                    Glide.with(this@WorkOutDetailActivity)
                                        .load(model.creator_profile_image).fitCenter()
                                        .into(binding.ivWorkoutCreatorImage)
                                }

                                if (exerciseList.size > 0 || setAndRepsExerciseList.size > 0) {

//                                "allowed_users": "7,336,335,352,456,17500,121,128,240,17520,17613",

                                    //set Data in Binding
                                    try {

                                        //  binding.workout = workerDetail.data.workout_detail[0]
                                        binding.workout = WDetail
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    binding.musicLayout.visibility = VISIBLE








                                    if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!! == "0" && !workerDetail.data.workout_detail[0].workout_access_level.equals(
                                            "open", true
                                        ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                                            .equals(
                                                "No", true
                                            )
                                    ) {
                                        binding.musicLayout1.visibility = GONE
                                        binding.upgradeLayout.visibility = VISIBLE
                                    } else {
                                        binding.musicLayout1.visibility = VISIBLE
                                        binding.upgradeLayout.visibility = GONE
                                    }

                                    if (workerDetail.data.workout_detail[0].creator_id == getDataManager().getUserInfo().customer_id) {
                                        isMyWorkout = "yes"
                                    } else {
                                        isMyWorkout = "no"
                                    }
                                    showDataInOverViewDiaog(WDetail)
                                    binding.tvMinutes.text = WDetail.workout_total_time
                                    /**need to check properly if below code is usable or not because only need to show time like 30 Min and blow code is removing 30 Min*//*  if (WDetail.workout_total_time.contains("min")) {
                                          binding.tvMinutes.text =
                                              WDetail.workout_total_time.replace("min", "")
                                          binding.tvMinutes.text = "Min"
                                      } else if (WDetail.workout_total_time.contains("Sec")) {
                                          binding.tvMinutes.text =
                                              WDetail.workout_total_time.replace("Sec", "")
                                          binding.tvMinutes.text = "Sec"
                                      }*/



                                    if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                                            .equals(
                                                "Yes", true
                                            )
                                    ) {
                                        if (isAllDownload()) {
                                            if (!this@WorkOutDetailActivity.isDestroyed) {
                                                Glide.with(getActivity()).load(
                                                    ContextCompat.getDrawable(
                                                        getActivity(), R.drawable.new_play_icon
                                                    )
                                                ).into(binding.playVideo)
                                                binding.loader.visibility = GONE
                                            }

                                        } else {

                                            if (!this@WorkOutDetailActivity.isDestroyed) {
                                                Glide.with(getActivity()).load(
                                                        ContextCompat.getDrawable(
                                                            getActivity(),
                                                            R.drawable.ic_download_circle_svg
                                                        )
                                                    ).into(binding.playVideo)
                                            }

                                            // binding.loader.visibility = VISIBLE
                                        }
                                    } else if (WDetail.workout_access_level == "OPEN") {

                                        if (isAllDownload()) {
                                            Glide.with(getActivity()).load(
                                                    ContextCompat.getDrawable(
                                                        getActivity(), R.drawable.new_play_icon
                                                    )
                                                ).into(binding.playVideo)
                                            binding.loader.visibility = GONE
                                        } else {
                                            Glide.with(getActivity()).load(
                                                    ContextCompat.getDrawable(
                                                        getActivity(),
                                                        R.drawable.ic_download_circle_svg
                                                    )
                                                ).into(binding.playVideo)
                                            // binding.loader.visibility = VISIBLE
                                        }
                                    } else {
                                        if (Parentisalloweduser || isalloweduser) {
                                            binding.musicLayout1.visibility = VISIBLE
                                            binding.upgradeLayout.visibility = GONE
                                            if (WDetail.workout_access_level.equals("OPEN")) {

                                                if (isAllDownload()) {
                                                    Glide.with(getActivity()).load(
                                                            ContextCompat.getDrawable(
                                                                getActivity(),
                                                                R.drawable.new_play_icon
                                                            )
                                                        ).into(binding.playVideo)
                                                    binding.loader.visibility = GONE
                                                } else {
                                                    Glide.with(getActivity()).load(
                                                            ContextCompat.getDrawable(
                                                                getActivity(),
                                                                R.drawable.ic_download_circle_svg
                                                            )
                                                        ).into(binding.playVideo)
                                                    // binding.loader.visibility = VISIBLE
                                                }

                                            }
                                        } else {
                                            if (!this@WorkOutDetailActivity.isDestroyed) {
                                                Glide.with(getActivity()).load(
                                                        ContextCompat.getDrawable(
                                                            getActivity(),
                                                            R.drawable.ic_lock_incircle_ico
                                                        )
                                                    ).into(binding.playVideo)
                                            }

                                            binding.musicLayout1.visibility = GONE
                                            binding.upgradeLayout.visibility = VISIBLE

                                        }
                                    }
                                    if (WDetail.workout_fav_status.equals("1")) {
                                        binding.ivFav.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                getActivity(), R.drawable.ic_star_active
                                            )
                                        )
                                    } else {
                                        binding.ivFav.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                getActivity(), R.drawable.ic_star
                                            )
                                        )
                                    }

                                    if (!this@WorkOutDetailActivity.isDestroyed) {
                                        Glide.with(getActivity()).load(WDetail.workout_image)
                                            .into(binding.img)
                                    }
                                    exerciseAdapter.notifyDataSetChanged()
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                            /*   if (social_media_type==""){
                                   iv_social_menu_link.visibility = View.GONE
                               }else{
                                   workerDetail = getDataManager().mGson?.fromJson(
                                       response.toString(),
                                       WorkoutDetailResponce::class.java
                                   )!!
                               }*/


                        } else {
                            //  Constant.showCustomToast(this@WorkOutDetailActivity, "" + message)
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.myProgress.visibility = GONE
                        Constant.errorHandle(anError, this@WorkOutDetailActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val viewRect = Rect()
        rv_Media_links.getGlobalVisibleRect(viewRect)
        if (!viewRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
            rv_Media_links.visibility = GONE
            transparentBlurView.visibility = GONE
            binding.tvCreated.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun showDataInOverViewDiaog(wDetail: WorkoutDetail) {
        create_name_as_dialog_heading = wDetail.creator_name
        dialog_overView_heading = wDetail.workout_description
        workOut_share_link = wDetail.workout_share_url
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.overView_layout -> {
                //CommanUtils.lastClick()
                playAndStartworkoutButtonCLick()
              /*  filter_WorkOut_and_doviesFitness_dialog(
                    create_name_as_dialog_heading, dialog_overView_heading
                )*/
            }

            R.id.sharing_layout -> {
                CommanUtils.lastClick()
                sharing_link_dialog(workOut_share_link)
            }

            R.id.iv_social_menu_link -> {
                transparentBlurView.visibility = VISIBLE
                rv_Media_links.visibility = VISIBLE

                binding.tvCreated.setTextColor(ContextCompat.getColor(this, R.color.exo_gray))
                setSocialMediaLinkAdapter(socialMediaLinks, binding.rvMediaLinks)

            }

            R.id.upgrade_layout -> {
                CommanUtils.lastClick()
                var intent =
                    Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                        .putExtra("exercise", "yes")
                startActivityForResult(intent, 2)
            }

            R.id.iv_back -> {
                CommanUtils.lastClick()
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                onBackPressed()
                Constant.requestAudioFocusClose(applicationContext)

                //  FileOutputStream("/sdcard/file_name.extension");
            }/*
                        R.id.rl_header1 -> {

                            //  FileOutputStream("/sdcard/file_name.extension");
                        }
            *//*
                        R.id.opacity_layout -> {

                            //  FileOutputStream("/sdcard/file_name.extension");
                        }
            */
            R.id.iv_more -> {
                CommanUtils.lastClick()
                if (CommanUtils.isNetworkAvailable(this)) {
                    if (isMyWorkout != null && isMyWorkout == "yes") SaveEditWorkoutDialog.newInstance(
                        "myWorkout",
                        this
                    ).show(
                        supportFragmentManager
                    )
                    else SaveEditWorkoutDialog.newInstance("workout", this).show(
                        supportFragmentManager
                    )
                } else {
                    Constant.showInternetConnectionDialog(this)
                }
            }

            R.id.play_video -> {
                playAndStartworkoutButtonCLick()
         /*       CommanUtils.lastClick()
                if (CommanUtils.isNetworkAvailable(this)) {
                    if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                            .equals(
                                "Yes", true
                            )
                    ) {
                        downloadOrPlayVideo()
                    } else if (WDetail.workout_access_level == "OPEN") {
                        downloadOrPlayVideo()
                    } else if (isMyWorkout != null && isMyWorkout == "yes") {


                        var isLock = false
                        for (i in 0 until exerciseList.size) {
                            if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                                isLock = true
                            }
                        }

                        if (isLock) {
                            var intent = Intent(
                                getActivity(), SubscriptionActivity::class.java
                            ).putExtra("home", "no").putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        } else {
                            downloadOrPlayVideo()
                        }

                    } else {
                        if (Parentisalloweduser) {
                            downloadOrPlayVideo()
                        } else {
                            var intent =
                                Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                                    "home", "no"
                                ).putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        }
                    }
                } else {
                    Constant.showInternetConnectionDialog(this)
                }
*/
            }

            R.id.iv_fav -> {
                CommanUtils.lastClick()
                try {
                    if (WDetail != null) {

                        if (WDetail.workout_fav_status.equals("1")) {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.ic_star
                                )
                            )
                            setFavUnfavWorkout("0")
                        } else {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.ic_star_active
                                )
                            )
                            setFavUnfavWorkout("1")
                        }

                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


            }

            R.id.music_player_layout -> {
                CommanUtils.lastClick()
                try {
                    val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
    fun playAndStartworkoutButtonCLick(){
        CommanUtils.lastClick()
        if (CommanUtils.isNetworkAvailable(this)) {
            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                    .equals(
                        "Yes", true
                    )
            ) {
                downloadOrPlayVideo()
            } else if (WDetail.workout_access_level == "OPEN") {
                downloadOrPlayVideo()
            } else if (isMyWorkout != null && isMyWorkout == "yes") {


                var isLock = false
                for (i in 0 until exerciseList.size) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }

                if (isLock) {
                    var intent = Intent(
                        getActivity(), SubscriptionActivity::class.java
                    ).putExtra("home", "no").putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                } else {
                    downloadOrPlayVideo()
                }

            } else {
                if (Parentisalloweduser) {
                    downloadOrPlayVideo()
                } else {
                    var intent =
                        Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                            "home", "no"
                        ).putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                }
            }
        } else {
            Constant.showInternetConnectionDialog(this)
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 12345) {
            if (myWorkoutFragment != null && !myWorkoutFragment.isEmpty() && myWorkoutFragment.equals(
                    "myWorkoutFragment"
                )
            ) setResult(1234, Intent().putExtra("myFrag", "myFrag"))

            finish()
        } else if (requestCode == 2 && data != null) {
            exerciseList.clear()
            getWorkoutDetail(moduleId)

        } else {

        }

    }

    private fun downloadOrPlayVideo() {
        if (isAllDownload()) {
            binding.rlHeader1.visibility = VISIBLE
            binding.img.visibility = VISIBLE
            binding.countDownTxt.visibility = VISIBLE

            val animZoomIn = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)
            binding.rlHeader1.startAnimation(animZoomIn)

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            countdowngetReady()
            for (i in 0 until exerciseList.size) {

                Log.v("exerciseList", "" + exerciseList[i].workout_exercise_video)

                if (exerciseList[i] != null) {
                    // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                    val lastIndex = exerciseList[i].workout_exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName = exerciseList[i].workout_exercise_video.substring(
                            lastIndex + 1
                        )
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val encryptedPath =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                        val f = File(path)

                        if (f.exists()) {
                            exerciseList.get(i).workout_offline_video = path
                            // encrypt(path,encryptedPath,downloadFileName)
                        }
                        var MaxProgress =
                            Constant.getExerciseTime(exerciseList.get(i).workout_exercise_time)
                        exerciseList[i].MaxProgress = MaxProgress
                    }
                    // }
                }

            }

        } else {

            downloadWorkout()
            binding.playVideo.isEnabled = false
        }

    }

    private fun sharing_link_dialog(workOut_share_link: String) {
        //Shareing link
        sharePost(workOut_share_link)
    }

    private fun filter_WorkOut_and_doviesFitness_dialog(
        create_name_as_dialog_heading: String, dialog_overView_heading: String
    ) {

        /*sharp corner of dialog and visible icon and set proer text in discription*/
        //Inflate the dialog with custom view
        dialog = Dialog(this, R.style.MyTheme_Transparent)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnDismissListener { binding.viewTransParancy.visibility = GONE }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_fitness_dialog_view)

        dialog.window?.setLayout(width - 30, WindowManager.LayoutParams.WRAP_CONTENT)

        val dialog_Heading = dialog.findViewById(R.id.txt_dialog_heading) as TextView
        val dialog_overViewDiscription =
            dialog.findViewById(R.id.txt_overView_discritpion) as TextView

        if (!create_name_as_dialog_heading.isEmpty()) {
            dialog_Heading.text = create_name_as_dialog_heading
            dialog_overViewDiscription.text = dialog_overView_heading
            dialog_overViewDiscription.visibility = VISIBLE
            dialog.txt_hideView.visibility = GONE
            binding.viewTransParancy.visibility = VISIBLE

        } else {
            dialog_Heading.text = "FILTER WORKOUT"
            dialog_overViewDiscription.text = ""
            dialog.txt_hideView.visibility = VISIBLE
            binding.viewTransParancy.visibility = VISIBLE
        }

        dialog.show()

        dialog.iv_cancle_dialog.setOnClickListener {
            // view transparent background of dialog
            binding.viewTransParancy.visibility = GONE
            dialog.dismiss()
            hideNavigationBar()
        }
    }

    override fun onBackPressed() {
        Foreground.removeListener(foregroundListener)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //  Constant.requestAudioFocusClose(applicationContext)
        finish()
    }

    override fun videoPlayClick(
        isScroll: Boolean,
        data: WorkoutExercise,
        position: Int,
        mHolder: WorkoutAdapter.MyViewHolder,
        isLoad: Boolean
    ) {
        for (i in 0 until exerciseList.size) {
            if (exerciseList.get(i).isPlaying) {
                exerciseList.get(i).isPlaying = false
                this.isScroll = false
                binding.svMain.setEnableScrolling(true)
            } else {
                exerciseList.get(i).isPlaying = true
                this.isScroll = true
                binding.rvWorkout.requestChildFocus(mHolder.itemView, mHolder.itemView)
                binding.svMain.setEnableScrolling(false)
            }
        }
        exerciseAdapter.notifyDataSetChanged()
        if (isLoad) {
            downloadExercise(data.workout_exercise_video)
        }
    }

    override fun shareURL(data: WorkoutExercise) {
        sharePost(data.exercise_share_url)
    }

    override fun setFavUnfav(data: WorkoutExercise, position: Int, view: ImageView) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.module_name, StringConstant.exercise)
        param.put(StringConstant.module_id, data.workout_exercises_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        if (data.workout_exercise_is_favourite.equals("0")) {
                            data.workout_exercise_is_favourite = "1"
                            view.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.ic_star_active
                                )
                            )
                        } else {
                            data.workout_exercise_is_favourite = "0"
                            view.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.ic_star
                                )
                            )
                        }
                    } else {
                        //Constant.showCustomToast(getActivity(), "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })

    }

    /**this is for add workouts in favourites*/
    fun setFavUnfavWorkout(value: String) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.module_name, StringConstant.workout)
        param.put(StringConstant.module_id, moduleId)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        WDetail.workout_fav_status = value
                        if (value.equals("1")) {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.ic_star_active
                                )
                            )
                        } else {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.ic_star
                                )
                            )
                        }
                    } else {
                        Constant.showCustomToast(getActivity(), "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })

    }

    fun isAllDownload(): Boolean {

        var isDownload: Boolean = true

        if (WDetail.workout_type == "1") {
            for (i in 0..setAndRepsExerciseList.size - 1) {
                var model = setAndRepsExerciseList[i]
                for (j in 0 until model.groupSetsData[0].exerciseTransData.size) {
                    var childModel = model.groupSetsData[0].exerciseTransData[j]
                    val lastIndex = childModel.workout_exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName =
                            childModel.workout_exercise_video.substring(lastIndex + 1)
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val f = File(path)
                        if (!f.exists()) {
                            isDownload = false
                            break
                        }
                    }
                }
            }
        } else {
            for (i in 0 until exerciseList.size) {
                val lastIndex = exerciseList[i].workout_exercise_video.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName =
                        exerciseList.get(i).workout_exercise_video.substring(lastIndex + 1)
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                    val f = File(path)
                    if (!f.exists()) {
                        isDownload = false
                        break
                    } else {

                    }
                }
            }
        }

        return isDownload
    }

    fun downloadWorkout() {
        list.clear()

        if (WDetail.workout_type == "1") {
            for (i in 0..setAndRepsExerciseList.size - 1) {
                setAndRepsExerciseList.get(i).groupSetsData[0].exerciseTransData.forEachIndexed { _, model ->
                    //  if (setAndRepsExerciseList.get(i) != null) {
                    Log.d("video url", "video url...." + model.workout_exercise_video)

                    //   if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                    val lastIndex = model.workout_exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName = model.workout_exercise_video.substring(lastIndex + 1)
                        val subpath = "/Dovies//$downloadFileName"
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val f = File(path)
                        Log.e("download file path", "file path..." + f.absolutePath)
                        if (!f.exists()) {
                            //  f.createNewFile();
                            val foregroundListener = object : Foreground.Listener {
                                override fun background() {
                                    Log.e("background", "Go to background")
                                }

                                override fun foreground() {
                                    requestAudioFocusForMyApp(getActivity())
                                    Log.e("Foreground", "Go to foreground")
                                }
                            }
                            val Download_Uri = Uri.parse(model.workout_exercise_video)
                            val request = DownloadManager.Request(Download_Uri)
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                            request.setAllowedOverRoaming(false)
                            request.setTitle("Dovies Downloading $i.mp4")
                            request.setDescription("Downloading $i.mp4")
                            request.setVisibleInDownloadsUi(false)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                            request.setDestinationInExternalFilesDir(
                                getActivity(), "/." + Environment.DIRECTORY_DOWNLOADS, subpath
                            )
                            refid = downloadManager!!.enqueue(request)
                            list.add(refid)
                        } else {
                            //  Toast.makeText(getActivity(), "already downloaded...$i", Toast.LENGTH_SHORT).show()
                        }
                    }
                    //  }
                    //  }
                }


            }
        } else {
            for (i in 0..exerciseList.size - 1) {
                if (exerciseList.get(i) != null) {
                    Log.d("video url", "video url...." + exerciseList.get(i).workout_exercise_video)

                    //   if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                    val lastIndex = exerciseList.get(i).workout_exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName =
                            exerciseList.get(i).workout_exercise_video.substring(lastIndex + 1)
                        val subpath = "/Dovies//$downloadFileName"
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val f = File(path)
                        Log.e("download file path", "file path..." + f.absolutePath)
                        if (!f.exists()) {
                            //  f.createNewFile();
                            val foregroundListener = object : Foreground.Listener {
                                override fun background() {
                                    Log.e("background", "Go to background")
                                }

                                override fun foreground() {
                                    requestAudioFocusForMyApp(getActivity())
                                    Log.e("Foreground", "Go to foreground")
                                }
                            }
                            val Download_Uri = Uri.parse(exerciseList.get(i).workout_exercise_video)
                            val request = DownloadManager.Request(Download_Uri)
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                            request.setAllowedOverRoaming(false)
                            request.setTitle("Dovies Downloading $i.mp4")
                            request.setDescription("Downloading $i.mp4")
                            request.setVisibleInDownloadsUi(false)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                            request.setDestinationInExternalFilesDir(
                                getActivity(), "/." + Environment.DIRECTORY_DOWNLOADS, subpath
                            )
                            refid = downloadManager!!.enqueue(request)
                            list.add(refid)
                        } else {
                            //  Toast.makeText(getActivity(), "already downloaded...$i", Toast.LENGTH_SHORT).show()
                        }
                    }
                    //  }
                }

            }
        }

        if (!list.isEmpty() && list.size > 0) {
            binding.loader.visibility = VISIBLE
            binding.downloadingTxt.visibility = VISIBLE
            startDownloadThread()
        } else {
            binding.playVideo.isEnabled = true
        }

    }

    fun downloadExercise(videoUrl: String) {
        list.clear()
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            val subpath = "/Dovies//$downloadFileName"
            val path =
                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName + ".mp4"
            f = File(path)
            val Download_Uri = Uri.parse(videoUrl)
            val request = DownloadManager.Request(Download_Uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("Dovies Downloading .mp4")
            request.setDescription("Downloading .mp4")
            request.setVisibleInDownloadsUi(false)
            // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalFilesDir(
                getActivity(), "/." + Environment.DIRECTORY_DOWNLOADS, subpath
            )
            refid = downloadManager!!.enqueue(request)
            list.add(refid)
            startDownloadThread()
        }
    }

    private fun startDownloadThread() {
        // Initializing the broadcast receiver ...
        mBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                isRecieverRegistered = true

                if (WDetail.workout_type == "1") {
                    setAndRepsExerciseList.forEach { RoundModel ->
                        for (i in 0 until RoundModel.groupSetsData[0].exerciseTransData.size) {
                            // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                            val lastIndex =
                                RoundModel.groupSetsData[0].exerciseTransData[i].workout_exercise_video.lastIndexOf(
                                    "/"
                                )
                            if (lastIndex > -1) {
                                val downloadFileName =
                                    RoundModel.groupSetsData[0].exerciseTransData[i].workout_exercise_video.substring(
                                        lastIndex + 1
                                    )
                                val path =
                                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                                val encryptedPath =
                                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                                val f = File(path)
                                if (f.exists()) {
                                    RoundModel.groupSetsData[0].exerciseTransData[i].workout_offline_video =
                                        path
                                    // encrypt(path,encryptedPath,downloadFileName)
                                }
                            }
                            //  }

                        }

                    }


                } else {
                    for (i in 0 until exerciseList.size) {
                        // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                        val lastIndex = exerciseList.get(i).workout_exercise_video.lastIndexOf("/")
                        if (lastIndex > -1) {
                            val downloadFileName =
                                exerciseList.get(i).workout_exercise_video.substring(lastIndex + 1)
                            val path =
                                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                            val encryptedPath =
                                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                            val f = File(path)
                            if (f.exists()) {
                                exerciseList.get(i).workout_offline_video = path
                                // encrypt(path,encryptedPath,downloadFileName)
                            }
                        }
                        //  }

                    }
                }


                mFinishedFilesFromNotif.add(intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
                var referenceId = intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Log.e("IN", "" + referenceId)
                list.remove(referenceId)
                if (list.isEmpty()) {
                    binding.playVideo.isEnabled = true
                    if (isAllDownload()) {
                        Glide.with(getActivity()).load(
                                ContextCompat.getDrawable(
                                    getActivity(), R.drawable.new_play_icon
                                )
                            ).into(binding.playVideo)
                        binding.loader.visibility = GONE
                        binding.downloadingTxt.visibility = GONE

                    } else {
                        Glide.with(getActivity()).load(
                            ContextCompat.getDrawable(
                                getActivity(), R.drawable.ic_download_circle_svg
                            )
                        ).into(binding.playVideo)
                        //  binding.loader.visibility = VISIBLE
                    }

                }

            }
        }
        val intentFilter = IntentFilter("android.intent.action.DOWNLOAD_COMPLETE")
        registerReceiver(mBroadCastReceiver, intentFilter)


        // initializing the download manager instance ....
        // downloadManager = (DownloadManager).getSystemService(Context.DOWNLOAD_SERVICE);
        // starting the thread to track the progress of the download ..
        var isShown = true
        mProgressThread = Thread(Runnable {
            // Preparing the query for the download manager ...
            val q = DownloadManager.Query()
            val ids = LongArray(list.size)
            val idsArrList = java.util.ArrayList<Long>()
            var i = 0
            for (id in list) {
                ids[i++] = id
                idsArrList.add(id)
            }
            q.setFilterById(*ids)
            // getting the total size of the data ...
            var c: Cursor?

            while (true) {
                // check if the downloads are already completed ...
                // Here I have created a set of download ids from download manager to keep
                // track of all the files that are dowloaded, which I populate by creating
                //
                if (mFinishedFilesFromNotif.containsAll(idsArrList)) {
                    isDownloadSuccess = true
                    // TODO - Take appropriate action. Download is finished successfully
                    return@Runnable
                }
                // start iterating and noting progress ..
                c = downloadManager!!.query(q)

                if (c != null) {
                    var filesDownloaded = 0
                    var fileFracs = 0f // this stores the fraction of all the files in
                    // download
                    val columnTotalSize = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    val columnStatus = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    //final int columnId = c.getColumnIndex(DownloadManager.COLUMN_ID);
                    val columnDwnldSoFar =
                        c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)

                    while (c.moveToNext()) {
                        // checking the progress ..
                        if (c.getInt(columnStatus) == DownloadManager.STATUS_SUCCESSFUL) {
                            filesDownloaded++
                        } else if (c.getInt(columnTotalSize) > 0) {
                            fileFracs += c.getInt(columnDwnldSoFar) * 1.0f / c.getInt(
                                columnTotalSize
                            )
                        } else if (c.getInt(columnStatus) == DownloadManager.STATUS_FAILED) {
                            // TODO - Take appropriate action. Error in downloading one of the
                            // files.
                            return@Runnable
                        }// If the file is partially downloaded, take its fraction ..
                    }
                    c.close()
                    // calculate the progress to show ...
                    val progress = (filesDownloaded + fileFracs) / ids.size

                    // setting the progress text and bar...
                    val percentage = Math.round(progress * 100.0f)
                    val txt = "Loading ... $percentage%"
                    Log.d("progress...", "progress...$txt")
                    binding.loader.progress = percentage

                    if (percentage == 100 && isShown) {
                        showPlay(percentage)
                        isShown = false
                    }
                }
            }
        })
        mProgressThread.start()
    }

    fun showPlay(percentage: Int) {
        runOnUiThread {
            fun run() {
                if (percentage == 100) {
                    binding.playVideo.isEnabled = true
                    Glide.with(getActivity()).load(
                            ContextCompat.getDrawable(
                                getActivity(), R.drawable.new_play_icon
                            )
                        ).into(binding.playVideo)
                    Log.d("showPlay", "showPlay....$percentage")
                    binding.loader.visibility = GONE
                    binding.downloadingTxt.visibility = GONE

                }
            }
        }

    }

    private fun countdowngetReady() {
        binding.countDownTxt.setTextSize(
            TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen._25sdp)
        )
        binding.countDownTxt.text = "GET READY IN"
        var Timer: Int = 5
        countdownRunnable = object : Runnable {
            override fun run() {
                runOnUiThread {
                    Timer -= 1
                    if (Timer == 4) {


                    } else if (Timer > 0) {
                        binding.countDownTxt.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen._55sdp)
                        )
                        binding.countDownTxt.text = "" + Timer
                    } else if (Timer == 0) {
                        binding.countDownTxt.text = "GO"
                    } else if (Timer == -1) {

                        Constant.releaseAudioFocusForMyApp(getActivity())
                        Foreground.removeListener(foregroundListener)
                        //  val intent = Intent(getActivity(), WorkoutVideoPlayActivity::class.java)






                        if (WDetail.workout_type == "1") {
                            exerciseList.clear()
                            setAndRepsExerciseList.forEach { model ->
                                //  model.groupSetsData[0].exerciseTransData.forEach {
                                exerciseList.addAll(getExerciseList2(model.groupSetsData[0].exerciseTransData))
                                // }
                            }
                            var listOfExercises = ArrayList<SetAndRepsModel>()

                            var workoutType = ""
                            if (WDetail.workout_type == "1") {
                                workoutType = "Set & Reps"
                                setAndRepsExerciseList.forEach { setAndRepModel ->
                                    var roundName = ""
                                    if (setAndRepModel.iGroupType == "2") roundName = "Left & Right"
                                    else if (setAndRepModel.iGroupType == "1") roundName = "Superset"
                                    else roundName = ""
                                    listOfExercises.add(
                                        SetAndRepsModel(
                                            strExerciseType = "",
                                            strRoundCounts = "",
                                            strRoundName = "${roundName}",
                                            strTargetSets = "${setAndRepModel.iTargetSets}",
                                            strTargetReps = "${setAndRepModel.iTargetReps}",
                                            strSetsCounts = "",
                                            arrSets = getSetModel(setAndRepModel),
                                            isRoundPositionPopupOIsVisible = false,
                                            noteForExerciseInRound = "",
                                            isPostnotifiedExerciseAdapter = false
                                        )
                                    )
                                }
                            } else {


                                workoutType = "Follow Along"
                            }
                            val intent = Intent(this@WorkOutDetailActivity, NewPlayerView::class.java)
                            intent.putExtra("WDetail", WDetail)
                            intent.putExtra("complete_workoutDetail", workerDetail)
                            intent.putExtra("workoutExerciseList", exerciseList)
                            intent.putExtra("workoutExerciseList", exerciseList)
                            intent.putExtra("setAndRepsExerciseList", listOfExercises)
                            intent.putExtra("isMyWorkout", isMyWorkout)
                            intent.putExtra("workoutType", workoutType)
                            startActivity(intent)
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        }else{
                            exerciseList.clear()
                            exerciseList.addAll(workerDetail.data.workout_exercise_list)
                            val intent = Intent(getActivity(), WorkoutVideoPlayActivityNew::class.java)
                            intent.putExtra("exerciseList", exerciseList)
                            intent.putExtra("WDetail", WDetail)
                            intent.putExtra("from_ProgramPlan", program_plan_id)
                            intent.putExtra("from_which_frament", from_which_frament)
                            startActivity(intent)
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }









                    }
                    else {
                        binding.rlHeader1.visibility = GONE
                        binding.img.visibility = GONE
                        binding.countDownTxt.visibility = GONE
                        binding.opacityLayout.visibility = GONE
                        binding.countDownTxt.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen._25sdp)
                        )
                        binding.countDownTxt.text = "GET READY IN"
                    }
                }
                if (Timer > -2) {
                    countdownhandler.postDelayed(this, 1100)
                } else {
                    binding.rlHeader1.visibility = GONE
                    binding.img.visibility = GONE
                    binding.countDownTxt.visibility = GONE
                    binding.opacityLayout.visibility = GONE
                    binding.countDownTxt.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen._25sdp)
                    )
                    binding.countDownTxt.text = "GET READY IN"
                }
            }

        }
        countdownhandler.postDelayed(countdownRunnable as Runnable, 2000)
    }

    override fun onPause() {
        super.onPause()
        countdownRunnable?.let { countdownhandler.removeCallbacks(it) }
    }

    fun showDeleteDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)

        if (!whichScreen.isEmpty()) {
            dialog.tv_header.text = "Are you sure you want to delete this Workout?"
        } else {
            dialog.tv_header.text = "Are you sure you want to delete this Workout Log?"
        }
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteSavePost()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteSavePost() {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.workout_id, moduleId)

        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteWorkout(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            //first we call finish Activity but for manage loader issue we call here startactivity result
                            //finish()
                            val intent = Intent()
                            intent.putExtra("MESSAGE", "1")
                            setResult(20, intent)
                            finish()//finishing activity

                        } else {
                            finish()
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownRunnable?.let { countdownhandler.removeCallbacks(it) }
        if (isRecieverRegistered == true) unregisterReceiver(mBroadCastReceiver)
    }

    val foregroundListener = object : Foreground.Listener {
        override fun background() {
            Log.e("background", "Go to background")
        }

        override fun foreground() {
            requestAudioFocusForMyApp(getActivity())
            Log.e("Foreground", "Go to foreground")
        }
    }

    override fun overwriteClick(type: String) {
        if (type.equals(
                "workout", true
            ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                .equals(
                    "No", true
                ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                .equals(
                    "0"
                )
        ) {

            var isLock = false
            for (i in 0..exerciseList.size - 1) {
                if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                    isLock = true
                }
            }

            if (isLock) {
                var intent =
                    Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                        .putExtra("exercise", "yes")
                startActivityForResult(intent, 2)
            } else {
                AddToMyWorkout()
            }

        } else if (type.equals("delete", true)) {
            showDeleteDialog()
        } else if (type.equals("Save to my workout", true)) {

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                    .equals(
                        "No", true
                    ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                    .equals(
                        "0"
                    )
            ) {
                var isLock = false
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }

                if (isLock) {
                    var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                        "home", "no"
                    ).putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                } else {
                    AddToMyWorkout()
                }
                //  }
            } else {
                AddToMyWorkout()
                //call Save to my workout api
            }
        } else {
        }
    }

    private fun AddToMyWorkout() {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.workout_id, moduleId)

        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().addToMyWorkout(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            startActivity(Intent(getActivity(), ActivityMyWorkoutList::class.java))
                        } else {
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })
    }
}

/*

class WorkOutDetailActivity : BaseActivity(), View.OnClickListener,
    WorkoutAdapter.WorkOutItemClickListener,
    SaveEditWorkoutDialog.CommentCallBack, IsSubscribed {
    override fun isSubscribed() {
        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }

    private var program_id: String = ""
    private var from_which_frament: String = ""
    private var whichScreen: String = ""
    private var program_plan_id: String? = ""
    private var create_name_as_dialog_heading: String = ""
    private var dialog_overView_heading: String = ""
    private var workOut_share_link: String = ""
    private lateinit var layoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private lateinit var exerciseAdapter: WorkoutAdapter
    lateinit var binding: ActivityWorkOutDetailBinding
    lateinit var exerciseList: ArrayList<WorkoutExercise>
    var isScroll: Boolean = false
    var isdownload: Boolean = true
    var moduleId: String = ""
    var isMyWorkout = ""
    var myWorkoutFragment = ""
    var workoutAccessLvel = ""
    var fromDeepLinking = ""
    private var refid: Long = 0
    internal var list = ArrayList<Long>()
    lateinit var WDetail: WorkoutDetail
    private var downloadManager: DownloadManager? = null
    internal lateinit var mBroadCastReceiver: BroadcastReceiver
    var isRecieverRegistered = false
    internal var mFinishedFilesFromNotif = ArrayList<Long>()
    lateinit var mProgressThread: Thread
    var isDownloadSuccess = false
    lateinit var f: File
    private var countdownRunnable: Runnable? = null
    private val countdownhandler = Handler()
    lateinit var dialog: Dialog
    private var width: Int = 0
    private var fromHomeTab: String = ""
    lateinit var workerDetail: WorkoutDetailResponce
    var AllowUserList = ArrayList<String>()

    public companion object{
        var Parentisalloweduser:Boolean=false
        var isalloweduser:Boolean=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        hideNavigationBar()
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_out_detail)
        if (intent.hasExtra("From_WORKOUTPLAN")) {
            whichScreen = intent.getStringExtra("From_WORKOUTPLAN").toString()
        }
        moduleId = intent.getStringExtra("PROGRAM_DETAIL").toString()
        isMyWorkout = intent.getStringExtra("isMyWorkout").toString()
        fromDeepLinking = intent.getStringExtra("fromDeepLinking").toString()
        if (intent.hasExtra("from_ProgramPlan")) {
            if (intent.getStringExtra("from_ProgramPlan") != null) {
                program_plan_id = intent.getStringExtra("from_ProgramPlan")
            }
        }

        //when comke from workoutplanFromProfile
        if (intent.hasExtra("program_id")) {
            if (intent.getStringExtra("program_id") != null) {
                program_id = intent.getStringExtra("program_id")!!
            }
        }
        if (intent.hasExtra("myWorkoutFragment")) {
            if (intent.getStringExtra("myWorkoutFragment") != null) {
                myWorkoutFragment = intent.getStringExtra("myWorkoutFragment")!!
            }
        }

        if (intent.hasExtra("from_which_frament")) {
            if (intent.getStringExtra("from_which_frament") != null) {
                from_which_frament = intent.getStringExtra("from_which_frament")!!
            }
        }

        if (intent.hasExtra("fromHomeScreen")) {
            if (intent.getStringExtra("fromHomeScreen") != null) {
                fromHomeTab = intent.getStringExtra("fromHomeScreen")!!
            }
        }
//        if (intent.hasExtra("isalloweduser")) {
//
//            isalloweduser = intent.getBooleanExtra("isalloweduser",false)
//
//        }
        Parentisalloweduser=ShowDietPlanDetailActivity.isalloweduser
        Log.e("Parentisalloweduser",Parentisalloweduser.toString())
        binding.ivBack.setOnClickListener(this)
        binding.ivMore.setOnClickListener(this)
        binding.ivFav.setOnClickListener(this)
        binding.playVideo.setOnClickListener(this)
        binding.musicPlayerLayout.setOnClickListener(this)
        binding.overViewLayout.setOnClickListener(this)
        binding.sharingLayout.setOnClickListener(this)
        binding.sharingLayout.setOnClickListener(this)
        binding.upgradeLayout.setOnClickListener(this)
        //  binding.rlHeader1.setOnClickListener(this)
        //   binding.opacityLayout.setOnClickListener(this)

        Foreground.init(application)
        Foreground.addListener(foregroundListener)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        getWorkoutDetail(moduleId)

        val displaymetrics = DisplayMetrics()
        getActivity().windowManager?.defaultDisplay?.getMetrics(displaymetrics)
        width = displaymetrics.widthPixels
        initView()
    }

    private fun initView() {
        exerciseList = ArrayList()
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        //  val width = size.x
        //  val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320

        val videowidth = 120 + (160 * screenWidth)

        *//*Set Adapter*//*

        exerciseAdapter = WorkoutAdapter(this, this.exerciseList, this, videowidth, this)
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvWorkout.layoutManager = layoutManager
        binding.rvWorkout.adapter = exerciseAdapter

        binding.svMain.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                view: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY > 900) {
                    binding.musicLayout.visibility = View.GONE
                } else {
                    binding.musicLayout.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        countdownRunnable?.let { countdownhandler.postDelayed(it, 0) }
    }

    override fun textOnClick1(type: String) {

        if (type.equals(
                "edit",
                true
            ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                .equals(
                    "No", true
                ) &&
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                .equals(
                    "0"
                )
        ) {
            if (isMyWorkout.equals("yes")) {
                var isLock = false
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }

                workerDetail.data.workout_exercise_list.get(0).exercise_access_level
                if (isLock) {
                    var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                        "home",
                        "no"
                    )
                        .putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                } else {
                    val intent = Intent(getActivity(), CreateWorkoutActivity::class.java)
                    intent.putExtra("workoutDetail", WDetail)
                        .putExtra("workoutExerciseList", exerciseList).putExtra("edit", "edit")
                        .putExtra("isMyWorkout", isMyWorkout)
                        .putExtra("fromDeepLinking", fromDeepLinking)
                    startActivityForResult(intent, 12345)
                }

            } else {

                var isLock = false
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }

                workerDetail.data.workout_exercise_list.get(0).exercise_access_level
                if (isLock) {
                    var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                        "home",
                        "no"
                    )
                        .putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)

                } else {
                    val intent = Intent(getActivity(), CreateWorkoutActivity::class.java)
                    intent.putExtra("workoutDetail", WDetail)
                        .putExtra("workoutExerciseList", exerciseList).putExtra("edit", "edit")
                        .putExtra("isMyWorkout", isMyWorkout)
                        .putExtra("fromDeepLinking", fromDeepLinking)
                    startActivityForResult(intent, 12345)
                }

            }
        } else {

            val intent = Intent(getActivity(), CreateWorkoutActivity::class.java)
            intent.putExtra("workoutDetail", WDetail)
            intent.putExtra("workoutExerciseList", exerciseList).putExtra("edit", "edit")
            intent.putExtra("isMyWorkout", isMyWorkout)
            intent.putExtra("fromDeepLinking", fromDeepLinking)
            startActivityForResult(intent, 12345)

*//*
            startActivity(
                Intent(getActivity(), CreateWorkoutActivity::class.java).putExtra("workoutDetail", WDetail)
                    .putExtra("workoutExerciseList", exerciseList).putExtra("edit", "edit")
                    .putExtra("isMyWorkout", isMyWorkout)
                    .putExtra("fromDeepLinking", fromDeepLinking)


            )
*//*

        }
    }

    private fun getWorkoutDetail(news_module_id: String) {

        binding.myProgress.visibility = View.VISIBLE


        if (CommanUtils.isNetworkAvailable(this)!!) {
            val param = HashMap<String, String>()
            param.put("workout_id", news_module_id)

            if (!program_id.isEmpty()) {
                param.put("program_id", program_id)
            } else {
                param.put("program_id", "")
            }
            val header = HashMap<String, String>()
            header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
            getDataManager().workoutDetailApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("Workout response", "Workout response..." + response!!.toString(4))
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)

                        try {
                            if (success.equals("1")) {
                                binding.playVideo.visibility = View.VISIBLE
                                binding.myProgress.visibility = View.GONE
                                binding.svMain.visibility = View.VISIBLE
                                workerDetail = getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    WorkoutDetailResponce::class.java
                                )!!
                                exerciseList.addAll(workerDetail!!.data.workout_exercise_list)

                                WDetail = workerDetail.data.workout_detail.get(0)
                                workoutAccessLvel = WDetail.workout_access_level
                                if (WDetail.workout_name.isEmpty()) {
                                    finish()
                                    Constant.showCustomToast(
                                        this@WorkOutDetailActivity,
                                        "No Longer Available"
                                    )
                                }
                                val str = workerDetail.data.workout_detail.get(0).allowed_users
//
                                val lstValues: List<String> = str.split(",").map { it -> it.trim() }
                                lstValues.forEach { it ->
                                    Log.i("Values", "value=$it")
                                    AllowUserList.add(it)
                                    //Do Something
                                }

                                for (item in AllowUserList.indices) {
                                    println("marks[$item]: " + AllowUserList[item])
                                    val userInfoBean = Doviesfitness.getDataManager().getUserInfo()

                                    if (AllowUserList[item].equals(userInfoBean.customer_id)) {
                                        isalloweduser=true
                                    }else{
                                        isalloweduser=false
                                    }
                                }
                                if (exerciseList.size > 0) {

                                    Log.d(
                                        "Workout response",
                                        "Workout response exercise level..." + exerciseList.get(0).exercise_access_level.toString()
                                    )

//                                "allowed_users": "7,336,335,352,456,17500,121,128,240,17520,17613",
                                    binding.workout = workerDetail.data.workout_detail.get(0)

                                    //set Data in Binding
                                    binding.workout = WDetail
                                    binding.musicLayout.visibility = View.VISIBLE

                                    if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                                            .equals(
                                                "0"
                                            )
                                        && !workerDetail.data.workout_detail.get(0).workout_access_level.equals(
                                            "open",
                                            true
                                        )
                                        && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                                            .equals(
                                                "No",
                                                true
                                            )
                                    ) {
                                        binding.musicLayout1.visibility = View.GONE
                                        binding.upgradeLayout.visibility = View.VISIBLE
                                    } else {
                                        binding.musicLayout1.visibility = View.VISIBLE
                                        binding.upgradeLayout.visibility = View.GONE
                                    }

                                    if (workerDetail.data.workout_detail.get(0).creator_id.equals(
                                            getDataManager().getUserInfo().customer_id
                                        )
                                    ) {
                                        isMyWorkout = "yes"
                                    } else {
                                        isMyWorkout = "no"
                                    }
                                    showDataInOverViewDiaog(WDetail);

                                    if (WDetail.workout_total_time.contains("min")) {
                                        binding.tvMinutes.text =
                                            WDetail.workout_total_time.replace("min", "")
                                        binding.minutes.text = "Min"
                                    } else if (WDetail.workout_total_time.contains("sec")) {
                                        binding.tvMinutes.text =
                                            WDetail.workout_total_time.replace("sec", "")
                                        binding.minutes.text = "Sec"
                                    } else {

                                    }

                                    try {
                                        Picasso.with(getActivity()).load(WDetail.workout_image)
                                            .into(binding.ivFeatred)

                                        if (CommanUtils.isValidContextForGlide(baseContext)) {
                                            Glide.with(binding.ivProfileImg.context)
                                                .load(WDetail.creator_profile_image)
                                                .into(binding.ivProfileImg)
                                        }
                                    } catch (e: Exception) {

                                    }


                                    if (getDataManager().getUserStringData(com.doviesfitness.data.local.AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                                            .equals(
                                                "Yes",
                                                true
                                            )
                                    ) {
                                        if (isAllDownload()) {
                                            if (!this@WorkOutDetailActivity.isDestroyed) {
                                                Glide.with(getActivity()).load(
                                                    ContextCompat.getDrawable(
                                                        getActivity(),
                                                        R.drawable.new_play_icon
                                                    )
                                                ).into(binding.playVideo)
                                                binding.loader.visibility = GONE
                                            }

                                        } else {

                                            if (!this@WorkOutDetailActivity.isDestroyed) {
                                                Glide.with(getActivity())
                                                    .load(
                                                        ContextCompat.getDrawable(
                                                            getActivity(),
                                                            R.drawable.ic_download_circle_svg
                                                        )
                                                    )
                                                    .into(binding.playVideo)
                                            }

                                            // binding.loader.visibility = VISIBLE
                                        }
                                    } else if (WDetail.workout_access_level.equals("OPEN")) {

                                        if (isAllDownload()) {
                                            Glide.with(getActivity())
                                                .load(
                                                    ContextCompat.getDrawable(
                                                        getActivity(),
                                                        R.drawable.new_play_icon
                                                    )
                                                )
                                                .into(binding.playVideo)
                                            binding.loader.visibility = GONE
                                        } else {
                                            Glide.with(getActivity())
                                                .load(
                                                    ContextCompat.getDrawable(
                                                        getActivity(),
                                                        R.drawable.ic_download_circle_svg
                                                    )
                                                )
                                                .into(binding.playVideo)
                                            // binding.loader.visibility = VISIBLE
                                        }
                                    } else {
                                        if(Parentisalloweduser==true || isalloweduser==true){
                                            binding.musicLayout1.visibility = View.VISIBLE
                                            binding.upgradeLayout.visibility = View.GONE
                                            if (WDetail.workout_access_level.equals("OPEN")) {

                                                if (isAllDownload()) {
                                                    Glide.with(getActivity())
                                                        .load(
                                                            ContextCompat.getDrawable(
                                                                getActivity(),
                                                                R.drawable.new_play_icon
                                                            )
                                                        )
                                                        .into(binding.playVideo)
                                                    binding.loader.visibility = GONE
                                                } else {
                                                    Glide.with(getActivity())
                                                        .load(
                                                            ContextCompat.getDrawable(
                                                                getActivity(),
                                                                R.drawable.ic_download_circle_svg
                                                            )
                                                        )
                                                        .into(binding.playVideo)
                                                    // binding.loader.visibility = VISIBLE
                                                }

                                            }
                                        }else {
                                            if (!this@WorkOutDetailActivity.isDestroyed) {
                                                Glide.with(getActivity())
                                                    .load(
                                                        ContextCompat.getDrawable(
                                                            getActivity(),
                                                            R.drawable.ic_lock_incircle_ico
                                                        )
                                                    )
                                                    .into(binding.playVideo)
                                            }

                                            binding.musicLayout1.visibility = View.GONE
                                            binding.upgradeLayout.visibility = View.VISIBLE

                                        }
                                    }
                                    if (WDetail.workout_fav_status.equals("1")) {
                                        binding.ivFav.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                getActivity(),
                                                R.drawable.ic_star_active
                                            )
                                        )
                                    } else {
                                        binding.ivFav.setImageDrawable(
                                            ContextCompat.getDrawable(
                                                getActivity(),
                                                R.drawable.ic_star
                                            )
                                        )
                                    }

                                    if (!this@WorkOutDetailActivity.isDestroyed) {
                                        Glide.with(getActivity()).load(WDetail.workout_image)
                                            .into(binding.img)
                                    }
                                    exerciseAdapter.notifyDataSetChanged()
                                }
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }

                    override fun onError(anError: ANError) {
                        binding.myProgress.visibility = View.GONE
                        Constant.errorHandle(anError, this@WorkOutDetailActivity)
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
        }
    }

    private fun showDataInOverViewDiaog(wDetail: WorkoutDetail) {
        create_name_as_dialog_heading = wDetail.creator_name
        dialog_overView_heading = wDetail.workout_description
        workOut_share_link = wDetail.workout_share_url
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.overView_layout -> {
                CommanUtils.lastClick()
                filter_WorkOut_and_doviesFitness_dialog(
                    create_name_as_dialog_heading,
                    dialog_overView_heading
                )
            }

            R.id.sharing_layout -> {
                CommanUtils.lastClick()
                sharing_link_dialog(workOut_share_link)
            }

            R.id.upgrade_layout -> {
                CommanUtils.lastClick()
                var intent =
                    Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                        .putExtra("exercise", "yes")
                startActivityForResult(intent, 2)
            }
            R.id.iv_back -> {
                CommanUtils.lastClick()
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                onBackPressed()
                Constant.requestAudioFocusClose(applicationContext)

                //  FileOutputStream("/sdcard/file_name.extension");
            }
*//*
            R.id.rl_header1 -> {

                //  FileOutputStream("/sdcard/file_name.extension");
            }
*//*

*//*
            R.id.opacity_layout -> {

                //  FileOutputStream("/sdcard/file_name.extension");
            }
*//*

            R.id.iv_more -> {
                CommanUtils.lastClick()
                if (CommanUtils.isNetworkAvailable(this)!!) {
                    if (isMyWorkout != null && isMyWorkout.equals("yes"))
                        SaveEditWorkoutDialog.newInstance("myWorkout", this).show(
                            supportFragmentManager
                        )
                    else
                        SaveEditWorkoutDialog.newInstance("workout", this).show(
                            supportFragmentManager
                        )
                } else {
                    Constant.showInternetConnectionDialog(this)
                }
            }
            R.id.play_video -> {
                CommanUtils.lastClick()
                if (CommanUtils.isNetworkAvailable(this)!!) {
                    if (getDataManager().getUserStringData(com.doviesfitness.data.local.AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                            .equals(
                                "Yes",
                                true
                            )
                    ) {
                        downloadOrPlayVideo()
                    } else if (WDetail.workout_access_level.equals("OPEN")) {
                        downloadOrPlayVideo()
                    } else if (isMyWorkout != null && isMyWorkout.equals("yes")) {


                        var isLock = false
                        for (i in 0..exerciseList.size - 1) {
                            if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                                isLock = true
                            }
                        }

                        if (isLock) {
                            var intent = Intent(
                                getActivity(),
                                SubscriptionActivity::class.java
                            ).putExtra("home", "no")
                                .putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        } else {
                            downloadOrPlayVideo()
                        }

                    } else {
                        if(Parentisalloweduser==true)
                        {
                            downloadOrPlayVideo()
                        }else {
                            var intent =
                                Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                                    "home",
                                    "no"
                                )
                                    .putExtra("exercise", "yes")
                            startActivityForResult(intent, 2)
                        }
                    }
                } else {
                    Constant.showInternetConnectionDialog(this)
                }

            }
            R.id.iv_fav -> {
                CommanUtils.lastClick()
                try {
                    if (WDetail != null) {

                        if (WDetail.workout_fav_status.equals("1")) {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star
                                )
                            )
                            setFavUnfavWorkout("0")
                        } else {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star_active
                                )
                            )
                            setFavUnfavWorkout("1")
                        }

                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


            }
            R.id.music_player_layout -> {
                CommanUtils.lastClick()
                try {
                    val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 12345) {
            if (myWorkoutFragment != null && !myWorkoutFragment.isEmpty() && myWorkoutFragment.equals("myWorkoutFragment"))
                setResult(1234, Intent().putExtra("myFrag", "myFrag"))

            finish()
        } else if (requestCode == 2 && data != null) {
            exerciseList.clear()
            getWorkoutDetail(moduleId)

        } else {

        }

    }

    private fun downloadOrPlayVideo() {
        if (isAllDownload()) {
            binding.rlHeader1.visibility = VISIBLE
            binding.img.visibility = VISIBLE
            binding.countDownTxt.visibility = VISIBLE

            val animZoomIn = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)
            binding.rlHeader1.startAnimation(animZoomIn)

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            countdowngetReady()
            for (i in 0..exerciseList.size - 1) {

                Log.v("exerciseList", "" + exerciseList.get(i).workout_exercise_video)

                if (exerciseList.get(i) != null) {
                    // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                    val lastIndex =
                        exerciseList.get(i).workout_exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName =
                            exerciseList.get(i).workout_exercise_video.substring(
                                lastIndex + 1
                            )
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val encryptedPath =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                        val f = File(path)

                        if (f.exists()) {
                            exerciseList.get(i).workout_offline_video = path
                            // encrypt(path,encryptedPath,downloadFileName)
                        }
                        var MaxProgress =
                            Constant.getExerciseTime(exerciseList.get(i).workout_exercise_time)
                        exerciseList.get(i).MaxProgress = MaxProgress
                    }
                    // }
                }

            }

        } else {

            downloadWorkout()
            binding.playVideo.isEnabled = false
        }

    }


    private fun sharing_link_dialog(workOut_share_link: String) {
        //Shareing link
        sharePost(workOut_share_link)
    }

    private fun filter_WorkOut_and_doviesFitness_dialog(
        create_name_as_dialog_heading: String,
        dialog_overView_heading: String
    ) {

        *//*sharp corner of dialog and visible icon and set proer text in discription*//*

        //Inflate the dialog with custom view
        dialog = Dialog(this, R.style.MyTheme_Transparent)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(dialog: DialogInterface?) {
                binding.viewTransParancy.visibility = View.GONE
            }

        })
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_fitness_dialog_view);

        dialog.window?.setLayout(width - 30, WindowManager.LayoutParams.WRAP_CONTENT)

        val dialog_Heading = dialog.findViewById(R.id.txt_dialog_heading) as TextView
        val dialog_overViewDiscription =
            dialog.findViewById(R.id.txt_overView_discritpion) as TextView

        if (!create_name_as_dialog_heading.isEmpty()) {
            dialog_Heading.text = create_name_as_dialog_heading
            dialog_overViewDiscription.text = dialog_overView_heading
            dialog_overViewDiscription.visibility = VISIBLE
            dialog.txt_hideView.visibility = View.GONE
            binding.viewTransParancy.visibility = View.VISIBLE

        } else {
            dialog_Heading.text = "FILTER WORKOUT"
            dialog_overViewDiscription.text = ""
            dialog.txt_hideView.visibility = View.VISIBLE
            binding.viewTransParancy.visibility = View.VISIBLE
        }

        dialog.show();

        dialog.iv_cancle_dialog.setOnClickListener {
            // view transparent background of dialog
            binding.viewTransParancy.visibility = View.GONE
            dialog.dismiss()
            hideNavigationBar()
        }
    }

    override fun onBackPressed() {
        Foreground.removeListener(foregroundListener)
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //  Constant.requestAudioFocusClose(applicationContext)
        finish()
    }

    override fun videoPlayClick(
        isScroll: Boolean,
        data: WorkoutExercise,
        position: Int,
        mHolder: WorkoutAdapter.MyViewHolder,
        isLoad: Boolean
    ) {
        for (i in 0 until exerciseList.size) {
            if (exerciseList.get(i).isPlaying) {
                exerciseList.get(i).isPlaying = false
                this.isScroll = false
                binding.svMain.setEnableScrolling(true)
            } else {
                exerciseList.get(i).isPlaying = true
                this.isScroll = true
                binding.rvWorkout.requestChildFocus(mHolder.itemView, mHolder.itemView)
                binding.svMain.setEnableScrolling(false)
            }
        }
        exerciseAdapter.notifyDataSetChanged()
        if (isLoad) {
            downloadExercise(data.workout_exercise_video)
        }
    }

    override fun shareURL(data: WorkoutExercise) {
        sharePost(data.exercise_share_url)
    }

    override fun setFavUnfav(data: WorkoutExercise, position: Int, view: ImageView) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.module_name, StringConstant.exercise)
        param.put(StringConstant.module_id, data.workout_exercises_id)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        if (data.workout_exercise_is_favourite.equals("0")) {
                            data.workout_exercise_is_favourite = "1"
                            view.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star_active
                                )
                            )
                        } else {
                            data.workout_exercise_is_favourite = "0"
                            view.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star
                                )
                            )
                        }
                    } else {
                        //Constant.showCustomToast(getActivity(), "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })

    }

    fun setFavUnfavWorkout(value: String) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.module_name, StringConstant.workout)
        param.put(StringConstant.module_id, moduleId)
        val header = HashMap<String, String>()
        header.put(authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        WDetail.workout_fav_status = value
                        if (value.equals("1")) {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star_active
                                )
                            )
                        } else {
                            binding.ivFav.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star
                                )
                            )
                        }
                    } else {
                        Constant.showCustomToast(getActivity(), "" + message)
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity())
                }
            })

    }

    fun isAllDownload(): Boolean {
        var isDownload: Boolean = true
        for (i in 0..exerciseList.size - 1) {
            val lastIndex = exerciseList.get(i).workout_exercise_video.lastIndexOf("/")
            if (lastIndex > -1) {
                val downloadFileName =
                    exerciseList.get(i).workout_exercise_video.substring(lastIndex + 1)
                val path =
                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                            packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                val f = File(path)
                if (!f.exists()) {
                    isDownload = false
                    break
                } else {

                }
            }
        }
        return isDownload
    }

    fun downloadWorkout() {
        list.clear()
        for (i in 0..exerciseList.size - 1) {
            if (exerciseList.get(i) != null) {
                Log.d("video url", "video url...." + exerciseList.get(i).workout_exercise_video)

                //   if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                val lastIndex = exerciseList.get(i).workout_exercise_video.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName =
                        exerciseList.get(i).workout_exercise_video.substring(lastIndex + 1)
                    val subpath = "/Dovies//$downloadFileName"
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                    val f = File(path)
                    Log.e("download file path", "file path..." + f.absolutePath)
                    if (!f.exists()) {
                        //  f.createNewFile();
                        val foregroundListener = object : Foreground.Listener {
                            override fun background() {
                                Log.e("background", "Go to background")
                            }

                            override fun foreground() {
                                requestAudioFocusForMyApp(getActivity())
                                Log.e("Foreground", "Go to foreground")
                            }
                        }
                        val Download_Uri = Uri.parse(exerciseList.get(i).workout_exercise_video)
                        val request = DownloadManager.Request(Download_Uri)
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        request.setAllowedOverRoaming(false)
                        request.setTitle("Dovies Downloading $i.mp4")
                        request.setDescription("Downloading $i.mp4")
                        request.setVisibleInDownloadsUi(false)
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                        request.setDestinationInExternalFilesDir(
                            getActivity(),
                            "/." + Environment.DIRECTORY_DOWNLOADS,
                            subpath
                        )
                        refid = downloadManager!!.enqueue(request)
                        list.add(refid)
                    } else {
                        //  Toast.makeText(getActivity(), "already downloaded...$i", Toast.LENGTH_SHORT).show()
                    }
                }
                //  }
            }

        }
        if (!list.isEmpty() && list.size > 0) {
            binding.loader.visibility = View.VISIBLE
            binding.downloadingTxt.visibility = View.VISIBLE
            startDownloadThread()
        } else {
            binding.playVideo.isEnabled = true
        }

    }

    fun downloadExercise(videoUrl: String) {
        list.clear()
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            val subpath = "/Dovies//$downloadFileName"
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName + ".mp4"
            f = File(path)
            val Download_Uri = Uri.parse(videoUrl)
            val request = DownloadManager.Request(Download_Uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("Dovies Downloading .mp4")
            request.setDescription("Downloading .mp4")
            request.setVisibleInDownloadsUi(false)
            // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalFilesDir(
                getActivity(),
                "/." + Environment.DIRECTORY_DOWNLOADS,
                subpath
            )
            refid = downloadManager!!.enqueue(request)
            list.add(refid)
            startDownloadThread()
        }
    }

    private fun startDownloadThread() {
        // Initializing the broadcast receiver ...
        mBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                isRecieverRegistered = true

                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i) != null) {
                        // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                        val lastIndex = exerciseList.get(i).workout_exercise_video.lastIndexOf("/")
                        if (lastIndex > -1) {
                            val downloadFileName =
                                exerciseList.get(i).workout_exercise_video.substring(lastIndex + 1)
                            val path =
                                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                            val encryptedPath =
                                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                            val f = File(path)
                            if (f.exists()) {
                                exerciseList.get(i).workout_offline_video = path
                                // encrypt(path,encryptedPath,downloadFileName)
                            }
                        }
                        //  }
                    }

                }
                mFinishedFilesFromNotif.add(intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
                var referenceId = intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Log.e("IN", "" + referenceId);
                list.remove(referenceId);
                if (list.isEmpty()) {
                    binding.playVideo.isEnabled = true
                    if (isAllDownload()) {
                        Glide.with(getActivity())
                            .load(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_video_play_circle_ico
                                )
                            )
                            .into(binding.playVideo)
                        binding.loader.visibility = GONE
                        binding.downloadingTxt.visibility = View.GONE

                    } else {
                        Glide.with(getActivity()).load(
                            ContextCompat.getDrawable(
                                getActivity(),
                                R.drawable.ic_download_circle_svg
                            )
                        ).into(binding.playVideo)
                        //  binding.loader.visibility = VISIBLE
                    }

                }

            }
        }
        val intentFilter = IntentFilter("android.intent.action.DOWNLOAD_COMPLETE")
        registerReceiver(mBroadCastReceiver, intentFilter)


        // initializing the download manager instance ....
        // downloadManager = (DownloadManager).getSystemService(Context.DOWNLOAD_SERVICE);
        // starting the thread to track the progress of the download ..
        var isShown = true
        mProgressThread = Thread(Runnable {
            // Preparing the query for the download manager ...
            val q = DownloadManager.Query()
            val ids = LongArray(list.size)
            val idsArrList = java.util.ArrayList<Long>()
            var i = 0
            for (id in list) {
                ids[i++] = id
                idsArrList.add(id)
            }
            q.setFilterById(*ids)
            // getting the total size of the data ...
            var c: Cursor?

            while (true) {
                // check if the downloads are already completed ...
                // Here I have created a set of download ids from download manager to keep
                // track of all the files that are dowloaded, which I populate by creating
                //
                if (mFinishedFilesFromNotif.containsAll(idsArrList)) {
                    isDownloadSuccess = true
                    // TODO - Take appropriate action. Download is finished successfully
                    return@Runnable
                }
                // start iterating and noting progress ..
                c = downloadManager!!.query(q)

                if (c != null) {
                    var filesDownloaded = 0
                    var fileFracs = 0f // this stores the fraction of all the files in
                    // download
                    val columnTotalSize = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    val columnStatus = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    //final int columnId = c.getColumnIndex(DownloadManager.COLUMN_ID);
                    val columnDwnldSoFar =
                        c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)

                    while (c.moveToNext()) {
                        // checking the progress ..
                        if (c.getInt(columnStatus) == DownloadManager.STATUS_SUCCESSFUL) {
                            filesDownloaded++
                        } else if (c.getInt(columnTotalSize) > 0) {
                            fileFracs += c.getInt(columnDwnldSoFar) * 1.0f / c.getInt(
                                columnTotalSize
                            )
                        } else if (c.getInt(columnStatus) == DownloadManager.STATUS_FAILED) {
                            // TODO - Take appropriate action. Error in downloading one of the
                            // files.
                            return@Runnable
                        }// If the file is partially downloaded, take its fraction ..
                    }
                    c.close()
                    // calculate the progress to show ...
                    val progress = (filesDownloaded + fileFracs) / ids.size

                    // setting the progress text and bar...
                    val percentage = Math.round(progress * 100.0f)
                    val txt = "Loading ... $percentage%"
                    Log.d("progress...", "progress...$txt")
                    binding.loader.setProgress(percentage)

                    if (percentage == 100 && isShown) {
                        showPlay(percentage)
                        isShown = false
                    }
                }
            }
        })
        mProgressThread.start()
    }

    fun showPlay(percentage: Int) {
        runOnUiThread(Runnable() {
            fun run() {
                if (percentage == 100) {
                    binding.playVideo.isEnabled = true
                    Glide.with(getActivity())
                        .load(
                            ContextCompat.getDrawable(
                                getActivity(),
                                R.drawable.new_play_icon
                            )
                        )
                        .into(binding.playVideo)
                    Log.d("showPlay", "showPlay...." + percentage)
                    binding.loader.visibility = GONE
                    binding.downloadingTxt.visibility = View.GONE

                }
            }
        })

    }

    private fun countdowngetReady() {
        binding.countDownTxt.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen._25sdp)
        );
        binding.countDownTxt.text = "GET READY IN"
        var Timer: Int = 5
        countdownRunnable = object : Runnable {
            override fun run() {
                runOnUiThread {
                    Timer = Timer - 1
                    if (Timer == 4) {


                    } else if (Timer > 0) {
                        binding.countDownTxt.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen._55sdp)
                        )
                        binding.countDownTxt.text = "" + Timer
                    } else if (Timer == 0) {
                        binding.countDownTxt.text = "GO"
                    } else if (Timer == -1) {

                        Constant.releaseAudioFocusForMyApp(getActivity());
                        Foreground.removeListener(foregroundListener)
                        //  val intent = Intent(getActivity(), WorkoutVideoPlayActivity::class.java)
                        val intent = Intent(getActivity(), WorkoutVideoPlayActivityNew::class.java)
                        intent.putExtra("exerciseList", exerciseList)
                        intent.putExtra("WDetail", WDetail)
                        intent.putExtra("from_ProgramPlan", program_plan_id)
                        intent.putExtra("from_which_frament", from_which_frament)
                        startActivity(intent)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    } else {
                        binding.rlHeader1.visibility = GONE
                        binding.img.visibility = GONE
                        binding.countDownTxt.visibility = GONE
                        binding.opacityLayout.visibility = GONE
                        binding.countDownTxt.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen._25sdp)
                        );
                        binding.countDownTxt.text = "GET READY IN"
                    }

                }
                if (Timer > -2) {
                    countdownhandler.postDelayed(this, 1100)
                } else {
                    binding.rlHeader1.visibility = GONE
                    binding.img.visibility = GONE
                    binding.countDownTxt.visibility = GONE
                    binding.opacityLayout.visibility = GONE
                    binding.countDownTxt.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen._25sdp)
                    );
                    binding.countDownTxt.text = "GET READY IN"
                }

            }

        }
        countdownhandler.postDelayed(countdownRunnable as Runnable, 2000)
    }

    override fun onPause() {
        super.onPause()
        countdownRunnable?.let { countdownhandler.removeCallbacks(it) }
    }

    fun showDeleteDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.setContentView(R.layout.dialog_delete)

        if (!whichScreen.isEmpty()) {
            dialog.tv_header.setText("Are you sure you want to delete this Workout?")
        } else {
            dialog.tv_header.setText("Are you sure you want to delete this Workout Log?")
        }
        val tv_no = dialog.findViewById<TextView>(R.id.tv_no)
        val tv_delete = dialog.findViewById<TextView>(R.id.tv_delete)

        tv_no.setOnClickListener { v -> dialog.dismiss() }

        tv_delete.setOnClickListener { v ->
            deleteSavePost()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteSavePost() {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, "")
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.workout_id, moduleId)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().deleteWorkout(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            //first we call finish Activity but for manage loader issue we call here startactivity result
                            //finish()
                            val intent = Intent()
                            intent.putExtra("MESSAGE", "1")
                            setResult(20, intent);
                            finish();//finishing activity

                        } else {
                            finish()
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        countdownRunnable?.let { countdownhandler.removeCallbacks(it) }
        if (isRecieverRegistered == true)
            unregisterReceiver(mBroadCastReceiver)
    }

    val foregroundListener = object : Foreground.Listener {
        override fun background() {
            Log.e("background", "Go to background")
        }

        override fun foreground() {
            requestAudioFocusForMyApp(getActivity())
            Log.e("Foreground", "Go to foreground")
        }
    }

    override fun overwriteClick(type: String) {
        if (type.equals(
                "workout",
                true
            ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                .equals(
                    "No",
                    true
                )
            && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                .equals(
                    "0"
                )
        ) {

            var isLock = false
            for (i in 0..exerciseList.size - 1) {
                if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                    isLock = true
                }
            }

            if (isLock) {
                var intent =
                    Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
                        .putExtra("exercise", "yes")
                startActivityForResult(intent, 2)
            } else {
                AddToMyWorkout()
            }

        } else if (type.equals("delete", true)) {
            showDeleteDialog()
        } else if (type.equals("Save to my workout", true)) {

            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                    .equals(
                        "No",
                        true
                    ) && getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!
                    .equals(
                        "0"
                    )
            ) {
                var isLock = false
                for (i in 0..exerciseList.size - 1) {
                    if (exerciseList.get(i).exercise_access_level.equals("LOCK", true)) {
                        isLock = true
                    }
                }

                if (isLock) {
                    var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra(
                        "home",
                        "no"
                    )
                        .putExtra("exercise", "yes")
                    startActivityForResult(intent, 2)
                } else {
                    AddToMyWorkout()
                }
                //  }
            } else {
                AddToMyWorkout()
                //call Save to my workout api
            }
        } else {
        }
    }

    private fun AddToMyWorkout() {
        val param = HashMap<String, String>()

        param.put(StringConstant.device_token, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.device_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.workout_id, moduleId)

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")
        getDataManager().addToMyWorkout(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val json: JSONObject? = response?.getJSONObject(StringConstant.settings)
                        val status = json?.get(StringConstant.success)
                        val msg = json?.get(StringConstant.message)
                        if (status!!.equals("1")) {
                            startActivity(Intent(getActivity(), ActivityMyWorkoutList::class.java))
                        } else {
                            //Constant.showCustomToast(mContext, "" + msg)
                        }
                    } catch (ex: Exception) {
                        Constant.showCustomToast(getActivity(), getString(R.string.something_wrong))
                    }
                }

                override fun onError(anError: ANError) {
                    Constant.errorHandle(anError, getActivity() as Activity)
                }
            })
    }

}*/
