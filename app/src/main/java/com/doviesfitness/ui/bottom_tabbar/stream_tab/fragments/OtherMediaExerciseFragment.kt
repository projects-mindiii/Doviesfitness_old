package com.doviesfitness.ui.bottom_tabbar.stream_tab.fragments

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.databinding.ActivityExerciseDetaillistBinding
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseFragment
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutExercise
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity.Companion.otherMediaList
import com.doviesfitness.ui.bottom_tabbar.workout_tab.activity.ExerciseDetailListActivityNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.adapters.ExerciseDetailAdapterNew
import com.doviesfitness.ui.bottom_tabbar.workout_tab.model.ExerciseListingResponse
import com.doviesfitness.utils.Constant
import com.doviesfitness.utils.StringConstant
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class OtherMediaExerciseFragment : BaseFragment(),ExerciseDetailAdapterNew.OnItemClick,
    IsSubscribed {

    var isDownload = false
    var urlDownload = ""
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


    override fun videoPlayClick(
        isScroll: Boolean,
        data: ExerciseListingResponse.Data,
        position: Int,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        isLoad: Boolean
    ) {
        isDownload=isLoad
        urlDownload=""



        for (i in 0 until exerciseListing.size) {
            if (exerciseListing.get(i).isPlaying) {
                exerciseListing.get(i).isPlaying = false
                binding.svMain.setEnableScrolling(true)
                //  isPlayingFlag=true
                Log.d("scrolling flag","scrolling flag.....true")

            }
            else {
                exerciseListing.get(i).isPlaying = true
                exerciseListing.get(i).showLoader = false
                try {
                    binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                binding.svMain.setEnableScrolling(false)
                Log.d("scrolling flag","scrolling flag.....false")
            }
        }

        /*if ( binding.svMain.isEnableScrolling()){
            binding.svMain.setEnableScrolling(false)
        }
         else{
            binding.svMain.setEnableScrolling(true)
        }*/
        if (ExerciseDetailListActivityNew.flag){
            ExerciseDetailListActivityNew.flag =false
            binding.svMain.setEnableScrolling(true)
        }else{
            ExerciseDetailListActivityNew.flag =true
            binding.svMain.setEnableScrolling(false)
        }
        adapter.notifyDataSetChanged()
    }

    override fun videoDownload(
        isScroll: Boolean,
        data:ExerciseListingResponse.Data,
        view: ExerciseDetailAdapterNew.MyViewHolder,
        position: Int,isLoad: Boolean
    ) {
        isDownload=isLoad
        urlDownload=data.exercise_video
        if (isDownload) {
            for (i in 0 until exerciseListing.size) {
                if (i==position) {
                    exerciseListing.get(i).isClicked = true
                    binding.exerciseRv.requestChildFocus(view.itemView, view.itemView)

                } else {
                    exerciseListing.get(i).isClicked = false
                }
            }
            binding.svMain.setEnableScrolling(false)
            Log.d("scrolling flag","scrolling flag play.....false")
            adapter.notifyDataSetChanged()
        }
        else{
            binding.svMain.setEnableScrolling(true)
            Log.d("scrolling flag","scrolling flag stop.....true")
        }
        if (isDownload)
            downloadExercise(data.exercise_video,position,view,isScroll)
    }

    override fun shareURL(data: ExerciseListingResponse.Data) {
    }

    override fun setFavUnfavForExercies(data: ExerciseListingResponse.Data, position: Int, view: ImageView) {
        val param = HashMap<String, String>()
        param.put(StringConstant.auth_customer_id, getDataManager().getUserInfo().customer_auth_token)
        param.put(StringConstant.module_name, StringConstant.exercise)
        param.put(StringConstant.module_id, data.exercise_id)
        val header = HashMap<String, String>()
        header.put(StringConstant.authToken, getDataManager().getUserInfo().customer_auth_token)
        getDataManager().makeFavoriteApi(param, header)?.getAsJSONObject(object :
            JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {

                val jsonObject: JSONObject? = response?.getJSONObject(StringConstant.settings)
                val success: String? = jsonObject?.getString(StringConstant.success)
                val message: String? = jsonObject?.getString(StringConstant.message)
                if (success.equals("1")) {
                    if (data.exercise_is_favourite.equals("0")) {
                        data.exercise_is_favourite = "1"
                        view.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_star_active))
                    } else {
                        data.exercise_is_favourite = "0"
                        view.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_star))
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

    override fun setFavUnfavForFavExercies(data: WorkoutExercise, position: Int, view: ImageView) {
    }

    override fun setSelected(
        position: Int,
        isSelected: Boolean,
        exercise: ExerciseListingResponse.Data
    ) {
    }

    override fun isSubscribed() {
        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }


    private lateinit var binding: ActivityExerciseDetaillistBinding;
    private lateinit var adapter: ExerciseDetailAdapterNew;
    private var exerciseListing = ArrayList<ExerciseListingResponse.Data>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_exercise_detaillist, container, false)
        downloadManager = activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        initialization()
        return binding.root
    }

    private fun initialization() {

        binding.toolbarLayout.visibility=View.GONE
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        Doviesfitness.height = size.y
        Doviesfitness.weight = size.x

        val screenWidth = size.x / 320

        val videowidth = 120 + (179 * screenWidth)

        if (otherMediaList.size>0){
            for (i in 0..otherMediaList.size-1){
                var data= otherMediaList.get(i)
                var listData=ExerciseListingResponse.Data(data.exercise_access_level,
                    data.exercise_amount,
                    data.exercise_amount_display,
                    data.exercise_body_parts,
                    data.media_description,
                    data.exercise_equipments,
                    data.media_id,
                    data.media_image,
                    data.exercise_is_favourite,
                    data.exercise_level,
                    data.media_name,
                    "",
                    data.exercise_tags,
                    data.media_video,
                    "",
                    false,
                    false,
                    true,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    true,
                    false,
                    false,
                    0)
                exerciseListing.add(listData)
            }
        }



        adapter = ExerciseDetailAdapterNew(activity!!, false,exerciseListing, this, videowidth, "",this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getActivity())

        binding.exerciseRv.setNestedScrollingEnabled(false);
        binding.exerciseRv.setHasFixedSize(false);
        binding.exerciseRv.layoutManager = layoutManager
        binding.exerciseRv.adapter = adapter


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val streamDetailActivity = context as StreamDetailActivity
        if (streamDetailActivity != null) {
           // streamDetailActivity.setUpdateVideoListListener(this)
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
                    activity!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
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
            startDownloadThread(videoUrl,position,view,scroll)
        }

    }

    fun closeVideo(){
        try {
            if (adapter!=null){

                for (i in 0 until exerciseListing.size) {
                    if (exerciseListing.get(i).isPlaying) {
                        exerciseListing.get(i).isPlaying = false
                        binding.svMain.setEnableScrolling(true)
                        adapter.notifyDataSetChanged()

                    }
                }

            }
        }
        catch (ex:Exception){
            ex.printStackTrace()
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
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +activity!!.packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + downloadFileName
/*
                        val encryptedPath =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/encrypted"
*/
                        val f = File(path)
                        if (f.exists()) {
                            if (isDownload&&urlDownload.equals(videoUrl,true)){
                                var uri = Uri.parse(path)
                                exerciseListing.get(position).isClicked = false
                                adapter.intializePlayer(uri,position,view)
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
        activity!!.registerReceiver(mBroadCastReceiver, intentFilter)


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


}
