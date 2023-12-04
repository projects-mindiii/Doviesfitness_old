// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.doviesfitness.R;
import eightbitlab.com.blurview.BlurView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityWorkoutLogBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout actionBarHeader;

  @NonNull
  public final RelativeLayout addCaloriLayout;

  @NonNull
  public final EditText addNote;

  @NonNull
  public final RelativeLayout addWeightLayout;

  @NonNull
  public final RadioButton bad;

  @NonNull
  public final TextView caloriBurn;

  @NonNull
  public final TextView cancle;

  @NonNull
  public final RelativeLayout containerId;

  @NonNull
  public final LinearLayout contentLayout;

  @NonNull
  public final TextView currentWeight;

  @NonNull
  public final View deviderView;

  @NonNull
  public final TextView duration;

  @NonNull
  public final RadioButton good;

  @NonNull
  public final RadioButton great;

  @NonNull
  public final RelativeLayout header;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final RelativeLayout progressLayout;

  @NonNull
  public final RecyclerView progressRv;

  @NonNull
  public final RadioGroup radioGroup;

  @NonNull
  public final RadioButton reasonable;

  @NonNull
  public final NestedScrollView scrollView;

  @NonNull
  public final Button submitBtn;

  @NonNull
  public final TextView title;

  @NonNull
  public final BlurView topBlurView;

  @NonNull
  public final BlurView transparentBlurView;

  @NonNull
  public final RelativeLayout transparentLayout;

  @NonNull
  public final ImageView workoutImg;

  @NonNull
  public final TextView workoutName;

  protected ActivityWorkoutLogBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RelativeLayout actionBarHeader, RelativeLayout addCaloriLayout, EditText addNote,
      RelativeLayout addWeightLayout, RadioButton bad, TextView caloriBurn, TextView cancle,
      RelativeLayout containerId, LinearLayout contentLayout, TextView currentWeight,
      View deviderView, TextView duration, RadioButton good, RadioButton great,
      RelativeLayout header, ProgressBar loader, RelativeLayout progressLayout,
      RecyclerView progressRv, RadioGroup radioGroup, RadioButton reasonable,
      NestedScrollView scrollView, Button submitBtn, TextView title, BlurView topBlurView,
      BlurView transparentBlurView, RelativeLayout transparentLayout, ImageView workoutImg,
      TextView workoutName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actionBarHeader = actionBarHeader;
    this.addCaloriLayout = addCaloriLayout;
    this.addNote = addNote;
    this.addWeightLayout = addWeightLayout;
    this.bad = bad;
    this.caloriBurn = caloriBurn;
    this.cancle = cancle;
    this.containerId = containerId;
    this.contentLayout = contentLayout;
    this.currentWeight = currentWeight;
    this.deviderView = deviderView;
    this.duration = duration;
    this.good = good;
    this.great = great;
    this.header = header;
    this.loader = loader;
    this.progressLayout = progressLayout;
    this.progressRv = progressRv;
    this.radioGroup = radioGroup;
    this.reasonable = reasonable;
    this.scrollView = scrollView;
    this.submitBtn = submitBtn;
    this.title = title;
    this.topBlurView = topBlurView;
    this.transparentBlurView = transparentBlurView;
    this.transparentLayout = transparentLayout;
    this.workoutImg = workoutImg;
    this.workoutName = workoutName;
  }

  @NonNull
  public static ActivityWorkoutLogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_workout_log, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityWorkoutLogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityWorkoutLogBinding>inflateInternal(inflater, R.layout.activity_workout_log, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityWorkoutLogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_workout_log, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityWorkoutLogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityWorkoutLogBinding>inflateInternal(inflater, R.layout.activity_workout_log, null, false, component);
  }

  public static ActivityWorkoutLogBinding bind(@NonNull View view) {
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
  public static ActivityWorkoutLogBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityWorkoutLogBinding)bind(component, view, R.layout.activity_workout_log);
  }
}
