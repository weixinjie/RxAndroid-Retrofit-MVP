package com.will.custom_rxandroid.ui.token;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.pojo.token.TokenDataBean;
import com.will.custom_rxandroid.presenter.token.TokenPresenter;
import com.will.custom_rxandroid.presenter.token.TokenView;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenActivity extends BaseActivity implements TokenView {
    @BindView(R.id.tv_token)
    TextView tv_token;
    @BindView(R.id.bt_token)
    Button bt_token;
    @BindView(R.id.error_layout)
    View error_layout;
    @BindView(R.id.token_layout)
    View token_layout;
    @BindView(R.id.refresh_view)
    XRefreshView refresh_view;

    TokenPresenter tokenPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        ButterKnife.bind(this);

        refresh_view.setPinnedTime(500);
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                tokenPresenter.load_data();
            }
        });
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
                refresh_view.startRefresh();
                break;
        }
    }

    @Override
    public void show_toast(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void show_loading() {
        ToastUtils.showToast("loading...");
    }

    @Override
    public void hide_loading() {
        ToastUtils.showToast("hide loading...");
    }

    @Override
    public void show_error() {
        error_layout.setVisibility(View.VISIBLE);
        token_layout.setVisibility(View.GONE);
    }

    @Override
    public void load_data_success(TokenDataBean data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create_utc:     " + data.getCreate_utc());
        stringBuilder.append("\n");
        stringBuilder.append("parent:    " + data.getParent());
        stringBuilder.append("\n");
        stringBuilder.append("token:   " + data.getToken());
        stringBuilder.append("\n");
        stringBuilder.append("user_id:    " + data.getUser_id());
        stringBuilder.append("\n");
        tv_token.setText(stringBuilder.toString());
    }

    @Override
    public void show_success_view() {
        token_layout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
    }

    @Override
    public void stop_refresh() {
        refresh_view.stopRefresh();
    }
}
