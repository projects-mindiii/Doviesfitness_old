package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.subscription.IsSubscribed
import com.doviesfitness.subscription.SubscriptionActivity
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.DownLoadStreamWorkOutAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.ui.room_db.*
import com.doviesfitness.utils.Constant
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.activity_downloads_stream.*
import kotlinx.android.synthetic.main.activity_fav_stream.edit_icon
import kotlinx.android.synthetic.main.activity_fav_stream.iv_back
import java.io.File


class DownloadsStreamActivity : BaseActivity(), DownLoadStreamWorkOutAdapter.OnWorkOutClickListener,
    View.OnClickListener, FinishActivityDialog.IsDelete, IsSubscribed,FinishActivityDialog.IsYesClick {

    override fun isSubscribed() {
        var intent = Intent(getActivity(), SubscriptionActivity::class.java).putExtra("home", "no")
            .putExtra("exercise", "yes")
        startActivityForResult(intent, 2)
    }


    lateinit var adapter: DownLoadStreamWorkOutAdapter
    private var mLastClickTime: Long = 0
    private var editClick: Boolean = false
    var downloadedList = ArrayList<DownloadedModal>()
    var deletePosition=-1
    var workout_id=""
    var progressVal=0
    var isAdmin=""
    var isSubscribe=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        //setContentView(R.layout.activity_fav_stream)
        setContentView(R.layout.activity_downloads_stream)
        isAdmin= getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!
        if (intent.hasExtra("isSubscribe")){
            isSubscribe=  intent.getStringExtra("isSubscribe")!!
        }
        initialisation()
    }

    fun initialisation() {
        iv_back.setOnClickListener(this)
        edit_icon.setOnClickListener(this)
        btn_explore.setOnClickListener(this)
        val layoutManager = androidx.recyclerview.widget.GridLayoutManager(getActivity(), 3);
        workout_rv.layoutManager = layoutManager
        val spacing = Constant.deviceSize(getActivity()) / 2

// apply spacing
        workout_rv.setPadding(spacing, spacing, spacing, spacing)
        workout_rv.setClipToPadding(false)
        workout_rv.setClipChildren(false)
        workout_rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext()).registerReceiver(receiver, IntentFilter("download_progress"))


        //  getAllWorkoutList("")
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext()).unregisterReceiver(receiver)

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_explore -> {
                onBackPressed()
            }
            R.id.edit_icon -> {
                lastClick()

                if (editClick) {
                    editClick = false
                    edit_icon.setImageResource(R.drawable.ic_edit_workout)

                    // getAllWorkoutList("fromEdit")
                } else {
                    editClick = true
                    if (downloadedList.size>0) {
                        edit_icon.setImageResource(R.drawable.circle_right_click)
                    }
                    // getAllWorkoutList("")
                }


                if (downloadedList.size>0){
                    no_data_found_layout.visibility=View.GONE
                    workout_rv.visibility=View.VISIBLE
                    adapter = DownLoadStreamWorkOutAdapter(getActivity(),
                        editClick, downloadedList, this@DownloadsStreamActivity,isAdmin,this)
                    workout_rv.adapter = adapter
                }
                else{
                    no_data_found_layout.visibility=View.VISIBLE
                    workout_rv.visibility=View.GONE
                }
                /* val intent = Intent(getActivity(), DeleteDownlodedStreamActivity::class.java)
                 startActivity(intent)*/
            }
        }
    }


/*
    fun isAlreadyDownloaded(videoUrl: String):String{
        //check if user was subscribed
        var url=""
        val customerName = getDataManager().getUserInfo().customer_user_name
        if (videoUrl != null && !TextUtils.isEmpty(videoUrl)) {
            val lastIndex = videoUrl.lastIndexOf("/")
            if (lastIndex > -1) {
                val downloadFileName = videoUrl.substring(lastIndex + 1)
                url = Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                            "com.doviesfitness.debug" + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + downloadFileName

            }
        }
        return url

    }
*/

    fun setData(){

       // var isAlreadySubscribed=false

        if (DownloadUtil.getDownloadedData("downloaded") != null)
            downloadedList = DownloadUtil.getDownloadedData("downloaded")
        val taskList = ArrayList<LocalStreamVideoDataModal>()


        if (downloadedList.size > 0) {
            for (i in 0..downloadedList.size - 1) {
                if (downloadedList.get(i).download_list != null && downloadedList.get(i).download_list.size > 0) {
                    var j = 0
                    while (j < downloadedList.get(i).download_list.size) {
                        downloadedList.get(i).download_list.get(j).isAddedQueue=false

                     /*  var str= isAlreadyDownloaded(downloadedList.get(i).download_list.get(j).VideoUrl)
                        if (str.equals(downloadedList.get(i).download_list.get(j).downLoadUrl))
                        {
                            isAlreadySubscribed=true
                        }*/

                        var isdownloaded = downloadedList.get(i).download_list.get(j).downLoadUrl
                        var DFile = File(isdownloaded)
                        if (!DFile.exists()) {
                            downloadedList.get(i).download_list.removeAt(j)

                        } else
                            j++
                    }
                }
            }
        }
//////


        val list = DownloadUtil.getData("video")
        if (list != null && list.size > 0) {
            for (i in 0..list.size - 1) {
                if (downloadedList != null && downloadedList.size > 0) {
                    var isAvail=false
                    for (j in 0..downloadedList.size - 1) {
                        if (list.get(i).workout_id.equals(downloadedList.get(j).stream_workout_id)){
                            downloadedList.get(j).download_list.add(list.get(i))
                            isAvail=true
                            break
                        }
                    }
                    if (!isAvail){
                        var PModel=list.get(i)
                        val Plist = java.util.ArrayList<DownloadedModal.ProgressModal>()
                        Plist.add(PModel)
                        val DModel = DownloadedModal(
                            "","",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            PModel.stream_workout_description,
                            PModel.workout_id,
                            PModel.stream_workout_image,
                            PModel.stream_workout_image_url,
                            PModel.stream_workout_name,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            Plist,
                            "",
                            ""
                        )
                        downloadedList.add(DModel)
                    }
                }
                else{
                    downloadedList = ArrayList<DownloadedModal>()
                    var PModel=list.get(i)
                    val Plist = java.util.ArrayList<DownloadedModal.ProgressModal>()
                    Plist.add(PModel)
                    val DModel = DownloadedModal(
                        "","",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        PModel.stream_workout_description,
                        PModel.workout_id,
                        PModel.stream_workout_image,
                        PModel.stream_workout_image_url,
                        PModel.stream_workout_name,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        Plist,
                        "",
                        ""
                    )
                    downloadedList.add(DModel)
                }
            }
        }
/////////

        if (downloadedList!=null&&downloadedList.size>0){
            var j = 0
/*
            for (i in 0..downloadedList.size-1){
                if (downloadedList.get(i).download_list!=null&&downloadedList.get(i).download_list.size>0){
                }
                else{
                    downloadedList.removeAt(i)
                }
            }*/
            //

            while (j < downloadedList.size) {
                if (downloadedList.get(j).download_list!=null&&downloadedList.get(j).download_list.size>0){
                    j++
                }
                else{
                    downloadedList.removeAt(j)

                }
            }

        }


/////////

        if (downloadedList.size>0){
            edit_icon.visibility=View.VISIBLE
            no_data_found_layout.visibility=View.GONE
            btn_explore.visibility=View.GONE
            workout_rv.visibility=View.VISIBLE
            adapter = DownLoadStreamWorkOutAdapter(getActivity(),
                editClick, downloadedList, this@DownloadsStreamActivity,isAdmin,this)
            workout_rv.adapter = adapter
           // adapter.notifyDataSetChanged()
        }
        else{
            edit_icon.visibility=View.GONE
            no_data_found_layout.visibility=View.VISIBLE
            btn_explore.visibility=View.VISIBLE
            workout_rv.visibility=View.GONE
        }

    }

    fun lastClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }



    override fun onResume() {
        super.onResume()
        val handler = Handler()
        handler.postDelayed(object:Runnable {
            public override fun run() {
                setData()
                hideNavigationBar()
            }
        }, 1000)

    }

    override fun onClickWorkOut(status: String, pos: Int) {

        /* if (status.equals("Delete")) {
             if(localStreamVideoDataModal.stream_workout_id != null){
                 getAllWorkVideoListUsingId(localStreamVideoDataModal.stream_workout_id!!)
             }

         } else {

             if (localStreamVideoDataModal != null) {
                 val intent =
                     Intent(this@DownloadsStreamActivity, DownWorKOutsVideoActvity::class.java)
                 intent.putExtra("localStreamVideoDataModal", localStreamVideoDataModal)
                 startActivity(intent)
             }

             if (localStreamVideoDataModal != null) {*/
        val intent = Intent(this@DownloadsStreamActivity, DownWorKOutsVideoActvity::class.java)
        intent.putExtra("localStreamVideoDataModal", downloadedList.get(pos))
        startActivity(intent)
        // }
        //  }
    }

    override fun isDelete() {
        if (downloadedList.size > 0) {

            if (workout_id.equals(downloadedList.get(deletePosition).stream_workout_id)&&progressVal<100) {
                Toast.makeText(this, "Can not delete downloading workout", Toast.LENGTH_SHORT)
                    .show()
            }
            else {

                val Vlist = DownloadUtil.getData("video")


                if (downloadedList.get(deletePosition).download_list != null && downloadedList.get(
                        deletePosition
                    ).download_list.size > 0
                ) {
                    for (i in 0..downloadedList.get(deletePosition).download_list.size - 1) {
                        var PData = downloadedList.get(deletePosition).download_list.get(i)
                        var PFile = File(PData.downLoadUrl)
                        if (PFile.exists()) {
                            PFile.delete()
                        }
                        if (Vlist != null && Vlist.size > 0) {

/*
                            for (j in 0..Vlist.size-1){
                                if (Vlist.get(j).stream_video_id.equals(downloadedList.get(deletePosition).download_list.get(i).stream_video_id))

                                {
                                    Vlist.removeAt(j)
                                }
                            }
*/
                            /////
                            var j = 0
                            while (j < Vlist.size) {
                                if (Vlist.get(j).stream_video_id.equals(downloadedList.get(deletePosition).download_list.get(i).stream_video_id))

                                {
                                    Vlist.removeAt(j)
                                }
                                else{
                                    j++
                                }
                            }



                        }

                    }
                }
                downloadedList.removeAt(deletePosition)
                adapter.notifyDataSetChanged()

                if (downloadedList!=null&&downloadedList.size>0)
                    DownloadUtil.setDownloadedData(downloadedList)
                else
                    Doviesfitness.preferences.edit().putString("downloaded", "").commit()

                if (Vlist != null && Vlist.size > 0)
                    DownloadUtil.setData(Vlist)
                else
                    Doviesfitness.preferences.edit().putString("video", "").commit()


                if (downloadedList.size>0){
                    edit_icon.visibility=View.VISIBLE
                    no_data_found_layout.visibility=View.GONE
                    btn_explore.visibility=View.GONE
                    workout_rv.visibility=View.VISIBLE

                }
                else{
                    edit_icon.visibility=View.GONE
                    no_data_found_layout.visibility=View.VISIBLE
                    btn_explore.visibility=View.VISIBLE
                    workout_rv.visibility=View.GONE
                }

            }
        }

/*

        DownloadUtil.mdownloadListener.getDownloadService().stopForeground(true)
        val AList = DownloadUtil.getData("video")
        if (AList != null && AList.size > 0) {
            var j = 0
            while (j < AList.size) {
                if (AList.get(j).workout_id.equals(downloadedList.get(deletePosition).stream_workout_id)){
                    AList.removeAt(j)
                }
                else
                    j++
            }

        }

        if (AList.size>0) {
            DownloadUtil.setData(AList)
            HomeTabActivity.downloadBinder!!.startDownload(AList[0].VideoUrl, 0)

        }
        else {
            Doviesfitness.preferences.edit().putString("video", "").commit()
        }


*/

      //  getDataManager().setUserStringData("downloaded","")
    }


    override fun onDeleteWorkOut(pos: Int) {
        deletePosition=pos

        val dialog = FinishActivityDialog.newInstance("Delete", "No", "Are you sure, you want to delete this video?")
        dialog.setListener2(this@DownloadsStreamActivity)
        dialog.show(supportFragmentManager)

    }

    //delete WorkOut
    private fun deleteTask(task: LocalStreamVideoDataModal) {
        class DeleteTask : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg voids: Void): String? {
                DatabaseClient.getInstance(applicationContext).appDatabase.taskDao()
                    .deleteWorkOutFromList(task)
                return ""
            }

            override fun onPostExecute(aVoid: String) {
                super.onPostExecute(aVoid)
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
            }
        }

        val dt = DeleteTask()
        dt.execute()
    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("Broadcast Receiver", "Broadcast Receiver...:$intent")
             workout_id = intent.getStringExtra("workout_id")!!
            if(intent.hasExtra("progress")) {
              var  progressstr = intent.getStringExtra("progress")!!
                progressVal =progressstr!!.toInt()
            }
            else if(intent.hasExtra("cancel")) {
                if (intent.getStringExtra("cancel") != null && !intent.getStringExtra(
                        "cancel"
                    )!!.isEmpty()){
                    progressVal=100
                }
            }
        }
    }


    //get all workout list
/*
    private fun getAllWorkoutList(fromWhich: String) {
        class GetAllWorkoutListtt() :
            AsyncTask<Void, Void, List<LocalStreamVideoDataModal>>() {
            override fun doInBackground(vararg voids: Void): List<LocalStreamVideoDataModal> {
                val taskList =
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                        .getAllWorkList()
                Log.v("videoList", "" + taskList)
                return taskList
            }

            override fun onPostExecute(taskList: List<LocalStreamVideoDataModal>) {
                super.onPostExecute(taskList)

                if (taskList.size == 0) {
                    adapter = DownLoadStreamWorkOutAdapter(
                        getActivity(),
                        editClick,
                        downloadedList,
                        this@DownloadsStreamActivity
                    )
                    workout_rv.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                if (taskList != null && taskList.size != 0) {

                    var dlownloadVideoList = ArrayList<LocalStreamVideoDataModal>()
                    dlownloadVideoList.addAll(taskList)

                    adapter = DownLoadStreamWorkOutAdapter(
                        getActivity(),
                        editClick,
                        downloadedList,
                        this@DownloadsStreamActivity
                    )
                    workout_rv.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        }
        GetAllWorkoutListtt().execute()
    }
*/

    //get particuler workout list for delete videos
    private fun getAllWorkVideoListUsingId(id: String) {

        class GetAllWorkoutListtt() :
            AsyncTask<Void, Void, List<MyVideoList>>() {
            override fun doInBackground(vararg voids: Void): List<MyVideoList> {
                val taskList =
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao()
                        .getAllMyWorkOutListVideo(id)
                Log.v("List", "" + taskList)
                return taskList
            }

            override fun onPostExecute(taskList: List<MyVideoList>) {
                super.onPostExecute(taskList)

                if (taskList.size != 0 && !taskList.isEmpty()) {
                    deletFilePath(taskList, id)
                }
            }
        }
        GetAllWorkoutListtt().execute()
    }

    private fun deletFilePath(taskList: List<MyVideoList>, id: String) {
        for (i in 0..taskList.size - 1) {

            Log.v("SizeCount", "" + taskList.size)
            if (taskList.get(i).time_stemp_url != null) {
                val videoUrl = taskList.get(i).time_stemp_url
                val pathget =
                    Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                            packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + videoUrl + "/dovies_video"

                val f = File(pathget)
                if (f.exists()) {
                    Log.v("filePath", "" + f)
                    val idDeleted = f.delete()
                    if (idDeleted) {
                        val directoryPath =
                            Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                    packageName + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + "/" + videoUrl
                        val dir = File(directoryPath)
                        if (dir.isDirectory) {
                            dir.delete()
                        }
                    }
                }
            }
        }

        // delete work out complete
        deleteWorkout(id)
    }

    private fun deleteWorkout(stream_workout_id: String) {
        class DeleteTask : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String? {
                DatabaseClient.getInstance(applicationContext).appDatabase.taskDao()
                    .deleteWorkOutVideo(stream_workout_id)
                return ""
            }

            override fun onPostExecute(aVoid: String) {
                super.onPostExecute(aVoid)
              //  getAllWorkoutList("fromEdit")
                adapter.notifyDataSetChanged()
                Toast.makeText(applicationContext, "Workout Deleted", Toast.LENGTH_LONG).show()
            }
        }

        val dt = DeleteTask()
        dt.execute()
    }



    internal class GetData(UName:  String ,UserId:String):AsyncTask<Void, Void, java.util.ArrayList<DownloadedModal>>() {
        var Uname=UName
        var UserId=UserId
        override fun doInBackground(vararg params: Void): java.util.ArrayList<DownloadedModal> {
            var strList = java.util.ArrayList<DownloadedModal>()
            try {
                var list: java.util.ArrayList<LocalStream>? = java.util.ArrayList()
                list = DatabaseClient.getInstance(Doviesfitness.instance).appDatabase
                    .taskDao()
                    .getAllList(Uname, UserId) as java.util.ArrayList<LocalStream>?
                if (list != null && list.size > 0) {
                    val t = list[0]
                    strList =
                        GithubTypeConverters.stringToSomeObjectList(t.wList) as java.util.ArrayList<DownloadedModal>

                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }



            return strList
        }
        override fun onPostExecute(list:java.util.ArrayList<DownloadedModal>) {
            Log.d("list..","list..."+list.size)
            // Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        }
    }

    override fun isYesClick() {
        TODO("Not yet implemented")
    }

    override fun hideTransparentView() {
        TODO("Not yet implemented")
    }


}