package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityStreamDetailNewBindingImpl extends ActivityStreamDetailNewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.main_content, 1);
        sViewsWithIds.put(R.id.appbar, 2);
        sViewsWithIds.put(R.id.collapsing_toolbar, 3);
        sViewsWithIds.put(R.id.rl_pinned, 4);
        sViewsWithIds.put(R.id.iv_workout, 5);
        sViewsWithIds.put(R.id.workout_name, 6);
        sViewsWithIds.put(R.id.workout_description, 7);
        sViewsWithIds.put(R.id.play_video_layout, 8);
        sViewsWithIds.put(R.id.iv_share, 9);
        sViewsWithIds.put(R.id.share_txt, 10);
        sViewsWithIds.put(R.id.play_video, 11);
        sViewsWithIds.put(R.id.iv_fav, 12);
        sViewsWithIds.put(R.id.favorites, 13);
        sViewsWithIds.put(R.id.plan_category_tablayout, 14);
        sViewsWithIds.put(R.id.plan_view_pager, 15);
        sViewsWithIds.put(R.id.iv_back, 16);
        sViewsWithIds.put(R.id.chrome_cast, 17);
        sViewsWithIds.put(R.id.lock_img, 18);
        sViewsWithIds.put(R.id.iv_setting_profile, 19);
        sViewsWithIds.put(R.id.rltv_loader, 20);
        sViewsWithIds.put(R.id.loader, 21);
        sViewsWithIds.put(R.id.transparentBlurView, 22);
        sViewsWithIds.put(R.id.transparent_layout, 23);
        sViewsWithIds.put(R.id.btn_Exclusive, 24);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityStreamDetailNewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 25, sIncludes, sViewsWithIds));
    }
    private ActivityStreamDetailNewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[2]
            , (android.widget.Button) bindings[24]
            , (android.widget.ImageView) bindings[17]
            , (com.google.android.material.appbar.CollapsingToolbarLayout) bindings[3]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.TextView) bindings[13]
            , (android.widget.ImageView) bindings[16]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ProgressBar) bindings[21]
            , (android.widget.ImageView) bindings[18]
            , (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[1]
            , (com.google.android.material.tabs.TabLayout) bindings[14]
            , (androidx.viewpager.widget.ViewPager) bindings[15]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.RelativeLayout) bindings[4]
            , (android.widget.RelativeLayout) bindings[20]
            , (android.widget.TextView) bindings[10]
            , (eightbitlab.com.blurview.BlurView) bindings[22]
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[6]
            );
        this.containerView.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
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
            return variableSet;
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
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}