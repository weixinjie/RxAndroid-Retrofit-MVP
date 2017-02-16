package com.will.custom_rxandroid.ui.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.will.custom_rxandroid.utils.LogUtils;


/**
 * Created by will on 16/9/19.
 * 介绍一下getMeasureHeight跟getHeight的区别：
 * 屏幕能显示的下这个View的时候两个值是一样的，
 * 屏幕显示不下的时候getMeasureHeight = getHeight+屏幕现实不下的高度
 */

public class ClickView extends View implements View.OnClickListener {
    private Paint paint;
    private Rect mBounds;

    private int text_size = 30;
    private int count = 0;

    public ClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        paint = new Paint();
        mBounds = new Rect();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * view已经不再页面上了
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 当前的view已经准备好了
     *
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setColor(Color.RED);
        paint.setTextSize(text_size);
        String current_count = String.valueOf("当前数量" + count);
        canvas.drawText(current_count, getMeasuredWidth() / 2 - measureText()[0] / 2, getMeasuredHeight() / 2 + measureText()[1] / 2, paint);
    }

    @Override
    public void onClick(View v) {
        count++;
        invalidate();
    }


    /**
     * 注意系统帮我们测量的高度和宽度都是MATCH_PARNET，当我们设置明确的宽度和高度时，
     * 系统帮我们测量的结果就是我们设置的结果，
     * 当我们设置为FILL_PARENT,或者MATCH_PARENT系统帮我们测量的结果就是MATCH_PARENT的长度So当我们设置wrap_content的时候
     * View的宽跟高都需要我们自己设置
     * <p>
     * 重写之前先了解MeasureSpec的specMode,一共三种类型:
     * **EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
     * **AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
     * **UNSPECIFIED：表示子布局想要多大就多大，很少使用
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width_model = MeasureSpec.getMode(widthMeasureSpec);
        int width_size = MeasureSpec.getSize(widthMeasureSpec);
        int height_model = MeasureSpec.getMode(heightMeasureSpec);
        int height_size = MeasureSpec.getSize(heightMeasureSpec);

        int view_width;
        int view_height;


        if (width_model == MeasureSpec.EXACTLY) { //如果模式为精准的
            view_width = getPaddingLeft() + getPaddingRight() + width_size;
        } else {
            view_width = (int) (measureText()[0] + getPaddingLeft() + getPaddingRight());
        }

        if (height_model == MeasureSpec.EXACTLY) {
            view_height = getPaddingTop() + getPaddingBottom() + height_size;
        } else {
            view_height = (int) measureText()[1] + getPaddingBottom() + getPaddingTop();
        }
        setMeasuredDimension(view_width, view_height);
    }

    /**
     * 测量文字的宽度跟高度
     *
     * @return
     */
    private float[] measureText() {
        paint.setTextSize(text_size);
        String text = String.valueOf("当前数量" + count);
        paint.getTextBounds(text, 0, text.length(), mBounds);
        return new float[]{mBounds.width(), mBounds.height()};
    }

    public void clearText() {
        count = 0;
        invalidate();
    }

}
