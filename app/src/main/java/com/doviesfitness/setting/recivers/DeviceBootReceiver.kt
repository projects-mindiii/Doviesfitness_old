package com.doviesfitness.setting.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.AlarmManager
import android.app.PendingIntent
import android.util.Log
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.data.local.AppPreferencesHelper
import java.util.*


class DeviceBootReceiver : BroadcastReceiver(){
     var str= listOf<String>()
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")) {

                // on device boot compelete, reset the alarm
                val alarmIntent = Intent(context, MyNotificationPublisher::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                val manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if(getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!! != ""){
                    str= getDataManager().getUserStringData(AppPreferencesHelper.PREF_REMINDER_TIME)!!.split(":")
                }
                val calendar = Calendar.getInstance()
                calendar.setTimeInMillis(System.currentTimeMillis())
                calendar.set(Calendar.HOUR_OF_DAY, str[0].toInt())
                calendar.set(Calendar.MINUTE, str[1].toInt())
                calendar.set(Calendar.SECOND, 0)
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        }catch (e:Exception){
            Log.d("DeviceBootReceiver", "onReceive: ${e.printStackTrace()}")

        }

    }

}