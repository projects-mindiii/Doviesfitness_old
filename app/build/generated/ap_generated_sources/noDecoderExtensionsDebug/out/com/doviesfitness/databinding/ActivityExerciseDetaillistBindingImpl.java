package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityExerciseDetaillistBindingImpl extends ActivityExerciseDetaillistBinding  {

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
        sViewsWithIds.put(R.id.done_btn, 4);
        sViewsWithIds.put(R.id.filter_icon, 5);
        sViewsWithIds.put(R.id.devider_view, 6);
        sViewsWithIds.put(R.id.sv_main, 7);
        sViewsWithIds.put(R.id.txt_filter, 8);
        sViewsWithIds.put(R.id.exercise_rv, 9);
        sViewsWithIds.put(R.id.no_record_icon, 10);
        sViewsWithIds.put(R.id.no_record_found, 11);
        sViewsWithIds.put(R.id.progress_layout, 12);
        sViewsWithIds.put(R.id.loader, 13);
        sViewsWithIds.put(R.id.tv_video, 14);
        sViewsWithIds.put(R.id.btn_clear_filter, 15);
        sViewsWithIds.put(R.id.add_exercises, 16);
        sViewsWithIds.put(R.id.ll_delete_duplicate, 17);
        sViewsWithIds.put(R.id.selected_count, 18);
        sViewsWithIds.put(R.id.btn_addExercise, 19);
        sViewsWithIds.put(R.id.ll_setsAndRepsBottomView, 20);
        sViewsWithIds.put(R.id.btn_CreateLeftAndRight, 21);
        sViewsWithIds.put(R.id.btn_CreateSuperSet, 22);
        sViewsWithIds.put(R.id.btn_addExercise_setAndReps, 23);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityExerciseDetaillistBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 24, sIncludes, sViewsWithIds));
    }
    private ActivityExerciseDetaillistBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.Button) bindings[16]
            , (android.widget.TextView) bindings[19]
            , (android.widget.TextView) bindings[23]
            , (android.widget.Button) bindings[15]
            , (android.widget.TextView) bindings[21]
            , (android.widget.TextView) bindings[22]
            , (android.view.View) bindings[6]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[3]
            , (androidx.recyclerview.widget.RecyclerView) bindings[9]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.LinearLayout) bindings[17]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.ProgressBar) bindings[13]
            , (android.widget.TextView) bindings[11]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.RelativeLayout) bindings[12]
            , (android.widget.TextView) bindings[18]
            , (com.doviesfitness.utils.CustomNestedScrollView) bindings[7]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[8]
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