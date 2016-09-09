package com.will.custom_rxandroid.presenter.token_avanced;

import com.will.custom_rxandroid.pojo.token.TokenDataBean;
import com.will.custom_rxandroid.presenter.base.BaseView;

/**
 * Created by will on 16/9/9.
 */

public interface TokenAvancedView extends BaseView {
    void show_error(String message);

    void show_success_view();

    void load_success(TokenDataBean data);

    void stop_refresh();
}
