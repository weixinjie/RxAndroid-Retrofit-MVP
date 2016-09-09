package com.will.custom_rxandroid.presenter.map;


import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by will on 16/9/7.
 */

public class MapPresenter extends BasePresenter<MapView> {
    public MapPresenter(MapView mapView) {
        this.mvpView = mapView;
    }

    /**
     * 加载数据
     *
     * @param page_count
     * @param current_page
     * @param is_refresh
     */
    public void load_data(int page_count, int current_page, final boolean is_refresh) {
        mvpView.show_loading();
        subscription = BaseApi.getGankApi()
                .get_gank(page_count, current_page)
                .map(new GankResult2GankBeanUtils())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankBean>>() {
                    @Override
                    public void onCompleted() {
                        mvpView.hide_loading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mvpView.stop_refresh();
                        mvpView.stop_loadmore();
                        mvpView.show_error(e.getMessage());
                    }

                    @Override
                    public void onNext(List<GankBean> images) {
                        if (is_refresh) {
                            mvpView.stop_refresh();
                            if (images.size() > 0) {
                                mvpView.set_loadmore_complete(false);
                                mvpView.refresh_success(images);
                            } else {
                                mvpView.show_empty();
                            }
                        } else {
                            mvpView.stop_loadmore();
                            if (images.size() > 0)
                                mvpView.set_loadmore_complete(false);
                            else
                                mvpView.set_loadmore_complete(true);
                            mvpView.loadmore_success(images);
                        }
                    }
                });
    }

}
