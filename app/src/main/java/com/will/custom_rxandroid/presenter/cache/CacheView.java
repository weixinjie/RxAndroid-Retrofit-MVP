package com.will.custom_rxandroid.presenter.cache;

import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BaseView;

import java.util.List;

/**
 * Created by will on 16/9/9.
 */

public interface CacheView extends BaseView {
    void show_error(String message);

    void show_success_view();

    void load_data_success(List<GankBean> data, String detail_message);

    void stop_refresh();

}
