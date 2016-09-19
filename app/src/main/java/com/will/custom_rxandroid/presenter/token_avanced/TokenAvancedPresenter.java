package com.will.custom_rxandroid.presenter.token_avanced;

import com.will.custom_rxandroid.BaseApp;
import com.will.custom_rxandroid.http.VirtualApi;
import com.will.custom_rxandroid.pojo.token.TokenBean;
import com.will.custom_rxandroid.pojo.token.TokenDataBean;
import com.will.custom_rxandroid.presenter.base.BasePresenter;
import com.will.custom_rxandroid.utils.PreferencesUtils;

import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by will on 16/9/9.
 */

public class TokenAvancedPresenter extends BasePresenter<TokenAvancedView> {

    public static String NAME_TOKEN = "token";
    public static String NAME_CREATE_UTC = "create_utc";

    VirtualApi virtualApi;

    public TokenAvancedPresenter(TokenAvancedView tokenAvancedView) {
        this.mvpView = tokenAvancedView;
        virtualApi = new VirtualApi();

    }

    public void load_data() {
        mvpView.show_loading();
        subscription = Observable.just(null).flatMap(new Func1<Object, Observable<TokenDataBean>>() {
            @Override
            public Observable<TokenDataBean> call(Object o) {
                String token = PreferencesUtils.getString(BaseApp.getInstance(), NAME_TOKEN);
                String create_utc = PreferencesUtils.getString(BaseApp.getInstance(), NAME_CREATE_UTC);
                return token == null
                        ? Observable.<TokenDataBean>error(new NullPointerException("token is null"))
                        : virtualApi.get_user_data(new TokenBean(token, create_utc));
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (throwable instanceof NullPointerException) {
                            return virtualApi.get_token("token无效,重新加载token " + System.currentTimeMillis()).doOnNext(new Action1<TokenBean>() {
                                @Override
                                public void call(TokenBean tokenBean) {
                                    PreferencesUtils.putString(BaseApp.getInstance(), NAME_TOKEN, tokenBean.getToken());
                                    PreferencesUtils.putString(BaseApp.getInstance(), NAME_CREATE_UTC, tokenBean.getCreate_utc());
                                }
                            });
                        }
                        return Observable.error(throwable);
                    }
                });
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
                        e.printStackTrace();
                        mvpView.show_error(e.getMessage());
                        mvpView.stop_refresh();
                    }

                    @Override
                    public void onNext(TokenDataBean tokenDataBean) {
                        mvpView.stop_refresh();
                        mvpView.show_success_view();
                        mvpView.load_success(tokenDataBean);
                    }
                });
    }
}
