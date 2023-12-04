package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityStreamVideoPlayLandscapeBindingImpl extends ActivityStreamVideoPlayLandscapeBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.playerView, 1);
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
        sViewsWithIds.put(R.id.chrome_cast, 18);
        sViewsWithIds.put(R.id.iv_music, 19);
        sViewsWithIds.put(R.id.iv_play, 20);
        sViewsWithIds.put(R.id.iv_pause, 21);
        sViewsWithIds.put(R.id.remaining_time1, 22);
        sViewsWithIds.put(R.id.cast_dialog_layout, 23);
        sViewsWithIds.put(R.id.data_layout, 24);
        sViewsWithIds.put(R.id.title1, 25);
        sViewsWithIds.put(R.id.seperator_line, 26);
        sViewsWithIds.put(R.id.list_view, 27);
        sViewsWithIds.put(R.id.disconnect, 28);
        sViewsWithIds.put(R.id.no_device_found_layout, 29);
        sViewsWithIds.put(R.id.no_data_icon, 30);
        sViewsWithIds.put(R.id.txt_no_data_found, 31);
        sViewsWithIds.put(R.id.help, 32);
        sViewsWithIds.put(R.id.loader_layout, 33);
        sViewsWithIds.put(R.id.loader, 34);
        sViewsWithIds.put(R.id.media_layout, 35);
        sViewsWithIds.put(R.id.device_name_layout, 36);
        sViewsWithIds.put(R.id.cast_icon, 37);
        sViewsWithIds.put(R.id.device_name, 38);
        sViewsWithIds.put(R.id.seperator_line1, 39);
        sViewsWithIds.put(R.id.disconnect1, 40);
        sViewsWithIds.put(R.id.iv_cancle_dialog, 41);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityStreamVideoPlayLandscapeBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 42, sIncludes, sViewsWithIds));
    }
    private ActivityStreamVideoPlayLandscapeBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[3]
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.ImageView) bindings[37]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.LinearLayout) bindings[24]
            , (android.widget.TextView) bindings[38]
            , (android.widget.RelativeLayout) bindings[36]
            , (android.widget.TextView) bindings[28]
            , (android.widget.TextView) bindings[40]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[32]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[41]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.ImageView) bindings[21]
            , (android.widget.ImageView) bindings[20]
            , (android.widget.ListView) bindings[27]
            , (android.widget.ProgressBar) bindings[34]
            , (android.widget.RelativeLayout) bindings[33]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.RelativeLayout) bindings[35]
            , (android.widget.TextView) bindings[14]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.ImageView) bindings[30]
            , (android.widget.LinearLayout) bindings[29]
            , (com.google.android.exoplayer2.ui.PlayerView) bindings[1]
            , (android.widget.TextView) bindings[12]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.TextView) bindings[22]
            , (android.widget.SeekBar) bindings[6]
            , (android.view.View) bindings[26]
            , (android.view.View) bindings[39]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[25]
            , (eightbitlab.com.blurview.BlurView) bindings[15]
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.TextView) bindings[31]
            , (android.widget.LinearLayout) bindings[9]
            , (androidx.recyclerview.widget.RecyclerView) bindings[16]
            , (android.widget.ImageView) bindings[5]
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