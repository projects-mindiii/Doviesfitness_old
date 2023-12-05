// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.ViewPager;
import com.doviesfitness.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityShowDietPlanDetailBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appbar;

  @NonNull
  public final FrameLayout containerId;

  @NonNull
  public final RelativeLayout containerView;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final ImageView ivSettingProfile;

  @NonNull
  public final ImageView ivUserProfile;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final CoordinatorLayout mainContent;

  @NonNull
  public final TabLayout planCategoryTablayout;

  @NonNull
  public final ViewPager planViewPager;

  @NonNull
  public final RelativeLayout rltvLoader;

  @NonNull
  public final RelativeLayout topView;

  @NonNull
  public final TextView tvSubHeader;

  @NonNull
  public final TextView txtUserName;

  protected ActivityShowDietPlanDetailBinding(Object _bindingComponent, View _root,
      int _localFieldCount, AppBarLayout appbar, FrameLayout containerId,
      RelativeLayout containerView, ImageView ivBack, ImageView ivSettingProfile,
      ImageView ivUserProfile, ProgressBar loader, CoordinatorLayout mainContent,
      TabLayout planCategoryTablayout, ViewPager planViewPager, RelativeLayout rltvLoader,
      RelativeLayout topView, TextView tvSubHeader, TextView txtUserName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appbar = appbar;
    this.containerId = containerId;
    this.containerView = containerView;
    this.ivBack = ivBack;
    this.ivSettingProfile = ivSettingProfile;
    this.ivUserProfile = ivUserProfile;
    this.loader = loader;
    this.mainContent = mainContent;
    this.planCategoryTablayout = planCategoryTablayout;
    this.planViewPager = planViewPager;
    this.rltvLoader = rltvLoader;
    this.topView = topView;
    this.tvSubHeader = tvSubHeader;
    this.txtUserName = txtUserName;
  }

  @NonNull
  public static ActivityShowDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_show_diet_plan_detail, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityShowDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityShowDietPlanDetailBinding>inflateInternal(inflater, R.layout.activity_show_diet_plan_detail, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityShowDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_show_diet_plan_detail, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityShowDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityShowDietPlanDetailBinding>inflateInternal(inflater, R.layout.activity_show_diet_plan_detail, null, false, component);
  }

  public static ActivityShowDietPlanDetailBinding bind(@NonNull View view) {
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
  public static ActivityShowDietPlanDetailBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityShowDietPlanDetailBinding)bind(component, view, R.layout.activity_show_diet_plan_detail);
  }
}