package com.will.custom_rxandroid.presenter.time;

import com.will.custom_rxandroid.presenter.base.BasePresenter;
import com.will.custom_rxandroid.presenter.base.BaseView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by will on 16/9/12.
 */

public class CountDownPresenter extends BasePresenter<BaseView> {
    public CountDownPresenter(BaseView baseView) {
        this.mvpView = baseView;
    }

    /**
     * 2s之后做一些事情(例如,启动页面2S之后打开app主页)
     */
    public void do_sth_after_2s() {
        subscription = Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mvpView.show_toast("do sth after 2s(for example: open main_activity)");
                    }
                });
    }
}
