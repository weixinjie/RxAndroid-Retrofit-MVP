package com.will.custom_rxandroid.ui.some_layout;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.token.TokenPresenter;
import com.will.custom_rxandroid.utils.LogUtils;

/**
 * Created by will on 2016/12/11.
 */

public class MBehavior extends CoordinatorLayout.Behavior<Button> {
    public MBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * 这个方法用来判断是否是depence
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Button child, View dependency) {
        return dependency.getId() == R.id.move_view;
    }

    /**
     * 当dependency发生变化时会回调此方法，在此方法中对child进行响应
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Button child, View dependency) {

        int top = dependency.getTop();
        int right = dependency.getRight();

        LogUtils.e("top " + top);

        CoordinatorLayout.MarginLayoutParams params = (CoordinatorLayout.MarginLayoutParams) child.getLayoutParams();
        params.topMargin = top;
        child.setLayoutParams(params);

        return true;
    }
}
