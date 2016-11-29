package com.will.custom_rxandroid.ui.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.utils.LogUtils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static android.R.attr.name;

public class HttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        Executors.newSingleThreadExecutor().submit(new MCallable());

    }

    class MCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            testHttpRequestHeader("https://www.baidu.com");
            return null;
        }
    }

    String name;

    private void testHttpRequestHeader(String path) {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Map<String, List<String>> response_headers = httpURLConnection.getHeaderFields();
            Iterator<String> header_iterator = response_headers.keySet().iterator();
            while (header_iterator.hasNext()) {
                name = header_iterator.next();
                List<String> values = response_headers.get(name);
                Iterator<String> values_iterator = values.iterator();
                while (values_iterator.hasNext()) {
                    String data = values_iterator.next();
                    LogUtils.e("测试response的响应头 key-----" + name + "-----value-----" + data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
