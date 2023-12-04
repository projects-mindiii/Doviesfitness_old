package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityWorkOutDetailBindingImpl extends ActivityWorkOutDetailBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.sv_main, 4);
        sViewsWithIds.put(R.id.rl_layout, 5);
        sViewsWithIds.put(R.id.rl_header, 6);
        sViewsWithIds.put(R.id.iv_featred, 7);
        sViewsWithIds.put(R.id.my_progress, 8);
        sViewsWithIds.put(R.id.progress_layout, 9);
        sViewsWithIds.put(R.id.loader, 10);
        sViewsWithIds.put(R.id.play_video, 11);
        sViewsWithIds.put(R.id.downloading_txt, 12);
        sViewsWithIds.put(R.id.ll_workout_info, 13);
        sViewsWithIds.put(R.id.tv_minutes, 14);
        sViewsWithIds.put(R.id.iv_exercise_level, 15);
        sViewsWithIds.put(R.id.tv_workout_level, 16);
        sViewsWithIds.put(R.id.iv_workou_type, 17);
        sViewsWithIds.put(R.id.tv_workoutType, 18);
        sViewsWithIds.put(R.id.tv_gdfr, 19);
        sViewsWithIds.put(R.id.tv_title_eqpmbt, 20);
        sViewsWithIds.put(R.id.llOverView, 21);
        sViewsWithIds.put(R.id.tv_title_overView, 22);
        sViewsWithIds.put(R.id.tv_overview, 23);
        sViewsWithIds.put(R.id.iv_profile_img, 24);
        sViewsWithIds.put(R.id.ll_workout_created, 25);
        sViewsWithIds.put(R.id.iv_workout_creator_image, 26);
        sViewsWithIds.put(R.id.tv_created, 27);
        sViewsWithIds.put(R.id.tv_creator_name, 28);
        sViewsWithIds.put(R.id.tv_workout_creator_type, 29);
        sViewsWithIds.put(R.id.iv_social_menu_link, 30);
        sViewsWithIds.put(R.id.transparentBlurView, 31);
        sViewsWithIds.put(R.id.transparent_layout, 32);
        sViewsWithIds.put(R.id.rv_Media_links, 33);
        sViewsWithIds.put(R.id.tv_total_exercise_count, 34);
        sViewsWithIds.put(R.id.rv_workout, 35);
        sViewsWithIds.put(R.id.iv_back, 36);
        sViewsWithIds.put(R.id.iv_fav, 37);
        sViewsWithIds.put(R.id.iv_more, 38);
        sViewsWithIds.put(R.id.music_layout, 39);
        sViewsWithIds.put(R.id.music_layout1, 40);
        sViewsWithIds.put(R.id.sharing_layout, 41);
        sViewsWithIds.put(R.id.overView_layout, 42);
        sViewsWithIds.put(R.id.bottom_Center_button, 43);
        sViewsWithIds.put(R.id.music_player_layout, 44);
        sViewsWithIds.put(R.id.upgrade_layout, 45);
        sViewsWithIds.put(R.id.rl_header1, 46);
        sViewsWithIds.put(R.id.img, 47);
        sViewsWithIds.put(R.id.opacity_layout, 48);
        sViewsWithIds.put(R.id.countDownTxt, 49);
        sViewsWithIds.put(R.id.view_trans_parancy, 50);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityWorkOutDetailBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 51, sIncludes, sViewsWithIds));
    }
    private ActivityWorkOutDetailBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[43]
            , (android.widget.TextView) bindings[49]
            , (android.widget.RelativeLayout) bindings[12]
            , (android.widget.ImageView) bindings[47]
            , (android.widget.ImageView) bindings[36]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[37]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[38]
            , (android.widget.ImageView) bindings[24]
            , (android.widget.ImageView) bindings[30]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[26]
            , (android.widget.RelativeLayout) bindings[21]
            , (android.widget.RelativeLayout) bindings[25]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.ProgressBar) bindings[10]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[39]
            , (android.widget.LinearLayout) bindings[40]
            , (android.widget.LinearLayout) bindings[44]
            , (android.widget.ProgressBar) bindings[8]
            , (android.widget.RelativeLayout) bindings[48]
            , (android.widget.LinearLayout) bindings[42]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.RelativeLayout) bindings[9]
            , (android.widget.RelativeLayout) bindings[6]
            , (android.widget.RelativeLayout) bindings[46]
            , (android.widget.RelativeLayout) bindings[5]
            , (androidx.recyclerview.widget.RecyclerView) bindings[33]
            , (androidx.recyclerview.widget.RecyclerView) bindings[35]
            , (android.widget.LinearLayout) bindings[41]
            , (com.doviesfitness.utils.CustomNestedScrollView) bindings[4]
            , (eightbitlab.com.blurview.BlurView) bindings[31]
            , (android.widget.RelativeLayout) bindings[32]
            , (android.widget.TextView) bindings[27]
            , (android.widget.TextView) bindings[28]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[19]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[20]
            , (android.widget.TextView) bindings[22]
            , (android.widget.TextView) bindings[34]
            , (android.widget.TextView) bindings[29]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[18]
            , (android.widget.LinearLayout) bindings[45]
            , (android.view.View) bindings[50]
            );
        this.mainLayout.setTag(null);
        this.tvEquipment.setTag(null);
        this.tvGoodfor.setTag(null);
        this.tvWorkoutName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.workout == variableId) {
            setWorkout((com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setWorkout(@Nullable com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail Workout) {
        this.mWorkout = Workout;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.workout);
        super.requestRebind();
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
        java.lang.String workoutWorkoutName = null;
        java.lang.String workoutWorkoutEquipmentReplaceJavaLangStringJavaLangString = null;
        java.lang.String workoutWorkoutGoodForReplaceJavaLangStringJavaLangString = null;
        com.doviesfitness.ui.bottom_tabbar.home_tab.model.WorkoutDetail workout = mWorkout;
        java.lang.String workoutWorkoutEquipment = null;
        java.lang.String workoutWorkoutGoodFor = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (workout != null) {
                    // read workout.workout_name
                    workoutWorkoutName = workout.getWorkout_name();
                    // read workout.workout_equipment
                    workoutWorkoutEquipment = workout.getWorkout_equipment();
                    // read workout.workout_good_for
                    workoutWorkoutGoodFor = workout.getWorkout_good_for();
                }


                if (workoutWorkoutEquipment != null) {
                    // read workout.workout_equipment.replace(" |", ",")
                    workoutWorkoutEquipmentReplaceJavaLangStringJavaLangString = workoutWorkoutEquipment.replace(" |", ",");
                }
                if (workoutWorkoutGoodFor != null) {
                    // read workout.workout_good_for.replace(" |", ",")
                    workoutWorkoutGoodForReplaceJavaLangStringJavaLangString = workoutWorkoutGoodFor.replace(" |", ",");
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvEquipment, workoutWorkoutEquipmentReplaceJavaLangStringJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvGoodfor, workoutWorkoutGoodForReplaceJavaLangStringJavaLangString);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvWorkoutName, workoutWorkoutName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): workout
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}