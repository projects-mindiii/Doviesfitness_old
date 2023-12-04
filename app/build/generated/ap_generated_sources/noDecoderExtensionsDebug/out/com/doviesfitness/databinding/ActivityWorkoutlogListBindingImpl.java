package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityWorkoutlogListBindingImpl extends ActivityWorkoutlogListBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.swipe_refresh, 1);
        sViewsWithIds.put(R.id.my_workout_main, 2);
        sViewsWithIds.put(R.id.my_workout_rv, 3);
        sViewsWithIds.put(R.id.no_Workout_found, 4);
        sViewsWithIds.put(R.id.dumble, 5);
        sViewsWithIds.put(R.id.txt_description, 6);
        sViewsWithIds.put(R.id.view_trans_parancy, 7);
        sViewsWithIds.put(R.id.topBlurView, 8);
        sViewsWithIds.put(R.id.toolbar_layout, 9);
        sViewsWithIds.put(R.id.iv_back, 10);
        sViewsWithIds.put(R.id.mW_title_name, 11);
        sViewsWithIds.put(R.id.iv_add, 12);
        sViewsWithIds.put(R.id.devider_view, 13);
        sViewsWithIds.put(R.id.loader, 14);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityWorkoutlogListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds));
    }
    private ActivityWorkoutlogListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[0]
            , (android.view.View) bindings[13]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.ProgressBar) bindings[14]
            , (android.widget.TextView) bindings[11]
            , (com.doviesfitness.utils.CustomNestedScrollView) bindings[2]
            , (androidx.recyclerview.widget.RecyclerView) bindings[3]
            , (android.widget.RelativeLayout) bindings[4]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[1]
            , (android.widget.RelativeLayout) bindings[9]
            , (eightbitlab.com.blurview.BlurView) bindings[8]
            , (android.widget.TextView) bindings[6]
            , (android.view.View) bindings[7]
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