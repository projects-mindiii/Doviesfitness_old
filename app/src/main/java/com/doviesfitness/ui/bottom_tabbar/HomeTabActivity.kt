package com.doviesfitness.ui.bottom_tabbar

import android.app.FragmentManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.BuildConfig
import com.doviesfitness.R
import com.doviesfitness.chromecast.settings.StreamCollectionNew
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.fcm.NotificationModal
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.StartDownloadManager
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.diet_plan.DietPlanFragment
import com.doviesfitness.ui.bottom_tabbar.diet_plan.fragment.DietPCategoryDetailFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.HomeTabFragment
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadBinder
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadService
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments.StreamCollection
import com.doviesfitness.ui.bottom_tabbar.workout_plan.PlansFragment
import com.doviesfitness.ui.bottom_tabbar.workout_plan.WorkoutPlanFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.WorkoutFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.ui.profile.MyProfileFragment
import com.doviesfitness.ui.profile.inbox.activity.CustomNotificationActivity
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.Constants
import com.doviesfitness.utils.PermissionAll
import com.doviesfitness.utils.StringConstant
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
 import java.net.URLDecoder
import java.util.*
 import kotlin.collections.HashMap

class HomeTabActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mNavigationDrawerFragment: NavigationDrawerFragment
    private var isFavWorkOut: Boolean = false
    private var module_id: String = ""
    private var fromDeepLinking: String = ""
    private var FromAddDietPlan: String = ""
    private var notificationType: String = ""
    private lateinit var notificationModal: NotificationModal
    public var comeFromCreatPlan: Boolean = false
    var clickId: Int = 0
    lateinit var actionBar1: RelativeLayout
    private var exerciseList = ArrayList<FilterExerciseResponse.Data>()
    private lateinit var active: androidx.fragment.app.Fragment
    lateinit var fm: androidx.fragment.app.FragmentTransaction
    private var myPRofileFramgnet = MyProfileFragment()
    private var dietPlanFramgnet = DietPlanFragment()
    private var workOutFramgnet = WorkoutFragment()
    private var homeTabFramgnet = HomeTabFragment()
    private var workOutPlanFragment = WorkoutPlanFragment()
    private var streamCollection = StreamCollection()
    private var streamCollection1 = StreamCollectionNew()
    private var plansFragment = PlansFragment()
    var showSubs=true
    public companion object {
        var fromNotificationClick = "0"
        var whichScreen: String = ""
        public  var downloadBinder: DownloadBinder? = null
    }

    /*var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
      override  fun onReceive(context: Context, intent: Intent) {
          getUSerDetail()
        }
    }*/


    /*var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
      override  fun onReceive(context: Context, intent: Intent) {
          getUSerDetail()
        }
    }*/


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            downloadBinder = service as DownloadBinder
            Log.d("ServiceConnection", "ServiceConnection...")

            val list = DownloadUtil.getData("video")
            if (list != null && list.size > 0) {
              //  downloadBinder!!.continueDownload()
                getDataManager().setUserStringData(AppPreferencesHelper.IS_RESUME,"pause")

                deleteDownloadLocalFile(list.get(0).VideoUrl)
                try {
                    if (DownloadUtil.referanceId!=null&& DownloadUtil.list!=null && DownloadUtil.list.size>0)
                        DownloadUtil.deleteDownload()
                }catch (e:Exception){
                   e.printStackTrace()
                }

                DownloadUtil.downloadExercise(list.get(0).VideoUrl, 0, null, null)
                getDataManager().setUserStringData(AppPreferencesHelper.STEAM_WORKOUT_ID,list[0].workout_id)


            }
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private fun deleteDownloadLocalFile(downloadFileUrl: String?) {
        var ret: File? = null
        val customerName = getDataManager().getUserInfo().customer_user_name
        try {
            if (downloadFileUrl != null && !TextUtils.isEmpty(downloadFileUrl)) {
                val lastIndex = downloadFileUrl.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName1 = downloadFileUrl.substring(lastIndex + 1)
                    val path =
                        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                BuildConfig.APPLICATION_ID+ "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + downloadFileName1

                    ret = File(path)
                    if (!ret.parentFile.exists()) {
                    }
                    if (!ret.exists()) {
                    } else {
                        ret.delete()
                    }
                }
            }
        } catch (ex: IOException) {
            Log.e(DownloadUtil.TAG_DOWNLOAD_MANAGER, ex.message, ex)
        }
    }


    private fun startAndBindDownloadService() {
        try {
            val downloadIntent = Intent(this, DownloadService::class.java)
            startService(downloadIntent)
            bindService(downloadIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_home_tab)
//startAndBindDownloadService()
        Handler().postDelayed({
            startRemainingDownloadVideo()
        }, 5000)

        initView(savedInstanceState)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(savedInstanceState: Bundle?) {
        getDeviseVolume()
        home_ll.setOnClickListener(this)
        workout_ll.setOnClickListener(this)
        diet_plan_ll.setOnClickListener(this)
        workout_plan_ll.setOnClickListener(this)
        my_profile_ll.setOnClickListener(this)
        stream_layout.setOnClickListener(this)

        val windowBackground = window.decorView.background
        topBlurView.setupWith(mainView_id)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(25f)
            .setHasFixedTransformationMatrix(true)


        // initilize all fragments here
        if (savedInstanceState == null) {
            allFragmentsInitilize()
        }

        // get deep linking data
        val intent = intent
        val data = intent.data

        if (data != null && !data.toString().isEmpty()) {
            // deep LInking  Code Work
            val afterDecode = URLDecoder.decode(data.toString(), "UTF-8")

            // deep LInking  Code Work
            //val afterDecode = URLDecoder.decode("https://www.doviesfitness.com/d?share=U2R3L1dPUktPVVQvT08xJjRaUC8zNTAvQ1Jm", "UTF-8")
            val uri = Uri.parse(afterDecode)
            val limit = uri.getQueryParameter("share")

            Log.v("log1", "1")

            forHandlingDeepLinking(limit)
        }

        // when we click on add icon india myDietPlanActivity then we redirect it on diet tab from this
        if (intent.getStringExtra("fromMyDietPlanActvity") != null) {
            whichScreen = intent.getStringExtra("fromMyDietPlanActvity")!!
            if (!whichScreen.isEmpty()) {
                workout_plan_ll.callOnClick()
            }
        } else if (intent.getStringExtra("fromWhichScreen") != null) {
            //when it come from WorkOutPlanDetailActivity then redirect it on My profile directly
            whichScreen = intent.getStringExtra("fromWhichScreen")!!
            if (!whichScreen.isEmpty()) {
                my_profile_ll.callOnClick()
            }
        } else if (intent.getStringExtra("fromMyPlanActvity") != null) {
            whichScreen = intent.getStringExtra("fromMyPlanActvity")!!
            if (!whichScreen.isEmpty()) {
                workout_plan_ll.callOnClick()
            }

        } else if (intent.getStringExtra("FromAddDietPlan") != null) {
            whichScreen = intent.getStringExtra("FromAddDietPlan")!!
            if (!whichScreen.isEmpty()) {
                diet_plan_ll.callOnClick()
            }
        } else if (intent.getStringExtra("WhenPlanCreate") != null) {
            whichScreen = intent.getStringExtra("WhenPlanCreate")!!
            if (!whichScreen.isEmpty()) {
                comeFromCreatPlan = true
                my_profile_ll.callOnClick()
            }
        } else {
            //For background notification
            handleNotification(getIntent());
        }

        //here we come from workout plan to see exesciting plan in list that why
        if (!whichScreen.equals("fromMyPlanActvity")) {
            Log.v("log1", "3")
            if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_IS_SUBSCRIBED)!!.equals(
                    "0"
                ) &&
                getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                    "No",
                    true
                )
            ) {
                if (!whichScreen.equals("fromMyDietPlanActvity") && !whichScreen.equals("WhenWorkoutPlanDetailActivity") && !fromDeepLinking.equals(
                        "fromDeepLinking")) {
                    if (showSubs) {
                        showSubs=true
                        startActivity(
                            Intent(
                                getActivity(),
                                SubscriptionActivity::class.java
                            ).putExtra("home", "yes")
                        )
                    }
                    else{
                    }
                    fromDeepLinking = ""
                }
            }
        }

        //disable the swipe geture on the navigation drawer
        drawer_layout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawer_layout.addDrawerListener(object : androidx.drawerlayout.widget.DrawerLayout.DrawerListener { override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                try {
                    if (mNavigationDrawerFragment!=null)
                        mNavigationDrawerFragment.setData()
                }catch (e:Exception){
                   e.printStackTrace()
                }


            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        val permissin = PermissionAll()
        permissin.RequestMultiplePermission(getActivity())

        val window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite))
        getFilterWorkoutData()
        //get user data here and not on myProfilefragement
       //also manage batchcount here
      //  getUSerDetail()
    }

    /* ..............for handle notification of group request...............*/
    private fun handleNotification(intent: Intent?) {

        var type="";
        val bundle = getIntent().extras
        if (intent!!.getSerializableExtra("FromNotificaton") != null) {
            //when come from notification
            /*if(homeTabFramgnet!=null){
                homeTabFramgnet.increaseCount()
            }*/
            fromNotificationClick="3"
            val comeFromNotifications =
                intent.getSerializableExtra("FromNotificaton") as NotificationModal
            if (!comeFromNotifications.module_name.isEmpty()) {
                showSubs=false
                notificationModal = NotificationModal()

                notificationModal.module_id = comeFromNotifications.module_id
                notificationModal.module_name = comeFromNotifications.module_name
                notificationModal.mediaUrl = comeFromNotifications.mediaUrl

                module_id = comeFromNotifications.module_id
                notificationType = comeFromNotifications.module_name

                if ("welCome".equals(comeFromNotifications.module_name)) {
                    home_ll.callOnClick()
                    type="welcome"
                   /* val intent = Intent(this, CustomNotificationActivity::class.java)
                    intent.putExtra("FromNotificationsFromBAckAndFore", module_id)
                    intent.putExtra("Notification_code", "welcome")
                    startActivity(intent)*/
                }
                else if ("PROGRAM".equals(comeFromNotifications.module_name)) {
                    type="Program_plan"
                    workout_plan_ll.callOnClick()
                }
                else if ("NEWSFEED".equals(comeFromNotifications.module_name)) {
                    type="News_feed"
                    if (module_id != null && !module_id.isEmpty()) {
                        homeTabFramgnet = homeTabFramgnet.newInstance(module_id)
                        showFragment(homeTabFramgnet)
                        module_id = ""
                        notificationType = ""
                    }
                    //rt454home_ll.callOnClick()
                }
                else if ("Stream_workout".equals(comeFromNotifications.module_name,true)) {
                    type="Stream_workout"
                    stream_layout.callOnClick()
                    //rt454home_ll.callOnClick()
                }
                else if ("DIET_PLAN".equals(comeFromNotifications.module_name)) {
                    type="Diet_Plan"
                    workout_plan_ll.callOnClick()
                }
                else if ("WORKOUT".equals(comeFromNotifications.module_name)) {
                    type="Workout"
                    workout_ll.callOnClick()
                }
                else if ("EXERCISE".equals(comeFromNotifications.module_name)) {
                    type="Exercise"
                    workout_ll.callOnClick()
                }
                else if ("CUSTOM".equals(comeFromNotifications.module_name)) {
                   // my_profile_ll.callOnClick()
                    val intent = Intent(this, CustomNotificationActivity::class.java)
                    intent.putExtra("FromNotificationsFromBAckAndFore", module_id)
                    intent.putExtra("Notification_code", "custom")
                    startActivity(intent)
                }

                if (module_id!=null&&!module_id.isEmpty()){
                    updateNotificationStatus(module_id,type)
                }
            }
        }
        else {
            // handling when app is in background and kill

            /*  Bundle[{google.delivered_priority=high, google.sent_time=1574060012947, module_id=782, google.ttl=2419200, google.original_priority=high, module_name=PROGRAM, mediaUrl=https://d37p7paypigdpd.cloudfront.net/program_images/Ax4ZfJqilGHKvuzO.jpg, body=New plan has been added., from=971897138475, name=Workout12, sound=default, title=, click_action=ChatActivity, google.message_id=0:1574060012958567%108564b4108564b4, collapse_key=com.doviesfitness.debug}]*/

            if (bundle != null) {
                if (bundle.getString("module_name") != null) {
                    showSubs=false
                    fromNotificationClick="3"
                    notificationModal = NotificationModal()
                    notificationModal.mediaUrl = bundle.getString("mediaUrl")!!.toString()
                    notificationModal.module_name = bundle.getString("module_name")!!.toString()
                    notificationModal.module_id = bundle.getString("module_id")!!.toString()

                    module_id = notificationModal.module_id
                    notificationType = notificationModal.module_name

                    if ("welCome".equals(notificationModal.module_name)) {
                        type="welcome"
                        home_ll.callOnClick()
                      /*  val intent = Intent(this, CustomNotificationActivity::class.java)
                        intent.putExtra("FromNotificationsFromBAckAndFore", module_id)
                        intent.putExtra("Notification_code", "welcome")
                        startActivity(intent)*/
                    } else if ("PROGRAM".equals(notificationModal.module_name)) {
                        type="Program_plan"
                        workout_plan_ll.callOnClick()
                    } else if ("NEWSFEED".equals(notificationModal.module_name)) {
                        type="News_feed"
                        if (module_id != null && !module_id.isEmpty()) {
                            homeTabFramgnet = homeTabFramgnet.newInstance(module_id)
                            showFragment(homeTabFramgnet)
                            module_id = ""
                            notificationType = ""
                        }
                        //home_ll.callOnClick()
                    }

                    else if ("Stream_workout".equals(notificationModal.module_name,true)) {
                        type="Stream_workout"
                        stream_layout.callOnClick()
                        //rt454home_ll.callOnClick()
                    }
                    else if ("DIET_PLAN".equals(notificationModal.module_name)) {
                        type="Diet_Plan"
                        workout_plan_ll.callOnClick()
                    } else if ("WORKOUT".equals(notificationModal.module_name)) {
                        type="Workout"
                        workout_ll.callOnClick()
                    } else if ("EXERCISE".equals(notificationModal.module_name)) {
                        type="Exercise"
                        workout_ll.callOnClick()
                    } else if ("CUSTOM".equals(notificationModal.module_name)) {
                      //  my_profile_ll.callOnClick()
                        val intent = Intent(this, CustomNotificationActivity::class.java)
                        intent.putExtra("FromNotificationsFromBAckAndFore", module_id)
                        intent.putExtra("Notification_code", "custom")
                        startActivity(intent)
                    }
                    if (module_id!=null&&!module_id.isEmpty()){
                        updateNotificationStatus(module_id, type)
                    }
                }
            }
        }
    }

    private fun allFragmentsInitilize() {
        homeTabFramgnet = HomeTabFragment()
        workOutFramgnet = WorkoutFragment()
        dietPlanFramgnet = DietPlanFragment()
        myPRofileFramgnet = MyProfileFragment()
        workOutPlanFragment = WorkoutPlanFragment()
        mNavigationDrawerFragment = NavigationDrawerFragment()
        plansFragment = PlansFragment()
      //  streamCollection = StreamCollection()
        streamCollection1 = StreamCollectionNew()

        mNavigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment

        home_ll.callOnClick()
    }

    // show and hide fragments
    public fun showFragment(fragment: androidx.fragment.app.Fragment) {
        if (supportFragmentManager != null) {
            val mAllFragments = supportFragmentManager.fragments
            for (mFragment in mAllFragments) {
                if (mFragment !is NavigationDrawerFragment) {
                    if (!mFragment.isHidden()) {
                        supportFragmentManager.beginTransaction().hide(mFragment).commit();
                    }
                }
            }
            val ft = supportFragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(R.id.container_id, fragment)
                ft.show(fragment);
            }
            ft.commit();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun forHandlingDeepLinking(limit: String?) {
        // Sending side
        // val data1 = text.getBytes("UTF-8")
        //val base64 = Base64.encodeToString(data1, Base64.DEFAULT)

        if (limit != null) {
            val originalInput = limit
            val result = android.util.Base64.decode(originalInput, android.util.Base64.DEFAULT)
            Log.v("testinput", String(result))

            val currentString = String(result)
            val separated =
                currentString.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val firstArrAry = separated[0] // this will contain "Fruit"
            val secondArrAry = separated[1]
            Log.v("current", "---------" + firstArrAry + "---------" + secondArrAry);

            val firstArraryString = firstArrAry
            val separatedBySlace =
                firstArraryString.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val moduleName = separatedBySlace[1]

            val secondArraryString = secondArrAry
            val second_separatedBySlace =
                secondArraryString.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val moduleId = second_separatedBySlace[1]
            Log.v("current1", "" + moduleName + "----------------" + moduleId)

            //Fro Redirection on screens
            module_id = moduleId
            notificationType = moduleName
            fromDeepLinking = "fromDeepLinking"

            if ("PROGRAM".equals(notificationType)) {
                workout_plan_ll.callOnClick()
                fromDeepLinking = "fromDeepLinking"

            } else if ("NEWSFEED".equals(notificationType)) {

                if (module_id != null && !module_id.isEmpty()) {
                    homeTabFramgnet = homeTabFramgnet.newInstance(module_id)
                    showFragment(homeTabFramgnet)
                    module_id = ""
                    notificationType = ""
                }

                //home_ll.callOnClick()
            }
            else if ("DIET_PLAN".equals(notificationType)) {
                workout_plan_ll.callOnClick()
                fromDeepLinking = "fromDeepLinking"
            }
            else if ("Stream_workout".equals(notificationType,true)) {
                stream_layout.callOnClick()
                fromDeepLinking = "fromDeepLinking"
            }

            else if ("WORKOUT".equals(notificationType)) {

                /* workOutFramgnet = workOutFramgnet.newInstance(module_id, notificationType, isFavWorkOut, fromDeepLinking)
                 showFragment(workOutFramgnet)
                 isFavWorkOut = false
                 module_id = ""
                 notificationType = ""
                 fromDeepLinking = ""
 */
                workout_ll.callOnClick()
            } else if ("EXERCISE".equals(notificationType)) {
                workout_ll.callOnClick()
                fromDeepLinking = "fromDeepLinking"
            } else if ("CUSTOM".equals(notificationType)) {
              //  my_profile_ll.callOnClick()
                val intent = Intent(this, CustomNotificationActivity::class.java)
                intent.putExtra("FromNotificationsFromBAckAndFore", module_id)
                intent.putExtra("Notification_code", "custom")
                startActivity(intent)
                fromDeepLinking = "fromDeepLinking"
            }
        }
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val handler = Handler()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()

            val listener = object : FragmentManager.OnBackStackChangedListener,
                androidx.fragment.app.FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {
                    val fragment = supportFragmentManager.findFragmentById(R.id.container_id1)
                    if (fragment is HomeTabFragment) {
                        Handler().postDelayed({
                            actionBar1.visibility = View.VISIBLE

                        }, 400)

                    } else if (fragment is DietPCategoryDetailFragment) {
                        fragment.onResume()
                    } else if (fragment is DietPlanFragment) {
                        fragment.onResume()
                    }else if (fragment is DietPlanFragment) {
                        fragment.onResume()
                    }else if (fragment is WorkoutPlanFragment) {
                        fragment.onResume()
                    }/*else if(fragment is  WorkoutPlanDetailFragment){
                        fragment.onResume()
                    }else if(fragment is DietPlanDetailFragment){
                        fragment.onResume()
                    }*/
                }
            }
            supportFragmentManager.addOnBackStackChangedListener(listener)

        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true
            Constant.showCustomToast(this, "Click again to exit")

            handler.postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideNavigationBar()
        getDeviseVolume()

    }

    fun fromWitchScreen(fromFavWorkout: String) {
        if (fromFavWorkout.equals("fromFavWorkout")) {
            isFavWorkOut = true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.home_ll -> {
                if (clickId != R.id.home_ll) {
                    clickId = R.id.home_ll
                    allInactive()
                    iv_home.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_active_home_ico
                        )
                    )
                    tv_home.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                    if (module_id != null && !module_id.isEmpty()) {
                        homeTabFramgnet = homeTabFramgnet.newInstance(module_id)
                        showFragment(homeTabFramgnet)
                    } else
                        showFragment(homeTabFramgnet)
                    module_id = ""
                    notificationType = ""
                } else {
                    var runable: Runnable? = null
                    val handler = Handler()
                    runable = object : Runnable {
                        override fun run() {
                            homeTabFramgnet.scrollUp()
                            handler.removeCallbacks(runable!!)
                        }
                    }
                    handler.postDelayed(runable, 100)
                }
            }
            R.id.workout_ll -> {
                if (clickId != R.id.workout_ll) {
                    clickId = R.id.workout_ll
                    allInactive()
                    iv_workout.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_active_workout
                        )
                    )
                    tv_workout.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.statusBarColor = ContextCompat.getColor(
                            getActivity(),
                            R.color.colorBlack
                        )
                    }

                    val view = window.decorView
                    view.systemUiVisibility =
                        view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    if (module_id != null && !module_id.isEmpty()) {
                        workOutFramgnet = workOutFramgnet.newInstance(
                            module_id,
                            notificationType,
                            isFavWorkOut,
                            fromDeepLinking
                        )
                        showFragment(workOutFramgnet)
                    } else {
                        showFragment(workOutFramgnet)
                    }
                    isFavWorkOut = false
                    module_id = ""
                    notificationType = ""
                    fromDeepLinking = ""
                }
            }
            R.id.stream_layout -> {

                if (clickId != R.id.stream_layout) {
                    clickId = R.id.stream_layout
                    allInactive()
                    iv_stream.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_active_stram_ico
                        )
                    )
                    tv_stream.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorWhite
                        )
                    )

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(
                            ContextCompat.getColor(
                                getActivity(),
                                R.color.splash_screen_color
                            )
                        )
                    }

                    val view = window.decorView
                    view.systemUiVisibility =
                        view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    if (module_id != null && !module_id.isEmpty()) {
                      //  streamCollection = streamCollection.newInstance(module_id)
                        streamCollection1 = streamCollection1.newInstance(module_id)
                        showFragment(streamCollection1)
                    } else {
                        showFragment(streamCollection1)
                    }

                    module_id = ""
                    notificationType = ""
                    fromDeepLinking = ""
                }
            }


            R.id.diet_plan_ll -> {
                if (clickId != R.id.diet_plan_ll) {
                    clickId = R.id.diet_plan_ll

                    if (!whichScreen.isEmpty()) {
                        if (whichScreen.equals("FromAddDietPlan")) {
                            tabbar.visibility = View.GONE

                            iv_diet_plan.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_active_diet_plan
                                )
                            )
                            tv_diet_plan.setTextColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.colorWhite
                                )
                            )

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.setStatusBarColor(
                                    ContextCompat.getColor(
                                        getActivity(),
                                        R.color.splash_screen_color
                                    )
                                )
                            }

                            val view = window.decorView
                            view.systemUiVisibility =
                                view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

                            val dietPlanFragment = DietPlanFragment()
                            val args = Bundle()
                            args.putString("FromAddDietPlan", "0")
                            dietPlanFragment.setArguments(args)
                            addFragment(dietPlanFragment, R.id.container_id, false)
                            FromAddDietPlan = "0"
                            if (module_id != null && !module_id.isEmpty()) {
                                dietPlanFramgnet = dietPlanFramgnet.newInstance(module_id)
                                showFragment(dietPlanFramgnet)
                            } else {
                                showFragment(dietPlanFramgnet)
                            }
                            module_id = ""
                            notificationType = ""

                        } else {
                            allInactive()
                            iv_diet_plan.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_active_diet_plan
                                )
                            )
                            tv_diet_plan.setTextColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.colorWhite
                                )
                            )

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                window.setStatusBarColor(
                                    ContextCompat.getColor(
                                        getActivity(),
                                        R.color.splash_screen_color
                                    )
                                )
                            }

                            val view = window.decorView
                            view.systemUiVisibility =
                                view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                            if (module_id != null && !module_id.isEmpty()) {
                                dietPlanFramgnet = dietPlanFramgnet.newInstance(module_id)
                                showFragment(dietPlanFramgnet)
                            } else {
                                showFragment(dietPlanFramgnet)
                            }
                            module_id = ""
                            notificationType = ""
                        }

                    } else {
                        allInactive()
                        iv_diet_plan.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_active_diet_plan
                            )
                        )
                        tv_diet_plan.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.colorWhite
                            )
                        )

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setStatusBarColor(
                                ContextCompat.getColor(
                                    getActivity(),
                                    R.color.splash_screen_color
                                )
                            )
                        }

                        val view = window.decorView
                        view.systemUiVisibility =
                            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        if (module_id != null && !module_id.isEmpty()) {

                            dietPlanFramgnet = dietPlanFramgnet.newInstance(module_id)
                            showFragment(dietPlanFramgnet)
                        } else {
                            showFragment(dietPlanFramgnet)
                        }
                        module_id = ""
                        notificationType = ""
                    }
                }
            }
            R.id.workout_plan_ll -> {
                if (clickId != R.id.workout_plan_ll) {
                    clickId = R.id.workout_plan_ll
                    allInactive()
                    iv_workout_plan.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_active_calender_ico
                        )
                    )
                    tv_workout_plan.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorWhite
                        )
                    )

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(
                            ContextCompat.getColor(
                                getActivity(),
                                R.color.splash_screen_color
                            )
                        )
                    }

                    val view = window.decorView
                    view.systemUiVisibility =
                        view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    if (module_id != null && !module_id.isEmpty()) {
                        plansFragment = plansFragment.newInstance(module_id, notificationType, isFavWorkOut, fromDeepLinking,whichScreen)
                        showFragment(plansFragment)
                    }
                    else {
                        showFragment(plansFragment)
                    }
                    module_id = ""
                    notificationType = ""
                    fromDeepLinking = ""
                }
            }
            R.id.my_profile_ll -> {
                if (clickId != R.id.my_profile_ll) {
                    clickId = R.id.my_profile_ll
                    allInactive()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(
                            ContextCompat.getColor(
                                getActivity(),
                                R.color.splash_screen_color
                            )
                        )
                    }

                    iv_my_profile.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_active_profile
                        )
                    )
                    tv_my_profile.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorWhite
                        )
                    )
                    if (module_id != null && !module_id.isEmpty()) {
                        myPRofileFramgnet = myPRofileFramgnet.newInstance(module_id)
                        showFragment(myPRofileFramgnet)
                    } else {
                        showFragment(myPRofileFramgnet)
                    }
                    module_id = ""
                    notificationType = ""
                }
            }


            R.id.iv_navigation -> {
                if (drawer_layout.isDrawerOpen(Gravity.START)) {
                    drawer_layout.closeDrawer(Gravity.START)
                } else {
                    drawer_layout.openDrawer(Gravity.START)
                }
            }

            R.id.iv_navigation_diet -> {
                if (drawer_layout.isDrawerOpen(Gravity.START)) {
                    drawer_layout.closeDrawer(Gravity.START)
                } else {
                    drawer_layout.openDrawer(Gravity.START)
                }
            }
        }
    }
//dsfndfsjbf jdsb djbdsfgnds

    private fun allInactive() {
        iv_home.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_inactive_home_ico))
        tv_home.setTextColor(ContextCompat.getColor(this, R.color.colorGray8))

        iv_workout.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_inactive_workout))
        tv_workout.setTextColor(ContextCompat.getColor(this, R.color.colorGray8))

        iv_diet_plan.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_diet_plan
            )
        )
        tv_diet_plan.setTextColor(ContextCompat.getColor(this, R.color.colorGray8))

        iv_workout_plan.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_calender_ico
            )
        )
        tv_workout_plan.setTextColor(ContextCompat.getColor(this, R.color.colorGray8))

        iv_my_profile.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_profile
            )
        )
        tv_my_profile.setTextColor(ContextCompat.getColor(this, R.color.colorGray8))
        iv_stream.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_stram_ico
            )
        )
        tv_stream.setTextColor(ContextCompat.getColor(this, R.color.colorGray8))
    }

    private fun updateNotificationStatus(notificationId: String, type: String) {

        val param = HashMap<String, String>()
        param.put(StringConstant.type, ""+type)
        param.put("connection_id",""+notificationId)

        val header = HashMap<String, String>()
        header.put("auth-token", getDataManager().getUserInfo().customer_auth_token)

        getDataManager().readPushNotificationCount(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("resonse","response status..."+response!!.toString())
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                   // val message: String? = jsonObject?.getString(StringConstant.message)
                   /* if (success.equals("1")) {
                    }*/
                }
                override fun onError(anError: ANError) {
                    Log.d("resonse","response error..."+anError!!.toString())
                }
            })
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
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        val exerciseData =
                            getDataManager().mGson?.fromJson(
                                response.toString(),
                                FilterExerciseResponse::class.java
                            )
                        exerciseList.addAll(exerciseData!!.data)
                        getDataManager().saveFilterExerciseList(exerciseList)
                    } else {
                    }
                }

                override fun onError(anError: ANError) {}
            })
    }

    public fun updateUi(){
        myPRofileFramgnet.updateCount()
    }


    private fun startRemainingDownloadVideo() {
        Log.d("fmafklmas", "startRemainingDownloadVideo: Before ${DownloadVideosUtil.downloadId}")
        DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)?.let {
            Log.d("fmafklmas", "startRemainingDownloadVideo: SIZE ${it.size}")
            deleteDownloadLocalFile("")

            DownloadVideosUtil.dmRefIdList.let {
                Log.d("fmafklmas", "startRemainingDownloadVideo: ref id avaiable")
                DownloadVideosUtil.deleteCurrentDownloadingVideo()
                DownloadVideosUtil.dmRefIdList.clear()
            }
            StartDownloadManager.beginDownload()
        }
    }
}