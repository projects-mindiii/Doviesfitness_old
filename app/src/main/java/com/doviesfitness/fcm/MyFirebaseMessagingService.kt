package com.doviesfitness.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import com.doviesfitness.Doviesfitness.Companion.getDataManager
import com.doviesfitness.R
import com.doviesfitness.data.local.AppPreferencesHelper
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
class MyFirebaseMessagingService : FirebaseMessagingService() {
    internal var CHANNEL_ID = "com.doviesfitness"// The id of the channel.
    private lateinit var notificationModal: NotificationModal

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.v(
            "device_token1",
            getDataManager().getUserStringData(AppPreferencesHelper.PREF_FIREBASE_TOKEN)!!
        )

        Log.v(TAG, "Body: " + remoteMessage.data.toString())
        Log.v(TAG, "Body1: $remoteMessage")
       /* var intent1 = Intent(this, HomeTabActivity::class.java)
        sendBroadcast(intent1)*/

        var str=getDataManager().getUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT)
        if (!str!!.isEmpty()){
            var count= str.toInt()
            Log.v("count", "count.......$count")
            var MCount = (count + 1)
            getDataManager().setUserStringData(AppPreferencesHelper.PREF_KEY_APP_CUSTOMER_NOTIFICATION_COUNT,MCount.toString())

        }

        val intent1 = Intent("INTENT_FILTER_NAME")
        intent1.putExtra("key", "YOUR_EXTRA_DATA_WOULD_BE_HERE")
        sendBroadcast(intent1)

/* code for play store pending issue resolving purpose
Intent base = new Intent("ACTION_FOO");

base.setPackage("some_package");
PendingIntent pi = PendingIntent.getService(this, 0, base, 0);*/
        if (remoteMessage.data.containsKey("module_id")) {
            notificationModal = NotificationModal()
            notificationModal.mediaUrl = remoteMessage.data["mediaUrl"].toString()
            notificationModal.module_name = remoteMessage.data["module_name"].toString()
            notificationModal.body = "" + remoteMessage.notification?.body
            notificationModal.module_id = remoteMessage.data["module_id"].toString()
        }


        var intent: Intent? = null

        if ("CUSTOM".equals(notificationModal.module_name)) {

            if (!notificationModal.module_id.isEmpty()) {
                intent = Intent(this, HomeTabActivity::class.java)
                //  intent.setPackage(packageName); //play store pending issue
               // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("FromNotificaton", notificationModal)
                sendNotification(remoteMessage.ttl, intent, "CUSTOM", notificationModal)

            }
        }else if("welCome".equals(notificationModal.module_name)){
            if (!notificationModal.module_id.isEmpty()) {
                intent = Intent(this, HomeTabActivity::class.java)

              //  intent.setPackage(packageName); //play store pending issue

                intent.putExtra("FromNotificaton", notificationModal)
                sendNotification(remoteMessage.ttl, intent, "welCome", notificationModal)
            }
        } else {
            if (!notificationModal.module_id.isEmpty()) {
                intent = Intent(this, HomeTabActivity::class.java)
                //  intent.setPackage(packageName); //play store pending issue
                intent.putExtra("FromNotificaton", notificationModal)
                sendNotification(remoteMessage.ttl, intent, "others", notificationModal)
            }
        }
    }

/*
    Bundle[
    {
        google.delivered_priority = high,
        google.sent_time = 1574313049901,
        module_id = 0,
        google.ttl = 2419200,
        google.original_priority = high,
        gcm.notification.e = 1,
        module_name = welCome,
        gcm.notification.module_id = 0,
        mediaUrl =, gcm.notification.sound = default,
        gcm.notification.module_name = welCome,
        body = Time to create and share your workout with friends and family,
        from = 971897138475,
        name =, gcm.notification.sound2 = default,
        sound = default,
        title =, click_action = ChatActivity,
        google.message_id = 0:1574313049918903%108564b4108564b4,
        gcm.notification.body = Time to create and share your workout with friends and family,
        gcm.notification.name =, gcm.notification.mediaUrl =,
        google.c.a.e = 1, gcm.notification.click_action = ChatActivity,
        collapse_key = com.doviesfitness.debug
    }]
*/


   /* private fun sendBroadCast() {
        val intent = Intent("BadgeCount")
        intent.putExtra("","")

        sendBroadcast(intent)
    }*/


    //    27/dec/2018 Changes
    private fun sendNotification(id: Int, intent: Intent?, forWhich: String, notificationModal: NotificationModal?) {
        var pendingIntent: PendingIntent? = null
        val notificationBuilder: NotificationCompat.Builder
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        //sendBroadCast()

        if (forWhich.equals("CUSTOM")) {
            notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.transprent_img)
                .setColor(getResources().getColor(R.color.colorBlack))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.transprent_img))
                .setShowWhen(false)
                .setContentTitle(notificationModal!!.module_name)
                .setContentText(notificationModal.body)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        } else if (forWhich.equals("welCome")){
            notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.transprent_img)
                .setColor(getResources().getColor(R.color.colorBlack))
                .setContentText(notificationModal!!.body)
                .setSmallIcon(R.drawable.transprent_img)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationModal.body))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        }else{
            notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.transprent_img)
                .setColor(getResources().getColor(R.color.colorBlack))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.transprent_img))
                .setShowWhen(false)
                .setContentText(notificationModal!!.body)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        }


            //.setContentTitle(notificationModal!!.module_name)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "MyChannal"// The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel: NotificationChannel? = null

            mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(mChannel)
        }
        assert(notificationManager != null)
        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {

        private val TAG = "MyFirebaseMsgService"
    }
}