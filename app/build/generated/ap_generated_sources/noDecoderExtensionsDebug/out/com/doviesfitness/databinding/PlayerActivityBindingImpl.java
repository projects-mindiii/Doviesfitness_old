package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class PlayerActivityBindingImpl extends PlayerActivityBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.player_view, 1);
        sViewsWithIds.put(R.id.transparent_layout, 2);
        sViewsWithIds.put(R.id.backword, 3);
        sViewsWithIds.put(R.id.iv_forword, 4);
        sViewsWithIds.put(R.id.vollume_icon, 5);
        sViewsWithIds.put(R.id.seekBar, 6);
        sViewsWithIds.put(R.id.title, 7);
        sViewsWithIds.put(R.id.episode_layout, 8);
        sViewsWithIds.put(R.id.video_layout, 9);
        sViewsWithIds.put(R.id.episodes, 10);
        sViewsWithIds.put(R.id.previous_layout, 11);
        sViewsWithIds.put(R.id.previous, 12);
        sViewsWithIds.put(R.id.next_layout, 13);
        sViewsWithIds.put(R.id.next, 14);
        sViewsWithIds.put(R.id.topBlurView, 15);
        sViewsWithIds.put(R.id.video_Rv, 16);
        sViewsWithIds.put(R.id.iv_back, 17);
        sViewsWithIds.put(R.id.iv_music, 18);
        sViewsWithIds.put(R.id.iv_play, 19);
        sViewsWithIds.put(R.id.iv_pause, 20);
        sViewsWithIds.put(R.id.remaining_time1, 21);
        sViewsWithIds.put(R.id.track_layout, 22);
        sViewsWithIds.put(R.id.debug_text_view, 23);
        sViewsWithIds.put(R.id.controls_root, 24);
        sViewsWithIds.put(R.id.select_tracks_button, 25);
        sViewsWithIds.put(R.id.quality_container, 26);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public PlayerActivityBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 27, sIncludes, sViewsWithIds));
    }
    private PlayerActivityBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[3]
            , (android.widget.LinearLayout) bindings[24]
            , (android.widget.TextView) bindings[23]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.TextView) bindings[10]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.ImageView) bindings[20]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.TextView) bindings[14]
            , (android.widget.LinearLayout) bindings[13]
            , (com.google.android.exoplayer2.ui.StyledPlayerView) bindings[1]
            , (android.widget.TextView) bindings[12]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.FrameLayout) bindings[26]
            , (android.widget.TextView) bindings[21]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.SeekBar) bindings[6]
            , (android.widget.Button) bindings[25]
            , (android.widget.TextView) bindings[7]
            , (eightbitlab.com.blurview.BlurView) bindings[15]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.LinearLayout) bindings[9]
            , (androidx.recyclerview.widget.RecyclerView) bindings[16]
            , (android.widget.ImageView) bindings[5]
            );
        this.root.setTag(null);
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