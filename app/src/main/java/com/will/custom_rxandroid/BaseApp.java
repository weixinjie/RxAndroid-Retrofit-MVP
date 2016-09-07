package com.will.custom_rxandroid;

import android.app.Application;

import com.andview.refreshview.utils.LogUtils;

/**
 * Created by will on 16/9/7.
 */

public class BaseApp extends Application {
    public static BaseApp instance;

    public static BaseApp getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.allowD = false;
        LogUtils.allowI = false;
        LogUtils.allowV = false;
        LogUtils.allowW = false;
        LogUtils.allowWtf = false;
    }
}
