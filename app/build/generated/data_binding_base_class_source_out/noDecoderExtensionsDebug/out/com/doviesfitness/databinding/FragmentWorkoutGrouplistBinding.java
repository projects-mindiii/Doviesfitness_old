// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.doviesfitness.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentWorkoutGrouplistBinding extends ViewDataBinding {
  @NonNull
  public final TextView description;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final TextView levelName;

  @NonNull
  public final LinearLayout llparent;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final RelativeLayout rltvLoader;

  @NonNull
  public final SwipeRefreshLayout swipeRefresh;

  @NonNull
  public final RecyclerView workoutGroupRv;

  @NonNull
  public final ImageView workoutImg;

  @NonNull
  public final TextView workoutName;

  protected FragmentWorkoutGrouplistBinding(Object _bindingComponent, View _root,
      int _localFieldCount, TextView description, ImageView ivBack, TextView levelName,
      LinearLayout llparent, ProgressBar loader, RelativeLayout rltvLoader,
      SwipeRefreshLayout swipeRefresh, RecyclerView workoutGroupRv, ImageView workoutImg,
      TextView workoutName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.description = description;
    this.ivBack = ivBack;
    this.levelName = levelName;
    this.llparent = llparent;
    this.loader = loader;
    this.rltvLoader = rltvLoader;
    this.swipeRefresh = swipeRefresh;
    this.workoutGroupRv = workoutGroupRv;
    this.workoutImg = workoutImg;
    this.workoutName = workoutName;
  }

  @NonNull
  public static FragmentWorkoutGrouplistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_workout_grouplist, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentWorkoutGrouplistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentWorkoutGrouplistBinding>inflateInternal(inflater, R.layout.fragment_workout_grouplist, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentWorkoutGrouplistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_workout_grouplist, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentWorkoutGrouplistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentWorkoutGrouplistBinding>inflateInternal(inflater, R.layout.fragment_workout_grouplist, null, false, component);
  }

  public static FragmentWorkoutGrouplistBinding bind(@NonNull View view) {
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
  public static FragmentWorkoutGrouplistBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (FragmentWorkoutGrouplistBinding)bind(component, view, R.layout.fragment_workout_grouplist);
  }
}
