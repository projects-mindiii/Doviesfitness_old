// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.doviesfitness.R;
import com.doviesfitness.utils.CustomNestedScrollView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityExerciseDetaillistBinding extends ViewDataBinding {
  @NonNull
  public final Button addExercises;

  @NonNull
  public final TextView btnAddExercise;

  @NonNull
  public final TextView btnAddExerciseSetAndReps;

  @NonNull
  public final Button btnClearFilter;

  @NonNull
  public final TextView btnCreateLeftAndRight;

  @NonNull
  public final TextView btnCreateSuperSet;

  @NonNull
  public final View deviderView;

  @NonNull
  public final TextView doneBtn;

  @NonNull
  public final TextView exerciseName;

  @NonNull
  public final RecyclerView exerciseRv;

  @NonNull
  public final ImageView filterIcon;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final LinearLayout llDeleteDuplicate;

  @NonNull
  public final LinearLayout llSetsAndRepsBottomView;

  @NonNull
  public final ProgressBar loader;

  @NonNull
  public final TextView noRecordFound;

  @NonNull
  public final ImageView noRecordIcon;

  @NonNull
  public final RelativeLayout progressLayout;

  @NonNull
  public final TextView selectedCount;

  @NonNull
  public final CustomNestedScrollView svMain;

  @NonNull
  public final RelativeLayout toolbarLayout;

  @NonNull
  public final TextView tvVideo;

  @NonNull
  public final TextView txtFilter;

  protected ActivityExerciseDetaillistBinding(Object _bindingComponent, View _root,
      int _localFieldCount, Button addExercises, TextView btnAddExercise,
      TextView btnAddExerciseSetAndReps, Button btnClearFilter, TextView btnCreateLeftAndRight,
      TextView btnCreateSuperSet, View deviderView, TextView doneBtn, TextView exerciseName,
      RecyclerView exerciseRv, ImageView filterIcon, ImageView ivBack,
      LinearLayout llDeleteDuplicate, LinearLayout llSetsAndRepsBottomView, ProgressBar loader,
      TextView noRecordFound, ImageView noRecordIcon, RelativeLayout progressLayout,
      TextView selectedCount, CustomNestedScrollView svMain, RelativeLayout toolbarLayout,
      TextView tvVideo, TextView txtFilter) {
    super(_bindingComponent, _root, _localFieldCount);
    this.addExercises = addExercises;
    this.btnAddExercise = btnAddExercise;
    this.btnAddExerciseSetAndReps = btnAddExerciseSetAndReps;
    this.btnClearFilter = btnClearFilter;
    this.btnCreateLeftAndRight = btnCreateLeftAndRight;
    this.btnCreateSuperSet = btnCreateSuperSet;
    this.deviderView = deviderView;
    this.doneBtn = doneBtn;
    this.exerciseName = exerciseName;
    this.exerciseRv = exerciseRv;
    this.filterIcon = filterIcon;
    this.ivBack = ivBack;
    this.llDeleteDuplicate = llDeleteDuplicate;
    this.llSetsAndRepsBottomView = llSetsAndRepsBottomView;
    this.loader = loader;
    this.noRecordFound = noRecordFound;
    this.noRecordIcon = noRecordIcon;
    this.progressLayout = progressLayout;
    this.selectedCount = selectedCount;
    this.svMain = svMain;
    this.toolbarLayout = toolbarLayout;
    this.tvVideo = tvVideo;
    this.txtFilter = txtFilter;
  }

  @NonNull
  public static ActivityExerciseDetaillistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_exercise_detaillist, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityExerciseDetaillistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityExerciseDetaillistBinding>inflateInternal(inflater, R.layout.activity_exercise_detaillist, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityExerciseDetaillistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_exercise_detaillist, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityExerciseDetaillistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityExerciseDetaillistBinding>inflateInternal(inflater, R.layout.activity_exercise_detaillist, null, false, component);
  }

  public static ActivityExerciseDetaillistBinding bind(@NonNull View view) {
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
  public static ActivityExerciseDetaillistBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityExerciseDetaillistBinding)bind(component, view, R.layout.activity_exercise_detaillist);
  }
}
