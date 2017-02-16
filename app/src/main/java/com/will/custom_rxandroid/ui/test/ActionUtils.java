package com.will.custom_rxandroid.ui.test;

import android.view.MotionEvent;

/**
 * Created by will on 2017/1/14.
 */

public class ActionUtils {

    /**
     * 获取action name
     *
     * @param motionEvent
     * @return
     */
    public static String getActionName(MotionEvent motionEvent) {
        String actionName;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionName = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                actionName = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                actionName = "ACTION_UP";
                break;
            default:
                actionName = String.valueOf(motionEvent.getAction());
                break;
        }

        return actionName;
    }
}
