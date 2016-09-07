package com.will.custom_rxandroid.presenter.base;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
