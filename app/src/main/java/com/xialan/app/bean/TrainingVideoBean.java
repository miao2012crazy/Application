package com.xialan.app.bean;

/**
 * Created by Administrator on 2017/8/1.
 * ID # 分类名 # 题目 # 来源 # 说明 # 图片文件名 # 视频文件名 # 视频时长（秒） # 日期 @
 */
public class TrainingVideoBean {
    //商品id
    private String id;
    //分类id
    private String category_id;
    //标题
    private String title;
    //来源
    private String soruce;
    //说明
    private String content_detail;
    //图片文件名称
    private String iamge_name;
    //视频文件名称
    private String video_name;
    //视频时长
    private String video_time;
    //日期
    private String date;
    //是否已选中  0:  未选中 1: 选中
    private  String isSelected;

    public TrainingVideoBean(String id, String category_id, String title, String soruce, String content_detail, String iamge_name, String video_name, String video_time, String date,String isSelected) {
        this.id = id;
        this.category_id = category_id;
        this.title = title;
        this.soruce = soruce;
        this.content_detail = content_detail;
        this.iamge_name = iamge_name;
        this.video_name = video_name;
        this.video_time = video_time;
        this.date = date;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSoruce() {
        return soruce;
    }

    public void setSoruce(String soruce) {
        this.soruce = soruce;
    }

    public String getContent_detail() {
        return content_detail;
    }

    public void setContent_detail(String content_detail) {
        this.content_detail = content_detail;
    }

    public String getIamge_name() {
        return iamge_name;
    }

    public void setIamge_name(String iamge_name) {
        this.iamge_name = iamge_name;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
