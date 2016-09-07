package com.will.custom_rxandroid.http;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by will on 16/9/6.
 */

public class BaseApi {
    public static String ZHUANGBI_API = "http://zhuangbi.info/";

    private static ZhuangBiApi zhuangBiApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory convert_factory = GsonConverterFactory.create();
    private static CallAdapter.Factory calladapter_factory = RxJavaCallAdapterFactory.create();

    /**
     * 获取ZhuangBiApi的实例
     *
     * @return
     */
    public static ZhuangBiApi getZhuangBiApi() {
        if (zhuangBiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(convert_factory)
                    .addCallAdapterFactory(calladapter_factory)
                    .baseUrl(ZHUANGBI_API).build();
            zhuangBiApi = retrofit.create(ZhuangBiApi.class);
        }
        return zhuangBiApi;
    }
}
