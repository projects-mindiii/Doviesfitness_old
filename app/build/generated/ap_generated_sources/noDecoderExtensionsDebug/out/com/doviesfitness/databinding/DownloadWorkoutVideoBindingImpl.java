package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DownloadWorkoutVideoBindingImpl extends DownloadWorkoutVideoBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.mcdelete_post, 1);
        sViewsWithIds.put(R.id.iv_for_delete, 2);
        sViewsWithIds.put(R.id.main_view, 3);
        sViewsWithIds.put(R.id.top_layout, 4);
        sViewsWithIds.put(R.id.video_thumb_layout, 5);
        sViewsWithIds.put(R.id.video_thumb, 6);
        sViewsWithIds.put(R.id.time_duration, 7);
        sViewsWithIds.put(R.id.name, 8);
        sViewsWithIds.put(R.id.episode, 9);
        sViewsWithIds.put(R.id.progress_layout, 10);
        sViewsWithIds.put(R.id.loader, 11);
        sViewsWithIds.put(R.id.download_icon, 12);
        sViewsWithIds.put(R.id.downloading_txt, 13);
        sViewsWithIds.put(R.id.description, 14);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DownloadWorkoutVideoBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds));
    }
    private DownloadWorkoutVideoBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[14]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.RelativeLayout) bindings[13]
            , (android.widget.TextView) bindings[9]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ProgressBar) bindings[11]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[8]
            , (android.widget.RelativeLayout) bindings[10]
            , (com.daimajia.swipe.SwipeLayout) bindings[0]
            , (android.widget.TextView) bindings[7]
            , (android.widget.RelativeLayout) bindings[4]
            , (com.makeramen.roundedimageview.RoundedImageView) bindings[6]
            , (android.widget.RelativeLayout) bindings[5]
            );
        this.swipe.setTag(null);
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