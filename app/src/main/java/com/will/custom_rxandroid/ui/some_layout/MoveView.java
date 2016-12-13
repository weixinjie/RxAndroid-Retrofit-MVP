package com.will.custom_rxandroid.ui.some_layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


/**
 * 1、requestLayout： 当view确定自身已经不再适合现有的区域时，该view本身调用这个方法要求parent view重新调用他的onMeasure onLayout来对重新设置自己位置。
 * 特别的当view的layoutparameter发生改变，并且它的值还没能应用到view上，这时候适合调用这个方法。必须是在UI线程中进行工作
 * 2、invalidate： View本身调用迫使view重画。必须是在UI线程中进行工作。比如在修改某个view的显示时，调用invalidate()才能看到重新绘制的界面。
 * 3、postInvalidate： View本身调用迫使view重画。在非UI线程中进行。
 */

public class MoveView extends View {

    private int lastX;
    private int lastY;
    private int screenWidth;
    private int screenHeight;

    int currentX;
    int currentY;

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 50, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        currentX = (int) event.getRawX();
        currentY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) getLayoutParams();
                int left = layoutParams.leftMargin + currentX - lastX;
                int top = layoutParams.topMargin + currentY - lastY;

                layoutParams.leftMargin = left;
                layoutParams.topMargin = top;
                setLayoutParams(layoutParams);
                requestLayout();
                break;
            }

            case MotionEvent.ACTION_UP: {

                break;
            }
        }
        lastX = currentX;
        lastY = currentY;
        return true;
    }
}
