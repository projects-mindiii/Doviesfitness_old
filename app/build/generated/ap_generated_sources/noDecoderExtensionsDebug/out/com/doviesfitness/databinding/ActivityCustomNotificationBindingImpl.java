package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCustomNotificationBindingImpl extends ActivityCustomNotificationBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.app_bar, 1);
        sViewsWithIds.put(R.id.toolbar_layout1, 2);
        sViewsWithIds.put(R.id.iv_custom_image, 3);
        sViewsWithIds.put(R.id.toolbar, 4);
        sViewsWithIds.put(R.id.nested_sv, 5);
        sViewsWithIds.put(R.id.complete_view, 6);
        sViewsWithIds.put(R.id.bottom_view, 7);
        sViewsWithIds.put(R.id.custom_title_heading, 8);
        sViewsWithIds.put(R.id.txt_discription, 9);
        sViewsWithIds.put(R.id.btn_status, 10);
        sViewsWithIds.put(R.id.like_comment_layout, 11);
        sViewsWithIds.put(R.id.iv_heart, 12);
        sViewsWithIds.put(R.id.iv_comment, 13);
        sViewsWithIds.put(R.id.tv_likes, 14);
        sViewsWithIds.put(R.id.tv_commments, 15);
        sViewsWithIds.put(R.id.rl_description, 16);
        sViewsWithIds.put(R.id.tv_description, 17);
        sViewsWithIds.put(R.id.tv_comments, 18);
        sViewsWithIds.put(R.id.rv_comments, 19);
        sViewsWithIds.put(R.id.ll_message, 20);
        sViewsWithIds.put(R.id.et_message, 21);
        sViewsWithIds.put(R.id.send_msg_button, 22);
        sViewsWithIds.put(R.id.tv_read_all_comments, 23);
        sViewsWithIds.put(R.id.toolbar_layout, 24);
        sViewsWithIds.put(R.id.iv_backIcon, 25);
        sViewsWithIds.put(R.id.progress_layout, 26);
        sViewsWithIds.put(R.id.loader, 27);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCustomNotificationBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 28, sIncludes, sViewsWithIds));
    }
    private ActivityCustomNotificationBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[1]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.Button) bindings[10]
            , (android.widget.RelativeLayout) bindings[6]
            , (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[0]
            , (android.widget.TextView) bindings[8]
            , (android.widget.EditText) bindings[21]
            , (android.widget.ImageView) bindings[25]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.ProgressBar) bindings[27]
            , (androidx.core.widget.NestedScrollView) bindings[5]
            , (android.widget.RelativeLayout) bindings[26]
            , (android.widget.RelativeLayout) bindings[16]
            , (androidx.recyclerview.widget.RecyclerView) bindings[19]
            , (android.widget.ImageView) bindings[22]
            , (androidx.appcompat.widget.Toolbar) bindings[4]
            , (android.widget.RelativeLayout) bindings[24]
            , (com.google.android.material.appbar.CollapsingToolbarLayout) bindings[2]
            , (android.widget.TextView) bindings[18]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[9]
            );
        this.coordinateLayout.setTag(null);
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