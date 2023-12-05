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

public abstract class FragmentExerciseBinding extends ViewDataBinding {
  @NonNull
  public final Button btnAddExercise;

  @NonNull
  public final ImageView dumble;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final CustomNestedScrollView myFavExerciesMain;

  @NonNull
  public final RecyclerView myFavExerciesRv;

  @NonNull
  public final RelativeLayout noExerciesFound;

  @NonNull
  public final TextView txtAddPlan;

  @NonNull
  public final TextView txtDescription;

  protected FragmentExerciseBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button btnAddExercise, ImageView dumble, ProgressBar loader,
      CustomNestedScrollView myFavExerciesMain, RecyclerView myFavExerciesRv,
      RelativeLayout noExerciesFound, TextView txtAddPlan, TextView txtDescription) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnAddExercise = btnAddExercise;
    this.dumble = dumble;
    this.loader = loader;
    this.myFavExerciesMain = myFavExerciesMain;
    this.myFavExerciesRv = myFavExerciesRv;
    this.noExerciesFound = noExerciesFound;
    this.txtAddPlan = txtAddPlan;
    this.txtDescription = txtDescription;
  }

  @NonNull
  public static FragmentExerciseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentExerciseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentExerciseBinding>inflateInternal(inflater, R.layout.fragment_exercise, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentExerciseBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentExerciseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentExerciseBinding>inflateInternal(inflater, R.layout.fragment_exercise, null, false, component);
  }

  public static FragmentExerciseBinding bind(@NonNull View view) {
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
  public static FragmentExerciseBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentExerciseBinding)bind(component, view, R.layout.fragment_exercise);
  }
}