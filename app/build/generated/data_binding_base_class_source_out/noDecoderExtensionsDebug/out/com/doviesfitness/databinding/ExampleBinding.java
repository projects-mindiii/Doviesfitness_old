// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.doviesfitness.R;
import com.doviesfitness.utils.MutedVideoView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ExampleBinding extends ViewDataBinding {
  @NonNull
  public final TextView countDownTime;

  @NonNull
  public final RelativeLayout previewRestTimeLayout;

  @NonNull
  public final TextView previewRestTimeTv;

  @NonNull
  public final TextView previewRestTv;

  @NonNull
  public final MutedVideoView previewVideoView;

  @NonNull
  public final TextView repeat;

  @NonNull
  public final RelativeLayout restTimeLayout;

  @NonNull
  public final TextView restTimeTv;

  @NonNull
  public final TextView restTv;

  @NonNull
  public final RelativeLayout rlPreviewLayout;

  @NonNull
  public final RelativeLayout rlTxtNext;

  @NonNull
  public final RelativeLayout rlTxtView;

  @NonNull
  public final RelativeLayout shadowLayout;

  @NonNull
  public final TextView txtNext;

  @NonNull
  public final TextView txtRestNext;

  @NonNull
  public final MutedVideoView videoView;

  @NonNull
  public final View view;

  @NonNull
  public final View view1;

  protected ExampleBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView countDownTime, RelativeLayout previewRestTimeLayout, TextView previewRestTimeTv,
      TextView previewRestTv, MutedVideoView previewVideoView, TextView repeat,
      RelativeLayout restTimeLayout, TextView restTimeTv, TextView restTv,
      RelativeLayout rlPreviewLayout, RelativeLayout rlTxtNext, RelativeLayout rlTxtView,
      RelativeLayout shadowLayout, TextView txtNext, TextView txtRestNext, MutedVideoView videoView,
      View view, View view1) {
    super(_bindingComponent, _root, _localFieldCount);
    this.countDownTime = countDownTime;
    this.previewRestTimeLayout = previewRestTimeLayout;
    this.previewRestTimeTv = previewRestTimeTv;
    this.previewRestTv = previewRestTv;
    this.previewVideoView = previewVideoView;
    this.repeat = repeat;
    this.restTimeLayout = restTimeLayout;
    this.restTimeTv = restTimeTv;
    this.restTv = restTv;
    this.rlPreviewLayout = rlPreviewLayout;
    this.rlTxtNext = rlTxtNext;
    this.rlTxtView = rlTxtView;
    this.shadowLayout = shadowLayout;
    this.txtNext = txtNext;
    this.txtRestNext = txtRestNext;
    this.videoView = videoView;
    this.view = view;
    this.view1 = view1;
  }

  @NonNull
  public static ExampleBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.example, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ExampleBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ExampleBinding>inflateInternal(inflater, R.layout.example, root, attachToRoot, component);
  }

  @NonNull
  public static ExampleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.example, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ExampleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ExampleBinding>inflateInternal(inflater, R.layout.example, null, false, component);
  }

  public static ExampleBinding bind(@NonNull View view) {
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
  public static ExampleBinding bind(@NonNull View view, @Nullable Object component) {
    return (ExampleBinding)bind(component, view, R.layout.example);
  }
}