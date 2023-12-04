package com.doviesfitness.chromecast.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.recyclerview.widget.RecyclerView;

import com.doviesfitness.R;
import com.google.android.exoplayer2.util.Log;

import java.util.Comparator;
import java.util.List;

public class MediaRouteChooserDialog extends Dialog {
    private final MediaRouter mRouter;
    private final MediaRouterCallback mCallback;
    private MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;
    private RouteAdapter mAdapter;
    private ListView mListView;
    private boolean mAttachedToWindow;



    public MediaRouteChooserDialog(Context context) {
        this(context, 0);
    }
    public MediaRouteChooserDialog(Context context, int theme) {
        super(MediaRouterThemeHelper.createThemedContext(context, true), theme);
        context = getContext();
        mRouter = MediaRouter.getInstance(context);
        mCallback = new MediaRouterCallback();
    }
    /**
     * Gets the media route selector for filtering the routes that the user can select.
     *
     * @return The selector, never null.
     */
    public MediaRouteSelector getRouteSelector() {
        return mSelector;
    }
    /**
     * Sets the media route selector for filtering the routes that the user can select.
     *
     * @param selector The selector, must not be null.
     */
    public void setRouteSelector(MediaRouteSelector selector) {
        if (selector == null) {
            throw new IllegalArgumentException("selector must not be null");
        }
        if (!mSelector.equals(selector)) {
            mSelector = selector;
            if (mAttachedToWindow) {
                mRouter.removeCallback(mCallback);
                mRouter.addCallback(selector, mCallback,
                        MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
            }
            refreshRoutes();
        }
    }
    /**
     * Returns true if the route should be included in the list.
     * <p>
     * The default implementation returns true for non-default routes that
     * match the selector.  Subclasses can override this method to filter routes
     * differently.
     * </p>
     *
     * @param route The route to consider, never null.
     * @return True if the route should be included in the chooser dialog.
     */
    public boolean onFilterRoute(MediaRouter.RouteInfo route) {
        return !route.isDefault() && route.matchesSelector(mSelector);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.chrome_cast_custom_dialog);
        setTitle("Dovies devices");
        // Must be called after setContentView.
/*
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                MediaRouterThemeHelper.getThemeResource(
                        getContext(), R.attr.mediaRouteOffDrawable));
*/
        mAdapter = new RouteAdapter(getContext());
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
      //  mListView.setEmptyView(findViewById(R.id.empty_view));
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mAttachedToWindow = true;
        mRouter.addCallback(mSelector, mCallback, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
        refreshRoutes();
    }
    @Override
    public void onDetachedFromWindow() {
        mAttachedToWindow = false;
        mRouter.removeCallback(mCallback);
        super.onDetachedFromWindow();
    }
    /**
     * Refreshes the list of routes that are shown in the chooser dialog.
     */
    public void refreshRoutes() {
        if (mAttachedToWindow) {
            mAdapter.update();
        }
    }
    private final class RouteAdapter extends ArrayAdapter<MediaRouter.RouteInfo>
            implements ListView.OnItemClickListener {
        private final LayoutInflater mInflater;
        public RouteAdapter(Context context) {
            super(context, 0);
            mInflater = LayoutInflater.from(context);
        }
        public void update() {
            clear();
            final List<MediaRouter.RouteInfo> routes = mRouter.getRoutes();
            final int count = routes.size();
            boolean isAdded=false;
            for (int i = 0; i < count; i++) {
                MediaRouter.RouteInfo route = routes.get(i);
                if (onFilterRoute(route)) {
                    add(route);
                    isAdded=true;
                }
            }
            sort(RouteComparator.sInstance);
            if (isAdded){
                //show in list
            }
            else {
                // show no device message
            }
            notifyDataSetChanged();
        }
        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }
        @Override
        public boolean isEnabled(int position) {
            return getItem(position).isEnabled();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.browse_row, parent, false);
            }
            MediaRouter.RouteInfo route = getItem(position);
            TextView text1 = (TextView)view.findViewById(R.id.textView1);
            TextView text2 = (TextView)view.findViewById(R.id.textView2);
            text1.setText(route.getName());
            String description = route.getDescription();
            if (TextUtils.isEmpty(description)) {
                text2.setVisibility(View.GONE);
                text2.setText("");
            } else {
                text2.setVisibility(View.VISIBLE);
                text2.setText(description);
            }
            view.setEnabled(route.isEnabled());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaRouter.RouteInfo route = getItem(position);

                    if (route.isEnabled()) {
                       // route.select();
                      //  mRouter.selectRoute(route);
                        mRouter.unselect(0);
                      //  dismiss();
                    }
                }
            });

            return view;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaRouter.RouteInfo route = getItem(position);
            if (route.isEnabled()) {
                route.select();
                dismiss();
            }
        }
    }


    private final class MediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo info) {
            refreshRoutes();
        }
        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo info) {
            refreshRoutes();
        }
        @Override
        public void onRouteChanged(MediaRouter router, MediaRouter.RouteInfo info) {
            refreshRoutes();
        }
        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
            Log.d("root selected","root selected");
            dismiss();
        }
    }

    private static final class RouteComparator implements Comparator<MediaRouter.RouteInfo> {
        public static final RouteComparator sInstance = new RouteComparator();
        @Override
        public int compare(MediaRouter.RouteInfo lhs, MediaRouter.RouteInfo rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }
}