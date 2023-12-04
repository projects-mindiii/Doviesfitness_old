package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ExampleBindingImpl extends ExampleBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.video_view, 1);
        sViewsWithIds.put(R.id.restTime_layout, 2);
        sViewsWithIds.put(R.id.rest_tv, 3);
        sViewsWithIds.put(R.id.restTime_tv, 4);
        sViewsWithIds.put(R.id.shadow_layout, 5);
        sViewsWithIds.put(R.id.preview_restTime_layout, 6);
        sViewsWithIds.put(R.id.txt_rest_next, 7);
        sViewsWithIds.put(R.id.rlTxtNext, 8);
        sViewsWithIds.put(R.id.preview_rest_tv, 9);
        sViewsWithIds.put(R.id.preview_restTime_tv, 10);
        sViewsWithIds.put(R.id.rl_preview_layout, 11);
        sViewsWithIds.put(R.id.rl_txt_view, 12);
        sViewsWithIds.put(R.id.txt_next, 13);
        sViewsWithIds.put(R.id.preview_video_view, 14);
        sViewsWithIds.put(R.id.view, 15);
        sViewsWithIds.put(R.id.view1, 16);
        sViewsWithIds.put(R.id.countDownTime, 17);
        sViewsWithIds.put(R.id.repeat, 18);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ExampleBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 19, sIncludes, sViewsWithIds));
    }
    private ExampleBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[17]
            , (android.widget.RelativeLayout) bindings[6]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[9]
            , (com.doviesfitness.utils.MutedVideoView) bindings[14]
            , (android.widget.TextView) bindings[18]
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[3]
            , (android.widget.RelativeLayout) bindings[11]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.RelativeLayout) bindings[12]
            , (android.widget.RelativeLayout) bindings[5]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[7]
            , (com.doviesfitness.utils.MutedVideoView) bindings[1]
            , (android.view.View) bindings[15]
            , (android.view.View) bindings[16]
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