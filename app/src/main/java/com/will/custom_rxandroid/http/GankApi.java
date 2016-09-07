package com.will.custom_rxandroid.http;

import com.will.custom_rxandroid.pojo.map.GankResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * 干货集中营API
 */

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankResult> get_gank(@Path("number") int per_number, @Path("page") int page);
}
