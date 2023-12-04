// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.doviesfitness.R;
import com.doviesfitness.utils.CustomNestedScrollView;
import eightbitlab.com.blurview.BlurView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityAddToWorkOutPlanBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout actionBarHeader;

  @NonNull
  public final RecyclerView addToMyWopRv;

  @NonNull
  public final Button btnAddPlan;

  @NonNull
  public final RelativeLayout containerId;

  @NonNull
  public final TextView dpTitleName;

  @NonNull
  public final ImageView dumble;

  @NonNull
  public final ImageView ivAdd;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final CustomNestedScrollView myWopMain;

  @NonNull
  public final RelativeLayout noPlanFound;

  @NonNull
  public final SwipeRefreshLayout swipeRefresh;

  @NonNull
  public final RelativeLayout toolbarLayout;

  @NonNull
  public final BlurView topBlurView;

  @NonNull
  public final TextView txtAddPlan;

  @NonNull
  public final TextView txtDescription;

  protected ActivityAddToWorkOutPlanBinding(Object _bindingComponent, View _root,
      int _localFieldCount, RelativeLayout actionBarHeader, RecyclerView addToMyWopRv,
      Button btnAddPlan, RelativeLayout containerId, TextView dpTitleName, ImageView dumble,
      ImageView ivAdd, ImageView ivBack, CustomNestedScrollView myWopMain,
      RelativeLayout noPlanFound, SwipeRefreshLayout swipeRefresh, RelativeLayout toolbarLayout,
      BlurView topBlurView, TextView txtAddPlan, TextView txtDescription) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actionBarHeader = actionBarHeader;
    this.addToMyWopRv = addToMyWopRv;
    this.btnAddPlan = btnAddPlan;
    this.containerId = containerId;
    this.dpTitleName = dpTitleName;
    this.dumble = dumble;
    this.ivAdd = ivAdd;
    this.ivBack = ivBack;
    this.myWopMain = myWopMain;
    this.noPlanFound = noPlanFound;
    this.swipeRefresh = swipeRefresh;
    this.toolbarLayout = toolbarLayout;
    this.topBlurView = topBlurView;
    this.txtAddPlan = txtAddPlan;
    this.txtDescription = txtDescription;
  }

  @NonNull
  public static ActivityAddToWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_add_to_work_out_plan, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityAddToWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityAddToWorkOutPlanBinding>inflateInternal(inflater, R.layout.activity_add_to_work_out_plan, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityAddToWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_add_to_work_out_plan, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityAddToWorkOutPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityAddToWorkOutPlanBinding>inflateInternal(inflater, R.layout.activity_add_to_work_out_plan, null, false, component);
  }

  public static ActivityAddToWorkOutPlanBinding bind(@NonNull View view) {
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
  public static ActivityAddToWorkOutPlanBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityAddToWorkOutPlanBinding)bind(component, view, R.layout.activity_add_to_work_out_plan);
  }
}
