package com.will.custom_rxandroid.ui.token;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.token.TokenPresenter;
import com.will.custom_rxandroid.presenter.token.TokenView;
import com.will.custom_rxandroid.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenActivity extends BaseActivity implements TokenView {
    @BindView(R.id.tv_token)
    TextView tv_token;
    @BindView(R.id.bt_token)
    Button bt_token;

    TokenPresenter tokenPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachView() {
        tokenPresenter = new TokenPresenter(this);
    }

    @Override
    protected void detachView() {
        tokenPresenter.detachView();
    }

    @OnClick({R.id.bt_token})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_token:
                tokenPresenter.load_data();
                break;
        }
    }

    @Override
    public void show_toast(String message) {

    }

    @Override
    public void show_loading() {

    }

    @Override
    public void hide_loading() {

    }
}
