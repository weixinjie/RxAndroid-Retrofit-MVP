package com.will.custom_rxandroid.ui.web;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.will.custom_rxandroid.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomRouterActivity extends AppCompatActivity {

    @BindView(R.id.tv_test)
    TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_router);
        ButterKnife.bind(this);

        Uri data = getIntent().getData();
        String scheme = data.getScheme(); // "http"
        try {
            String host = data.getHost(); // "twitter.com"
            List<String> params = data.getPathSegments();
            String first = params.get(0); // "status"
            Log.e("--------", first);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("------", scheme);
        tv_test.setText(scheme);
    }
}
