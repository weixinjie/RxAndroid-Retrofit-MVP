package com.will.custom_rxandroid.ui.download;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.utils.LogUtils;
import com.will.custom_rxandroid.utils.ToastUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.functions.Action1;

/**
 * 进行http的多线程下载(采用了线程池)
 */
public class DownLoadByThreadPoolActivity extends AppCompatActivity {

    File file;
    String download_url = "http://d3.yqdown.net/y9/hdd/mdyyg.apk";

    URL url;
    HttpURLConnection urlConnection = null;
    int fileSize = 0; //需要下载的文件长度
    ExecutorService downPool;
    int threadCount = 9; //默认的下载线程为6
    DownLoadProgress progress; //负责监听下载进度的接口
    int down_size_all = 0; //当前下载的总长度

    TextView tv_progress;
    RxPermissions rxPermissions = null; //用来获取6.0权限的RxPermissions

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            int current_progress = (int) msg.obj;

            float temp = (float) current_progress
                    / (float) fileSize;

            tv_progress.setText((int) (temp * 100) + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        rxPermissions = new RxPermissions(this);

        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    ToastUtils.showToast(String.valueOf(aBoolean));
                } else {
                    ToastUtils.showToast(String.valueOf(aBoolean));
                }
            }
        });

        tv_progress = (TextView) findViewById(R.id.tv_progress);

        progress = new DownLoadProgress();
        new Thread() {
            @Override
            public void run() {
                downPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                file = getCacheFile();
                try {
                    url = new URL(download_url);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    fileSize = urlConnection.getContentLength();

                    long down_size_for_everythread = (fileSize % threadCount) == 0 ? fileSize / threadCount
                            : fileSize / threadCount + 1;

                    for (int i = 1; i <= threadCount; i++) {
                        CustomDownRunnable customDownRunnable = new CustomDownRunnable(url,
                                String.valueOf(i), down_size_for_everythread * (i - 1), down_size_for_everythread * i - 1, file, progress);
                        downPool.submit(customDownRunnable);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private File getCacheFile() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        LogUtils.e("path " + filePath);
        File file = new File(filePath + "/weixin.apk");
        return file;
    }

    class DownLoadProgress implements IDownLoadProgress {

        @Override
        public void onDownLoad(String threadID, int progress) {
            add(threadID, progress);

        }

        /**
         * 一定注意，多线程条件下，这个方法一定要经过同步
         *
         * @param threadID
         * @param progress
         */
        private synchronized void add(String threadID, int progress) {
            down_size_all += progress;
            Message msg = handler.obtainMessage();
            msg.obj = down_size_all;
            handler.sendMessage(msg);
            LogUtils.e("-----threadID " + threadID + " end " + down_size_all + "-------总共 " + fileSize);
        }
    }
}
