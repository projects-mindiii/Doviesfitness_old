package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background;

import android.os.Binder;
import android.text.TextUtils;
import android.widget.ProgressBar;

public class DownloadBinder extends Binder {

    public String currDownloadUrl = "";
    private CustomDownloadManager downloadManager = null;
    private DownloadListener downloadListener = null;
    private final ProgressBar progressBar = null;
    private String StreamWorkoutId = "";

    public DownloadBinder() {

        if (downloadListener == null) {
            downloadListener = new DownloadListener(progressBar);
        }
    }

    public CustomDownloadManager getDownloadManager() {
        return downloadManager;
    }

    public void startDownload(String downloadUrl, String Stream_workout_id, int progress/*,ProgressBar progressBar*/) {

        /* Because downloadManager is a subclass of AsyncTask, and AsyncTask can only be executed once,
         * So each download need a new downloadManager. */
        downloadManager = new CustomDownloadManager(downloadListener);
        downloadManager.currDownloadUrl = downloadUrl;
        //  this.progressBar=progressBar;

        /* Because DownloadUtil has a static variable of downloadManger, so each download need to use new downloadManager. */
        DownloadUtil.setDownloadManager(downloadManager, downloadListener);

        // Execute download manager, this will invoke downloadManager's doInBackground() method.
        downloadManager.execute(downloadUrl);

        // Save current download file url.
        currDownloadUrl = downloadUrl;
        StreamWorkoutId = Stream_workout_id;

        // Create and start foreground service with notification.
        //  Notification notification = downloadListener.getDownloadNotification("Downloading...",StreamWorkoutId, progress);
        //  downloadListener.getDownloadService().startForeground(1, notification);

    }

    public void continueDownload() {
        if (currDownloadUrl != null && !TextUtils.isEmpty(currDownloadUrl)) {
            int lastDownloadProgress = downloadManager.getLastDownloadProgress();
            startDownload(currDownloadUrl, StreamWorkoutId, lastDownloadProgress/*progressBar*/);
        }
    }

    public void cancelDownload() {
        if (downloadManager != null)
            downloadManager.cancelDownload();
    }

    public void pauseDownload() {
        downloadManager.pauseDownload();
    }

    public DownloadListener getDownloadListener() {
        return downloadListener;
    }

    public void setUrlToList(DownloadedModal.ProgressModal modal) {
        DownloadUtil.setUrlToList(modal);

    }
}