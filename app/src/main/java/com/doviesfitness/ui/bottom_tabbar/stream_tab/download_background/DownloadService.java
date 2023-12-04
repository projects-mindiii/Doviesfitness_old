package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.doviesfitness.Doviesfitness;
import com.doviesfitness.data.local.AppPreferencesHelper;

import java.util.ArrayList;

public class DownloadService extends Service {

    public static final String ACTION_PAUSE_DOWNLOAD = "ACTION_PAUSE_DOWNLOAD";

    public static final String ACTION_CONTINUE_DOWNLOAD = "ACTION_CONTINUE_DOWNLOAD";

    public static final String ACTION_CANCEL_DOWNLOAD = "ACTION_CANCEL_DOWNLOAD";

    private DownloadBinder downloadBinder = new DownloadBinder();

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        downloadBinder.getDownloadListener().setDownloadService(this);
        return downloadBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // displayNumber();

        if (intent.getAction()!=null){
            String action = intent.getAction();
            if(ACTION_PAUSE_DOWNLOAD.equals(action))
            {
                downloadBinder.pauseDownload();
                //  Toast.makeText(getApplicationContext(), "Download is paused", Toast.LENGTH_LONG).show();
            }else if(ACTION_CANCEL_DOWNLOAD.equals(action))
            {
                downloadBinder.cancelDownload();
                Log.d("cancelDownload", "cancelDownload:.. ");

                //  Toast.makeText(getApplicationContext(), "Download is canceled", Toast.LENGTH_LONG).show();
            }else if(ACTION_CONTINUE_DOWNLOAD.equals(action))
            {
                downloadBinder.continueDownload();
                Doviesfitness.Companion.getDataManager().setUserStringData("IS_RESUME","pause");

                //  Toast.makeText(getApplicationContext(), "Download continue", Toast.LENGTH_LONG).show();
            }

            // return super.onStartCommand(intent, flags, startId);
        }
        return START_STICKY;

    }

/*
    public void deleteFromVideo(){

        ArrayList<DownloadedModal.ProgressModal> AList = DownloadUtil.getData("video");
        AList.remove(0);
        if (AList.size() > 0) {
            DownloadUtil.setData(AList);
            if (AList != null && AList.size() > 0) {
                downloadBinder.startDownload(AList.get(0).getVideoUrl(),AList.get(0).getWorkout_id(),0);
*/
/*
                    Doviesfitness.Companion.getDataManager().setUserStringData( AppPreferencesHelper.STEAM_WORKOUT_ID,
                            AList.get(0).getWorkout_id());
*//*

                    }
                } else {
                    Doviesfitness.preferences.edit()
                            .putString("video", "").commit();
                }
            }
*/
        }