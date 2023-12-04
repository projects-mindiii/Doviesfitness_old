package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityGoodForBindingImpl extends ActivityGoodForBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar_layout, 1);
        sViewsWithIds.put(R.id.iv_back, 2);
        sViewsWithIds.put(R.id.exercise_name, 3);
        sViewsWithIds.put(R.id.devider_view, 4);
        sViewsWithIds.put(R.id.search_layout, 5);
        sViewsWithIds.put(R.id.search_icon, 6);
        sViewsWithIds.put(R.id.et_search, 7);
        sViewsWithIds.put(R.id.clear_txt, 8);
        sViewsWithIds.put(R.id.cancel_action, 9);
        sViewsWithIds.put(R.id.exercise_rv, 10);
        sViewsWithIds.put(R.id.no_record_found, 11);
        sViewsWithIds.put(R.id.done_btn, 12);
        sViewsWithIds.put(R.id.progress_layout, 13);
        sViewsWithIds.put(R.id.loader, 14);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityGoodForBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds));
    }
    private ActivityGoodForBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[9]
            , (android.widget.ImageView) bindings[8]
            , (android.view.View) bindings[4]
            , (android.widget.Button) bindings[12]
            , (android.widget.EditText) bindings[7]
            , (android.widget.TextView) bindings[3]
            , (com.doviesfitness.utils.MyRecycleView) bindings[10]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ProgressBar) bindings[14]
            , (android.widget.TextView) bindings[11]
            , (android.widget.RelativeLayout) bindings[13]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.RelativeLayout) bindings[5]
            , (android.widget.RelativeLayout) bindings[1]
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