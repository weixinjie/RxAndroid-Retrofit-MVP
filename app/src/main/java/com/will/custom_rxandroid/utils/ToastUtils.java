package com.will.custom_rxandroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by will on 16/9/7.
 */

public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(Context mContext, String text) {
        if (toast == null) {
            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
