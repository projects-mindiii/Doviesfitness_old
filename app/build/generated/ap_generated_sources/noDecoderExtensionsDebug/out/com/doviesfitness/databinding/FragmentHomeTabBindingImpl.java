package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentHomeTabBindingImpl extends FragmentHomeTabBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.swipe_refresh, 1);
        sViewsWithIds.put(R.id.nested_sv, 2);
        sViewsWithIds.put(R.id.rl_featured, 3);
        sViewsWithIds.put(R.id.rv_featured, 4);
        sViewsWithIds.put(R.id.right_arrow, 5);
        sViewsWithIds.put(R.id.rv_feed, 6);
        sViewsWithIds.put(R.id.topBlurView, 7);
        sViewsWithIds.put(R.id.action_bar_header, 8);
        sViewsWithIds.put(R.id.action_bar1, 9);
        sViewsWithIds.put(R.id.iv_navigation, 10);
        sViewsWithIds.put(R.id.inbox_icon, 11);
        sViewsWithIds.put(R.id.msg_count, 12);
        sViewsWithIds.put(R.id.rltv_loader, 13);
        sViewsWithIds.put(R.id.loader, 14);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentHomeTabBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds));
    }
    private FragmentHomeTabBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.FrameLayout) bindings[11]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.ProgressBar) bindings[14]
            , (android.widget.TextView) bindings[12]
            , (androidx.core.widget.NestedScrollView) bindings[2]
            , (androidx.appcompat.widget.AppCompatImageView) bindings[5]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.RelativeLayout) bindings[13]
            , (androidx.recyclerview.widget.RecyclerView) bindings[4]
            , (androidx.recyclerview.widget.RecyclerView) bindings[6]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[1]
            , (eightbitlab.com.blurview.BlurView) bindings[7]
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