package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityAddExerciseBindingImpl extends ActivityAddExerciseBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_layout, 1);
        sViewsWithIds.put(R.id.iv_back, 2);
        sViewsWithIds.put(R.id.search_img, 3);
        sViewsWithIds.put(R.id.filter_icon, 4);
        sViewsWithIds.put(R.id.done_btn, 5);
        sViewsWithIds.put(R.id.rl_tab, 6);
        sViewsWithIds.put(R.id.tab_layout, 7);
        sViewsWithIds.put(R.id.view, 8);
        sViewsWithIds.put(R.id.viewpager, 9);
        sViewsWithIds.put(R.id.llFollowAlongBottomView, 10);
        sViewsWithIds.put(R.id.selected_count, 11);
        sViewsWithIds.put(R.id.btn_addExercise, 12);
        sViewsWithIds.put(R.id.llSetAndRepsBView, 13);
        sViewsWithIds.put(R.id.btn_CreateLeftAndRight, 14);
        sViewsWithIds.put(R.id.btn_CreateSuperSet, 15);
        sViewsWithIds.put(R.id.btn_addExercise_setAndReps, 16);
        sViewsWithIds.put(R.id.replace_exercises, 17);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityAddExerciseBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 18, sIncludes, sViewsWithIds));
    }
    private ActivityAddExerciseBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[5]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.LinearLayout) bindings[10]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.Button) bindings[17]
            , (android.widget.RelativeLayout) bindings[6]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.TextView) bindings[11]
            , (com.google.android.material.tabs.TabLayout) bindings[7]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.view.View) bindings[8]
            , (androidx.viewpager.widget.ViewPager) bindings[9]
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