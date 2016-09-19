package com.will.custom_rxandroid.ui.rx.token_avanced;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.will.custom_rxandroid.BaseApp;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.pojo.token.TokenDataBean;
import com.will.custom_rxandroid.presenter.token_avanced.TokenAvancedPresenter;
import com.will.custom_rxandroid.presenter.token_avanced.TokenAvancedView;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.utils.PreferencesUtils;
import com.will.custom_rxandroid.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 有的 token 并非一次性的，而是可以多次使用，直到它超时或被销毁（多数 token 都是这样的）。
 * 这样的 token 处理起来比较麻烦：需要把它保存起来，并且在发现它失效的时候要能够自动重新获取新的 token 并继续访问之前由于 token 失效而失败的请求。
 * 如果项目中有多处的接口请求都需要这样的自动修复机制，使用传统的 Callback 形式需要写出非常复杂的代码。
 * 而使用 RxJava ，可以用 retryWhen() 来轻松地处理这样的问题。
 */
public class TokenAvancedActivity extends BaseActivity implements TokenAvancedView {

    TokenAvancedPresenter presenter;

    @BindView(R.id.refresh_view)
    XRefreshView refresh_view;
    @BindView(R.id.error_layout)
    View error_layout;
    @BindView(R.id.tv_token)
    TextView tv_token;
    @BindView(R.id.bt_token)
    Button bt_token;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_avanced);
        ButterKnife.bind(this);

        refresh_view.setPinnedTime(500);
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load_data();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_view.startRefresh();
    }

    @OnClick({R.id.bt_token, R.id.bt_clear_token})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_token:
                refresh_view.startRefresh();
                break;
            case R.id.bt_clear_token: //create token
                PreferencesUtils.remove(BaseApp.getInstance(), TokenAvancedPresenter.NAME_TOKEN);
                PreferencesUtils.remove(BaseApp.getInstance(), TokenAvancedPresenter.NAME_CREATE_UTC);
                refresh_view.startRefresh();
                break;
        }
    }


    @Override
    protected void attachView() {
        presenter = new TokenAvancedPresenter(this);
    }

    @Override
    protected void detachView() {
        presenter.detachView();
    }

    @Override
    public void show_error(String message) {
        error_layout.setVisibility(View.VISIBLE);
        refresh_view.setVisibility(View.GONE);
        TextView error_text = (TextView) error_layout.findViewById(R.id.tv_error);
        Button error_button = (Button) error_layout.findViewById(R.id.bt_error_reload);
        error_text.setText(message);
        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_success_view();
                refresh_view.startRefresh();
            }
        });
    }

    @Override
    public void show_success_view() {
        refresh_view.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
    }

    @Override
    public void load_success(TokenDataBean data) {
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
    public void stop_refresh() {
        refresh_view.stopRefresh();
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
}
