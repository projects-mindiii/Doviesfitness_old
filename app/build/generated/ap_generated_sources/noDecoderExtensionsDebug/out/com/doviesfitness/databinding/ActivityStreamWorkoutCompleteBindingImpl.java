package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityStreamWorkoutCompleteBindingImpl extends ActivityStreamWorkoutCompleteBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.portrait_layout, 1);
        sViewsWithIds.put(R.id.complete_icon, 2);
        sViewsWithIds.put(R.id.title, 3);
        sViewsWithIds.put(R.id.description1, 4);
        sViewsWithIds.put(R.id.no_btn, 5);
        sViewsWithIds.put(R.id.yes_btn, 6);
        sViewsWithIds.put(R.id.description, 7);
        sViewsWithIds.put(R.id.iv_back, 8);
        sViewsWithIds.put(R.id.progress_layout, 9);
        sViewsWithIds.put(R.id.loader, 10);
        sViewsWithIds.put(R.id.transparentBlurView, 11);
        sViewsWithIds.put(R.id.transparent_layout, 12);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityStreamWorkoutCompleteBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private ActivityStreamWorkoutCompleteBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[2]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[4]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ProgressBar) bindings[10]
            , (android.widget.TextView) bindings[5]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.TextView) bindings[3]
            , (eightbitlab.com.blurview.BlurView) bindings[11]
            , (android.widget.RelativeLayout) bindings[12]
            , (android.widget.TextView) bindings[6]
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