package com.will.custom_rxandroid.presenter.elementary;


import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.ZhuangBiImage;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by will on 16/9/7.
 */

public class ElementaryPresenter extends BasePresenter<ElementaryView> {

    public ElementaryPresenter(ElementaryView mvpView) {
        this.mvpView = mvpView;
    }

    public void search(String des, final boolean is_refresh, int current_page) {
        mvpView.show_loading();
//        mvpView.show_toast("loading page---" + current_page); //only for debug
        subscription = BaseApi.getZhuangBiApi().search(des)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ZhuangBiImage>>() {
                    @Override
                    public void onCompleted() {
                        mvpView.hide_loading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.show_error(e.getMessage());
                    }

                    public void onNext(List<ZhuangBiImage> zhuangBiImage) {
                        if (is_refresh) {
                            mvpView.stop_refresh();
                            if (zhuangBiImage.size() > 0) {
                                mvpView.set_loadmore_complete(false);
                                mvpView.refresh_success(zhuangBiImage);
                            } else {
                                mvpView.show_empty();
                            }
                        } else {
                            mvpView.stop_loadmore();
                            if (zhuangBiImage.size() > 0)
                                mvpView.set_loadmore_complete(false);
                            else
                                mvpView.set_loadmore_complete(true);
                            mvpView.loadmore_success(zhuangBiImage);
                        }
                    }
                });
    }

}
