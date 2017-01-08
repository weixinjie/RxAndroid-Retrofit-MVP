package com.will.custom_rxandroid.ui.custome_view_viewdraphelper;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.custom_rxandroid.utils.LogUtils;

/**
 * Created by will on 2017/1/6.
 */

public class CustomView extends LinearLayout {
    private ViewDragHelper mDragHelper; //ViewDragHelper一般用在一个自定义ViewGroup的内部

    public CustomView(Context context) {
        super(context);
        init();
    }


    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        /**
         * @params ViewGroup forParent 必须是一个ViewGroup
         * @params float sensitivity 灵敏度
         * @params Callback cb 回调
         */
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        /**
         * 尝试捕获子view，一定要返回true
         *
         * @param view      child 尝试捕获的view
         * @param pointerId pointerId 指示器id？
         *                  这里可以决定哪个子view可以拖动
         */
        @Override
        public boolean tryCaptureView(View view, int pointerId) {
//   return mCanDragView == view;
            return view instanceof TextView;
        }

        /**
         * 返回滑动范围，用来设置拖拽的范围。
         * 大于0即可，不会真正限制child的移动范围。
         * 内部用来计算是否此方向是否可以拖拽，以及释放时动画执行时间
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth();
        }

        ;

        /**
         * releasedChild : 释放的对象
         * xvel：x轴上的每秒速率
         * yvel：y轴上的每秒速率
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // 当释放滑动的view时，处理最后的事情。（执行开启或关闭的动画）
            Log.e("-------", "onViewReleased");
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        ;

        /**
         * 处理水平方向上的拖动
         *
         * @param child child 被拖动到view
         * @param left  left 移动的x轴的距离
         * @param dx    所产生的偏移量
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            System.out.println("left = " + left + ", dx = " + dx);

            // 两个if主要是为了让viewViewGroup里
            if (getPaddingLeft() > left) {
                return getPaddingLeft();
            }

            if (getWidth() - child.getWidth() < left) {
                return getWidth() - child.getWidth();
            }

            return left;
        }

        /**
         * 处理竖直方向上的拖动
         *
         * @param child child 被拖动到view
         * @param top   top 移动y轴的距离
         * @param dy    dy 所产生的偏移量
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 两个if主要是为了让viewViewGroup里
            if (getPaddingTop() > top) {
                return getPaddingTop();
            }

            if (getHeight() - child.getHeight() < top) {
                return getHeight() - child.getHeight();
            }

            return top;
        }

        /**
         * 当拖拽到状态改变时回调
         *
         * @params 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                    Log.e("-------", "我正在被拖动");
                    break;
                case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                    Log.e("-------", "view没有被拖拽或者 正在进行fling/snap");
                    break;
                case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                    Log.e("-------", "fling完毕后被放置到一个位置");
                    break;
            }
            super.onViewDragStateChanged(state);
        }

        /**
         * 用户点击边缘
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            LogUtils.e("onEdgeTouched " + String.valueOf(edgeFlags));
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        /**
         * 用户开始拖动父view的边缘了
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            LogUtils.e("onEdgeDragStarted " + String.valueOf(edgeFlags));
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }
    }

    /**
     * 要让ViewDragHelper能够处理拖动需要将触摸事件传递给ViewDragHelper
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_DOWN:
                mDragHelper.cancel(); // 相当于调用 processTouchEvent收到ACTION_CANCEL
                break;
        }

        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 处理拦截到的事件
         * 这个方法会在返回前分发事件
         */
        mDragHelper.processTouchEvent(event);
        return true;
    }


}