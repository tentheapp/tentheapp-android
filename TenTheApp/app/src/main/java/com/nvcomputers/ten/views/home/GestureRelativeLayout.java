package com.nvcomputers.ten.views.home;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by rkumar4 on 6/14/2017.
 */

public class GestureRelativeLayout extends RelativeLayout {
    GestureDetector myGesture;

    public GestureRelativeLayout(Context context, GestureDetector gest) {
        super(context);
        myGesture = gest;
    }

    public GestureRelativeLayout(Context context) {
        super(context);
    }

    public GestureRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (myGesture != null && myGesture.onTouchEvent(ev))
            return true;
        else
            return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (myGesture != null && myGesture.onTouchEvent(ev))
            return true;
        else
            return super.onInterceptTouchEvent(ev);
    }

    public void update(GestureDetector gest) {
        myGesture = gest;
    }
}