package com.will.custom_rxandroid.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.ui.drawable_test.DrawableActivity;
import com.will.custom_rxandroid.ui.exception.ANRActivity;
import com.will.custom_rxandroid.ui.download.DownLoadByThreadPoolActivity;
import com.will.custom_rxandroid.ui.exception.OOMActivity;
import com.will.custom_rxandroid.ui.glide.GlideActivity;
import com.will.custom_rxandroid.ui.http.HttpActivity;
import com.will.custom_rxandroid.ui.pull_to_refresh.PullToRefreshActivity;
import com.will.custom_rxandroid.ui.rx.cache.CacheActivity;
import com.will.custom_rxandroid.ui.custom_view.CustomViewActivity;
import com.will.custom_rxandroid.ui.rx.elementary.ElementaryActivity;
import com.will.custom_rxandroid.ui.rx.map.MapActivity;
import com.will.custom_rxandroid.ui.rx.some_operator.OperatorActivity;
import com.will.custom_rxandroid.ui.rx.subject.SubjectActivity;
import com.will.custom_rxandroid.ui.rx.time.TimeActivity;
import com.will.custom_rxandroid.ui.rx.token.TokenActivity;
import com.will.custom_rxandroid.ui.rx.token_avanced.TokenAvancedActivity;
import com.will.custom_rxandroid.ui.rx.zip.ZipActivity;
import com.will.custom_rxandroid.ui.slidefinish.SlideFinishActivity;
import com.will.custom_rxandroid.ui.some_layout.CoordinatorLayoutActivity;
import com.will.custom_rxandroid.ui.web.CustomRouterActivity;
import com.will.custom_rxandroid.ui.web.WebActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SlideFinishActivity.class));
        ButterKnife.bind(this);
    }


    @OnClick({R.id.bt_elementary, R.id.bt_map, R.id.bt_zip, R.id.bt_token
            , R.id.bt_token_avanced, R.id.bt_cache, R.id.bt_time, R.id.bt_operator,
            R.id.bt_subject, R.id.bt_customview, R.id.bt_drawable, R.id.bt_custom_pull
            , R.id.bt_http, R.id.bt_oom, R.id.bt_web, R.id.bt_strict, R.id.bt_download, R.id.bt_glide
            , R.id.bt_layout, R.id.bt_export})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_elementary:
                startActivity(new Intent(this, ElementaryActivity.class));
                break;
            case R.id.bt_map:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.bt_zip:
                startActivity(new Intent(this, ZipActivity.class));
                break;
            case R.id.bt_token:
                startActivity(new Intent(this, TokenActivity.class));
                break;
            case R.id.bt_token_avanced:
                startActivity(new Intent(this, TokenAvancedActivity.class));
                break;
            case R.id.bt_cache:
                startActivity(new Intent(this, CacheActivity.class));
                break;
            case R.id.bt_time:
                startActivity(new Intent(this, TimeActivity.class));
                break;
            case R.id.bt_operator:
                startActivity(new Intent(this, OperatorActivity.class));
                break;
            case R.id.bt_subject:
                startActivity(new Intent(this, SubjectActivity.class));
                break;
            case R.id.bt_customview:
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
            case R.id.bt_drawable:
                startActivity(new Intent(this, DrawableActivity.class));
                break;
            case R.id.bt_custom_pull:
                startActivity(new Intent(this, PullToRefreshActivity.class));
                break;
            case R.id.bt_http:
                startActivity(new Intent(this, HttpActivity.class));
                break;
            case R.id.bt_oom:
                startActivity(new Intent(this, OOMActivity.class));
                break;
            case R.id.bt_web:
                startActivity(new Intent(this, WebActivity.class));
                break;
            case R.id.bt_strict:
                startActivity(new Intent(this, ANRActivity.class));
                break;
            case R.id.bt_download:
                startActivity(new Intent(this, DownLoadByThreadPoolActivity.class));
                break;
            case R.id.bt_glide:
                startActivity(new Intent(this, GlideActivity.class));
                break;
            case R.id.bt_layout:
                startActivity(new Intent(this, CoordinatorLayoutActivity.class));
                break;
            case R.id.bt_export:
                startActivity(new Intent(this, CustomRouterActivity.class));
                break;
        }
    }

}
