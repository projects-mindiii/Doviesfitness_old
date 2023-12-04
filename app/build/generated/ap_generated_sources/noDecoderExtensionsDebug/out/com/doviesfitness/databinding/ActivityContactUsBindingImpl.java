package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityContactUsBindingImpl extends ActivityContactUsBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.name_txt, 1);
        sViewsWithIds.put(R.id.name_edt, 2);
        sViewsWithIds.put(R.id.error_name_txt, 3);
        sViewsWithIds.put(R.id.email_txt, 4);
        sViewsWithIds.put(R.id.email_edt, 5);
        sViewsWithIds.put(R.id.error_email_txt, 6);
        sViewsWithIds.put(R.id.subject_txt, 7);
        sViewsWithIds.put(R.id.subject_layout, 8);
        sViewsWithIds.put(R.id.subject_value, 9);
        sViewsWithIds.put(R.id.arrow_icon, 10);
        sViewsWithIds.put(R.id.error_subject_txt, 11);
        sViewsWithIds.put(R.id.message_txt, 12);
        sViewsWithIds.put(R.id.message, 13);
        sViewsWithIds.put(R.id.error_message_txt, 14);
        sViewsWithIds.put(R.id.invite_btn, 15);
        sViewsWithIds.put(R.id.facebook_icon, 16);
        sViewsWithIds.put(R.id.insta_icon, 17);
        sViewsWithIds.put(R.id.twitter_icon, 18);
        sViewsWithIds.put(R.id.dovies_icon, 19);
        sViewsWithIds.put(R.id.progress_layout, 20);
        sViewsWithIds.put(R.id.loader, 21);
        sViewsWithIds.put(R.id.topBlurView, 22);
        sViewsWithIds.put(R.id.action_bar_header, 23);
        sViewsWithIds.put(R.id.header_layout, 24);
        sViewsWithIds.put(R.id.iv_back, 25);
        sViewsWithIds.put(R.id.devider_view, 26);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityContactUsBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 27, sIncludes, sViewsWithIds));
    }
    private ActivityContactUsBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.view.View) bindings[26]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.EditText) bindings[5]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[11]
            , (android.widget.ImageView) bindings[16]
            , (android.widget.RelativeLayout) bindings[24]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.Button) bindings[15]
            , (android.widget.ImageView) bindings[25]
            , (android.widget.ProgressBar) bindings[21]
            , (android.widget.EditText) bindings[13]
            , (android.widget.TextView) bindings[12]
            , (android.widget.EditText) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (android.widget.RelativeLayout) bindings[20]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[9]
            , (eightbitlab.com.blurview.BlurView) bindings[22]
            , (android.widget.ImageView) bindings[18]
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