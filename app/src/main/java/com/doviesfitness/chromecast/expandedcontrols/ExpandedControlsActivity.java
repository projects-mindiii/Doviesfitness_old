


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

package com.doviesfitness.chromecast.expandedcontrols;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.doviesfitness.R;
import com.doviesfitness.chromecast.browser.VideoProvider;
import com.doviesfitness.chromecast.utils.CustomChromeCastBottomSheetDialog;
import com.doviesfitness.chromecast.utils.CustomChromeDisconnectStopDialog;
import com.doviesfitness.databinding.ActivityExpandedControlsChromecastBinding;
import com.doviesfitness.utils.CommanUtils;
import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.MediaQueue;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.uicontroller.UIMediaController;
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


/**
 * An example of extending {@link ExpandedControllerActivity} to add a cast button.
 */
public class ExpandedControlsActivity extends ExpandedControllerActivity implements View.OnClickListener, CustomChromeDisconnectStopDialog.ChromeCastListener {

    ActivityExpandedControlsChromecastBinding binding;
    CastContext mCastContext;
    CastSession mCastSession;
    RemoteMediaClient remoteMediaClient;
    ApplicationMetadata metadata;
    MediaQueueItem mediaQueueItem;
    MediaInfo mediaInfo;
    UIMediaController uiMediaController;
    Boolean isCastConnected = false;
    String from="",streamImage="";
    TextView title,subtitle;

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.expanded_controller, menu);
         CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item);
         return true;
     }

 */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expanded_controls_chromecast);
        title=findViewById(R.id.title);
        subtitle=findViewById(R.id.subtitle);
        hideNavigationBar();
        initialization();
    }

    void initialization() {
        binding.play.setOnClickListener(this);
        binding.backIcon.setOnClickListener(this);
        binding.chromeCast.setOnClickListener(this);
        if (getIntent().hasExtra("from"))
        {
            from=getIntent().getStringExtra("from");
        }
        if (getIntent().hasExtra("streamImage"))
        {
            streamImage=getIntent().getStringExtra("streamImage");
        }


        try {
            mCastContext = CastContext.getSharedInstance(this);
            metadata = mCastContext.getSessionManager().getCurrentCastSession().getApplicationMetadata();
            mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
            remoteMediaClient = mCastContext.getSessionManager().getCurrentCastSession().getRemoteMediaClient();
            mediaQueueItem = remoteMediaClient.getCurrentItem();
            mediaInfo = remoteMediaClient.getMediaInfo();
            binding.tvHeading.setText("Casting to " + mCastSession.getCastDevice().getFriendlyName());


        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            if (mediaInfo.getMetadata().getImages().get(mediaInfo.getMetadata().getImages().size() - 1).getUrl() != null) {
                Glide.with(this).load(mediaInfo.getMetadata().getImages().get(mediaInfo.getMetadata().getImages().size() - 1).getUrl()).into(binding.mediaImage);
            }
            if(!streamImage.isEmpty())
            {
                Glide.with(this).load(streamImage).into(binding.mediaImage);
            }
//            else{
//                Glide.with(this).load(mediaInfo.getMetadata().getImages().get(mediaInfo.getMetadata().getImages().size() - 1).getUrl()).into(binding.mediaImage);
//
//            }
            } catch (Exception exception) {
            Glide.with(this).load(streamImage).into(binding.mediaImage);

            exception.printStackTrace();
        }
//        MediaMetadata mm = mediaInfo.getMetadata();
//        title.setText(CommanUtils.Companion.capitalize(mediaInfo.getCustomData().optString(
//                VideoProvider.KEY_DESCRIPTION)));
        try {
            subtitle.setText(CommanUtils.Companion.capitalize(mediaInfo.getMetadata().getString(MediaMetadata.KEY_SUBTITLE)));
            title.setText(CommanUtils.Companion.capitalize(mediaInfo.getMetadata().getString(MediaMetadata.KEY_TITLE)));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

       /* if (remoteMediaClient.isPlaying()) {
            remoteMediaClient.pause();
            Glide.with(getBaseContext()).load(R.drawable.exo_ic_play_circle_filled).into(binding.play);
        }
        else {
            remoteMediaClient.play();
            Glide.with(getBaseContext()).load(R.drawable.exo_ic_pause_circle_filled).into(binding.play);
        }*/
        if (mCastSession != null && mCastSession.isConnected()) {
            isCastConnected = true;
            Glide.with(getBaseContext()).load(R.drawable.ic_mr_button_connected_30_dark).into(binding.chromeCast);
            binding.chromeCast.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorOrange1), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            isCastConnected = false;
            Glide.with(getBaseContext()).load(R.drawable.ic_mr_button_connecting_00_dark).into(binding.chromeCast);
            binding.chromeCast.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        uiMediaController = new UIMediaController(this);
        uiMediaController.bindSeekBar(binding.seekBar, 1); // Binding a CastSeekBar to video progress
        uiMediaController.bindTextViewToStreamPosition(binding.positionTv, true); // Binding a TextView to video current position
        uiMediaController.bindTextViewToStreamDuration(binding.durationtv);
        //uiMediaController.bindTextViewToMetadataOfCurrentItem(binding.title, MediaMetadata.KEY_TITLE);
       // uiMediaController.bindTextViewToMetadataOfCurrentItem(binding.subtitle, MediaMetadata.KEY_SUBTITLE);
        uiMediaController.bindImageViewToMuteToggle(binding.unmute);
        uiMediaController.bindViewToForward(binding.forword, 30000);
        uiMediaController.bindViewToRewind(binding.backword, 30000);
        uiMediaController.bindViewToClosedCaption(binding.closeCaption);
        uiMediaController.bindImageViewToPlayPauseToggle(binding.play, getResources().getDrawable(R.drawable.exo_ic_play_circle_filled),
                getResources().getDrawable(R.drawable.exo_ic_pause_circle_filled), getResources().getDrawable(R.drawable.cast_ic_stop_circle_filled_white),
                null, false);
      //  uiMediaController.bindImageViewToImageOfCurrentItem(binding.mediaImage,);
    }

    private void hideNavigationBar() {

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*
            case R.id.play: {
                if (remoteMediaClient.isPlaying()) {
                    remoteMediaClient.pause();
                    Glide.with(getBaseContext()).load(R.drawable.exo_ic_play_circle_filled).into(binding.play);
                } else {
                    remoteMediaClient.play();
                    Glide.with(getBaseContext()).load(R.drawable.exo_ic_pause_circle_filled).into(binding.play);
                }
                break;
            }
*/
            case R.id.back_icon: {
                // remoteMediaClient.stop();
                if (from!=null && !from.isEmpty() && from.equals("player")){
                    setResult(Activity.RESULT_OK,new Intent().putExtra("expand","expand"));
                }
                finish();
                break;
            }
            case R.id.chrome_cast: {
                CustomChromeDisconnectStopDialog openDialog = CustomChromeDisconnectStopDialog.newInstance(this, getBaseContext(), isCastConnected, "" + mCastSession.getCastDevice().getFriendlyName());
                openDialog.show(getSupportFragmentManager());

            }

        }

    }

    @Override
    public void chromeCastListener(@NotNull String message) {
        if (message.equals("stop")) {
            remoteMediaClient.stop();
        } else {
            mCastContext.getSessionManager().endCurrentSession(true);
        }

    }

    @Override
    public void dismisDialog() {
        hideNavigationBar();
    }
}
