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
import com.doviesfitness.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentWeekFourBinding extends ViewDataBinding {
  @NonNull
  public final RecyclerView week4Rv;

  protected FragmentWeekFourBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RecyclerView week4Rv) {
    super(_bindingComponent, _root, _localFieldCount);
    this.week4Rv = week4Rv;
  }

  @NonNull
  public static FragmentWeekFourBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_week_four, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentWeekFourBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentWeekFourBinding>inflateInternal(inflater, R.layout.fragment_week_four, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentWeekFourBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_week_four, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentWeekFourBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentWeekFourBinding>inflateInternal(inflater, R.layout.fragment_week_four, null, false, component);
  }

  public static FragmentWeekFourBinding bind(@NonNull View view) {
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
  public static FragmentWeekFourBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentWeekFourBinding)bind(component, view, R.layout.fragment_week_four);
  }
}