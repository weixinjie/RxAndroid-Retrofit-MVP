package com.will.custom_rxandroid.presenter.token;

import com.will.custom_rxandroid.presenter.base.BasePresenter;

/**
 * Created by will on 16/9/9.
 */

public class TokenPresenter extends BasePresenter<TokenView> {
    public TokenPresenter(TokenView tokenView) {
        this.mvpView = tokenView;
    }

    /**
     * 加载数据
     */
    public void load_data() {

    }
}
