package com.will.custom_rxandroid.presenter.some_operator;

import android.app.AlertDialog;

import com.will.custom_rxandroid.presenter.base.BasePresenter;
import com.will.custom_rxandroid.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

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
        subscription = Observable.from(datas).subscribe(

                new Action1<Integer>() {
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
     * flatMap操作符是把Observable产生的结果转换成多个Observable，然后把这多个Observable“扁平化”成一个Observable，并依次提交产生的结果给订阅者。
     * flatMap操作符通过传入一个函数作为参数转换源Observable，在这个函数中，
     * 你可以自定义转换规则，最后在这个函数中返回一个新的Observable，
     * 然后flatMap操作符通过合并这些Observable结果成一个Observable，并依次提交结果给订阅者。
     * 值得注意的是，flatMap操作符在合并Observable结果时，有可能存在交叉的情况
     */
    public void flatmap() {
        subscription = Observable.just(1, 2, 3, 4, 5, 6).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just(String.valueOf("加入flatmap label后: " + integer));
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
     * cancatMap操作符与flatMap操作符类似，都是把Observable产生的结果转换成多个Observable，
     * 然后把这多个Observable“扁平化”成一个Observable，并依次提交产生的结果给订阅者。
     * 与flatMap操作符不同的是，concatMap操作符在处理产生的Observable时，
     * 采用的是“连接(concat)”的方式，而不是“合并(merge)”的方式，这就能保证产生结果的顺序性，
     * 也就是说提交给订阅者的结果是按照顺序提交的，不会存在交叉的情况。
     */
    public void concatMap() {
        subscription = Observable.just(1, 2, 3, 4, 5, 6).concatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just(String.valueOf("加入label: " + integer));
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
     * switchMap操作符与flatMap操作符类似，都是把Observable产生的结果转换成多个Observable，
     * 然后把这多个Observable“扁平化”成一个Observable，并依次提交产生的结果给订阅者。
     * 与flatMap操作符不同的是，switchMap操作符会保存最新的Observable产生的结果而舍弃旧的结果，
     * 举个例子来说，比如源Observable产生A、B、C三个结果，通过switchMap的自定义映射规则，
     * 映射后应该会产生A1、A2、B1、B2、C1、C2，但是在产生B2的同时，C1已经产生了，这样最后的结果就变成A1、A2、B1、C1、C2，B2被舍弃掉了！
     */
    public void switchMap() {
        subscription = Observable.just(1, 2, 3, 4, 5, 6).switchMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just(String.valueOf("add lable: " + integer));
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtils.e(s);
            }
        });
    }

    /**
     * 上面三种运算符的比较(下面为log输出)
     * switchMap Next:30
     * 09-13 17:56:58.824 1646-1646/com.will.custom_rxandroid I/System.out: switchMap Next:30
     * 09-13 17:56:58.824 1646-1646/com.will.custom_rxandroid I/System.out: switchMap Next:15
     * 09-13 17:56:58.859 1646-1646/com.will.custom_rxandroid E/OperatorPresenter$19.call(L:348): concatMap: 10
     * 09-13 17:56:58.860 1646-1646/com.will.custom_rxandroid E/OperatorPresenter$19.call(L:348): concatMap: 5
     * 09-13 17:56:59.045 1646-1646/com.will.custom_rxandroid E/OperatorPresenter$19.call(L:348): concatMap: 20
     * 09-13 17:56:59.046 1646-1646/com.will.custom_rxandroid E/OperatorPresenter$19.call(L:348): concatMap: 10
     * 09-13 17:56:59.222 1646-1646/com.will.custom_rxandroid E/OperatorPresenter$19.call(L:348): concatMap: 30
     * 09-13 17:56:59.222 1646-1646/com.will.custom_rxandroid E/OperatorPresenter$19.call(L:348): concatMap: 15
     * 09-13 17:57:00.437 1646-1646/com.will.custom_rxandroid I/System.out: flatMap Next:20
     * 09-13 17:57:00.437 1646-1646/com.will.custom_rxandroid I/System.out: flatMap Next:30
     * 09-13 17:57:00.437 1646-1646/com.will.custom_rxandroid I/System.out: flatMap Next:15
     * 09-13 17:57:00.437 1646-1646/com.will.custom_rxandroid I/System.out: flatMap Next:10
     * 09-13 17:57:00.635 1646-1646/com.will.custom_rxandroid I/System.out: flatMap Next:10
     * 09-13 17:57:00.635 1646-1646/com.will.custom_rxandroid I/System.out: flatMap Next:5
     */
    public void compare() {
        //flatMap操作符的运行结果
        Observable.just(10, 20, 30).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 2000;
                if (integer > 10)
                    delay = 1800;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("flatMap Next:" + integer);
            }
        });

        //concatMap操作符的运行结果
        Observable.just(10, 20, 30).concatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer > 10)
                    delay = 180;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e("concatMap: " + integer);
            }
        });

        //switchMap操作符的运行结果
        Observable.just(10, 20, 30).switchMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer > 10)
                    delay = 180;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("switchMap Next:" + integer);
            }
        });
    }

    /**
     * groupBy操作符是对源Observable产生的结果进行分组，形成一个类型为GroupedObservable的结果集，
     * GroupedObservable中存在一个方法为getKey()，可以通过该方法获取结果集的Key值（类似于HashMap的key)。
     * 值得注意的是，由于结果集中的GroupedObservable是把分组结果缓存起来，
     * 如果对每一个GroupedObservable不进行处理（既不订阅执行也不对其进行别的操作符运算），
     * 就有可能出现内存泄露。因此，如果你对某个GroupedObservable不进行处理，最好是对其使用操作符take(0)处理。
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
     * map操作符是把源Observable产生的结果，通过映射规则转换成另一个结果集，并提交给订阅者进行处理。
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
     * cast操作符类似于map操作符，不同的地方在于map操作符可以通过自定义规则，
     * 把一个值A1变成另一个值A2，A1和A2的类型可以一样也可以不一样；而cast操作符主要是做类型转换的，
     * 传入参数为类型class，如果源Observable产生的结果不能转成指定的class，则会抛出ClassCastException运行时异常。
     */
    public void cast() {
        subscription = Observable.just(1, 2, 3).cast(Integer.class).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * 可以对数据进行包裹,并且会把上次返回的数据进行发送
     * 例如:下面call函数中 s为上次返回的字符串 s2为当前的字符串
     * 如果Observable.just中写入的是int值,那么我们可以利用这个函数进行求和运算(只是举个例子)
     * <p>
     * scan操作符通过遍历源Observable产生的结果，
     * 依次对每一个结果项按照指定规则进行运算，计算后的结果作为下一个迭代项参数，
     * 每一次迭代项都会把计算结果输出给订阅者。
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
     * window操作符非常类似于buffer操作符，
     * 区别在于buffer操作符产生的结果是一个List缓存，
     * 而window操作符产生的结果是一个Observable，订阅者可以对这个结果Observable重新进行订阅处理。
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
                .debounce(2, TimeUnit.SECONDS)
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

    //---------------------------Combining Observables(Observable的组合操作符)--------------------------//

    /**
     * combineLatest操作符把两个Observable产生的结果进行合并，合并的结果组成一个新的Observable。
     * 这两个Observable中任意一个Observable产生的结果，
     * 都和另一个Observable最后产生的结果，按照一定的规则进行合并。(合并结果的数量为数量多的Observable)
     * 图解:http://reactivex.io/documentation/operators/combinelatest.html
     * <p>
     * 09-18 09:28:42.018 2576-2752/com.will.custom_rxandroid E/----: -----aLong=0aLong2=0
     * 09-18 09:28:42.018 2576-2752/com.will.custom_rxandroid E/----: 0
     * 09-18 09:28:43.013 2576-2751/com.will.custom_rxandroid E/----: -----aLong=1aLong2=0
     * 09-18 09:28:43.013 2576-2751/com.will.custom_rxandroid E/----: 1
     * 09-18 09:28:45.018 2576-2752/com.will.custom_rxandroid E/----: -----aLong=1aLong2=2
     * 09-18 09:28:45.018 2576-2752/com.will.custom_rxandroid E/----: 3
     * 09-18 09:28:48.018 2576-2752/com.will.custom_rxandroid E/----: -----aLong=1aLong2=4
     * 09-18 09:28:48.018 2576-2752/com.will.custom_rxandroid E/----: 5
     * 09-18 09:28:51.018 2576-2752/com.will.custom_rxandroid E/----: -----aLong=1aLong2=6
     * 09-18 09:28:51.018 2576-2752/com.will.custom_rxandroid E/----: 7
     * 09-18 09:28:51.018 2576-2752/com.will.custom_rxandroid E/----: onComplete
     */
    public void combineLatest() {
        Observable observable1 = Observable.interval(2, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.just(aLong);
            }
        }).take(2);

        Observable observable2 = Observable.interval(3, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.just(aLong * 2);
            }
        }).take(4);

        subscription = Observable.combineLatest(observable1, observable2, new Func2<Long, Long, Long>() {

            @Override
            public Long call(Long aLong, Long aLong2) {
                LogUtils.e("-----aLong=" + aLong + "aLong2=" + aLong2);
                return aLong + aLong2;
            }
        }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Long o) {
                LogUtils.e(String.valueOf(o));
            }
        });
    }

    /**
     * join操作符把类似于combineLatest操作符，也是两个Observable产生的结果进行合并，合并的结果组成一个新的Observable，
     * 但是join操作符可以控制每个Observable产生结果的生命周期，在每个结果的生命周期内，可以与另一个Observable产生的结果按照一定的规则进行合并
     * <p>
     * 09-14 11:03:44.432 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$64.call(L:985): aLong=0 aLong2=0
     * 09-14 11:03:44.432 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$61.onNext(L:1001): 0
     * 09-14 11:03:44.920 23789-23894/com.will.custom_rxandroid E/OperatorPresenter$64.call(L:985): aLong=5 aLong2=0
     * 09-14 11:03:44.920 23789-23894/com.will.custom_rxandroid E/OperatorPresenter$61.onNext(L:1001): 5
     * 09-14 11:03:46.427 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$64.call(L:985): aLong=10 aLong2=10
     * 09-14 11:03:46.427 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$61.onNext(L:1001): 20
     * 09-14 11:03:46.921 23789-23894/com.will.custom_rxandroid E/OperatorPresenter$64.call(L:985): aLong=15 aLong2=10
     * 09-14 11:03:46.921 23789-23894/com.will.custom_rxandroid E/OperatorPresenter$61.onNext(L:1001): 25
     * 09-14 11:03:48.427 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$64.call(L:985): aLong=20 aLong2=20
     * 09-14 11:03:48.427 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$61.onNext(L:1001): 40
     * 09-14 11:03:48.427 23789-23895/com.will.custom_rxandroid E/OperatorPresenter$61.onCompleted(L:991): onCompleted
     */
    public void join() {
        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);

        Observable<Long> observable2 = Observable.interval(500, 2000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(3);


        subscription = observable1.join(observable2,
                new Func1<Long, Observable<Long>>() {  //控制observable1的生命周期,这里是延时600mm发送
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
                    }
                }, new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) { //控制observable2的生命周期,这里是延时600mm发送
                        return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
                    }
                }, new Func2<Long, Long, Long>() { //控制observable1与observable2的合并规则
                    @Override
                    public Long call(Long aLong, Long aLong2) {
                        LogUtils.e("aLong=" + aLong + " aLong2=" + aLong2);
                        return aLong + aLong2;
                    }
                }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Long o) {
                LogUtils.e(String.valueOf(o));
            }
        });
    }

    /**
     * groupJoin操作符非常类似于join操作符，区别在于join操作符中第四个参数的传入函数不一致
     * 通过下面的日志可以看出,分组的数量为observable1(源数据)的发送次数
     * <p>
     * 09-14 11:42:23.076 25687-25775/com.will.custom_rxandroid E/----: along=0 along2=0
     * 09-14 11:42:23.076 25687-25775/com.will.custom_rxandroid E/----: 0
     * 09-14 11:42:23.575 25687-25774/com.will.custom_rxandroid E/----: along=5 along2=0
     * 09-14 11:42:23.575 25687-25774/com.will.custom_rxandroid E/----: 5
     * 09-14 11:42:24.578 25687-25776/com.will.custom_rxandroid E/----: child onCompleted
     * 09-14 11:42:25.076 25687-25775/com.will.custom_rxandroid E/----: along=5 along2=10
     * 09-14 11:42:25.076 25687-25775/com.will.custom_rxandroid E/----: 15
     * 09-14 11:42:25.076 25687-25775/com.will.custom_rxandroid E/----: along=10 along2=10
     * 09-14 11:42:25.076 25687-25775/com.will.custom_rxandroid E/----: 20
     * 09-14 11:42:25.576 25687-25774/com.will.custom_rxandroid E/----: along=15 along2=10
     * 09-14 11:42:25.576 25687-25774/com.will.custom_rxandroid E/----: 25
     * 09-14 11:42:25.578 25687-25774/com.will.custom_rxandroid E/----: child onCompleted
     * 09-14 11:42:26.577 25687-25775/com.will.custom_rxandroid E/----: child onCompleted
     * 09-14 11:42:27.079 25687-25775/com.will.custom_rxandroid E/----: along=20 along2=20
     * 09-14 11:42:27.079 25687-25775/com.will.custom_rxandroid E/----: 40
     * 09-14 11:42:27.079 25687-25775/com.will.custom_rxandroid E/----: along=15 along2=20
     * 09-14 11:42:27.079 25687-25775/com.will.custom_rxandroid E/----: 35
     * 09-14 11:42:27.576 25687-25786/com.will.custom_rxandroid E/----: child onCompleted
     * 09-14 11:42:28.580 25687-25774/com.will.custom_rxandroid E/----: child onCompleted
     * 09-14 11:42:33.075 25687-25775/com.will.custom_rxandroid E/----: parent onComplete
     */
    public void group_join() {

        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);

        Observable<Long> observable2 = Observable.interval(500, 2000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(6);

        observable1.groupJoin(observable2, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.just(aLong).delay(2, TimeUnit.SECONDS);
            }
        }, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.just(aLong).delay(1, TimeUnit.SECONDS);
            }
        }, new Func2<Long, Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(final Long aLong, Observable<Long> longObservable) {
                return longObservable.map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long along2) {
                        LogUtils.e("along=" + aLong + " along2=" + along2);
                        return aLong + along2;
                    }
                });
            }
        }).subscribe(new Subscriber<Observable<Long>>() {
            @Override
            public void onCompleted() {
                LogUtils.e("parent onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Observable<Long> longObservable) {
                longObservable.subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("child onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.e(String.valueOf(aLong));
                    }
                });
            }
        });
    }

    /**
     * merge操作符是按照两个Observable提交结果的时间顺序，对Observable进行合并。
     * 如ObservableA每隔500毫秒产生数据为0,5,10,15,20；
     * 而ObservableB每隔500毫秒产生数据0,10,20,30,40，其中第一个数据延迟500毫秒产生，最后合并结果为：0,0,5,10,10,20,15,30,20,40
     * <p>
     * 09-14 11:55:56.085 5437-5564/com.will.custom_rxandroid E/----: 第一个数列已经产生0
     * 09-14 11:55:56.085 5437-5564/com.will.custom_rxandroid E/----: 0
     * 09-14 11:55:56.583 5437-5565/com.will.custom_rxandroid E/----: 第二个数列已经产生0
     * 09-14 11:55:56.583 5437-5565/com.will.custom_rxandroid E/----: 0
     * 09-14 11:55:57.082 5437-5564/com.will.custom_rxandroid E/----: 第一个数列已经产生5
     * 09-14 11:55:57.082 5437-5564/com.will.custom_rxandroid E/----: 5
     * 09-14 11:55:57.082 5437-5565/com.will.custom_rxandroid E/----: 第二个数列已经产生10
     * 09-14 11:55:57.082 5437-5565/com.will.custom_rxandroid E/----: 10
     * 09-14 11:55:57.583 5437-5565/com.will.custom_rxandroid E/----: 第二个数列已经产生20
     * 09-14 11:55:57.583 5437-5565/com.will.custom_rxandroid E/----: 20
     * 09-14 11:55:58.082 5437-5564/com.will.custom_rxandroid E/----: 第一个数列已经产生10
     * 09-14 11:55:58.082 5437-5564/com.will.custom_rxandroid E/----: 10
     * 09-14 11:55:58.084 5437-5565/com.will.custom_rxandroid E/----: 第二个数列已经产生30
     * 09-14 11:55:58.084 5437-5565/com.will.custom_rxandroid E/----: 30
     * 09-14 11:55:58.582 5437-5565/com.will.custom_rxandroid E/----: 第二个数列已经产生40
     * 09-14 11:55:58.583 5437-5565/com.will.custom_rxandroid E/----: 40
     * 09-14 11:55:59.082 5437-5564/com.will.custom_rxandroid E/----: 第一个数列已经产生15
     * 09-14 11:55:59.083 5437-5564/com.will.custom_rxandroid E/----: 15
     * 09-14 11:56:00.081 5437-5564/com.will.custom_rxandroid E/----: 第一个数列已经产生20
     * 09-14 11:56:00.081 5437-5564/com.will.custom_rxandroid E/----: 20
     */
    public void merge() {
        Observable<Long> observable1 = Observable.timer(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        long result = aLong * 5;
                        LogUtils.e("第一个数列已经产生" + result);
                        return result;
                    }
                }).take(5);

        Observable<Long> observable2 = Observable.timer(500, 500, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        long result = aLong * 10;
                        LogUtils.e("第二个数列已经产生" + result);
                        return aLong * 10;
                    }
                }).take(5);

        Observable.merge(observable1, observable2).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LogUtils.e(String.valueOf(aLong));
            }
        });
    }


    /**
     * 从merge操作符的流程图可以看出，一旦合并的某一个Observable中出现错误，
     * 就会马上停止合并，并对订阅者回调执行onError方法，
     * 而mergeDelayError操作符会把错误放到所有结果都合并完成之后才执行
     * 而对于已经出现错误的observable则会停止发送数据,只会最后发送error
     * <p>
     * 09-14 14:09:33.381 23321-23321/com.will.custom_rxandroid E/----: 第一个数列已经产生0
     * 09-14 14:09:33.382 23321-23321/com.will.custom_rxandroid E/----: 0
     * 09-14 14:09:34.383 23321-23321/com.will.custom_rxandroid E/----: 第一个数列已经产生1
     * 09-14 14:09:34.383 23321-23321/com.will.custom_rxandroid E/----: 1
     * 09-14 14:09:35.385 23321-23321/com.will.custom_rxandroid E/----: 第一个数列已经产生2
     * 09-14 14:09:35.385 23321-23321/com.will.custom_rxandroid E/----: 2
     * 09-14 14:09:36.889 23321-23974/com.will.custom_rxandroid E/----: 第二个数列已经产生0
     * 09-14 14:09:36.889 23321-23974/com.will.custom_rxandroid E/----: 0
     * 09-14 14:09:37.888 23321-23974/com.will.custom_rxandroid E/----: 第二个数列已经产生10
     * 09-14 14:09:37.888 23321-23974/com.will.custom_rxandroid E/----: 10
     * 09-14 14:09:38.888 23321-23974/com.will.custom_rxandroid E/----: 第二个数列已经产生20
     * 09-14 14:09:38.888 23321-23974/com.will.custom_rxandroid E/----: 20
     * 09-14 14:09:39.890 23321-23974/com.will.custom_rxandroid E/----: 第二个数列已经产生30
     * 09-14 14:09:39.890 23321-23974/com.will.custom_rxandroid E/----: 30
     * 09-14 14:09:40.888 23321-23974/com.will.custom_rxandroid E/----: 第二个数列已经产生40
     * 09-14 14:09:40.888 23321-23974/com.will.custom_rxandroid E/----: 40
     * 09-14 14:09:40.888 23321-23974/com.will.custom_rxandroid W/System.err: java.lang.Throwable: custom error
     */
    public void mergeDelayError() {
        Observable<Long> observable1 = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                for (int i = 0; i < 3; i++) {
                    LogUtils.e("第一个数列已经产生" + i);
                    subscriber.onNext(Long.parseLong(String.valueOf(i)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onError(new Throwable("custom error"));
            }
        });

        Observable<Long> observable2 = Observable.timer(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        long result = aLong * 10;
                        LogUtils.e("第二个数列已经产生" + result);
                        return aLong * 10;
                    }
                }).take(5);

        Observable.mergeDelayError(observable1, observable2).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Long aLong) {
                LogUtils.e(String.valueOf(aLong));
            }
        });
    }

    /**
     * startWith操作符是在源Observable提交结果之前，插入指定的某些数据
     * <p>
     * 09-18 09:34:21.453 2576-2576/com.will.custom_rxandroid E/----: 5555
     * 09-18 09:34:21.453 2576-2576/com.will.custom_rxandroid E/----: 1
     * 09-18 09:34:21.453 2576-2576/com.will.custom_rxandroid E/----: 2
     * 09-18 09:34:21.453 2576-2576/com.will.custom_rxandroid E/----: 3
     * 09-18 09:34:21.453 2576-2576/com.will.custom_rxandroid E/----: 4
     */
    public void startWith() {
        subscription = Observable.just(1, 2, 3, 4).startWith(5555).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * switchOnNext操作符是把一组Observable转换成一个Observable，转换规则为：
     * 对于这组Observable中的每一个Observable所产生的结果，
     * 如果在同一个时间内存在两个或多个Observable提交的结果，
     * 只取最后一个Observable提交的结果给订阅者
     */
    public void switchOnNext() {
        Observable<Observable<Long>> observable = Observable
                .interval(500, TimeUnit.MILLISECONDS) //每隔2S产生一个Observable
                .map(new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable
                                .interval(200, TimeUnit.MILLISECONDS) //每隔1S产生一个数据
                                .map(new Func1<Long, Long>() {
                                    @Override
                                    public Long call(Long aLong) {
                                        return aLong * 10;
                                    }
                                }).take(5);
                    }
                }).take(2);


        subscription = Observable
                .switchOnNext(observable)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.e(String.valueOf(aLong));
                    }
                });
    }

    /**
     * zip操作符是把两个observable提交的结果，严格按照顺序进行合并(这里的顺序是指产生的顺序),产生的数量为合并中数量较少的一方
     * <p>
     * 09-18 09:38:27.475 11167-11278/com.will.custom_rxandroid E/----: along: 0 along2: 0
     * 09-18 09:38:27.475 11167-11278/com.will.custom_rxandroid E/----: 0
     * 09-18 09:38:29.474 11167-11278/com.will.custom_rxandroid E/----: along: 5 along2: 10
     * 09-18 09:38:29.474 11167-11278/com.will.custom_rxandroid E/----: 15
     * 09-18 09:38:31.475 11167-11278/com.will.custom_rxandroid E/----: along: 10 along2: 20
     * 09-18 09:38:31.476 11167-11278/com.will.custom_rxandroid E/----: 30
     * 09-18 09:38:33.476 11167-11278/com.will.custom_rxandroid E/----: along: 15 along2: 30
     * 09-18 09:38:33.476 11167-11278/com.will.custom_rxandroid E/----: 45
     * 09-18 09:38:35.475 11167-11278/com.will.custom_rxandroid E/----: along: 20 along2: 40
     * 09-18 09:38:35.475 11167-11278/com.will.custom_rxandroid E/----: 60
     */
    public void zip() {
        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);

        Observable<Long> observable2 = Observable.interval(0, 2000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(6);

        subscription = Observable.zip(observable1, observable2, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                LogUtils.e("along: " + aLong + " along2: " + aLong2);
                return aLong + aLong2;
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LogUtils.e(String.valueOf(aLong));
            }
        });
    }

    //---------------------------Error Handling Operators(Observable的错误处理操作符)--------------------------//

    /**
     * onErrorReturn操作符是在Observable发生错误或异常的时候（即将回调oError方法时），
     * 拦截错误并执行指定的逻辑，返回一个跟源Observable相同类型的结果，最后回调订阅者的onComplete方法
     * <p>
     * 09-14 14:59:34.739 3699-3699/com.will.custom_rxandroid E/----: 0
     * 09-14 14:59:34.739 3699-3699/com.will.custom_rxandroid E/----: 1
     * 09-14 14:59:34.739 3699-3699/com.will.custom_rxandroid E/----: 2
     * 09-14 14:59:34.739 3699-3699/com.will.custom_rxandroid E/----: 3
     * 09-14 14:59:34.740 3699-3699/com.will.custom_rxandroid E/----: 502
     * 09-14 14:59:34.740 3699-3699/com.will.custom_rxandroid E/----: onComplete
     */
    public void onErrorReturn() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 0; i < 5; i++) {
                        if (i == 4) {
                            throw new Exception("custom error");
                        }
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).onErrorReturn(new Func1<Throwable, Integer>() {
            @Override
            public Integer call(Throwable throwable) {
                return 502;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * onErrorResumeNext操作符跟onErrorReturn类似，只不过onErrorReturn只能在错误或异常发生时只返回一个和源Observable相同类型的结果，
     * 而onErrorResumeNext操作符是在错误或异常发生时返回一个Observable，也就是说可以返回多个和源Observable相同类型的结果
     * <p>
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 0
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 1
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 2
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 3
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 301
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 404
     * 09-14 15:12:25.076 14706-14706/com.will.custom_rxandroid E/----: 502
     */
    public void onErrorResumeNext() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 0; i < 5; i++) {
                        if (i == 4) {
                            throw new Exception("custom error");
                        }
                        subscriber.onNext(i);
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends Integer>>() {
            @Override
            public Observable<? extends Integer> call(Throwable throwable) {
                return Observable.just(301, 404, 502);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * onExceptionResumeNext操作符和onErrorResumeNext操作符类似，
     * 不同的地方在于onErrorResumeNext操作符是当Observable发生错误或异常时触发，而onExceptionResumeNext是当Observable发生异常时才触发。
     * 这里要普及一个概念就是，java的异常分为错误（error）和异常（exception）两种，它们都是继承于Throwable类。
     * 错误（error）一般是比较严重的系统问题，比如我们经常遇到的OutOfMemoryError、StackOverflowError等都是错误。
     * 错误一般继承于Error类，而Error类又继承于Throwable类，如果需要捕获错误，需要使用try..catch(Error e)或者try..catch(Throwable e)句式。使用try..catch(Exception e)句式无法捕获错误
     * 异常（Exception）也是继承于Throwable类，一般是根据实际处理业务抛出的异常，分为运行时异常（RuntimeException）和普通异常。
     * 普通异常直接继承于Exception类，如果方法内部没有通过try..catch句式进行处理，必须通过throws关键字把异常抛出外部进行处理（即checked异常）；
     * 而运行时异常继承于RuntimeException类，如果方法内部没有通过try..catch句式进行处理，不需要显式通过throws关键字抛出外部，如IndexOutOfBoundsException、NullPointerException、ClassCastException等都是运行时异常，
     * 当然RuntimeException也是继承于Exception类，因此是可以通过try..catch(Exception e)句式进行捕获处理的。
     */
    public void onExceptionResumeNext() {

    }

    /**
     * retry操作符是当Observable发生错误或者异常时，重新尝试执行Observable的逻辑，
     * 如果经过n次重新尝试执行后仍然出现错误或者异常，
     * 则最后回调执行onError方法；当然如果源Observable没有错误或者异常出现，则按照正常流程执行。
     * <p>
     * 09-14 15:36:26.114 684-684/com.will.custom_rxandroid E/----: 0
     * 09-14 15:36:26.114 684-684/com.will.custom_rxandroid E/----: 1
     * 09-14 15:36:26.114 684-684/com.will.custom_rxandroid E/----: 0
     * 09-14 15:36:26.115 684-684/com.will.custom_rxandroid E/----: 1
     * 09-14 15:36:26.115 684-684/com.will.custom_rxandroid E/----: 0
     * 09-14 15:36:26.115 684-684/com.will.custom_rxandroid E/----: 1
     * 09-14 15:36:26.115 684-684/com.will.custom_rxandroid E/----: 0
     * 09-14 15:36:26.115 684-684/com.will.custom_rxandroid E/----: 1
     * 09-14 15:36:26.115 684-684/com.will.custom_rxandroid W/System.err: java.lang.Exception: custom error
     */
    public void retry() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 0; i < 3; i++) {
                        if (i == 2) {
                            throw new Exception("custom error");
                        }
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).retry(3).subscribe(new Subscriber<Integer>() {
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
     * retryWhen操作符类似于retry操作符，都是在源observable出现错误或者异常时，
     * 重新尝试执行源observable的逻辑，不同在于retryWhen操作符是在源Observable出现错误或者异常时，
     * 通过回调第二个Observable来判断是否重新尝试执行源Observable的逻辑，
     * 如果第二个Observable没有错误或者异常出现，则就会重新尝试执行源Observable的逻辑，否则就会直接回调执行订阅者的onError方法。
     * <p>
     * 09-14 17:24:25.065 32014-32014/com.will.custom_rxandroid E/----: 开始订阅
     * 09-14 17:24:25.065 32014-32014/com.will.custom_rxandroid E/----: 2333
     * <p>(如果retryWhen中的Func1中的call方法中改成  subscriber.onError(new Throwable("retry when error"));)
     * E/----: onError
     * W/System.err: java.lang.Throwable: retry when error
     */
    int result = -1;

    public void retryWhen() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                LogUtils.e("开始订阅");
                if (result > 0) {
                    subscriber.onNext(2333);
                } else {
                    subscriber.onError(new Throwable("call error"));
                }

            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
//                        result = 100;
                        subscriber.onError(new Throwable("retry when error"));
                    }
                });
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    //---------------------------Observable Utility Operators(Observable的功能性操作符)--------------------------//

    /**
     * 顾名思义，Delay操作符就是让发射数据的时机延后一段时间，这样所有的数据都会依次延后一段时间发射。
     * 在Rxjava中将其实现为Delay和DelaySubscription。不同之处在于Delay是延时数据的发射，
     * 而DelaySubscription是延时注册Subscriber。
     */
    public void delay() {
        final long current_time = System.currentTimeMillis();
        subscription = Observable
                .just(1, 2, 3).delay(2, TimeUnit.SECONDS)
                .delaySubscription(4, TimeUnit.SECONDS).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtils.e(String.valueOf(integer));
                        LogUtils.e("use time: " + String.valueOf(System.currentTimeMillis() - current_time) + "mm");
                    }
                });
    }

    /**
     * Do操作符就是给Observable的生命周期的各个阶段加上一系列的回调监听，
     * 当Observable执行到这个阶段的时候，这些回调就会被触发。在Rxjava实现了很多的doxxx操作符。
     */
    public void do_sth() {
        subscription = Observable.range(1, 6)
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        LogUtils.e("DoOnEach可以给Observable加上这样的样一个回调：" +
                                "Observable每发射一个数据的时候就会触发这个回调，" +
                                "不仅包括onNext还包括onError和onCompleted。");
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtils.e("DoOnNext则只有onNext的时候才会被触发。");
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.e("doOnSubscribe在Subscriber进行订阅的时候触发");
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.e("doOnUnsubscribe在解除订阅的时候触发," +
                                "当一个Observable通过OnError或者OnCompleted结束的时候，会反订阅所有的Subscriber。 ");
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e("DoOnError会在OnError发生的时候触发回调，" +
                                "并将Throwable对象作为参数传进回调函数里；");
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.e("DoOnComplete会在OnCompleted发生的时候触发回调。 ");
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.e("DoOnTerminate会在Observable结束前触发回调，无论是正常还是异常终止");
                    }
                })
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
                        LogUtils.e("onNext: " + integer);
                    }
                });
    }

    /**
     * Meterialize操作符将OnNext/OnError/OnComplete都转化为一个Notification对象并按照原来的顺序发射出来，
     * 而DeMeterialize则是执行相反的过程。
     */
    public void materialize() {
        subscription = Observable.range(1, 6)
                .materialize()
                .subscribe(new Subscriber<Notification<Integer>>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Notification<Integer> integerNotification) {
                        LogUtils.e("onNext: " + String.valueOf(integerNotification.getValue()));
                    }
                });
    }


    /**
     * TimeInterval会拦截发射出来的数据，取代为前后两个发射两个数据的间隔时间。对于第一个发射的数据，其时间间隔为订阅后到首次发射的间隔。
     * <p>
     * 09-17 22:03:29.160 6572-6645/com.will.custom_rxandroid E/----: timeInterval: 0times: 2
     * 09-17 22:03:30.163 6572-6645/com.will.custom_rxandroid E/----: timeInterval: 1times: 1003
     * 09-17 22:03:32.164 6572-6645/com.will.custom_rxandroid E/----: timeInterval: 2times: 2002
     * 09-17 22:03:35.167 6572-6645/com.will.custom_rxandroid E/----: timeInterval: 3times: 3003
     * 09-17 22:03:39.169 6572-6645/com.will.custom_rxandroid E/----: timeInterval: 4times: 4002
     */
    public void timeInterval() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(i * 1000);
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .timeInterval()
                .subscribe(new Action1<TimeInterval<Integer>>() {
                    @Override
                    public void call(TimeInterval<Integer> integerTimeInterval) {
                        LogUtils.e("timeInterval: " + integerTimeInterval.getValue() + "times: " + integerTimeInterval.getIntervalInMilliseconds());
                    }
                });
    }

    /**
     * TimeStamp会将每个数据项给重新包装一下，加上了一个时间戳来标明每次发射的时间
     * <p>
     * 09-17 22:11:09.407 11665-11792/com.will.custom_rxandroid E/----: currentTime: 1474121469406
     * 09-17 22:11:09.407 11665-11792/com.will.custom_rxandroid E/----: timeStamp: 2  timeStampMillis: 1474121469406
     * 09-17 22:11:12.409 11665-11792/com.will.custom_rxandroid E/----: currentTime: 1474121472409
     * 09-17 22:11:12.409 11665-11792/com.will.custom_rxandroid E/----: timeStamp: 3  timeStampMillis: 1474121472409
     * 09-17 22:11:16.410 11665-11792/com.will.custom_rxandroid E/----: currentTime: 1474121476410
     * 09-17 22:11:16.410 11665-11792/com.will.custom_rxandroid E/----: timeStamp: 4  timeStampMillis: 1474121476410
     */
    public void timeStamp() {
        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 2; i < 5; i++) {
                        Thread.sleep(i * 1000);
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .timestamp()
                .subscribe(new Action1<Timestamped<Integer>>() {
                    @Override
                    public void call(Timestamped<Integer> integerTimestamped) {
                        LogUtils.e("currentTime: " + System.currentTimeMillis());
                        LogUtils.e("timeStamp: " + integerTimestamped.getValue() + "  timeStampMillis: " + integerTimestamped.getTimestampMillis());
                    }
                });
    }

    /**
     * subscrieOn用来指定被观察者的线程
     * observeOn用来指定观察者的线程
     */
    public void thread() {
        subscription = Observable.just(1, 3, 5)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        try {
                            Thread.sleep(2000);
                            LogUtils.e("current thread: " + Thread.currentThread().getName());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return integer;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtils.e("current thread:" + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * Timeout操作符给Observable加上超时时间，每发射一个数据后就重置计时器，
     * 当超过预定的时间还没有发射下一个数据，就抛出一个超时的异常。
     * Rxjava将Timeout实现为很多不同功能的操作符，
     * 比如说超时后用一个备用的Observable继续发射数据等。
     * <p>
     * 09-17 20:49:13.477 15425-19675/com.will.custom_rxandroid E/----: 500
     * 09-17 20:49:13.477 15425-19675/com.will.custom_rxandroid E/----: onComplete
     */
    public void timeOut() {
        subscription = Observable.range(1, 5)
                .delay(3, TimeUnit.SECONDS)
                .timeout(1, TimeUnit.SECONDS,
                        Observable.just(500))
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
     * Using操作符创建一个在Observable生命周期内存活的资源，
     * 也可以这样理解：我们创建一个资源并使用它，
     * 用一个Observable来限制这个资源的使用时间，当这个Observable终止的时候，这个资源就会被销毁。
     * <p>
     * 09-17 21:46:20.798 23652-23725/com.will.custom_rxandroid E/----: func0
     * 09-17 21:46:20.798 23652-23725/com.will.custom_rxandroid E/----: func1
     * 09-17 21:46:22.800 23652-23652/com.will.custom_rxandroid E/----: People{name='weixinjie'}
     * (点击了返回按钮,subscription调用了unsubscribe函数之后打印如下)
     * 09-17 21:46:26.821 23652-23652/com.will.custom_rxandroid E/----: action1
     */
    public void using() {
        subscription = Observable.using(new Func0<People>() {
            @Override
            public People call() {
                LogUtils.e("func0");
                return new People("weixinjie");
            }
        }, new Func1<People, Observable<People>>() {
            @Override
            public Observable<People> call(final People people) {
                LogUtils.e("func1");
                return Observable.create(new Observable.OnSubscribe<People>() {
                    @Override
                    public void call(Subscriber<? super People> subscriber) {
                        try {
                            Thread.sleep(2000);
                            subscriber.onNext(people);
                        } catch (InterruptedException e) {
                            subscriber.onError(e);
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, new Action1<People>() {
            @Override
            public void call(People people) {
                LogUtils.e("action1");
                people = null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<People>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(People people) {
                        LogUtils.e(String.valueOf(people));
                    }
                });
    }

    class People {
        private String name;

        public People(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "People{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    //---------------------------Conditional and Boolean Operators(条件/布尔操作)--------------------------//

    /**
     * 判断所有的数据项是否满足某个条件，内部通过OperatorAll实现。
     * <p>
     * 09-17 22:24:48.364 23874-23874/com.will.custom_rxandroid E/----: true
     * 09-17 22:24:48.364 23874-23874/com.will.custom_rxandroid E/----: onComplete
     */
    public void all() {
        subscription = Observable.range(1, 6).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer > 0;
            }
        }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                LogUtils.e(String.valueOf(aBoolean));
            }
        });
    }

    /**
     * 过滤所有数据,判断是否存在数据项满足某个条件。内部通过OperatorAny实现。
     * <p>
     * 09-17 22:28:28.600 26903-26903/com.will.custom_rxandroid E/----: false
     */
    public void exists() {
        subscription = Observable.just(1, 2, 3).exists(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer > 3;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                LogUtils.e(String.valueOf(aBoolean));
            }
        });
    }

    /**
     * 判断在发射的所有数据项中是否包含指定的数据，内部调用的其实是exists
     * <p>
     * 09-17 22:30:30.691 28951-28951/com.will.custom_rxandroid E/----: true
     */
    public void contains() {
        subscription = Observable.just(1, 2, 3).contains(1).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                LogUtils.e(String.valueOf(aBoolean));
            }
        });
    }

    /**
     * 用于判断两个Observable发射的数据是否相同（数据，发射顺序，终止状态）。
     * <p>
     * 09-17 22:35:46.278 1074-1074/com.will.custom_rxandroid E/----: true
     */
    public void sequenceEqual() {
        subscription = Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3)).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                LogUtils.e(String.valueOf(aBoolean));
            }
        });
    }

    /**
     * 用于判断Observable发射完毕时，有没有发射数据。有数据false，如果只收到了onComplete通知则为true。
     */
    public void isEmpty() {
        subscription = Observable.range(1, 2).isEmpty().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                LogUtils.e(String.valueOf(aBoolean));
            }
        });
    }

    /**
     * 给定多个Observable，只让第一个发射数据的Observable发射全部数据，其他Observable将会被忽略。
     * 经过测试:observable1必须要运行在其他的线程中(不可以运行在主线程)
     */
    public void amb() {
        Observable observable1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Thread.sleep(2000);
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io());


        Observable observable2 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(33);
                subscriber.onCompleted();
            }
        });


        subscription = Observable.amb(observable1, observable2).subscribe(new Subscriber<Integer>() {
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
     * 如果原始Observable正常终止后仍然没有发射任何数据，就使用备用的Observable。
     */
    public void switchIfEmpty() {
        subscription = Observable.empty().switchIfEmpty(Observable.just(2333)).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                LogUtils.e(String.valueOf(o));
            }
        });
    }

    /**
     * 如果原始Observable正常终止后仍然没有发射任何数据，就发射一个默认值,内部调用的switchIfEmpty。
     */
    public void defaultIfEmpty() {
        subscription = Observable.empty().defaultIfEmpty(500).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                LogUtils.e(String.valueOf(o));
            }
        });
    }

    /**
     * 当发射的数据满足某个条件后（包含该数据），或者第二个Observable发送完毕，终止第一个Observable发送数据。
     * <p>
     * 09-18 11:29:14.670 24437-24437/com.will.custom_rxandroid E/----: 0
     * 09-18 11:29:15.667 24437-24437/com.will.custom_rxandroid E/----: 1
     * 09-18 11:29:16.666 24437-24437/com.will.custom_rxandroid E/----: 2
     * 09-18 11:29:17.665 24437-24437/com.will.custom_rxandroid E/----: onComplete
     */
    public void takeUntil() {
        // 1.使用func1来判断
//        subscription = Observable.range(1, 6).takeUntil(new Func1<Integer, Boolean>() {
//            @Override
//            public Boolean call(Integer integer) {
//                return integer > 4;
//            }
//        }).subscribe(new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//                LogUtils.e("onComplete");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                LogUtils.e(String.valueOf(integer));
//            }
//        });

        // 2.第二个Observable发送完毕之后终止第一个Observable发送数据
        subscription = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .takeUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.e(String.valueOf(aLong));
                    }
                });
    }

    /**
     * 当发射的数据不满足某个条件后（不包含该数据），终止Observable发送数据。
     * <p>
     * 09-18 11:30:43.572 26165-26165/com.will.custom_rxandroid E/----: 1
     * 09-18 11:30:43.572 26165-26165/com.will.custom_rxandroid E/----: 2
     * 09-18 11:30:43.572 26165-26165/com.will.custom_rxandroid E/----: 3
     * 09-18 11:30:43.572 26165-26165/com.will.custom_rxandroid E/----: onComplete
     */
    public void takeWhile() {
        subscription = Observable.just(1, 2, 3, 4, 5).takeWhile(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer != 4;
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
            public void onNext(Integer aLong) {
                LogUtils.e(String.valueOf(aLong));
            }
        });
    }

    /**
     * 丢弃Observable发射的数据，直到第二个Observable发送数据。（丢弃条件数据）
     * <p>
     * 09-18 11:36:16.234 28978-29058/com.will.custom_rxandroid E/----: 3
     * 09-18 11:36:17.233 28978-29058/com.will.custom_rxandroid E/----: 4
     * 09-18 11:36:18.234 28978-29058/com.will.custom_rxandroid E/----: 5
     * 09-18 11:36:19.235 28978-29058/com.will.custom_rxandroid E/----: 6
     */
    public void skipUntil() {
        subscription = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .skipUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.e(String.valueOf(aLong));
                    }
                });
    }

    /**
     * 丢弃Observable发射的数据，直到一个指定的条件不成立（不丢弃条件数据）
     * <p>
     * 09-18 11:41:44.395 975-1231/com.will.custom_rxandroid E/----: 4
     * 09-18 11:41:45.393 975-1231/com.will.custom_rxandroid E/----: 5
     * 09-18 11:41:46.394 975-1231/com.will.custom_rxandroid E/----: 6
     * 09-18 11:41:47.393 975-1231/com.will.custom_rxandroid E/----: 7
     * 09-18 11:41:48.394 975-1231/com.will.custom_rxandroid E/----: 8
     * 09-18 11:41:49.394 975-1231/com.will.custom_rxandroid E/----: 9
     */
    public void skipWhile() {
        subscription = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .skipWhile(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong != 4;
                    }
                })
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.e(String.valueOf(aLong));
                    }
                });
    }

    //---------------------------Mathematical and Aggregate Operators(综合操作)--------------------------//

    /**
     * Reduce操作符应用一个函数接收Observable发射的数据和函数的计算结果作为下次计算的参数，输出最后的结果。
     * 跟前面我们了解过的scan操作符很类似，只是scan会输出每次计算的结果，而reduce只会输出最后的结果。
     * <p>
     * 09-18 11:56:47.800 14959-14959/com.will.custom_rxandroid E/----: integer: 1 integer2: 2
     * 09-18 11:56:47.800 14959-14959/com.will.custom_rxandroid E/----: integer: 3 integer2: 3
     * 09-18 11:56:47.801 14959-14959/com.will.custom_rxandroid E/----: 6
     */
    public void reduce() {
        subscription = Observable.range(1, 3).reduce(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                LogUtils.e("integer: " + integer + " integer2: " + integer2);
                return integer + integer2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * Collect操作符类似于Reduce，但是其目的不同，collect用来将源Observable发射的数据给收集到一个数据结构里面，需要使用两个参数：
     * 1.一个产生收集数据结构的函数。
     * 2.一个接收第一个函数产生的数据结构和源Observable发射的数据作为参数的函数。
     * <p>
     * 09-18 12:02:45.243 20130-20130/com.will.custom_rxandroid E/----: [1, 2, 3]
     * 09-18 12:02:45.243 20130-20130/com.will.custom_rxandroid E/----: onCompleted
     */
    public void collect() {
        subscription = Observable.range(1, 3).collect(new Func0<List<Integer>>() {
            @Override
            public List<Integer> call() {
                return new ArrayList<Integer>();
            }
        }, new Action2<List<Integer>, Integer>() {
            @Override
            public void call(List<Integer> integers, Integer integer) {
                integers.add(integer);
            }
        }).subscribe(new Subscriber<List<Integer>>() {
            @Override
            public void onCompleted() {
                LogUtils.e("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Integer> integers) {
                LogUtils.e(String.valueOf(integers));
            }
        });
    }

    /**
     * 09-18 12:52:14.138 24470-24470/com.will.custom_rxandroid E/----: 1
     * 09-18 12:52:14.138 24470-24470/com.will.custom_rxandroid E/----: 2
     */
    public void concat() {
        Observable.concat(Observable.just(1), Observable.just(2)).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    /**
     * 记录Observable发送的数量
     */
    public void count() {
        subscription = Observable.just(1, 3, 4).count().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.e(String.valueOf(integer));
            }
        });
    }

    //---------------------------Operators to Convert Observables(转换操作)--------------------------//

    /**
     * 收集原始Observable发射的所有数据到一个列表，然后返回这个列表.
     * <p>
     * 09-18 13:38:24.607 22740-22740/com.will.custom_rxandroid E/----: [1, 2, 3]
     */
    public void toList() {
        subscription = Observable.range(1, 3).toList().subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                LogUtils.e(String.valueOf(integers));
            }
        });
    }

    /**
     * toSortedList： 收集原始Observable发射的所有数据到一个有序列表，然后返回这个列表。
     * <p>
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 3 integer2: 6
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 5 integer2: 3
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 5 integer2: 3
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 5 integer2: 6
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 7 integer2: 5
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 7 integer2: 6
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 4 integer2: 5
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 4 integer2: 3
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 2 integer2: 5
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: integer: 2 integer2: 3
     * 09-18 13:49:45.142 32464-32464/com.will.custom_rxandroid E/----: [7, 6, 5, 4, 3, 2]
     */
    public void toSortList() {
        subscription = Observable.just(6, 3, 5, 7, 4, 2)
                .toSortedList(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer2 - integer; //integer2-integer代表倒序排序,integer-integer2代表正序排序
                    }
                })
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        LogUtils.e(String.valueOf(integers));
                    }
                });
    }

    /**
     * 将序列数据转换为一个Map。我们可以根据数据项生成key和生成value。
     * <p>
     * 9-18 14:05:00.741 10714-10714/com.will.custom_rxandroid E/----: {key:4=40, key:2=20, key:1=10, key:3=30}
     */
    public void toMap() {
        subscription = Observable.range(1, 4).toMap(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return String.valueOf("key:" + integer); //根据数据项生成的key
            }
        }, new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * 10; //根据数据项生成的value
            }
        }).subscribe(new Action1<Map<String, Integer>>() {
            @Override
            public void call(Map<String, Integer> stringIntegerMap) {
                LogUtils.e(String.valueOf(stringIntegerMap));
            }
        });
    }

    /**
     * 类似于toMap，不同的地方在于map的value是一个集合。
     * <p>
     * {key:4=[value:4], key:2=[value:2], key:1=[value:1], key:3=[value:3]}
     */
    public void toMultiMap() {
        subscription = Observable.range(1, 4).toMultimap(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return String.valueOf("key:" + integer);
            }
        }, new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return String.valueOf("value:" + integer);
            }
        }).subscribe(new Action1<Map<String, Collection<String>>>() {
            @Override
            public void call(Map<String, Collection<String>> stringCollectionMap) {
                LogUtils.e(String.valueOf(stringCollectionMap));
            }
        });
    }

    //---------------------------Connectable Observable Operators(连接操作)--------------------------//

    /**
     * 首先我们有必要来了解一下什么是Connectable Observable: 就是一种特殊的Observable对象，
     * 并不是Subscrib的时候就发射数据，而是只有对其应用connect操作符的时候才开始发射数据，所以可以用来更灵活的控制数据发射的时机。
     * 而Publish操作符就是用来将一个普通的Observable对象转化为一个Connectable Observable。
     * 需要注意的是如果发射数据已经开始了再进行订阅只能接收以后发射的数据。
     * Connect操作符就是用来触发Connectable Observable发射数据的。
     * 应用Connect操作符后会返回一个Subscription对象，通过这个Subscription对象，我们可以调用其unsubscribe方法来终止数据的发射。
     * 另外，如果还没有订阅者订阅的时候就应用Connect操作符也是可以使其开始发射数据的。
     * <p>
     * -------->>>>><<<<<<<-------
     * RefCount操作符就是将一个Connectable Observable 对象再重新转化为一个普通的Observable对象，这时候如果由订阅者进行订阅将会触发数据的发射。
     * -------->>>>><<<<<<<-------
     * <p>
     * 09-18 14:59:51.559 13584-13848/com.will.custom_rxandroid E/----: sleep time: 3000
     * 09-18 14:59:51.579 13584-13902/com.will.custom_rxandroid E/----: 0
     * 09-18 14:59:53.571 13584-13902/com.will.custom_rxandroid E/----: 1
     * 09-18 14:59:54.552 13584-13849/com.will.custom_rxandroid E/----: unsubscribe
     */
    public void publish_() {
        final ConnectableObservable<Long> connectableObservable = Observable
                .interval(0, 2, TimeUnit.SECONDS)
                .publish();

        subscription = connectableObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                LogUtils.e(String.valueOf(aLong));
            }
        });

        //3s后订阅
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    long current_time = System.currentTimeMillis();
                    sleep(3000);
                    LogUtils.e("sleep time: " + (System.currentTimeMillis() - current_time));
                    connectableObservable.connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //6s之后取消订阅,此时不会再打印
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(6000);
                    subscription.unsubscribe();
                    LogUtils.e("unsubscribe");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Replay操作符返回一个Connectable Observable 对象并且可以缓存其发射过的数据，这样即使有订阅者在其发射数据之后进行订阅也能收到其之前发射过的数据。
     * 不过使用Replay操作符我们最好还是限定其缓存的大小，否则缓存的数据太多了可会占用很大的一块内存。对缓存的控制可以从空间和时间两个方面来实现。
     * 下面打印的0 1 2是一下子打印出来的,因为这是缓存的内容
     * 09-18 15:37:38.130 15408-17624/com.will.custom_rxandroid E/----: 0
     * 09-18 15:37:38.130 15408-17624/com.will.custom_rxandroid E/----: 1
     * 09-18 15:37:38.130 15408-17624/com.will.custom_rxandroid E/----: 2
     * 09-18 15:37:39.121 15408-17623/com.will.custom_rxandroid E/----: 3
     */
    public void replay() {
        final ConnectableObservable<Long> connectableObservable = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .replay(2, TimeUnit.SECONDS);
        connectableObservable.connect();
        subscription = connectableObservable
                .delaySubscription(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        LogUtils.e(String.valueOf(aLong));
                    }
                });
    }

}
