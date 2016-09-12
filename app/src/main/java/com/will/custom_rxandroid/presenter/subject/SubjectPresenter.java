package com.will.custom_rxandroid.presenter.subject;

import com.andview.refreshview.utils.LogUtils;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by will on 16/9/12.
 */

public class SubjectPresenter extends BasePresenter {

    public void publish_subject() {
        PublishSubject publishSubject = PublishSubject.create();
        subscription = publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                LogUtils.e("on complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String o) {
                LogUtils.e(o);
            }
        });

        publishSubject.onNext("hello word");
    }


}
