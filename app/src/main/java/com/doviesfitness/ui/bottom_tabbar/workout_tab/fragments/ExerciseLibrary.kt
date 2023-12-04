package com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.ExerciseLibraryAdapter
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseLibResponse
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import com.doviesfitness.utils.StringConstant.Companion.Android
import org.json.JSONObject
import com.doviesfitness.databinding.FragmentExerciseLibraryBinding
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ExerciseDetailListActivityNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.utils.CommanUtils
import java.io.File

private const val ARG_PARAM1 = "param1"

class ExerciseLibrary : BaseFragment(), ExerciseLibraryAdapter.OnItemCLick {

    private var page: Int = 1
    private var nextPage: Int = 0
    private var param1 = ""
    private var param2: String? = null
    public lateinit var binding: FragmentExerciseLibraryBinding
    private lateinit var adapter: ExerciseLibraryAdapter
    public var exerciseList = ArrayList<ExerciseLibResponse.Data>()
    private var allExerciseListing = ArrayList<ExerciseListingResponse.Data>()
    private var downloadManager: DownloadManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_library, container, false)
        downloadManager = mActivity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        initialization()
        return binding.root
    }

    private fun initialization() {

        adapter = ExerciseLibraryAdapter(mContext, exerciseList, param1, this, binding.hideView)
        val loutManager = androidx.recyclerview.widget.GridLayoutManager(mContext, 2);
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen._1sdp)
        binding.exerciseRv.addItemDecoration(MyItemDecoration(spacingInPixels, binding.hideView))
        binding.exerciseRv.layoutManager = loutManager;
        binding.exerciseRv.adapter = adapter


        getExerciseData(page)
        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            exerciseList.clear()
            allExerciseListing.clear()
            getExerciseData(page)
        }


        binding.excericesMain.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage == 1) {
                            adapter.notifyDataSetChanged()
                            getExerciseData(page)
                        }
                    }
                }
            }
        })

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onItemClick(exercise_category_id: String, display_name: String) {
        activity!!.startActivityForResult(
            Intent(mContext, ExerciseDetailListActivityNew::class.java)
                .putExtra("category_id", exercise_category_id)
                .putExtra("name", display_name)
                .putExtra("create", param1), 7
        )
    }

    private fun getExerciseData(page: Int) {


        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            val param = HashMap<String, String>()
            param.put(StringConstant.device_token, "")
            param.put(StringConstant.device_id, "")
            param.put(StringConstant.device_type, Android)
            param.put(StringConstant.page_index, "" + page)
            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")
            getDataManager().exerciseLibraryApi(param, header)
                ?.getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        binding.swipeRefresh.isRefreshing = false
                        val jsonObject: JSONObject? =
                            response?.getJSONObject(StringConstant.settings)
                        val success: String? = jsonObject?.getString(StringConstant.success)
                        val message: String? = jsonObject?.getString(StringConstant.message)
                        val next_page = jsonObject?.getString(StringConstant.next_page)
                        nextPage = next_page!!.toInt()
                        Log.v("nextPage", "" + nextPage)
                        if (success.equals("1")) {
                            //  Constant.showCustomToast(mContext!!, "" + message)
                            val exercises = getDataManager().mGson?.fromJson(
                                response.toString(),
                                ExerciseLibResponse::class.java
                            )
                            exerciseList.addAll(exercises!!.data);
                            adapter.notifyDataSetChanged()
                            // getExerciseListingData(0)
                            //  Runnable { getExerciseListingData(0) }


                        } else {
                            // Constant.showCustomToast(mContext!!, "" + message)
                        }
                    }

                    override fun onError(anError: ANError) {
                        binding.swipeRefresh.isRefreshing = false
                        Constant.errorHandle(anError, activity!!)
                        Constant.showCustomToast(mContext!!, getString(R.string.something_wrong))
                    }
                })
        } else {
            binding.swipeRefresh.setRefreshing(false)
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }

    class MyItemDecoration(space: Int, view: View) :
        androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val space: Int

        init {
            this.space = space / 2
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            } else {
                outRect.top = space * 2
            }
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.right = space
            } else {
                outRect.left = space
            }
        }
    }

    ///////
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            ExerciseLibrary().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


    ///////////////////////////
    private fun getExerciseListingData(id: Int) {
        var idCount = id;
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.page_index, "" + 1)
        param.put(StringConstant.auth_customer_id, "")
        param.put(StringConstant.filter_level, "")
        param.put(StringConstant.filter_exercises, "")
        param.put(StringConstant.filter_tags, "")
        param.put(StringConstant.filter_body_part, "")
        param.put(StringConstant.filter_equipments, "")
        param.put(
            StringConstant.filter_category,
            "" + exerciseList.get(idCount).exercise_category_id
        )
        param.put(StringConstant.filter_good_for, "")
        param.put("filter_keyword", "")
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

        getDataManager().exerciseDetailListApi(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    if (success.equals("1")) {
                        //  Log.d("response", "response...list data..." + response!!.toString(4))
                        // Constant.showCustomToast(binding.exerciseRv.context!!, "" + message)
                        val exercisesListData = getDataManager().mGson?.fromJson(
                            response.toString(),
                            ExerciseListingResponse::class.java
                        )
                        allExerciseListing.addAll(exercisesListData!!.`data`)
                        downloadExercises()
                        idCount = idCount + 1
                        if (idCount < exerciseList.size)
                            getExerciseListingData(idCount)
                        //  Runnable {  getExerciseListingData(idCount)}
                        //  downloadExercises()
                        //   page=page+1
                        //  nextPage = exercisesListData.settings.next_page.toInt()
                    }
                }

                override fun onError(anError: ANError) {

                }
            })
    }

    fun downloadExercises() {
        for (i in 0..allExerciseListing.size - 1) {
            if (allExerciseListing.get(i) != null) {
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                        "Yes"
                    ) ||
                    allExerciseListing.get(i).exercise_access_level.equals("OPEN")
                ) {
                    val lastIndex = allExerciseListing.get(i).exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName =
                            allExerciseListing.get(i).exercise_video.substring(lastIndex + 1)
                        val subpath = "/Dovies//$downloadFileName"
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                    mActivity!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
                        val f = File(path)
                        if (!f.exists()) {
                            val Download_Uri = Uri.parse(allExerciseListing.get(i).exercise_video)
                            val request = DownloadManager.Request(Download_Uri)
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                            request.setAllowedOverRoaming(false)
                            request.setTitle("Dovies Downloading $i.mp4")
                            request.setDescription("Downloading $i.mp4")
                            request.setVisibleInDownloadsUi(false)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                            if (subpath != null) {
                                request.setDestinationInExternalFilesDir(
                                    getActivity(),
                                    "/." + Environment.DIRECTORY_DOWNLOADS,
                                    subpath
                                )
                                downloadManager!!.enqueue(request)
                            }

                        }
                    } else {

                    }
                }
            }
        }
    }
}