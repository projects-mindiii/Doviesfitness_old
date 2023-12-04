package com.doviesfitness.ui.profile.favourite.fragment

import android.app.Activity
import androidx.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.adapter.WorkoutAdapter
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.profile.favourite.modal.FavExerciseModel
import com.doviesfitness.utils.CommanUtils
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import kotlinx.android.synthetic.main.activity_home_tab.*
import org.json.JSONObject
import java.lang.Exception


class ExerciseFragment : BaseFragment(), FavExerciesAdapter.OnItemClick, View.OnClickListener {

    lateinit var binding: com.doviesfitness.databinding.FragmentExerciseBinding
    lateinit var adapter: FavExerciesAdapter
    lateinit var exerciseList: ArrayList<FavExerciseModel.Data>
    private lateinit var homeTabActivity: HomeTabActivity
    internal var list = ArrayList<Long>()
    private var page: Int = 1
    private var mLastClickTime: Long = 0
    private var nextPage: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, container, false)
        initialization()
        return binding.root
    }

    private fun initialization() {
        homeTabActivity = activity as HomeTabActivity
        exerciseList = ArrayList()
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        //  val width = size.x
        //  val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320
        val videowidth = 120 + (160 * screenWidth)

        /*Set Adapter*/
        adapter = FavExerciesAdapter(mContext, exerciseList, this, videowidth,"")
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        binding.myFavExerciesRv.layoutManager = layoutManager
        binding.myFavExerciesRv.adapter = adapter

        getFavWorkoutData(page)

        setOnClick(binding.btnAddExercise)


      /*  binding.myFavExerciesMain.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int) {
                if (v!!.getChildAt(v!!.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page = page + 1
                        if (page != 0 && nextPage == 1) {
                            adapter.showLoading(true)
                            adapter.notifyDataSetChanged()
                            getFavWorkoutData(page)
                        }
                    }
                }
            }
        })*/


    }

    private fun setOnClick(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_add_exercise -> {
                homeTabActivity.onResume()
                homeTabActivity.workout_ll.callOnClick()
            }
        }
    }

    private fun getFavWorkoutData(page: Int) {

        if (CommanUtils.isNetworkAvailable(mContext)!!) {

            val param = HashMap<String, String>()
            param.put(StringConstant.device_token, "")
            param.put(StringConstant.device_id, "")
            param.put(StringConstant.device_type, StringConstant.Android)
            param.put(StringConstant.module_type, "Exercise")
            param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
            val header = HashMap<String, String>()
            header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
            header.put(StringConstant.apiKey, "HBDEV")
            header.put(StringConstant.apiVersion, "1")

            getDataManager().getCustomerFavourites(param, header)?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    binding.loader.visibility = View.GONE
                    if (success.equals("1")) {
                        exerciseList.clear()
                        binding.noExerciesFound.visibility = View.GONE
                        val favExerciseModel =
                            getDataManager().mGson?.fromJson(response.toString(), FavExerciseModel::class.java)
                        exerciseList.addAll(favExerciseModel!!.data);
                    }
                    if (exerciseList.isEmpty()) {
                        binding.noExerciesFound.visibility = View.VISIBLE
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError) {
                    binding.loader.visibility = View.GONE
                    Constant.showCustomToast(context!!, getString(R.string.something_wrong))
                    try {
                        Constant.errorHandle(anError!!, activity)
                    }
                    catch (e: Exception){
                    }
                    //binding.noExerciesFound.visibility = View.VISIBLE
                }
            })

        }else{
            Constant.showInternetConnectionDialog(mContext as Activity)
        }
    }

    override fun videoPlayClick(isScroll: Boolean, data: FavExerciseModel.Data, position: Int, view: FavExerciesAdapter.MyViewHolder, isLoad: Boolean) {
        for (i in 0 until exerciseList.size) {
            if (exerciseList.get(i).isPlaying) {
                exerciseList.get(i).isPlaying = false
                binding.myFavExerciesMain.setEnableScrolling(true)
            } else {
                exerciseList.get(i).isPlaying = true
                binding.myFavExerciesRv.requestChildFocus(view.itemView, view.itemView)
                binding.myFavExerciesMain.setEnableScrolling(false)
            }
        }
        adapter.notifyDataSetChanged()
        if (isLoad) {
            //downloadExercise(data.exercise_video)
        }
    }

    override fun shareURL(data: FavExerciseModel.Data) {
    }

    override fun videoPlayClick(isScroll: Boolean, data: WorkoutExercise, position: Int, view: WorkoutAdapter.MyViewHolder) {

    }

    override fun setFavUnfavForExercies(data: FavExerciseModel.Data, position: Int, view: ImageView) {
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
                        view.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_active))
                    } else {
                        data.exercise_is_favourite = "0"
                        view.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star))
                    }

                    if(position != -1 && position<exerciseList.size){
                        exerciseList.removeAt(position)
                        adapter.notifyDataSetChanged()
                    }

                    getFavWorkoutData(page)
                } else {
                    //Constant.showCustomToast(getActivity(), "" + message)
                }
            }

            override fun onError(anError: ANError) {
                Constant.errorHandle(anError, getActivity())
            }
        })
    }

    override fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView) {
        // no need this listener here
    }

    override fun setSelected(position: Int, isSelected: Boolean, exercise: FavExerciseModel.Data) {

    }

    /*fun downloadExercise(videoUrl: String) {
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
    }*/

}
