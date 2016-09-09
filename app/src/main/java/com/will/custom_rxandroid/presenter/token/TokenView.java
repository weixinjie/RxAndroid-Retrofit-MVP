package com.will.custom_rxandroid.presenter.token;

import com.will.custom_rxandroid.pojo.token.TokenDataBean;
import com.will.custom_rxandroid.presenter.base.BaseView;

/**
 * Created by will on 16/9/9.
 */

public interface TokenView extends BaseView {
    void show_error();

    void load_data_success(TokenDataBean data);

    void show_success_view();

    void stop_refresh();
}
