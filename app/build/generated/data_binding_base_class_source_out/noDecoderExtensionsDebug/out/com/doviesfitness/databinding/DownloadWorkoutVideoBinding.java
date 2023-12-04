// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.daimajia.swipe.SwipeLayout;
import com.doviesfitness.R;
import com.makeramen.roundedimageview.RoundedImageView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class DownloadWorkoutVideoBinding extends ViewDataBinding {
  @NonNull
  public final TextView description;

  @NonNull
  public final ImageView downloadIcon;

  @NonNull
  public final RelativeLayout downloadingTxt;

  @NonNull
  public final TextView episode;

  @NonNull
  public final ImageView ivForDelete;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final RelativeLayout mainView;

  @NonNull
  public final RelativeLayout mcdeletePost;

  @NonNull
  public final TextView name;

  @NonNull
  public final RelativeLayout progressLayout;

  @NonNull
  public final SwipeLayout swipe;

  @NonNull
  public final TextView timeDuration;

  @NonNull
  public final RelativeLayout topLayout;

  @NonNull
  public final RoundedImageView videoThumb;

  @NonNull
  public final RelativeLayout videoThumbLayout;

  protected DownloadWorkoutVideoBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView description, ImageView downloadIcon, RelativeLayout downloadingTxt, TextView episode,
      ImageView ivForDelete, ProgressBar loader, RelativeLayout mainView,
      RelativeLayout mcdeletePost, TextView name, RelativeLayout progressLayout, SwipeLayout swipe,
      TextView timeDuration, RelativeLayout topLayout, RoundedImageView videoThumb,
      RelativeLayout videoThumbLayout) {
    super(_bindingComponent, _root, _localFieldCount);
    this.description = description;
    this.downloadIcon = downloadIcon;
    this.downloadingTxt = downloadingTxt;
    this.episode = episode;
    this.ivForDelete = ivForDelete;
    this.loader = loader;
    this.mainView = mainView;
    this.mcdeletePost = mcdeletePost;
    this.name = name;
    this.progressLayout = progressLayout;
    this.swipe = swipe;
    this.timeDuration = timeDuration;
    this.topLayout = topLayout;
    this.videoThumb = videoThumb;
    this.videoThumbLayout = videoThumbLayout;
  }

  @NonNull
  public static DownloadWorkoutVideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.download_workout_video, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static DownloadWorkoutVideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<DownloadWorkoutVideoBinding>inflateInternal(inflater, R.layout.download_workout_video, root, attachToRoot, component);
  }

  @NonNull
  public static DownloadWorkoutVideoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.download_workout_video, null, false, component)
   */
  @NonNull
  @Deprecated
  public static DownloadWorkoutVideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<DownloadWorkoutVideoBinding>inflateInternal(inflater, R.layout.download_workout_video, null, false, component);
  }

  public static DownloadWorkoutVideoBinding bind(@NonNull View view) {
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
  public static DownloadWorkoutVideoBinding bind(@NonNull View view, @Nullable Object component) {
    return (DownloadWorkoutVideoBinding)bind(component, view, R.layout.download_workout_video);
  }
}
