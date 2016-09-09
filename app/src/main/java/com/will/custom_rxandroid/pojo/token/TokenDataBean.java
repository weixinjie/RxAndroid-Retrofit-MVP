package com.will.custom_rxandroid.pojo.token;

/**
 * Created by will on 16/9/9.
 */

public class TokenDataBean {
    double user_id;
    String parent;
    String token;
    String create_utc;

    public double getUser_id() {
        return this.user_id;
    }

    public void setUser_id(double user_id) {
        this.user_id = user_id;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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
}
