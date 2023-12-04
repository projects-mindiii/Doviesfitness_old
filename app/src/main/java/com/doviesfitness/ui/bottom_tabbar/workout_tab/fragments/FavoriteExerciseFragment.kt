package com.doviesfitness.ui.bottom_tabbar.workout_tab.fragments

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import androidx.databinding.DataBindingUtil
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.FilterExerciseResponse
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import com.doviesfitness.databinding.FragmentFavoriteExerciseBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.ExerciseDetailAdapterNew
import kotlinx.android.synthetic.main.fragment_favorite_exercise.*
import java.io.File
import java.lang.Exception



private const val ARG_PARAM = "forReplace"
private const val ARG_PARAM2 = "isAddingInRound"
private const val ARG_PARAM3 = "exerciseType"
class FavoriteExerciseFragment : BaseFragment(), View.OnClickListener,
    ExerciseDetailAdapterNew.OnItemClick, IsSubscribed {

    private var callback: MyCallback? = null


    fun setCallback(callback: MyCallback) {
        this.callback = callback
    }



    override fun isSubscribed() {
        startActivity(Intent(activity, SubscriptionActivity::class.java).putExtra("home", "no"))
    }

    override fun setFavUnfavForExercies(data: ExerciseListingResponse.Data, position: Int, view: ImageView) {
    }

    override fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView) {
    }

    override fun setSelected(
        position: Int,
        isSelected: Boolean,
        exercise: ExerciseListingResponse.Data
    ) {
        callback?.onDataReceived(getSelectedExercise())
    }

    interface MyCallback {
        fun onDataReceived(selectedList: ArrayList<ExerciseListingResponse.Data>)
    }

    override fun shareURL(data: ExerciseListingResponse.Data) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var forReplace: String = ""
    private lateinit var binding: FragmentFavoriteExerciseBinding
    private lateinit var adapter: ExerciseDetailAdapterNew
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
    var urlDownload = ""
    internal var list = ArrayList<Long>()
    private var downloadManager: DownloadManager? = null
    var from = ""
    var isReplaceExcerices = false
    ///////for downloading
    internal lateinit var mBroadCastReceiver: BroadcastReceiver
    var isRecieverRegistered = false
    internal var mFinishedFilesFromNotif = ArrayList<Long>()
    lateinit var mProgressThread: Thread
    var isDownloadSuccess = false
    var isDownload = false
    var exerciseType=""
    var isAddingInRound=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            forReplace = it.getString(ARG_PARAM)!!
            isAddingInRound = it.getString(ARG_PARAM2)!!
            exerciseType = it.getString(ARG_PARAM3)!!
             if(forReplace.isNotEmpty()){
                isReplaceExcerices = true
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_exercise, container, false);
        downloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        initialization()
        return binding.root
    }

    private fun initialization() {
        //  iv_back.setOnClickListener(this)
        val display = mActivity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x
        val screenWidth = size.x / 320
        val videowidth = 120 + (179 * screenWidth)
        adapter = ExerciseDetailAdapterNew(
            mContext,
            isReplaceExcerices,
            exerciseListing,
            this,
            videowidth,
            "create",
            this
        )
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())
        binding.exerciseRv.layoutManager = layoutManager
        binding.exerciseRv.adapter = adapter

        // exerciseListing.clear()
        // page = 1
        getExerciseListingData(1)
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
        fun newInstance(forReplace: String?, exerciseType: String?, isAddingInRound: String?) =
            FavoriteExerciseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, forReplace)
                    putString(ARG_PARAM2, isAddingInRound)
                    putString(ARG_PARAM3, exerciseType)
                }
            }
    }

    public fun getSelectedExercise(): ArrayList<ExerciseListingResponse.Data> {
        selectedList.clear()
        for (i in 0..exerciseListing.size - 1) {
            if (exerciseListing.get(i).isSelected == true) {
                selectedList.add(exerciseListing.get(i))
            }
        }
        return selectedList;
    }

    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }

    private fun getExerciseListingData(pageCount: Int) {
        val param = HashMap<String, String>()
        param.put(StringConstant.device_token, "")
        param.put(StringConstant.device_id, "")
        param.put(StringConstant.device_type, StringConstant.Android)
        param.put(StringConstant.module_type, "Exercise")
        // param.put(StringConstant.page_index, "" + pageCount)
        param.put(
            StringConstant.auth_customer_id,
            getDataManager().getUserInfo().customer_auth_token
        )
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        header.put(StringConstant.apiKey, "HBDEV")
        header.put(StringConstant.apiVersion, "1")


        getDataManager().getCustomerFavourites(param, header)
            ?.getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                    val success: String? = jsonObject?.getString(StringConstant.success)
                    val message: String? = jsonObject?.getString(StringConstant.message)
                    binding.progressLayout.visibility = View.GONE
                    if (success.equals("1")) {
                        no_record_found.visibility = View.GONE
                        no_record_icon.visibility = View.GONE
                        start_saving_fav.visibility = View.GONE
                        exercise_rv.visibility = View.VISIBLE
                        //   swipe_refresh.isRefreshing = false
                        Log.d("response", "response...list data..." + response!!.toString(4))
                        // Constant.showCustomToast(binding.exerciseRv.context!!, "" + message)
                        val exercisesListData =
                            getDataManager().mGson?.fromJson(
                                response.toString(),
                                ExerciseListingResponse::class.java
                            )
                        exerciseListing.addAll(exercisesListData!!.`data`)
                        downloadExercises()
                        //   page=page+1
                        //   nextPage = exercisesListData.settings.next_page.toInt()
                        Log.d("response", "nextPage......" + nextPage)
                        adapter.notifyDataSetChanged()
                        //  hideFooterLoiader()
                    }
                    if (exerciseListing.isEmpty() && binding != null) {
                        no_record_found.visibility = View.VISIBLE
                        no_record_icon.visibility = View.VISIBLE
                        start_saving_fav.visibility = View.VISIBLE
                        exercise_rv.visibility = View.GONE
                    }
                }

                override fun onError(anError: ANError) {
                    //  hideFooterLoiader()
                    binding.progressLayout.visibility = View.GONE
                    //  Constant.errorHandle(anError,binding.exerciseRv.context!!)
                    Constant.showCustomToast(
                        binding.exerciseRv.context!!,
                        getString(R.string.something_wrong)
                    )
                    try {
                        Constant.errorHandle(anError!!, activity)
                    } catch (e: Exception) {
                    }
                }
            })
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
            } else {
                exerciseListing.get(i).isPlaying = true
                exerciseListing.get(i).showLoader = false
                try {
                    binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                binding.svMain.setEnableScrolling(false)
            }
        }
        adapter.notifyDataSetChanged()
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
            adapter.notifyDataSetChanged()
        } else {
            binding.svMain.setEnableScrolling(true)
        }
        if (isDownload)
            downloadExercise(data.exercise_video, position, view, isScroll)
    }


    fun downloadExercise(
        videoUrl: String,
        position: Int,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        scroll: Boolean
    ) {
        //list.clear()
        val lastIndex = videoUrl.lastIndexOf("/")
        if (lastIndex > -1) {
            val downloadFileName = videoUrl.substring(lastIndex + 1)
            var subpath = "/Dovies//$downloadFileName"
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                    mActivity!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
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
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + mActivity!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
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
        mActivity!!.registerReceiver(mBroadCastReceiver, intentFilter)


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

    fun downloadExercises() {
        for (i in 0..exerciseListing.size - 1) {
            if (exerciseListing.get(i) != null) {
                if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals(
                        "Yes"
                    ) ||
                    exerciseListing.get(i).exercise_access_level.equals("OPEN")
                ) {
                    val lastIndex = exerciseListing.get(i).exercise_video.lastIndexOf("/")
                    if (lastIndex > -1) {
                        val downloadFileName =
                            exerciseListing.get(i).exercise_video.substring(lastIndex + 1)
                        val subpath = "/Dovies//$downloadFileName"
                        val path =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                    mActivity!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
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
                            // list.add(refid)
                        }
                    } else {

                    }
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRecieverRegistered == true)
            mActivity!!.unregisterReceiver(mBroadCastReceiver)

    }
}