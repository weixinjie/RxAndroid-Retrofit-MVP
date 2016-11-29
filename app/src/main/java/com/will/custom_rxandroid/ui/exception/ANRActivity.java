package com.will.custom_rxandroid.ui.exception;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.utils.FileUtils;
import com.will.custom_rxandroid.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 测试严格模式，Application中开启的设置
 */
public class ANRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        LogUtils.e("path " + filePath);
        File file = new File(filePath + "/weixin.apk");

        for (int i = 0; i < 10; i++) {
            File newFile = new File(filePath + "/weixin" + i + ".apk");
            FileUtils.copyfile(file, newFile, true);
        }
    }


}
