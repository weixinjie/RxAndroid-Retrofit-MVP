package com.will.custom_rxandroid.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by will on 16/9/5.
 */

public abstract class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
    }


    /**
     * connect to "MVP"
     */
    protected abstract void attachView();

    /**
     * disconnect to "MVP"
     */
    protected abstract void detachView();
}
