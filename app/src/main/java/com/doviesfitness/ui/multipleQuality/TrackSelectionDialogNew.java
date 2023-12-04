/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.doviesfitness.ui.multipleQuality;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.doviesfitness.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Dialog to select tracks. */
public final class TrackSelectionDialogNew extends DialogFragment implements TrackSelectionView.DismissLayout{
  @Override
  public void dismisslayout(int trackIndex, String autorRsolution) {

  }

  @Override
  public int getautoValue() {
    return 0;
  }
  int i = 0;
String stream_qty="";
  private static  SparseArray<TrackSelectionViewFragment> tabFragments;
  private static  ArrayList<Integer> tabTrackTypes;
  public static boolean isVisible=false;
  StreamVideoPlayUrlActivityTemp mactivity;
  private int titleId;
  public Dialog dialog1;
  public DialogInterface.OnClickListener onClickListener;
  public DialogInterface.OnDismissListener onDismissListener;
  public TrackSelectionView trackSelectionView;

  /**
   * Returns whether a track selection dialog will have content to display if initialized with the
   * specified {@link DefaultTrackSelector} in its current state.
   */
  public static boolean willHaveContent(DefaultTrackSelector trackSelector) {
    MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
    return mappedTrackInfo != null && willHaveContent(mappedTrackInfo);
  }

  /**
   * Returns whether a track selection dialog will have content to display if initialized with the
   * specified {@link MappedTrackInfo}.
   */
  public static boolean willHaveContent(MappedTrackInfo mappedTrackInfo) {
    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
      if (showTabForRenderer(mappedTrackInfo, i)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a dialog for a given {@link DefaultTrackSelector}, whose parameters will be
   * automatically updated when tracks are selected.
   *
   * @param trackSelector The {@link DefaultTrackSelector}.
   * @param onDismissListener A {@link DialogInterface.OnDismissListener} to call when the dialog is
   *     dismissed.
   */
  public static TrackSelectionDialogNew createForTrackSelector(StreamVideoPlayUrlActivityTemp activity,DefaultTrackSelector trackSelector,
                                                               DialogInterface.OnDismissListener onDismissListener) {
   // mactivity=activity;
    MappedTrackInfo mappedTrackInfo =
        Assertions.checkNotNull(trackSelector.getCurrentMappedTrackInfo());
   TrackSelectionDialogNew trackSelectionDialog = new TrackSelectionDialogNew();
    DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();
    trackSelectionDialog.init(activity,/* titleId= */ R.string.track_selection_title, mappedTrackInfo,/* initialParameters = */ parameters,/* allowAdaptiveSelections =*/ true,
        /* allowMultipleOverrides= */ false,
        /* onClickListener=*/ (dialog, which) -> {
          DefaultTrackSelector.ParametersBuilder builder = parameters.buildUpon();
          for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            builder
                .clearSelectionOverrides(/* rendererIndex= */ i)
                .setRendererDisabled(
                    /* rendererIndex= */ i,
                    getIsDisabled(/* rendererIndex= */ i));
            List<SelectionOverride> overrides =
                getOverrides(/* rendererIndex= */ i);
            if (!overrides.isEmpty()) {
              builder.setSelectionOverride(
                  /* rendererIndex= */ i,
                  mappedTrackInfo.getTrackGroups(/* rendererIndex= */ i),
                  overrides.get(0));
            }
          }
          trackSelector.setParameters(builder);
        },
        onDismissListener);
   /* Window window = trackSelectionDialog.getDialog().getWindow();
    WindowManager.LayoutParams wlp = window.getAttributes();

    wlp.gravity = Gravity.BOTTOM;
    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    window.setAttributes(wlp);
*/
    return trackSelectionDialog;
  }

  /**
   * Creates a dialog for given {@link MappedTrackInfo} and {@link DefaultTrackSelector.Parameters}.
   *
   * @param titleId The resource id of the dialog title.
   * @param mappedTrackInfo The {@link MappedTrackInfo} to display.
   * @param initialParameters The {@link DefaultTrackSelector.Parameters} describing the initial
   *     track selection.
   * @param allowAdaptiveSelections Whether adaptive selections (consisting of more than one track)
   *     can be made.
   * @param allowMultipleOverrides Whether tracks from multiple track groups can be selected.
   * @param onClickListener {@link DialogInterface.OnClickListener} called when tracks are selected.
   * @param onDismissListener {@link DialogInterface.OnDismissListener} called when the dialog is
   *     dismissed.
   */
  public static TrackSelectionDialogNew createForMappedTrackInfoAndParameters(StreamVideoPlayUrlActivityTemp activity,
      int titleId,
      MappedTrackInfo mappedTrackInfo,
      DefaultTrackSelector.Parameters initialParameters,
      boolean allowAdaptiveSelections,
      boolean allowMultipleOverrides,
      DialogInterface.OnClickListener onClickListener,
      DialogInterface.OnDismissListener onDismissListener) {
    TrackSelectionDialogNew trackSelectionDialog = new TrackSelectionDialogNew();
    trackSelectionDialog.init(
            activity,
        titleId,
        mappedTrackInfo,
        initialParameters,
        allowAdaptiveSelections,
        allowMultipleOverrides,
        onClickListener,
        onDismissListener);
    return trackSelectionDialog;
  }

  public TrackSelectionDialogNew() {
    tabFragments = new SparseArray<>();
    tabTrackTypes = new ArrayList<>();
    // Retain instance across activity re-creation to prevent losing access to init data.
    setRetainInstance(true);
  }

  //shivangi
  public  Fragment showFragment(StreamVideoPlayUrlActivityTemp mactivity) {
    this.mactivity=mactivity;
    Fragment newFragment = tabFragments.get(0);
    return newFragment;

  }

  private  void init(
          StreamVideoPlayUrlActivityTemp activity,
          int titleId1,
          MappedTrackInfo mappedTrackInfo,
          DefaultTrackSelector.Parameters initialParameters,
          boolean allowAdaptiveSelections,
          boolean allowMultipleOverrides,
          DialogInterface.OnClickListener onClickListener,
          DialogInterface.OnDismissListener onDismissListener) {
   this.titleId = titleId1;
   this.mactivity=activity;
    this.onClickListener = onClickListener;
    this.onDismissListener = onDismissListener;
    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
      if (showTabForRenderer(mappedTrackInfo, i)) {
        int trackType = mappedTrackInfo.getRendererType(/* rendererIndex= */ i);
        TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(i);
        dialog1=getDialog();
        TrackSelectionViewFragment tabFragment = new TrackSelectionViewFragment(onClickListener,onDismissListener,mactivity,dialog1);
        tabFragment.init(
            mappedTrackInfo,
            /* rendererIndex= */ i,
            initialParameters.getRendererDisabled(/* rendererIndex= */ i),
            initialParameters.getSelectionOverride(/* rendererIndex= */ i, trackGroupArray),
            allowAdaptiveSelections,
            allowMultipleOverrides);
        tabFragments.put(i, tabFragment);
        tabTrackTypes.add(trackType);
      }
    }
  }

  /**
   * Returns whether a renderer is disabled.
   *
   * @param rendererIndex Renderer index.
   * @return Whether the renderer is disabled.
   */
  public static boolean getIsDisabled(int rendererIndex) {
    TrackSelectionViewFragment rendererView = tabFragments.get(rendererIndex);
    return rendererView != null && rendererView.isDisabled;
  }

  /**
   * Returns the list of selected track selection overrides for the specified renderer. There will
   * be at most one override for each track group.
   *
   * @param rendererIndex Renderer index.
   * @return The list of track selection overrides for this renderer.
   */
  public static List<SelectionOverride> getOverrides(int rendererIndex) {
    TrackSelectionViewFragment rendererView = tabFragments.get(rendererIndex);
    return rendererView == null ? Collections.emptyList() : rendererView.overrides;
  }

  @Override
  @NonNull
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // We need to own the view to let tab layout work correctly on all API levels. We can't use
    // AlertDialog because it owns the view itself, so we use AppCompatDialog instead, themed using
    // the AlertDialog theme overlay with force-enabled title.
    AppCompatDialog dialog =
        new AppCompatDialog(getActivity());
    dialog.setTitle("Quality");
    Window window = dialog.getWindow();
    WindowManager.LayoutParams wlp = window.getAttributes();

    wlp.gravity = Gravity.BOTTOM;
    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    window.setAttributes(wlp);
    return dialog;
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
   // onDismissListener.onDismiss(dialog);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View dialogView = inflater.inflate(R.layout.track_selection_dialog, container, false);
    TabLayout tabLayout = dialogView.findViewById(R.id.track_selection_dialog_tab_layout);
    ViewPager viewPager = dialogView.findViewById(R.id.track_selection_dialog_view_pager);
    Button cancelButton = dialogView.findViewById(R.id.track_selection_dialog_cancel_button);
    Button okButton = dialogView.findViewById(R.id.track_selection_dialog_ok_button);
    viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.setVisibility(tabFragments.size() > 1 ? View.VISIBLE : View.GONE);
    cancelButton.setOnClickListener(view -> dismiss());
    okButton.setOnClickListener(
        view -> {
          onClickListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
          dismiss();
        });
   return dialogView;
  }

  private static boolean showTabForRenderer(MappedTrackInfo mappedTrackInfo, int rendererIndex) {
    TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex);
    if (trackGroupArray.length == 0) {
      return false;
    }
    int trackType = mappedTrackInfo.getRendererType(rendererIndex);
    return isSupportedTrackType(trackType);
  }

  private static boolean isSupportedTrackType(int trackType) {
    switch (trackType) {
      case C.TRACK_TYPE_VIDEO:
      case C.TRACK_TYPE_AUDIO:
      case C.TRACK_TYPE_TEXT:
        return true;
      default:
        return false;
    }
  }

  private static String getTrackTypeString(Resources resources, int trackType) {
    switch (trackType) {
      case C.TRACK_TYPE_VIDEO:
        return resources.getString(R.string.exo_track_selection_title_video);
      case C.TRACK_TYPE_AUDIO:
        return resources.getString(R.string.exo_track_selection_title_audio);
      case C.TRACK_TYPE_TEXT:
        return resources.getString(R.string.exo_track_selection_title_text);
      default:
        throw new IllegalArgumentException();
    }
  }

  private final class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fragmentManager) {
      super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    //shivangi
    @Override
    @NonNull
    public Fragment getItem(int position) {
   //   return tabFragments.valueAt(position);
      return tabFragments.valueAt(0);
    }

    @Override
    public int getCount() {
     // return tabFragments.size();
      return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getTrackTypeString(getResources(), tabTrackTypes.get(position));
    }
  }

  /** Fragment to show a track selection in tab of the track selection dialog. */
  public static final class TrackSelectionViewFragment extends Fragment
      implements TrackSelectionView.TrackSelectionListener ,TrackSelectionView.DismissLayout {


    @Override
    public void dismisslayout(int trackIndex, String autorRsolution) {
      if(handler!=null&&dialogRunnable!=null)
      handler.removeCallbacks(dialogRunnable);

      StreamVideoPlayUrlActivityTemp.Companion.setVideoTackIndex(trackIndex);//callback to play video after clicking the select video resolution item
      StreamVideoPlayUrlActivityTemp.Companion.setVideoResolution(autorRsolution);





      /*new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          StreamVideoPlayUrlActivityTemp.Companion.setVideoTackIndex(4);//callback to play video after clicking the select video resolution item
          StreamVideoPlayUrlActivityTemp.Companion.setVideoResolution("360");
          Log.d("fbjasbfjas", trackIndex+" dismisslayout: --=====- "+ StreamVideoPlayUrlActivityTemp.Companion.getVideoTackIndex());
        }
      },15*1000);*/

      /*StreamVideoPlayUrlActivityTemp.Companion.setVideoTackIndex(trackIndex);//callback to play video after clicking the select video resolution item
      StreamVideoPlayUrlActivityTemp.Companion.setVideoResolution(autorRsolution);
      */
    //  mactivity.dismissQwalityDialog();

    }

    @Override
    public int getautoValue() {
      return StreamVideoPlayUrlActivityTemp.Companion.getVideoTackIndex();

    }


    private MappedTrackInfo mappedTrackInfo;
    private int rendererIndex;
    private boolean allowAdaptiveSelections;
    private boolean allowMultipleOverrides;
    DialogInterface.OnClickListener onClickListener;
     DialogInterface.OnDismissListener onDismissListener;
    public TrackSelectionView trackSelectionView;
    /* package */ boolean isDisabled;
    StreamVideoPlayUrlActivityTemp mactivity;
    public Dialog dialog;
    /* package */ List<SelectionOverride> overrides;
    Handler handler;

    Runnable dialogRunnable=new Runnable() {
      @Override
      public void run() {
        try {
          mactivity.dismissQwalityDialog();
          onDismissListener.onDismiss(dialog);
        }
        catch (Exception ex){
          ex.printStackTrace();
        }
      }
    };
    public TrackSelectionViewFragment(DialogInterface.OnClickListener onClickListener, DialogInterface.OnDismissListener onDismissListener,
                                      StreamVideoPlayUrlActivityTemp activity, Dialog dialog) {
      // Retain instance across activity re-creation to prevent losing access to init data.
      setRetainInstance(true);
      this.onClickListener=onClickListener;
      this.onDismissListener=onDismissListener;
      this.mactivity=activity;
      this.dialog=dialog;
    //  this.trackSelectionView=trackSelectionView;
    }

    public void init(
        MappedTrackInfo mappedTrackInfo,
        int rendererIndex,
        boolean initialIsDisabled,
        @Nullable SelectionOverride initialOverride,
        boolean allowAdaptiveSelections,
        boolean allowMultipleOverrides) {
      this.mappedTrackInfo = mappedTrackInfo;
      this.rendererIndex = rendererIndex;
      this.isDisabled = initialIsDisabled;
      this.overrides =
          initialOverride == null
              ? Collections.emptyList()
              : Collections.singletonList(initialOverride);
      this.allowAdaptiveSelections = allowAdaptiveSelections;
      this.allowMultipleOverrides = allowMultipleOverrides;
    }


    @Override
    public View onCreateView(
        LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      View rootView =
          inflater.inflate(
              R.layout.exo_track_selection_dialog, container, false);
    //  TrackSelectionView  trackSelectionView=new TrackSelectionView(mactivity);

       trackSelectionView = rootView.findViewById(R.id.exo_track_selection_view);
    //  TrackSelectionView.videoTrackindex1=StreamVideoPlayUrlActivityTemp.Companion.getVideoTackIndex();
      trackSelectionView.setclickListener(this,onClickListener,dialog,onDismissListener,StreamVideoPlayUrlActivityTemp.Companion.getVideoTackIndex(),
              StreamVideoPlayUrlActivityTemp.Companion.getVideoAutoResolution(),StreamVideoPlayUrlActivityTemp.Companion.getVideoResolution(),StreamVideoPlayUrlActivityTemp.Companion.getStream_qty(),StreamVideoPlayUrlActivityTemp.Companion.getStream_qty_list(),StreamVideoPlayUrlActivityTemp.Companion.getVideoResolutionOnBandwith());
      trackSelectionView.setShowDisableOption(true);
     // trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides);
      trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections);

      trackSelectionView.init(
          mappedTrackInfo, rendererIndex, isDisabled, overrides,
 this);
   //   Button cancelButton = rootView.findViewById(R.id.track_selection_dialog_cancel_button);
      LinearLayout top_view = rootView.findViewById(R.id.top_view);
      ImageView backIcon = rootView.findViewById(R.id.back_icon);
    //  Button okButton = rootView.findViewById(R.id.track_selection_dialog_ok_button);
     if (TrackSelectionDialogNew.isVisible) {
       if(handler!=null&&dialogRunnable!=null)
         handler.removeCallbacks(dialogRunnable);

         handler = new Handler();
       handler.postDelayed(dialogRunnable, 5000);
     }


      //   top_view.addView(trackSelectionView);

      backIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(handler!=null&&dialogRunnable!=null)
            handler.removeCallbacks(dialogRunnable);
          mactivity.dismissQwalityDialog();
          onDismissListener.onDismiss(dialog);

        }
      });
/*
      okButton.setOnClickListener(
              view -> {
                onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                mactivity.dismissQwalityDialog();
                onDismissListener.onDismiss(dialog);

              });
*/

      return rootView;
    }



    @Override
    public void onTrackSelectionChanged(
        boolean isDisabled, @NonNull List<SelectionOverride> overrides) {
      Log.d("jfasjflas", "onTrackSelectionChanged: ONE "+overrides);


      this.isDisabled = isDisabled;
      this.overrides = overrides;

    }

  }
}
