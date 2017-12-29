package com.xialan.app.utils;


import android.content.Context;

import com.xialan.app.MainActivity;

/**
 * 网络请求路径
 * Created by root on 2016/11/11.
 *
 * @author mcl
 */
public class HttpUrl {


    /**
     * http://192.168.1.148:80/
     * http://beauty.dawoonad.com/
     * 设置get请求体 和url
     *
     * @param paramas get请求拼接
     * @return
     */
    public static String setGetUrl(String paramas) {
        return getRootUrl() + paramas;
    }

    private static Boolean isDebug = true;

    /**
     * 返回baseurl
     * @return 返回baseurl
     */
    public static String baseUrl() {
        return getRootUrl();
    }

    private static String getRootUrl() {
        if (isDebug) {
            return "http://www.gou-mei.com/";
        } else {
            return "http://beauty.dawoonad.com/";
        }
    }


}
