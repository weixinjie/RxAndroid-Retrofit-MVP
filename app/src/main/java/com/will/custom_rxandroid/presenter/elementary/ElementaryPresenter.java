package com.will.custom_rxandroid.presenter.elementary;


import android.util.Log;

import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.elementary.ZhuangBiImage;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
        subscription = BaseApi.getZhuangBiApi().search("装逼")
//                .flatMap(new Func1<List<ZhuangBiImage>, Observable<ZhuangBiImage>>() { //这里的转化是像for循环一样一个一个的发送数据
//                    @Override
//                    public Observable<ZhuangBiImage> call(List<ZhuangBiImage> zhuangBiImages) {
//                        return Observable.from(zhuangBiImages);
//                    }
//                })
//                .take(3) //只取前三个(需要与from进行配合使用)
//                .filter(new Func1<ZhuangBiImage, Boolean>() { //对得到的数据进行过滤,该数据符合发送的标准则返回true,如果该数据不符合返回的标准则返回false
//                    @Override
//                    public Boolean call(ZhuangBiImage zhuangBiImage) {
//                        if (zhuangBiImage.getImage_url().contains("http://zhuangbi.idagou.com/i/2016-01-27-bd6f1e1e31fea64f7f2f33ba31b0b1a2.gif"))
//                            return true;
//                        else
//                            return false;
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<ZhuangBiImage>() {
//                    @Override
//                    public void call(ZhuangBiImage zhuangBiImage) {
//                        Log.e("-----url", zhuangBiImage.getImage_url());
//                    }
//                });
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
