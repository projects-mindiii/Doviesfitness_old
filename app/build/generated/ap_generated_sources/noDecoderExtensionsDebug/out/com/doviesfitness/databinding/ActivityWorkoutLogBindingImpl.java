package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityWorkoutLogBindingImpl extends ActivityWorkoutLogBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.topBlurView, 1);
        sViewsWithIds.put(R.id.action_bar_header, 2);
        sViewsWithIds.put(R.id.header, 3);
        sViewsWithIds.put(R.id.title, 4);
        sViewsWithIds.put(R.id.cancle, 5);
        sViewsWithIds.put(R.id.devider_view, 6);
        sViewsWithIds.put(R.id.scrollView, 7);
        sViewsWithIds.put(R.id.content_layout, 8);
        sViewsWithIds.put(R.id.workout_img, 9);
        sViewsWithIds.put(R.id.workout_name, 10);
        sViewsWithIds.put(R.id.duration, 11);
        sViewsWithIds.put(R.id.radioGroup, 12);
        sViewsWithIds.put(R.id.great, 13);
        sViewsWithIds.put(R.id.good, 14);
        sViewsWithIds.put(R.id.reasonable, 15);
        sViewsWithIds.put(R.id.bad, 16);
        sViewsWithIds.put(R.id.add_calori_layout, 17);
        sViewsWithIds.put(R.id.calori_burn, 18);
        sViewsWithIds.put(R.id.add_weight_layout, 19);
        sViewsWithIds.put(R.id.current_weight, 20);
        sViewsWithIds.put(R.id.add_note, 21);
        sViewsWithIds.put(R.id.progress_rv, 22);
        sViewsWithIds.put(R.id.submit_btn, 23);
        sViewsWithIds.put(R.id.progress_layout, 24);
        sViewsWithIds.put(R.id.loader, 25);
        sViewsWithIds.put(R.id.transparentBlurView, 26);
        sViewsWithIds.put(R.id.transparent_layout, 27);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityWorkoutLogBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 28, sIncludes, sViewsWithIds));
    }
    private ActivityWorkoutLogBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.RelativeLayout) bindings[17]
            , (android.widget.EditText) bindings[21]
            , (android.widget.RelativeLayout) bindings[19]
            , (android.widget.RadioButton) bindings[16]
            , (android.widget.TextView) bindings[18]
            , (android.widget.TextView) bindings[5]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.TextView) bindings[20]
            , (android.view.View) bindings[6]
            , (android.widget.TextView) bindings[11]
            , (android.widget.RadioButton) bindings[14]
            , (android.widget.RadioButton) bindings[13]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.ProgressBar) bindings[25]
            , (android.widget.RelativeLayout) bindings[24]
            , (androidx.recyclerview.widget.RecyclerView) bindings[22]
            , (android.widget.RadioGroup) bindings[12]
            , (android.widget.RadioButton) bindings[15]
            , (androidx.core.widget.NestedScrollView) bindings[7]
            , (android.widget.Button) bindings[23]
            , (android.widget.TextView) bindings[4]
            , (eightbitlab.com.blurview.BlurView) bindings[1]
            , (eightbitlab.com.blurview.BlurView) bindings[26]
            , (android.widget.RelativeLayout) bindings[27]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.TextView) bindings[10]
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