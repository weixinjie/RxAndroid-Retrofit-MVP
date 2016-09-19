package com.will.custom_rxandroid.ui;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.presenter.cache.CachePresenter;
import com.will.custom_rxandroid.ui.cache.CacheActivity;
import com.will.custom_rxandroid.ui.elementary.ElementaryActivity;
import com.will.custom_rxandroid.ui.map.MapActivity;
import com.will.custom_rxandroid.ui.some_operator.OperatorActivity;
import com.will.custom_rxandroid.ui.subject.SubjectActivity;
import com.will.custom_rxandroid.ui.time.TimeActivity;
import com.will.custom_rxandroid.ui.token.TokenActivity;
import com.will.custom_rxandroid.ui.token_avanced.TokenAvancedActivity;
import com.will.custom_rxandroid.ui.token_avanced.TokenAvancedActivity_ViewBinding;
import com.will.custom_rxandroid.ui.zip.ZipActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_elementary, R.id.bt_map, R.id.bt_zip, R.id.bt_token
            , R.id.bt_token_avanced, R.id.bt_cache, R.id.bt_time, R.id.bt_operator, R.id.bt_subject})
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
        }
    }
}
