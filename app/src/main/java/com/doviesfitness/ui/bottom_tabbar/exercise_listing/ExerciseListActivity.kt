package com.doviesfitness.ui.bottom_tabbar.exercise_listing

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import androidx.databinding.DataBindingUtil
import android.graphics.Point
import android.net.Uri
import android.os.*
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityExerciseListingBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.base.EndlessRecyclerViewScrollListener
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.FilterExerciseActivity
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_exercise_listing.*
import kotlinx.android.synthetic.main.exercise_listing_item_view.view.*
import kotlinx.android.synthetic.main.play_video_layout.view.*
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.net.URL


class ExerciseListActivity : BaseActivity(), ExerciseVideoPlayAdapter.OnItemClick,
    ExerciseListAdapter.StopScroll, View.OnClickListener ,
    IsSubscribed{
    override fun calculateVideoSize(
        videoUrl: String,
        uri: Uri,
        position: Int,
        holder: ExerciseVideoPlayAdapter.MyViewHolder,
        myHolder: ExerciseListAdapter.MyViewHolder,
        file: File,parentPos:Int
    ) {
        holder.loaderLayout.visibility = View.VISIBLE
       someTask(videoUrl,uri,position,holder,myHolder,file,parentPos).execute()
        Handler().postDelayed(Runnable {
            try {
                myHolder.itemView.exercise_video_rv.requestChildFocus(holder.itemView, holder.itemView.divider_view)
            }
            catch (e:Exception){
            }
        },300)

    }

    override fun stopScrolling( holder: ExerciseListAdapter.MyViewHolder) {
        try {
            if (player!=null)
                player.release()
            holder.exerciseVideoRv.removeAllViews()
            layoutManager.setScrollEnabled(true)
        }
        catch (e:Exception){
            layoutManager.setScrollEnabled(true)
        }

    }

    override fun isSubscribed() {
        startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home","no"))
    }
/*
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
                    if (selectedList.get(i).exercise_id.equals(exercise.exercise_id)){
                        selectedList.removeAt(i)
                    }
                }
            }
        }
    }
*/

    override fun videoDownload(
        isScroll: Boolean,
        data:ExerciseListingResponse.Data,
        view: ExerciseVideoPlayAdapter.MyViewHolder,
        view1: ExerciseListAdapter.MyViewHolder,
        position: Int,isLoad: Boolean,parentPos:Int
    ) {


        isDownload=isLoad
        urlDownload=data.exercise_video

        view1.itemView.exercise_video_rv.requestChildFocus(view.itemView, view.itemView.divider_view)
        if (isDownload) {
            try {
                downloadExercise(data.exercise_video, position, view,view1, isScroll,parentPos)
            }catch (e:Exception){
            }



          /*  Handler().postDelayed(Runnable {
                try {
                    view1.itemView.exercise_video_rv.requestChildFocus(view.itemView, view.itemView.divider_view)
                }catch (e:Exception){}
            },500)
*/

            try {
                h3.removeCallbacks(R3!!)
                h3 = Handler()
                R3 = Runnable({
                    try {
                        view1.itemView.exercise_video_rv.requestChildFocus(view.itemView, view.itemView.divider_view)
                        h3.removeCallbacks(R3!!)
                    }
                    catch (e:Exception){}
                })
                h3.postDelayed(R3!!, 500)
            }
            catch (e:Exception){

            }


          /*  Handler().postDelayed(Runnable {
              try {
                  layoutManager.setScrollEnabled(false)
              }
              catch (e:Exception){ }
            },1000)
*/
            try {
                h4.removeCallbacks(R4!!)
                h4 = Handler()
                R4 = Runnable({
                    try {
                        layoutManager.setScrollEnabled(false)
                        h4.removeCallbacks(R4!!)
                    }
                    catch (e:Exception){}
                })
                h4.postDelayed(R4!!, 1000)
            }
            catch (e:Exception){

            }
        }
        else{
            layoutManager.setScrollEnabled(true)
        }
    }



    override fun videoPlayClick(
        isScroll: Boolean,
        data: Uri,
        position: Int,
        view: ExerciseVideoPlayAdapter.MyViewHolder,
        view1: ExerciseListAdapter.MyViewHolder,
        isLoad: Boolean,parentPos:Int
    ) {
        isDownload=isLoad
        urlDownload=""

        intializePlayer(data, position, view, view1,parentPos)
        try {
            h1.removeCallbacks(R1!!)
            h1 = Handler()
            R1 = Runnable({
                try {
                    view1.itemView.exercise_video_rv.requestChildFocus(
                        view.itemView,
                        view.itemView.divider_view
                    )
                    h1.removeCallbacks(R1!!)
                }
                catch (e:Exception){}
            })
            h1.postDelayed(R1!!, 300)
        }
        catch (e:Exception){

        }
        ////
        try {
            h2.removeCallbacks(R2!!)
            h2 = Handler()
            R2 = Runnable({
                try {
                    layoutManager.setScrollEnabled(false)
                    h2.removeCallbacks(R2!!)
                }
                catch (e:Exception){}
            })
            h2.postDelayed(R2!!, 700)
        }
        catch (e:Exception){
        }

      /* Handler().postDelayed(Runnable {
           try {
               layoutManager.setScrollEnabled(false)
           }
           catch (e:Exception){
              // layoutManager.setScrollEnabled(false)
           }
        },700)*/

    }
    private var R1: Runnable? = null
    var h1=Handler()
    private var R2: Runnable? = null
    var h2=Handler()
    private var R3: Runnable? = null
    var h3=Handler()
    private var R4: Runnable? = null
    var h4=Handler()


/*
    override fun shareURL(data: ExerciseListingResponse.Data) {
        sharePost(data.exercise_share_url)
    }
*/
    var isPlayingFlag=false
    private lateinit var binding: ActivityExerciseListingBinding;
    private lateinit var adapter: ExerciseListAdapter;
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
    lateinit var  layoutManager:CustomLinearLayoutManager
        ///////for downloading
    internal lateinit var mBroadCastReceiver: BroadcastReceiver
    var isRecieverRegistered = false
    internal var mFinishedFilesFromNotif = ArrayList<Long>()
    lateinit var mProgressThread: Thread
    var isDownloadSuccess = false
    var isDownload = false
    var urlDownload = ""
    var keyword = ""
    var videowidth = 0
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private var trackSelector: DefaultTrackSelector? = null
  //  private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()

    companion object{
     public  var flag=true
        var curroptedFile=File("")
        var curroptedUrl=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideNavStatusBar()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_listing)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        create = intent.getStringExtra("create")!!
        if (create != null && !create.isEmpty()) {
            binding.doneBtn.visibility = View.VISIBLE
        } else {
            create = ""
            binding.doneBtn.visibility = View.GONE
        }

        if (intent.getStringExtra("category_id") != null && intent.getStringExtra("category_id")!!.isEmpty()) {
            // startActivityForResult(Intent(getActivity(),FilterExerciseActivity::class.java),101)
            exercise_name.setText(getString(R.string.exercises_library))


            if (intent.getStringExtra("from") != null && !intent.getStringExtra("from")!!.isEmpty() && intent.getStringExtra(
                    "from"
                ).equals("search")
            ) {
                from = intent.getStringExtra("from")!!;
                var list = ArrayList<FilterExerciseResponse.Data.X>()
                list = intent.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
                keyword=""
                for (i in 0..list.size - 1) {
                    if (list.get(i).group_key.equals(StringConstant.filter_level, true)) {
                        filterLevel = filterLevel + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_tags, true)) {
                        filterTags = filterTags + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_body_part, true)) {
                        filterBodyPart = filterBodyPart + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_equipments, true)) {
                        filterEquipments = filterEquipments + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_good_for, true)) {
                        filterGoodFor = filterGoodFor + list.get(i).id + ","
                    } else if (list.get(i).group_key.equals(StringConstant.filter_exercises, true)) {
                        filterExercises = filterExercises + list.get(i).id + ","
                    } else {

                    }
                }
            }
            else if(intent.getStringExtra("keyword") != null && !intent.getStringExtra("keyword")!!.isEmpty()){
                keyword=intent.getStringExtra("keyword")!!
            }
            else {
                keyword=""
                tempList = intent.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
                for (i in 0..tempList.size - 1) {
                    if (tempList.get(i).group_key.equals(StringConstant.filter_level, true)) {
                        filterLevel = filterLevel + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_tags, true)) {
                        filterTags = filterTags + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_body_part, true)) {
                        filterBodyPart = filterBodyPart + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_equipments, true)) {
                        filterEquipments = filterEquipments + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_good_for, true)) {
                        filterGoodFor = filterGoodFor + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_exercises, true)) {
                        filterExercises = filterExercises + tempList.get(i).id + ","
                    } else {

                    }
                }
                btn_clear_filter.visibility = View.VISIBLE
              //  txt_filter.visibility = View.VISIBLE
            }

            // Constant.intent(getActivity(),"size.."+tempList.size)
            exerciseListing.clear()
            page = 1

            getExerciseListingData(1)

        }
        else {
            keyword=""
            categoryId = intent.getStringExtra("category_id")!!
            name = intent.getStringExtra("name")!!
            val Str: String = name.toUpperCase()
            exercise_name.setText(Str)
            binding.progressLayout.visibility = View.VISIBLE
            getExerciseListingData(page)
        }
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
        btn_clear_filter.setOnClickListener(this)
        filter_icon.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        done_btn.setOnClickListener(this)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320

         videowidth = 120 + (179 * screenWidth)

        if (create != null && !create.isEmpty())
            adapter = ExerciseListAdapter(getActivity(), exerciseListing, this,this,"",this)
        else
            adapter = ExerciseListAdapter(getActivity(), exerciseListing, this,this,"",this)
        //val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
         layoutManager = CustomLinearLayoutManager(getActivity())
      //  binding.exerciseRv.setNestedScrollingEnabled(false);
       // binding.exerciseRv.setHasFixedSize(false);
        binding.exerciseRv.layoutManager = layoutManager
        binding.exerciseRv.adapter = adapter

        binding.exerciseRv.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (page != 0 && nextPage == 1) {
                    adapter.showLoading(true)
                    adapter.notifyItemChanged(exerciseListing.size - 1)
                    //adapter.notifyDataSetChanged()
                    getExerciseListingData(page)
                }
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.filter_icon -> {
                keyword=""
                filterLevel = "";filterBodyPart = "";filterEquipments = "";filterExercises = "";filterGoodFor =
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
            R.id.iv_back -> {
                if (tempList.size != 0)
                    tempList.clear()
                onBackPressed()
            }
            R.id.done_btn -> {
                getSelectedExercise()

            }
            R.id.btn_clear_filter -> {
                clearFilterData()
                if (intent.getStringExtra("category_id") != null && intent.getStringExtra("category_id")!!.isEmpty()) {

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

    public fun getSelectedExercise() {
        /* selectedList.clear()
         for (i india 0..exerciseListing.size-1){
             if (exerciseListing.get(i).isSelected==true){
                 selectedList.add(exerciseListing.get(i))
             }
         }*/
        val intent = Intent()
        intent.putExtra("list", selectedList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun clearFilterData() {
// Set blank india filter paramater
        filterLevel = ""
        filterExercises = ""
        filterTags = ""
        filterBodyPart = ""
        filterEquipments = ""
        filterGoodFor = ""
        keyword=""
        tempList.clear()
        exerciseListing.clear()
        btn_clear_filter.visibility = View.GONE
       // txt_filter.visibility = View.GONE
        getExerciseListingData(1)

    }

    /*
    * URL==https://dev.doviesfitness.com/WS/exercise_listing
Param===Optional(["filter_good_for": "", "filter_tags": "", "auth_customer_subscription": "Paid", "filter_body_part": "", "auth_customer_id": "523a72640c710ec076bae3fa5e7e0ca5e1b34a02f3e1fd8b1ba5216acf425db1", "page_index": "1", "filter_equipments": "1,27,23,4,16,20,25,24,14,10,17,13,2", "device_token": diSB317yNW8:APA91bF5NJb84Sohi0693mH07LIFDLKBRW4NrUAOuQPgp3tiaSB9gz7R0YGj9AA-yS2qQxTKyYCIvorPJ11zb808gryzrL2Tm3ssc2tn0_gYjeQt2e6hpNjGcj9Ccm8r0xJcCxbI7RvT, "filter_exercises": "", "device_type": "Ios", "device_id": "7565183F-C7F9-40C1-B021-13513F00E585", "filter_level": ""])
headers===Optional(["APIVERSION": "1", "AUTHTOKEN": "523a72640c710ec076bae3fa5e7e0ca5e1b34a02f3e1fd8b1ba5216acf425db1", "APIKEY": "HBDEV"])
    *
    * */

    private fun getExerciseListingData(pageCount: Int) {

        if(CommanUtils.isNetworkAvailable(this)!!){
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
            var customerType=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_USER_TYPE)
            if (customerType!=null&& !customerType!!.isEmpty()){
                param.put(StringConstant.auth_customer_subscription, customerType)
            }
            else{
                param.put(StringConstant.auth_customer_subscription, "free")
            }
            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().exerciseDetailListApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    val next_page = jsonObject?.getString(StringConstant.next_page)
                    nextPage = next_page!!.toInt()
                    Log.d("response", "response......" + response.toString(4))


                    binding.progressLayout.visibility = View.GONE
                    if (success.equals("1")) {
                        no_record_found.visibility = View.GONE
                        no_record_icon.visibility = View.GONE
                        exercise_rv.visibility = View.VISIBLE

                        val exercisesListData =
                            getDataManager().mGson?.fromJson(response.toString(), ExerciseListingResponse::class.java)
                        exerciseListing.addAll(exercisesListData!!.`data`);
                        //  downloadExercises()
                        //   page=page+1
                        //nextPage = exercisesListData.settings.next_page.toInt()
                        hideFooterLoiader(exercisesListData!!.`data`.size)
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
                    Constant.showCustomToast(binding.exerciseRv.context!!, getString(R.string.something_wrong))
                }
            })
        }else{
            Constant.showInternetConnectionDialog(this)
        }
    }

    private fun hideFooterLoiader(itemSize: Int = 0) {
        adapter.showLoading(false)
       // isPlayingFlag=true
       // adapter.notifyDataSetChanged()
       adapter.notifyItemRangeInserted(exerciseListing.size-2,itemSize);
        //binding.svMain.setEnableScrolling(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101 && data != null) {
                tempList = data.getSerializableExtra("list") as ArrayList<FilterExerciseResponse.Data.X>
                // Constant.showCustomToast(getActivity(),"size.."+tempList.size)
                for (i in 0..tempList.size - 1) {
                    if (tempList.get(i).group_key.equals(StringConstant.filter_level, true)) {
                        filterLevel = filterLevel + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_tags, true)) {
                        filterTags = filterTags + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_body_part, true)) {
                        filterBodyPart = filterBodyPart + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_equipments, true)) {
                        filterEquipments = filterEquipments + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_good_for, true)) {
                        filterGoodFor = filterGoodFor + tempList.get(i).id + ","
                    } else if (tempList.get(i).group_key.equals(StringConstant.filter_exercises, true)) {
                        filterExercises = filterExercises + tempList.get(i).id + ","
                    } else {

                    }
                }
                keyword=""
                page = 1
                exerciseListing.clear()
                adapter.notifyDataSetChanged()
                btn_clear_filter.visibility = View.VISIBLE
               // txt_filter.visibility = View.VISIBLE
                getExerciseListingData(1)

            }
        } else if (resultCode == 101) {
            if (categoryId.isEmpty())
                onBackPressed()
        }
    }

    fun downloadExercise(
        videoUrl: String,
        position: Int,
        view: ExerciseVideoPlayAdapter.MyViewHolder,
        view1: ExerciseListAdapter.MyViewHolder,
        scroll: Boolean,
        parentPos: Int
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
            request.setDestinationInExternalFilesDir(getActivity(), "/." + Environment.DIRECTORY_DOWNLOADS, subpath)
            refid = downloadManager!!.enqueue(request)
            list.add(refid)
            startDownloadThread(videoUrl,position,view,view1,scroll,parentPos)
        }

    }


   /* override fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView) {

    }

    override fun setFavUnfavForExercies(data: ExerciseListingResponse.Data, position: Int, view: ImageView) {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.module_name, StringConstant.EXERCISE)
        param.put(StringConstant.module_id, data.exercise_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.AUTHTOKEN, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {

                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    if (data.exercise_is_favourite.equals("0")) {
                        data.exercise_is_favourite = "1"
                        view.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_star_active))
                    } else {
                        data.exercise_is_favourite = "0"
                        view.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_star))
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

*/

    private fun startDownloadThread(
        videoUrl: String,
        position: Int,
        view: ExerciseVideoPlayAdapter.MyViewHolder,
        view1: ExerciseListAdapter.MyViewHolder,
        scroll: Boolean,parentPos:Int
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
                            if (isDownload&&urlDownload.equals(videoUrl,true)){
                                var uri = Uri.parse(path)
                                exerciseListing.get(position).isClicked = false
                                intializePlayer(uri, position, view, view1, parentPos)


                                try {
                                    h1.removeCallbacks(R1!!)
                                    h1 = Handler()
                                    R1 = Runnable({
                                        try {
                                            view1.itemView.exercise_video_rv.requestChildFocus(
                                                view.itemView,
                                                view.itemView.divider_view
                                            )
                                            h1.removeCallbacks(R1!!)
                                        }
                                        catch (e:Exception){}
                                    })
                                    h1.postDelayed(R1!!, 300)
                                }
                                catch (e:Exception){

                                }



/*
                                Handler().postDelayed(Runnable {
                                    try {
                                        view1.itemView.exercise_video_rv.requestChildFocus(view.itemView, view.itemView.divider_view)
                                    }
                                    catch (e:Exception){
                                      //  layoutManager.setScrollEnabled(true)
                                    }
                                },300)
*/



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
    inner class someTask(
        exercise_video: String,
        uri: Uri,
        position: Int,
        holder: ExerciseVideoPlayAdapter.MyViewHolder,
        myHolder: ExerciseListAdapter.MyViewHolder,
        f: File,parentPos:Int

    ) : AsyncTask<Void, Void, Boolean>() {
        var exercise_video1 = exercise_video
        var uri = uri
        var position = position
        var holder = holder
        var file = f
        var myHolder = myHolder
        var parentPos=parentPos
      //  var exercise = exercise


        override fun doInBackground(vararg params: Void?): Boolean? {
            var url = URL(exercise_video1);
            var urlConnection = url.openConnection();
            urlConnection.connect();
            var file_size = urlConnection.getContentLength();
            Log.e("video size", "video size url..." + file_size)
            Log.e("video size", "video size file..." + file.length())

            getDataManager().setStringData(file.absolutePath, file_size.toString())
            //  exercise.videoLength=file_size.toLong()
            if (file_size.toLong() == file.length()) {
                return true
            } else {
                return false
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            holder.loaderLayout.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)


            if (result!!) {
             /*   holder.loaderLayout.visibility = View.GONE
                if (tempPosition == position)*/
                intializePlayer(uri, position, holder, myHolder, parentPos)
                try {
                    myHolder.itemView.exercise_video_rv.requestChildFocus(holder.itemView, holder.itemView.divider_view)
                }
                catch (e:Exception){}
                Handler().postDelayed(Runnable {
                    try {
                      layoutManager.setScrollEnabled(false)
                    }
                    catch (e:Exception){

                    }
                },300)

            } else {
                file.delete()
                holder.loaderLayout.visibility = View.VISIBLE
                downloadExercise(exercise_video1, position, holder, myHolder, false, parentPos)
                Handler().postDelayed(Runnable {
                    try {
                        myHolder.itemView.exercise_video_rv.requestChildFocus(holder.itemView, holder.itemView.divider_view)
                    }
                    catch (e:Exception){
                    }
                },300)

            }
        }
    }
    public fun intializePlayer(
        news_video: Uri,
        position: Int,
        holder: ExerciseVideoPlayAdapter.MyViewHolder,
        myHolder: ExerciseListAdapter.MyViewHolder,
        parentPos: Int
    ) {
        try {
            if (player != null)
                player.release()
            holder.fl_vv.removeAllViews()
        }
        catch (e:Exception){

        }

        var playerView: PlayerView
        playerView = PlayerView(getActivity())
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        try {
            if (exerciseListing.size != 0 && exerciseListing.size != -1) {
                //  layoutManager.setScrollEnabled(false)
                holder.loaderLayout.visibility=View.GONE
                holder.fl_vv.visibility=View.VISIBLE
                holder.fl_vv.addView(playerView)
                holder.fl_vv.requestFocus()
                playerView.setOnClickListener {
                      adapter.stopVideo(myHolder,parentPos)
                    holder.main_layout.performClick()
                }
                playerView.layoutParams.height = videowidth
                playerView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
                mediaDataSourceFactory = DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"))
                val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(news_video)
                player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector!!)
                with(player) {
                    prepare(mediaSource, false, false)
                    playWhenReady = true
                    repeatMode =
                        com.google.android.exoplayer2.Player.REPEAT_MODE_ONE //repeat play video
                    setVideoScalingMode(com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                playerView.setShutterBackgroundColor(
                    ContextCompat.getColor(
                        getActivity(),
                        R.color.colorBlack
                    )
                )
                playerView.player = player
                playerView.requestFocus()
             //   holder.ivHideControllerButton.visibility = View.GONE

/*
                playerView.setPlaybackPreparer(object:PlaybackPreparer{
                    override fun preparePlayback() {
                       Log.d("Playback","Playback")
                    }

                })
*/

                player.addListener(object:Player.EventListener{
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        Log.d("Playback","Playback isPlaying...."+isPlaying)
                    }

                    override fun onLoadingChanged(isLoading: Boolean) {
                        Log.d("Playback","Playback isLoading...."+isLoading)

                    }

                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        Log.d("Playback","Playback playWhenReady...."+playWhenReady+"...playbackState..."+playbackState)
/*
                        if (playbackState==1){
                            if (curroptedFile.exists())
                            curroptedFile.delete()
                          //  holder.loaderLayout.visibility = View.VISIBLE
                            downloadExercise(curroptedUrl, position, holder, myHolder, false, parentPos)
                        }
*/
                    }
                })

                playerView.setShowBuffering(false)
                // myHolder.itemView.exercise_video_rv.requestChildFocus(holder.itemView, holder.itemView.divider_view)

               /* holder.ivHideControllerButton.setOnClickListener {
                    // playerView.hideController()
                }
          */
               // lastSeenTrackGroupArray = null
            }
        }
        catch (e:Exception){

        }


    }


}