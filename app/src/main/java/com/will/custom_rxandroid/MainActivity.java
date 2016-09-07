package com.will.custom_rxandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.will.custom_rxandroid.base.Custom_BaseActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_base;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setClick();
    }

    private void findViews() {
        bt_base = (Button) findViewById(R.id.bt_base);
    }

    private void setClick() {
        bt_base.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_base:
                startActivity(new Intent(this, Custom_BaseActivity.class));
                break;
        }
    }
}
