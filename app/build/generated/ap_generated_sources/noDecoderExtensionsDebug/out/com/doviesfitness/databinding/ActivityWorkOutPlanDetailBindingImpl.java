package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityWorkOutPlanDetailBindingImpl extends ActivityWorkOutPlanDetailBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.full_container, 4);
        sViewsWithIds.put(R.id.my_progress, 5);
        sViewsWithIds.put(R.id.iv_featured, 6);
        sViewsWithIds.put(R.id.tv_header, 7);
        sViewsWithIds.put(R.id.tv_sub_header, 8);
        sViewsWithIds.put(R.id.tv_title_GDFR, 9);
        sViewsWithIds.put(R.id.tv_ttl_eqpmnt, 10);
        sViewsWithIds.put(R.id.ttl_overview, 11);
        sViewsWithIds.put(R.id.btn_addToMyPlan, 12);
        sViewsWithIds.put(R.id.btn_Exclusive, 13);
        sViewsWithIds.put(R.id.action_bar, 14);
        sViewsWithIds.put(R.id.iv_back, 15);
        sViewsWithIds.put(R.id.lock_img, 16);
        sViewsWithIds.put(R.id.dollor_img, 17);
        sViewsWithIds.put(R.id.iv_share, 18);
        sViewsWithIds.put(R.id.rl_loader, 19);
        sViewsWithIds.put(R.id.loader, 20);
        sViewsWithIds.put(R.id.container_id1, 21);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityWorkOutPlanDetailBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 22, sIncludes, sViewsWithIds));
    }
    private ActivityWorkOutPlanDetailBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[14]
            , (android.widget.Button) bindings[12]
            , (android.widget.Button) bindings[13]
            , (android.widget.FrameLayout) bindings[21]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.ProgressBar) bindings[20]
            , (android.widget.ImageView) bindings[16]
            , (android.widget.ProgressBar) bindings[5]
            , (android.widget.RelativeLayout) bindings[19]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[3]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvEquipment.setTag(null);
        this.tvGoodFor.setTag(null);
        this.txtOverview.setTag(null);
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
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.program);
        super.requestRebind();
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
        java.lang.String programGoodforMasterName = null;
        java.lang.String programGoodforMasterNameReplaceJavaLangStringJavaLangString = null;
        com.doviesfitness.ui.bottom_tabbar.home_tab.model.GetCustomerProgramDetail program = mProgram;
        java.lang.String ProgramGoodforMasterNameReplaceJavaLangStringJavaLangString1 = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (program != null) {
                    // read program.goodforMasterName
                    programGoodforMasterName = program.getGoodforMasterName();
                }


                if (programGoodforMasterName != null) {
                    // read program.goodforMasterName.replace("|", ", ")
                    programGoodforMasterNameReplaceJavaLangStringJavaLangString = programGoodforMasterName.replace("|", ", ");
                    // read program.goodforMasterName.replace(" |", ",")
                    ProgramGoodforMasterNameReplaceJavaLangStringJavaLangString1 = programGoodforMasterName.replace(" |", ",");
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvEquipment, programGoodforMasterNameReplaceJavaLangStringJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvGoodFor, ProgramGoodforMasterNameReplaceJavaLangStringJavaLangString1);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.txtOverview, programGoodforMasterNameReplaceJavaLangStringJavaLangString);
        }
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