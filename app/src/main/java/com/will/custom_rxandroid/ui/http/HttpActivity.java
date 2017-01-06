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

    /**
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----null-----value-----HTTP/1.1 200 OK
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Cache-Control-----value-----no-cache
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Connection-----value-----keep-alive
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Content-Type-----value-----text/html;charset=utf-8
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Date-----value-----Fri, 06 Jan 2017 13:13:37 GMT
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----P3p-----value-----CP=" OTI DSP COR IVA OUR IND COM "
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Server-----value-----bfe/1.0.8.18
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Set-Cookie-----value-----BAIDUID=24591E936C958886A4842EE3ABBDE048:FG=1; max-age=31536000; expires=Sat, 06-Jan-18 13:13:37 GMT; domain=.baidu.com; path=/; version=1
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Set-Cookie-----value-----H_WISE_SIDS=102065_100038_111887_100102_111910_109814_111142_114134_107800_114001_113932_110498_113461_114208_113888_112107_111463_109588_107312_112175_112134_114131_112037_114275_110085; path=/; domain=.baidu.com
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Set-Cookie-----value-----BDSVRTM=18; path=/
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Strict-Transport-Security-----value-----max-age=172800
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----Traceid-----value-----1483708417061753319417547699709746490165
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----X-Android-Received-Millis-----value-----1483740765308
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----X-Android-Response-Source-----value-----NETWORK 200
     * 01-07 06:12:45.301 17534-18787/com.will.custom_rxandroid E/----: 测试response的响应头 key-----X-Android-Sent-Millis-----value-----1483740765241
     */

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
