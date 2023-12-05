// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.doviesfitness.R;
import com.doviesfitness.utils.CustomNestedScrollView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentExerciseLibraryBinding extends ViewDataBinding {
  @NonNull
  public final CustomNestedScrollView excericesMain;

  @NonNull
  public final RecyclerView exerciseRv;

  @NonNull
  public final View hideView;

  @NonNull
  public final SwipeRefreshLayout swipeRefresh;

  protected FragmentExerciseLibraryBinding(Object _bindingComponent, View _root,
      int _localFieldCount, CustomNestedScrollView excericesMain, RecyclerView exerciseRv,
      View hideView, SwipeRefreshLayout swipeRefresh) {
    super(_bindingComponent, _root, _localFieldCount);
    this.excericesMain = excericesMain;
    this.exerciseRv = exerciseRv;
    this.hideView = hideView;
    this.swipeRefresh = swipeRefresh;
  }

  @NonNull
  public static FragmentExerciseLibraryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_library, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentExerciseLibraryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentExerciseLibraryBinding>inflateInternal(inflater, R.layout.fragment_exercise_library, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentExerciseLibraryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_library, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentExerciseLibraryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentExerciseLibraryBinding>inflateInternal(inflater, R.layout.fragment_exercise_library, null, false, component);
  }

  public static FragmentExerciseLibraryBinding bind(@NonNull View view) {
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
  public static FragmentExerciseLibraryBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (FragmentExerciseLibraryBinding)bind(component, view, R.layout.fragment_exercise_library);
  }
}