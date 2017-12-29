package com.xialan.app.bean;

/** 首页广告
 * Created by Administrator on 2017/9/5.
 */
public class NoticeBean {
    private String title;
    private String link;

    public NoticeBean(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
