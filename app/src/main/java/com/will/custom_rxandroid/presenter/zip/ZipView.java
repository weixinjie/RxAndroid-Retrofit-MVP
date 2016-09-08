package com.will.custom_rxandroid.presenter.zip;

import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.base.BaseView;

import java.util.List;

/**
 * Created by will on 16/9/8.
 */

public interface ZipView extends BaseView {
    void load_success(List<GankBean> data);

    void show_error(String message);

    void show_empty();

    void show_success_view();

    void stop_refresh();

}
