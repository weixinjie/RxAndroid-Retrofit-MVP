package com.will.custom_rxandroid.ui.custome_view_viewdraphelper;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by will on 2017/1/7.
 */

public class CustomDrawLayout extends LinearLayout {
    private ViewDragHelper viewDragHelper;

    View menuView; //菜单布局
    View contentView; //主页布局

    private int mMinDrawerMargin = 80; //draw距离父容器最右边的最小边距

    public CustomDrawLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new CustomDragCallback());
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    /**
     * 当布局完成填充时回调此函数
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize); //保存一下

        View menuView = getChildAt(1);
        MarginLayoutParams menuLayoutParams = (MarginLayoutParams) menuView.getLayoutParams();
        final int menuWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                menuLayoutParams.leftMargin + menuLayoutParams.rightMargin + 100, menuLayoutParams.width);
        final int menuHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                menuLayoutParams.topMargin + menuLayoutParams.bottomMargin, menuLayoutParams.height);
        menuView.measure(menuWidthSpec, menuHeightSpec);

        View contentView = getChildAt(0);
        MarginLayoutParams contentLayoutParams = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidhtSpec = MeasureSpec.makeMeasureSpec(widthSize -
                contentLayoutParams.leftMargin - contentLayoutParams.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize -
                contentLayoutParams.topMargin - contentLayoutParams.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidhtSpec, contentHeightSpec);

        this.menuView = menuView;
        this.contentView = contentView;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        MarginLayoutParams menuLayoutParams = (MarginLayoutParams) menuView.getLayoutParams();
        int menuWidth = menuView.getMeasuredWidth();
        int menuHeight = menuView.getMeasuredHeight();
        menuView.layout(-menuWidth, menuLayoutParams.topMargin, 0, menuLayoutParams.topMargin + menuHeight);

        MarginLayoutParams contentLayoutParams = (MarginLayoutParams) contentView.getLayoutParams();
        int contentWidth = contentView.getWidth();
        int contentHeight = contentView.getHeight();

        contentView.layout(0 + contentLayoutParams.leftMargin,
                contentLayoutParams.topMargin, contentWidth,
                contentLayoutParams.topMargin + contentHeight);
    }


    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                viewDragHelper.cancel();
                break;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    class CustomDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //捕获该view
            return child == menuView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int newLeft = Math.max(-child.getWidth(), Math.min(left, 0));
            //始终都是取left的值，初始值为-child.getWidth()，当向右拖动的时候left值增大，当left大于0的时候取0
            return newLeft;
        }

        //手指释放的时候回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int childWidth = releasedChild.getWidth();
            //0~1f
            float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;

            viewDragHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth,
                    releasedChild.getTop());
            //由于offset 取值为0~1，所以settleCapturedViewAt初始值为 -childWidth，滑动小于0.5取值也为-childWidth，
            //大于0.5取值为0
            invalidate();
        }

        //在边界拖动时回调
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            viewDragHelper.captureChildView(menuView, pointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            int childWidth = changedView.getWidth();
            float offset = (float) (childWidth + left) / childWidth;
            changedView.setVisibility(offset == 0 ? View.GONE : View.VISIBLE);
            //offset 为0 的时候隐藏 ， 不为0显示
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            //始终取值为child.getWidth()
            return menuView == child ? child.getWidth() : 0;
        }

    }
}
