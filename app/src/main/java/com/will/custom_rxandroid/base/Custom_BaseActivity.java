package com.will.custom_rxandroid.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.http.BaseApi;
import com.will.custom_rxandroid.pojo.ZhuangBiImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Custom_BaseActivity extends com.will.custom_rxandroid.BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.et_base)
    EditText et_base;
    @BindView(R.id.bt_base_search)
    Button bt_base_search;
    @BindView(R.id.sr_base)
    SwipeRefreshLayout sr_base;
    @BindView(R.id.lv_base)
    ListView lv_base;

    Custom_BaseAdapter baseAdapter;
    List<ZhuangBiImage> zhuangBiImages;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        zhuangBiImages = new ArrayList<>();
        baseAdapter = new Custom_BaseAdapter(this);
        baseAdapter.setData(zhuangBiImages);
        sr_base.setOnRefreshListener(this);
        lv_base.setAdapter(baseAdapter);
    }

    @OnClick(R.id.bt_base_search)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_base_search:
                sr_base.setRefreshing(true);
                break;
        }
    }


    Observer<List<ZhuangBiImage>> observer = new Observer<List<ZhuangBiImage>>() {
        @Override
        public void onCompleted() {
            sr_base.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getApplicationContext(), "出错了", Toast.LENGTH_SHORT).show();

        }

        public void onNext(List<ZhuangBiImage> zhuangBiImage) {
            zhuangBiImages = zhuangBiImage;
            baseAdapter.clear();
            baseAdapter.setData(zhuangBiImage);
            baseAdapter.notifyDataSetChanged();

        }
    };

    @Override
    public void onRefresh() {
        String search = et_base.getText().toString().trim();
        subscription = BaseApi.getZhuangBiApi().search(search).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
