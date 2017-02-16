package com.will.custom_rxandroid.ui.test;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.will.custom_rxandroid.ui.test.ActionUtils;

/**
 * Created by will on 2017/1/13.
 */

public class CustomViewPager extends ViewPager {

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("-------CustomViewPager", "dispatchTouchEvent" + ActionUtils.getActionName(ev) + "  return " + super.dispatchTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        Log.e("-------CustomViewPager", "onInterceptHoverEvent" + ActionUtils.getActionName(event) + "  return " + super.onInterceptHoverEvent(event));
        return super.onInterceptHoverEvent(event);
    }


    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("-------CustomViewPager", "onTouchEvent" + ActionUtils.getActionName(ev) + "  return " + super.onTouchEvent(ev));
        return super.onTouchEvent(ev);
    }
}
