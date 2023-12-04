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
import com.doviesfitness.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityTempDownloadStreamBinding extends ViewDataBinding {
  @NonNull
  public final Button btnExplore;

  @NonNull
  public final View deviderView;

  @NonNull
  public final ImageView downloadIcon;

  @NonNull
  public final ImageView editIcon;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final RelativeLayout noDataFoundLayout;

  @NonNull
  public final TextView titleName;

  @NonNull
  public final RelativeLayout topLayout;

  @NonNull
  public final TextView txtNoDataFound;

  @NonNull
  public final RecyclerView workoutRv;

  protected ActivityTempDownloadStreamBinding(Object _bindingComponent, View _root,
      int _localFieldCount, Button btnExplore, View deviderView, ImageView downloadIcon,
      ImageView editIcon, ImageView ivBack, RelativeLayout noDataFoundLayout, TextView titleName,
      RelativeLayout topLayout, TextView txtNoDataFound, RecyclerView workoutRv) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnExplore = btnExplore;
    this.deviderView = deviderView;
    this.downloadIcon = downloadIcon;
    this.editIcon = editIcon;
    this.ivBack = ivBack;
    this.noDataFoundLayout = noDataFoundLayout;
    this.titleName = titleName;
    this.topLayout = topLayout;
    this.txtNoDataFound = txtNoDataFound;
    this.workoutRv = workoutRv;
  }

  @NonNull
  public static ActivityTempDownloadStreamBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_temp_download_stream, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityTempDownloadStreamBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityTempDownloadStreamBinding>inflateInternal(inflater, R.layout.activity_temp_download_stream, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityTempDownloadStreamBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_temp_download_stream, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityTempDownloadStreamBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityTempDownloadStreamBinding>inflateInternal(inflater, R.layout.activity_temp_download_stream, null, false, component);
  }

  public static ActivityTempDownloadStreamBinding bind(@NonNull View view) {
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
  public static ActivityTempDownloadStreamBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityTempDownloadStreamBinding)bind(component, view, R.layout.activity_temp_download_stream);
  }
}