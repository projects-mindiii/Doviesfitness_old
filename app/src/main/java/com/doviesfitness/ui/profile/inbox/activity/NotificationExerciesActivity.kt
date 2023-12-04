package com.doviesfitness.ui.profile.inbox.activity

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityNotificationExerciesBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.WorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.ui.profile.inbox.addapter.NotificationExerciseDetailAdapter
import com.doviesfitness.ui.profile.inbox.modal.NotificationExerciseModel
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_exercise_detaillist.*
import org.json.JSONObject
import java.io.File


class NotificationExerciesActivity : BaseActivity(), NotificationExerciseDetailAdapter.OnItemClick,
    View.OnClickListener , IsSubscribed {
    override fun isSubscribed() {
        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }

    private lateinit var binding: ActivityNotificationExerciesBinding
    //private lateinit var adapter: ExerciseDetailAdapter;
    private lateinit var adapter: NotificationExerciseDetailAdapter;
    private var exerciseListing = ArrayList<NotificationExerciseModel.Data>()
    private var selectedList = ArrayList<NotificationExerciseModel.Data>()

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_exercies)
        initialization()
    }

    private fun initialization() {

        if (intent.getStringExtra("category_id") != null) {
            categoryId = intent.getStringExtra("category_id")!!
            name = intent.getStringExtra("name")!!
            val Str: String = name.toUpperCase()
            exercise_name.setText(Str)
            binding.progressLayout.visibility = View.VISIBLE
            getExerciseListingData(page)
        }

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

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

        val videowidth = 120 + (160 * screenWidth)


        adapter = NotificationExerciseDetailAdapter(getActivity(), exerciseListing, this, videowidth, "",this)
        val layoutManager = LinearLayoutManager(getActivity())
        binding.exerciseRv.layoutManager = layoutManager
        binding.exerciseRv.adapter = adapter


       /* swipe_refresh.setOnRefreshListener {
            page = 1
            exerciseListing.clear()
            getExerciseListingData(page)
        }*/
/*
        binding.exerciseRv.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (page != 0 && nextPage > 0 && page < nextPage) {
                    adapter.showLoading(true)

                    getExerciseListingData(page)
                }
            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2 && data != null) {
                page = 1
                exerciseListing.clear()
                getExerciseListingData(page)
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun videoPlayClick(
        isScroll: Boolean,
        data: WorkoutExercise,
        position: Int,
        view: WorkoutAdapter.MyViewHolder
    ) {

    }


    private fun getExerciseListingData(pageCount: Int) {

        val param = HashMap<String, String>()
        param.put(StringConstant.page_index, "" + pageCount)
        param.put(StringConstant.auth_customer_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.exercise_id, categoryId)
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_token, "")

        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")

        getDataManager().getExercisesDetail(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
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
                    val notificationExerciseModel =
                        getDataManager().mGson?.fromJson(response.toString(), NotificationExerciseModel::class.java)
                    exerciseListing.addAll(notificationExerciseModel!!.data);
                    //   page=page+1
                    //nextPage = notificationExerciseModel.settings.next_page.toInt()
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
                Constant.showCustomToast(binding.exerciseRv.context!!, getString(R.string.something_wrong))
            }
        })
    }

    private fun hideFooterLoiader() {
        adapter.showLoading(false)
        adapter.notifyDataSetChanged()
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

    override fun videoPlayClick(
        isScroll: Boolean,
        data: NotificationExerciseModel.Data,
        position: Int,
        view: NotificationExerciseDetailAdapter.MyViewHolder,
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

    override fun shareURL(data: NotificationExerciseModel.Data) {
        sharePost(data.exercise_share_url)
    }

    override fun setFavUnfavForExercies(data: NotificationExerciseModel.Data, position: Int, view: ImageView) {
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

    override fun setSelected(position: Int, isSelected: Boolean, exercise: NotificationExerciseModel.Data) {
        if (isSelected == true) {
            selectedList.add(exercise)
        } else {
            if (selectedList.size > 0) {
                for (i in 0..selectedList.size - 1) {
                    if (selectedList.get(i).exercise_id.equals(exercise.exercise_id)) {
                        selectedList.removeAt(i)
                    }
                }
            }
        }
    }


}
