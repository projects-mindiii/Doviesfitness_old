// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.doviesfitness.R;
import eightbitlab.com.blurview.BlurView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentStreamCollectionNewBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout actionBar1;

  @NonNull
  public final RelativeLayout actionBarHeader;

  @NonNull
  public final ImageView chromeCast;

  @NonNull
  public final RelativeLayout containerId;

  @NonNull
  public final TextView download;

  @NonNull
  public final TextView favorite;

  @NonNull
  public final TextView firstCollectionName;

  @NonNull
  public final ImageView ivDownload;

  @NonNull
  public final ImageView ivFav;

  @NonNull
  public final ImageView ivOverview;

  @NonNull
  public final ImageView ivPined;

  @NonNull
  public final ImageView ivSearch;

  @NonNull
  public final ImageView ivShare;

  @NonNull
  public final TextView levelName;

  @NonNull
  public final ImageView logIcon;

  @NonNull
  public final NestedScrollView nestedSv;

  @NonNull
  public final ImageView nextIcon1;

  @NonNull
  public final ImageView nextIcon2;

  @NonNull
  public final TextView overviewTxt;

  @NonNull
  public final ImageView playVideo;

  @NonNull
  public final RelativeLayout playVideoLayout;

  @NonNull
  public final AppCompatImageView rightArrow;

  @NonNull
  public final RelativeLayout rlFeatured;

  @NonNull
  public final RelativeLayout rlPinned;

  @NonNull
  public final RelativeLayout rlRecent;

  @NonNull
  public final RecyclerView rvFeatured;

  @NonNull
  public final RecyclerView rvRecent;

  @NonNull
  public final RecyclerView rvWorkout;

  @NonNull
  public final TextView shareTxt;

  @NonNull
  public final LinearLayout streamListLayout;

  @NonNull
  public final SwipeRefreshLayout swipeRefresh;

  @NonNull
  public final View topHiddenView;

  @NonNull
  public final BlurView transparentBlurView;

  @NonNull
  public final RelativeLayout transparentLayout;

  @NonNull
  public final TextView tvMostRecent;

  @NonNull
  public final TextView txtAllWorkoutCollection;

  @NonNull
  public final TextView txtNoDataFound;

  @NonNull
  public final TextView workoutDescription;

  @NonNull
  public final TextView workoutName;

  protected FragmentStreamCollectionNewBinding(Object _bindingComponent, View _root,
      int _localFieldCount, RelativeLayout actionBar1, RelativeLayout actionBarHeader,
      ImageView chromeCast, RelativeLayout containerId, TextView download, TextView favorite,
      TextView firstCollectionName, ImageView ivDownload, ImageView ivFav, ImageView ivOverview,
      ImageView ivPined, ImageView ivSearch, ImageView ivShare, TextView levelName,
      ImageView logIcon, NestedScrollView nestedSv, ImageView nextIcon1, ImageView nextIcon2,
      TextView overviewTxt, ImageView playVideo, RelativeLayout playVideoLayout,
      AppCompatImageView rightArrow, RelativeLayout rlFeatured, RelativeLayout rlPinned,
      RelativeLayout rlRecent, RecyclerView rvFeatured, RecyclerView rvRecent,
      RecyclerView rvWorkout, TextView shareTxt, LinearLayout streamListLayout,
      SwipeRefreshLayout swipeRefresh, View topHiddenView, BlurView transparentBlurView,
      RelativeLayout transparentLayout, TextView tvMostRecent, TextView txtAllWorkoutCollection,
      TextView txtNoDataFound, TextView workoutDescription, TextView workoutName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actionBar1 = actionBar1;
    this.actionBarHeader = actionBarHeader;
    this.chromeCast = chromeCast;
    this.containerId = containerId;
    this.download = download;
    this.favorite = favorite;
    this.firstCollectionName = firstCollectionName;
    this.ivDownload = ivDownload;
    this.ivFav = ivFav;
    this.ivOverview = ivOverview;
    this.ivPined = ivPined;
    this.ivSearch = ivSearch;
    this.ivShare = ivShare;
    this.levelName = levelName;
    this.logIcon = logIcon;
    this.nestedSv = nestedSv;
    this.nextIcon1 = nextIcon1;
    this.nextIcon2 = nextIcon2;
    this.overviewTxt = overviewTxt;
    this.playVideo = playVideo;
    this.playVideoLayout = playVideoLayout;
    this.rightArrow = rightArrow;
    this.rlFeatured = rlFeatured;
    this.rlPinned = rlPinned;
    this.rlRecent = rlRecent;
    this.rvFeatured = rvFeatured;
    this.rvRecent = rvRecent;
    this.rvWorkout = rvWorkout;
    this.shareTxt = shareTxt;
    this.streamListLayout = streamListLayout;
    this.swipeRefresh = swipeRefresh;
    this.topHiddenView = topHiddenView;
    this.transparentBlurView = transparentBlurView;
    this.transparentLayout = transparentLayout;
    this.tvMostRecent = tvMostRecent;
    this.txtAllWorkoutCollection = txtAllWorkoutCollection;
    this.txtNoDataFound = txtNoDataFound;
    this.workoutDescription = workoutDescription;
    this.workoutName = workoutName;
  }

  @NonNull
  public static FragmentStreamCollectionNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_stream_collection_new, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentStreamCollectionNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentStreamCollectionNewBinding>inflateInternal(inflater, R.layout.fragment_stream_collection_new, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentStreamCollectionNewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_stream_collection_new, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentStreamCollectionNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentStreamCollectionNewBinding>inflateInternal(inflater, R.layout.fragment_stream_collection_new, null, false, component);
  }

  public static FragmentStreamCollectionNewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentStreamCollectionNewBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (FragmentStreamCollectionNewBinding)bind(component, view, R.layout.fragment_stream_collection_new);
  }
}
