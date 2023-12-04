package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class StreamOverviewFragmentBindingImpl extends StreamOverviewFragmentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.level_layout, 1);
        sViewsWithIds.put(R.id.tv_level, 2);
        sViewsWithIds.put(R.id.tv_level_desc, 3);
        sViewsWithIds.put(R.id.good_for_layout, 4);
        sViewsWithIds.put(R.id.tv_title_good_for, 5);
        sViewsWithIds.put(R.id.tv_good_for, 6);
        sViewsWithIds.put(R.id.equipment_layout, 7);
        sViewsWithIds.put(R.id.tv_title_equipment, 8);
        sViewsWithIds.put(R.id.tv_equipment, 9);
        sViewsWithIds.put(R.id.description_layout, 10);
        sViewsWithIds.put(R.id.tv_title_overview, 11);
        sViewsWithIds.put(R.id.tv_description, 12);
    }
    // views
    @NonNull
    private final androidx.core.widget.NestedScrollView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public StreamOverviewFragmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private StreamOverviewFragmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[10]
            , (android.widget.RelativeLayout) bindings[7]
            , (android.widget.RelativeLayout) bindings[4]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[11]
            );
        this.mboundView0 = (androidx.core.widget.NestedScrollView) bindings[0];
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