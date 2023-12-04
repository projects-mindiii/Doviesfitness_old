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

import android.net.Uri;
import android.util.Log;

import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamLogModel;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.StreamPlayedHistoryModel;
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.PinnedWorkout;
import  com.doviesfitness.ui.bottom_tabbar.stream_tab.model.VideoListItem;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.common.images.WebImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provider of the list of videos.
 */
public class VideoProvider {

    private static final String TAG = "VideoProvider";
    private static final String TAG_VIDEOS = "videos";
    private static final String TAG_HLS = "hls";
    private static final String TAG_DASH = "dash";
    private static final String TAG_MP4 = "mp4";
    private static final String TAG_IMAGES = "images";
    private static final String TAG_VIDEO_TYPE = "type";
    private static final String TAG_VIDEO_URL = "url";
    private static final String TAG_VIDEO_MIME = "mime";

    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_NAME = "name";
    private static final String TAG_STUDIO = "studio";
    private static final String TAG_SOURCES = "sources";
    private static final String TAG_SUBTITLE = "subtitle";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_TRACKS = "tracks";
    private static final String TAG_TRACK_ID = "id";
    private static final String TAG_TRACK_TYPE = "type";
    private static final String TAG_TRACK_SUBTYPE = "subtype";
    private static final String TAG_TRACK_CONTENT_ID = "contentId";
    private static final String TAG_TRACK_NAME = "name";
    private static final String TAG_TRACK_LANGUAGE = "language";
    private static final String TAG_THUMB = "image-480x270"; // "thumb";
    private static final String TAG_IMG_780_1200 = "image-780x1200";
    private static final String TAG_TITLE = "title";

    public static final String KEY_DESCRIPTION = "description";

    private static final String TARGET_FORMAT = TAG_HLS;
    private static List<MediaInfo> mediaList;

    protected JSONObject parseUrl(String urlString) {
        InputStream is = null;
        try {
            java.net.URL url = new java.net.URL(urlString);
            URLConnection urlConnection = url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "iso-8859-1"), 1024);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return new JSONObject(json);
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse the json for media list", e);
            return null;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static List<MediaInfo> buildMedia(String url) throws JSONException {

        if (null != mediaList) {
            return mediaList;
        }
        Map<String, String> urlPrefixMap = new HashMap<>();
        mediaList = new ArrayList<>();
        JSONObject jsonObj = new VideoProvider().parseUrl(url);
        JSONArray categories = jsonObj.getJSONArray(TAG_CATEGORIES);
        if (null != categories) {
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
                urlPrefixMap.put(TAG_HLS, category.getString(TAG_HLS));
                urlPrefixMap.put(TAG_DASH, category.getString(TAG_DASH));
                urlPrefixMap.put(TAG_MP4, category.getString(TAG_MP4));
                urlPrefixMap.put(TAG_IMAGES, category.getString(TAG_IMAGES));
                urlPrefixMap.put(TAG_TRACKS, category.getString(TAG_TRACKS));
                category.getString(TAG_NAME);
                JSONArray videos = category.getJSONArray(TAG_VIDEOS);
                if (null != videos) {
                    for (int j = 0; j < videos.length(); j++) {
                        String videoUrl = null;
                        String mimeType = null;
                        JSONObject video = videos.getJSONObject(j);
                        String subTitle = video.getString(TAG_SUBTITLE);
                        JSONArray videoSpecs = video.getJSONArray(TAG_SOURCES);
                        if (null == videoSpecs || videoSpecs.length() == 0) {
                            continue;
                        }
                        for (int k = 0; k < videoSpecs.length(); k++) {
                            JSONObject videoSpec = videoSpecs.getJSONObject(k);
                            if (TARGET_FORMAT.equals(videoSpec.getString(TAG_VIDEO_TYPE))) {
/*
                                videoUrl = urlPrefixMap.get(TARGET_FORMAT) + videoSpec
                                        .getString(TAG_VIDEO_URL);
*/
                                //    videoUrl="https://d1n9vl26vbyc5s.cloudfront.net/stream_video/8.571937196955432.mp4";
                                //   videoUrl="https://d1n9vl26vbyc5s.cloudfront.net/stream_video/15.620846808822076.mp4";
                                videoUrl = "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/8.571937196955432_cbr_spHQ_360p.mp4";
                                //    videoUrl="https://d1n9vl26vbyc5s.cloudfront.net/stream_video/hls/8.571937196955432.m3u8";

                                //  mimeType = videoSpec.getString(TAG_VIDEO_MIME);
                                //  mimeType = "application/x-mpegurl";
                                mimeType = "videos/mp4";

                                Log.d("video url and formate", "video url..." + videoUrl + "...formate.." + mimeType);
                            }
                        }
                        if (videoUrl == null) {
                            continue;
                        }
                        String imageUrl = urlPrefixMap.get(TAG_IMAGES) + video.getString(TAG_THUMB);
                        String bigImageUrl = urlPrefixMap.get(TAG_IMAGES) + video
                                .getString(TAG_IMG_780_1200);
                        String title = video.getString(TAG_TITLE);
                        String studio = video.getString(TAG_STUDIO);
                        int duration = video.getInt(TAG_DURATION);
                        List<MediaTrack> tracks = null;
                        if (video.has(TAG_TRACKS)) {
                            JSONArray tracksArray = video.getJSONArray(TAG_TRACKS);
                            if (tracksArray != null) {
                                tracks = new ArrayList<>();
                                for (int k = 0; k < tracksArray.length(); k++) {
                                    JSONObject track = tracksArray.getJSONObject(k);
                                    tracks.add(buildTrack(track.getLong(TAG_TRACK_ID),
                                            track.getString(TAG_TRACK_TYPE),
                                            track.getString(TAG_TRACK_SUBTYPE),
                                            urlPrefixMap.get(TAG_TRACKS) + track
                                                    .getString(TAG_TRACK_CONTENT_ID),
                                            track.getString(TAG_TRACK_NAME),
                                            track.getString(TAG_TRACK_LANGUAGE)
                                    ));
                                }
                            }
                        }
                        //   videoUrl="https://d1n9vl26vbyc5s.cloudfront.net/stream_video/8.571937196955432.mp4";
                        //   videoUrl="https://d1n9vl26vbyc5s.cloudfront.net/stream_video/hls/8.571937196955432.m3u8";
                        //  videoUrl="https:\\/\\/d1n9vl26vbyc5s.cloudfront.net\\/stream_video\\/hls\\/15.620846808822076.m3u8";
                        //   videoUrl="https://d1n9vl26vbyc5s.cloudfront.net/stream_video/15.620846808822076.mp4";
                        mediaList.add(buildMediaInfo(title, studio, subTitle, duration, videoUrl,
                                mimeType, imageUrl, bigImageUrl, tracks));
                    }
                }
            }
        }
        return mediaList;
    }

    public static List<MediaInfo> buildMedia1(List<VideoListItem> pinnedWorkout,String imagePath) throws Exception {

      /*  if (null != mediaList) {
            return mediaList;
        }*/
        mediaList = new ArrayList<>();
        List<MediaTrack> tracks = null;
        if(pinnedWorkout!=null) {
            for (int i = 0; i < pinnedWorkout.size(); i++) {
                VideoListItem videoItem = pinnedWorkout.get(i);

                String title = "" + videoItem.getStreamVideoName();
                String studio = "" + videoItem.getStreamVideoSubtitle();
                String subTitle = "" + videoItem.getStreamVideoDescription();
                // int duration =  videoItem.getVideoDuration();
                int duration = 30;
                //  String videoUrl = "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/8.571937196955432_cbr_spHQ_360p.mp4";
                String videoUrl = videoItem.getMp4Video().getVMpeg720p();
                String mimeType = "videos/mp4";

                String imageUrl = videoItem.getStreamVideoImageUrl() + videoItem.getStreamVideoImage();
                String bigImageUrl = videoItem.getStreamVideoImageUrl() + videoItem.getStreamVideoImage();
                mediaList.add(buildMediaInfo(title, studio, subTitle, duration, videoUrl, mimeType, imagePath, imagePath, tracks));

            }
        }
        return mediaList;
    }

    public static List<MediaInfo> buildMedia2(PinnedWorkout pinnedWorkout,String imagePath) throws Exception {

       /* if (null != mediaList) {
            return mediaList;
        }*/
        mediaList = new ArrayList<>();
        List<MediaTrack> tracks = null;
        for (int i = 0; i < pinnedWorkout.getVideoList().size(); i++) {
            com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem videoItem=  pinnedWorkout.getVideoList().get(i);

            String title = ""+ videoItem.getStreamVideoName();
            String studio = ""+videoItem.getStreamVideoSubtitle();
            String subTitle = ""+ videoItem.getStreamVideoDescription();
           // int duration =  videoItem.getVideoDuration();
             int duration = 30;
          //  String videoUrl = "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/8.571937196955432_cbr_spHQ_360p.mp4";
            String videoUrl = videoItem.getMp4Video().getVMpeg720p();
            String mimeType = "videos/mp4";

            String imageUrl = videoItem.getStreamVideoImageUrl()+videoItem.getStreamVideoImage();
            String bigImageUrl = videoItem.getStreamVideoImageUrl()+videoItem.getStreamVideoImage();
            mediaList.add(buildMediaInfo(title, studio, subTitle, duration, videoUrl, mimeType, imagePath, imagePath, tracks));

        }
        return mediaList;
    }
    public static List<MediaInfo> buildMedia3(StreamPlayedHistoryModel.Data historyData,String imagePath) throws Exception {

       /* if (null != mediaList) {
            return mediaList;
        }*/
        mediaList = new ArrayList<>();
        List<MediaTrack> tracks = null;
     //   for (int i = 0; i < pinnedWorkout.getVideoList().size(); i++) {
          //  com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem videoItem=  pinnedWorkout.getVideoList().get(i);

            String title = ""+ historyData.getVideo_title();
            String studio = ""+historyData.getVideo_subtitle();
            String subTitle = ""+historyData.getVideo_description();
           // int duration =  videoItem.getVideoDuration();
             int duration = 30;
          //  String videoUrl = "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/8.571937196955432_cbr_spHQ_360p.mp4";
            String videoUrl = historyData.getMp4_video().getVMpeg720p();
            String mimeType = "videos/mp4";

            String imageUrl = historyData.getVideo_image();
            String bigImageUrl = historyData.getVideo_image();
            mediaList.add(buildMediaInfo(title, studio, subTitle, duration, videoUrl, mimeType, imagePath, imagePath, tracks));

      //  }
        return mediaList;
    }

    public static List<MediaInfo> buildMedia4(DownloadedModal.ProgressModal progressModal, String imagePath) throws Exception {

       /* if (null != mediaList) {
            return mediaList;
        }*/
        mediaList = new ArrayList<>();
        List<MediaTrack> tracks = null;
        //   for (int i = 0; i < pinnedWorkout.getVideoList().size(); i++) {
        //  com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem videoItem=  pinnedWorkout.getVideoList().get(i);

        String title = ""+ progressModal.getStream_workout_name();
        String studio = ""+progressModal.getStream_video_subtitle();
        String subTitle = ""+progressModal.getStream_video_description();;
        // int duration =  videoItem.getVideoDuration();
        int duration =0;
        //  String videoUrl = "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/8.571937196955432_cbr_spHQ_360p.mp4";
        String videoUrl = progressModal.getVideoUrl(); //TODO cross verify this URL may be it will come as storage video url or actual required URL

        String mimeType = "videos/mp4";
        Log.e("CreateLoader Call-->","videoUrl "+ progressModal.getVideoUrl());

        String imageUrl = progressModal.getStream_video_image_url();
        String bigImageUrl = progressModal.getStream_workout_image_url();
        mediaList.add(buildMediaInfo(title, studio, subTitle, duration, videoUrl, mimeType, imagePath, imagePath, tracks));
        Log.e("CreateLoader Call-->","buildMedia4 "+mediaList.size());
        return mediaList;
    }

    //-------------------------Hemant Log History List Crome Cast----------
    public static List<MediaInfo> buildMedia5(StreamLogModel.Data historyData, String imagePath) throws Exception {

       /* if (null != mediaList) {
            return mediaList;
        }*/
        mediaList = new ArrayList<>();
        List<MediaTrack> tracks = null;
        //   for (int i = 0; i < pinnedWorkout.getVideoList().size(); i++) {
        //  com.doviesfitness.ui.bottom_tabbar.stream_tab.model.tv.VideoListItem videoItem=  pinnedWorkout.getVideoList().get(i);

        String title = ""+ historyData.getVideo_title();
        String studio = ""+historyData.getVideo_subtitle();
        String subTitle = ""+historyData.getVideo_description();
        // int duration =  videoItem.getVideoDuration();
        int duration = 30;
        //  String videoUrl = "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/8.571937196955432_cbr_spHQ_360p.mp4";
        String videoUrl = historyData.getMp4_video().getVMpeg720p();
        String mimeType = "videos/mp4";

        String imageUrl = historyData.getVideo_image();
        String bigImageUrl = historyData.getVideo_image();
        mediaList.add(buildMediaInfo(title, studio, subTitle, duration, videoUrl, mimeType, imagePath, imagePath, tracks));

        //  }
        return mediaList;
    }



    private static MediaInfo buildMediaInfo(String title, String studio, String subTitle,
                                            int duration, String url, String mimeType, String imgUrl, String bigImageUrl,
                                            List<MediaTrack> tracks) {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, studio);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, title);
//        movieMetadata.addImage(new WebImage(Uri.parse(imgUrl)));
        movieMetadata.addImage(new WebImage(Uri.parse(imgUrl)));
//        movieMetadata.addImage(new WebImage(Uri.parse(bigImageUrl)));
 //       movieMetadata.addImage(new WebImage(Uri.parse("https://picsum.photos/id/237/200/300")));

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();
            jsonObj.put(KEY_DESCRIPTION, subTitle);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to add description to the json object", e);
        }

        return new MediaInfo.Builder(url)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(mimeType)
                .setMetadata(movieMetadata)
                // .setMediaTracks(tracks)
                //.setStreamDuration(duration * 1000)
              //  .setStreamDuration(33 * 60 * 1000)
                .setCustomData(jsonObj)
                .build();
    }

    private static MediaTrack buildTrack(long id, String type, String subType, String contentId,
                                         String name, String language) {
        int trackType = MediaTrack.TYPE_UNKNOWN;
        if ("text".equals(type)) {
            trackType = MediaTrack.TYPE_TEXT;
        } else if ("video".equals(type)) {
            trackType = MediaTrack.TYPE_VIDEO;
        } else if ("audio".equals(type)) {
            trackType = MediaTrack.TYPE_AUDIO;
        }

        int trackSubType = MediaTrack.SUBTYPE_NONE;
        if (subType != null) {
            if ("captions".equals(type)) {
                trackSubType = MediaTrack.SUBTYPE_CAPTIONS;
            } else if ("subtitle".equals(type)) {
                trackSubType = MediaTrack.SUBTYPE_SUBTITLES;
            }
        }

        return new MediaTrack.Builder(id, trackType)
                .setName(name)
                .setSubtype(trackSubType)
                .setContentId(contentId)
                .setLanguage(language).build();
    }
}
