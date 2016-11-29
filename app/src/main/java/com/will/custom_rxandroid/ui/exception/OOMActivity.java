package com.will.custom_rxandroid.ui.exception;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.will.custom_rxandroid.R;

import java.util.ArrayList;

public class OOMActivity extends AppCompatActivity {
    public static ArrayList<Bitmap> mBitmaps = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oom);
//        startAsyncTask();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            mBitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }

    }

    void startAsyncTask() {
        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(20000);
                return null;
            }
        }.execute();
    }
}
