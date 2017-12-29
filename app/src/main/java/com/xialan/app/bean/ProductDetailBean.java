package com.xialan.app.bean;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ProductDetailBean {
    private String title;
    private String product_id;
    private String product_image_url;
    private String video_url;

    public ProductDetailBean(String title, String product_id, String product_image_url, String video_url) {
        this.title = title;
        this.product_id = product_id;
        this.product_image_url = product_image_url;
        this.video_url = video_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
