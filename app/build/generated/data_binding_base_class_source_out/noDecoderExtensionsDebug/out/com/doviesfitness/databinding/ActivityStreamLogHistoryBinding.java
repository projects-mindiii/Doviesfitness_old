// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.ViewPager;
import com.doviesfitness.R;
import com.google.android.material.tabs.TabLayout;
import eightbitlab.com.blurview.BlurView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityStreamLogHistoryBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout containerId;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final ImageView ivCalender;

  @NonNull
  public final TextView mWTitleName;

  @NonNull
  public final RelativeLayout rlTab;

  @NonNull
  public final TabLayout tabLayout;

  @NonNull
  public final RelativeLayout topLayout;

  @NonNull
  public final BlurView transparentBlurView;

  @NonNull
  public final View viewTransParancy;

  @NonNull
  public final ViewPager viewpager;

  protected ActivityStreamLogHistoryBinding(Object _bindingComponent, View _root,
      int _localFieldCount, RelativeLayout containerId, ImageView ivBack, ImageView ivCalender,
      TextView mWTitleName, RelativeLayout rlTab, TabLayout tabLayout, RelativeLayout topLayout,
      BlurView transparentBlurView, View viewTransParancy, ViewPager viewpager) {
    super(_bindingComponent, _root, _localFieldCount);
    this.containerId = containerId;
    this.ivBack = ivBack;
    this.ivCalender = ivCalender;
    this.mWTitleName = mWTitleName;
    this.rlTab = rlTab;
    this.tabLayout = tabLayout;
    this.topLayout = topLayout;
    this.transparentBlurView = transparentBlurView;
    this.viewTransParancy = viewTransParancy;
    this.viewpager = viewpager;
  }

  @NonNull
  public static ActivityStreamLogHistoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_stream_log_history, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityStreamLogHistoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityStreamLogHistoryBinding>inflateInternal(inflater, R.layout.activity_stream_log_history, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityStreamLogHistoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_stream_log_history, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityStreamLogHistoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityStreamLogHistoryBinding>inflateInternal(inflater, R.layout.activity_stream_log_history, null, false, component);
  }

  public static ActivityStreamLogHistoryBinding bind(@NonNull View view) {
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
  public static ActivityStreamLogHistoryBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityStreamLogHistoryBinding)bind(component, view, R.layout.activity_stream_log_history);
  }
}