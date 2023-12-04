package com.doviesfitness.ui.profile.inbox.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.doviesfitness.R;

public class OverScrollBounceBehavior extends CoordinatorLayout.Behavior<View> {

    private int mOverScrollY;
    Context context;

    public OverScrollBounceBehavior() {
    }

    public OverScrollBounceBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        mOverScrollY = 0;
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed == 0) {
            return;
        }

        mOverScrollY -= dyUnconsumed;
        final ViewGroup group = (ViewGroup) target;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            final View view = group.getChildAt(i);
            Animation animZoomIn = AnimationUtils.loadAnimation(context, R.anim.custom_bounce_anim);
            view.startAnimation(animZoomIn);
           // view.setTranslationY(mOverScrollY);
           // view.setScaleY(mOverScrollY);
          /*  Animation animZoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
            view.startAnimation(animZoomIn);*/
           /* if (R.id.iv_custom_image==view.getId()) {
                //view.setTranslationY(mOverScrollY);
                Animation animZoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
                view.startAnimation(animZoomIn);
               // ViewCompat.animate(view).scaleY(10).start();
            }
            else   {
                view.setTranslationY(mOverScrollY);
            }*/

        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        final ViewGroup group = (ViewGroup) target;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            final View view = group.getChildAt(i);
            ViewCompat.animate(view).translationY(0).start();
         //   ViewCompat.animate(view).scaleY(0).start();
        }
    }

}
