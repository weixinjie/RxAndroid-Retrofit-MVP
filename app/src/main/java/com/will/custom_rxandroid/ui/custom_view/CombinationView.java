package com.will.custom_rxandroid.ui.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.will.custom_rxandroid.R;

/**
 * Created by will on 16/9/19.
 */

public class CombinationView extends FrameLayout {
    Button back;
    TextView content;

    public CombinationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        back = (Button) findViewById(R.id.bt_back);
        content = (TextView) findViewById(R.id.tv_content);
    }

    public void setLeftClick(OnClickListener onClickListener) {
        back.setOnClickListener(onClickListener);
    }

    public void setContent(String text) {
        content.setText(text);
    }

    public void setLeftText(String text) {
        back.setText(text);
    }
}
