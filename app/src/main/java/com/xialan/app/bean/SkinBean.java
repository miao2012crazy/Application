package com.xialan.app.bean;

/**
 * Created by Administrator on 2017/8/7.
 */
public class SkinBean {
    private String imagedrawage;
    private String title;
    private String detailContent;
    private boolean is_select;

    public SkinBean(String imagedrawage, String title, String detailContent, boolean is_select) {
        this.imagedrawage = imagedrawage;
        this.title = title;
        this.detailContent = detailContent;
        this.is_select = is_select;
    }

    public String getImagedrawage() {
        return imagedrawage;
    }

    public void setImagedrawage(String imagedrawage) {
        this.imagedrawage = imagedrawage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }
}
