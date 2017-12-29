package com.xialan.app.listener;

/** 是否展示进度条接口 需要展示进度条的直接实现此接口
  * Created by Administrator on 2017/10/8.
 */
public interface OnProgressListener {
    /**
     * 展示进度条
     */
    void showProgressBar();
    /**
     *  关闭进度条
     */
    void hideProgressBar();
}
