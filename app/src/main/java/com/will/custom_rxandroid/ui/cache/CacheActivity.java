package com.will.custom_rxandroid.ui.cache;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.map.MapAdapter;
import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.cache.CachePresenter;
import com.will.custom_rxandroid.presenter.cache.CacheView;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CacheActivity extends BaseActivity implements CacheView {
    @BindView(R.id.tv_load_message)
    TextView tv_load_message;
    @BindView(R.id.bt_clear_memory)
    Button bt_clear_memory;
    @BindView(R.id.bt_clear_disk)
    Button bt_clear_disk;
    @BindView(R.id.refresh_view)
    XRefreshView refresh_view;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.empty_layout)
    View empty_layout;
    @BindView(R.id.error_layout)
    View error_layout;

    CachePresenter presenter;
    MapAdapter adapter;

    GridLayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        ButterKnife.bind(this);

        adapter = new MapAdapter(this);
        rc_view.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this, 2);
        rc_view.setLayoutManager(layoutManager);
        refresh_view.setPinnedTime(500);
        refresh_view.setPinnedContent(true); //刷新完成之前设置不能让列表滑动
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load_data();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_view.startRefresh();
    }

    @OnClick({R.id.bt_clear_memory, R.id.bt_clear_disk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_clear_memory:
                presenter.clear_memory();
                break;
            case R.id.bt_clear_disk:
                presenter.clear_disk();
                break;
        }
    }

    @Override

    protected void attachView() {
        presenter = new CachePresenter(this);
    }

    @Override
    protected void detachView() {
        presenter.detachView();
    }

    @Override
    public void show_error(String message) {
        error_layout.setVisibility(View.VISIBLE);
        empty_layout.setVisibility(View.GONE);
        refresh_view.setVisibility(View.GONE);
        TextView error_text = (TextView) error_layout.findViewById(R.id.tv_error);
        Button error_button = (Button) error_layout.findViewById(R.id.bt_error_reload);
        error_text.setText(message);
        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_success_view();
                refresh_view.startRefresh();
            }
        });
    }

    @Override
    public void show_success_view() {
        error_layout.setVisibility(View.GONE);
        empty_layout.setVisibility(View.GONE);
        refresh_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void load_data_success(List<GankBean> data, String detail_message) {
        adapter.setData(data);
        tv_load_message.setText(detail_message);
    }

    @Override
    public void stop_refresh() {
        refresh_view.stopRefresh();
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
