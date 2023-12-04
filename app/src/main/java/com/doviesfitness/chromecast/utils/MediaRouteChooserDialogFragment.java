package com.doviesfitness.chromecast.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.mediarouter.app.MediaRouteChooserDialog;
import androidx.mediarouter.media.MediaRouteSelector;

public class MediaRouteChooserDialogFragment extends DialogFragment {
    private final String ARGUMENT_SELECTOR = "selector";
    private MediaRouteSelector mSelector;
    /**
     * Creates a media route chooser dialog fragment.
     * <p>
     * All subclasses of this class must also possess a default constructor.
     * </p>
     */
    public MediaRouteChooserDialogFragment() {
        setCancelable(true);
    }
    /**
     * Gets the media route selector for filtering the routes that the user can select.
     *
     * @return The selector, never null.
     */
    public MediaRouteSelector getRouteSelector() {
        ensureRouteSelector();
        return mSelector;
    }
    private void ensureRouteSelector() {
        if (mSelector == null) {
            Bundle args = getArguments();
            if (args != null) {
                mSelector = MediaRouteSelector.fromBundle(args.getBundle(ARGUMENT_SELECTOR));
            }
            if (mSelector == null) {
                mSelector = MediaRouteSelector.EMPTY;
            }
        }
    }
    /**
     * Sets the media route selector for filtering the routes that the user can select.
     * This method must be called before the fragment is added.
     *
     * @param selector The selector to set.
     */
    public void setRouteSelector(MediaRouteSelector selector) {
        if (selector == null) {
            throw new IllegalArgumentException("selector must not be null");
        }
        ensureRouteSelector();
        if (!mSelector.equals(selector)) {
            mSelector = selector;
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putBundle(ARGUMENT_SELECTOR, selector.asBundle());
            setArguments(args);
            MediaRouteChooserDialog dialog = (MediaRouteChooserDialog)getDialog();
            if (dialog != null) {
                dialog.setRouteSelector(selector);
            }
        }
    }
    /**
     * Called when the chooser dialog is being created.
     * <p>
     * Subclasses may override this method to customize the dialog.
     * </p>
     */
    public MediaRouteChooserDialog onCreateChooserDialog(Context context, Bundle savedInstanceState) {
        return new MediaRouteChooserDialog(context);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MediaRouteChooserDialog dialog = onCreateChooserDialog(getActivity(), savedInstanceState);
        dialog.setRouteSelector(getRouteSelector());
        return dialog;
    }
}