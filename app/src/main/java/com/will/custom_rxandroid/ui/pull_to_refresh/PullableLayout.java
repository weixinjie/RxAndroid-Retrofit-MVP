package com.will.custom_rxandroid.ui.pull_to_refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.will.custom_rxandroid.R;

/**
 * Created by will on 2016/11/17.
 */

public class PullableLayout extends ViewGroup {

    private int maxScrollDis = 0; //最大滑动距离

    View mHeader;
    View mFooter;

    TextView tv_header;
    TextView tv_footer;

    Scroller scroller;

    public PullableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeader = LayoutInflater.from(context).inflate(R.layout.header, null);
        mFooter = LayoutInflater.from(context).inflate(R.layout.footer, null);
        tv_header = (TextView) mHeader.findViewById(R.id.tv_head);
        tv_footer = (TextView) mHeader.findViewById(R.id.tv_head);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(mHeader, params);
        addView(mFooter, params);
    }


    int mLayoutContentHeight = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutContentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == mHeader) { //header view 进行布局的时候要隐藏
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
                maxScrollDis = child.getMeasuredHeight();

            } else if (child == mFooter) { //footer view 进行布局的时候要隐藏
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
            } else {
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
                mLayoutContentHeight += child.getMeasuredHeight();
            }
        }
    }

    @Override
    /**
     *负责测量的方法
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    int mLayoutY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int current_y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLayoutY = current_y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dis = mLayoutY - current_y;
                if (Math.abs(getScrollY()) < maxScrollDis) { //如果滑动的距离小于最大滑动距离
                    scrollBy(0, dis);
                } else {
                    tv_header.setText("松开刷新");
                    tv_footer.setText("松开刷新");
                }
                break;
            case MotionEvent.ACTION_UP: //手指放开的时候
                if (Math.abs(getScrollY()) < maxScrollDis) { //如果滑动的距离小于最大滑动的距离
                    scroller.startScroll(0, getScrollY(), 0, -getScrollY());
                } else {
                    scroller.startScroll(0, getScrollY(), 0, -(getScrollY() + maxScrollDis));
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
