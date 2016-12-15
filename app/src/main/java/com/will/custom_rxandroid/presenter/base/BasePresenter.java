package com.will.custom_rxandroid.presenter.base;


import com.will.custom_rxandroid.BaseApp;

import rx.Subscription;

/**
 * Created by WuXiaolong on 2016/3/30.
 */
public class BasePresenter<V> implements Presenter<V> {
    protected V mvpView;
    protected Subscription subscription;

    @Override
    public void attachView(V mvpView) {
        this.mvpView = mvpView;

    }


    @Override
    //avoid memory leaks
    public void detachView() {
        if (this.mvpView != null) {
            this.mvpView = null;
        }
        if (this.subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

}
