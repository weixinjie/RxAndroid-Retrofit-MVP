package com.will.custom_rxandroid.http;

import com.will.custom_rxandroid.pojo.token.TokenBean;
import com.will.custom_rxandroid.pojo.token.TokenDataBean;

import java.util.Date;
import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by will on 16/9/9.
 * 模拟API接口,获取token与授权
 */

public class VirtualApi {
    Random random = new Random();

    /**
     * 获取从网络上得到token
     *
     * @param user_name
     * @return
     */
    Observable<TokenBean> get_token(String user_name) {
        return Observable.just(user_name).map(new Func1<String, TokenBean>() {
            @Override
            public TokenBean call(String s) {
                int time_cost = random.nextInt(500) + 500; //模拟从网络上获取数据的时间
                try {
                    Thread.sleep(time_cost); //模拟网络耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TokenBean tokenBean = new TokenBean();
                tokenBean.setCreate_utc(new Date(System.currentTimeMillis()).toString());
                tokenBean.setToken(create_token(s));
                return tokenBean;
            }
        });
    }

    /**
     * 模拟生产token
     */
    private String create_token(String user_name) {
        return user_name + System.currentTimeMillis() % 10000;
    }


    /**
     * 模拟得到用户数据
     *
     * @param token
     * @return
     */
    Observable<TokenDataBean> get_user_data(TokenBean token) {
        return Observable.just(token).map(new Func1<TokenBean, TokenDataBean>() {
            @Override
            public TokenDataBean call(TokenBean tokenBean) {
                int time_cost = random.nextInt(500) + 500; //模拟从网络上获取数据的时间
                try {
                    Thread.sleep(time_cost); //模拟网络耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TokenDataBean tokenDataBean = new TokenDataBean();
                tokenDataBean.setToken(tokenBean.getToken());
                tokenDataBean.setCreate_utc(tokenBean.getCreate_utc());
                tokenDataBean.setParent("parent-" + time_cost);
                tokenDataBean.setUser_id(System.currentTimeMillis() % 10000);
                return tokenDataBean;
            }
        });
    }
}
