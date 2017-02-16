package com.will.custom_rxandroid.ui.test;

import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by will on 2017/1/13.
 */

public class CustomScrollingMovementMethod extends ScrollingMovementMethod {
    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        return true;
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {

        return true;
    }

    @Override
    protected boolean bottom(TextView widget, Spannable buffer) {

        return super.bottom(widget, buffer);
    }
}
