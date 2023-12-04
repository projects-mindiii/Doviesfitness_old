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
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.doviesfitness.R;
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data5;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentDietPlanDetailBinding extends ViewDataBinding {
  @NonNull
  public final Button btnStatus;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final ImageView ivFeatured;

  @NonNull
  public final ImageView ivShare;

  @NonNull
  public final ImageView lockImg;

  @NonNull
  public final ProgressBar myProgress;

  @NonNull
  public final RelativeLayout rltvImage;

  @NonNull
  public final RelativeLayout toolbarLayout;

  @NonNull
  public final TextView tvHeading;

  @NonNull
  public final TextView txtDietDiscription;

  @Bindable
  protected Data5 mFeaturedData;

  protected FragmentDietPlanDetailBinding(Object _bindingComponent, View _root,
      int _localFieldCount, Button btnStatus, ImageView ivBack, ImageView ivFeatured,
      ImageView ivShare, ImageView lockImg, ProgressBar myProgress, RelativeLayout rltvImage,
      RelativeLayout toolbarLayout, TextView tvHeading, TextView txtDietDiscription) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnStatus = btnStatus;
    this.ivBack = ivBack;
    this.ivFeatured = ivFeatured;
    this.ivShare = ivShare;
    this.lockImg = lockImg;
    this.myProgress = myProgress;
    this.rltvImage = rltvImage;
    this.toolbarLayout = toolbarLayout;
    this.tvHeading = tvHeading;
    this.txtDietDiscription = txtDietDiscription;
  }

  public abstract void setFeaturedData(@Nullable Data5 featuredData);

  @Nullable
  public Data5 getFeaturedData() {
    return mFeaturedData;
  }

  @NonNull
  public static FragmentDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_diet_plan_detail, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentDietPlanDetailBinding>inflateInternal(inflater, R.layout.fragment_diet_plan_detail, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_diet_plan_detail, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentDietPlanDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentDietPlanDetailBinding>inflateInternal(inflater, R.layout.fragment_diet_plan_detail, null, false, component);
  }

  public static FragmentDietPlanDetailBinding bind(@NonNull View view) {
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
  public static FragmentDietPlanDetailBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentDietPlanDetailBinding)bind(component, view, R.layout.fragment_diet_plan_detail);
  }
}
