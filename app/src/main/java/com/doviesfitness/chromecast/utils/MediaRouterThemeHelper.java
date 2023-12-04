package com.doviesfitness.chromecast.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.appcompat.view.ContextThemeWrapper;

import com.doviesfitness.R;

public class MediaRouterThemeHelper {
    private MediaRouterThemeHelper() {
    }
    public static Context createThemedContext(Context context, boolean forceDark) {
        boolean isLightTheme = isLightTheme(context);
        if (isLightTheme && forceDark) {
            context = new ContextThemeWrapper(context, R.style.Theme_AppCompat);
            isLightTheme = false;
        }
        return new ContextThemeWrapper(context, isLightTheme ?
                R.style.Theme_MediaRouter_Light : R.style.Theme_MediaRouter);
    }
    public static int getThemeResource(Context context, int attr) {
        TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(attr, value, true) ? value.resourceId : 0;
    }
    public static Drawable getThemeDrawable(Context context, int attr) {
        int res = getThemeResource(context, attr);
        return res != 0 ? context.getResources().getDrawable(res) : null;
    }
    private static boolean isLightTheme(Context context) {
        TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(R.attr.isLightTheme, value, true)
                && value.data != 0;
    }
}
