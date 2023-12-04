package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityStreamVideoPlayBindingImpl extends ActivityStreamVideoPlayBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.video_view, 1);
        sViewsWithIds.put(R.id.playerView, 2);
        sViewsWithIds.put(R.id.shadow_layout, 3);
        sViewsWithIds.put(R.id.view, 4);
        sViewsWithIds.put(R.id.view1, 5);
        sViewsWithIds.put(R.id.preview_img, 6);
        sViewsWithIds.put(R.id.restTime_layout, 7);
        sViewsWithIds.put(R.id.rest_tv, 8);
        sViewsWithIds.put(R.id.restTime_tv, 9);
        sViewsWithIds.put(R.id.countDownTime, 10);
        sViewsWithIds.put(R.id.repeat, 11);
        sViewsWithIds.put(R.id.video_Rv, 12);
        sViewsWithIds.put(R.id.landscape_exercise_time_layout, 13);
        sViewsWithIds.put(R.id.landscape_exercise_time, 14);
        sViewsWithIds.put(R.id.landscape_exercise_name, 15);
        sViewsWithIds.put(R.id.play_controler_layout, 16);
        sViewsWithIds.put(R.id.music_layout, 17);
        sViewsWithIds.put(R.id.previous_layout, 18);
        sViewsWithIds.put(R.id.previous_icon, 19);
        sViewsWithIds.put(R.id.pause_layout, 20);
        sViewsWithIds.put(R.id.next_layout, 21);
        sViewsWithIds.put(R.id.next_icon, 22);
        sViewsWithIds.put(R.id.orientation_layout, 23);
        sViewsWithIds.put(R.id.orientation_icon, 24);
        sViewsWithIds.put(R.id.pause_workout_layout, 25);
        sViewsWithIds.put(R.id.totaltime, 26);
        sViewsWithIds.put(R.id.EName, 27);
        sViewsWithIds.put(R.id.play_video, 28);
        sViewsWithIds.put(R.id.end_layout, 29);
        sViewsWithIds.put(R.id.music_layout1, 30);
        sViewsWithIds.put(R.id.end_workout_layout, 31);
        sViewsWithIds.put(R.id.orientation_layout1, 32);
        sViewsWithIds.put(R.id.pause_workout_layout1, 33);
        sViewsWithIds.put(R.id.totaltime1, 34);
        sViewsWithIds.put(R.id.EName1, 35);
        sViewsWithIds.put(R.id.play_video1, 36);
        sViewsWithIds.put(R.id.end_layout1, 37);
        sViewsWithIds.put(R.id.music_layout11, 38);
        sViewsWithIds.put(R.id.end_workout_layout1, 39);
        sViewsWithIds.put(R.id.landscape_orientation_layout, 40);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityStreamVideoPlayBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 41, sIncludes, sViewsWithIds));
    }
    private ActivityStreamVideoPlayBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[27]
            , (android.widget.TextView) bindings[35]
            , (com.iambedant.text.OutlineTextView) bindings[10]
            , (android.widget.LinearLayout) bindings[29]
            , (android.widget.LinearLayout) bindings[37]
            , (android.widget.LinearLayout) bindings[31]
            , (android.widget.LinearLayout) bindings[39]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[14]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.LinearLayout) bindings[40]
            , (android.widget.LinearLayout) bindings[17]
            , (android.widget.LinearLayout) bindings[30]
            , (android.widget.LinearLayout) bindings[38]
            , (android.widget.ImageView) bindings[22]
            , (android.widget.LinearLayout) bindings[21]
            , (android.widget.ImageView) bindings[24]
            , (android.widget.LinearLayout) bindings[23]
            , (android.widget.LinearLayout) bindings[32]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.RelativeLayout) bindings[25]
            , (android.widget.RelativeLayout) bindings[33]
            , (android.widget.LinearLayout) bindings[16]
            , (android.widget.ImageView) bindings[28]
            , (android.widget.ImageView) bindings[36]
            , (com.google.android.exoplayer2.ui.PlayerView) bindings[2]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[19]
            , (android.widget.LinearLayout) bindings[18]
            , (android.widget.TextView) bindings[11]
            , (android.widget.RelativeLayout) bindings[7]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[8]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[26]
            , (android.widget.TextView) bindings[34]
            , (androidx.recyclerview.widget.RecyclerView) bindings[12]
            , (com.doviesfitness.utils.MutedVideoView) bindings[1]
            , (android.view.View) bindings[4]
            , (android.view.View) bindings[5]
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