package com.will.custom_rxandroid.ui.map;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.map.MapAdapter;
import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.map.MapPresenter;
import com.will.custom_rxandroid.presenter.map.MapView;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity implements MapView {

    public static int PAGE_COUNT = 6;

    @BindView(R.id.refresh_view)
    XRefreshView refresh_view;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.empty_layout)
    View empty_layout;
    @BindView(R.id.error_layout)
    View error_layout;

    MapPresenter presenter;
    MapAdapter adapter;

    GridLayoutManager layoutManager;

    int current_page = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        adapter = new MapAdapter(this);
        rc_view.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this, 2);
        rc_view.setLayoutManager(layoutManager);
        refresh_view.setPinnedTime(500);
        refresh_view.setAutoLoadMore(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this)); //一定要设置尾视图,否则自动加载没有效果(must!!!)
        refresh_view.setPinnedContent(true); //刷新完成之前设置不能让列表滑动
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                current_page = 1;
                presenter.load_data(PAGE_COUNT, current_page, true);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                current_page += 1;
                presenter.load_data(PAGE_COUNT, current_page, false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_view.startRefresh();
    }

    private void setView(boolean show_list, boolean show_error, boolean show_empty) {
        if (show_list) {
            refresh_view.setVisibility(View.VISIBLE);
        } else {
            refresh_view.setVisibility(View.GONE);
        }
        if (show_error) {
            error_layout.setVisibility(View.VISIBLE);
        } else {
            error_layout.setVisibility(View.GONE);
        }
        if (show_empty) {
            empty_layout.setVisibility(View.VISIBLE);
        } else {
            empty_layout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void attachView() {
        presenter = new MapPresenter(this);
    }

    @Override
    protected void detachView() {
        presenter.detachView();
    }

    @Override
    public void refresh_success(List<GankBean> data) {
        setView(true, false, false);
        adapter.setData(data);
    }

    @Override
    public void loadmore_success(List<GankBean> data) {
        setView(true, false, false);
        adapter.addItemLast(data);
    }

    @Override
    public void set_loadmore_complete(boolean is_complete) {
        refresh_view.setLoadComplete(is_complete);
    }

    @Override
    public void show_error(String message) {
        setView(false, true, false);
        TextView error_text = (TextView) error_layout.findViewById(R.id.tv_error);
        Button error_button = (Button) error_layout.findViewById(R.id.bt_error_reload);
        error_text.setText(message);
        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView(true, false, false);
                current_page = 1;
                refresh_view.startRefresh();
            }
        });
    }

    @Override
    public void show_empty() {
        setView(false, false, true);
    }

    @Override
    public void stop_refresh() {
        refresh_view.stopRefresh();
    }

    @Override
    public void stop_loadmore() {
        refresh_view.stopLoadMore();
    }

    @Override
    public void show_toast(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void show_loading() {
        ToastUtils.showToast("loading...");
    }

    @Override
    public void hide_loading() {
        ToastUtils.showToast("hide loading...");
    }
}
