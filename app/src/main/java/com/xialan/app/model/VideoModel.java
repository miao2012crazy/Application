package com.xialan.app.model;

import com.xialan.app.contract.VideoContract;
import com.xialan.app.retrofit.RetrofitUtil;
import com.xialan.app.utils.ParseDataUtil;
import com.xialan.app.utils.SharePreUtils;
import com.xialan.app.utils.UIUtils;

/**
 * Created by Administrator on 2017/10/12.
 */

public class VideoModel implements VideoContract.Model {
    private VideoContract.Presenter mPresenter;

    public VideoModel(VideoContract.Presenter presenter) {
        this.mPresenter=presenter;
    }


    @Override
    public void getVideoDataFromNet() {
        RetrofitUtil.setCallBack(RetrofitUtil.getApiservice().getVideo(), new RetrofitUtil.HttpCallBack() {
            @Override
            public void onstart() {

            }

            @Override
            public void onSusscess(String data) {
                SharePreUtils.put(UIUtils.getContext(),"video_data",data);
                String[] parse = ParseDataUtil.parse(data);
                mPresenter.videoDataSuccessed(parse);
            }

            @Override
            public void onError(String meg) {
                UIUtils.showToast(UIUtils.getContext(),"网络连接失败");
                String video_data = (String) SharePreUtils.get(UIUtils.getContext(), "video_data", "");
                if (video_data==""){
                    return;
                }
                String[] parse = ParseDataUtil.parse(video_data);
                mPresenter.videoDataSuccessed(parse);
            }
            @Override
            public void onCompleted() {

            }
        });
    }
}
