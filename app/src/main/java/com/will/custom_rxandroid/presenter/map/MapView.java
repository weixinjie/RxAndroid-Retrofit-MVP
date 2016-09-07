package com.will.custom_rxandroid.presenter.map;

import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BaseView;

import java.util.List;

/**
 * Created by will on 16/9/7.
 */

public interface MapView extends BaseView {
    void refresh_success(List<GankBean> data);

    void loadmore_success(List<GankBean> data);

    void set_loadmore_complete(boolean is_complete);

    void show_error(String message);

    void show_empty();

    void stop_refresh();

    void stop_loadmore();
}
