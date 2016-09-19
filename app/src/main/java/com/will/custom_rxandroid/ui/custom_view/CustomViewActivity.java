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

/**
 * http://blog.csdn.net/jdsjlzx/article/details/41113969
 * <p>
 * 组合控件的意思就是，我们并不需要自己去绘制视图上显示的内容，而只是用系统原生的控件就好了，但我们可以将几个系统原生的控件组合到一起，这样创建出的控件就被称为组合控件。
 * 举个例子来说，标题栏就是个很常见的组合控件，很多界面的头部都会放置一个标题栏，标题栏上会有个返回按钮和标题，点击按钮后就可以返回到上一个界面。那么下面我们就来尝试去实现这样一个标题栏控件。
 */
public class CustomViewActivity extends AppCompatActivity {

    @BindView(R.id.click_view)
    ClickView clickView;
    @BindView(R.id.bt_clear)
    Button bt_clear;
    @BindView(R.id.combination_view)
    CombinationView combination_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);

        combination_view.setContent("content");
        combination_view.setLeftText("返回");
        combination_view.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
