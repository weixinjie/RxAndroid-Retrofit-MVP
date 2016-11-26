package com.will.custom_rxandroid;

import android.app.Application;
import android.content.Context;

import com.andview.refreshview.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        BaseApp application = (BaseApp) context.getApplicationContext();
        return application.refWatcher;
    }
}
