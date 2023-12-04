// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.doviesfitness.R;
import com.doviesfitness.utils.CustomNestedScrollView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityNotificationExerciesBinding extends ViewDataBinding {
  @NonNull
  public final Button btnClearFilter;

  @NonNull
  public final View deviderView;

  @NonNull
  public final TextView doneBtn;

  @NonNull
  public final TextView exerciseName;

  @NonNull
  public final RecyclerView exerciseRv;

  @NonNull
  public final ImageView filterIcon;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final TextView noRecordFound;

  @NonNull
  public final ImageView noRecordIcon;

  @NonNull
  public final RelativeLayout progressLayout;

  @NonNull
  public final CustomNestedScrollView svMain;

  @NonNull
  public final RelativeLayout toolbarLayout;

  @NonNull
  public final TextView tvVideo;

  @NonNull
  public final TextView txtFilter;

  protected ActivityNotificationExerciesBinding(Object _bindingComponent, View _root,
      int _localFieldCount, Button btnClearFilter, View deviderView, TextView doneBtn,
      TextView exerciseName, RecyclerView exerciseRv, ImageView filterIcon, ImageView ivBack,
      ProgressBar loader, TextView noRecordFound, ImageView noRecordIcon,
      RelativeLayout progressLayout, CustomNestedScrollView svMain, RelativeLayout toolbarLayout,
      TextView tvVideo, TextView txtFilter) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnClearFilter = btnClearFilter;
    this.deviderView = deviderView;
    this.doneBtn = doneBtn;
    this.exerciseName = exerciseName;
    this.exerciseRv = exerciseRv;
    this.filterIcon = filterIcon;
    this.ivBack = ivBack;
    this.loader = loader;
    this.noRecordFound = noRecordFound;
    this.noRecordIcon = noRecordIcon;
    this.progressLayout = progressLayout;
    this.svMain = svMain;
    this.toolbarLayout = toolbarLayout;
    this.tvVideo = tvVideo;
    this.txtFilter = txtFilter;
  }

  @NonNull
  public static ActivityNotificationExerciesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_notification_exercies, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityNotificationExerciesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityNotificationExerciesBinding>inflateInternal(inflater, R.layout.activity_notification_exercies, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityNotificationExerciesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_notification_exercies, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityNotificationExerciesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityNotificationExerciesBinding>inflateInternal(inflater, R.layout.activity_notification_exercies, null, false, component);
  }

  public static ActivityNotificationExerciesBinding bind(@NonNull View view) {
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
  public static ActivityNotificationExerciesBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityNotificationExerciesBinding)bind(component, view, R.layout.activity_notification_exercies);
  }
}
