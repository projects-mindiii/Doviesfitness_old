package com.doviesfitness.ui.multipleQuality;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.doviesfitness.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.IntArrayQueue;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackSelectionView extends PopupWindow {
    public TrackSelectionView(Context context) {
        super(context);
        tabFragments = new SparseArray<>();
        tabTrackTypes = new ArrayList<>();
        // Retain instance across activity re-creation to prevent losing access to init data.
     //   setRetainInstance(true);

    }

    public TrackSelectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tabFragments = new SparseArray<>();
        tabTrackTypes = new ArrayList<>();
    }

    public TrackSelectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tabFragments = new SparseArray<>();
        tabTrackTypes = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TrackSelectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        tabFragments = new SparseArray<>();
        tabTrackTypes = new ArrayList<>();
    }
///////
private final SparseArray<TrackSelectionViewFragment> tabFragments;
    private final ArrayList<Integer> tabTrackTypes;

    private int titleId;
    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnDismissListener onDismissListener;

    /**
     * Returns whether a track selection dialog will have content to display if initialized with the
     * specified {@link DefaultTrackSelector} in its current state.
     */
    public static boolean willHaveContent(DefaultTrackSelector trackSelector) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        return mappedTrackInfo != null && willHaveContent(mappedTrackInfo);
    }

    /**
     * Returns whether a track selection dialog will have content to display if initialized with the
     * specified {@link MappingTrackSelector.MappedTrackInfo}.
     */
    public static boolean willHaveContent(MappingTrackSelector.MappedTrackInfo mappedTrackInfo) {
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            if (showTabForRenderer(mappedTrackInfo, i)) {
                return true;
            }
        }
        return false;
    }

    private void init(
            int titleId,
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo,
            DefaultTrackSelector.Parameters initialParameters,
            boolean allowAdaptiveSelections,
            boolean allowMultipleOverrides,
            DialogInterface.OnClickListener onClickListener,
            DialogInterface.OnDismissListener onDismissListener) {
        this.titleId = titleId;
        this.onClickListener = onClickListener;
        this.onDismissListener = onDismissListener;
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            if (showTabForRenderer(mappedTrackInfo, i)) {
                int trackType = mappedTrackInfo.getRendererType(/* rendererIndex= */ i);
                TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(i);
                TrackSelectionViewFragment tabFragment = new TrackSelectionViewFragment();
                tabFragment.init(
                        mappedTrackInfo,
                        /* rendererIndex= */ i,
                        initialParameters.getRendererDisabled(/* rendererIndex= */ i),
                        initialParameters.getSelectionOverride(/* rendererIndex= */ i, trackGroupArray),
                        allowAdaptiveSelections,
                        allowMultipleOverrides);
                tabFragments.put(i, tabFragment);
                tabTrackTypes.add(trackType);
            }
        }
    }


   static public TrackSelectionView createForTrackSelector(
            Context context, DefaultTrackSelector trackSelector, DialogInterface.OnDismissListener onDismissListener) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo =
                Assertions.checkNotNull(trackSelector.getCurrentMappedTrackInfo());
        TrackSelectionView trackSelectionDialog = new TrackSelectionView(context);
        DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();
       trackSelectionDialog.init(
                /* titleId= */ R.string.track_selection_title,
                mappedTrackInfo,
                /* initialParameters = */ parameters,
                /* allowAdaptiveSelections =*/ true,
                /* allowMultipleOverrides= */ false,
                /* onClickListener= */ (dialog, which) -> {
                    DefaultTrackSelector.ParametersBuilder builder = parameters.buildUpon();
                    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                        builder
                                .clearSelectionOverrides(/* rendererIndex= */ i)
                                .setRendererDisabled(
                                        /* rendererIndex= */ i,
                                        trackSelectionDialog.getIsDisabled(/* rendererIndex= */ i));
                        List<DefaultTrackSelector.SelectionOverride> overrides =
                                trackSelectionDialog.getOverrides(/* rendererIndex= */ i);
                        if (!overrides.isEmpty()) {
                            builder.setSelectionOverride(
                                    /* rendererIndex= */ i,
                                    mappedTrackInfo.getTrackGroups(/* rendererIndex= */ i),
                                    overrides.get(0));
                        }
                    }
                    trackSelector.setParameters(builder);
                },
                onDismissListener);
   /* Window window = trackSelectionDialog.getDialog().getWindow();
    WindowManager.LayoutParams wlp = window.getAttributes();

    wlp.gravity = Gravity.BOTTOM;
    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    window.setAttributes(wlp);
*/
        return trackSelectionDialog;
    }
    public boolean getIsDisabled(int rendererIndex) {
        TrackSelectionViewFragment rendererView = tabFragments.get(rendererIndex);
        return rendererView != null && rendererView.isDisabled;
    }

    /**
     * Returns the list of selected track selection overrides for the specified renderer. There will
     * be at most one override for each track group.
     *
     * @param rendererIndex Renderer index.
     * @return The list of track selection overrides for this renderer.
     */
    public List<DefaultTrackSelector.SelectionOverride> getOverrides(int rendererIndex) {
        TrackSelectionViewFragment rendererView = tabFragments.get(rendererIndex);
        return rendererView == null ? Collections.emptyList() : rendererView.overrides;
    }

    private static boolean showTabForRenderer(MappingTrackSelector.MappedTrackInfo mappedTrackInfo, int rendererIndex) {
        TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex);
        if (trackGroupArray.length == 0) {
            return false;
        }
        int trackType = mappedTrackInfo.getRendererType(rendererIndex);
        return isSupportedTrackType(trackType);
    }

    private static boolean isSupportedTrackType(int trackType) {
        switch (trackType) {
            case C.TRACK_TYPE_VIDEO:
            case C.TRACK_TYPE_AUDIO:
            case C.TRACK_TYPE_TEXT:
                return true;
            default:
                return false;
        }
    }

    private static String getTrackTypeString(Resources resources, int trackType) {
        switch (trackType) {
            case C.TRACK_TYPE_VIDEO:
                return resources.getString(R.string.exo_track_selection_title_video);
            case C.TRACK_TYPE_AUDIO:
                return resources.getString(R.string.exo_track_selection_title_audio);
            case C.TRACK_TYPE_TEXT:
                return resources.getString(R.string.exo_track_selection_title_text);
            default:
                throw new IllegalArgumentException();
        }
    }



    private final class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        //shivangi
        @Override
        @NonNull
        public Fragment getItem(int position) {
            //   return tabFragments.valueAt(position);
            return tabFragments.valueAt(0);
        }

        @Override
        public int getCount() {
            // return tabFragments.size();
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Quality";
        }
    }

    /** Fragment to show a track selection in tab of the track selection dialog. */
    public final class TrackSelectionViewFragment extends Fragment
            implements com.google.android.exoplayer2.ui.TrackSelectionView.TrackSelectionListener {

        private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;
        private int rendererIndex;
        private boolean allowAdaptiveSelections;
        private boolean allowMultipleOverrides;

        /* package */ boolean isDisabled;
        /* package */ List<DefaultTrackSelector.SelectionOverride> overrides;

        public TrackSelectionViewFragment() {
            // Retain instance across activity re-creation to prevent losing access to init data.
            setRetainInstance(true);
        }

        public void init(
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo,
                int rendererIndex,
                boolean initialIsDisabled,
                @Nullable DefaultTrackSelector.SelectionOverride initialOverride,
                boolean allowAdaptiveSelections,
                boolean allowMultipleOverrides) {
            this.mappedTrackInfo = mappedTrackInfo;
            this.rendererIndex = rendererIndex;
            this.isDisabled = initialIsDisabled;
            this.overrides =
                    initialOverride == null
                            ? Collections.emptyList()
                            : Collections.singletonList(initialOverride);
            this.allowAdaptiveSelections = allowAdaptiveSelections;
            this.allowMultipleOverrides = allowMultipleOverrides;
        }


/*
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.exo_track_selection_dialog, container, false);
            com.google.android.exoplayer2.ui.TrackSelectionView trackSelectionView = rootView.findViewById(R.id.exo_track_selection_view);
            trackSelectionView.setShowDisableOption(true);
            trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides);
            trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections);
            trackSelectionView.init(
                    mappedTrackInfo, rendererIndex, isDisabled, overrides,
                    this);
            return rootView;
        }
*/

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View dialogView = inflater.inflate(R.layout.track_selection_dialog, container, false);
            TabLayout tabLayout = dialogView.findViewById(R.id.track_selection_dialog_tab_layout);
            ViewPager viewPager = dialogView.findViewById(R.id.track_selection_dialog_view_pager);
            Button cancelButton = dialogView.findViewById(R.id.track_selection_dialog_cancel_button);
            Button okButton = dialogView.findViewById(R.id.track_selection_dialog_ok_button);
         //   viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
            viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setVisibility(tabFragments.size() > 1 ? View.VISIBLE : View.GONE);
            cancelButton.setOnClickListener(view -> dismiss());
/*
            okButton.setOnClickListener(
                    view -> {
                        onClickListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
                        dismiss();
                    });
*/
            return dialogView;
        }


        @Override
        public void onTrackSelectionChanged(
                boolean isDisabled, @NonNull List<DefaultTrackSelector.SelectionOverride> overrides) {
            this.isDisabled = isDisabled;
            this.overrides = overrides;
        }

    }
}
