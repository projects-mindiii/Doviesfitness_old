package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FeedViewBindingImpl extends FeedViewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.header, 1);
        sViewsWithIds.put(R.id.iv_profile, 2);
        sViewsWithIds.put(R.id.tv_user_name, 3);
        sViewsWithIds.put(R.id.tv_time, 4);
        sViewsWithIds.put(R.id.fl_feed, 5);
        sViewsWithIds.put(R.id.iv_feed, 6);
        sViewsWithIds.put(R.id.iv_play, 7);
        sViewsWithIds.put(R.id.tv_new, 8);
        sViewsWithIds.put(R.id.top_view, 9);
        sViewsWithIds.put(R.id.iv_heart, 10);
        sViewsWithIds.put(R.id.iv_comment, 11);
        sViewsWithIds.put(R.id.iv_share, 12);
        sViewsWithIds.put(R.id.iv_fav, 13);
        sViewsWithIds.put(R.id.tv_likes, 14);
        sViewsWithIds.put(R.id.rl_description, 15);
        sViewsWithIds.put(R.id.tv_description, 16);
        sViewsWithIds.put(R.id.tv_comments, 17);
        sViewsWithIds.put(R.id.bottom_view, 18);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FeedViewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 19, sIncludes, sViewsWithIds));
    }
    private FeedViewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.view.View) bindings[18]
            , (android.widget.FrameLayout) bindings[5]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.ImageView) bindings[13]
            , (com.makeramen.roundedimageview.RoundedImageView) bindings[6]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.FrameLayout) bindings[7]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[2]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.RelativeLayout) bindings[15]
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[3]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
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
            setFeaturedData((com.doviesfitness.ui.bottom_tabbar.home_tab.model.AllOtherThenFeatured) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setFeaturedData(@Nullable com.doviesfitness.ui.bottom_tabbar.home_tab.model.AllOtherThenFeatured FeaturedData) {
        this.mFeaturedData = FeaturedData;
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): featuredData
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}