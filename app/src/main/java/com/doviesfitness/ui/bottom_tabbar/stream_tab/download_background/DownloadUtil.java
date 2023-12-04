package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.doviesfitness.BuildConfig;
import com.doviesfitness.Doviesfitness;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import android.app.DownloadManager;


public class DownloadUtil {

    public static final String TAG_DOWNLOAD_MANAGER = "TAG_DOWNLOAD_MANAGER";

    public static final int DOWNLOAD_SUCCESS = 1;

    public static final int DOWNLOAD_FAILED = 2;

    public static final int DOWNLOAD_PAUSED = 3;

    public static final int DOWNLOAD_CANCELED = 4;

    public static Long referanceId = 0l;
    static boolean isReceiverRegistered = false;
    static BroadcastReceiver mBroadCastReceiver;
    public static DownloadManager MDownloadManager;
    static boolean flag = false;
    static ArrayList<Long> mFinishedFilesFromNotification = new ArrayList<Long>();
   public static ArrayList<Long> list = new ArrayList<Long>();
    static ArrayList<DownloadedModal.ProgressModal> progressList = new ArrayList<>();
    static Thread mProgressThread;
    private static CustomDownloadManager customdownloadManager = null;
    static DownloadedModal.ProgressModal progressModal;
    public static File localFile;
    public static Handler handler = new Handler();
    public static Runnable runnable;
    public static DownloadListener mDownloadListener;
    public static String reasonText="";
    public static CustomDownloadManager getDownloadManager() {
        return customdownloadManager;
    }

    public static void setDownloadManager(CustomDownloadManager downloadManager, DownloadListener downloadListener) {
        DownloadUtil.customdownloadManager = downloadManager;
        mDownloadListener = downloadListener;
        MDownloadManager = (DownloadManager) Doviesfitness.Companion.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
    }
    public static void setData(ArrayList<DownloadedModal.ProgressModal> list) {
        try {
            Gson gson = new Gson();
            String category = gson.toJson(list);
            Doviesfitness.preferences.edit().putString("video", category).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setDownloadedData(ArrayList<DownloadedModal> list) {
        try {
            Gson gson = new Gson();
            String category = gson.toJson(list);
            Doviesfitness.preferences.edit().putString("downloaded", category).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUrlToList(DownloadedModal.ProgressModal modal) {
        setData(modal, "video");
    }

    public static void sendLocalBroadcast() {

        if (progressModal != null) {
            String str = localFile.getAbsolutePath();

            File PFile = new File(str);
            String isDeleted = "yes";
            if (PFile.exists()) {
                PFile.delete();
                isDeleted = "yes";
            }
            Intent intent = new Intent("download_progress");
            intent.putExtra("position", progressModal.getPosition());
            intent.putExtra("cancel", "yes");
            intent.putExtra("deleted", isDeleted);
            intent.putExtra("file", str);
            intent.putExtra("workout_id", progressModal.getWorkout_id());
            intent.putExtra("order", progressModal.getOrder());
            LocalBroadcastManager.getInstance(Doviesfitness.Companion.getInstance()).sendBroadcast(intent);

        }

    }

    public static void setData(DownloadedModal.ProgressModal modal, String listName) {

        Gson gson1 = new Gson();
        String valueJson = Doviesfitness.preferences.getString(listName, "");
        if (!valueJson.isEmpty()) {
            List<DownloadedModal.ProgressModal> videos = gson1.fromJson(valueJson, new TypeToken<List<DownloadedModal.ProgressModal>>() {
            }.getType());
            videos.add(modal);
            try {
                Gson gson = new Gson();
                String category = gson.toJson(videos);
                Doviesfitness.preferences.edit().putString(listName, category).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                Gson gson = new Gson();
                List<DownloadedModal.ProgressModal> videos = new ArrayList<>();
                videos.add(modal);
                String category = gson.toJson(videos);
                Doviesfitness.preferences.edit().putString(listName, category).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static void setDownloadedData(DownloadedModal modal, String listName) {

        Gson gson1 = new Gson();
        String valueJson = Doviesfitness.preferences.getString(listName, "");
        if (!valueJson.isEmpty()) {
            List<DownloadedModal> videos = gson1.fromJson(valueJson, new TypeToken<List<DownloadedModal>>() {
            }.getType());
            videos.add(modal);
            try {
                Gson gson = new Gson();
                String category = gson.toJson(videos);
                Doviesfitness.preferences.edit().putString(listName, category).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                Gson gson = new Gson();
                List<DownloadedModal> videos = new ArrayList<>();
                videos.add(modal);
                String category = gson.toJson(videos);
                Doviesfitness.preferences.edit().putString(listName, category).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static ArrayList<DownloadedModal.ProgressModal> getData(String listName) {

        Gson gson1 = new Gson();
        String valueJson = Doviesfitness.preferences.getString(listName, "");
        ArrayList<DownloadedModal.ProgressModal> videos = gson1.fromJson(valueJson, new TypeToken<List<DownloadedModal.ProgressModal>>() {
        }.getType());
        return videos;
    }

    public static ArrayList<DownloadedModal> getDownloadedData(String listName) {

        Gson gson1 = new Gson();
        String valueJson = Doviesfitness.preferences.getString(listName, "");
        ArrayList<DownloadedModal> videos = gson1.fromJson(valueJson, new TypeToken<List<DownloadedModal>>() {
        }.getType());
        return videos;
    }


    public static File createDownloadLocalFile(String downloadFileUrl) {
        File ret = null;
        String customerName = Doviesfitness.Companion.getDataManager().getUserInfo().getCustomer_user_name();
        try {
            if (downloadFileUrl != null && !TextUtils.isEmpty(downloadFileUrl)) {

                Log.v("Check ", "Which Url Downlod.." + downloadFileUrl);
                int lastIndex = downloadFileUrl.lastIndexOf("/");
                if (lastIndex > -1) {
                    String downloadFileName = downloadFileUrl.substring(lastIndex + 1);
                    String[] extensionName = downloadFileName.split("_");
                    String lastIndexName = extensionName[0];
                    Log.v("lastIndexName", "" + lastIndexName);
                    File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + lastIndexName;
                    Log.v("path", "" + path);
                    ret = new File(path);

                }
            }
        } catch (Exception ex) {
            Log.e(DownloadUtil.TAG_DOWNLOAD_MANAGER, ex.getMessage(), ex);
        } finally {
            return ret;
        }
    }

    public static void deleteDownload() {
        MDownloadManager.remove(referanceId);
        list.remove(referanceId);
        try {
            Doviesfitness.Companion.getInstance().unregisterReceiver(mBroadCastReceiver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isReceiverRegistered = false;

        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

        flag = false;
        MDownloadManager = null;

    }

    public static int downloadExercise(String videoUrl, int position, View view, Boolean scroll) {
        flag = true;
        int ret = DOWNLOAD_SUCCESS;
        if (isReceiverRegistered == true) {
            try {
                Log.e("mBroadCastReceiver....", "mBroadCastReceiver..." + mBroadCastReceiver);
                Doviesfitness.Companion.getInstance().unregisterReceiver(mBroadCastReceiver);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        progressList = getData("video");
        int lastIndex = videoUrl.lastIndexOf("/");
        if (lastIndex > -1) {
            MDownloadManager = (DownloadManager) Doviesfitness.Companion.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
            String customerName = Doviesfitness.Companion.getDataManager().getUserInfo().getCustomer_user_name();

            String downloadFileName = videoUrl.substring(lastIndex + 1);

            String[] extensionName = downloadFileName.split("_");
            String lastIndexName = extensionName[0]+".mp4";

            String subPath = "/Dovies/" + customerName + "//" + lastIndexName;

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + BuildConfig.APPLICATION_ID + "/files/." + Environment.DIRECTORY_DOWNLOADS + "/Dovies/" + customerName + "//" + lastIndexName;
            Log.v("Downloaded_path", "" + path);

            File f = new File(path);
            Uri Download_Uri = Uri.parse(videoUrl);
            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("Dovies Downloading .mp4");
            request.setDescription("Downloading .mp4");
            request.setVisibleInDownloadsUi(false);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalFilesDir(Doviesfitness.Companion.getInstance(), "/." + Environment.DIRECTORY_DOWNLOADS, subPath);
            referanceId = MDownloadManager.enqueue(request);
            list.clear();
            list.add(referanceId);
            ret = startDownloadThread(videoUrl, path, view, scroll);
        }

        return ret;
    }

    private static int startDownloadThread(
            String videoUrl,
            String path,
            View view, Boolean scroll
    ) {

        mBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isReceiverRegistered = true;
                flag = false;
                Log.e("on Receive....", "" + intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                Log.e("on Receive....", "on Receive reason..."+reasonText );
                if (progressList != null && progressList.size() > 0) {
                    //  getDownloadManager().updateTaskProgress(100, progressList.get(0).getWorkout_id());
                    progressModal = progressList.get(0);
                    Intent intent1 = new Intent("download_progress");
                    intent1.putExtra("position", progressList.get(0).getPosition());
                    intent1.putExtra("progress", String.valueOf(100));
                    intent1.putExtra("workout_id", progressList.get(0).getWorkout_id());
                    intent1.putExtra("order", progressList.get(0).getOrder());
                    LocalBroadcastManager.getInstance(Doviesfitness.Companion.getInstance()).sendBroadcast(intent1);
                    progressList = getData("video");
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (progressList != null && progressList.size() > 0) {
                                DownloadedModal.ProgressModal downloadedModel = progressList.remove(0);

                                if (mProgressThread != null) {
                                    if (progressList.size() > 0) {
                                        setData(progressList);
                                        downloadExercise(progressList.get(0).getVideoUrl(), 0, null, null);
                                    } else {
                                        Doviesfitness.preferences.edit().putString("video", "").commit();
                                        Doviesfitness.Companion.getInstance().unregisterReceiver(mBroadCastReceiver);

                                        isReceiverRegistered = false;
                                    }
                                    handler.removeCallbacks(this);
                                }

                                downloadedModel.setDownLoadUrl("" + path);
                                downloadedModel.setAddedQueue(false);
                                ArrayList<DownloadedModal.ProgressModal> DList = new ArrayList<>();
                                DList.add(downloadedModel);
                                DownloadedModal DModel = new DownloadedModal("", "", "", "", "", "", "",
                                        "", downloadedModel.getStream_workout_description(), downloadedModel.getWorkout_id(), downloadedModel.getStream_workout_image(),
                                        downloadedModel.getStream_workout_image_url(), downloadedModel.getStream_workout_name(), "", "", "",
                                        "", "", "", DList, "", "");


                                ArrayList<DownloadedModal> getDList = new ArrayList();
                                if (getDownloadedData("downloaded") != null) {
                                    getDList = getDownloadedData("downloaded");
                                    boolean isAdded = false;
                                    if (getDList.size() > 0) {
                                        for (int j = 0; j < getDList.size(); j++) {
                                            if (getDList.get(j).getStream_workout_id().equals(downloadedModel.getWorkout_id())) {
                                                getDList.get(j).getDownload_list().add(downloadedModel);
                                                setDownloadedData(getDList);
                                                isAdded = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!isAdded) {
                                        setDownloadedData(DModel, "downloaded");
                                    }
                                } else {
                                    setDownloadedData(DModel, "downloaded");
                                }

                            }

                        }
                    };

                    handler.postDelayed(runnable, 1000);

                }

                mFinishedFilesFromNotification.add(intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                Long referenceId = intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.e("referenceId IN", "referenceId..." + referenceId);
                list.remove(referenceId);
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        Doviesfitness.Companion.getInstance().registerReceiver(mBroadCastReceiver, intentFilter);


        mProgressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    long[] ids = new long[list.size()];
                    ArrayList idsArrList = new ArrayList<Long>();
                    int id = 0;
                    for (int i = 0; i < list.size(); i++) {
                        ids[id++] = list.get(i);
                        idsArrList.add(list.get(i));
                    }
                    //  long lid=ids;
                    q.setFilterById(ids);
                    // q.setFilterById(*ids);
                    // getting the total size of the data ...
                    Cursor c;

                    while (true) {
                        if (flag) {
                            // check if the downloads are already completed ...
                            // Here I have created a set of download ids from download manager to keep
                            // track of all the files that are dowloaded, which I populate by creating
                            //
                            if (mFinishedFilesFromNotification.containsAll(idsArrList)) {
                                Log.d("all downloads","all downloads..."+mFinishedFilesFromNotification.size()+"..."+idsArrList.size());
                                //  isDownloadSuccess = true
                                // TODO - Take appropriate action. Download is finished successfully
                                return;
                            }
                            // start iterating and noting progress ..

                            if (MDownloadManager != null && q != null) {
                                c = MDownloadManager.query(q);
                                if (c != null) {
                                    int filesDownloaded = 0;
                                    Float fileFracsion = 0f;// this stores the fraction of all the files in
                                    // download
                                    int columnTotalSize = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                                    int columnStatus = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                    //final int columnId = c.getColumnIndex(DownloadManager.COLUMN_ID);
                                    int columnDwnldSoFar =
                                            c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

                                    while (c.moveToNext()) {
                                        // checking the progress ..
                                        if (c.getInt(columnStatus) == DownloadManager.STATUS_SUCCESSFUL) {
                                            filesDownloaded++;
                                            reasonText = "STATUS_SUCCESSFUL";
                                        }
                                        else if (c.getInt(columnTotalSize) > 0) {
                                            fileFracsion += c.getInt(columnDwnldSoFar) * 1.0f / c.getInt(
                                                    columnTotalSize
                                            );
                                            reasonText = "STATUS_DOWNLOADING";
                                        }
                                        else if (c.getInt(columnStatus) == DownloadManager.STATUS_FAILED) {
                                            // TODO - Take appropriate action. Error in downloading one of the
                                            reasonText = "ERROR_STATUS_FAILED";
                                            return;
                                        }
                                        else if (c.getInt(columnStatus) == DownloadManager.ERROR_CANNOT_RESUME) {
                                          //  reasonText = "ERROR_CANNOT_RESUME";
                                            reasonText = "ERROR_CANNOT_RESUME";
                                            return;
                                        }
                                        else if (c.getInt(columnStatus) == DownloadManager.ERROR_DEVICE_NOT_FOUND) {
                                            // TODO - Take appropriate action. Error in downloading one of the
                                            reasonText = "ERROR_DEVICE_NOT_FOUND";
                                            return;
                                        }
                                        else {
                                            reasonText = "STATUS_Error";
                                        }

                                    }
                                    c.close();
                                    // calculate the progress to show ...
                                    Float progress = (filesDownloaded + fileFracsion) / ids.length;

                                    // setting the progress text and bar...
                                    float percentage = Math.round(progress * 100.0f);
                                    String txt = "Loading ... " + percentage + "%";
                                    Log.d("progress...", "progress..." + txt);
                                    int downloadProgress = (int) percentage;


                                    if (progressList != null && progressList.size() > 0 && downloadProgress > 0) {
                                        progressModal = progressList.get(0);
                                        //  getDownloadManager().updateTaskProgress(downloadProgress, progressList.get(0).getWorkout_id());
                                        Intent intent = new Intent("download_progress");
                                        intent.putExtra("position", progressModal.getPosition());
                                        intent.putExtra("progress", String.valueOf(downloadProgress));
                                        intent.putExtra("workout_id", progressModal.getWorkout_id());
                                        intent.putExtra("order", progressModal.getOrder());
                                        LocalBroadcastManager.getInstance(Doviesfitness.Companion.getInstance()).sendBroadcast(intent);
                                    }
                                    // Show the progress appropriately ...
                                }
                            }
                        }
                    }
                }
            }
        });
        mProgressThread.start();
        return 0;
    }

}