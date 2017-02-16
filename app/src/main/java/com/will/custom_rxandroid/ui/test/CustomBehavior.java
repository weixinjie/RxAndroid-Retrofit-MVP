package com.will.custom_rxandroid.ui.test;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by will on 2017/1/16.
 */

public class CustomBehavior extends CoordinatorLayout.Behavior<NestedScrollView> {

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("----------", "开始创建了");
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        return dependency instanceof ViewPager;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean isVertical = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        Log.e("--------isVertical", String.valueOf(isVertical));
        return isVertical; //只考虑垂直滑动
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, NestedScrollView child, View target, int dx, int dy, int[] consumed) {
        Log.e("--------", "调用了onNestedPreScroll");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }
}
