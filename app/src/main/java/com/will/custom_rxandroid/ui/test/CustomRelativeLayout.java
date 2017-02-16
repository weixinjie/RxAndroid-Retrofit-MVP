package com.will.custom_rxandroid.ui.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by will on 2017/1/13.
 */

public class CustomRelativeLayout extends RelativeLayout {
    int downX;
    int downY;

    CustomViewPager customViewPager;
    CustomTextView customTextView;

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.customViewPager = (CustomViewPager) getChildAt(0);
        this.customTextView = (CustomTextView) getChildAt(1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isDis = super.dispatchTouchEvent(ev);
        Log.e("-----RelativeLayout", "dispatchTouchEvent" + ActionUtils.getActionName(ev) + "  return " + super.dispatchTouchEvent(ev));
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = (int) ev.getX();
//                downY = (int) ev.getY();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int customX = (int) ev.getX();
//                int customY = (int) ev.getY();
//
//                int delayX = Math.abs(customX) - Math.abs(downX);
//                int delayY = Math.abs(customY) - Math.abs(downY);
//
//
//                downX = customX;
//                downY = customY;
//
//                if (Math.abs(delayX) > Math.abs(delayY)) {
//                    Log.e("-------", "给ViewPager");
//                    this.customViewPager.dispatchTouchEvent(ev);
//                } else {
//                    Log.e("-------", "给TextView");
//                    this.customTextView.dispatchTouchEvent(ev);
//                }
//                break;
//        }
        return isDis;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("-----RelativeLayout", "onTouchEvent" + ActionUtils.getActionName(event) + "  return " + super.onTouchEvent(event));
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("-----RelativeLayout", "onInterceptTouchEvent" + ActionUtils.getActionName(ev) + "  return " + super.onInterceptTouchEvent(ev));
        return super.onInterceptTouchEvent(ev);
    }
}
