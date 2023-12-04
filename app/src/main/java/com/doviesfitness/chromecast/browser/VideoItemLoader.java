/*
 * Copyright 2019 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doviesfitness.chromecast.browser;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.AsyncTaskLoader;

import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogModel;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamPlayedHistoryModel;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.PinnedWorkout;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.VideoListItem;
import com.google.android.gms.cast.MediaInfo;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An {@link AsyncTaskLoader} that loads the list of videos in the background.
 */
public class VideoItemLoader extends AsyncTaskLoader<List<MediaInfo>> {

    private static final String TAG = "VideoItemLoader";
    private String mUrl="";
    private  List<VideoListItem> videoList;
    private  PinnedWorkout pinnedWorkout;
    private  StreamPlayedHistoryModel.Data historyData;
    private   StreamLogModel.Data loglist;
    private  DownloadedModal.ProgressModal downloadedVideo;
    private String from="";
    private String imageUrl="";

    public VideoItemLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
        from="dummy";
    }

    public VideoItemLoader(Context context, @Nullable List<VideoListItem> videoList,String imageUrl1) {
        super(context);
        this.videoList = videoList;
        from="detail";
        imageUrl=imageUrl1;
    }

    public VideoItemLoader(Context context, @Nullable PinnedWorkout pinnedWorkout,String imageUrl1) {
        super(context);
        this.pinnedWorkout = pinnedWorkout;
        from="collection";
        imageUrl=imageUrl1;
    }
    public VideoItemLoader(Context context, @Nullable StreamPlayedHistoryModel.Data historyData,String imageUrl1) {
        super(context);
        this.historyData = historyData;
        from="history";
        imageUrl=imageUrl1;
    }
    public VideoItemLoader(Context context, @Nullable StreamLogModel.Data loglist, String imageUrl1) {
        super(context);
        this.loglist = loglist;
        from="loglist";
        imageUrl=imageUrl1;
    }


    public VideoItemLoader(Context context, DownloadedModal.ProgressModal downloadedVideo, String imageUrl){
        super(context);
        this.downloadedVideo = downloadedVideo;
        from="downloadedActivity";
        this.imageUrl=imageUrl;
    }



    @Override
    public List<MediaInfo> loadInBackground() {
        try {
          //  return VideoProvider.buildMedia(mUrl);
            if (from.equals("detail"))
            return VideoProvider.buildMedia1(videoList,imageUrl);
           else if (from.equals("history"))
            return VideoProvider.buildMedia3(historyData,imageUrl);
          else if (from.equals("dummy"))
            return VideoProvider.buildMedia(mUrl);
          else if(from.equals("downloadedActivity")){
                Log.e("CreateLoader Call-->","downloadedActivity ");
                Log.e("CreateLoader Call-->","imageUrl"+imageUrl);
                return VideoProvider.buildMedia4(downloadedVideo,imageUrl);
            }    else if(from.equals("loglist")){
                Log.e("CreateLoader Call-->","downloadedActivity ");
                Log.e("CreateLoader Call-->","imageUrl"+imageUrl);
                return VideoProvider.buildMedia5(loglist,imageUrl);
            }
            else
            return VideoProvider.buildMedia2(pinnedWorkout,imageUrl);

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch media data", e);
            Log.e("CreateLoader Call-->","loadInBackground "+" "+e.getMessage());
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

}
