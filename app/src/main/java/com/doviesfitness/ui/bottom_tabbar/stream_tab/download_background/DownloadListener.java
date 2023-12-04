package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.app.NotificationChannel;
import android.util.Log;
import android.widget.ProgressBar;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.doviesfitness.Doviesfitness;
import com.doviesfitness.R;
import com.doviesfitness.ui.bottom_tabbar.HomeTabActivity;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.activities.StreamDetailActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
import static com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil.getDownloadManager;
import static com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadUtil.sendLocalBroadcast;

public class DownloadListener {

    public DownloadListener( ProgressBar progressBar) {

        this.progressBar = progressBar;
    }

    private DownloadService downloadService = null;

    private int lastProgress = 0;
    ProgressBar progressBar;

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public void onSuccess()
    {
        downloadService.stopForeground(true);
      //  sendDownloadNotification("Download success.", -1);
    }

    public void onFailed()
    {
        downloadService.stopForeground(true);
       // sendDownloadNotification("Download failed.", -1);
    }
    public void onPaused()
    {
       // sendDownloadNotification("Download paused.", "",lastProgress);
    }
    public void onCanceled()
    {
        downloadService.stopForeground(true);

        sendLocalBroadcast();
        // sendDownloadNotification("Download canceled.", -1);
    }

    public void onUpdateDownloadProgress(int progress,String streamWorkoutId)
    {
        try {
            lastProgress = progress;
            sendDownloadNotification("Downloading...",streamWorkoutId, progress);
            Log.d("DownloadProgress", "onUpdateDownloadProgress..."+progress);

            // Thread sleep 0.2 seconds to let Pause, Continue and Cancel button in notification clickable.
            Thread.sleep(200);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public void sendDownloadNotification(String title, String StreamWorkoutId, int progress)
    {
     //   Notification notification = getDownloadNotification(title, progress);

       // NotificationManager notificationManager = (NotificationManager)downloadService.getSystemService(NOTIFICATION_SERVICE);
       // notificationManager.notify(1, notification);

        Log.d("Downloading", "Downloading progress:.. "+progress);
        Notification notification = getDownloadNotification("Downloading...",StreamWorkoutId, progress);
        getDownloadService().startForeground(1, notification);

    }

    public Notification getDownloadNotification(String title,String StreamWorkoutId, int progress)
    {
/*
        Intent intent = new Intent(Doviesfitness.Companion.getInstance(), StreamDetailActivity.class)
                .putExtra("download","download")
                .putExtra("StreamWorkoutId",StreamWorkoutId);
*/
        Intent intent = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(downloadService, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
      /*  NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(downloadService,"default");
        notifyBuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon);*/

       /* Bitmap bitmap = BitmapFactory.decodeResource(downloadService.getResources(), android.R.drawable.stat_sys_download);
        notifyBuilder.setLargeIcon(bitmap);

        notifyBuilder.setContentIntent(pendingIntent);
        notifyBuilder.setContentTitle(title);
        notifyBuilder.setFullScreenIntent(pendingIntent, true);
        notifyBuilder.setChannelId("10000").setAutoCancel(true);*/



        //////

        String channelId ="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId=  createNotificationChannel("10000", "My Background Service");
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)

        }

        NotificationCompat.Builder notifyBuilder =new  NotificationCompat.Builder(downloadService, channelId );
       // startForeground(101, notification)
        /////

       /* if(progress > 0 && progress < 100)
        {*/
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Download progress ");
            stringBuffer.append(progress);
            stringBuffer.append("%");

            notifyBuilder.setContentText("Download progress " + progress + "%");

            notifyBuilder.setProgress(100, progress, false);

            // Add Pause download button intent in notification.
           /*Intent pauseDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            pauseDownloadIntent.setAction(DownloadService.ACTION_PAUSE_DOWNLOAD);
            PendingIntent pauseDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, pauseDownloadIntent, 0);
            NotificationCompat.Action pauseDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pauseDownloadPendingIntent);
            notifyBuilder.addAction(pauseDownloadAction);

            // Add Continue download button intent in notification.
            Intent continueDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            continueDownloadIntent.setAction(DownloadService.ACTION_CONTINUE_DOWNLOAD);
            PendingIntent continueDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, continueDownloadIntent, 0);
            NotificationCompat.Action continueDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Continue", continueDownloadPendingIntent);
            notifyBuilder.addAction(continueDownloadAction);

            // Add Cancel download button intent in notification.
            Intent cancelDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            cancelDownloadIntent.setAction(DownloadService.ACTION_CANCEL_DOWNLOAD);
            PendingIntent cancelDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, cancelDownloadIntent, 0);
            NotificationCompat.Action cancelDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_delete, "Cancel", cancelDownloadPendingIntent);
            notifyBuilder.addAction(cancelDownloadAction);*/
      //  }
/*
        else{
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Download progress ");
            stringBuffer.append(progress);
            stringBuffer.append("%");

            notifyBuilder.setContentText("Download progress " + progress + "%");

            notifyBuilder.setProgress(100, progress, false);

            // Add Pause download button intent in notification.
            Intent pauseDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            pauseDownloadIntent.setAction(DownloadService.ACTION_PAUSE_DOWNLOAD);
            PendingIntent pauseDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, pauseDownloadIntent, 0);
            NotificationCompat.Action pauseDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pauseDownloadPendingIntent);
            notifyBuilder.addAction(pauseDownloadAction);

            // Add Continue download button intent in notification.
            Intent continueDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            continueDownloadIntent.setAction(DownloadService.ACTION_CONTINUE_DOWNLOAD);
            PendingIntent continueDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, continueDownloadIntent, 0);
            NotificationCompat.Action continueDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Continue", continueDownloadPendingIntent);
            notifyBuilder.addAction(continueDownloadAction);

            // Add Cancel download button intent in notification.
            Intent cancelDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            cancelDownloadIntent.setAction(DownloadService.ACTION_CANCEL_DOWNLOAD);
            PendingIntent cancelDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, cancelDownloadIntent, 0);
            NotificationCompat.Action cancelDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_delete, "Cancel", cancelDownloadPendingIntent);
            notifyBuilder.addAction(cancelDownloadAction);

*/
/*
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifyBuilder.setChannelId("10000");
            }
*//*

        }
*/


/*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("10000", "NOTIFICATION_CHANNEL_NAME", importance);
            assert(getDownloadManager() != null);
            getDownloadManager().createNotificationChannel(notificationChannel);
        }
*/
     //   Notification notification = notifyBuilder.build();

        Notification notification = notifyBuilder.setSmallIcon(R.drawable.transprent_img)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setFullScreenIntent(pendingIntent,true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        return notification;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel( String channelId,  String channelName){
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager)downloadService.getSystemService(NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

}