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
            R.id.map, R.id.scan, R.id.window, R.id.debounce
            , R.id.distinct, R.id.elementat, R.id.filter, R.id.oftype
            , R.id.first, R.id.single})
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
            case R.id.scan:
                operatorPresenter.scan();
                break;
            case R.id.window:
                operatorPresenter.window();
                break;
            case R.id.debounce:
                operatorPresenter.debounce();
                break;
            case R.id.distinct:
                operatorPresenter.distinct();
                break;
            case R.id.elementat:
                operatorPresenter.elementat();
                break;
            case R.id.filter:
                operatorPresenter.filter();
                break;
            case R.id.oftype:
                operatorPresenter.oftype();
                break;
            case R.id.first:
                operatorPresenter.first();
                break;
            case R.id.single:
                operatorPresenter.single();
                break;
        }
    }
}
