package com.will.custom_rxandroid;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by will on 16/9/5.
 */

public class BaseActivity extends Activity {
    protected Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        un_scripte();
    }

    protected void un_scripte() {
        if (subscription != null)
            subscription.unsubscribe();
    }
}
