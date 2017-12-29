package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.VideoContract;
import com.xialan.app.model.VideoModel;

/**
 * Created by Administrator on 2017/10/12.
 */

public class VideoPresenter extends BasePresenter implements VideoContract.Presenter {
    private VideoContract.View mView;
    private final VideoModel mVideoModel;

    public VideoPresenter(VideoContract.View view) {
        this.mView=view;
        mVideoModel = new VideoModel(this);
    }

    @Override
    public void videoDataSuccessed(String[] video_data) {
        mView.OnVideoDataSuccessed(video_data);
    }

    @Override
    public void videoDataFailed() {
        mView.OnVideoDataFailed();
    }

    @Override
    public void getVideodata() {
        mVideoModel.getVideoDataFromNet();
    }
}
