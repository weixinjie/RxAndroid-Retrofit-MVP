package com.will.custom_rxandroid.ui.custom_view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;


/**
 * http://blog.qiji.tech/archives/6758
 * <p>
 * 1.scrollTo与scrollBy都是让内容动:比如textview是让中间的文字动,imgaeview是让图片动,所以滑动的时候需要getParent
 * 2.据我理解:Scroller的startScroll方法并不是开始滑动,真正滑动的是  ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
 */


public class CustomView extends View {
    private int lastX;
    private int lastY;
    private Scroller mScroller;
    private Point point;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        point = new Point();
        display.getSize(point);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int current_x = (int) event.getX();
        int current_y = (int) event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = current_x;
                lastY = current_y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offset_x = current_x - lastX;
                int offset_y = current_y - lastY;
                ((View) getParent()).scrollBy(offset_x, offset_y); //开始滑动
                break;
            case MotionEvent.ACTION_UP:
                View parent = (View) getParent();
                mScroller.startScroll(parent.getScrollX(), parent.getScrollY(), -parent.getScrollX(), -parent.getScrollY());
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

}