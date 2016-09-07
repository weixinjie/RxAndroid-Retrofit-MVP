package com.will.custom_rxandroid.presenter.elementary;

import com.will.custom_rxandroid.pojo.ZhuangBiImage;
import com.will.custom_rxandroid.presenter.base.BaseView;

import java.util.List;

/**
 * Created by will on 16/9/7.
 */

public interface ElementaryView extends BaseView {
    void refresh_success(List<ZhuangBiImage> data);

    void loadmore_success(List<ZhuangBiImage> data);

    void set_loadmore_complete(boolean is_complete);

    void show_error(String message);

    void show_empty();

    void stop_refresh();

    void stop_loadmore();
}
