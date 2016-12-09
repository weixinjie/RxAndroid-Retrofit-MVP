package com.will.custom_rxandroid;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.andview.refreshview.utils.LogUtils;
import com.bugtags.library.Bugtags;
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

        Bugtags.start("4ef9dfef20678ee5ffbf5972e0b77d00", this, Bugtags.BTGInvocationEventBubble);

        instance = this;
        LogUtils.allowD = false;
        LogUtils.allowI = false;
        LogUtils.allowV = false;
        LogUtils.allowW = false;
        LogUtils.allowWtf = false;

        initStrictMode();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    private static RefWatcher refWatcher;

    public static synchronized RefWatcher getRefWatcher() {
        return refWatcher;
    }

    /**
     * 开启严格模式
     */
    public void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().detectDiskReads().penaltyLog().build());

//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
    }
}
