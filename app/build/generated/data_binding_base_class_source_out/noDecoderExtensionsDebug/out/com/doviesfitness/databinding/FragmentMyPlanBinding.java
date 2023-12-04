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
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.doviesfitness.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentMyPlanBinding extends ViewDataBinding {
  @NonNull
  public final LinearLayout containerId;

  @NonNull
  public final View deviderView;

  @NonNull
  public final TextView dpTitleName;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final LinearLayout llButton;

  @NonNull
  public final RelativeLayout rlButton;

  @NonNull
  public final RelativeLayout toolbarLayout;

  @NonNull
  public final TextView txtCreatePlan;

  @NonNull
  public final TextView txtInbuildPlan;

  @NonNull
  public final TextView txtWorkPlanDiscription;

  protected FragmentMyPlanBinding(Object _bindingComponent, View _root, int _localFieldCount,
      LinearLayout containerId, View deviderView, TextView dpTitleName, ImageView ivBack,
      LinearLayout llButton, RelativeLayout rlButton, RelativeLayout toolbarLayout,
      TextView txtCreatePlan, TextView txtInbuildPlan, TextView txtWorkPlanDiscription) {
    super(_bindingComponent, _root, _localFieldCount);
    this.containerId = containerId;
    this.deviderView = deviderView;
    this.dpTitleName = dpTitleName;
    this.ivBack = ivBack;
    this.llButton = llButton;
    this.rlButton = rlButton;
    this.toolbarLayout = toolbarLayout;
    this.txtCreatePlan = txtCreatePlan;
    this.txtInbuildPlan = txtInbuildPlan;
    this.txtWorkPlanDiscription = txtWorkPlanDiscription;
  }

  @NonNull
  public static FragmentMyPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentMyPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentMyPlanBinding>inflateInternal(inflater, R.layout.fragment_my_plan, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentMyPlanBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_my_plan, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentMyPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentMyPlanBinding>inflateInternal(inflater, R.layout.fragment_my_plan, null, false, component);
  }

  public static FragmentMyPlanBinding bind(@NonNull View view) {
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
  public static FragmentMyPlanBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentMyPlanBinding)bind(component, view, R.layout.fragment_my_plan);
  }
}