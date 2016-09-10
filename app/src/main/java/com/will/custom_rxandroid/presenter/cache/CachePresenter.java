package com.will.custom_rxandroid.presenter.cache;

import android.util.Log;

import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.List;

import rx.Observer;

/**
 * Created by will on 16/9/9.
 */

public class CachePresenter extends BasePresenter<CacheView> {
    public CachePresenter(CacheView mvpView) {
        this.mvpView = mvpView;
    }

    public void load_data() {
        final long startingTime = System.currentTimeMillis();
        mvpView.show_loading();
        subscription = CacheData.getInstance()
                .subscribeData(new Observer<List<GankBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mvpView.show_error(e.getMessage());
                    }

                    @Override
                    public void onNext(List<GankBean> items) {
                        long loadingTime = (System.currentTimeMillis() - startingTime);

                        mvpView.stop_refresh();
                        mvpView.show_success_view();
                        mvpView.load_data_success(items, CacheData.getInstance().getDataSourceText() + " 耗时:" + loadingTime + "mm");
                    }
                });
    }

    public void clear_memory() {
        CacheData.getInstance().clearMemoryCache();
    }

    public void clear_disk() {
        CacheData.getInstance().clearMemoryAndDiskCache();
    }

}
