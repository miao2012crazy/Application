package com.xialan.app.bean;

/**
 * Created by Administrator on 2017/7/31.
 */
public class SkinKeepingBean {
    private   String title;
    private  String iv_keep_lv;
    private boolean is_Select;
    public SkinKeepingBean(String title, String iv_keep_lv, boolean is_Select) {
        this.title = title;
        this.iv_keep_lv = iv_keep_lv;
        this.is_Select=is_Select;
    }

    public boolean is_Select() {
        return is_Select;
    }

    public void setIs_Select(boolean is_Select) {
        this.is_Select = is_Select;
    }

    public SkinKeepingBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIv_keep_lv() {
        return iv_keep_lv;
    }

    public void setIv_keep_lv(String iv_keep_lv) {
        this.iv_keep_lv = iv_keep_lv;
    }
}
