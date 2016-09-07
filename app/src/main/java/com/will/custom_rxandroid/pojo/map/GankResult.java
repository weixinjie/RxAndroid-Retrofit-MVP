package com.will.custom_rxandroid.pojo.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by will on 16/9/7.
 */

public class GankResult {
    public boolean error;
    public
    @SerializedName("results")
    List<GankBean> beauties;
}
