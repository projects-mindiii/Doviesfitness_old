package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.doviesfitness.BuildConfig;
import com.doviesfitness.Doviesfitness;

import java.io.File;
import java.io.IOException;
public class CustomDownloadManager extends AsyncTask<String, Integer, Integer> {

    private DownloadListener downloadListener = null;

    private boolean downloadCanceled = false;

    private boolean downloadPaused = false;

    private int lastDownloadProgress = 0;

    public String currDownloadUrl = "";

    public boolean isDownloadCanceled() {
        return downloadCanceled;
    }

    public void setDownloadCanceled(boolean downloadCanceled) {
        this.downloadCanceled = downloadCanceled;
    }

    public boolean isDownloadPaused() {
        return downloadPaused;
    }

    public void setDownloadPaused(boolean downloadPaused) {
        this.downloadPaused = downloadPaused;
    }

    public int getLastDownloadProgress() {
        return lastDownloadProgress;
    }

    public void setLastDownloadProgress(int lastDownloadProgress) {
        this.lastDownloadProgress = lastDownloadProgress;
    }

    public CustomDownloadManager(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        this.setDownloadPaused(false);
        this.setDownloadCanceled(false);
    }

    /* This method is invoked after doInBackground() method. */
    @Override
    protected void onPostExecute(Integer downloadStatue) {
        if(downloadStatue == DownloadUtil.DOWNLOAD_SUCCESS)
        {
            this.setDownloadCanceled(false);
            this.setDownloadPaused(false);
            downloadListener.onSuccess();
            Log.d("Downloaded", "Downloaded...");
           // this.execute("");

        }else if(downloadStatue == DownloadUtil.DOWNLOAD_FAILED)
        {
            this.setDownloadCanceled(false);
            this.setDownloadPaused(false);
            downloadListener.onFailed();
        }else if(downloadStatue == DownloadUtil.DOWNLOAD_PAUSED)
        {
            downloadListener.onPaused();
        }else if(downloadStatue == DownloadUtil.DOWNLOAD_CANCELED)
        {
            downloadListener.onCanceled();
        }
    }


    /* Invoked when this async task execute.When this method return, onPostExecute() method will be called.*/
    @Override
    protected Integer doInBackground(String... params) {

        // Set current thread priority lower than main thread priority, so main thread Pause, Continue and Cancel action will not be blocked.
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY - 2);

        String downloadFileUrl = "";
        if(params!=null && params.length > 0)
        {
            downloadFileUrl = params[0];
        }

        currDownloadUrl = downloadFileUrl;

       // File downloadLocalFile = createDownloadLocalFile(downloadFileUrl);

        //int ret = DownloadUtil.downloadFileFromUrl(downloadFileUrl, downloadLocalFile);
        //int ret = DownloadUtil.downloadFileFromUrl(downloadFileUrl, downloadLocalFile);
      int ret=  DownloadUtil.downloadExercise(downloadFileUrl,0,null,null);

        return ret;
    }

    /*
     * Parse the download file name from the download file url,
     * check whether the file exist in sdcard download directory or not.
     * If the file do not exist then create it.
     *
     * Return the file object.
     * */
    private File createDownloadLocalFile(String downloadFileUrl)
    {
        File ret = null;

       String customerName= Doviesfitness.Companion.getDataManager().getUserInfo().getCustomer_user_name();

        try {
            if (downloadFileUrl != null && !TextUtils.isEmpty(downloadFileUrl)) {
                int lastIndex = downloadFileUrl.lastIndexOf("/");
                if (lastIndex > -1) {
                    String downloadFileName = downloadFileUrl.substring(lastIndex + 1);
                    String downloadDirectoryName = Environment.DIRECTORY_DOWNLOADS;
                    File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    String downloadDirectoryPath = downloadDirectory.getPath();






                    String downloadFileName1 = downloadFileUrl.substring(lastIndex + 1);
                  //  val subpath = "/Dovies//$downloadFileName"
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" +
                            BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/" + customerName+"//" + downloadFileName1 ;

                 //   ret = new File(downloadDirectoryPath + "/" + downloadFileName);
                    ret = new File(path);

                    if (! ret.getParentFile().exists())
                    {
                        ret.getParentFile().mkdirs();
                    }
                    if (!ret.exists()) {
                        ret.createNewFile();
                    }
                    Log.d("path", "path.....: "+ret);
                }
            }
        }catch(IOException ex)
        {
            Log.e(DownloadUtil.TAG_DOWNLOAD_MANAGER, ex.getMessage(), ex);
        }finally {
            return ret;
        }
    }

    /* Update download async task progress. */
    public void updateTaskProgress(Integer newDownloadProgress,String streamWorkoutId)
    {
        lastDownloadProgress = newDownloadProgress;
        downloadListener.onUpdateDownloadProgress(newDownloadProgress,streamWorkoutId);
    }

    public void pauseDownload()
    {
        this.setDownloadPaused(true);
    }

    public void cancelDownload()
    {
        this.setDownloadCanceled(true);
    }
}