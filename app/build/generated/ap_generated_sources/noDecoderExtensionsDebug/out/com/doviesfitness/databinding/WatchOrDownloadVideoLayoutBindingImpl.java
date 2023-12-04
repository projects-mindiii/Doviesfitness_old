package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class WatchOrDownloadVideoLayoutBindingImpl extends WatchOrDownloadVideoLayoutBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.top_layout, 1);
        sViewsWithIds.put(R.id.video_thumb_layout, 2);
        sViewsWithIds.put(R.id.video_thumb, 3);
        sViewsWithIds.put(R.id.play_video, 4);
        sViewsWithIds.put(R.id.time_duration, 5);
        sViewsWithIds.put(R.id.name, 6);
        sViewsWithIds.put(R.id.episode, 7);
        sViewsWithIds.put(R.id.progress_layout, 8);
        sViewsWithIds.put(R.id.loader, 9);
        sViewsWithIds.put(R.id.download_layout, 10);
        sViewsWithIds.put(R.id.download_icon, 11);
        sViewsWithIds.put(R.id.lock_img, 12);
        sViewsWithIds.put(R.id.description, 13);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WatchOrDownloadVideoLayoutBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private WatchOrDownloadVideoLayoutBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[13]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.RelativeLayout) bindings[10]
            , (android.widget.TextView) bindings[7]
            , (android.widget.ProgressBar) bindings[9]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.TextView) bindings[5]
            , (android.widget.RelativeLayout) bindings[1]
            , (com.makeramen.roundedimageview.RoundedImageView) bindings[3]
            , (android.widget.RelativeLayout) bindings[2]
            );
        this.mainLayout.setTag(null);
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