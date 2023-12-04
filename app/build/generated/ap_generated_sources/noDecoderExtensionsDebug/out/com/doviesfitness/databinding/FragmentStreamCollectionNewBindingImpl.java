package com.doviesfitness.databinding;
import com.doviesfitness.R;
import com.doviesfitness.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentStreamCollectionNewBindingImpl extends FragmentStreamCollectionNewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.containerId, 1);
        sViewsWithIds.put(R.id.swipe_refresh, 2);
        sViewsWithIds.put(R.id.nested_sv, 3);
        sViewsWithIds.put(R.id.rl_pinned, 4);
        sViewsWithIds.put(R.id.iv_pined, 5);
        sViewsWithIds.put(R.id.workout_name, 6);
        sViewsWithIds.put(R.id.workout_description, 7);
        sViewsWithIds.put(R.id.play_video_layout, 8);
        sViewsWithIds.put(R.id.iv_share, 9);
        sViewsWithIds.put(R.id.share_txt, 10);
        sViewsWithIds.put(R.id.play_video, 11);
        sViewsWithIds.put(R.id.iv_overview, 12);
        sViewsWithIds.put(R.id.overview_txt, 13);
        sViewsWithIds.put(R.id.stream_list_layout, 14);
        sViewsWithIds.put(R.id.top_hidden_view, 15);
        sViewsWithIds.put(R.id.tv_mostRecent, 16);
        sViewsWithIds.put(R.id.next_icon1, 17);
        sViewsWithIds.put(R.id.rl_recent, 18);
        sViewsWithIds.put(R.id.rv_recent, 19);
        sViewsWithIds.put(R.id.first_collection_name, 20);
        sViewsWithIds.put(R.id.next_icon2, 21);
        sViewsWithIds.put(R.id.level_name, 22);
        sViewsWithIds.put(R.id.rl_featured, 23);
        sViewsWithIds.put(R.id.rv_featured, 24);
        sViewsWithIds.put(R.id.right_arrow, 25);
        sViewsWithIds.put(R.id.txt_all_workout_collection, 26);
        sViewsWithIds.put(R.id.rv_workout, 27);
        sViewsWithIds.put(R.id.action_bar_header, 28);
        sViewsWithIds.put(R.id.action_bar1, 29);
        sViewsWithIds.put(R.id.log_icon, 30);
        sViewsWithIds.put(R.id.favorite, 31);
        sViewsWithIds.put(R.id.chrome_cast, 32);
        sViewsWithIds.put(R.id.download, 33);
        sViewsWithIds.put(R.id.iv_search, 34);
        sViewsWithIds.put(R.id.iv_fav, 35);
        sViewsWithIds.put(R.id.iv_download, 36);
        sViewsWithIds.put(R.id.txt_no_data_found, 37);
        sViewsWithIds.put(R.id.transparentBlurView, 38);
        sViewsWithIds.put(R.id.transparent_layout, 39);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentStreamCollectionNewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 40, sIncludes, sViewsWithIds));
    }
    private FragmentStreamCollectionNewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[29]
            , (android.widget.RelativeLayout) bindings[28]
            , (android.widget.ImageView) bindings[32]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[33]
            , (android.widget.TextView) bindings[31]
            , (android.widget.TextView) bindings[20]
            , (android.widget.ImageView) bindings[36]
            , (android.widget.ImageView) bindings[35]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[34]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.TextView) bindings[22]
            , (android.widget.ImageView) bindings[30]
            , (androidx.core.widget.NestedScrollView) bindings[3]
            , (android.widget.ImageView) bindings[17]
            , (android.widget.ImageView) bindings[21]
            , (android.widget.TextView) bindings[13]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.RelativeLayout) bindings[8]
            , (androidx.appcompat.widget.AppCompatImageView) bindings[25]
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.RelativeLayout) bindings[4]
            , (android.widget.RelativeLayout) bindings[18]
            , (androidx.recyclerview.widget.RecyclerView) bindings[24]
            , (androidx.recyclerview.widget.RecyclerView) bindings[19]
            , (androidx.recyclerview.widget.RecyclerView) bindings[27]
            , (android.widget.TextView) bindings[10]
            , (android.widget.LinearLayout) bindings[14]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[2]
            , (android.view.View) bindings[15]
            , (eightbitlab.com.blurview.BlurView) bindings[38]
            , (android.widget.RelativeLayout) bindings[39]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[26]
            , (android.widget.TextView) bindings[37]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[6]
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