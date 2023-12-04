package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentEditProfileBindingImpl extends FragmentEditProfileBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ll_main_layout, 1);
        sViewsWithIds.put(R.id.profile_ImageView, 2);
        sViewsWithIds.put(R.id.select_iv_camera, 3);
        sViewsWithIds.put(R.id.et_username, 4);
        sViewsWithIds.put(R.id.tv_username_status, 5);
        sViewsWithIds.put(R.id.et_fullName, 6);
        sViewsWithIds.put(R.id.error_fullname, 7);
        sViewsWithIds.put(R.id.txt_gender, 8);
        sViewsWithIds.put(R.id.txt_birthday, 9);
        sViewsWithIds.put(R.id.txt_height, 10);
        sViewsWithIds.put(R.id.txt_weight, 11);
        sViewsWithIds.put(R.id.et_email, 12);
        sViewsWithIds.put(R.id.error_email, 13);
        sViewsWithIds.put(R.id.btn_update, 14);
        sViewsWithIds.put(R.id.topBlurView, 15);
        sViewsWithIds.put(R.id.toolbar_layout, 16);
        sViewsWithIds.put(R.id.iv_back, 17);
        sViewsWithIds.put(R.id.dp_title_name, 18);
        sViewsWithIds.put(R.id.devider_view, 19);
        sViewsWithIds.put(R.id.loader, 20);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentEditProfileBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds));
    }
    private FragmentEditProfileBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.Button) bindings[14]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.view.View) bindings[19]
            , (android.widget.TextView) bindings[18]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[7]
            , (android.widget.EditText) bindings[12]
            , (android.widget.EditText) bindings[6]
            , (android.widget.EditText) bindings[4]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.LinearLayout) bindings[1]
            , (android.widget.ProgressBar) bindings[20]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[2]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.RelativeLayout) bindings[16]
            , (eightbitlab.com.blurview.BlurView) bindings[15]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[11]
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