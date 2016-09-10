package com.will.custom_rxandroid.presenter.cache;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.map.GankResult2GankBeanUtils;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by will on 16/9/9.
 * 请注意,内存缓存还有一定的问题,硬盘缓存跟网络获取已经没有问题了
 */

public class CacheData {
    private static CacheData instance;
    private static final int SOURCE_DISK = 1; //硬盘缓存
    private static final int SOURCE_NET = 2; //网络获取
//    private static final int SOURCE_MEMORY = 3; //内存缓存

    int data_source;

    @IntDef({SOURCE_DISK, SOURCE_NET})
//    @IntDef({SOURCE_DISK, SOURCE_NET, SOURCE_MEMORY})
    @interface DataSource {
    }

    BehaviorSubject<List<GankBean>> cache;

    private CacheData() {

    }

    public static CacheData getInstance() {
        if (instance == null) {
            instance = new CacheData();
        }
        return instance;
    }

    private void set_data_source(@DataSource int data_source) {
        this.data_source = data_source;
    }

    public String getDataSourceText() {
        String data_source_str;
        switch (data_source) {
            case SOURCE_DISK:
                data_source_str = "硬盘加载";
                break;
            case SOURCE_NET:
                data_source_str = "网络加载";
                break;
//            case SOURCE_MEMORY:
//                data_source_str = "内存加载";
//            break;
            default:
                data_source_str = "网络加载";
        }
        return data_source_str;
    }


    public Subscription subscribeData(@NonNull Observer<List<GankBean>> observer) {
//        if (cache == null) {
        cache = BehaviorSubject.create();
        Observable.create(new Observable.OnSubscribe<List<GankBean>>() {
            @Override
            public void call(Subscriber<? super List<GankBean>> subscriber) {
                List<GankBean> items = CacheDB.getInstance().readItems();
                if (items == null) {
                    set_data_source(SOURCE_NET);
                    load_form_net();
                } else {
                    set_data_source(SOURCE_DISK);
                    cache.onNext(items);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(cache);

//        } else {
//            set_data_source(SOURCE_MEMORY);
//        }
        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    public void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        CacheDB.getInstance().delete();
    }

    /**
     * 从网络加载
     */
    private void load_form_net() {
        BaseApi.getGankApi()
                .get_gank(100, 1)
                .subscribeOn(Schedulers.io())
                .map(new GankResult2GankBeanUtils())
                .doOnNext(new Action1<List<GankBean>>() {
                    @Override
                    public void call(List<GankBean> items) {
                        CacheDB.getInstance().writeItems(items);
                    }
                })
                .subscribe(new Action1<List<GankBean>>() {
                    @Override
                    public void call(List<GankBean> items) {
                        cache.onNext(items);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
