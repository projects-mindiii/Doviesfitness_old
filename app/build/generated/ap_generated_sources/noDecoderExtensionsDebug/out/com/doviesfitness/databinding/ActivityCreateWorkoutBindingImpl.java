package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCreateWorkoutBindingImpl extends ActivityCreateWorkoutBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.hide_show_view, 1);
        sViewsWithIds.put(R.id.sv_main, 2);
        sViewsWithIds.put(R.id.img_layout, 3);
        sViewsWithIds.put(R.id.image, 4);
        sViewsWithIds.put(R.id.image1, 5);
        sViewsWithIds.put(R.id.img_txt, 6);
        sViewsWithIds.put(R.id.workout_name, 7);
        sViewsWithIds.put(R.id.level_layout, 8);
        sViewsWithIds.put(R.id.ll_good, 9);
        sViewsWithIds.put(R.id.good_level, 10);
        sViewsWithIds.put(R.id.level_name, 11);
        sViewsWithIds.put(R.id.iv_forword, 12);
        sViewsWithIds.put(R.id.overview, 13);
        sViewsWithIds.put(R.id.good_for_layout, 14);
        sViewsWithIds.put(R.id.ll_goods, 15);
        sViewsWithIds.put(R.id.txt_good_for, 16);
        sViewsWithIds.put(R.id.good_for, 17);
        sViewsWithIds.put(R.id.iv_goods, 18);
        sViewsWithIds.put(R.id.equipment_layout, 19);
        sViewsWithIds.put(R.id.ll_equipment, 20);
        sViewsWithIds.put(R.id.equipmemnts, 21);
        sViewsWithIds.put(R.id.tv_equipment, 22);
        sViewsWithIds.put(R.id.iv_equipments, 23);
        sViewsWithIds.put(R.id.admin_layout, 24);
        sViewsWithIds.put(R.id.created_by_layout, 25);
        sViewsWithIds.put(R.id.create_by, 26);
        sViewsWithIds.put(R.id.allowed_users_layout, 27);
        sViewsWithIds.put(R.id.ll_allow, 28);
        sViewsWithIds.put(R.id.txt_allowed, 29);
        sViewsWithIds.put(R.id.allowed_users_txt, 30);
        sViewsWithIds.put(R.id.iv_allow, 31);
        sViewsWithIds.put(R.id.access_level_layout, 32);
        sViewsWithIds.put(R.id.access_level, 33);
        sViewsWithIds.put(R.id.display_newsfeed_layout, 34);
        sViewsWithIds.put(R.id.display_newsfeed, 35);
        sViewsWithIds.put(R.id.allow_notification_layout, 36);
        sViewsWithIds.put(R.id.allow_notification, 37);
        sViewsWithIds.put(R.id.show_hide_view, 38);
        sViewsWithIds.put(R.id.show_hide_layout, 39);
        sViewsWithIds.put(R.id.timer_exercise_layout, 40);
        sViewsWithIds.put(R.id.time_exercise_txt, 41);
        sViewsWithIds.put(R.id.arrow_icon2, 42);
        sViewsWithIds.put(R.id.repetetion_layout, 43);
        sViewsWithIds.put(R.id.repetetion_txt, 44);
        sViewsWithIds.put(R.id.arrow_icon1, 45);
        sViewsWithIds.put(R.id.reps_timer_layout, 46);
        sViewsWithIds.put(R.id.reps_timer_txt, 47);
        sViewsWithIds.put(R.id.arrow_icon, 48);
        sViewsWithIds.put(R.id.show_hide_view_Sets_reps, 49);
        sViewsWithIds.put(R.id.show_hide_layout1, 50);
        sViewsWithIds.put(R.id.timer_exercise_layout_Sets_reps, 51);
        sViewsWithIds.put(R.id.time_exercise_txt1, 52);
        sViewsWithIds.put(R.id.arrow_icon_Sets_reps, 53);
        sViewsWithIds.put(R.id.repetetion_layout_Sets_reps, 54);
        sViewsWithIds.put(R.id.repetetion_txt1, 55);
        sViewsWithIds.put(R.id.arrow_icon_Sets_reps1, 56);
        sViewsWithIds.put(R.id.reps_timer_layout_Sets_reps, 57);
        sViewsWithIds.put(R.id.reps_timer_txt1, 58);
        sViewsWithIds.put(R.id.arrow_icon_Sets_reps2, 59);
        sViewsWithIds.put(R.id.ll_FollowAlong, 60);
        sViewsWithIds.put(R.id.follow_along_icon, 61);
        sViewsWithIds.put(R.id.tv_title_along, 62);
        sViewsWithIds.put(R.id.ll_sets_and_reps, 63);
        sViewsWithIds.put(R.id.select_sets_reps_icon, 64);
        sViewsWithIds.put(R.id.tv_title_sets, 65);
        sViewsWithIds.put(R.id.seperator_line, 66);
        sViewsWithIds.put(R.id.exercise_rv, 67);
        sViewsWithIds.put(R.id.ll_addExerciseView, 68);
        sViewsWithIds.put(R.id.llInfo, 69);
        sViewsWithIds.put(R.id.info_icon, 70);
        sViewsWithIds.put(R.id.vertical_divider_0, 71);
        sViewsWithIds.put(R.id.add_exercise_btn, 72);
        sViewsWithIds.put(R.id.vertical_divider, 73);
        sViewsWithIds.put(R.id.ll_addNote, 74);
        sViewsWithIds.put(R.id.total_exercise_time, 75);
        sViewsWithIds.put(R.id.tv_add_note_total_time, 76);
        sViewsWithIds.put(R.id.ll_delete_duplicate, 77);
        sViewsWithIds.put(R.id.duplicate_icon, 78);
        sViewsWithIds.put(R.id.delete_icon_create_exercise, 79);
        sViewsWithIds.put(R.id.view_trans_parancy, 80);
        sViewsWithIds.put(R.id.progress_layout, 81);
        sViewsWithIds.put(R.id.loader, 82);
        sViewsWithIds.put(R.id.topBlurView1, 83);
        sViewsWithIds.put(R.id.header_layout, 84);
        sViewsWithIds.put(R.id.cancel_button, 85);
        sViewsWithIds.put(R.id.done_btn, 86);
        sViewsWithIds.put(R.id.view, 87);
        sViewsWithIds.put(R.id.ll_mark_as_ungroup_delete, 88);
        sViewsWithIds.put(R.id.tv_mark_as, 89);
        sViewsWithIds.put(R.id.tv_ungroup, 90);
        sViewsWithIds.put(R.id.tv_delete_from_rounds, 91);
        sViewsWithIds.put(R.id.ll_duplicate_delete, 92);
        sViewsWithIds.put(R.id.llDuplicate, 93);
        sViewsWithIds.put(R.id.ic_duplicate, 94);
        sViewsWithIds.put(R.id.tv_duplicate, 95);
        sViewsWithIds.put(R.id.llDelete, 96);
        sViewsWithIds.put(R.id.iv_icon, 97);
        sViewsWithIds.put(R.id.tv_delete, 98);
        sViewsWithIds.put(R.id.ll_selectAll, 99);
        sViewsWithIds.put(R.id.header_layout_select_deselect, 100);
        sViewsWithIds.put(R.id.ll_icon_all, 101);
        sViewsWithIds.put(R.id.select_select_deselect_all, 102);
        sViewsWithIds.put(R.id.cancel_button_select_deselect, 103);
        sViewsWithIds.put(R.id.Done_create_workoutt, 104);
        sViewsWithIds.put(R.id.selected_count, 105);
        sViewsWithIds.put(R.id.view_select_deselect, 106);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCreateWorkoutBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 107, sIncludes, sViewsWithIds));
    }
    private ActivityCreateWorkoutBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[104]
            , (android.widget.TextView) bindings[33]
            , (android.widget.RelativeLayout) bindings[32]
            , (android.widget.LinearLayout) bindings[72]
            , (android.widget.LinearLayout) bindings[24]
            , (android.widget.TextView) bindings[37]
            , (android.widget.RelativeLayout) bindings[36]
            , (android.widget.RelativeLayout) bindings[27]
            , (android.widget.TextView) bindings[30]
            , (android.widget.ImageView) bindings[48]
            , (android.widget.ImageView) bindings[45]
            , (android.widget.ImageView) bindings[42]
            , (android.widget.ImageView) bindings[53]
            , (android.widget.ImageView) bindings[56]
            , (android.widget.ImageView) bindings[59]
            , (android.widget.TextView) bindings[85]
            , (android.widget.TextView) bindings[103]
            , (android.widget.RelativeLayout) bindings[0]
            , (android.widget.TextView) bindings[26]
            , (android.widget.RelativeLayout) bindings[25]
            , (android.widget.ImageView) bindings[79]
            , (android.widget.TextView) bindings[35]
            , (android.widget.RelativeLayout) bindings[34]
            , (android.widget.TextView) bindings[86]
            , (android.widget.ImageView) bindings[78]
            , (android.widget.TextView) bindings[21]
            , (android.widget.RelativeLayout) bindings[19]
            , (androidx.recyclerview.widget.RecyclerView) bindings[67]
            , (android.widget.ImageView) bindings[61]
            , (android.widget.TextView) bindings[17]
            , (android.widget.RelativeLayout) bindings[14]
            , (android.widget.TextView) bindings[10]
            , (android.widget.RelativeLayout) bindings[84]
            , (android.widget.RelativeLayout) bindings[100]
            , (android.view.View) bindings[1]
            , (android.widget.ImageView) bindings[94]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ImageView) bindings[70]
            , (android.widget.ImageView) bindings[31]
            , (android.widget.ImageView) bindings[23]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.ImageView) bindings[97]
            , (android.widget.RelativeLayout) bindings[8]
            , (android.widget.TextView) bindings[11]
            , (android.widget.RelativeLayout) bindings[68]
            , (android.widget.LinearLayout) bindings[74]
            , (android.widget.LinearLayout) bindings[28]
            , (android.widget.RelativeLayout) bindings[96]
            , (android.widget.LinearLayout) bindings[77]
            , (android.widget.RelativeLayout) bindings[93]
            , (android.widget.LinearLayout) bindings[92]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.LinearLayout) bindings[60]
            , (android.widget.LinearLayout) bindings[9]
            , (android.widget.LinearLayout) bindings[15]
            , (android.widget.LinearLayout) bindings[101]
            , (android.widget.LinearLayout) bindings[69]
            , (android.widget.LinearLayout) bindings[88]
            , (eightbitlab.com.blurview.BlurView) bindings[99]
            , (android.widget.LinearLayout) bindings[63]
            , (android.widget.ProgressBar) bindings[82]
            , (android.widget.EditText) bindings[13]
            , (android.widget.RelativeLayout) bindings[81]
            , (android.widget.RelativeLayout) bindings[43]
            , (android.widget.RelativeLayout) bindings[54]
            , (android.widget.TextView) bindings[44]
            , (android.widget.TextView) bindings[55]
            , (android.widget.RelativeLayout) bindings[46]
            , (android.widget.RelativeLayout) bindings[57]
            , (android.widget.TextView) bindings[47]
            , (android.widget.TextView) bindings[58]
            , (android.widget.ImageView) bindings[102]
            , (android.widget.ImageView) bindings[64]
            , (android.widget.TextView) bindings[105]
            , (android.view.View) bindings[66]
            , (android.widget.LinearLayout) bindings[39]
            , (android.widget.LinearLayout) bindings[50]
            , (android.widget.ImageView) bindings[38]
            , (android.widget.ImageView) bindings[49]
            , (com.doviesfitness.utils.CustomNestedScrollViewForWorkout) bindings[2]
            , (android.widget.TextView) bindings[41]
            , (android.widget.TextView) bindings[52]
            , (android.widget.RelativeLayout) bindings[40]
            , (android.widget.RelativeLayout) bindings[51]
            , (eightbitlab.com.blurview.BlurView) bindings[83]
            , (android.widget.ImageView) bindings[75]
            , (android.widget.TextView) bindings[76]
            , (android.widget.TextView) bindings[98]
            , (android.widget.TextView) bindings[91]
            , (android.widget.TextView) bindings[95]
            , (android.widget.TextView) bindings[22]
            , (android.widget.TextView) bindings[89]
            , (android.widget.TextView) bindings[62]
            , (android.widget.TextView) bindings[65]
            , (android.widget.TextView) bindings[90]
            , (android.widget.TextView) bindings[29]
            , (android.widget.TextView) bindings[16]
            , (android.view.View) bindings[73]
            , (android.view.View) bindings[71]
            , (android.view.View) bindings[87]
            , (android.view.View) bindings[106]
            , (android.view.View) bindings[80]
            , (android.widget.EditText) bindings[7]
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