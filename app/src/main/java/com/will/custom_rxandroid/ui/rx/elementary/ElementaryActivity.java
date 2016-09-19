package com.will.custom_rxandroid.ui.rx.elementary;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.will.custom_rxandroid.BaseApp;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerAdapter;
import com.will.custom_rxandroid.presenter.elementary.ElementaryPresenter;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.elementary.ElementaryAdapter;
import com.will.custom_rxandroid.pojo.elementary.ZhuangBiImage;
import com.will.custom_rxandroid.presenter.elementary.ElementaryView;
import com.will.custom_rxandroid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Rxjava与Retrofit的基本使用
 */
public class ElementaryActivity extends BaseActivity implements ElementaryView {

    @BindView(R.id.et_base)
    EditText et_base;
    @BindView(R.id.bt_base_search)
    Button bt_base_search;
    @BindView(R.id.xf_base)
    XRefreshView xf_base;
    @BindView(R.id.rv_base)
    RecyclerView rv_base;

    ElementaryAdapter elementaryAdapter;
    ElementaryPresenter presenter;
    GridLayoutManager layoutManager;
    private int current_page = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);

        elementaryAdapter = new ElementaryAdapter(this);
        rv_base.setAdapter(elementaryAdapter);
        layoutManager = new GridLayoutManager(this, 2);
        rv_base.setLayoutManager(layoutManager);
        xf_base.setPinnedTime(500);
        xf_base.setAutoLoadMore(true);
        elementaryAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this)); //一定要设置尾视图,否则自动加载没有效果(must!!!)
        xf_base.setPinnedContent(true); //刷新完成之前设置不能让列表滑动
        xf_base.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                String text = et_base.getText().toString().trim();
                current_page = 0;
                presenter.search(text, true, current_page);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                //注意:本api没有分页的功能,所以上拉加载请求的数据与下拉刷新是一样的
                String text = et_base.getText().toString().trim();
                current_page += 1;
                presenter.search(text, false, current_page);
            }
        });

        elementaryAdapter.setOnItemClickListener(new Custom_BaseRecyclerAdapter.OnItemClickListener<ZhuangBiImage>() {
            @Override
            public void onItemClick(View view, int position, ZhuangBiImage model) {
                ToastUtils.showToast("position-" + position + "model.getDesc-" + model.getDescription());
            }

            @Override
            public void onItemLongClick(View view, int position, ZhuangBiImage model) {

            }
        });

    }

    @Override
    protected void attachView() {
        presenter = new ElementaryPresenter(this);
    }

    @Override
    protected void detachView() {
        presenter.detachView();
    }

    @OnClick(R.id.bt_base_search)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_base_search:
                xf_base.startRefresh();
                break;
        }
    }

    @Override
    public void refresh_success(List<ZhuangBiImage> data) {
        elementaryAdapter.setData(data);
    }

    @Override
    public void loadmore_success(List<ZhuangBiImage> data) {
        elementaryAdapter.addItemLast(data);
    }

    @Override
    public void set_loadmore_complete(boolean is_complete) {
        xf_base.setLoadComplete(is_complete);
    }

    @Override
    public void show_error(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void show_empty() {
        ToastUtils.showToast("empty");
    }

    @Override
    public void stop_refresh() {
        xf_base.stopRefresh();
    }

    @Override
    public void stop_loadmore() {
        xf_base.stopLoadMore();
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
