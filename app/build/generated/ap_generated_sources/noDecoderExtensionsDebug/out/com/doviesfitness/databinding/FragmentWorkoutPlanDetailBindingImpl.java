package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentWorkoutPlanDetailBindingImpl extends FragmentWorkoutPlanDetailBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.full_container, 1);
        sViewsWithIds.put(R.id.my_progress, 2);
        sViewsWithIds.put(R.id.iv_featured, 3);
        sViewsWithIds.put(R.id.tv_header, 4);
        sViewsWithIds.put(R.id.tv_good_for, 5);
        sViewsWithIds.put(R.id.tv_equipment, 6);
        sViewsWithIds.put(R.id.txt_overview, 7);
        sViewsWithIds.put(R.id.btn_status, 8);
        sViewsWithIds.put(R.id.action_bar, 9);
        sViewsWithIds.put(R.id.iv_back, 10);
        sViewsWithIds.put(R.id.lock_img, 11);
        sViewsWithIds.put(R.id.iv_share, 12);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentWorkoutPlanDetailBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private FragmentWorkoutPlanDetailBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.Button) bindings[8]
            , (android.widget.LinearLayout) bindings[1]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.ProgressBar) bindings[2]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[7]
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
        if (BR.program == variableId) {
            setProgram((com.doviesfitness.ui.bottom_tabbar.home_tab.model.GetCustomerProgramDetail) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setProgram(@Nullable com.doviesfitness.ui.bottom_tabbar.home_tab.model.GetCustomerProgramDetail Program) {
        this.mProgram = Program;
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
        flag 0 (0x1L): program
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}