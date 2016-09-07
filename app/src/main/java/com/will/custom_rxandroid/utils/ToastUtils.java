package com.will.custom_rxandroid.utils;

import android.content.Context;
import android.widget.Toast;

import com.will.custom_rxandroid.BaseApp;

/**
 * Created by will on 16/9/7.
 */

public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(BaseApp.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
