package com.will.custom_rxandroid.ui.rx.some_operator;

import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.some_operator.OperatorPresenter;
import com.will.custom_rxandroid.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperatorActivity extends BaseActivity {

    OperatorPresenter operatorPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachView() {
        operatorPresenter = new OperatorPresenter();
    }

    @Override
    protected void detachView() {
        operatorPresenter.detachView();
    }

    @OnClick({R.id.just, R.id.from, R.id.defer,
            R.id.inteveal, R.id.repeat, R.id.repeat_when,
            R.id.buffer, R.id.flatmap, R.id.group_by,
            R.id.map, R.id.scan, R.id.window, R.id.debounce
            , R.id.distinct, R.id.elementat, R.id.filter, R.id.oftype
            , R.id.first, R.id.single, R.id.last, R.id.ignoreelements
            , R.id.sample, R.id.skip, R.id.skiplast, R.id.take,
            R.id.take_first, R.id.take_last, R.id.contact_map,
            R.id.switch_map, R.id.cast, R.id.compare, R.id.combine_latest
            , R.id.join, R.id.group_join, R.id.merge, R.id.mergeDelayError
            , R.id.start_with, R.id.switch_on_next, R.id.zip
            , R.id.onerror_return, R.id.on_error_resume_next, R.id.retry,
            R.id.retry_when, R.id.delay, R.id.do_sth, R.id.materialize, R.id.thread
            , R.id.time_out, R.id.using, R.id.time_interval, R.id.time_stamp, R.id.all
            , R.id.exists, R.id.contains, R.id.sequence_equal, R.id.is_empty, R.id.amb
            , R.id.switch_if_empty, R.id.default_if_empty, R.id.take_until, R.id.take_while, R.id.skip_until, R.id.skip_while, R.id.reduce
            , R.id.collect, R.id.concat, R.id.count, R.id.to_list, R.id.to_sort_list, R.id.to_map, R.id.to_multi_map, R.id.publish
            , R.id.replay
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.just:
                operatorPresenter.just();
                break;
            case R.id.from:
                operatorPresenter.from();
                break;
            case R.id.defer:
                operatorPresenter.defer();
                break;
            case R.id.inteveal:
                operatorPresenter.interval();
                break;
            case R.id.repeat:
                operatorPresenter.repeat();
                break;
            case R.id.repeat_when:
                operatorPresenter.repeat_when();
                break;
            case R.id.buffer:
                operatorPresenter.buffer();
                break;
            case R.id.flatmap:
                operatorPresenter.flatmap();
                break;
            case R.id.group_by:
                operatorPresenter.group_by();
                break;
            case R.id.map:
                operatorPresenter.map();
                break;
            case R.id.scan:
                operatorPresenter.scan();
                break;
            case R.id.window:
                operatorPresenter.window();
                break;
            case R.id.debounce:
                operatorPresenter.debounce();
                break;
            case R.id.distinct:
                operatorPresenter.distinct();
                break;
            case R.id.elementat:
                operatorPresenter.elementat();
                break;
            case R.id.filter:
                operatorPresenter.filter();
                break;
            case R.id.oftype:
                operatorPresenter.oftype();
                break;
            case R.id.first:
                operatorPresenter.first();
                break;
            case R.id.single:
                operatorPresenter.single();
                break;
            case R.id.last:
                operatorPresenter.last();
                break;
            case R.id.ignoreelements:
                operatorPresenter.ignoreElements();
                break;
            case R.id.sample:
                operatorPresenter.sample();
                break;
            case R.id.skip:
                operatorPresenter.skip();
                break;
            case R.id.skiplast:
                operatorPresenter.skipLast();
                break;
            case R.id.take:
                operatorPresenter.take();
                break;
            case R.id.take_first:
                operatorPresenter.takeFirst();
                break;
            case R.id.take_last:
                operatorPresenter.takeLast();
                break;
            case R.id.contact_map:
                operatorPresenter.concatMap();
                break;
            case R.id.switch_map:
                operatorPresenter.switchMap();
                break;
            case R.id.cast:
                operatorPresenter.cast();
                break;
            case R.id.compare:
                operatorPresenter.compare();
                break;
            case R.id.combine_latest:
                operatorPresenter.combineLatest();
                break;
            case R.id.join:
                operatorPresenter.join();
                break;
            case R.id.group_join:
                operatorPresenter.group_join();
                break;
            case R.id.merge:
                operatorPresenter.merge();
                break;
            case R.id.mergeDelayError:
                operatorPresenter.mergeDelayError();
                break;
            case R.id.start_with:
                operatorPresenter.startWith();
                break;
            case R.id.switch_on_next:
                operatorPresenter.switchOnNext();
                break;
            case R.id.zip:
                operatorPresenter.zip();
                break;
            case R.id.onerror_return:
                operatorPresenter.onErrorReturn();
                break;
            case R.id.on_error_resume_next:
                operatorPresenter.onErrorResumeNext();
                break;
            case R.id.retry:
                operatorPresenter.retry();
                break;
            case R.id.retry_when:
                operatorPresenter.retryWhen();
                break;
            case R.id.delay:
                operatorPresenter.delay();
                break;
            case R.id.do_sth:
                operatorPresenter.do_sth();
                break;
            case R.id.materialize:
                operatorPresenter.materialize();
                break;
            case R.id.thread:
                operatorPresenter.thread();
                break;
            case R.id.time_out:
                operatorPresenter.timeOut();
                break;
            case R.id.using:
                operatorPresenter.using();
                break;
            case R.id.time_interval:
                operatorPresenter.timeInterval();
                break;
            case R.id.time_stamp:
                operatorPresenter.timeStamp();
                break;
            case R.id.all:
                operatorPresenter.all();
                break;
            case R.id.exists:
                operatorPresenter.exists();
                break;
            case R.id.contains:
                operatorPresenter.contains();
                break;
            case R.id.sequence_equal:
                operatorPresenter.sequenceEqual();
                break;
            case R.id.is_empty:
                operatorPresenter.isEmpty();
                break;
            case R.id.amb:
                operatorPresenter.amb();
                break;
            case R.id.switch_if_empty:
                operatorPresenter.switchIfEmpty();
                break;
            case R.id.default_if_empty:
                operatorPresenter.defaultIfEmpty();
                break;
            case R.id.take_until:
                operatorPresenter.takeUntil();
                break;
            case R.id.take_while:
                operatorPresenter.takeWhile();
                break;
            case R.id.skip_until:
                operatorPresenter.skipUntil();
                break;
            case R.id.skip_while:
                operatorPresenter.skipWhile();
                break;
            case R.id.reduce:
                operatorPresenter.reduce();
                break;
            case R.id.collect:
                operatorPresenter.collect();
                break;
            case R.id.concat:
                operatorPresenter.concat();
                break;
            case R.id.count:
                operatorPresenter.count();
                break;
            case R.id.to_list:
                operatorPresenter.toList();
                break;
            case R.id.to_sort_list:
                operatorPresenter.toSortList();
                break;
            case R.id.to_map:
                operatorPresenter.toMap();
                break;
            case R.id.to_multi_map:
                operatorPresenter.toMultiMap();
                break;
            case R.id.publish:
                operatorPresenter.publish_();
                break;
            case R.id.replay:
                operatorPresenter.replay();
                break;
        }
    }
}
