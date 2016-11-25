package com.will.custom_rxandroid.ui.http;

import com.will.custom_rxandroid.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by will on 2016/11/23.
 */

public class CustomDownRunnable implements Runnable {
    private String threadID;
    private long startDownLoadLocation;
    private long endDownLocaLocation;
    private RandomAccessFile randomAccessFile;
    private URL url;
    private IDownLoadProgress iDownLoadProgress;

    public CustomDownRunnable(URL url, String threadID, long startDownLoad, long endDownLoad, File file, IDownLoadProgress progress) {
        this.url = url;
        this.threadID = threadID;
        this.startDownLoadLocation = startDownLoad;
        this.endDownLocaLocation = endDownLoad;
        try {
            this.randomAccessFile = new RandomAccessFile(file, "rwd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.iDownLoadProgress = progress;
    }


    @Override
    public void run() {
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Range", "bytes=" + startDownLoadLocation + "-" + endDownLocaLocation);
            inputStream = httpURLConnection.getInputStream();
            byte[] buffer = new byte[1024];
            bufferedInputStream = new BufferedInputStream(inputStream);
            randomAccessFile.seek(startDownLoadLocation); //设置文件开头的偏移指针
            int len;
            while ((len = bufferedInputStream.read(buffer, 0, 1024)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                iDownLoadProgress.onDownLoad(threadID, len); //实现接口回调通知进度
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
//                randomAccessFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
//                inputStream.close();
//                bufferedInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
