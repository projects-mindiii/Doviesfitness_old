// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.doviesfitness.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityAddNotesNewBinding extends ViewDataBinding {
  @NonNull
  public final TextView cancelButtonNote;

  @NonNull
  public final View divider;

  @NonNull
  public final EditText etNote;

  @NonNull
  public final RelativeLayout headerLayout;

  @NonNull
  public final ImageView ivPlus;

  @NonNull
  public final LinearLayout llSaveButton;

  @NonNull
  public final TextView tvDone;

  protected ActivityAddNotesNewBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView cancelButtonNote, View divider, EditText etNote, RelativeLayout headerLayout,
      ImageView ivPlus, LinearLayout llSaveButton, TextView tvDone) {
    super(_bindingComponent, _root, _localFieldCount);
    this.cancelButtonNote = cancelButtonNote;
    this.divider = divider;
    this.etNote = etNote;
    this.headerLayout = headerLayout;
    this.ivPlus = ivPlus;
    this.llSaveButton = llSaveButton;
    this.tvDone = tvDone;
  }

  @NonNull
  public static ActivityAddNotesNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_add_notes_new, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityAddNotesNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityAddNotesNewBinding>inflateInternal(inflater, R.layout.activity_add_notes_new, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityAddNotesNewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_add_notes_new, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityAddNotesNewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityAddNotesNewBinding>inflateInternal(inflater, R.layout.activity_add_notes_new, null, false, component);
  }

  public static ActivityAddNotesNewBinding bind(@NonNull View view) {
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
  public static ActivityAddNotesNewBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityAddNotesNewBinding)bind(component, view, R.layout.activity_add_notes_new);
  }
}