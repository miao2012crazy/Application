package com.xialan.app.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/3.
 */
public class HeadStyleImageBean {
    private Drawable imageid;

    public HeadStyleImageBean(Drawable imageid) {
        this.imageid = imageid;
    }

    public HeadStyleImageBean() {
    }

    public Drawable getImageid() {
        return imageid;
    }

    public void setImageid(Drawable imageid) {
        this.imageid = imageid;
    }
}
