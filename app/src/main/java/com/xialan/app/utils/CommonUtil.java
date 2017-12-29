package com.xialan.app.utils;

import android.app.backup.BackupDataInput;
import android.util.Log;

import com.xialan.app.bean.ProductDetailBean;
import com.xialan.app.utils.StringUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */
public class CommonUtil {
    private static String[] split;

    /**
     * 处理数据 返回图片 数组
     *
     * @param data
     */
    public static List<ProductDetailBean> parseData(String data) {
        List<ProductDetailBean> mlist=new ArrayList<>();
        split = StringUtil.split(data, '@');
        for (int i=0;i<split.length;i++){
            String[] dataForspild = getDataForspild(split[i]);
            ProductDetailBean productDetailBean = new ProductDetailBean(dataForspild[0], dataForspild[1], dataForspild[2], dataForspild[3]);
            mlist.add(productDetailBean);
        }
//        ArrayList<String> strings = new ArrayList<>();
//        for (int i = 0; i < split.length; i++) {
//            String[] dataForspild = getDataForspild(split[i]);
//            strings.add(dataForspild[2]);
//        }
//        String[] strings1 = (String[])strings.toArray(new String[strings.size()]);
        return mlist;
    }

    /**
     * 切割字符串 获取发型前后对比的图片
     *
     * @param data  @
     * @return
     */
    public static String[] getBAdata(String data) {
        String[] baData = StringUtil.split(data, '@');
        return baData;
    }

    public static String[] getSkupSampledata(String data) {
        String[] skupData = StringUtil.split(data, '@');
        return skupData;
    }

    /**
     * 解析教育视频数据
     *
     * @param data
     * @return
     */
    public static String[] getTrainingVideoData(String data) {
        String[] trainVideoData = StringUtil.split(data, '@');
        return trainVideoData;
    }

    public static String[] getDataForVideo(String s) {
        String[] transvideo = StringUtil.split(s, '#');
        return transvideo;
    }

    /**
     * 数据分割  #
     */
    public static String[] getDataForspild(String s) {
        String[] spo = null;
        try {
            spo = StringUtil.split(s, '#');
        } catch (Exception e) {
            return null;
        }
        return spo;
    }


    /**
     * 数据分割 /
     */
    public static String[] getDataForspild_2(String s) {
        String[] spo = null;
        try {
            spo = StringUtil.split(s, '/');
        } catch (Exception e) {
            return null;
        }
        return spo;
    }




}
