package com.will.custom_rxandroid.presenter.some_operator;


import com.andview.refreshview.utils.LogUtils;
import com.will.custom_rxandroid.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

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

    /**
     * 发送一个可观测的序列(根据初始值跟数量)
     */
    public void interval() {
        subscription = Observable.range(1, 5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * 多次发送同一组数据(repeat(int)中的int值为重复几次,并且没有时间延迟)
     */
    public void repeat() {
        subscription = Observable.range(1, 5).repeat(5, AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * 同上,不过repeat_when代表的是多次订阅信息
     */
    public void repeat_when() {
        subscription = Observable.range(1, 5).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return Observable.timer(5, TimeUnit.SECONDS);
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    //---------------------------Transforming Observables(以下为变换操作符)--------------------------//

    /**
     * buffer操作符周期性地收集源Observable产生的结果到列表中，并把这个列表提交给订阅者，
     * 订阅者处理后，清空buffer列表，同时接收下一次收集的结果并提交给订阅者，周而复始。
     * 需要注意的是，一旦源Observable在产生结果的过程中出现异常，
     * 即使buffer已经存在收集到的结果，订阅者也会马上收到这个异常
     */
    public void buffer() {
        final String[] messages = new String[]{"baidu", "alibaba", "tencent"};
        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Random random = new Random();
                while (true) {
                    try {
                        String message = messages[random.nextInt(messages.length)];
                        subscriber.onNext(message);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        subscriber.onError(e);
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).buffer(3, TimeUnit.SECONDS).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<String> strings) {
                LogUtils.e(String.valueOf(strings));
            }
        });

        /**
         * 下面的函数没有搞懂,请补充
         */
//        subscription = Observable.range(1, 6).buffer(new Func0<Observable<List<Integer>>>() {
//            @Override
//            public Observable<List<Integer>> call() {
//                List<Integer> data = new ArrayList<Integer>();
//                for (int i = 0; i < 3; i++) {
//                    data.add(i);
//                }
//                return Observable.just(data);
//            }
//        }).subscribe(new Action1<List<Integer>>() {
//            @Override
//            public void call(List<Integer> integers) {
//
//            }
//        });
    }

    /**
     * FlatMap将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable
     */
    public void flatmap() {
        subscription = Observable.just(1, 2, 3, 4, 5, 6).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just(String.valueOf(integer));
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                LogUtils.e(s);
            }
        });
    }

    /**
     * 分组
     */
    public void group_by() {
        subscription = Observable.range(1, 6).groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer % 2;
            }
        }).subscribe(new Subscriber<GroupedObservable<Integer, Integer>>() {
            @Override
            public void onCompleted() {
                LogUtils.e("大的onCommplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {
                integerIntegerGroupedObservable.subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("小的onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.e("group: " + integerIntegerGroupedObservable.getKey() + "number: " + String.valueOf(integer));
                    }
                });
            }
        });
    }

    /**
     * 进行map转化
     */
    public void map() {
        subscription = Observable.just(1, 2, 3, 4, 5).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return String.valueOf("integer: " + integer);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtils.e(s);
            }
        });
    }

    /**
     * 可以对数据进行包裹,并且会把上次返回的数据进行发送
     * 例如:下面call函数中 s为上次返回的字符串 s2为当前的字符串
     * 如果Observable.just中写入的是int值,那么我们可以利用这个函数进行求和运算(只是举个例子)
     */
    public void scan() {
        subscription = Observable.just("weixinjie", "zhangrui").scan(new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                if (s != null)
                    LogUtils.e("-----以前的字符串为" + s);
                if (s2 != null)
                    LogUtils.e("-----当前的字符串为" + s2);
                return s2;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtils.e(s);
            }
        });
    }

    /**
     * Window is similar to Buffer(没搞懂,请补充)
     */
    public void window() {
        subscription = Observable.interval(1, TimeUnit.SECONDS).take(12)
                .window(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> observable) {
                        LogUtils.e("subdivide begin......");
                        observable.subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                LogUtils.e("Next:" + aLong);
                            }
                        });
                    }
                });
    }

    //---------------------------Filtering Observables(以下为过滤操作符)--------------------------//

    /**
     * debounce操作符对源Observable每产生一个结果后，如果在规定的间隔时间内没有别的结果产生，则把这个结果提交给订阅者处理，否则忽略该结果。
     * 值得注意的是，如果源Observable产生的最后一个结果后在规定的时间间隔内调用了onCompleted，那么通过debounce操作符也会把这个结果提交给订阅者。
     */
    public void debounce() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed())
                    return;
                for (int i = 0; i < 10; i++) {
                    try {
                        subscriber.onNext(i);
                        Thread.sleep(i * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .debounce(4, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.e(String.valueOf(integer));
                    }
                });
    }


    /**
     * 对重复的数据进行清理
     */
    public void distinct() {
        subscription = Observable.just(1, 2, 3, 4, 2, 1, 5).distinct()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.e(String.valueOf(integer));
                    }
                });
    }

    /**
     * elementAt操作符在源Observable产生的结果中，仅仅把指定"索引"的结果提交给订阅者，索引是从0开始的
     */
    public void elementat() {
        subscription = Observable.just("weixinjie", "zhangrui", "hehe").elementAt(2).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtils.e(s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, new Action0() {
            @Override
            public void call() {
                LogUtils.e("onComplete");
            }
        });
    }

    /**
     * filter操作符是对源Observable产生的结果按照指定条件进行过滤，只有满足条件的结果才会提交给订阅者，
     */
    public void filter() {
        subscription = Observable.just("baidu", "tencent", "alibaba").filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                if (s.contains("ali"))
                    return true;
                return false;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtils.e(s);
            }
        });
    }

    /**
     * ofType操作符类似于filter操作符，区别在于ofType操作符是按照类型对结果进行过滤
     */
    public void oftype() {
        subscription = Observable.just("baidu", 1, "tencent", 1.888).ofType(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtils.e(s);
            }
        });
    }

    /**
     * first操作符是把源Observable产生的结果的第一个提交给订阅者，first操作符可以使用elementAt(0)和take(1)替代
     * 需要注意的是如果first取不到数据则会抛出异常NoSuchElement
     */
    public void first() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        })
                .first()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtils.e(String.valueOf(integer));
                    }
                });
    }

    /**
     * last操作符把源Observable产生的结果的最后一个提交给订阅者，last操作符可以使用takeLast(1)替代。
     */
    public void last() {
        subscription = Observable.range(1, 6).last().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * single操作符是对源Observable的结果进行判断，
     * 如果产生的结果满足指定条件的数量不为1，则抛出异常，否则把满足条件的结果提交给订阅者
     */
    public void single() {
        subscription = Observable.just(1, 2, 3, 4, 5, 6, 6).single(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 3 && integer > 2;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * gnoreElements操作符忽略所有源Observable产生的结果，只把Observable的onCompleted和onError事件通知给订阅者。
     * ignoreElements操作符适用于不太关心Observable产生的结果，只是在Observable结束时(onCompleted)或者出现错误时能够收到通知。
     */
    public void ignoreElements() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(3);
                subscriber.onError(new Throwable("custom error"));
            }
        }).ignoreElements().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * sample操作符定期扫描源Observable产生的结果，在指定的时间间隔范围内对源Observable产生的结果进行采样
     * 即达到sample指定的时间后才会发送数据
     */
    public void sample() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 0; i < 9; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(1000);
                    }
                    Thread.sleep(2000);
                    subscriber.onNext(9);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).sample(2200, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Integer>() {
                               @Override
                               public void onCompleted() {
                                   LogUtils.e("onComplete");
                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(Integer integer) {
                                   LogUtils.e(String.valueOf(integer));
                               }
                           }

                );
    }

    /**
     * skip操作符针对源Observable产生的结果，跳过前面n个不进行处理，而把后面的结果提交给订阅者处理
     */
    public void skip() {
        subscription = Observable.just(1, 2, 3, 4, 5, 6).skip(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * skipLast操作符针对源Observable产生的结果，忽略Observable最后产生的n个结果，而把前面产生的结果提交给订阅者处理，
     * 值得注意的是，skipLast操作符提交满足条件的结果给订阅者是存在延迟效果的
     */
    public void skipLast() {
        subscription = Observable.range(1, 6).skipLast(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * take操作符是把源Observable产生的结果，提取前面的n个提交给订阅者，而忽略后面的结果
     */
    public void take() {
        subscription = Observable.range(1, 6).take(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * takeFirst操作符类似于take操作符，同时也类似于first操作符，
     * 都是获取源Observable产生的结果列表中符合指定条件的前一个，
     * 与first操作符不同的是，first操作符如果获取不到数据，则会抛出NoSuchElementException异常，
     * 而takeFirst则会返回一个空的Observable，该Observable只有onCompleted通知而没有onNext通知。
     */
    public void takeFirst() {
        subscription = Observable.range(1, 6).takeFirst(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
//                if (integer > 3 && integer < 4) return true;
                if (integer > 2) return true;
                return false;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * takeLast操作符是把源Observable产生的结果的后n项提交给订阅者，提交时机是Observable发布onCompleted通知之时
     */
    public void takeLast() {
        subscription = Observable.range(1, 6).takeLast(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }
}
