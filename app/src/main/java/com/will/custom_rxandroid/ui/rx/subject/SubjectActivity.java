package com.will.custom_rxandroid.ui.rx.subject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.subject.SubjectPresenter;
import com.will.custom_rxandroid.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * subject是一个神奇的对象，它可以是一个Observable同时也可以是一个Observer：它作为连接这两个世界的一座桥梁。
 * 一个Subject可以订阅一个Observable，就像一个观察者，并且它可以发射新的数据，或者传递它接受到的数据，就像一个Observable。
 * 很明显，作为一个Observable，观察者们或者其它Subject都可以订阅它。
 * RxJava提供四种不同的Subject：
 * PublishSubject
 * BehaviorSubject
 * ReplaySubject.
 * AsyncSubject
 */
public class SubjectActivity extends BaseActivity {

    SubjectPresenter subjectPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachView() {
        subjectPresenter = new SubjectPresenter();
    }

    @Override
    protected void detachView() {
        subjectPresenter.detachView();
    }

    @OnClick({R.id.publish_subject})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_subject:
                subjectPresenter.publish_subject();
                break;
        }
    }
}
