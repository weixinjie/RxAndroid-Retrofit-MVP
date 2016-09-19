package com.will.custom_rxandroid.ui.rx.time;

import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.base.BaseView;
import com.will.custom_rxandroid.presenter.time.CountDownPresenter;
import com.will.custom_rxandroid.presenter.time.TimePresenter;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeActivity extends BaseActivity implements BaseView {

    private TimePresenter presenter;
    private CountDownPresenter countDownPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachView() {
        presenter = new TimePresenter(this);
        countDownPresenter = new CountDownPresenter(this);
    }

    @Override
    protected void detachView() {
        presenter.detachView();
        countDownPresenter.detachView();
    }


    @OnClick({R.id.bt_stop, R.id.bt_start, R.id.bt_start_count_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_stop:
                presenter.stop_circle();
                break;
            case R.id.bt_start:
                presenter.start_circle();
                break;
            case R.id.bt_start_count_down:
                countDownPresenter.do_sth_after_2s();
                break;
        }

    }

    @Override
    public void show_toast(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void show_loading() {
    }

    @Override
    public void hide_loading() {

    }
}
