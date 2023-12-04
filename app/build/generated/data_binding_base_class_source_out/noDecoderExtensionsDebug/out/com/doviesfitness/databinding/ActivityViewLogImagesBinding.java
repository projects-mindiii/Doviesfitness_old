// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.doviesfitness.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityViewLogImagesBinding extends ViewDataBinding {
  @NonNull
  public final ImageView cancelButton;

  @NonNull
  public final ImageView downloadIcon;

  @NonNull
  public final FrameLayout frameLayout;

  @NonNull
  public final ImageView ivShare;

  @NonNull
  public final TextView logDate;

  @NonNull
  public final RecyclerView rvImages;

  @NonNull
  public final ViewPager viewPager;

  @NonNull
  public final TextView weight;

  protected ActivityViewLogImagesBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView cancelButton, ImageView downloadIcon, FrameLayout frameLayout, ImageView ivShare,
      TextView logDate, RecyclerView rvImages, ViewPager viewPager, TextView weight) {
    super(_bindingComponent, _root, _localFieldCount);
    this.cancelButton = cancelButton;
    this.downloadIcon = downloadIcon;
    this.frameLayout = frameLayout;
    this.ivShare = ivShare;
    this.logDate = logDate;
    this.rvImages = rvImages;
    this.viewPager = viewPager;
    this.weight = weight;
  }

  @NonNull
  public static ActivityViewLogImagesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_view_log_images, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityViewLogImagesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityViewLogImagesBinding>inflateInternal(inflater, R.layout.activity_view_log_images, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityViewLogImagesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_view_log_images, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityViewLogImagesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityViewLogImagesBinding>inflateInternal(inflater, R.layout.activity_view_log_images, null, false, component);
  }

  public static ActivityViewLogImagesBinding bind(@NonNull View view) {
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
  public static ActivityViewLogImagesBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityViewLogImagesBinding)bind(component, view, R.layout.activity_view_log_images);
  }
}