package com.xialan.app.utils;


/**
 * Created by Administrator on 2017/8/1.
 */
public class ParseDataUtil {
    /**
     * 视频解析
     * @param data
     * @return
     */
    public static String[] parse(String data) {
        String[] split = StringUtil.split(data, '/');
        return split;
    }
}
