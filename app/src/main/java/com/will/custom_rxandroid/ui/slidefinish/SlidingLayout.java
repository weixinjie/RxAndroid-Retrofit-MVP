package com.will.custom_rxandroid.ui.slidefinish;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by will on 2017/1/1.
 */

public class SlidingLayout extends FrameLayout {
    private Scroller mScroller; //滑动帮助类
    private int mTouchSlop; //最小的滑动距离，与机型有关，需要动态获取
    private ViewGroup parentView; //顶层ViewGroup,我们的布局文件的最外层系统都会帮我们包装一层FrameLayout
    private int screenWidth; //屏幕宽度

    private int last_x; //上次按下的x y
    private int last_y;

    private int delay_x;
    private int delay_y;

    private boolean isFinish = false;

    private SlidingLayout.OnActivityFinishListener onActivityFinish;

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            parentView = (ViewGroup) getParent();
            screenWidth = getWidth();
        }
    }


    /**
     * 进行事件拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last_x = (int) ev.getRawX();
                last_y = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int current_x = (int) ev.getRawX();
                int current_y = (int) ev.getRawY();

                delay_x = current_x - last_x;
                delay_y = current_y - last_y;
                if ((Math.abs(delay_x) / (Math.abs(delay_y) + 1) >= 2)) { //30度角的时候才开始拦截事件
                    isIntercept = true;
                } else {
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return isIntercept;
    }

    int alpha;

    /**
     * 进行事件处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last_x = (int) ev.getRawX();
                last_y = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int current_x = (int) ev.getRawX();
                int current_y = (int) ev.getRawY();

                delay_x = current_x - last_x;
                delay_y = current_y - last_y;

                if (Math.abs(delay_x) > mTouchSlop) {
                    if ((Math.abs(delay_x) / (Math.abs(delay_y) + 1) >= 2)) { //30度角的时候才开始拦截事件
                        parentView.scrollBy(-delay_x, 0); //开始滑动

                    } else {
                        return false;
                    }

                } else {
                    return false;
                }

                last_x = current_x;
                last_y = current_y;
                break;
            case MotionEvent.ACTION_UP:

                if (Math.abs(parentView.getScrollX()) > (screenWidth / 3)) {
                    scrollFinish();
                } else {
                    scrollOriginal();
                }
                break;
        }
        return true;
    }

    /**
     * 滑动销毁
     */
    private void scrollFinish() {
        final int delta = (screenWidth + parentView.getScrollX());

        mScroller.startScroll(parentView.getScrollX(), 0, -delta + 1, 0);
        postInvalidate();
        isFinish = true;
    }

    private void scrollOriginal() {
        mScroller.startScroll(parentView.getScrollX(), 0, -parentView.getScrollX(), 0);
        postInvalidate();
        isFinish = false;
    }

    public void setOnActivityFinish(SlidingLayout.OnActivityFinishListener onActivityFinish) {
        this.onActivityFinish = onActivityFinish;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            parentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                onActivityFinish.onActivityFinish();
            }

        }
    }

    public interface OnActivityFinishListener {
        void onActivityFinish();
    }
}
