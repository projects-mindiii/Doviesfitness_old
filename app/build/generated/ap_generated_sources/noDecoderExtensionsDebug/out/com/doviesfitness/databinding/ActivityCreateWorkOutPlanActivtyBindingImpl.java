package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCreateWorkOutPlanActivtyBindingImpl extends ActivityCreateWorkOutPlanActivtyBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.main_content, 1);
        sViewsWithIds.put(R.id.appbar, 2);
        sViewsWithIds.put(R.id.img_layout, 3);
        sViewsWithIds.put(R.id.image, 4);
        sViewsWithIds.put(R.id.image1, 5);
        sViewsWithIds.put(R.id.img_txt, 6);
        sViewsWithIds.put(R.id.ll_workout_name, 7);
        sViewsWithIds.put(R.id.workout_name, 8);
        sViewsWithIds.put(R.id.ll_show_app, 9);
        sViewsWithIds.put(R.id.app_show, 10);
        sViewsWithIds.put(R.id.ll_sub_title, 11);
        sViewsWithIds.put(R.id.workout_sub_title, 12);
        sViewsWithIds.put(R.id.level_layout, 13);
        sViewsWithIds.put(R.id.ll_good, 14);
        sViewsWithIds.put(R.id.good_level, 15);
        sViewsWithIds.put(R.id.level_name, 16);
        sViewsWithIds.put(R.id.iv_forword, 17);
        sViewsWithIds.put(R.id.good_for_layout, 18);
        sViewsWithIds.put(R.id.ll_goods, 19);
        sViewsWithIds.put(R.id.txt_good_for, 20);
        sViewsWithIds.put(R.id.good_for, 21);
        sViewsWithIds.put(R.id.iv_goods, 22);
        sViewsWithIds.put(R.id.equipment_layout, 23);
        sViewsWithIds.put(R.id.ll_equipment, 24);
        sViewsWithIds.put(R.id.equipmemnts, 25);
        sViewsWithIds.put(R.id.tv_equipment, 26);
        sViewsWithIds.put(R.id.iv_equipments, 27);
        sViewsWithIds.put(R.id.ll_for_admin_use_only, 28);
        sViewsWithIds.put(R.id.allowed_users_layout, 29);
        sViewsWithIds.put(R.id.ll_allow, 30);
        sViewsWithIds.put(R.id.txt_allowed, 31);
        sViewsWithIds.put(R.id.allowed_users_txt, 32);
        sViewsWithIds.put(R.id.iv_allow, 33);
        sViewsWithIds.put(R.id.access_level_layout, 34);
        sViewsWithIds.put(R.id.access_level, 35);
        sViewsWithIds.put(R.id.dp_amount_layout, 36);
        sViewsWithIds.put(R.id.txt_amount, 37);
        sViewsWithIds.put(R.id.display_newsfeed_layout, 38);
        sViewsWithIds.put(R.id.display_newsfeed, 39);
        sViewsWithIds.put(R.id.allow_notification_layout, 40);
        sViewsWithIds.put(R.id.allow_notification, 41);
        sViewsWithIds.put(R.id.plan_category_tablayout, 42);
        sViewsWithIds.put(R.id.plan_view_pager, 43);
        sViewsWithIds.put(R.id.topBlurView, 44);
        sViewsWithIds.put(R.id.toolbar_layout, 45);
        sViewsWithIds.put(R.id.txt_cancel, 46);
        sViewsWithIds.put(R.id.dp_title_name, 47);
        sViewsWithIds.put(R.id.iv_submit_data, 48);
        sViewsWithIds.put(R.id.devider_view, 49);
        sViewsWithIds.put(R.id.loader, 50);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCreateWorkOutPlanActivtyBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 51, sIncludes, sViewsWithIds));
    }
    private ActivityCreateWorkOutPlanActivtyBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[35]
            , (android.widget.RelativeLayout) bindings[34]
            , (android.widget.TextView) bindings[41]
            , (android.widget.RelativeLayout) bindings[40]
            , (android.widget.RelativeLayout) bindings[29]
            , (android.widget.TextView) bindings[32]
            , (android.widget.TextView) bindings[10]
            , (com.google.android.material.appbar.AppBarLayout) bindings[2]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.view.View) bindings[49]
            , (android.widget.TextView) bindings[39]
            , (android.widget.RelativeLayout) bindings[38]
            , (android.widget.RelativeLayout) bindings[36]
            , (android.widget.TextView) bindings[47]
            , (android.widget.TextView) bindings[25]
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.TextView) bindings[21]
            , (android.widget.RelativeLayout) bindings[18]
            , (android.widget.TextView) bindings[15]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ImageView) bindings[33]
            , (android.widget.ImageView) bindings[27]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[22]
            , (android.widget.ImageView) bindings[48]
            , (android.widget.RelativeLayout) bindings[13]
            , (android.widget.TextView) bindings[16]
            , (android.widget.LinearLayout) bindings[30]
            , (android.widget.LinearLayout) bindings[24]
            , (android.widget.LinearLayout) bindings[28]
            , (android.widget.LinearLayout) bindings[14]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.ProgressBar) bindings[50]
            , (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[1]
            , (com.google.android.material.tabs.TabLayout) bindings[42]
            , (androidx.viewpager.widget.ViewPager) bindings[43]
            , (android.widget.RelativeLayout) bindings[45]
            , (eightbitlab.com.blurview.BlurView) bindings[44]
            , (android.widget.TextView) bindings[26]
            , (android.widget.TextView) bindings[31]
            , (android.widget.EditText) bindings[37]
            , (android.widget.TextView) bindings[46]
            , (android.widget.TextView) bindings[20]
            , (android.widget.EditText) bindings[8]
            , (android.widget.EditText) bindings[12]
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