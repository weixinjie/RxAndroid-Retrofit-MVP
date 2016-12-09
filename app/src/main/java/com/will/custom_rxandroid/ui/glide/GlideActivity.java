package com.will.custom_rxandroid.ui.glide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.will.custom_rxandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlideActivity extends AppCompatActivity {

    @BindView(R.id.iv_test)
    ImageView iv_test;

    String image_path = "http://img.masala-sg.goldenmob.com/img/12d0282a5a18962ac5d16e43b077fe74/i_0_579a07ed19a7928072016185605";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        ButterKnife.bind(this);

        Glide.with(this).load(image_path).asGif().fitCenter().error(R.mipmap.ic_launcher).into(iv_test);
    }
}
