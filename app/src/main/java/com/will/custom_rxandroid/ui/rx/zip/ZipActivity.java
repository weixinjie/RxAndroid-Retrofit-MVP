package com.will.custom_rxandroid.ui.rx.zip;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.zip.ZipAdapter;
import com.will.custom_rxandroid.pojo.map.GankBean;
import com.will.custom_rxandroid.presenter.zip.ZipPresenter;
import com.will.custom_rxandroid.presenter.zip.ZipView;
import com.will.custom_rxandroid.ui.base.BaseActivity;
import com.will.custom_rxandroid.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 有的时候，app 中会需要同时访问不同接口，然后将结果糅合后转为统一的格式后输出
 * （例如将第三方广告 API 的广告夹杂进自家平台返回的数据 List 中）。
 * 这种并行的异步处理比较麻烦，不过用了 zip() 之后就会简单得多
 */
public class ZipActivity extends BaseActivity implements ZipView {

    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.refresh_view)
    XRefreshView refresh_view;
    @BindView(R.id.empty_layout)
    View empty_layout;
    @BindView(R.id.error_layout)
    View error_layout;

    ZipPresenter presenter;
    ZipAdapter adapter;
    LinearLayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        ButterKnife.bind(this);
        adapter = new ZipAdapter(this);

        rc_view.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this, 2);
        rc_view.setLayoutManager(layoutManager);
        refresh_view.setPinnedTime(500);
        //禁止加载更多
//        refresh_view.setAutoLoadMore(true);
//        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this)); //一定要设置尾视图,否则自动加载没有效果(must!!!)
        refresh_view.setPinnedContent(true); //刷新完成之前设置不能让列表滑动
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load_data("装逼");
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                presenter.load_data("我");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_view.startRefresh();
    }

    @Override
    protected void attachView() {
        presenter = new ZipPresenter(this);
    }

    @Override
    protected void detachView() {
        presenter.detachView();
    }


    @Override
    public void load_success(List<GankBean> data) {
        adapter.setData(data);
    }

    @Override
    public void show_error(String message) {
        error_layout.setVisibility(View.VISIBLE);
        refresh_view.setVisibility(View.GONE);
        empty_layout.setVisibility(View.GONE);
        TextView error_text = (TextView) error_layout.findViewById(R.id.tv_error);
        Button error_button = (Button) error_layout.findViewById(R.id.bt_error_reload);
        error_text.setText(message);
        error_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_success_view();
                refresh_view.stopRefresh();
                refresh_view.stopLoadMore();

                refresh_view.startRefresh();
            }
        });
    }

    @Override
    public void show_empty() {
        error_layout.setVisibility(View.GONE);
        empty_layout.setVisibility(View.VISIBLE);
        refresh_view.setVisibility(View.GONE);
    }

    @Override
    public void show_success_view() {
        error_layout.setVisibility(View.GONE);
        empty_layout.setVisibility(View.GONE);
        refresh_view.setVisibility(View.VISIBLE);
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
