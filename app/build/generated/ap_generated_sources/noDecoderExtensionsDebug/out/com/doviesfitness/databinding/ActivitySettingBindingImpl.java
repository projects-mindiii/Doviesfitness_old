package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySettingBindingImpl extends ActivitySettingBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.container_id, 1);
        sViewsWithIds.put(R.id.rl_edit_profile, 2);
        sViewsWithIds.put(R.id.rl_change_password, 3);
        sViewsWithIds.put(R.id.rl_units, 4);
        sViewsWithIds.put(R.id.unit_value, 5);
        sViewsWithIds.put(R.id.next_icon, 6);
        sViewsWithIds.put(R.id.rl_remindertime, 7);
        sViewsWithIds.put(R.id.reminder_time, 8);
        sViewsWithIds.put(R.id.next_icon1, 9);
        sViewsWithIds.put(R.id.rl_notification, 10);
        sViewsWithIds.put(R.id.iv_notification, 11);
        sViewsWithIds.put(R.id.rl_preview, 12);
        sViewsWithIds.put(R.id.iv_preview, 13);
        sViewsWithIds.put(R.id.rl_country, 14);
        sViewsWithIds.put(R.id.tv_country, 15);
        sViewsWithIds.put(R.id.country_name, 16);
        sViewsWithIds.put(R.id.next_icon3, 17);
        sViewsWithIds.put(R.id.rl_ratesUsaap, 18);
        sViewsWithIds.put(R.id.rl_invitefriend, 19);
        sViewsWithIds.put(R.id.rl_doviesworkout, 20);
        sViewsWithIds.put(R.id.rl_aboutApplication, 21);
        sViewsWithIds.put(R.id.rl_termsandcondition, 22);
        sViewsWithIds.put(R.id.rl_privacypolicy, 23);
        sViewsWithIds.put(R.id.rl_appfaq, 24);
        sViewsWithIds.put(R.id.rl_contectus, 25);
        sViewsWithIds.put(R.id.rl_deleteAc, 26);
        sViewsWithIds.put(R.id.rl_logout, 27);
        sViewsWithIds.put(R.id.rl_version, 28);
        sViewsWithIds.put(R.id.topBlurView, 29);
        sViewsWithIds.put(R.id.toolbar_layout, 30);
        sViewsWithIds.put(R.id.iv_back, 31);
        sViewsWithIds.put(R.id.mW_title_name, 32);
        sViewsWithIds.put(R.id.devider_view, 33);
        sViewsWithIds.put(R.id.container_id1, 34);
        sViewsWithIds.put(R.id.rl_blurlayout, 35);
        sViewsWithIds.put(R.id.transparentBlurView, 36);
        sViewsWithIds.put(R.id.transparent_layout, 37);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivitySettingBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 38, sIncludes, sViewsWithIds));
    }
    private ActivitySettingBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.FrameLayout) bindings[34]
            , (android.widget.TextView) bindings[16]
            , (android.view.View) bindings[33]
            , (android.widget.ImageView) bindings[31]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.TextView) bindings[32]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.TextView) bindings[8]
            , (android.widget.RelativeLayout) bindings[21]
            , (android.widget.RelativeLayout) bindings[24]
            , (android.widget.RelativeLayout) bindings[35]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.RelativeLayout) bindings[25]
            , (android.widget.RelativeLayout) bindings[14]
            , (android.widget.RelativeLayout) bindings[26]
            , (android.widget.RelativeLayout) bindings[20]
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.RelativeLayout) bindings[19]
            , (android.widget.RelativeLayout) bindings[27]
            , (android.widget.RelativeLayout) bindings[10]
            , (android.widget.RelativeLayout) bindings[12]
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.RelativeLayout) bindings[18]
            , (android.widget.RelativeLayout) bindings[7]
            , (android.widget.RelativeLayout) bindings[22]
            , (android.widget.RelativeLayout) bindings[4]
            , (android.widget.RelativeLayout) bindings[28]
            , (android.widget.RelativeLayout) bindings[30]
            , (eightbitlab.com.blurview.BlurView) bindings[29]
            , (eightbitlab.com.blurview.BlurView) bindings[36]
            , (android.widget.RelativeLayout) bindings[37]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[5]
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