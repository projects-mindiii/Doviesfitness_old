package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.doviesfitness.Doviesfitness
import com.doviesfitness.R
import com.doviesfitness.chromecast.browser.VideoItemLoader
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.base.BaseActivity
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.doviesfitness.ui.bottom_tabbar.stream_tab.adapters.DownLoadStreamWorkOutVideoAdapter
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import com.doviesfitness.ui.bottom_tabbar.workout_tab.dialog.FinishActivityDialog
import com.doviesfitness.ui.room_db.DatabaseClient
import com.doviesfitness.ui.room_db.MyVideoList
import com.facebook.FacebookSdk
import com.google.android.gms.cast.MediaInfo
import kotlinx.android.synthetic.main.activity_down_wor_kouts_video_actvity.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class DownWorKOutsVideoActvity : BaseActivity(), View.OnClickListener,
    DownLoadStreamWorkOutVideoAdapter.OnWorkOutVideoClick, FinishActivityDialog.IsDelete, LoaderManager.LoaderCallbacks<List<MediaInfo>>,FinishActivityDialog.IsYesClick {

    private lateinit var localStreamList: ArrayList<DownloadedModal.ProgressModal>
    private lateinit var adapter: DownLoadStreamWorkOutVideoAdapter
    private lateinit var localStreamVideoDataModal: DownloadedModal
    private var mainTempList = ArrayList<DownloadedModal.ProgressModal>()
    var deletePos = -1
    var itemPosition = -1
    lateinit var mapList: MutableMap<String, DownloadedModal.ProgressModal>
    var streamList = ArrayList<DownloadedModal.ProgressModal>()
    var position = 0
    var  castLoaderData= ArrayList<MediaInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_down_wor_kouts_video_actvity)
        initialisation()
    }

    fun initialisation() {
        mapList = HashMap()
        iv_back.setOnClickListener(this)
        workout_vdo_rv.setLayoutManager(LinearLayoutManager(this))
        if (intent.getSerializableExtra("localStreamVideoDataModal") != null) {
            localStreamVideoDataModal = intent.getSerializableExtra("localStreamVideoDataModal") as DownloadedModal
            title_name.text = localStreamVideoDataModal.stream_workout_name
        }

        localStreamList = ArrayList<DownloadedModal.ProgressModal>()
        mainTempList = ArrayList<DownloadedModal.ProgressModal>()
        localStreamList.addAll(localStreamVideoDataModal.download_list)

        for (i in 0..localStreamList.size-1){
            var hgfkjd  = localStreamList.get(i)
            mapList[localStreamList.get(i).stream_video_id] = hgfkjd
        }
        val values: Collection<DownloadedModal.ProgressModal> = mapList.values
        mainTempList.addAll(values)


        localStreamList.clear()
        localStreamList.addAll(mainTempList)

        if (localStreamList.size > 0) {
            txt_no_data_found.visibility = View.GONE
            workout_vdo_rv.visibility = View.VISIBLE

            Collections.sort(localStreamList, object : Comparator<DownloadedModal.ProgressModal> {
                override fun compare(
                    lhs: DownloadedModal.ProgressModal,
                    rhs: DownloadedModal.ProgressModal
                ): Int {
                    return lhs.order.compareTo(rhs.order)
                }
            })

            val hashSet = LinkedHashSet(localStreamList)
            localStreamList.clear()
            localStreamList = ArrayList(hashSet)

            adapter = DownLoadStreamWorkOutVideoAdapter(getActivity(), localStreamList, this)
            workout_vdo_rv.adapter = adapter
        } else {
            txt_no_data_found.visibility = View.VISIBLE
            workout_vdo_rv.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext())
            .registerReceiver(receiver, IntentFilter("download_progress"))

        hideNavigationBar()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext())
            .unregisterReceiver(receiver)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getWorkoutVideoClick(pos: Int) {
        /*if (status.equals("Delete")) {
            deleteVideo(myVideoList)
        } else {
            tempList.clear()
            val tempListModal = StreamWorkoutDetailModel.Settings.Data.Video(
                stream_video = "",
                stream_video_description = myVideoList.stream_video_description!!,
                stream_video_id = myVideoList.stream_video_id!!,
                video_duration = myVideoList.video_duration!!,
                stream_video_image = myVideoList.stream_video_image!!,
                stream_video_image_url = myVideoList.stream_video_image_url!!,
                stream_video_name = myVideoList.stream_video_name!!,
                stream_video_subtitle = myVideoList.stream_video_subtitle!!,
                Progress = 0,
                MaxProgress = 10,
                seekTo = 0,
                isPlaying = false,
                isComplete = false,
                isRestTime = false,
                downLoadUrl = myVideoList.downLoad_url!!,
                timeStempUrl = myVideoList.time_stemp_url!!
            )

            tempList.add(tempListModal)
            Log.v("tempList", "" + tempList)*/






        if (!localStreamList.get(pos).isAddedQueue) {
            if (DownloadUtil.getDownloadedData("downloaded") != null) {
                var downloadedList = DownloadUtil.getDownloadedData("downloaded")


                if (downloadedList.size > 0) {
                    for (i in 0..downloadedList.size - 1) {
                        if (downloadedList.get(i).stream_workout_id.equals(localStreamList.get(pos).workout_id)) {
                            if (downloadedList.get(i).download_list != null && downloadedList.get(i).download_list.size > 0) {
                                for (j in 0..downloadedList.get(i).download_list.size - 1) {
                                    if (downloadedList.get(i).download_list.get(j).stream_video_id.equals(
                                            localStreamList.get(pos).stream_video_id
                                        )
                                    ) {
                                        localStreamList.get(pos).downLoadUrl =
                                            downloadedList.get(i).download_list.get(j).downLoadUrl
                                        break
                                    }
                                }
                            }
                            break
                        }
                    }
                }
            }

            if(streamList.size>0) streamList.clear()
            streamList = ArrayList<DownloadedModal.ProgressModal>()
            position = pos
            streamList.add(localStreamList[position])


            val intent = Intent(this, StreamDownloadedVideoActivity::class.java)
            intent.putExtra("localStreamList", localStreamList)
                intent.putExtra("workout_id", "" + localStreamVideoDataModal.stream_workout_id)
            intent.putExtra("local", "yes")
            intent.putExtra("position", pos)
            intent.putExtra("castMedia", castLoaderData) //sending downloaded video list to caste it on somewhere like T.V and all.
            startActivity(intent)
        }
    }

    override fun deleteVideo(pos: Int) {
        itemPosition = pos

        val dialog = FinishActivityDialog.newInstance(
            "Delete",
            "No",
            "Are you sure, you want to delete this video?"
        )
        //dialog.setListener(this)
        dialog.setListener2(this@DownWorKOutsVideoActvity)
        dialog.show(supportFragmentManager)

/*
        if (deletePos == pos) {
            Toast.makeText(this, "Can not delete downloading video", Toast.LENGTH_SHORT)
                .show()
        }
        else {
            if (DownloadUtil.getDownloadedData("downloaded") != null) {
                var downloadedList = DownloadUtil.getDownloadedData("downloaded")


                if (downloadedList != null && downloadedList.size > 0) {
                    for (i in 0..downloadedList.size - 1) {
                        if (downloadedList.get(i).stream_workout_id.equals(localStreamVideoDataModal.stream_workout_id)) {
                            for (j in 0..downloadedList.get(i).download_list.size - 1) {
                                if (downloadedList.get(i).download_list.get(j).stream_video_id.equals(
                                        localStreamList.get(pos).stream_video_id
                                    )
                                ) {

                                    var PData = downloadedList.get(i).download_list.get(j)
                                    var PFile = File(PData.downLoadUrl)
                                    if (PFile.exists()) {
                                        PFile.delete()
                                    }

                                    downloadedList.get(i).download_list.removeAt(j)
                                    break
                                }
                            }
                            break
                        }

                    }
                }



                if (downloadedList != null && downloadedList.size > 0) {
*/
/*
                    for (i in 0..downloadedList.size-1){
                        if (downloadedList.get(i).download_list!=null&&downloadedList.get(i).download_list.size>0){
                        }
                        else{
                            downloadedList.removeAt(i)
                        }
                    }
*//*


                    var j = 0
                    while (j < downloadedList.size) {
                        if (downloadedList.get(j).download_list != null && downloadedList.get(j).download_list.size > 0) {
                            j++
                        } else {
                            downloadedList.removeAt(j)

                        }
                    }

                    if (downloadedList != null && downloadedList.size > 0) {
                        DownloadUtil.setDownloadedData(downloadedList)
                    } else
                        Doviesfitness.preferences.edit().putString("downloaded", "").commit()
                    // getDataManager().setUserStringData("downloaded", "")
                } else
                //  getDataManager().setUserStringData("downloaded", "")
                    Doviesfitness.preferences.edit().putString("downloaded", "").commit()
            }
            var isTrue = false
            if (DownloadUtil.getData("video") != null) {
                val Vlist = DownloadUtil.getData("video")

                if (Vlist != null && Vlist.size > 0) {


                    for (k in 0..Vlist.size - 1) {

                        if (HomeTabActivity.downloadBinder!!.downloadManager != null
                            && HomeTabActivity.downloadBinder?.downloadManager?.currDownloadUrl.equals(
                                localStreamList.get(pos).VideoUrl
                            )
                            && HomeTabActivity.downloadBinder?.downloadManager?.currDownloadUrl.equals(
                                Vlist[k].VideoUrl
                            )
                        ) {
                            Toast.makeText(
                                this,
                                "Can not delete downloading video",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isTrue = true
                           // break


                        } else {
                            if (Vlist.get(k).stream_video_id.equals(localStreamList.get(pos).stream_video_id)) {
                                Vlist.removeAt(k)
                                break
                            }
                        }

                    }
                }

                if (Vlist != null && Vlist.size > 0)
                    DownloadUtil.setData(Vlist)
                else
                    Doviesfitness.preferences.edit().putString("video", "").commit()
            }



            if (!isTrue) {
                localStreamList.removeAt(pos)
                adapter.notifyDataSetChanged()
            }


            if (localStreamList.size > 0) {
                txt_no_data_found.visibility = View.GONE
                workout_vdo_rv.visibility = View.VISIBLE

            } else {
                txt_no_data_found.visibility = View.VISIBLE
                workout_vdo_rv.visibility = View.GONE
            }
        }
*/

    }


    private fun deleteVideo(myVideoList: MyVideoList) {
        class DeleteTask : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg voids: Void): String? {
                DatabaseClient.getInstance(applicationContext).appDatabase.taskDao()
                    .deleteWorkOutVideoListItem(
                        myVideoList.stream_video_name,
                        myVideoList.stream_video_id
                    )
                return ""
            }

            override fun onPostExecute(aVoid: String) {
                super.onPostExecute(aVoid)
                getWorkVideoListUsingId(localStreamVideoDataModal.stream_workout_id!!)
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
            }
        }

        val dt = DeleteTask()
        dt.execute()
    }

    //get all workoutsvideo list
    private fun getWorkVideoListUsingId(id: String) {
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
                if (taskList.size == 0) {
                    onBackPressed()
                }
                if (taskList.size != 0 && !taskList.isEmpty()) {
                    localStreamList.clear()
                    //  localStreamList.addAll(taskList)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        GetAllWorkoutListtt().execute()
    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("Broadcast Receiver", "Broadcast Receiver...:$intent")
            if (intent.action == "download_progress") {
                val position = intent.getIntExtra("position", 0)
                var order = -1
                if (intent.hasExtra("order"))
                    order = intent.getIntExtra("order", 0)

                if (intent.getStringExtra("cancel") != null && !intent.getStringExtra("cancel")!!
                        .isEmpty()
                ) {
                    if (intent.getStringExtra("file") != null && !intent.getStringExtra("file")!!
                            .isEmpty()
                    ) {
                        val AList = DownloadUtil.getData("video")
                        if (AList != null && AList.size > 0) {
                            var loaderPos = -1
                            if (AList[0].workout_id.equals(localStreamVideoDataModal.stream_workout_id)) {
                                for (i in 0..localStreamList.size - 1) {
                                    if (localStreamList.get(i).isAddedQueue && order == localStreamList.get(
                                            i
                                        ).order
                                    ) {
                                        loaderPos = i
                                        break
                                    }
                                }
                            }

                            if (loaderPos != -1) {
                                if (workout_vdo_rv.findViewHolderForAdapterPosition(loaderPos) != null) {
                                    val holder =
                                        workout_vdo_rv.findViewHolderForAdapterPosition(
                                            loaderPos
                                        ) as DownLoadStreamWorkOutVideoAdapter.MyViewHolder


                                    var filePath = intent.getStringExtra("file")
                                    var deleted = intent.getStringExtra("deleted")
                                    var PFile = File(filePath)
                                    if (PFile.exists() || deleted.equals("yes")) {
                                        holder.loader.visibility = View.GONE
                                        holder.downloadIcon.visibility = View.VISIBLE
                                        holder.downloadIcon.setImageResource(R.drawable.icon_download_new);
                                        localStreamList.removeAt(loaderPos)
                                        adapter.notifyDataSetChanged()

                                        if (PFile.exists())
                                            PFile.delete()
                                        AList.removeAt(0)
                                        if (AList.size > 0) {
                                            DownloadUtil.setData(AList)

                                            if (AList != null && AList.size > 0) {
                                                HomeTabActivity.downloadBinder!!.startDownload(
                                                    AList[0].VideoUrl,
                                                    StreamDetailActivity.streamWorkoutId,
                                                    0
                                                )
                                                getDataManager().setUserStringData(
                                                    AppPreferencesHelper.STEAM_WORKOUT_ID,
                                                    StreamDetailActivity.streamWorkoutId
                                                )
                                            }
                                        } else {
                                            Doviesfitness.preferences.edit().putString("video", "")
                                                .commit()
                                        }
                                    }
                                }
                            }
                        }
                    }

                } else {
                    if (intent.hasExtra("progress") && intent.getStringExtra("progress") != null) {
                        val progress = Integer.parseInt(intent.getStringExtra("progress"))
                        val workout_id = intent.getStringExtra("workout_id")
                        Log.d(
                            "Broadcast Receiver",
                            "Broadcast Receiver download...:$progress...pos...$position"
                        )

                        var loaderPos = -1
                        if (workout_id != null && workout_id.equals(localStreamVideoDataModal.stream_workout_id)) {
                            for (i in 0..localStreamList.size - 1) {
                                if (localStreamList.get(i).isAddedQueue && order == localStreamList.get(
                                        i
                                    ).order
                                ) {
                                    loaderPos = i
                                    deletePos = i
                                    break
                                }
                            }
                        }

                        if (loaderPos != -1) {
                            if (workout_vdo_rv.findViewHolderForAdapterPosition(loaderPos) != null) {
                                val holder =
                                    workout_vdo_rv.findViewHolderForAdapterPosition(loaderPos) as DownLoadStreamWorkOutVideoAdapter.MyViewHolder
                                holder.loader.visibility = View.VISIBLE
                                holder.downloadIcon.visibility = View.GONE
                                holder.loader.max = 100
                                holder.loader.progress = progress
                                if (progress == 100) {
                                    localStreamList.get(loaderPos).isAddedQueue = false
                                    // holder.episode.text = "Downloaded"
                                    deletePos = -1
                                    holder.loader.visibility = View.GONE
                                    holder.downloadIcon.setImageResource(R.drawable.complete_downloaded);
                                    Log.d("Complete download", "Complete download..." + progress)
                                    holder.downloadIcon.visibility = View.VISIBLE
                                } else {

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun isDelete() {

        if (deletePos == itemPosition) {
            Toast.makeText(this, "Can not delete downloading video", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (DownloadUtil.getDownloadedData("downloaded") != null) {
                var downloadedList = DownloadUtil.getDownloadedData("downloaded")


                if (downloadedList != null && downloadedList.size > 0) {
                    for (i in 0..downloadedList.size - 1) {
                        if (downloadedList.get(i).stream_workout_id.equals(localStreamVideoDataModal.stream_workout_id)) {
                            for (j in 0..downloadedList.get(i).download_list.size - 1) {
                                if (downloadedList.get(i).download_list.get(j).stream_video_id.equals(
                                        localStreamList.get(itemPosition).stream_video_id
                                    )
                                ) {
                                    var PData = downloadedList.get(i).download_list.get(j)
                                    var PFile = File(PData.downLoadUrl)
                                    if (PFile.exists()) {
                                        PFile.delete()
                                    }

                                    downloadedList.get(i).download_list.removeAt(j)
                                    break
                                }
                            }
                            break
                        }
                    }
                }

                if (downloadedList != null && downloadedList.size > 0) {
/*
                    for (i in 0..downloadedList.size-1){
                        if (downloadedList.get(i).download_list!=null&&downloadedList.get(i).download_list.size>0){
                        }
                        else{
                            downloadedList.removeAt(i)
                        }
                    }
*/

                    var j = 0
                    while (j < downloadedList.size) {
                        if (downloadedList.get(j).download_list != null && downloadedList.get(j).download_list.size > 0) {
                            j++
                        } else {
                            downloadedList.removeAt(j)

                        }
                    }

                    if (downloadedList != null && downloadedList.size > 0) {
                        DownloadUtil.setDownloadedData(downloadedList)
                    } else
                        Doviesfitness.preferences.edit().putString("downloaded", "").commit()
                    // getDataManager().setUserStringData("downloaded", "")
                } else
                //  getDataManager().setUserStringData("downloaded", "")
                    Doviesfitness.preferences.edit().putString("downloaded", "").commit()
            }
            var isTrue = false
            if (DownloadUtil.getData("video") != null) {
                val Vlist = DownloadUtil.getData("video")

                if (Vlist != null && Vlist.size > 0) {


                    for (k in 0..Vlist.size - 1) {

                        if (HomeTabActivity.downloadBinder!!.downloadManager != null
                            && HomeTabActivity.downloadBinder?.downloadManager?.currDownloadUrl.equals(
                                localStreamList.get(itemPosition).VideoUrl
                            )
                            && HomeTabActivity.downloadBinder?.downloadManager?.currDownloadUrl.equals(
                                Vlist[k].VideoUrl
                            )
                        ) {
                            Toast.makeText(
                                this,
                                "Can not delete downloading video",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isTrue = true
                            // break


                        } else {
                            if (Vlist.get(k).stream_video_id.equals(localStreamList.get(itemPosition).stream_video_id)) {
                                Vlist.removeAt(k)
                                break
                            }
                        }

                    }
                }

                if (Vlist != null && Vlist.size > 0)
                    DownloadUtil.setData(Vlist)
                else
                    Doviesfitness.preferences.edit().putString("video", "").commit()
            }



            if (!isTrue) {
                localStreamList.removeAt(itemPosition)
                adapter.notifyDataSetChanged()
            }


            if (localStreamList.size > 0) {
                txt_no_data_found.visibility = View.GONE
                workout_vdo_rv.visibility = View.VISIBLE

            } else {
                txt_no_data_found.visibility = View.VISIBLE
                workout_vdo_rv.visibility = View.GONE
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MediaInfo>> {
        return VideoItemLoader(getActivity(),   streamList[position],
            streamList[position].stream_workout_image_url)
    }

    override fun onLoadFinished(loader: Loader<List<MediaInfo>>, data: List<MediaInfo>?) {
        /*safe call*/
        data?.let {getData->
            castLoaderData.addAll(getData)
        }
    }

    override fun onLoaderReset(loader: Loader<List<MediaInfo>>) {

    }

    override fun isYesClick() {
        TODO("Not yet implemented")
    }

    override fun hideTransparentView() {
        TODO("Not yet implemented")
    }
}
