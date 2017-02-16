package com.will.custom_rxandroid.ui.test;

import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by will on 2017/1/13.
 */

public class CustomScrollView extends ScrollView {

    private GestureDetector mGestureDetector;

    private boolean shouldDown = true;

    private MotionEvent motionEvent;


    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
       /* mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);*/
    }


    int downX = 0;
    int downY = 0;

    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("----------", "onTouchEvent 接收到了事件 " + ActionUtils.getActionName(ev));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();

                motionEvent = MotionEvent.obtain(ev);
                motionEvent.setAction(MotionEvent.ACTION_DOWN);


                break;
            case MotionEvent.ACTION_MOVE:
                int customX = (int) ev.getRawX();
                int customY = (int) ev.getRawY();

                int delayX = customX - downX;
                int delayY = customY - downY;

                if (Math.abs(delayX) > Math.abs(delayY)) {
                    shouldDown = false;
                    dispatchTouchEvent(motionEvent);

                } else {
                    shouldDown = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("-------", "触发了 ACTION_UP");
                break;
        }

        return super.onTouchEvent(ev);
    }


    // Return false if we're scrolling in the x direction
/*    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }*/

}
