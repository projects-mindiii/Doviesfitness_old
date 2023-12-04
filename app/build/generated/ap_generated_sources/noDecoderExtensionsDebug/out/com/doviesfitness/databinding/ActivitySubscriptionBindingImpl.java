package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySubscriptionBindingImpl extends ActivitySubscriptionBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_view, 1);
        sViewsWithIds.put(R.id.topBlurView, 2);
        sViewsWithIds.put(R.id.action_bar_header, 3);
        sViewsWithIds.put(R.id.toolbar, 4);
        sViewsWithIds.put(R.id.iv_back, 5);
        sViewsWithIds.put(R.id.cancel_button, 6);
        sViewsWithIds.put(R.id.devider_view, 7);
        sViewsWithIds.put(R.id.main_layout, 8);
        sViewsWithIds.put(R.id.membership_rv, 9);
        sViewsWithIds.put(R.id.static_rv, 10);
        sViewsWithIds.put(R.id.success_stories_txt, 11);
        sViewsWithIds.put(R.id.succes_story_rv, 12);
        sViewsWithIds.put(R.id.iv_swipe, 13);
        sViewsWithIds.put(R.id.terms_title, 14);
        sViewsWithIds.put(R.id.terms_description, 15);
        sViewsWithIds.put(R.id.tv_terms_and_privacy, 16);
        sViewsWithIds.put(R.id.progress_layout, 17);
        sViewsWithIds.put(R.id.loader, 18);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivitySubscriptionBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 19, sIncludes, sViewsWithIds));
    }
    private ActivitySubscriptionBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.view.View) bindings[7]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.ProgressBar) bindings[18]
            , (androidx.core.widget.NestedScrollView) bindings[8]
            , (androidx.recyclerview.widget.RecyclerView) bindings[9]
            , (android.widget.RelativeLayout) bindings[17]
            , (androidx.recyclerview.widget.RecyclerView) bindings[10]
            , (androidx.recyclerview.widget.RecyclerView) bindings[12]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[14]
            , (android.widget.RelativeLayout) bindings[4]
            , (eightbitlab.com.blurview.BlurView) bindings[2]
            , (android.widget.LinearLayout) bindings[1]
            , (android.widget.TextView) bindings[16]
            );
        this.containerId.setTag(null);
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