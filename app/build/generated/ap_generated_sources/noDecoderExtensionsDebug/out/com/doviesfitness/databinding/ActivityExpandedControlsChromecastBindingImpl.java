package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityExpandedControlsChromecastBindingImpl extends ActivityExpandedControlsChromecastBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.media_image, 1);
        sViewsWithIds.put(R.id.text_layout, 2);
        sViewsWithIds.put(R.id.title, 3);
        sViewsWithIds.put(R.id.subtitle, 4);
        sViewsWithIds.put(R.id.bottom_layout, 5);
        sViewsWithIds.put(R.id.tv_heading, 6);
        sViewsWithIds.put(R.id.seekBar, 7);
        sViewsWithIds.put(R.id.positionTv, 8);
        sViewsWithIds.put(R.id.durationtv, 9);
        sViewsWithIds.put(R.id.close_caption, 10);
        sViewsWithIds.put(R.id.backword, 11);
        sViewsWithIds.put(R.id.play, 12);
        sViewsWithIds.put(R.id.forword, 13);
        sViewsWithIds.put(R.id.unmute, 14);
        sViewsWithIds.put(R.id.chrome_cast, 15);
        sViewsWithIds.put(R.id.back_icon, 16);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityExpandedControlsChromecastBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ActivityExpandedControlsChromecastBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[16]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.TextView) bindings[9]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.TextView) bindings[8]
            , (android.widget.SeekBar) bindings[7]
            , (android.widget.TextView) bindings[4]
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ImageView) bindings[14]
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