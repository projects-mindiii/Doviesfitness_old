// Generated by data binding compiler. Do not edit!
package com.doviesfitness.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.doviesfitness.R;
import com.doviesfitness.ui.bottom_tabbar.home_tab.model.AllOtherThenFeatured;
import com.makeramen.roundedimageview.RoundedImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FeedViewBinding extends ViewDataBinding {
  @NonNull
  public final View bottomView;

  @NonNull
  public final FrameLayout flFeed;

  @NonNull
  public final RelativeLayout header;

  @NonNull
  public final ImageView ivComment;

  @NonNull
  public final ImageView ivFav;

  @NonNull
  public final RoundedImageView ivFeed;

  @NonNull
  public final ImageView ivHeart;

  @NonNull
  public final FrameLayout ivPlay;

  @NonNull
  public final CircleImageView ivProfile;

  @NonNull
  public final ImageView ivShare;

  @NonNull
  public final RelativeLayout rlDescription;

  @NonNull
  public final RelativeLayout topView;

  @NonNull
  public final TextView tvComments;

  @NonNull
  public final TextView tvDescription;

  @NonNull
  public final TextView tvLikes;

  @NonNull
  public final TextView tvNew;

  @NonNull
  public final TextView tvTime;

  @NonNull
  public final TextView tvUserName;

  @Bindable
  protected AllOtherThenFeatured mFeaturedData;

  protected FeedViewBinding(Object _bindingComponent, View _root, int _localFieldCount,
      View bottomView, FrameLayout flFeed, RelativeLayout header, ImageView ivComment,
      ImageView ivFav, RoundedImageView ivFeed, ImageView ivHeart, FrameLayout ivPlay,
      CircleImageView ivProfile, ImageView ivShare, RelativeLayout rlDescription,
      RelativeLayout topView, TextView tvComments, TextView tvDescription, TextView tvLikes,
      TextView tvNew, TextView tvTime, TextView tvUserName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bottomView = bottomView;
    this.flFeed = flFeed;
    this.header = header;
    this.ivComment = ivComment;
    this.ivFav = ivFav;
    this.ivFeed = ivFeed;
    this.ivHeart = ivHeart;
    this.ivPlay = ivPlay;
    this.ivProfile = ivProfile;
    this.ivShare = ivShare;
    this.rlDescription = rlDescription;
    this.topView = topView;
    this.tvComments = tvComments;
    this.tvDescription = tvDescription;
    this.tvLikes = tvLikes;
    this.tvNew = tvNew;
    this.tvTime = tvTime;
    this.tvUserName = tvUserName;
  }

  public abstract void setFeaturedData(@Nullable AllOtherThenFeatured featuredData);

  @Nullable
  public AllOtherThenFeatured getFeaturedData() {
    return mFeaturedData;
  }

  @NonNull
  public static FeedViewBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.feed_view, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FeedViewBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FeedViewBinding>inflateInternal(inflater, R.layout.feed_view, root, attachToRoot, component);
  }

  @NonNull
  public static FeedViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.feed_view, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FeedViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FeedViewBinding>inflateInternal(inflater, R.layout.feed_view, null, false, component);
  }

  public static FeedViewBinding bind(@NonNull View view) {
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
  public static FeedViewBinding bind(@NonNull View view, @Nullable Object component) {
    return (FeedViewBinding)bind(component, view, R.layout.feed_view);
  }
}