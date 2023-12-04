package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityFeedDetailsBindingImpl extends ActivityFeedDetailsBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.main_layout, 11);
        sViewsWithIds.put(R.id.header, 12);
        sViewsWithIds.put(R.id.iv_profile, 13);
        sViewsWithIds.put(R.id.fl_feed, 14);
        sViewsWithIds.put(R.id.iv_feed, 15);
        sViewsWithIds.put(R.id.playerView, 16);
        sViewsWithIds.put(R.id.tv_new, 17);
        sViewsWithIds.put(R.id.iv_share, 18);
        sViewsWithIds.put(R.id.view1, 19);
        sViewsWithIds.put(R.id.tv_description, 20);
        sViewsWithIds.put(R.id.tv_comments, 21);
        sViewsWithIds.put(R.id.rl_comment1, 22);
        sViewsWithIds.put(R.id.iv_profile1, 23);
        sViewsWithIds.put(R.id.tv_username1, 24);
        sViewsWithIds.put(R.id.rl_comment2, 25);
        sViewsWithIds.put(R.id.iv_profile2, 26);
        sViewsWithIds.put(R.id.tv_usernam2, 27);
        sViewsWithIds.put(R.id.et_message, 28);
        sViewsWithIds.put(R.id.send_msg_button, 29);
        sViewsWithIds.put(R.id.progress_layout, 30);
        sViewsWithIds.put(R.id.loader, 31);
        sViewsWithIds.put(R.id.tv_video, 32);
        sViewsWithIds.put(R.id.topBlurView, 33);
        sViewsWithIds.put(R.id.action_bar_header, 34);
        sViewsWithIds.put(R.id.action_bar, 35);
        sViewsWithIds.put(R.id.iv_back, 36);
        sViewsWithIds.put(R.id.devider_view, 37);
    }
    // views
    @NonNull
    private final android.widget.TextView mboundView10;
    @NonNull
    private final android.view.View mboundView3;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityFeedDetailsBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 38, sIncludes, sViewsWithIds));
    }
    private ActivityFeedDetailsBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[35]
            , (android.widget.RelativeLayout) bindings[34]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.view.View) bindings[37]
            , (android.widget.EditText) bindings[28]
            , (android.widget.FrameLayout) bindings[14]
            , (android.widget.RelativeLayout) bindings[12]
            , (android.widget.ImageView) bindings[36]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[6]
            , (com.makeramen.roundedimageview.RoundedImageView) bindings[15]
            , (android.widget.ImageView) bindings[4]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[13]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[23]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[26]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.LinearLayout) bindings[9]
            , (android.widget.ProgressBar) bindings[31]
            , (android.widget.ScrollView) bindings[11]
            , (com.google.android.exoplayer2.ui.PlayerView) bindings[16]
            , (android.widget.RelativeLayout) bindings[30]
            , (android.widget.RelativeLayout) bindings[22]
            , (android.widget.RelativeLayout) bindings[25]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.ImageView) bindings[29]
            , (eightbitlab.com.blurview.BlurView) bindings[33]
            , (android.widget.TextView) bindings[21]
            , (android.widget.TextView) bindings[20]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[27]
            , (android.widget.TextView) bindings[24]
            , (android.widget.TextView) bindings[32]
            , (android.view.View) bindings[19]
            );
        this.containerId.setTag(null);
        this.ivComment.setTag(null);
        this.ivFav.setTag(null);
        this.ivHeart.setTag(null);
        this.llMessage.setTag(null);
        this.mboundView10 = (android.widget.TextView) bindings[10];
        this.mboundView10.setTag(null);
        this.mboundView3 = (android.view.View) bindings[3];
        this.mboundView3.setTag(null);
        this.rlDescription.setTag(null);
        this.tvLikes.setTag(null);
        this.tvTime.setTag(null);
        this.tvUserName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.featuredData == variableId) {
            setFeaturedData((com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data3) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setFeaturedData(@Nullable com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data3 FeaturedData) {
        this.mFeaturedData = FeaturedData;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.featuredData);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        boolean featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImage = false;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikes = null;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsCommentAllow = null;
        boolean featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0 = false;
        int featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1ViewVISIBLEViewGONE = 0;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsContentType = null;
        android.graphics.drawable.Drawable featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1IvHeartAndroidDrawableIcFavoriteActiveIvHeartAndroidDrawableIcFavorite = null;
        android.graphics.drawable.Drawable featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1IvFavAndroidDrawableIcStarActiveIvFavAndroidDrawableIcStar = null;
        boolean featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1 = false;
        boolean featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1 = false;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsDescription = null;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatus = null;
        boolean featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmpty = false;
        int featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmptyViewGONEViewVISIBLE = 0;
        boolean featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1 = false;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsAddedDate = null;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0FeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikeFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikes = null;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsCreatorName = null;
        com.doviesfitness.ui.bottom_tabbar.home_tab.model.GetNewsFeedDetail featuredDataGetNewsFeedDetailGetInt0 = null;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLike = null;
        boolean featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0 = false;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImageJavaLangStringPhotoFeaturedDataGetNewsFeedDetailGetInt0NewsContentType = null;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsLikeStatus = null;
        int featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImageViewGONEViewVISIBLE = 0;
        java.util.List<com.doviesfitness.ui.bottom_tabbar.home_tab.model.GetNewsFeedDetail> featuredDataGetNewsFeedDetail = null;
        com.doviesfitness.ui.bottom_tabbar.home_tab.model.Data3 featuredData = mFeaturedData;
        java.lang.String featuredDataGetNewsFeedDetailGetInt0NewsLikeCount = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (featuredData != null) {
                    // read featuredData.get_news_feed_detail
                    featuredDataGetNewsFeedDetail = featuredData.getGet_news_feed_detail();
                }


                if (featuredDataGetNewsFeedDetail != null) {
                    // read featuredData.get_news_feed_detail.get(0)
                    featuredDataGetNewsFeedDetailGetInt0 = featuredDataGetNewsFeedDetail.get(0);
                }


                if (featuredDataGetNewsFeedDetailGetInt0 != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_comment_allow
                    featuredDataGetNewsFeedDetailGetInt0NewsCommentAllow = featuredDataGetNewsFeedDetailGetInt0.getNews_comment_allow();
                    // read featuredData.get_news_feed_detail.get(0).news_content_type
                    featuredDataGetNewsFeedDetailGetInt0NewsContentType = featuredDataGetNewsFeedDetailGetInt0.getNews_content_type();
                    // read featuredData.get_news_feed_detail.get(0).news_description
                    featuredDataGetNewsFeedDetailGetInt0NewsDescription = featuredDataGetNewsFeedDetailGetInt0.getNews_description();
                    // read featuredData.get_news_feed_detail.get(0).news_favourite_status
                    featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatus = featuredDataGetNewsFeedDetailGetInt0.getNews_favourite_status();
                    // read featuredData.get_news_feed_detail.get(0).news_added_date
                    featuredDataGetNewsFeedDetailGetInt0NewsAddedDate = featuredDataGetNewsFeedDetailGetInt0.getNews_added_date();
                    // read featuredData.get_news_feed_detail.get(0).news_creator_name
                    featuredDataGetNewsFeedDetailGetInt0NewsCreatorName = featuredDataGetNewsFeedDetailGetInt0.getNews_creator_name();
                    // read featuredData.get_news_feed_detail.get(0).news_like_status
                    featuredDataGetNewsFeedDetailGetInt0NewsLikeStatus = featuredDataGetNewsFeedDetailGetInt0.getNews_like_status();
                    // read featuredData.get_news_feed_detail.get(0).news_like_count
                    featuredDataGetNewsFeedDetailGetInt0NewsLikeCount = featuredDataGetNewsFeedDetailGetInt0.getNews_like_count();
                }


                if (featuredDataGetNewsFeedDetailGetInt0NewsCommentAllow != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_comment_allow.equals("1")
                    featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1 = featuredDataGetNewsFeedDetailGetInt0NewsCommentAllow.equals("1");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
                if (featuredDataGetNewsFeedDetailGetInt0NewsContentType != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image")
                    featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImage = featuredDataGetNewsFeedDetailGetInt0NewsContentType.equals("Image");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImage) {
                        dirtyFlags |= 0x8000L;
                        dirtyFlags |= 0x20000L;
                }
                else {
                        dirtyFlags |= 0x4000L;
                        dirtyFlags |= 0x10000L;
                }
            }
                if (featuredDataGetNewsFeedDetailGetInt0NewsDescription != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_description.empty
                    featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmpty = featuredDataGetNewsFeedDetailGetInt0NewsDescription.isEmpty();
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmpty) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }
                if (featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatus != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_favourite_status.equals("1")
                    featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1 = featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatus.equals("1");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }
                if (featuredDataGetNewsFeedDetailGetInt0NewsLikeStatus != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_like_status.equals("1")
                    featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1 = featuredDataGetNewsFeedDetailGetInt0NewsLikeStatus.equals("1");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }
                if (featuredDataGetNewsFeedDetailGetInt0NewsLikeCount != null) {
                    // read featuredData.get_news_feed_detail.get(0).news_like_count.equals("0")
                    featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0 = featuredDataGetNewsFeedDetailGetInt0NewsLikeCount.equals("0");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }


                // read featuredData.get_news_feed_detail.get(0).news_comment_allow.equals("1") ? View.VISIBLE : View.GONE
                featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1ViewVISIBLEViewGONE = ((featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image") ? View.GONE : View.VISIBLE
                featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImageViewGONEViewVISIBLE = ((featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImage) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read featuredData.get_news_feed_detail.get(0).news_description.empty ? View.GONE : View.VISIBLE
                featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmptyViewGONEViewVISIBLE = ((featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmpty) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read featuredData.get_news_feed_detail.get(0).news_favourite_status.equals("1") ? @android:drawable/ic_star_active : @android:drawable/ic_star
                featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1IvFavAndroidDrawableIcStarActiveIvFavAndroidDrawableIcStar = ((featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivFav.getContext(), R.drawable.ic_star_active)) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivFav.getContext(), R.drawable.ic_star)));
                // read featuredData.get_news_feed_detail.get(0).news_like_status.equals("1") ? @android:drawable/ic_favorite_active : @android:drawable/ic_favorite
                featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1IvHeartAndroidDrawableIcFavoriteActiveIvHeartAndroidDrawableIcFavorite = ((featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivHeart.getContext(), R.drawable.ic_favorite_active)) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivHeart.getContext(), R.drawable.ic_favorite)));
        }
        // batch finished

        if ((dirtyFlags & 0x3L) != 0) {

                // read featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? true : featuredData.get_news_feed_detail.get(0).news_like_count.equals("0")
                featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0 = ((featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0) ? (true) : (featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0));
                // read featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image") ? "Photo" : featuredData.get_news_feed_detail.get(0).news_content_type
                featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImageJavaLangStringPhotoFeaturedDataGetNewsFeedDetailGetInt0NewsContentType = ((featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImage) ? ("Photo") : (featuredDataGetNewsFeedDetailGetInt0NewsContentType));
            if((dirtyFlags & 0x3L) != 0) {
                if(featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0) {
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x1000L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x1000L) != 0) {

                // read (featuredData.get_news_feed_detail.get(0).news_like_count) + (" likes")
                featuredDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikes = (featuredDataGetNewsFeedDetailGetInt0NewsLikeCount) + (" likes");
        }
        if ((dirtyFlags & 0x2000L) != 0) {

                // read (featuredData.get_news_feed_detail.get(0).news_like_count) + (" like")
                featuredDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLike = (featuredDataGetNewsFeedDetailGetInt0NewsLikeCount) + (" like");
        }

        if ((dirtyFlags & 0x3L) != 0) {

                // read featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? true : featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? (featuredData.get_news_feed_detail.get(0).news_like_count) + (" like") : (featuredData.get_news_feed_detail.get(0).news_like_count) + (" likes")
                featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0FeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikeFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikes = ((featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0) ? (featuredDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLike) : (featuredDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikes));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.ivComment.setVisibility(featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1ViewVISIBLEViewGONE);
            androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.ivFav, featuredDataGetNewsFeedDetailGetInt0NewsFavouriteStatusEqualsJavaLangString1IvFavAndroidDrawableIcStarActiveIvFavAndroidDrawableIcStar);
            androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.ivHeart, featuredDataGetNewsFeedDetailGetInt0NewsLikeStatusEqualsJavaLangString1IvHeartAndroidDrawableIcFavoriteActiveIvHeartAndroidDrawableIcFavorite);
            this.llMessage.setVisibility(featuredDataGetNewsFeedDetailGetInt0NewsCommentAllowEqualsJavaLangString1ViewVISIBLEViewGONE);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView10, featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImageJavaLangStringPhotoFeaturedDataGetNewsFeedDetailGetInt0NewsContentType);
            this.mboundView3.setVisibility(featuredDataGetNewsFeedDetailGetInt0NewsContentTypeEqualsJavaLangStringImageViewGONEViewVISIBLE);
            this.rlDescription.setVisibility(featuredDataGetNewsFeedDetailGetInt0NewsDescriptionEmptyViewGONEViewVISIBLE);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvLikes, featuredDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0BooleanTrueFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountEqualsJavaLangString0FeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikeFeaturedDataGetNewsFeedDetailGetInt0NewsLikeCountJavaLangStringLikes);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTime, featuredDataGetNewsFeedDetailGetInt0NewsAddedDate);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvUserName, featuredDataGetNewsFeedDetailGetInt0NewsCreatorName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): featuredData
        flag 1 (0x2L): null
        flag 2 (0x3L): featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? true : featuredData.get_news_feed_detail.get(0).news_like_count.equals("0")
        flag 3 (0x4L): featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? true : featuredData.get_news_feed_detail.get(0).news_like_count.equals("0")
        flag 4 (0x5L): featuredData.get_news_feed_detail.get(0).news_comment_allow.equals("1") ? View.VISIBLE : View.GONE
        flag 5 (0x6L): featuredData.get_news_feed_detail.get(0).news_comment_allow.equals("1") ? View.VISIBLE : View.GONE
        flag 6 (0x7L): featuredData.get_news_feed_detail.get(0).news_like_status.equals("1") ? @android:drawable/ic_favorite_active : @android:drawable/ic_favorite
        flag 7 (0x8L): featuredData.get_news_feed_detail.get(0).news_like_status.equals("1") ? @android:drawable/ic_favorite_active : @android:drawable/ic_favorite
        flag 8 (0x9L): featuredData.get_news_feed_detail.get(0).news_favourite_status.equals("1") ? @android:drawable/ic_star_active : @android:drawable/ic_star
        flag 9 (0xaL): featuredData.get_news_feed_detail.get(0).news_favourite_status.equals("1") ? @android:drawable/ic_star_active : @android:drawable/ic_star
        flag 10 (0xbL): featuredData.get_news_feed_detail.get(0).news_description.empty ? View.GONE : View.VISIBLE
        flag 11 (0xcL): featuredData.get_news_feed_detail.get(0).news_description.empty ? View.GONE : View.VISIBLE
        flag 12 (0xdL): featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? true : featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? (featuredData.get_news_feed_detail.get(0).news_like_count) + (" like") : (featuredData.get_news_feed_detail.get(0).news_like_count) + (" likes")
        flag 13 (0xeL): featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? true : featuredData.get_news_feed_detail.get(0).news_like_count.equals("0") ? (featuredData.get_news_feed_detail.get(0).news_like_count) + (" like") : (featuredData.get_news_feed_detail.get(0).news_like_count) + (" likes")
        flag 14 (0xfL): featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image") ? "Photo" : featuredData.get_news_feed_detail.get(0).news_content_type
        flag 15 (0x10L): featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image") ? "Photo" : featuredData.get_news_feed_detail.get(0).news_content_type
        flag 16 (0x11L): featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image") ? View.GONE : View.VISIBLE
        flag 17 (0x12L): featuredData.get_news_feed_detail.get(0).news_content_type.equals("Image") ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}