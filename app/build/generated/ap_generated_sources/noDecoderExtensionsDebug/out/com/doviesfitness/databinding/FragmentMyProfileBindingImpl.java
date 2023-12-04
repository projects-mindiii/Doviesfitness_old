package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMyProfileBindingImpl extends FragmentMyProfileBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_view, 1);
        sViewsWithIds.put(R.id.iv_user_profile, 2);
        sViewsWithIds.put(R.id.toolbar_layout, 3);
        sViewsWithIds.put(R.id.iv_context_menu, 4);
        sViewsWithIds.put(R.id.iv_setting_profile, 5);
        sViewsWithIds.put(R.id.txt_user_name, 6);
        sViewsWithIds.put(R.id.tv_nickName, 7);
        sViewsWithIds.put(R.id.bottom_view, 8);
        sViewsWithIds.put(R.id.sixtab_container, 9);
        sViewsWithIds.put(R.id.top_tabbar, 10);
        sViewsWithIds.put(R.id.my_workout_ll, 11);
        sViewsWithIds.put(R.id.iv_my_workout, 12);
        sViewsWithIds.put(R.id.tv_my_workout, 13);
        sViewsWithIds.put(R.id.diet_plan_ll, 14);
        sViewsWithIds.put(R.id.iv_diet_plan, 15);
        sViewsWithIds.put(R.id.tv_diet_plan, 16);
        sViewsWithIds.put(R.id.workout_log_lnr, 17);
        sViewsWithIds.put(R.id.iv_workout_log, 18);
        sViewsWithIds.put(R.id.tv_workout_log, 19);
        sViewsWithIds.put(R.id.tab_bottom, 20);
        sViewsWithIds.put(R.id.my_workout_plan_ll, 21);
        sViewsWithIds.put(R.id.iv_workout_plan, 22);
        sViewsWithIds.put(R.id.tv_workout_plan, 23);
        sViewsWithIds.put(R.id.favourite_ll, 24);
        sViewsWithIds.put(R.id.iv_my_profile, 25);
        sViewsWithIds.put(R.id.tv_my_profile, 26);
        sViewsWithIds.put(R.id.inbox_lnr, 27);
        sViewsWithIds.put(R.id.iv_inbox, 28);
        sViewsWithIds.put(R.id.txt_notification_count, 29);
        sViewsWithIds.put(R.id.tv_inbox, 30);
        sViewsWithIds.put(R.id.iv_add_btn, 31);
        sViewsWithIds.put(R.id.profile_container_id, 32);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMyProfileBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 33, sIncludes, sViewsWithIds));
    }
    private FragmentMyProfileBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[14]
            , (android.widget.LinearLayout) bindings[24]
            , (android.widget.LinearLayout) bindings[27]
            , (android.widget.ImageView) bindings[31]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[28]
            , (android.widget.ImageView) bindings[25]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.ImageView) bindings[22]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.LinearLayout) bindings[21]
            , (android.widget.FrameLayout) bindings[32]
            , (android.widget.LinearLayout) bindings[9]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.LinearLayout) bindings[10]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[30]
            , (android.widget.TextView) bindings[26]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[19]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[29]
            , (android.widget.TextView) bindings[6]
            , (android.widget.LinearLayout) bindings[17]
            );
        this.container.setTag(null);
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