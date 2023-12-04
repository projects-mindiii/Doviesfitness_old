package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityExerciseDetaillistBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.ExerciseDetailAdapterNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_exercise_detaillist.add_exercises
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_CreateLeftAndRight
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_CreateSuperSet
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_addExercise
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_addExercise_setAndReps
import kotlinx.android.synthetic.main.activity_exercise_detaillist.btn_clear_filter
import kotlinx.android.synthetic.main.activity_exercise_detaillist.exercise_name
import kotlinx.android.synthetic.main.activity_exercise_detaillist.exercise_rv
import kotlinx.android.synthetic.main.activity_exercise_detaillist.filter_icon
import kotlinx.android.synthetic.main.activity_exercise_detaillist.iv_back
import kotlinx.android.synthetic.main.activity_exercise_detaillist.ll_delete_duplicate
import kotlinx.android.synthetic.main.activity_exercise_detaillist.ll_setsAndRepsBottomView
import kotlinx.android.synthetic.main.activity_exercise_detaillist.no_record_found
import kotlinx.android.synthetic.main.activity_exercise_detaillist.no_record_icon
import kotlinx.android.synthetic.main.activity_exercise_detaillist.selected_count
import kotlinx.android.synthetic.main.activity_exercise_detaillist.txt_filter
import org.json.JSONObject
import java.io.File


class ExerciseDetailListActivityNew : BaseActivity(), ExerciseDetailAdapterNew.OnItemClick,
    View.OnClickListener, IsSubscribed {
    override fun isSubscribed() {
        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }

    override fun setSelected(
        position: Int,
        isSelected: Boolean,
        exercise: ExerciseListingResponse.Data
    ) {
        if (isSelected == true) {
            selectedList.add(exercise)
        } else {
            if (selectedList.size > 0) {
                for (i in 0..selectedList.size - 1) {
                    if (i < selectedList.size) {
                        if (selectedList.get(i).exercise_id.equals(exercise.exercise_id)) {
                            selectedList.removeAt(i)
                        }
                    }
                }
            }
        }

        manageBottomButtonsEnableDiable()
    }

    fun manageBottomButtonsEnableDiable() {
        val filteredList = selectedList.filter { it.isSelected }
        if (filteredList.isNotEmpty()) {
            btn_addExercise_setAndReps.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.exo_white
                )
            );

            if (exerciseType == "SetAndReps") {
                /** for SetAndReps related selection and bottom view upedations  done by @Santosh jaat */
                if (isAddingInRound == "yes" || isAddingInRound == "Yes") {
                    add_exercises.visibility = View.VISIBLE

                    if (filteredList.size > 1)
                        add_exercises.text = "Add Exercises"
                    else
                        add_exercises.text = "Add Exercise"
                } else if (forReplace == "forReplace") {
                    add_exercises.visibility = View.VISIBLE

                    add_exercises.text = "Replace"
                } else

                    ll_setsAndRepsBottomView.visibility = View.VISIBLE



                ll_delete_duplicate.visibility = View.GONE
                if (filteredList.size == 1) {
                    /** only add exercise will be enable because for creating super set and left and right need atleast 2 exercise*/
                    isSuperSetEnable = false
                    isLeftAndRightEnable = false
                    isAddExerciseEnable = true
                    btn_addExercise_setAndReps.text = "ADD EXERCISE"
                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    btn_CreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );
                    btn_CreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );

                } else if (filteredList.size == 2) {
                    /**left and right , super set and add exercise will be enable for 2 selected exercises*/
                    isSuperSetEnable = true
                    isLeftAndRightEnable = true
                    isAddExerciseEnable = true
                    btn_addExercise_setAndReps.text = "ADD EXERCISES"

                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    btn_CreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    btn_CreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );


                } else if (filteredList.size >= 3) {
                    /**super set and add exercise button is enable because selected exercises are equal or more then 3*/
                    isSuperSetEnable = true
                    isLeftAndRightEnable = false
                    isAddExerciseEnable = true
                    btn_addExercise_setAndReps.text = "ADD EXERCISES"

                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    btn_CreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.exo_white
                        )
                    );
                    btn_CreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );

                } else {
                    /**all buttons are disable because all no exercise is selected*/
                    isSuperSetEnable = false
                    isLeftAndRightEnable = false
                    isAddExerciseEnable = false
                    btn_addExercise_setAndReps.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );
                    btn_CreateSuperSet.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );
                    btn_CreateLeftAndRight.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.line_color
                        )
                    );

                }


            } else {
                /** for follow along related selection and bottom view upedations  done by @Santosh jaat */

                if (forReplace == "forReplace") {
                    add_exercises.visibility = View.VISIBLE
                    ll_delete_duplicate.visibility = View.GONE
                    add_exercises.text = "Replace"
                } else {
                    add_exercises.visibility = View.GONE
                    ll_delete_duplicate.visibility = View.VISIBLE
                }
                if (filteredList.size == 1) {
                    selected_count.text = "${filteredList.size} SELECTED"
                    btn_addExercise.text = "ADD EXERCISE"
                } else {
                    selected_count.text = "${filteredList.size} SELECTED"
                    btn_addExercise.text = "ADD EXERCISES"
                }
            }


        }
        else {
            add_exercises.visibility = View.GONE
            ll_delete_duplicate.visibility = View.GONE
            ll_setsAndRepsBottomView.visibility = View.GONE
            /** all buttons are disable because all no exercise is selected */
            isSuperSetEnable = false
            isLeftAndRightEnable = false
            isAddExerciseEnable = false
            btn_addExercise_setAndReps.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.line_color
                )
            );
            btn_CreateSuperSet.setTextColor(ContextCompat.getColor(this, R.color.line_color));
            btn_CreateLeftAndRight.setTextColor(ContextCompat.getColor(this, R.color.line_color));
        }
    }

    override fun videoDownload(
        isScroll: Boolean,
        data: ExerciseListingResponse.Data,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        position: Int, isLoad: Boolean
    ) {
        isDownload = isLoad
        urlDownload = data.exercise_video
        if (isDownload) {
            for (i in 0 until exerciseListing.size) {
                if (i == position) {
                    exerciseListing.get(i).isClicked = true
                    binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)

                } else {
                    exerciseListing.get(i).isClicked = false
                }
            }
            binding.svMain.setEnableScrolling(false)
            Log.d("scrolling flag", "scrolling flag play.....false")
            adapter.notifyDataSetChanged()
        } else {
            binding.svMain.setEnableScrolling(true)
            Log.d("scrolling flag", "scrolling flag stop.....true")
        }
        if (isDownload)
            downloadExercise(data.exercise_video, position, view, isScroll)
    }

    override fun videoPlayClick(
        isScroll: Boolean,
        data: ExerciseListingResponse.Data,
        position: Int,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        isLoad: Boolean
    ) {
        isDownload = isLoad
        urlDownload = ""



        for (i in 0 until exerciseListing.size) {
            if (exerciseListing.get(i).isPlaying) {
                exerciseListing.get(i).isPlaying = false
                binding.svMain.setEnableScrolling(true)
                //  isPlayingFlag=true
                Log.d("scrolling flag", "scrolling flag.....true")

            } else {
                exerciseListing.get(i).isPlaying = true
                exerciseListing.get(i).showLoader = false
                try {
                    binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                binding.svMain.setEnableScrolling(false)
                Log.d("scrolling flag", "scrolling flag.....false")
            }
        }

        /*if ( binding.svMain.isEnableScrolling()){
            binding.svMain.setEnableScrolling(false)
        }
         else{
            binding.svMain.setEnableScrolling(true)
        }*/
        if (flag) {
            flag = false
            binding.svMain.setEnableScrolling(true)
        } else {
            flag = true
            binding.svMain.setEnableScrolling(false)
        }
        adapter.notifyDataSetChanged()
    }


    override fun shareURL(data: ExerciseListingResponse.Data) {
        sharePost(data.exercise_share_url)
    }

    private var isSuperSetEnable: Boolean = false
    private var isLeftAndRightEnable: Boolean = false
    private var isAddExerciseEnable: Boolean = false
    private var isAddingInRound: String = ""
    private var exerciseType: String = ""
    private var singleItemChange: String? = ""
    private var forReplace: String = ""
    var isPlayingFlag = false
    private lateinit var binding: ActivityExerciseDetaillistBinding;
    private lateinit var adapter: ExerciseDetailAdapterNew;
    private var exerciseListing = ArrayList<ExerciseListingResponse.Data>()
    private var selectedList = ArrayList<ExerciseListingResponse.Data>()

    private var page: Int = 1
    private var nextPage: Int = 0
    private var categoryId: String = ""
    private var name: String = ""
    private var tempList = ArrayList<FilterExerciseResponse.Data.X>()
    private var filterTags: String = ""
    private var filterLevel: String = ""
    private var filterBodyPart: String = ""
    private var filterEquipments: String = ""
    private var filterGoodFor: String = ""
    private var filterExercises: String = ""
    private var refid: Long = 0
    internal var list = ArrayList<Long>()
    private var downloadManager: DownloadManager? = null
    var from = ""
    var create = ""

    ///////for downloading
    internal lateinit var mBroadCastReceiver: BroadcastReceiver
    var isRecieverRegistered = false
    internal var mFinishedFilesFromNotif = ArrayList<Long>()
    lateinit var mProgressThread: Thread
    var isDownloadSuccess = false
    var isDownload = false
    var urlDownload = ""
    var keyword = ""
    var ifReplaceExerciese = false

    companion object {
        public var flag = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideNavStatusBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_detaillist)
        initialization()
    }

    fun hideNavStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(
                ContextCompat.getColor(
                    getActivity(),
                    R.color.colorBlack
                )
            )
        }

        val view = window.decorView
        view.systemUiVisibility =
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }

    override fun onResume() {
        super.onResume()
        hideNavStatusBar()
    }

    private fun initialization() {
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        btn_clear_filter.setOnClickListener(this)
        filter_icon.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        // done_btn.setOnClickListener(this)
        btn_addExercise.setOnClickListener(this)
        btn_addExercise_setAndReps.setOnClickListener(this)
        btn_CreateSuperSet.setOnClickListener(this)
        btn_CreateLeftAndRight.setOnClickListener(this)
        add_exercises.setOnClickListener(this)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320

        val videowidth = 120 + (179 * screenWidth)

        /*..........................................................................*/

        if (intent.getStringExtra("exerciseType") != null) {
            exerciseType = intent.getStringExtra("exerciseType")!!
        }
        if (intent.getStringExtra("isAddingInRound") != null) {
            isAddingInRound = intent.getStringExtra("isAddingInRound")!!
        }
        Log.d("aaaaaaa", "initialization: exerciseType $exerciseType")
        if (intent.getStringExtra("create") != null) {
            create = intent.getStringExtra("create")!!

            if (intent.hasExtra("forReplace")) {
                forReplace = intent.getStringExtra("forReplace")!!
                if (!forReplace.isEmpty()) {
                    ifReplaceExerciese = true
                }
            }

            if (intent.hasExtra("singleItemChange")) {
                singleItemChange = intent.getStringExtra("singleItemChange")
            }
        }
        /**commented because bottom button added and if any issue occure please try to uncomment it - 11 july 2023*/
        /*   if (create != null && !create.isEmpty()) {
               binding.llDeleteDuplicate.visibility = View.VISIBLE
           } else {
               create = ""
               binding.llDeleteDuplicate.visibility = View.GONE
           }
   */
        if (intent.getStringExtra("category_id") != null && intent.getStringExtra("category_id")!!
                .isEmpty()
        ) {
            // startActivityForResult(Intent(getActivity(),FilterExerciseActivity::class.java),101)
            exercise_name.setText(getString(R.string.exercises_library))

            if (intent.getStringExtra("from") != null && !intent.getStringExtra("from")!!
                    .isEmpty() && intent.getStringExtra(
                    "from"
                ).equals("search")
            ) {
                from = intent.getStringExtra("from")!!;
                var list = ArrayList<FilterExerciseResponse.Data.X>()
                list =
                    intent.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
                keyword = ""
                for (i in 0..list.size - 1) {
                    if (list.get(i).group_key.equals(StringConstant.filter_level, true)) {
                        filterLevel = filterLevel + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_tags, true)) {
                        filterTags = filterTags + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(
                            StringConstant.filter_body_part,
                            true
                        )
                    ) {
                        filterBodyPart = filterBodyPart + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(
                            StringConstant.filter_equipments,
                            true
                        )
                    ) {
                        filterEquipments = filterEquipments + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_good_for, true)) {
                        filterGoodFor = filterGoodFor + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(
                            StringConstant.filter_exercises,
                            true
                        )
                    ) {
                        filterExercises = filterExercises + list.get(i).id + ","
                    } else {

                    }
                }
            } else if (intent.getStringExtra("keyword") != null && !intent.getStringExtra("keyword")!!
                    .isEmpty()
            ) {
                keyword = intent.getStringExtra("keyword")!!
            } else {
                keyword = ""
                try {
                    tempList =
                        intent.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
                    for (i in 0..tempList.size - 1) {
                        if (tempList.get(i).group_key.equals(StringConstant.filter_level, true)) {
                            filterLevel = filterLevel + tempList.get(i).id + ","
                        } else if (tempList.get(i).group_key.equals(
                                StringConstant.filter_tags,
                                true
                            )
                        ) {
                            filterTags = filterTags + tempList.get(i).id + ","
                        } else if (tempList.get(i).group_key.equals(
                                StringConstant.filter_body_part,
                                true
                            )
                        ) {
                            filterBodyPart = filterBodyPart + tempList.get(i).id + ","
                        } else if (tempList.get(i).group_key.equals(
                                StringConstant.filter_equipments,
                                true
                            )
                        ) {
                            filterEquipments = filterEquipments + tempList.get(i).id + ","
                        } else if (tempList.get(i).group_key.equals(
                                StringConstant.filter_good_for,
                                true
                            )
                        ) {
                            filterGoodFor = filterGoodFor + tempList.get(i).id + ","
                        } else if (tempList.get(i).group_key.equals(
                                StringConstant.filter_exercises,
                                true
                            )
                        ) {
                            filterExercises = filterExercises + tempList.get(i).id + ","
                        } else {

                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                btn_clear_filter.visibility = View.VISIBLE
                txt_filter.visibility = View.VISIBLE
            }

            // Constant.intent(getActivity(),"size.."+tempList.size)
            exerciseListing.clear()
            page = 1

            getExerciseListingData(1)

        } else {
            keyword = ""
            categoryId = intent.getStringExtra("category_id")!!
            name = intent.getStringExtra("name")!!
            val Str: String = name.toUpperCase()
            exercise_name.setText(Str)
            binding.progressLayout.visibility = View.VISIBLE
            getExerciseListingData(page)
        }

        /*..........................................................................*/

        if (create != null && !create.isEmpty())
            adapter = ExerciseDetailAdapterNew(
                getActivity(),
                ifReplaceExerciese,
                exerciseListing,
                this,
                videowidth,
                create,
                this
            )
        else
            adapter = ExerciseDetailAdapterNew(
                getActivity(),
                ifReplaceExerciese,
                exerciseListing,
                this,
                videowidth,
                "",
                this
            )
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())

        binding.exerciseRv.setNestedScrollingEnabled(false);
        binding.exerciseRv.setHasFixedSize(false);
        binding.exerciseRv.layoutManager = layoutManager
        binding.exerciseRv.adapter = adapter

        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
              swipe_refresh.setProgressViewOffset(
                  false,
                  resources.getDimension(R.dimen._10sdp).toInt(),
                  resources.getDimension(R.dimen._50sdp).toInt()
              )
          }

          swipe_refresh.setOnRefreshListener {
              page = 1
              exerciseListing.clear()
              getExerciseListingData(page)
          }
  */
        binding.svMain.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1)
                            .getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY
                    ) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage == 1) {
                            adapter.showLoading(true)
                            adapter.notifyDataSetChanged()
                            getExerciseListingData(page)
                        }
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.filter_icon -> {
                keyword = ""
                filterLevel = "";filterBodyPart = "";filterEquipments = "";filterExercises =
                    "";filterGoodFor =
                    "";filterTags = ""
                if (tempList.size != 0) {
                    val intent = Intent(this, FilterExerciseActivity::class.java)
                    intent.putExtra("list", tempList)
                    intent.putExtra("create", create)
                    startActivityForResult(intent, 101)
                } else
                    startActivityForResult(
                        Intent(getActivity(), FilterExerciseActivity::class.java).putExtra(
                            "create",
                            create
                        ), 101
                    )

            }

            R.id.add_exercises -> {
                //  if (isAddExerciseEnable) {
                if (forReplace == "forReplace") {
                    getSelectedExercise("")
                } else {
                    getSelectedExercise("")
                }

                //  }
            }

            R.id.btn_addExercise_setAndReps -> {
                if (isAddExerciseEnable) {

                    getSelectedExercise("")
                }
            }

            R.id.btn_CreateLeftAndRight -> {
                if (isLeftAndRightEnable) {


                    getSelectedExercise("LeftAndRight")
                }
            }

            R.id.btn_CreateSuperSet -> {
                if (isSuperSetEnable) {
                    // showTost("btn_CreateSuperSet")
                    getSelectedExercise("SuperSet")
                }
            }

            R.id.iv_back -> {
                if (tempList.size != 0)
                    tempList.clear()
                onBackPressed()
            }

            R.id.btn_addExercise -> {
                getSelectedExercise("SuperSet")

            }

            R.id.btn_clear_filter -> {
                clearFilterData()
                if (intent.getStringExtra("category_id") != null && intent.getStringExtra("category_id")!!
                        .isEmpty()
                ) {

                    val intent = Intent()
                    intent.putExtra("whenComeFromOut", "yes")
                    setResult(Activity.RESULT_OK, intent)
                    if (from.equals("search")) {

                    } else
                        onBackPressed()
                }
            }
        }
    }

    fun getSelectedExercise(exerciseType: String) {

        if (selectedList.isNotEmpty()) {
            selectedList.forEach {
                it.leftAndRightOrSuperSetOrAddExercise = exerciseType
            }
            if (ifReplaceExerciese) {

                Log.v("exerciseListing", "" + exerciseListing)
                val intent = Intent()
                intent.putExtra("list", exerciseListing)
                setResult(Activity.RESULT_OK, intent)
                finish()


            } else {
                val intent = Intent()
                intent.putExtra("list", selectedList)
                intent.putExtra("exerciseType", exerciseType)
                setResult(Activity.RESULT_OK, intent)
                finish()

            }
        }


    }

    private fun clearFilterData() {
// Set blank india filter paramater
        filterLevel = ""
        filterExercises = ""
        filterTags = ""
        filterBodyPart = ""
        filterEquipments = ""
        filterGoodFor = ""
        keyword = ""
        tempList.clear()
        exerciseListing.clear()
        btn_clear_filter.visibility = View.GONE
        ll_setsAndRepsBottomView.visibility = View.GONE

        btn_addExercise_setAndReps.setTextColor(ContextCompat.getColor(this, R.color.line_color));
        btn_CreateSuperSet.setTextColor(ContextCompat.getColor(this, R.color.line_color));
        btn_CreateLeftAndRight.setTextColor(ContextCompat.getColor(this, R.color.line_color));

        txt_filter.visibility = View.GONE
        getExerciseListingData(1)

    }

    /*
    * URL==https://dev.doviesfitness.com/WS/exercise_listing
Param===Optional(["filter_good_for": "", "filter_tags": "", "auth_customer_subscription": "Paid", "filter_body_part": "", "auth_customer_id": "523a72640c710ec076bae3fa5e7e0ca5e1b34a02f3e1fd8b1ba5216acf425db1", "page_index": "1", "filter_equipments": "1,27,23,4,16,20,25,24,14,10,17,13,2", "device_token": diSB317yNW8:APA91bF5NJb84Sohi0693mH07LIFDLKBRW4NrUAOuQPgp3tiaSB9gz7R0YGj9AA-yS2qQxTKyYCIvorPJ11zb808gryzrL2Tm3ssc2tn0_gYjeQt2e6hpNjGcj9Ccm8r0xJcCxbI7RvT, "filter_exercises": "", "device_type": "Ios", "device_id": "7565183F-C7F9-40C1-B021-13513F00E585", "filter_level": ""])
headers===Optional(["APIVERSION": "1", "AUTHTOKEN": "523a72640c710ec076bae3fa5e7e0ca5e1b34a02f3e1fd8b1ba5216acf425db1", "APIKEY": "HBDEV"])
    *
    * */

    private fun getExerciseListingData(pageCount: Int) {

        if (CommanUtils.isNetworkAvailable(this)!!) {
            val param = HashMap<String, String>()
            param.put(StringConstant.device_token, "")
            param.put(StringConstant.device_id, "")
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.page_index, "" + pageCount)
            param.put(StringConstant.auth_customer_id, "")
            param.put(StringConstant.filter_level, filterLevel)
            param.put(StringConstant.filter_exercises, filterExercises)
            param.put(StringConstant.filter_tags, filterTags)
            param.put(StringConstant.filter_body_part, filterBodyPart)
            param.put(StringConstant.filter_equipments, filterEquipments)
            param.put(StringConstant.filter_category, categoryId)
            param.put(StringConstant.filter_good_for, filterGoodFor)
            param.put("filter_keyword", keyword)
            var customerType =
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType != null && !customerType!!.isEmpty()) {
                param.put(StringConstant.auth_customer_subscription, customerType)
            } else {
                param.put(StringConstant.auth_customer_subscription, "free")
            }

            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().exerciseDetailListApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        val next_page = jsonObject?.getString(StringConstant.next_page)
                        nextPage = next_page!!.toInt()

                        binding.progressLayout.visibility = View.GONE
                        if (success.equals("1")) {
                            no_record_found.visibility = View.GONE
                            no_record_icon.visibility = View.GONE
                            exercise_rv.visibility = View.VISIBLE

                            val exercisesListData =
                                getDataManager().mGson?.fromJson(
                                    response.toString(),
                                    ExerciseListingResponse::class.java
                                )
                            exerciseListing.addAll(exercisesListData!!.`data`);
                            if (selectedList.isNotEmpty() && exerciseListing.isNotEmpty()) {
                                for (j in 0 until exerciseListing.size) {
                                    for (i in 0 until selectedList.size) {
                                        if (exerciseListing.get(j).exercise_id == selectedList.get(i).exercise_id) {
                                            exerciseListing.get(j).isSelected = true
                                        }

                                    }

                                }


                            }
                            manageBottomButtonsEnableDiable()
                            //  downloadExercises()
                            //   page=page+1
                            //nextPage = exercisesListData.settings.next_page.toInt()
                            //Log.d("response", "nextPage......" + nextPage)
                            hideFooterLoiader()
                        }
                        if (exerciseListing.isEmpty()) {
                            no_record_found.visibility = View.VISIBLE
                            no_record_icon.visibility = View.VISIBLE
                            exercise_rv.visibility = View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        hideFooterLoiader()
                        binding.progressLayout.visibility = View.GONE
                        //  Constant.errorHandle(anError,binding.exerciseRv.context!!)
                        Constant.showCustomToast(
                            binding.exerciseRv.context!!,
                            getString(R.string.something_wrong)
                        )
                    }
                })
        } else {
            Constant.showInternetConnectionDialog(this)
        }
    }

    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        // isPlayingFlag=true
        adapter.notifyDataSetChanged()
        binding.svMain.setEnableScrolling(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101 && data != null) {
                tempList =
                    data.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
                // Constant.showCustomToast(getActivity(),"size.."+tempList.size)
                for (i in 0..tempList.size - 1) {
                    if (tempList.get(i).group_key.equals(StringConstant.filter_level, true)) {
                        filterLevel = filterLevel + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_tags, true)) {
                        filterTags = filterTags + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(
                            StringConstant.filter_body_part,
                            true
                        )
                    ) {
                        filterBodyPart = filterBodyPart + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(
                            StringConstant.filter_equipments,
                            true
                        )
                    ) {
                        filterEquipments = filterEquipments + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(
                            StringConstant.filter_good_for,
                            true
                        )
                    ) {
                        filterGoodFor = filterGoodFor + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(
                            StringConstant.filter_exercises,
                            true
                        )
                    ) {
                        filterExercises = filterExercises + tempList.get(i).id + ","
                    } else {

                    }
                }
                keyword = ""
                page = 1
                exerciseListing.clear()
                adapter.notifyDataSetChanged()
                btn_clear_filter.visibility = View.VISIBLE
                txt_filter.visibility = View.VISIBLE
                getExerciseListingData(1)

            }
        } else if (resultCode == 101) {
            if (categoryId.isEmpty())
                onBackPressed()
        } else if (requestCode == 2 && data != null) {
            page = 1
            exerciseListing.clear()
            getExerciseListingData(page)
        }
    }

    fun downloadExercise(
        videoUrl: String,
        position: Int,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        scroll: Boolean
    ) {
        //  list.clear()
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            var subpath = "/Dovies//$downloadFileName"
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
            val f = File(path)
            var Download_Uri = Uri.parse(videoUrl)
            val request = DownloadManager.Request(Download_Uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("Dovies Downloading .mp4")
            request.setDescription("Downloading .mp4")
            request.setVisibleInDownloadsUi(false)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalFilesDir(
                getActivity(),
                "/." + Environment.DIRECTORY_DOWNLOADS,
                subpath
            )
            refid = downloadManager!!.enqueue(request)
            list.add(refid)
            startDownloadThread(videoUrl, position, view, scroll)
        }

    }


    override fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView) {

    }

    override fun setFavUnfavForExercies(
        data: ExerciseListingResponse.Data,
        position: Int,
        view: ImageView
    ) {
        val param = HashMap<String, String>()
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        param.put(StringConstant.module_name, StringConstant.exercise)
        param.put(StringConstant.module_id, data.exercise_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        if (data.exercise_is_favourite.equals("0")) {
                            data.exercise_is_favourite = "1"
                            view.setImageDrawable(
                                ContextCompat.getDrawable(
                                    getActivity(),
                                    R.drawable.ic_star_active
                                )
                            )
                        } else {
                            data.exercise_is_favourite = "0"
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


    private fun startDownloadThread(
        videoUrl: String,
        position: Int,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        scroll: Boolean
    ) {
        // Initializing the broadcast receiver ...
        mBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                isRecieverRegistered = true

                // for (i in 0..exerciseList.size - 1) {
                if (videoUrl != null) {
                    // if (exerciseList.get(i).exercise_access_level.equals("OPEN")) {
                    val lastIndex = videoUrl.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName = videoUrl.substring(lastIndex + 1)
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        /*
                                                val encryptedPath =
                                                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
                        */
                        val f = File(path)
                        if (f.exists()) {
                            if (isDownload && urlDownload.equals(videoUrl, true)) {
                                var uri = Uri.parse(path)
                                exerciseListing.get(position).isClicked = false
                                adapter.intializePlayer(uri, position, view)
                            }
                            // adapter.playVideo(path,mPlayview)
                            //  exerciseListing.get(pos).workout_offline_video = path
                            // encrypt(path,encryptedPath,downloadFileName)
                        }
                    }
                    //  }
                }

                // }
                mFinishedFilesFromNotif.add(intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID))
                var referenceId = intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                Log.e("IN", "" + referenceId);
                list.remove(referenceId);

            }
        }
        val intentFilter = IntentFilter("android.intent.action.DOWNLOAD_COMPLETE")
        registerReceiver(mBroadCastReceiver, intentFilter)


        // initializing the download manager instance ....
        // downloadManager = (DownloadManager).getSystemService(Context.DOWNLOAD_SERVICE);
        // starting the thread to track the progress of the download ..
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
                    //  binding.loader.setProgress(percentage)
                    // Show the progress appropriately ...
                }
            }
        })
        mProgressThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRecieverRegistered == true)
            unregisterReceiver(mBroadCastReceiver)

    }

    ///////download all open exercises
    fun downloadExercises() {
        for (i in 0..exerciseListing.size - 1) {
            if (exerciseListing.get(i) != null) {
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
                        .equals("Yes") ||
                    exerciseListing.get(i).exercise_access_level.equals("OPEN")
                ) {
                    val lastIndex = exerciseListing.get(i).exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName =
                            exerciseListing.get(i).exercise_video.substring(lastIndex + 1)
                        val subpath = "/Dovies//$downloadFileName"
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                    packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val f = File(path)
                        if (!f.exists()) {
                            val Download_Uri = Uri.parse(exerciseListing.get(i).exercise_video)
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
                        }
                    } else {

                    }
                }

            }
        }

    }


}