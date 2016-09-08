package com.will.custom_rxandroid.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by will on 16/9/6.
 */

public class BaseApi {
    public static String GANK_API = "http://gank.io/api/";
    public static String ZHUANGBI_API = "http://zhuangbi.info/";

    private static ZhuangBiApi zhuangBiApi;
    private static GankApi gankApi;
    private static Converter.Factory convert_factory = GsonConverterFactory.create();
    private static CallAdapter.Factory calladapter_factory = RxJavaCallAdapterFactory.create();

    /**
     * get instance of ZhuangBiApi
     *
     * @return
     */
    public static ZhuangBiApi getZhuangBiApi() {
        if (zhuangBiApi == null) {
            HttpLoggingInterceptor inteceptor = new HttpLoggingInterceptor();
            inteceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(inteceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(convert_factory)
                    .addCallAdapterFactory(calladapter_factory)
                    .baseUrl(ZHUANGBI_API).build();
            zhuangBiApi = retrofit.create(ZhuangBiApi.class);
        }
        return zhuangBiApi;
    }

    /**
     * get instance of GankApi
     *
     * @return
     */
    public static GankApi getGankApi() {
        if (gankApi == null) {
            HttpLoggingInterceptor inteceptor = new HttpLoggingInterceptor();
            inteceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(inteceptor).build();
            Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                    .addConverterFactory(convert_factory)
                    .addCallAdapterFactory(calladapter_factory)
                    .baseUrl(GANK_API).build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }
}
