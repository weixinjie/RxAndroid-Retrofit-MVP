package com.will.custom_rxandroid.pojo.token;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by will on 16/9/9.
 */

public class TokenBean implements Parcelable {
    private String token;
    private String create_utc;


    public TokenBean(String token, String create_utc) {
        this.token = token;
        this.create_utc = create_utc;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreate_utc() {
        return this.create_utc;
    }

    public void setCreate_utc(String create_utc) {
        this.create_utc = create_utc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.create_utc);
    }

    public TokenBean() {
    }

    protected TokenBean(Parcel in) {
        this.token = in.readString();
        this.create_utc = in.readString();
    }

    public static final Parcelable.Creator<TokenBean> CREATOR = new Parcelable.Creator<TokenBean>() {
        @Override
        public TokenBean createFromParcel(Parcel source) {
            return new TokenBean(source);
        }

        @Override
        public TokenBean[] newArray(int size) {
            return new TokenBean[size];
        }
    };
}
