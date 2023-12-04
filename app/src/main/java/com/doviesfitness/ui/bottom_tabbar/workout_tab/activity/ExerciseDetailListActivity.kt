package com.doviesfitness.ui.bottom_tabbar.workout_tab.activity

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.databinding.ActivityExerciseDetaillistBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.WorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.ExerciseDetailAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_exercise_detaillist.*
import org.json.JSONObject
import java.io.File


class ExerciseDetailListActivity : BaseActivity(), ExerciseDetailAdapter.OnItemClick, View.OnClickListener ,
    IsSubscribed{
    override fun isSubscribed() {
        startActivity(Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home","no"))
    }

    override fun setSelected(position: Int, isSelected: Boolean, exercise: ExerciseListingResponse.Data) {
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

    override fun videoPlayClick(
        isScroll: Boolean,
        data: WorkoutExercise,
        position: Int,
        view: WorkoutAdapter.MyViewHolder
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shareURL(data: ExerciseListingResponse.Data) {
        sharePost(data.exercise_share_url)
    }

    private lateinit var binding: ActivityExerciseDetaillistBinding;
    private lateinit var adapter: ExerciseDetailAdapter;
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideNavStatusBar()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_detaillist)
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
            } else {
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
                txt_filter.visibility = View.VISIBLE
            }

            // Constant.intent(getActivity(),"size.."+tempList.size)
            exerciseListing.clear()
            page = 1

            getExerciseListingData(1)

        }
        else {
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
                    R.color.splash_screen_color
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

        val videowidth = 120 + (179 * screenWidth)

        if (create != null && !create.isEmpty())
            adapter = ExerciseDetailAdapter(getActivity(), exerciseListing, this, videowidth, create,this)
        else
            adapter = ExerciseDetailAdapter(getActivity(), exerciseListing, this, videowidth, "",this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
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
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
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


    override fun videoPlayClick(
        isScroll: Boolean,
        data: ExerciseListingResponse.Data,
        position: Int,
        view: ExerciseDetailAdapter.MyViewHolder,
        isLoad: Boolean
    ) {
        for (i in 0 until exerciseListing.size) {
            if (exerciseListing.get(i).isPlaying) {
                exerciseListing.get(i).isPlaying = false
                binding.svMain.setEnableScrolling(true)
            } else {
                exerciseListing.get(i).isPlaying = true
                binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)
                binding.svMain.setEnableScrolling(false)
            }
        }
        adapter.notifyDataSetChanged()
        if (isLoad) {
            downloadExercise(data.exercise_video)
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
        tempList.clear()
        exerciseListing.clear()
        btn_clear_filter.visibility = View.GONE
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

        Log.d(
            "category data",
            "category..." + "...categoryId..." + categoryId + "...filterLevel.." + filterLevel + "...filterExercises..."
                    + filterExercises + "...filterTags..." + filterTags + "..filterBodyPart..." + filterBodyPart + "...filterEquipments..." +
                    "." + filterEquipments + "...filterGoodFor..." + filterGoodFor
        )
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
                binding.progressLayout.visibility = View.GONE
                if (success.equals("1")) {
                    no_record_found.visibility = View.GONE
                    no_record_icon.visibility = View.GONE
                    exercise_rv.visibility = View.VISIBLE
                   // swipe_refresh.isRefreshing = false

                    Log.d("response", "response...list data..." + response!!.toString(4))
                    // Constant.showCustomToast(binding.exerciseRv.context!!, "" + message)
                    val exercisesListData =
                        getDataManager().mGson?.fromJson(response.toString(), ExerciseListingResponse::class.java)
                    exerciseListing.addAll(exercisesListData!!.`data`);
                    //   page=page+1
                    nextPage = exercisesListData.settings.next_page.toInt()
                    Log.d("response", "nextPage......" + nextPage)
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
                  Constant.errorHandle(anError,getActivity())
              //  Constant.showCustomToast(binding.exerciseRv.context!!, getString(R.string.something_wrong))
            }
        })
    }

    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
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
        }
    }

    fun downloadExercise(videoUrl: String) {
        list.clear()
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
        }

    }


    override fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView) {

    }

    override fun setFavUnfavForExercies(data: ExerciseListingResponse.Data, position: Int, view: ImageView) {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.module_name, StringConstant.exercise)
        param.put(StringConstant.module_id, data.exercise_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
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





}