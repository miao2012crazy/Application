package com.xialan.app.bean;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HairStyleBABean {
    private boolean isSelect;
    private String title;
    private String image;

    public HairStyleBABean(String title, String image,boolean isSelect) {
        this.title = title;
        this.image = image;
        this.isSelect=isSelect;
    }

    public HairStyleBABean() {
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
