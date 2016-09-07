package com.will.custom_rxandroid.presenter.map;


import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.pojo.map.GankResult;

import java.util.List;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by will on 16/9/7.
 */

public class GankResult2GankBeanUtils implements Func1<GankResult, List<GankBean>> {
    @Override
    public List<GankBean> call(GankResult gankResult) {
        boolean is_error = gankResult.error;
        if (!is_error) {
            List<GankBean> data = gankResult.beauties;
            return data;
        } else {
            throw Exceptions.propagate(new Throwable("数据错误"));
        }
    }
}
