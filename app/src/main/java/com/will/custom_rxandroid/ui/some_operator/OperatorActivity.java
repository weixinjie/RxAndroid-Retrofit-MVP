package com.will.custom_rxandroid.ui.some_operator;

import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.some_operator.OperatorPresenter;
import com.will.custom_rxandroid.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperatorActivity extends BaseActivity {

    OperatorPresenter operatorPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachView() {
        operatorPresenter = new OperatorPresenter();
    }

    @Override
    protected void detachView() {
        operatorPresenter.detachView();
    }

    @OnClick({R.id.just, R.id.from, R.id.defer,
            R.id.inteveal, R.id.repeat, R.id.repeat_when,
            R.id.buffer, R.id.flatmap, R.id.group_by,
            R.id.map})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.just:
                operatorPresenter.just();
                break;
            case R.id.from:
                operatorPresenter.from();
                break;
            case R.id.defer:
                operatorPresenter.defer();
                break;
            case R.id.inteveal:
                operatorPresenter.interval();
                break;
            case R.id.repeat:
                operatorPresenter.repeat();
                break;
            case R.id.repeat_when:
                operatorPresenter.repeat_when();
                break;
            case R.id.buffer:
                operatorPresenter.buffer();
                break;
            case R.id.flatmap:
                operatorPresenter.flatmap();
                break;
            case R.id.group_by:
                operatorPresenter.group_by();
                break;
            case R.id.map:
                operatorPresenter.map();
                break;
        }
    }
}
