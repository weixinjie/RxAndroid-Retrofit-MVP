package com.will.custom_rxandroid.ui.custom_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.will.custom_rxandroid.R;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomViewActivity extends AppCompatActivity {

    @BindView(R.id.click_view)
    ClickView clickView;
    @BindView(R.id.bt_clear)
    Button bt_clear;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_clear})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_clear:
                clickView.clearText();
                break;
        }
    }
}
