package com.doviesfitness

import android.app.Application
import android.app.PendingIntent
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.clubz.helper.SyncWorker
import com.doviesfitness.data.AppDataManager
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.utils.Utility
import com.google.firebase.iid.FirebaseInstanceId
import net.khirr.library.foreground.Foreground
import android.app.AlarmManager
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.doviesfitness.setting.recivers.MyNotificationPublisher


class Doviesfitness : Application(), Foreground.Listener {
    private var Firebasetoken: String = ""

    override fun background() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun foreground() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        dataManager = AppDataManager.getAppDataManager(this)
        preferences = getSharedPreferences( "DownloadPreferance", MODE_PRIVATE);

        Foreground.init(this)
        Foreground.addListener(foregroundListener)
        getFirebaseTocan()

        if (getDataManager().isLoggedIn()&&
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_IS_ADMIN)!!.equals("No", true)) {
            val mWorkManager = WorkManager.getInstance()
            mWorkManager.enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
        }
        else{

        }

       if (getDataManager().getUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION)!=null&&
           !getDataManager().getUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION)!!.isEmpty()&&
           getDataManager().getUserStringData(AppPreferencesHelper.PREF_IS_NOTIFICATION).equals("yes",true)){

           if (!getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.equals("")) {
               var str = getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.split(":")
               Utility.scheduleNotification(this,str[0],str[1]);
           }
           else
           {
               Utility.scheduleNotification(this,"7","0")
           }

       }
        else{
           val myIntent = Intent(this, MyNotificationPublisher::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
           val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
           alarmManager.cancel(pendingIntent)
       }

    }

  /*  override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory();
    }*/


    companion object {
        lateinit var preferences: SharedPreferences
        private lateinit var dataManager: AppDataManager
        var instance: Doviesfitness? = null
        var height: Int = 0
        var weight: Int = 0

        fun getDataManager(): AppDataManager {
            return dataManager
        }
    }

    val foregroundListener = object : Foreground.Listener {
        override fun background() {
            //  Log.e("Foreground", "Go to background")
        }

        override fun foreground() {
            //   Log.e("Foreground", "Go to foreground")
        }
    }

    private fun getFirebaseTocan() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("OutloadApplicationClass", "getInstanceId failed", task.exception)
                    return@addOnCompleteListener
                }

                // Get new Instance ID token
                Firebasetoken = task.result!!.token

                val userInfoBean = getDataManager().getUserInfo()
                userInfoBean.firebaseToken = Firebasetoken
                getDataManager().setUserStringData(
                    AppPreferencesHelper.PREF_FIREBASE_TOKEN,
                    Firebasetoken
                )

                Log.d("tocan", Firebasetoken)
            }
    }

}