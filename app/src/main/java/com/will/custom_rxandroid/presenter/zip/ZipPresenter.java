package com.will.custom_rxandroid.presenter.zip;


import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.elementary.ZhuangBiImage;
import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BasePresenter;
import com.will.custom_rxandroid.presenter.map.GankResult2GankBeanUtils;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by will on 16/9/8.
 */

public class ZipPresenter extends BasePresenter<ZipView> {

    public ZipPresenter(ZipView mvpView) {
        this.mvpView = mvpView;
    }

    /**
     * 加载数据
     */
    public void load_data(String search) {
        mvpView.show_loading();
        subscription = Observable.zip(BaseApi.getGankApi().get_gank(10, 1)
                        .map(new GankResult2GankBeanUtils())
                , BaseApi.getZhuangBiApi().search(search), new Func2<List<GankBean>, List<ZhuangBiImage>, List<GankBean>>() {
                    @Override
                    public List<GankBean> call(List<GankBean> gankBeens, List<ZhuangBiImage> zhuangBiImages) {
                        for (ZhuangBiImage image : zhuangBiImages) {
                            GankBean gankBean = new GankBean();
                            gankBean.setCreatedAt(image.getDescription());
                            gankBean.setUrl(image.getImage_url());
                            gankBeens.add(gankBean);
                        }
                        return gankBeens;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankBean>>() {

                    @Override
                    public void onCompleted() {
                        mvpView.hide_loading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.show_error(e.getMessage());
                    }

                    @Override
                    public void onNext(List<GankBean> gankBeen) {
                        if (gankBeen != null && gankBeen.size() > 0) {
                            mvpView.show_success_view();
                            mvpView.stop_refresh();
                            mvpView.load_success(gankBeen);
                        } else {
                            mvpView.show_empty();
                        }

                    }
                });
    }

}
