package com.will.custom_rxandroid.ui.base;

import android.app.Activity;
import android.os.Bundle;

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
