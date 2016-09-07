package com.will.custom_rxandroid.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.ui.elementary.ElementaryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_elementary)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_elementary:
                startActivity(new Intent(this, ElementaryActivity.class));
                break;
        }
    }
}
