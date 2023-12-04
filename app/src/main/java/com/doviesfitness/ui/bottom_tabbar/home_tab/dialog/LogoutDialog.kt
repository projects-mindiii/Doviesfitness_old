package com.doviesfitness.ui.bottom_tabbar.home_tab.dialog


import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.doviesfitness.BuildConfig
import com.doviesfitness.Doviesfitness

import com.doviesfitness.R
import com.doviesfitness.temp.DownloadVideosUtil
import com.doviesfitness.temp.modal.VideoCategoryModal
import com.doviesfitness.ui.base.BaseDialog
import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil
import com.doviesfitness.ui.room_db.DatabaseClient
import com.doviesfitness.ui.room_db.GithubTypeConverters
import com.doviesfitness.ui.room_db.LocalStream
import com.doviesfitness.ui.room_db.LocalStreamVideoDataModal
import com.doviesfitness.utils.Constants
import com.doviesfitness.utils.ProgressDialog
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.dialog_logout.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogoutDialog.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LogoutDialog : BaseDialog(), View.OnClickListener {
    val TAG = LogoutDialog::class.java.name
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_logout, container, false)
    }

    fun show(fragmentManager: androidx.fragment.app.FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_logout.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_logout -> {
                //  Doviesfitness.getDataManager().logout(activity!!)


                val customerName = Doviesfitness.getDataManager().getUserInfo().customer_user_name

                var name= getBaseActivity().getDataManager().getUserInfo().customer_user_name
                var userId= getBaseActivity().getDataManager().getUserInfo().customer_id

                //   var list= DownloadUtil.getData("video")
                var list= DownloadVideosUtil.getVideoQueueList(Constants.QUEUE_VIDEO)
                //  var Dlist= DownloadUtil.getDownloadedData("downloaded")
                var Dlist=  DownloadVideosUtil.getDownloadedVideo(Constants.DOWNLOADED_VIDEO)

                if (Dlist!=null&&Dlist.size>0){
                }
                else{Dlist= ArrayList()
                }

                if (list!=null&&list.size>0)
                {
                    for (i in 0..list.size-1){

                        val lastIndex = list.get(i).videoUrl.lastIndexOf("/")
                        if (lastIndex > -1) {
                            val downloadFileName = list.get(i).videoUrl.substring(lastIndex + 1)

                            val path =
                                Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" +
                                        BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + downloadFileName

                            var ret = File(path)
                            if (ret.exists()){
                                ret.delete()
                            }
                        }
                    }
                    list.clear()
                    // DownloadUtil.setData(list)
                    DownloadVideosUtil.saveRemainingQueueVideosToLocal(list)


                }

                SaveTask(Dlist,name,userId,activity!!).execute()


                //   getBaseActivity().getDataManager().logout(activity!!)
            }
            R.id.tv_cancel -> {
                dialog?.dismiss()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogoutDialog.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoutDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class SaveTask(
        // list: ArrayList<DownloadedModal.ProgressModal>,
        Dlist: ArrayList<VideoCategoryModal>?,
        Name: String,
        userid: String,
        activity: FragmentActivity
    ) : AsyncTask<Void, Void, String>() {
        //   var Wlist = list
        var Uname = Name
        var userid = userid
        var activity=activity
        var Dlist=Dlist
        var progressDialog = ProgressDialog(activity)

        override fun onPreExecute() {
            super.onPreExecute()

            progressDialog.show()
            //ProgressBar(activity)
        }

        override fun doInBackground(vararg params: Void): String {

            try {
                if (Dlist != null && Dlist!!.size > 0) {

                    //    val DStr = GithubTypeConverters.someDownloadToString(Dlist)
                    val DStr = GithubTypeConverters.someDownloadToString1(Dlist)
                    val localData1 = LocalStreamVideoDataModal()
                    localData1.setUsername(Uname)
                    localData1.setWList(DStr)
                    localData1.setUserid(userid)

                    var Downlist = DatabaseClient.getInstance(Doviesfitness.instance).appDatabase
                        .taskDao()
                        .getDownloadedList(Uname, userid) as java.util.ArrayList<LocalStream>?

                    if (Downlist != null && Downlist.size > 0) {
                        DatabaseClient.getInstance(FacebookSdk.getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .updateDownloadList(DStr, Uname, userid)
                    } else {
                        DatabaseClient.getInstance(FacebookSdk.getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .insert(localData1)
                    }
                }
            }catch (e:Exception)
            {
                e.printStackTrace()
            }
            Doviesfitness.preferences.edit().clear().apply()

            return ""
        }

        override fun onPostExecute(aVoid: String) {
            super.onPostExecute(aVoid)
            progressDialog.dismiss()

            Doviesfitness.getDataManager().logout(activity!!)

            //  Log.d("data inserted","data inserted..."+Wlist.size)
        }
    }
}
