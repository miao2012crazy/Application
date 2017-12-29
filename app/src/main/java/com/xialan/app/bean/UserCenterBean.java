package com.xialan.app.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/10/25.
 */

public class UserCenterBean {
    private Drawable image_icon;
    private String title;

    public UserCenterBean(Drawable image_icon, String title) {
        this.image_icon = image_icon;
        this.title = title;
    }

    public UserCenterBean() {
    }

    public Drawable getImage_icon() {
        return image_icon;
    }

    public void setImage_icon(Drawable image_icon) {
        this.image_icon = image_icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "UserCenterBean{" +
                "image_icon=" + image_icon +
                ", title='" + title + '\'' +
                '}';
    }
}
