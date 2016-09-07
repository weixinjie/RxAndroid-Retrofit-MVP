package com.will.custom_rxandroid.http;

import com.will.custom_rxandroid.pojo.ZhuangBiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by will on 16/9/6.
 */

public interface ZhuangBiApi {
    @GET("search")
    Observable<List<ZhuangBiImage>> search(@Query("q") String query);
}
