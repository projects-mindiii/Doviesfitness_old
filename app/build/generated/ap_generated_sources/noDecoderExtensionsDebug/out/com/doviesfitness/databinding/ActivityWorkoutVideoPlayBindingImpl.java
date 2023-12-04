package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityWorkoutVideoPlayBindingImpl extends ActivityWorkoutVideoPlayBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.uper_view, 1);
        sViewsWithIds.put(R.id.video_view, 2);
        sViewsWithIds.put(R.id.restTime_layout, 3);
        sViewsWithIds.put(R.id.rest_tv, 4);
        sViewsWithIds.put(R.id.restTime_tv, 5);
        sViewsWithIds.put(R.id.rl_preview_layout, 6);
        sViewsWithIds.put(R.id.txt_next, 7);
        sViewsWithIds.put(R.id.boundry_frame, 8);
        sViewsWithIds.put(R.id.preview_video_view, 9);
        sViewsWithIds.put(R.id.rlTxtNext, 10);
        sViewsWithIds.put(R.id.preview_rest_tv, 11);
        sViewsWithIds.put(R.id.preview_restTime_tv, 12);
        sViewsWithIds.put(R.id.landscape_exercise_time_layout, 13);
        sViewsWithIds.put(R.id.landscape_exercise_time, 14);
        sViewsWithIds.put(R.id.landscape_exercise_time_spacer, 15);
        sViewsWithIds.put(R.id.shadow_layout, 16);
        sViewsWithIds.put(R.id.view, 17);
        sViewsWithIds.put(R.id.view1, 18);
        sViewsWithIds.put(R.id.countDownTime, 19);
        sViewsWithIds.put(R.id.repeat, 20);
        sViewsWithIds.put(R.id.video_Rv, 21);
        sViewsWithIds.put(R.id.play_controler_layout, 22);
        sViewsWithIds.put(R.id.music_layout, 23);
        sViewsWithIds.put(R.id.iv_music, 24);
        sViewsWithIds.put(R.id.previous_layout, 25);
        sViewsWithIds.put(R.id.previous_icon, 26);
        sViewsWithIds.put(R.id.pause_layout, 27);
        sViewsWithIds.put(R.id.next_layout, 28);
        sViewsWithIds.put(R.id.next_icon, 29);
        sViewsWithIds.put(R.id.orientation_layout, 30);
        sViewsWithIds.put(R.id.orientation_icon, 31);
        sViewsWithIds.put(R.id.pause_workout_layout, 32);
        sViewsWithIds.put(R.id.totaltime, 33);
        sViewsWithIds.put(R.id.EName, 34);
        sViewsWithIds.put(R.id.play_video, 35);
        sViewsWithIds.put(R.id.end_layout, 36);
        sViewsWithIds.put(R.id.music_layout1, 37);
        sViewsWithIds.put(R.id.end_workout_layout, 38);
        sViewsWithIds.put(R.id.orientation_layout1, 39);
        sViewsWithIds.put(R.id.pause_workout_layout1, 40);
        sViewsWithIds.put(R.id.totaltime1, 41);
        sViewsWithIds.put(R.id.EName1, 42);
        sViewsWithIds.put(R.id.play_video1, 43);
        sViewsWithIds.put(R.id.end_layout1, 44);
        sViewsWithIds.put(R.id.music_layout11, 45);
        sViewsWithIds.put(R.id.end_workout_layout1, 46);
        sViewsWithIds.put(R.id.landscape_orientation_layout, 47);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityWorkoutVideoPlayBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 48, sIncludes, sViewsWithIds));
    }
    private ActivityWorkoutVideoPlayBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[34]
            , (android.widget.TextView) bindings[42]
            , (android.widget.FrameLayout) bindings[8]
            , (android.widget.TextView) bindings[19]
            , (android.widget.LinearLayout) bindings[36]
            , (android.widget.LinearLayout) bindings[44]
            , (android.widget.LinearLayout) bindings[38]
            , (android.widget.LinearLayout) bindings[46]
            , (android.widget.ImageView) bindings[24]
            , (android.widget.TextView) bindings[14]
            , (android.widget.FrameLayout) bindings[13]
            , (android.widget.FrameLayout) bindings[15]
            , (android.widget.LinearLayout) bindings[47]
            , (android.widget.LinearLayout) bindings[23]
            , (android.widget.LinearLayout) bindings[37]
            , (android.widget.LinearLayout) bindings[45]
            , (android.widget.ImageView) bindings[29]
            , (android.widget.LinearLayout) bindings[28]
            , (android.widget.ImageView) bindings[31]
            , (android.widget.LinearLayout) bindings[30]
            , (android.widget.LinearLayout) bindings[39]
            , (android.widget.LinearLayout) bindings[27]
            , (android.widget.RelativeLayout) bindings[32]
            , (android.widget.RelativeLayout) bindings[40]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.ImageView) bindings[35]
            , (android.widget.ImageView) bindings[43]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[11]
            , (com.doviesfitness.utils.MutedVideoView) bindings[9]
            , (android.widget.ImageView) bindings[26]
            , (android.widget.LinearLayout) bindings[25]
            , (android.widget.TextView) bindings[20]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[4]
            , (android.widget.FrameLayout) bindings[6]
            , (android.widget.FrameLayout) bindings[10]
            , (android.widget.RelativeLayout) bindings[16]
            , (android.widget.TextView) bindings[33]
            , (android.widget.TextView) bindings[41]
            , (android.widget.TextView) bindings[7]
            , (android.widget.FrameLayout) bindings[1]
            , (androidx.recyclerview.widget.RecyclerView) bindings[21]
            , (com.doviesfitness.utils.MutedVideoView) bindings[2]
            , (android.view.View) bindings[17]
            , (android.view.View) bindings[18]
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