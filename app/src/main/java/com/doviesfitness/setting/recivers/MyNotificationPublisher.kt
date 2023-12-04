package com.doviesfitness.setting.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager import com.doviesfitness.ui.setting.SettingActivity.Companion.NOTIFICATION_CHANNEL_ID
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getColor
import com.doviesfitness.R
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity


class MyNotificationPublisher : BroadcastReceiver() {
    companion object{
        val NOTIFICATION_ID = "notification-id"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
       val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, HomeTabActivity::class.java)
        val pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context,"default")
            .setSmallIcon(R.drawable.transprent_img)
            .setContentTitle("Get going! It is Workout time!")
            .setColor(getColor(context,R.color.colorBlack1))
            .setContentIntent(pIntent)
            .setChannelId(NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        assert(notificationManager != null)
        notificationManager.notify(id, builder.build())
    }
}