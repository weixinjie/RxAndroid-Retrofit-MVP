package com.will.custom_rxandroid.presenter.time;

import com.will.custom_rxandroid.presenter.base.BasePresenter;
import com.will.custom_rxandroid.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by will on 16/9/12.
 */

public class TimePresenter extends BasePresenter<TimeView> {
    long start_time = System.currentTimeMillis();

    public TimePresenter(TimeView timeView) {
        this.mvpView = timeView;
    }

    /**
     * 在做App的时候，有些地方我们可能会时不时的去请求服务器，以至于客户端的数据是最新的，在RxJava中可以这样做
     * 开始轮询操作
     */
    public void start_circle() {
        subscription = Observable.interval(0, 5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        long current_time = System.currentTimeMillis();
                        long seconds = (current_time - start_time) / 1000;
                        ToastUtils.showToast("间隔时间为: " + seconds + "S");
                        start_time = current_time;
                    }
                });
    }

    /**
     * 停止轮询操作
     */
    public void stop_circle() {
        if (subscription != null)
            subscription.unsubscribe();
    }
}
