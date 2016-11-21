package com.will.custom_rxandroid.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.ui.drawable_test.DrawableActivity;
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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    List<Bitmap> bitmaps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bitmaps = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            bitmaps.add(bitmap);
        }
    }

    @OnClick({R.id.bt_elementary, R.id.bt_map, R.id.bt_zip, R.id.bt_token
            , R.id.bt_token_avanced, R.id.bt_cache, R.id.bt_time, R.id.bt_operator,
            R.id.bt_subject, R.id.bt_customview, R.id.bt_drawable, R.id.bt_custom_pull})
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
        }
    }
}
