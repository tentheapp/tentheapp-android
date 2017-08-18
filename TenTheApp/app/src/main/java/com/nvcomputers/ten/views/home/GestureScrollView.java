package com.nvcomputers.ten.views.home;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rkumar4 on 6/14/2017.
 */

public class GestureScrollView extends NestedScrollView {
    GestureDetector myGesture;

    public GestureScrollView(Context context, GestureDetector gest) {
        super(context);
        myGesture = gest;
    }

    public GestureScrollView(Context context) {
        super(context);
    }

    public GestureScrollView(Context context, AttributeSet attrs) {
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