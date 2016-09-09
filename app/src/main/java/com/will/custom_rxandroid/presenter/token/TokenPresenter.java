package com.will.custom_rxandroid.presenter.token;

import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.token.TokenBean;
import com.will.custom_rxandroid.pojo.token.TokenDataBean;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        mvpView.show_loading();
        subscription = BaseApi.getVirtualApi().get_token("weixinjie")
                .flatMap(new Func1<TokenBean, Observable<TokenDataBean>>() {
                    @Override
                    public Observable<TokenDataBean> call(TokenBean tokenBean) {
                        return BaseApi.getVirtualApi().get_user_data(tokenBean);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TokenDataBean>() {
                    @Override
                    public void onCompleted() {
                        mvpView.hide_loading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.show_error(e.getMessage());
                    }

                    @Override
                    public void onNext(TokenDataBean tokenDataBean) {
                        mvpView.stop_refresh();
                        mvpView.show_success_view();
                        mvpView.load_data_success(tokenDataBean);
                    }
                });
    }
}
