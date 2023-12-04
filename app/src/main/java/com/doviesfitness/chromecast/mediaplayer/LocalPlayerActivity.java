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

package com.doviesfitness.chromecast.mediaplayer;

import androidx.mediarouter.app.MediaRouteButton;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.doviesfitness.R;
import com.doviesfitness.chromecast.expandedcontrols.ExpandedControlsActivity;
import com.doviesfitness.chromecast.utils.CustomVolleyRequest;
import com.doviesfitness.chromecast.utils.Utils;
import com.doviesfitness.ui.multipleQuality.DemoUtil;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.MediaUtils;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
public class LocalPlayerActivity extends AppCompatActivity {

    private static final String TAG = "LocalPlayerActivity";
    private com.google.android.exoplayer2.ui.PlayerView mVideoView;
 //   private TextView mTitleView;
  //  private ImageView mPlayPause;
 //   private ProgressBar mLoading;
    private ImageView mCoverArt;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private MediaInfo mSelectedMedia;
    private ImageButton mPlayCircle;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;

    //  private ImageLoader mImageLoader;
    //////changes
    DefaultBandwidthMeter defaultbandwidhtmeter;
    private DefaultTrackSelector trackSelector;
    private TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(4000, 4000, 4000, 1f);
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private SimpleExoPlayer player;
    MediaRouteButton mediaButton;

    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }
    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity_chrome_cast);
        loadViews();
        mediaButton = (MediaRouteButton) findViewById(R.id.media_route_menu_item1);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mediaButton);
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        // see what we need to play and where
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSelectedMedia = getIntent().getParcelableExtra("media");
            boolean shouldStartPlayback = bundle.getBoolean("shouldStart");
            int startPosition = bundle.getInt("startPosition", 0);
////////////////changes
            DefaultTrackSelector.ParametersBuilder builder =new DefaultTrackSelector.ParametersBuilder( this);
            trackSelectorParameters = builder.build();
          //  boolean preferExtensionDecoders = intent.getBooleanExtra(IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA, false);
            RenderersFactory renderersFactory = DemoUtil.buildRenderersFactory( this, true);
            defaultbandwidhtmeter=  new DefaultBandwidthMeter.Builder(LocalPlayerActivity.this).build();
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            trackSelector.setParameters(trackSelectorParameters);

            //changed
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                    Util.getUserAgent(this, "mediaPlayerSample"),
                    defaultbandwidhtmeter,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, defaultbandwidhtmeter, httpDataSourceFactory);
            MediaSourceFactory mediaSourceFactory  = new DefaultMediaSourceFactory(dataSourceFactory);

              player = new SimpleExoPlayer.Builder(this, renderersFactory)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .setTrackSelector(trackSelector).build();
            setupControlsCallbacks();
            mVideoView.setPlayer(player);
            Log.d(TAG, "Setting url of the VideoView to: " + mSelectedMedia.getContentId());

          //  player.setMediaItem(MediaItem.fromUri(Uri.parse(mSelectedMedia.getContentId())));
            player.setMediaItem(MediaItem.fromUri(Uri.parse( "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/9.823734082249501_cbr_spHQ_360p.mp4")));
          //  mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));

            if (shouldStartPlayback) {
                // this will be the case only if we are coming from the
                // CastControllerActivity by disconnecting from a device
                mPlaybackState = PlaybackState.PLAYING;
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                updatePlayButton(mPlaybackState);
                if (startPosition > 0) {
                    player.seekTo(startPosition);
                }
                player.prepare();
                player.setPlayWhenReady(true);
            } else {
                if (mCastSession != null && mCastSession.isConnected()) {
                    updatePlaybackLocation(PlaybackLocation.REMOTE);
                } else {
                    updatePlaybackLocation(PlaybackLocation.LOCAL);
                }
                mPlaybackState = PlaybackState.IDLE;
                updatePlayButton(mPlaybackState);
            }
        }
    }

    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                Log.d("on session ","on session...Ended");
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                Log.d("on session ","on session...Resumed");

                onApplicationConnected(session);
            }
            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                Log.d("on session ","on session...failed");

                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                Log.d("on session ","on session...started");

                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                Log.d("on session ","on session...StartFailed");

                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
                Log.d("on session ","on session...Starting");

            }

            @Override
            public void onSessionEnding(CastSession session) {
                Log.d("on session ","on session...Ending");

            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
                Log.d("on session ","on session...Resuming");

            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
                Log.d("on session ","on session...Suspended");

            }

            private void onApplicationConnected(CastSession castSession) {
                Log.d("on session ","on session...ApplicationConnected");

                mCastSession = castSession;
                if (null != mSelectedMedia) {

                    if (mPlaybackState == PlaybackState.PLAYING) {
                        player.pause();
                        loadRemoteMedia(0, true);
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                Log.d("on session ","on session...Applicationdisconnected");

                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                invalidateOptionsMenu();
            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                setCoverArtStatus(null);
            } else {
                setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
        }
    }

    private void play(int position) {
        switch (mLocation) {
            case LOCAL:
                player.seekTo(position);
                player.play();
                break;
            case REMOTE:
                mPlaybackState = PlaybackState.BUFFERING;
                updatePlayButton(mPlaybackState);
                mCastSession.getRemoteMediaClient().seek(position);
                break;
            default:
                break;
        }
        restartTrickplayTimer();
    }

    private void togglePlayback() {
        switch (mPlaybackState) {
            case PAUSED:
                switch (mLocation) {
                    case LOCAL:
                        player.play();
                        Log.d(TAG, "Playing locally...");
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                        break;
                    case REMOTE:
                        loadRemoteMedia(0, true);
                        finish();
                        break;
                    default:
                        break;
                }
                break;

            case PLAYING:
                mPlaybackState = PlaybackState.PAUSED;
                player.pause();
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                   //     player.setMediaItem( MediaItem.fromUri(Uri.parse(mSelectedMedia.getContentId())));
                        player.setMediaItem(MediaItem.fromUri(Uri.parse( "https://d1n9vl26vbyc5s.cloudfront.net/stream_video/mp4/9.823734082249501_cbr_spHQ_360p.mp4")));

                        player.seekTo(0);
                        player.prepare();
                        player.setPlayWhenReady(true);
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);
                        break;
                    case REMOTE:
                        if (mCastSession != null && mCastSession.isConnected()) {
                            Utils.showQueuePopup(this, mPlayCircle, mSelectedMedia);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        updatePlayButton(mPlaybackState);
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.registerCallback(new RemoteMediaClient.Callback() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(LocalPlayerActivity.this, ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.unregisterCallback(this);
            }
        });
        remoteMediaClient.load(new MediaLoadRequestData.Builder()
                .setMediaInfo(mSelectedMedia)
                .setAutoplay(autoPlay)
                .setCurrentTime(position).build());
    }

    private void setCoverArtStatus(String url) {
        if (url != null) {
        //   mImageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
         //   mImageLoader.get(url, ImageLoader.getImageListener(mCoverArt, 0, 0));
          //  mCoverArt.setImageUrl(url, mImageLoader);
            Glide.with(getApplicationContext()).load(R.drawable.india).into(mCoverArt);

            mCoverArt.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.INVISIBLE);
        } else {
            mCoverArt.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
        }
    }

    private void stopTrickplayTimer() {
        Log.d(TAG, "Stopped TrickPlay Timer");
    }

    private void restartTrickplayTimer() {
        stopTrickplayTimer();
        Log.d(TAG, "Restarted TrickPlay Timer");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() was called");
        if (mLocation == PlaybackLocation.LOCAL) {
            player.pause();
            mPlaybackState = PlaybackState.PAUSED;
            updatePlayButton(PlaybackState.PAUSED);
        }
        mCastContext.getSessionManager().removeSessionManagerListener(mSessionManagerListener, CastSession.class);
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() is called");
       // stopControllersTimer();
        stopTrickplayTimer();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() was called");
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
        if (mCastSession != null && mCastSession.isConnected()) {
            updatePlaybackLocation(PlaybackLocation.REMOTE);
        } else {
            updatePlaybackLocation(PlaybackLocation.LOCAL);
        }

        super.onResume();
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return mCastContext.onDispatchVolumeKeyEventBeforeJellyBean(event)
                || super.dispatchKeyEvent(event);
    }

    private void setupControlsCallbacks() {

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState== Player.STATE_IDLE)
                {

                }
                else if (playbackState==  Player.STATE_BUFFERING){

                }
                else if (playbackState==  Player.STATE_READY){
                    Log.d(TAG, "onPrepared is reached");
                    restartTrickplayTimer();
                }
                else if (playbackState==  Player.STATE_ENDED){
                    stopTrickplayTimer();
                    Log.d(TAG, "setOnCompletionListener()");
                    mPlaybackState = PlaybackState.IDLE;
                    updatePlayButton(mPlaybackState);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                Log.e(TAG, "OnErrorListener.onError(): VideoView encountered an "
                        + "error, what: " + error.rendererName + ", extra: " + error.getLocalizedMessage());
                String msg=error.getMessage();
                Utils.showErrorDialog(LocalPlayerActivity.this, msg);
                player.stop();
                mPlaybackState = PlaybackState.IDLE;
                updatePlayButton(mPlaybackState);
            }
        });
    }
    private void updatePlayButton(PlaybackState state) {
        Log.d(TAG, "Controls: PlayBackState: " + state);
        boolean isConnected = (mCastSession != null)
                && (mCastSession.isConnected() || mCastSession.isConnecting());
        mPlayCircle.setVisibility(isConnected ? View.GONE : View.VISIBLE);
        switch (state) {
            case PLAYING:
                mPlayCircle.setVisibility(isConnected ? View.VISIBLE : View.GONE);
                break;
            case IDLE:
                mPlayCircle.setVisibility(View.VISIBLE);
                mCoverArt.setVisibility(View.VISIBLE);
                mVideoView.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                mPlayCircle.setVisibility(isConnected ? View.VISIBLE : View.GONE);
                break;
            case BUFFERING:
                break;
            default:
                break;
        }
    }
    private void loadViews() {
        mVideoView = (com.google.android.exoplayer2.ui.PlayerView) findViewById(R.id.videoView1);
        mCoverArt = (ImageView) findViewById(R.id.coverArtView);
        mPlayCircle = (ImageButton) findViewById(R.id.play_circle);
        mPlayCircle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayback();
            }
        });
    }
}
