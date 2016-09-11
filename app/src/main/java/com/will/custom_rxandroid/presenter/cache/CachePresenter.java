package com.will.custom_rxandroid.presenter.cache;

import android.util.Log;
import android.widget.Toast;

import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.internal.util.unsafe.MpmcArrayQueue;

/**
 * Created by will on 16/9/9.
 */

public class CachePresenter extends BasePresenter<CacheView> {
    public CachePresenter(CacheView mvpView) {
        this.mvpView = mvpView;
    }

    public void load_data() {
        final long startingTime = System.currentTimeMillis();
        subscription = CacheData.getInstance()
                .subscribeData(new Observer<List<GankBean>>() {
                                   @Override
                                   public void onCompleted() {
                                       mvpView.hide_loading();
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       mvpView.show_error(e.getMessage());
                                   }

                                   @Override
                                   public void onNext(List<GankBean> items) {
                                       String use_time = String.valueOf(System.currentTimeMillis() - startingTime);
                                       mvpView.stop_refresh();
                                       mvpView.load_data_success(items, CacheData.getInstance().getDataSourceText() + " 用时:" + use_time + "mm");
                                   }
                               }

                );
    }

    public void clear_memory() {
        CacheData.getInstance().clearMemoryCache();
    }

    public void clear_disk() {
        CacheData.getInstance().clearMemoryAndDiskCache();
    }

}
