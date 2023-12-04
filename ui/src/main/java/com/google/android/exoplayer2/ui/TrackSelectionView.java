 /*
  * Copyright (C) 2018 The Android Open Source Project
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
 package com.google.android.exoplayer2.ui;

 import android.app.Activity;
 import android.app.Dialog;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.res.TypedArray;
 import android.graphics.Typeface;
 import android.os.Build;
 import android.util.AttributeSet;
 import android.util.DisplayMetrics;
 import android.util.Log;
 import android.util.Pair;
 import android.util.SparseArray;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.widget.CheckedTextView;
 import android.widget.LinearLayout;

 import androidx.annotation.AttrRes;
 import androidx.annotation.Nullable;
 import androidx.annotation.RequiresApi;
 import androidx.core.content.res.ResourcesCompat;

 import com.google.android.exoplayer2.RendererCapabilities;
 import com.google.android.exoplayer2.source.TrackGroup;
 import com.google.android.exoplayer2.source.TrackGroupArray;
 import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
 import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride;
 import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
 import com.google.android.exoplayer2.util.Assertions;

 import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
 import org.checkerframework.checker.nullness.qual.RequiresNonNull;

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;

 /**
  * A view for making track selections.
  */
 public class TrackSelectionView extends LinearLayout {
     static public int i = 0;
     static boolean isAutoVisible = false;


//    String[] res  = {"1080p","720p","360p"};

     /**
      * Listener for changes to the selected tracks.
      */
     public interface TrackSelectionListener {

         /**
          * Called when the selected tracks changed.
          *
          * @param isDisabled Whether the renderer is disabled.
          * @param overrides  List of selected track selection overrides for the renderer.
          */
         void onTrackSelectionChanged(boolean isDisabled, List<SelectionOverride> overrides);
     }

     private final int selectableItemBackgroundResourceId;
     private final LayoutInflater inflater;
     private final CheckedTextView disableView;
     private final CheckedTextView defaultView;
     private final ComponentListener componentListener;
     private final SparseArray<SelectionOverride> overrides;

     private boolean allowAdaptiveSelections;
     private boolean allowMultipleOverrides;

     private TrackNameProvider trackNameProvider;
     private CheckedTextView[][] trackViews;

     private @MonotonicNonNull MappedTrackInfo mappedTrackInfo;
     private int rendererIndex;
     private TrackGroupArray trackGroups;
     private boolean isDisabled;
     @Nullable
     private TrackSelectionListener listener;
     DialogInterface.OnClickListener onClickListener;
     Dialog dialog;
     DialogInterface.OnDismissListener onDismissListener;
     public int videoTrackindex = 6;
     DismissLayout dismissLayout;

     String videoAutoResolution = "";
     String videoResolutionOnBandwith = "";
     String videoResolutions = "";
     //StreamVideoPlayUrlActivityTemp mactivity;
     int trackIndex;
     String streamQty = "";
     ArrayList<String> li_strem_qty = new ArrayList<>();

     /**
      * Creates a track selection view.
      */
     public TrackSelectionView(Context context) {
         this(context, null);
     }

     /**
      * Creates a track selection view.
      */
     public TrackSelectionView(Context context, @Nullable AttributeSet attrs) {
         this(context, attrs, 0);
     }

     /**
      * Creates a track selection view.
      */
     @SuppressWarnings("nullness")
     public TrackSelectionView(
             Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
         super(context, attrs, defStyleAttr);


         setOrientation(LinearLayout.VERTICAL);

         overrides = new SparseArray<>();

         // Don't save view hierarchy as it needs to be reinitialized with a call to init.
         setSaveFromParentEnabled(false);

         TypedArray attributeArray =
                 context
                         .getTheme()
                         .obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
         selectableItemBackgroundResourceId = attributeArray.getResourceId(0, 0);
         attributeArray.recycle();

         inflater = LayoutInflater.from(context);
         componentListener = new ComponentListener();
         trackNameProvider = new DefaultTrackNameProvider(getResources());
         trackGroups = TrackGroupArray.EMPTY;

         // View for disabling the renderer.
         disableView =
                 (CheckedTextView)
                         inflater.inflate(android.R.layout.simple_list_item_single_choice, this, false);
         disableView.setBackgroundResource(selectableItemBackgroundResourceId);
         disableView.setText(R.string.exo_track_selection_none);
         disableView.setTextColor(getResources().getColor(R.color.exo_white));
         disableView.setEnabled(false);
         disableView.setFocusable(true);
         disableView.setOnClickListener(componentListener);
         disableView.setVisibility(View.GONE);

         // addView(disableView);
         // Divider view.
         // addView(inflater.inflate(R.layout.exo_list_divider, this, false));
         // View for clearing the override to allow the selector to use its default selection logic.
         defaultView = (CheckedTextView)
                 inflater.inflate(android.R.layout.simple_list_item_single_choice, this, false);
         defaultView.setBackgroundResource(selectableItemBackgroundResourceId);
         defaultView.setText("Auto");
         defaultView.setCheckMarkDrawable(R.drawable.transparent_icon);


         defaultView.setTextColor(getResources().getColor(R.color.exo_white));
         defaultView.setEnabled(false);
         defaultView.setFocusable(true);

//        if(li_strem_qty.size()==5)
//        {
//            defaultView.setVisibility(View.VISIBLE);
//        }else{
//            defaultView.setVisibility(View.GONE);
//        }

         defaultView.setOnClickListener(componentListener);
   /* defaultView.setTextSize(14);
    LayoutParams params = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
    );
    params.setMargins(10, 0, 10, 10);
    defaultView.setLayoutParams(params);
*/
         addView(defaultView);
     }

     /**
      * Sets whether adaptive selections (consisting of more than one track) can be made using this
      * selection view.
      *
      * <p>For the view to enable adaptive selection it is necessary both for this feature to be
      * enabled, and for the target renderer to support adaptation between the available tracks.
      *
      * @param allowAdaptiveSelections Whether adaptive selection is enabled.
      */
     public void setAllowAdaptiveSelections(boolean allowAdaptiveSelections) {
         if (this.allowAdaptiveSelections != allowAdaptiveSelections) {
             this.allowAdaptiveSelections = allowAdaptiveSelections;
             updateViews();
         }
     }

     /**
      * Sets whether tracks from multiple track groups can be selected. This results in multiple {@link
      * SelectionOverride SelectionOverrides} to be returned by {@link #getOverrides()}.
      *
      * @param allowMultipleOverrides Whether multiple track selection overrides can be selected.
      */
     public void setAllowMultipleOverrides(boolean allowMultipleOverrides) {
         if (this.allowMultipleOverrides != allowMultipleOverrides) {
             this.allowMultipleOverrides = allowMultipleOverrides;
             if (!allowMultipleOverrides && overrides.size() > 1) {
                 for (int i = overrides.size() - 1; i > 0; i--) {
                     overrides.remove(i);
                 }
             }
             updateViews();
         }
     }

     /**
      * Sets whether an option is available for disabling the renderer.
      *
      * @param showDisableOption Whether the disable option is shown.
      */
     public void setShowDisableOption(boolean showDisableOption) {
         disableView.setVisibility(showDisableOption ? View.VISIBLE : View.GONE);
     }

     //shivangi
     public void setclickListener(DismissLayout dismissLayout1, DialogInterface.OnClickListener onClickListener,
                                  Dialog dialog, DialogInterface.OnDismissListener onDismissListener, int videoTackIndex, String videoAutoResolution,
                                  String videoResolutions, String stream_qty, ArrayList<String> stream_qty_list,String videoResolutionOnBandwith) {
         this.onClickListener = onClickListener;
         this.dialog = dialog;
         this.onDismissListener = onDismissListener;
         dismissLayout = dismissLayout1;
         videoTrackindex = videoTackIndex;
         this.videoAutoResolution = videoAutoResolution;
         this.videoResolutionOnBandwith = videoResolutionOnBandwith;
         this.videoResolutions = videoResolutions;
         this.streamQty = stream_qty;
         this.li_strem_qty = stream_qty_list;
         Log.d("videoAutoResolution", "Auto-> " + videoAutoResolution + "");
         Log.d("vnxmvnmxnvmx", videoTrackindex + " setclickListener: " + stream_qty_list);
     }

     /**
      * Sets the {@link TrackNameProvider} used to generate the user visible name of each track and
      * updates the view with track names queried from the specified provider.
      *
      * @param trackNameProvider The {@link TrackNameProvider} to use.
      */
     public void setTrackNameProvider(TrackNameProvider trackNameProvider) {
         this.trackNameProvider = Assertions.checkNotNull(trackNameProvider);
         updateViews();
     }

     /**
      * Initialize the view to select tracks for a specified renderer using {@link MappedTrackInfo} and
      * a set of {@link DefaultTrackSelector.Parameters}.
      *
      * @param mappedTrackInfo The {@link MappedTrackInfo}.
      * @param rendererIndex   The index of the renderer.
      * @param isDisabled      Whether the renderer should be initially shown as disabled.
      * @param overrides       List of initial overrides to be shown for this renderer. There must be at most
      *                        one override for each track group. If {@link #setAllowMultipleOverrides(boolean)} hasn't
      *                        been set to {@code true}, only the first override is used.
      * @param listener        An optional listener for track selection updates.
      */
     public void init(
             MappedTrackInfo mappedTrackInfo,
             int rendererIndex,
             boolean isDisabled,
             List<SelectionOverride> overrides,
             @Nullable TrackSelectionListener listener) {
         this.mappedTrackInfo = mappedTrackInfo;
         this.rendererIndex = rendererIndex;
         this.isDisabled = isDisabled;
         this.listener = listener;
         int maxOverrides = allowMultipleOverrides ? overrides.size() : Math.min(overrides.size(), 1);
         for (int i = 0; i < maxOverrides; i++) {
             SelectionOverride override = overrides.get(i);
             this.overrides.put(override.groupIndex, override);
         }
         Log.d("jfasjflas", "onTrackSelectionChanged:TWO ");
         updateViews();
     }

     /**
      * Returns whether the renderer is disabled.
      */
     public boolean getIsDisabled() {
         return isDisabled;
     }

     /**
      * Returns the list of selected track selection overrides. There will be at most one override for
      * each track group.
      */
     public List<SelectionOverride> getOverrides() {
         List<SelectionOverride> overrideList = new ArrayList<>(overrides.size());
         for (int i = 0; i < overrides.size(); i++) {
             overrideList.add(overrides.valueAt(i));
         }
         return overrideList;
     }

     // Private methods.

     @RequiresApi(api = Build.VERSION_CODES.O)
     private void updateViews() {
         DisplayMetrics metrics = new DisplayMetrics();
         ((Activity) getContext()).getWindowManager()
                 .getDefaultDisplay()
                 .getMetrics(metrics);

         //1080 x 2340 pixels


         // Remove previous per-track views.
         for (int i = getChildCount() - 1; i >= 3; i--) {
             removeViewAt(i);
         }

         if (mappedTrackInfo == null) {
             // The view is not initialized.
             disableView.setEnabled(false);
             defaultView.setEnabled(false);
             return;
         }
         disableView.setEnabled(true);
         defaultView.setEnabled(true);
         Log.d("Resolution", "Auto-> " + videoAutoResolution + "");


         if (li_strem_qty.size() == 5) {
             Log.d("zzzzzzzzzzzzzz", "updateViews: ONE Auto VISIBLE");
             isAutoVisible = true;
             /*Initially if list size is 5 then must show auto with selection otherwise must hide it as it did in else part*/
             //defaultView.setText("Auto (" + videoAutoResolution + ")");//hemant
             defaultView.setText("Auto (" + videoResolutionOnBandwith + ")");//hemant
             defaultView.setCheckMarkDrawable(R.drawable.ic_right_tick_ico);
             defaultView.setVisibility(View.VISIBLE);
         } else {
             isAutoVisible = false;
             Log.d("zzzzzzzzzzzzzz", "updateViews: TWO Auto INVISIBILE");
             defaultView.setVisibility(View.GONE);


         }
         Log.d("zzzzzzzzzzzzzz", videoResolutionOnBandwith+" videoAutoResolution: THREE auto " + videoAutoResolution);

         trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
         // Add per-track views.
         trackViews = new CheckedTextView[trackGroups.length][];
         Log.d("xbcnbxncbxncbnx", "updateViews: Started " + trackGroups);
         boolean enableMultipleChoiceForMultipleOverrides = shouldEnableMultiGroupSelection();
         for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
             Log.d("xbcnbxncbxncbnx", disableView.isChecked() + " updateViews: GROUP " + groupIndex);
             TrackGroup group = trackGroups.get(groupIndex);
             boolean enableMultipleChoiceForAdaptiveSelections = shouldEnableAdaptiveSelection(groupIndex);

             trackViews[groupIndex] = new CheckedTextView[group.length];

             for (trackIndex = 0; trackIndex < group.length; trackIndex++) {
                 Log.d("xbcnbxncbxncbnx", disableView.isChecked() + " updateViews: TRACK " + trackIndex);
                 Log.d("nmnmnmnmnmnmnnnm", disableView.isChecked() + " updateViews:  ONE " + defaultView.isChecked());

/*
        if (trackIndex == 0) {
          addView(inflater.inflate(R.layout.exo_list_divider, this, false));
        }
*/


/*
        int trackViewLayoutId =
            enableMultipleChoiceForAdaptiveSelections || enableMultipleChoiceForMultipleOverrides
                ? android.R.layout.simple_list_item_multiple_choice
                : android.R.layout.simple_list_item_single_choice;
*/


                 int trackViewLayoutId = android.R.layout.simple_list_item_single_choice;
                 CheckedTextView trackView = (CheckedTextView) inflater.inflate(trackViewLayoutId, this, false);
                 trackView.setBackgroundResource(selectableItemBackgroundResourceId);
                 Log.d("track name", "track name....: " + trackNameProvider.getTrackName(group.getFormat(trackIndex)) + "...pixel.." + group.getFormat(trackIndex).getPixelCount()
                         + "...bitrate.." + group.getFormat(trackIndex).bitrate + "...width.." + group.getFormat(trackIndex).width);
                 Log.d("track name", "track name....: " + trackNameProvider.getTrackName(group.getFormat(trackIndex)) + "....." + group.getFormat(trackIndex));

                 String str = trackNameProvider.getTrackName(group.getFormat(trackIndex));
                 String strarray[] = str.split("×");
                 String heightArray[] = strarray[1].split(",");
                 // trackView.setText(trackNameProvider.getTrackName(group.getFormat(trackIndex)));
                 // trackView.setText(strarray[0].trim()+"p");
                 Log.d("jfasjflas", "select:auto " + videoAutoResolution);
                 Log.d("nmnmnmnmnmnmnnnm", disableView.isChecked() + " updateViews:  TWo " + defaultView.isChecked());
                 try {
                     if (trackIndex == 0) {
                         if (getTractName("1440p")) {
                             trackView.setText("1440p");
                         }
                     } else if (trackIndex == 1) {
                         if (getTractName("1080p")) {
                             trackView.setText("1080p");
                         }
                     } else if (trackIndex == 2) {
                         if (getTractName("720p")) {
                             trackView.setText("720p");
                         }
                     } else if (trackIndex == 3) {
                         if (getTractName("480p")) {
                             trackView.setText("480p");
                         }
                     } else if (trackIndex == 4) {
                         if (getTractName("360p")) {
                             trackView.setText("360p");
                         }
                     }

                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
                 //   trackView.setText(trackNameProvider.getTrackName(group.getFormat(trackIndex)));

                 Log.d("fasfasfa", videoTrackindex + " updateViews: " + trackIndex);
                 if (videoTrackindex == trackIndex) {
                     Log.d("zzzzzzzzzzzzzz", "updateViews: FOUR THREE MATCHED");
                     if (!Utils.flag) {
                         trackView.setCheckMarkDrawable(R.drawable.ic_right_tick_ico);
                       /* defaultView.setText("Auto");
                        defaultView.setCheckMarkDrawable(R.drawable.transparent_icon)*/
                         ;
                     }
                     Utils.flag = true;

                 } else
                     trackView.setCheckMarkDrawable(R.drawable.transparent_icon);
//if(videoAutoResolution.equals("360"))
//{
//    trackView.setCheckMarkDrawable(R.drawable.ic_right_tick_ico);
//}

                 trackView.setTextColor(getResources().getColor(R.color.exo_white));
/*        if (mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex)
            == RendererCapabilities.FORMAT_HANDLED) {
          trackView.setFocusable(true);
          trackView.setTag(Pair.create(groupIndex, trackIndex));
          trackView.setOnClickListener(componentListener);
        } else {
          trackView.setFocusable(false);
          trackView.setEnabled(false);
        }*/
                 if (mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex)
                         == RendererCapabilities.FORMAT_HANDLED) {
                     if (trackView.getText().equals("")) {
                         trackView.setVisibility(GONE);
                     } else {
                         trackView.setFocusable(true);
                         trackView.setVisibility(VISIBLE);
                         trackView.setTag(Pair.create(groupIndex, trackIndex));
                         trackView.setOnClickListener(componentListener);
                     }
                 } else {
                     trackView.setFocusable(false);
                     trackView.setEnabled(false);
                     trackView.setVisibility(GONE);
                 }

                 trackViews[groupIndex][trackIndex] = trackView;

                 trackView.setTextSize(12);

//        if (metrics.heightPixels == 720) {
//          trackView.setPadding(25,13,25,22);
//        }
//        else if (metrics.heightPixels == 1080) {
//          trackView.setPadding(25,18,25,22);
//        }
//        else if (metrics.heightPixels == 1440) {
//          trackView.setPadding(25,18,25,32);
//        }
//        else {
                 trackView.setPadding(25, 10, 25, 12);
//        }
                 // Typeface typeface = getResources().getFont(R.font.myfont);
                 //or to support all versions use


                 Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.netflix_sans_light);
                 trackView.setTypeface(typeface);
                 LayoutParams params = new LayoutParams(
                         LayoutParams.MATCH_PARENT,
                         LayoutParams.WRAP_CONTENT
                 );
                 // params.setMargins(0, 14, 0, 14);
                 trackView.setLayoutParams(params);

                 addView(trackView);
                 defaultView.setTextSize(12);
                 Typeface typeface1 = ResourcesCompat.getFont(getContext(), R.font.netflix_sans_light);
                 defaultView.setTypeface(typeface1);

                 LayoutParams params1 = new LayoutParams(
                         LayoutParams.MATCH_PARENT,
                         LayoutParams.WRAP_CONTENT
                 );
                 //  params1.setMargins(6, 14, 6, 14);
                 defaultView.setLayoutParams(params1);

//        if (metrics.heightPixels == 720) {
//          defaultView.setPadding(25,24,25,22);
//        }
//        else if (metrics.heightPixels == 1080) {
//          defaultView.setPadding(25,24,25,22);
//        }
//        else if (metrics.heightPixels == 1440) {
//          defaultView.setPadding(25,24,25,26);
//        }
//        else {
                 defaultView.setPadding(25, 24, 25, 15);
//        }

                 //addView(imageView);

              /*  if(trackIndex == 4){
                    for(int i=0;i<res.length;i++){
                       // Log.d("fanskfnask", trackIndex+" updateViews: "+i);
                        //Log.d("xzcxzczxcz", res[i]+" NAME: "+trackView.getText().toString());

                        if(res[i].equals(trackView.getText().toString())){
                            trackView.setVisibility(View.VISIBLE);
                        }else {
                            trackView.setVisibility(View.GONE);
                        }
                    }
                }*/

                 trackView.setChecked(false);
             }

         }


         updateViewStates();
     }


     private void updateViewStates() {
         boolean isResolutionTrackSelected = false;
         // boolean onceSelected = false;
         disableView.setChecked(isDisabled);
         defaultView.setChecked(!isDisabled && overrides.size() == 0);
         defaultView.setTextSize(12);
         LayoutParams params1 = new LayoutParams(
                 LayoutParams.MATCH_PARENT,
                 LayoutParams.WRAP_CONTENT
         );
         defaultView.setLayoutParams(params1);

         for (int i = 0; i < trackViews.length; i++) {
             SelectionOverride override = overrides.get(i);
             for (int j = 0; j < trackViews[i].length; j++) {
                 //trackViews[i][j].setChecked(override != null && override.containsTrack(j));
                 // trackViews[i][j].setBackgroundColor(getResources().getColor(R.color.exo_gray));
                 Log.d("zzzzzzzzzzzzzz", trackViews[i][j].getText().toString() + " updateViews: FOUR MATCHED " + (override != null && override.containsTrack(j)));


                 if (override != null && override.containsTrack(j)) {
                     /*once you click on track then this block will invoke and show tick icon respectively tacks item*/
                     isResolutionTrackSelected = true;
                     trackViews[i][j].setCheckMarkDrawable(R.drawable.ic_right_tick_ico);
                 } else {
                     trackViews[i][j].setCheckMarkDrawable(null);
                     if (!isAutoVisible && !isResolutionTrackSelected) {
                         if (trackViews[i][j].getText().toString().equals(videoAutoResolution + "p")) {
                             trackViews[i][j].setCheckMarkDrawable(R.drawable.ic_right_tick_ico);
                         }
                     }
                 }

                 trackViews[i][j].setTextSize(12);
                 LayoutParams params = new LayoutParams(
                         LayoutParams.MATCH_PARENT,
                         LayoutParams.WRAP_CONTENT
                 );

                 trackViews[i][j].setLayoutParams(params);
             }
         }

         if (li_strem_qty.size() == 5) {
             /*if track selected then must not show select auto just show Auto title instead of showing tick along with it also below will only invoke in case of size is 5,*/
             if (isResolutionTrackSelected) {
                 defaultView.setCheckMarkDrawable(null);
                 defaultView.setText("Auto");
             }
         }
     }

     private void onClick(View view) {

         if (view == disableView) {
             onDisableViewClicked();
             updateViewStates();
             if (listener != null) {
                 listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
             }
             dismissLayout.dismisslayout(4, videoAutoResolution);
             onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
             onDismissListener.onDismiss(dialog);

         } else if (view == defaultView) {
             onDefaultViewClicked();
             updateViewStates();
             if (listener != null) {
                 listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
             }
             dismissLayout.dismisslayout(4, videoAutoResolution);

             onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
             onDismissListener.onDismiss(dialog);

         } else {
             Pair<Integer, Integer> tag = (Pair<Integer, Integer>) Assertions.checkNotNull(view.getTag());
             int groupIndex = tag.first;
             int trackIndex = tag.second;

             TrackGroup group = trackGroups.get(groupIndex);
             String str = trackNameProvider.getTrackName(group.getFormat(trackIndex));
             String strarray[] = str.split("×");
             String heightArray[] = strarray[1].split(",");
             Log.d("onTrackClick", "onTrackClick:...height..." + heightArray[0].trim() + "...videoAutoResolution..." + videoAutoResolution.trim() +
                     "...videoTrackindex..." + videoTrackindex + "...trackIndex..." + trackIndex);
             /*onTrackClick:...height...720...videoAutoResolution...360p...videoTrackindex...1...trackIndex...2*/


    /* if (heightArray[0].trim().equals(videoResolutions.trim())){
       onDefaultViewClicked();
       onTrackViewClicked(view,heightArray[0].trim());
       updateViewStates();
       if (listener != null) {
         listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
       }
       onClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
       onDismissListener.onDismiss(dialog);

      }
     else */
             if (videoTrackindex == trackIndex) {
                 onDefaultViewClicked();
                 onTrackViewClicked(view, heightArray[0].trim());
                 updateViewStates();

                 if (listener != null) {
                     listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
                 }
                 onDismissListener.onDismiss(dialog);

             } else if (heightArray[0].trim().equals(videoAutoResolution.trim())) {


                 if (heightArray[0].trim().equals(videoResolutions.trim())) {
                     onDefaultViewClicked();
                     onTrackViewClicked(view, heightArray[0].trim());
                     updateViewStates();

                     if (listener != null) {
                         listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
                     }
                     onDismissListener.onDismiss(dialog);
                 } else {
                     onDefaultViewClicked();
                     onTrackViewClicked(view, heightArray[0].trim());
                     updateViewStates();
                     if (listener != null) {
                         listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
                     }
                     onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                     onDismissListener.onDismiss(dialog);


                 }
             } else {
                 onDefaultViewClicked();
                 onTrackViewClicked(view, heightArray[0].trim());
                 updateViewStates();
                 if (listener != null) {
                     listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
                 }
                 onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                 onDismissListener.onDismiss(dialog);

             }

         }

     }

     private void onDisableViewClicked() {
         isDisabled = true;
         overrides.clear();
     }

     public void autoClick() {
         Log.d("player state", "player state auto clicked...");

         onDefaultViewClicked();
         updateViewStates();
         if (listener != null) {
             listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
         }
         dismissLayout.dismisslayout(4, videoAutoResolution);

         onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
         onDismissListener.onDismiss(dialog);

     }

     private void onDefaultViewClicked() {
         isDisabled = false;
         overrides.clear();
     }

     private void onTrackViewClicked(View view, String autorRsolution) {
         isDisabled = false;
         @SuppressWarnings("unchecked")
         Pair<Integer, Integer> tag = (Pair<Integer, Integer>) Assertions.checkNotNull(view.getTag());
         int groupIndex = tag.first;
         int trackIndex = tag.second;
         SelectionOverride override = overrides.get(groupIndex);
         Assertions.checkNotNull(mappedTrackInfo);
         dismissLayout.dismisslayout(trackIndex, autorRsolution);

         Log.d("index", "index...group: " + groupIndex + "....track..." + trackIndex);
         if (override == null) {
             // Start new override.
             if (!allowMultipleOverrides && overrides.size() > 0) {
                 // Removed other overrides if we don't allow multiple overrides.
                 overrides.clear();
             }
             overrides.put(groupIndex, new SelectionOverride(groupIndex, trackIndex));
         } else {
             // An existing override is being modified.
             int overrideLength = override.length;
             int[] overrideTracks = override.tracks;
             boolean isCurrentlySelected = ((CheckedTextView) view).isChecked();
             boolean isAdaptiveAllowed = shouldEnableAdaptiveSelection(groupIndex);
             boolean isUsingCheckBox = isAdaptiveAllowed || shouldEnableMultiGroupSelection();
             if (isCurrentlySelected && isUsingCheckBox) {
                 // Remove the track from the override.
                 if (overrideLength == 1) {
                     // The last track is being removed, so the override becomes empty.
                     overrides.remove(groupIndex);
                 } else {
                     int[] tracks = getTracksRemoving(overrideTracks, trackIndex);
                     overrides.put(groupIndex, new SelectionOverride(groupIndex, tracks));
                 }
             } else if (!isCurrentlySelected) {
                 if (isAdaptiveAllowed) {
                     // Add new track to adaptive override.
                     int[] tracks = getTracksAdding(overrideTracks, trackIndex);
                     overrides.put(groupIndex, new SelectionOverride(groupIndex, tracks));
                 } else {
                     // Replace existing track in override.
                     overrides.put(groupIndex, new SelectionOverride(groupIndex, trackIndex));
                 }
             }
         }
     }

     @RequiresNonNull("mappedTrackInfo")
     private boolean shouldEnableAdaptiveSelection(int groupIndex) {
         return allowAdaptiveSelections
                 && trackGroups.get(groupIndex).length > 1
                 && mappedTrackInfo.getAdaptiveSupport(
                 rendererIndex, groupIndex, /* includeCapabilitiesExceededTracks= */ false)
                 != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
     }

     private boolean shouldEnableMultiGroupSelection() {
         return allowMultipleOverrides && trackGroups.length > 1;
     }

     private static int[] getTracksAdding(int[] tracks, int addedTrack) {
         tracks = Arrays.copyOf(tracks, tracks.length + 1);
         tracks[tracks.length - 1] = addedTrack;
         return tracks;
     }

     private static int[] getTracksRemoving(int[] tracks, int removedTrack) {
         int[] newTracks = new int[tracks.length - 1];
         int trackCount = 0;
         for (int track : tracks) {
             if (track != removedTrack) {
                 newTracks[trackCount++] = track;
             }
         }
         return newTracks;
     }

     // Internal classes.

     private class ComponentListener implements OnClickListener {

         @Override
         public void onClick(View view) {
             TrackSelectionView.this.onClick(view);
         }
     }

     public interface DismissLayout {
         public void dismisslayout(int trackIndex, String resolution);

         public int getautoValue();
     }

     public boolean getTractName(String trackName) {


         /*
          * Need to understand some condition while implementing adaptive streaming in ExoPlayer
          * Basically mater url set to exoPlayer and behalf of master URL we are getting resolutions list like. 720p,360p,1080p etc.
          * earlier it was working like that but now something change.
          *======== New Concept======
          *Now
          * */
         boolean isMatched = false;
         for (String re : li_strem_qty) {
             if (re.equals(trackName)) {
                 isMatched = true;
                 break;
             }
         }

         return isMatched;
     }


 }
