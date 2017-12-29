package com.xialan.app.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HairStyleBean {
    private boolean isSelect;
    private Drawable image;

    public HairStyleBean(Drawable image, boolean isSelect) {
        this.image = image;
        this.isSelect=isSelect;
    }

    public HairStyleBean() {
    }


    public Drawable getImage() {
        return image;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
