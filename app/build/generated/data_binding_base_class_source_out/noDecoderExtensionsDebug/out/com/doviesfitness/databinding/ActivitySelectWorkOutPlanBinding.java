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

public abstract class ActivitySelectWorkOutPlanBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout actionBarHeader;

  @NonNull
  public final RelativeLayout containerId;

  @NonNull
  public final View deviderView;

  @NonNull
  public final TextView dpTitleName;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final TabLayout selectWorkoutPlanTablayout;

  @NonNull
  public final RelativeLayout toolbarLayout;

  @NonNull
  public final BlurView topBlurView;

  @NonNull
  public final TextView txtDone;

  @NonNull
  public final ViewPager workoutPlanViewPager;

  protected ActivitySelectWorkOutPlanBinding(Object _bindingComponent, View _root,
      int _localFieldCount, RelativeLayout actionBarHeader, RelativeLayout containerId,
      View deviderView, TextView dpTitleName, ImageView ivBack,
      TabLayout selectWorkoutPlanTablayout, RelativeLayout toolbarLayout, BlurView topBlurView,
      TextView txtDone, ViewPager workoutPlanViewPager) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actionBarHeader = actionBarHeader;
    this.containerId = containerId;
    this.deviderView = deviderView;
    this.dpTitleName = dpTitleName;
    this.ivBack = ivBack;
    this.selectWorkoutPlanTablayout = selectWorkoutPlanTablayout;
    this.toolbarLayout = toolbarLayout;
    this.topBlurView = topBlurView;
    this.txtDone = txtDone;
    this.workoutPlanViewPager = workoutPlanViewPager;
  }

  @NonNull
  public static ActivitySelectWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_select_work_out_plan, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivitySelectWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivitySelectWorkOutPlanBinding>inflateInternal(inflater, R.layout.activity_select_work_out_plan, root, attachToRoot, component);
  }

  @NonNull
  public static ActivitySelectWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_select_work_out_plan, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivitySelectWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivitySelectWorkOutPlanBinding>inflateInternal(inflater, R.layout.activity_select_work_out_plan, null, false, component);
  }

  public static ActivitySelectWorkOutPlanBinding bind(@NonNull View view) {
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
  public static ActivitySelectWorkOutPlanBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivitySelectWorkOutPlanBinding)bind(component, view, R.layout.activity_select_work_out_plan);
  }
}