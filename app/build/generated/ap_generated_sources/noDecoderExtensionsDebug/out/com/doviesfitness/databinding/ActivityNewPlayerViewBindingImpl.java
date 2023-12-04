package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityNewPlayerViewBindingImpl extends ActivityNewPlayerViewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.llAllTopbar, 1);
        sViewsWithIds.put(R.id.llTopBar, 2);
        sViewsWithIds.put(R.id.rl_playerNote, 3);
        sViewsWithIds.put(R.id.noteIcon, 4);
        sViewsWithIds.put(R.id.timer, 5);
        sViewsWithIds.put(R.id.rl_music, 6);
        sViewsWithIds.put(R.id.musicIcon, 7);
        sViewsWithIds.put(R.id.ll_selectAll, 8);
        sViewsWithIds.put(R.id.header_layout_select_deselect, 9);
        sViewsWithIds.put(R.id.ll_icon_all, 10);
        sViewsWithIds.put(R.id.select_select_deselect_all, 11);
        sViewsWithIds.put(R.id.cancel_button_select_deselect, 12);
        sViewsWithIds.put(R.id.Done_create_workoutt, 13);
        sViewsWithIds.put(R.id.selected_count, 14);
        sViewsWithIds.put(R.id.divider, 15);
        sViewsWithIds.put(R.id.sv_main, 16);
        sViewsWithIds.put(R.id.rv_new_player, 17);
        sViewsWithIds.put(R.id.ll_mark_as_ungroup_delete, 18);
        sViewsWithIds.put(R.id.tv_mark_as, 19);
        sViewsWithIds.put(R.id.tv_ungroup, 20);
        sViewsWithIds.put(R.id.tv_delete_from_rounds, 21);
        sViewsWithIds.put(R.id.ll_end_workout, 22);
        sViewsWithIds.put(R.id.tv_endworkout, 23);
        sViewsWithIds.put(R.id.iv_play_button, 24);
        sViewsWithIds.put(R.id.forword_previous_play_pause, 25);
        sViewsWithIds.put(R.id.music_layout1, 26);
        sViewsWithIds.put(R.id.previous_buton, 27);
        sViewsWithIds.put(R.id.play_pause_music, 28);
        sViewsWithIds.put(R.id.forword_button, 29);
        sViewsWithIds.put(R.id.add_exercise_btn, 30);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityNewPlayerViewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 31, sIncludes, sViewsWithIds));
    }
    private ActivityNewPlayerViewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[13]
            , (android.widget.LinearLayout) bindings[30]
            , (android.widget.TextView) bindings[12]
            , (android.view.View) bindings[15]
            , (android.widget.LinearLayout) bindings[29]
            , (android.widget.RelativeLayout) bindings[25]
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.ImageView) bindings[24]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.LinearLayout) bindings[10]
            , (android.widget.LinearLayout) bindings[18]
            , (eightbitlab.com.blurview.BlurView) bindings[8]
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.LinearLayout) bindings[26]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.LinearLayout) bindings[28]
            , (android.widget.LinearLayout) bindings[27]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.LinearLayout) bindings[3]
            , (androidx.recyclerview.widget.RecyclerView) bindings[17]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.TextView) bindings[14]
            , (com.doviesfitness.utils.CustomNestedScrollView) bindings[16]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[21]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[19]
            , (android.widget.TextView) bindings[20]
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