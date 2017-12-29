package com.xialan.app.contract;

import com.xialan.app.base.BaseView;

/**
 * Created by Administrator on 2017/10/12.
 */

public interface VideoContract {
    interface Model {
        /**
         * 获取视频信息
         */
        void getVideoDataFromNet();


    }

    interface View extends BaseView {
        /**
         * 获取视频数据成功
         */
        void OnVideoDataSuccessed(String[] video_data);

        /**
         * 获取视频数据失败
         */
        void OnVideoDataFailed();

        /**
         * 开始播放视频
         * 根据当前位置指针播放
         */
        void startVideo(int position);

        /**
         * 暂停当前播放视频
         */
        void pauseVideo();

        /**
         * 播放完成
         */
        void playVideoCompleted();

        /**
         * 播放失败
         */
        void playVideoError();
    }

    interface Presenter {
        /**
         * 获取视频成功 返回string[]
         * @param video_data
         */
        void videoDataSuccessed(String[] video_data);

        /**
         * 获取视频数据失败
         */
        void videoDataFailed();

        /**
         * 获取视频数据
         */
        void getVideodata();
    }
}
