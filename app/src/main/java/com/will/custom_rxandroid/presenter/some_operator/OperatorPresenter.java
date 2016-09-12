package com.will.custom_rxandroid.presenter.some_operator;

import android.util.Log;

import com.andview.refreshview.utils.LogUtils;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by will on 16/9/12.
 */

public class OperatorPresenter extends BasePresenter {
    /**
     * just()方法可以传入一到九个参数，它们会按照传入的参数的顺序来发射它们。
     * just()方法也可以接受列表或数组，就像from()方法，但是它不会迭代列表发射每个值,它将会发射整个列表。
     * 通常，当我们想发射一组已经定义好的值时会用到它。
     * 但是如果我们的函数不是时变性的，我们可以用just来创建一个更有组织性和可测性的代码库。
     */
    public void just() {
        subscription = Observable.just("hello word").subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtils.e("-----complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                LogUtils.e("-----onNext" + s);
            }
        });
    }

    /**
     * from()创建符可以从一个列表/数组来创建Observable,并一个接一个的从列表/数组中发射出来每一个对象。
     * 或者也可以从Java Future类来创建Observable，并发射Future对象的.get()方法返回的结果值。
     * 传入Future作为参数时,我们可以指定一个超时的值。Observable将等待来自Future的结果；
     * 如果在超时之前仍然没有结果返回，Observable将会触发onError()方法通知观察者有错误发生了。
     */
    public void from() {
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add(i);
        }
        subscription = Observable.from(datas).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e("------from" + integer);
            }
        });
    }

    /**
     * 需要一个Observable但是什么都不需要发射
     */
    public void empty() {
        Observable.empty();
    }

    /**
     * 构造一个不会发射任何数据但是永远都不会结束的observable
     */
    public void never() {
        Observable.never();
    }

    /**
     * 被订阅者只有被订阅的时候才会发出数据,而不是初始化的时候就发出数据
     * http://www.jianshu.com/p/c83996149f5b
     */
    public void defer() {
        Observable observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("weixinjie");
            }
        });

        //模拟延时订阅
        try {
            Thread.sleep(2000);
            subscription = observable.subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    LogUtils.e(s);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
